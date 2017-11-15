package ru.kuzmiankou.videorecorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder mediaRecorder;
    private File outputDir;
    private boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        outputDir = new File(moviesDir, "VideoRecorder");
        outputDir.mkdir();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mediaRecorder = new MediaRecorder();
        initAddConfigureMediaRecorder();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(recording) {
            try {
                mediaRecorder.stop();
            }
            catch (IllegalStateException e) {

            }
        }
        releaseMediaRecorder();
        Button button = (Button) findViewById(R.id.button1);
        button.setText("Start");
        recording = false;
    };

    private void releaseMediaRecorder() {
        if(mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void initAddConfigureMediaRecorder() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoFrameRate(10);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String outputFile = new File(outputDir, System.currentTimeMillis() + ".mp4").getAbsolutePath();
        mediaRecorder.setOutputFile(outputFile);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.videoView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
    }

    public void startStopRecording(View view) {
        Button button = (Button) findViewById(R.id.button1);
        if(recording) {
            button.setText("Start");
            try {
                mediaRecorder.stop();
            }
            catch (IllegalStateException e) {

            }
            releaseMediaRecorder();
        }
        else {
            button.setText("Stop");
            if(mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
                initAddConfigureMediaRecorder();
            }
            try {
                mediaRecorder.prepare();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
        }
        recording = !recording;
    }
}
