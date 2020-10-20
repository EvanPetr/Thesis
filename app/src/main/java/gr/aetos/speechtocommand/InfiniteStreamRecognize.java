package gr.aetos.speechtocommand;

import android.os.AsyncTask;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionMetadata;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechContext;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class InfiniteStreamRecognize extends AsyncTask<InfiniteStreamRecognizeArguments, Void, Void> {

    private static final int RECORDER_SAMPLE_RATE = 16000;
    private static final String LANGUAGE_CODE = "el-GR";
    private static final String MODEL = "command_and_search";

    private static final int STREAMING_LIMIT = 290000; // ~5 minutes

    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";

    private static int restartCounter = 0;
    private static ArrayList<ByteString> audioInput = new ArrayList<>();
    private static ArrayList<ByteString> lastAudioInput = new ArrayList<>();
    private static int resultEndTimeInMS = 0;
    private static int isFinalEndTime = 0;
    private static int finalRequestEndTime = 0;
    private static boolean newStream = true;
    private static double bridgingOffset = 0;
    private static boolean lastTranscriptWasFinal = false;
    private static StreamController referenceToStreamController;
    private static SpeechSettings speechSettings;

    // Creating shared object
    private static volatile BlockingQueue<byte[]> sharedQueue = new LinkedBlockingQueue<>();
    private static volatile List<TranscriptMatcher> transcriptMatcherList = new ArrayList<>();
    private static List<String> phrases = new ArrayList<>();
    private static int[] phrasesPreviousCurrentSizes = {0, 0};


    private static void setup(InputStream is) throws IOException {
        MicrophoneRecorder.blockingQueues.add(sharedQueue);
        GoogleCredentials credentials = GoogleCredentials.fromStream(is);
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);

        speechSettings = SpeechSettings.newBuilder()
                .setCredentialsProvider(credentialsProvider)
                .build();
        googleSTTSettings();

    }

    private static StreamingRecognitionConfig googleSTTSettings(){
        phrasesPreviousCurrentSizes = new int[]{phrases.size(), phrases.size()};
        SpeechContext sc = SpeechContext.newBuilder().addAllPhrases(phrases).build();

        RecognitionMetadata rm = RecognitionMetadata.newBuilder()
                .setInteractionType(RecognitionMetadata.InteractionType.VOICE_COMMAND)
                .setMicrophoneDistance(RecognitionMetadata.MicrophoneDistance.NEARFIELD)
                .setOriginalMediaType(RecognitionMetadata.OriginalMediaType.AUDIO)
                .setRecordingDeviceType(RecognitionMetadata.RecordingDeviceType.SMARTPHONE)
                .build();

        RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(RECORDER_SAMPLE_RATE)
                .setLanguageCode(LANGUAGE_CODE)
                .setModel(MODEL)
                .addSpeechContexts(sc)
                .setMetadata(rm)
                .build();

        return StreamingRecognitionConfig.newBuilder()
                .setConfig(recognitionConfig)
                .setInterimResults(true)
//                .setSingleUtterance(true)
                .build();
    }

    public static void addTranscriptMatcher(TranscriptMatcher transcriptMatcher){
        transcriptMatcherList.add(transcriptMatcher);
        for(Command command: transcriptMatcher.commands){
            phrases.addAll(command.getPhrases());
        }
        phrasesPreviousCurrentSizes = new int[]{phrasesPreviousCurrentSizes[1], phrases.size()};
    }

    public static void removeTranscriptMatcher(TranscriptMatcher transcriptMatcher){
        transcriptMatcherList.remove(transcriptMatcher);
        for(Command command: transcriptMatcher.commands){
            phrases.removeAll(command.getPhrases());
        }
        phrasesPreviousCurrentSizes = new int[]{phrasesPreviousCurrentSizes[1], phrases.size()};
    }

    public static void main(InputStream is) {
        try {
            setup(is);
            infiniteStreamingRecognize();
        } catch (Exception e) {
            System.out.println("Exception caught: " + e);
        }
    }

    public static String convertMillisToDate(double milliSeconds) {
        long millis = (long) milliSeconds;
        DecimalFormat format = new DecimalFormat();
        format.setMinimumIntegerDigits(2);
        return String.format(
                "%s:%s /",
                format.format(TimeUnit.MILLISECONDS.toMinutes(millis)),
                format.format(
                        TimeUnit.MILLISECONDS.toSeconds(millis)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
    }

    /** Performs infinite streaming speech recognition */
    public static void infiniteStreamingRecognize() throws Exception {
        ResponseObserver<StreamingRecognizeResponse> responseObserver;

        try (SpeechClient client = SpeechClient.create(speechSettings)) {
            ClientStream<StreamingRecognizeRequest> clientStream;
            responseObserver = new ResponseObserver<StreamingRecognizeResponse>() {
                ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

                public void onStart(StreamController controller) {
                    referenceToStreamController = controller;
                }

                public void onResponse(StreamingRecognizeResponse response) {
                    responses.add(response);
                    StreamingRecognitionResult result = response.getResultsList().get(0);
                    Duration resultEndTime = result.getResultEndTime();
                    resultEndTimeInMS =
                            (int)
                                    ((resultEndTime.getSeconds() * 1000) + (resultEndTime.getNanos() / 1000000));
                    double correctedTime =
                            resultEndTimeInMS - bridgingOffset + (STREAMING_LIMIT * restartCounter);

                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    if (result.getIsFinal()) {
                        for(TranscriptMatcher transcriptMatcher : transcriptMatcherList){
                            transcriptMatcher.match(alternative.getTranscript().trim());
                        }
                        System.out.print(GREEN);
                        System.out.print("\033[2K\r");
                        System.out.printf(
                                "%s: %s [confidence: %.2f]\n",
                                convertMillisToDate(correctedTime),
                                alternative.getTranscript(),
                                alternative.getConfidence());
                        isFinalEndTime = resultEndTimeInMS;
                        lastTranscriptWasFinal = true;
                    } else {
                        System.out.print(RED);
                        System.out.print("\033[2K\r");
                        System.out.printf(
                                "%s: %s", convertMillisToDate(correctedTime), alternative.getTranscript());
                        lastTranscriptWasFinal = false;
                    }
                }

                public void onComplete() {
                    System.out.println("COMPLETEEEEEE");
                }

                public void onError(Throwable t) {
                    System.out.println(t.getMessage());
                }
            };
            clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);

            StreamingRecognitionConfig streamingRecognitionConfig = googleSTTSettings();
            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                            .setStreamingConfig(streamingRecognitionConfig)
                            .build(); // The first request in a streaming call has to be a config

            clientStream.send(request);

            try {
                long startTime = System.currentTimeMillis();

                while (true) {

                    long estimatedTime = System.currentTimeMillis() - startTime;

                    if (estimatedTime >= STREAMING_LIMIT || (phrasesPreviousCurrentSizes[0] != phrasesPreviousCurrentSizes[1])) {

                        clientStream.closeSend();
                        referenceToStreamController.cancel(); // remove Observer

                        if (resultEndTimeInMS > 0) {
                            finalRequestEndTime = isFinalEndTime;
                        }
                        resultEndTimeInMS = 0;

                        lastAudioInput = null;
                        lastAudioInput = audioInput;
                        audioInput = new ArrayList<>();

                        restartCounter++;

                        if (!lastTranscriptWasFinal) {
                            System.out.print('\n');
                        }

                        newStream = phrasesPreviousCurrentSizes[0] == phrasesPreviousCurrentSizes[1];

                        clientStream = client.streamingRecognizeCallable().splitCall(responseObserver);
                        streamingRecognitionConfig = googleSTTSettings();
                        request =
                                StreamingRecognizeRequest.newBuilder()
                                        .setStreamingConfig(streamingRecognitionConfig)
                                        .build();

                        System.out.println(YELLOW);
                        System.out.printf("%d: RESTARTING REQUEST\n", restartCounter * STREAMING_LIMIT);

                        startTime = System.currentTimeMillis();

                    } else {

                        if ((newStream) && (lastAudioInput.size() > 0)) {
                            // if this is the first audio from a new request
                            // calculate amount of unfinalized audio from last request
                            // resend the audio to the speech client before incoming audio
                            double chunkTime = STREAMING_LIMIT / lastAudioInput.size();
                            // ms length of each chunk in previous request audio arrayList
                            if (chunkTime != 0) {
                                if (bridgingOffset < 0) {
                                    // bridging Offset accounts for time of resent audio
                                    // calculated from last request
                                    bridgingOffset = 0;
                                }
                                if (bridgingOffset > finalRequestEndTime) {
                                    bridgingOffset = finalRequestEndTime;
                                }
                                int chunksFromMS =
                                        (int) Math.floor((finalRequestEndTime - bridgingOffset) / chunkTime);
                                // chunks from MS is number of chunks to resend
                                bridgingOffset =
                                        (int) Math.floor((lastAudioInput.size() - chunksFromMS) * chunkTime);
                                // set bridging offset for next request
                                for (int i = chunksFromMS; i < lastAudioInput.size(); i++) {
                                    request =
                                            StreamingRecognizeRequest.newBuilder()
                                                    .setAudioContent(lastAudioInput.get(i))
                                                    .build();
                                    clientStream.send(request);
                                }
                            }
                            newStream = false;
                        }

                        ByteString tempByteString = ByteString.copyFrom(sharedQueue.take());

                        request =
                                StreamingRecognizeRequest.newBuilder().setAudioContent(tempByteString).build();

                        audioInput.add(tempByteString);
                    }

                    clientStream.send(request);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected Void doInBackground(InfiniteStreamRecognizeArguments... infiniteStreamRecognizeArguments) {
        try {
            setup(infiniteStreamRecognizeArguments[0].inputStream);
            infiniteStreamingRecognize();
        } catch (Exception e) {
            System.out.println("Exception caught: " + e);
        }
        return null;
    }
}