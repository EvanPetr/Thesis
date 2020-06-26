package gr.aetos.conapi;

import android.util.Log;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class QuickstartSample {
    String transcript = "ERROR";

    public String getTranscript(){
        return transcript;
    }

    /** Demonstrates using the Speech API to transcribe an audio file. */
    void main(InputStream credentials_stream, InputStream audio_strea, String fileName) throws Exception {
//        Credentials credentials = ServiceAccountCredentials.fromStream(credentials_stream);
//        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
//        TransportChannelProvider transportChannelProvider = SpeechSettings.defaultGrpcTransportProviderBuilder().setCredentials(credentials).build();
//
////        FixedTransportChannelProvider.create(transportChannelProvider);
////        ChannelProvider channelProvider = SpeechSettings.defaultChannelProviderBuilder().setCredentialsProvider(credentialsProvider).build();
////        TransportChannelProvider transportChannelProvider = SpeechSettings.defaultTransportChannelProvider().withCredentials(credentials);
////        SpeechSettings speechSettings = SpeechSettings.newBuilder().setTransportChannelProvider(transportChannelProvider).setCredentialsProvider(credentialsProvider).build();
//
//        SpeechSettings speechSettings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).setTransportChannelProvider(SpeechSettings.defaultGrpcTransportProviderBuilder().setCredentials(credentials).build()).build();

//
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentials_stream);
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);

        SpeechSettings speechSettings =
                SpeechSettings.newBuilder()
                        .setCredentialsProvider(credentialsProvider)
                        .build();
        try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {

            // The path to the audio file to transcribe
//            String fileName = "../../../../res/raw/audio.raw";

            // Reads the audio file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
//            byte[] data = new byte[audio_stream.available()];
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("el-GR")
                            .setModel("command_and_search")
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcript = alternative.getTranscript();
                Log.i("Transcription:", transcript);
            }
        }
    }
}
