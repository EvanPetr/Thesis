package gr.aetos.conapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

//public class MainActivity extends AppCompatActivity {
//    private static final int RECORDER_SAMPLERATE = 16000;
//    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
//    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int RECORDER_BPP = 16;
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Get the minimum buffer size required for the successful creation of an AudioRecord object.
//        int bufferSizeInBytes = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
//                RECORDER_CHANNELS,
//                RECORDER_AUDIO_ENCODING
//        );
//        // Initialize Audio Recorder.
//        AudioRecord audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                RECORDER_SAMPLERATE,
//                RECORDER_CHANNELS,
//                RECORDER_AUDIO_ENCODING,
//                bufferSizeInBytes
//        );
//        // Start Recording.
//        audioRecorder.startRecording();
//
//        int numberOfReadBytes = 0;
//        byte[] audioBuffer = new byte[bufferSizeInBytes];
//        boolean recording = false;
//        float[] tempFloatBuffer = new float[3];
//        int tempIndex = 0;
//        int totalReadBytes = 0;
//        byte[] totalByteBuffer = new byte[60 * 44100 * 2];
//
//
//        // While data come from microphone.
//        while (true) {
//            float totalAbsValue = 0.0f;
//            short sample = 0;
//
//            numberOfReadBytes = audioRecorder.read(audioBuffer, 0, bufferSizeInBytes);
//
//            // Analyze Sound.
//            for (int i = 0; i < bufferSizeInBytes; i += 2) {
//                sample = (short) ((audioBuffer[i]) | audioBuffer[i + 1] << 8);
//                totalAbsValue += Math.abs(sample) / (numberOfReadBytes / 2);
//            }
//
//            // Analyze temp buffer.
//            tempFloatBuffer[tempIndex % 3] = totalAbsValue;
//            float temp = 0.0f;
//            for (int i = 0; i < 3; ++i)
//                temp += tempFloatBuffer[i];
//
//            if ((temp >= 0 && temp <= 350) && !recording) {
//                Log.i("TAG", "1");
//                tempIndex++;
//                continue;
//            }
//
//            if (temp > 350 && !recording) {
//                Log.i("TAG", "2");
//                recording = true;
//            }
//
//            if ((temp >= 0 && temp <= 350) && recording) {
//                Log.i("TAG", "Save audio to file.");
//
//                // Save audio to file.
//                String filepath = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath();
////                String filepath = Environment.getExternalStorageDirectory().getPath();
////                File file = new File(filepath, "AudioRecorder");
////                if (!file.exists())
////                    file.mkdirs();
//
//                String fn = filepath + "/" + System.currentTimeMillis() + ".wav";
//
//                long totalAudioLen = 0;
//                long totalDataLen = totalAudioLen + 36;
//                long longSampleRate = RECORDER_SAMPLERATE;
//                int channels = 1;
//                long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;
//                totalAudioLen = totalReadBytes;
//                totalDataLen = totalAudioLen + 36;
//                byte[] finalBuffer = new byte[totalReadBytes + 44];
//
//                finalBuffer[0] = 'R';  // RIFF/WAVE header
//                finalBuffer[1] = 'I';
//                finalBuffer[2] = 'F';
//                finalBuffer[3] = 'F';
//                finalBuffer[4] = (byte) (totalDataLen & 0xff);
//                finalBuffer[5] = (byte) ((totalDataLen >> 8) & 0xff);
//                finalBuffer[6] = (byte) ((totalDataLen >> 16) & 0xff);
//                finalBuffer[7] = (byte) ((totalDataLen >> 24) & 0xff);
//                finalBuffer[8] = 'W';
//                finalBuffer[9] = 'A';
//                finalBuffer[10] = 'V';
//                finalBuffer[11] = 'E';
//                finalBuffer[12] = 'f';  // 'fmt ' chunk
//                finalBuffer[13] = 'm';
//                finalBuffer[14] = 't';
//                finalBuffer[15] = ' ';
//                finalBuffer[16] = 16;  // 4 bytes: size of 'fmt ' chunk
//                finalBuffer[17] = 0;
//                finalBuffer[18] = 0;
//                finalBuffer[19] = 0;
//                finalBuffer[20] = 1;  // format = 1
//                finalBuffer[21] = 0;
//                finalBuffer[22] = (byte) channels;
//                finalBuffer[23] = 0;
//                finalBuffer[24] = (byte) (longSampleRate & 0xff);
//                finalBuffer[25] = (byte) ((longSampleRate >> 8) & 0xff);
//                finalBuffer[26] = (byte) ((longSampleRate >> 16) & 0xff);
//                finalBuffer[27] = (byte) ((longSampleRate >> 24) & 0xff);
//                finalBuffer[28] = (byte) (byteRate & 0xff);
//                finalBuffer[29] = (byte) ((byteRate >> 8) & 0xff);
//                finalBuffer[30] = (byte) ((byteRate >> 16) & 0xff);
//                finalBuffer[31] = (byte) ((byteRate >> 24) & 0xff);
//                finalBuffer[32] = (byte) (2 * 16 / 8);  // block align
//                finalBuffer[33] = 0;
//                finalBuffer[34] = RECORDER_BPP;  // bits per sample
//                finalBuffer[35] = 0;
//                finalBuffer[36] = 'd';
//                finalBuffer[37] = 'a';
//                finalBuffer[38] = 't';
//                finalBuffer[39] = 'a';
//                finalBuffer[40] = (byte) (totalAudioLen & 0xff);
//                finalBuffer[41] = (byte) ((totalAudioLen >> 8) & 0xff);
//                finalBuffer[42] = (byte) ((totalAudioLen >> 16) & 0xff);
//                finalBuffer[43] = (byte) ((totalAudioLen >> 24) & 0xff);
//
//                for (int i = 0; i < totalReadBytes; ++i)
//                    finalBuffer[44 + i] = totalByteBuffer[i];
//
//                FileOutputStream out;
//                try {
//                    out = new FileOutputStream(fn);
//                    try {
//                        out.write(finalBuffer);
//                        out.close();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                } catch (FileNotFoundException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//
//                //*/
//                tempIndex++;
//                break;
//            }
//
//            // -> Recording sound here.
//            Log.i("TAG", "Recording Sound.");
//            for (int i = 0; i < numberOfReadBytes; i++)
//                totalByteBuffer[totalReadBytes + i] = audioBuffer[i];
//            totalReadBytes += numberOfReadBytes;
//            //*/
//
//            tempIndex++;
//        }
//    }
//}


