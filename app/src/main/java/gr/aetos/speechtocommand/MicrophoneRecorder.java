package gr.aetos.speechtocommand;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MicrophoneRecorder extends Thread {
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.UNPROCESSED;
    private static final int RECORDER_SAMPLE_RATE = 16000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BYTES_PER_BUFFER = 1600; // ~100ms
    private static AudioRecord audioRecord;
    public static volatile List<BlockingQueue<byte[]>> blockingQueues;

    @Override
    public void run() {
        audioRecord.startRecording();
        byte[] data = new byte[BYTES_PER_BUFFER];
        while (true) {
            try {
                int numBytesRead = audioRecord.read(data, 0, data.length);
                if ((numBytesRead <= 0)) {
                    continue;
                }
                for(BlockingQueue<byte[]> blockingQueue : blockingQueues){
                    blockingQueue.put(data.clone());
                }
            } catch (InterruptedException e) {
                System.out.println("Microphone input buffering interrupted : " + e.getMessage());
            }
        }
    }

    public static void main() {
        audioRecord = new AudioRecord(AUDIO_SOURCE,
                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BYTES_PER_BUFFER);
        blockingQueues = new ArrayList<>();
        new MicrophoneRecorder().start();
    }
}