//public class MainActivity extends AppCompatActivity {
//    private static final String LOG_TAG = "AudioRecordTest";
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//    private static String fileName = null;
//
//    private RecordButton recordButton = null;
//    private MediaRecorder recorder = null;
//
//    private PlayButton   playButton = null;
//    private MediaPlayer player = null;
//
//    // Requesting permission to RECORD_AUDIO
//    private boolean permissionToRecordAccepted = false;
//    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////        this.permissionRequest();
////        InputStream credentials_stream = getResources().openRawResource(R.raw.credentials);
////        InputStream audio_stream = getResources().openRawResource(R.raw.audio);
////        try {
////            QuickstartSample.main(credentials_stream, audio_stream);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case REQUEST_RECORD_AUDIO_PERMISSION:
//                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
//        }
//        if (!permissionToRecordAccepted ) finish();
//
//    }
//
//    private void onRecord(boolean start) {
//        if (start) {
//            startRecording();
//        } else {
//            stopRecording();
//        }
//    }
//
//    private void onPlay(boolean start) {
//        if (start) {
//            startPlaying();
//        } else {
//            stopPlaying();
//        }
//    }
//
//    private void startPlaying() {
//        player = new MediaPlayer();
//        try {
//            player.setDataSource(fileName);
//            player.prepare();
//            player.start();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//    }
//
//    private void stopPlaying() {
//        player.release();
//        player = null;
//    }
//
//    private void startRecording() {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
//        recorder.setOutputFile(fileName);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            recorder.prepare();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//
//        recorder.start();
//    }
//
//    private void stopRecording() {
//        recorder.stop();
//        recorder.release();
//        recorder = null;
//    }
//
//    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
//        boolean mStartRecording = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                onRecord(mStartRecording);
//                if (mStartRecording) {
//                    setText("Stop recording");
//                } else {
//                    setText("Start recording");
//                }
//                mStartRecording = !mStartRecording;
//            }
//        };
//
//        public RecordButton(Context ctx) {
//            super(ctx);
//            setText("Start recording");
//            setOnClickListener(clicker);
//        }
//    }
//
//    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
//        boolean mStartPlaying = true;
//
//        OnClickListener clicker = new OnClickListener() {
//            public void onClick(View v) {
//                InputStream credentials_stream = getResources().openRawResource(R.raw.credentials);
//                InputStream audio_stream = getResources().openRawResource(R.raw.audio);
//                try {
//                    QuickstartSample.main(credentials_stream, audio_stream, fileName);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                onPlay(mStartPlaying);
//                if (mStartPlaying) {
//                    setText("Stop playing");
//                } else {
//                    setText("Start playing");
//                }
//                mStartPlaying = !mStartPlaying;
//            }
//        };
//
//        public PlayButton(Context ctx) {
//            super(ctx);
//            setText("Start playing");
//            setOnClickListener(clicker);
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//
//        // Record to the external cache directory for visibility
//        fileName = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath();
//        fileName += "/audiorecordtest.raw";
//
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
//
//        LinearLayout ll = new LinearLayout(this);
//        recordButton = new RecordButton(this);
//        ll.addView(recordButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        playButton = new PlayButton(this);
//        ll.addView(playButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        setContentView(ll);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (recorder != null) {
//            recorder.release();
//            recorder = null;
//        }
//
//        if (player != null) {
//            player.release();
//            player = null;
//        }
//    }
//
//    protected void permissionRequest(){
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.INTERNET)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.INTERNET)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.INTERNET},
//                        1);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//        }
//    }
//}



public class MainActivity extends AppCompatActivity {
    private static final int RECORDER_SAMPLERATE = 16000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    QuickstartSample obj;
    TextView tv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionRequest();
        tv1 = findViewById(R.id.transcriptTextView);
        obj = new QuickstartSample();
        setButtonHandlers();
        enableButtons(false);

        int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        String filePath = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath() + "voice8K16bitmono.pcm";
        short[] sData = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + Arrays.toString(sData));
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte[] bData = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
            InputStream credentials_stream = getResources().openRawResource(R.raw.credentials);
            InputStream audio_stream = getResources().openRawResource(R.raw.audio);
            try {
                obj.main(credentials_stream, audio_stream, filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recordingThread = null;
        }
        tv1.setText(obj.getTranscript());
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart: {
                    enableButtons(true);
                    startRecording();
                    break;
                }
                case R.id.btnStop: {
                    enableButtons(false);
                    stopRecording();
                    break;
                }
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void permissionRequest(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }
}