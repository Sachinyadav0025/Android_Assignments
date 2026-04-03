package com.example.player;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private TextView statusText;
    private boolean isVideoActive = false;
    private final String videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        videoView = findViewById(R.id.videoView);
        statusText = findViewById(R.id.statusText);

        Button btnOpenFile = findViewById(R.id.btnOpenFile);
        Button btnOpenURL = findViewById(R.id.btnOpenURL);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPause = findViewById(R.id.btnPause);
        Button btnStop = findViewById(R.id.btnStop);
        Button btnRestart = findViewById(R.id.btnRestart);

        // Open Local Audio
        btnOpenFile.setOnClickListener(v -> {
            stopVideo();
            isVideoActive = false;
            loadAudio();
        });

        // Open Video URL
        btnOpenURL.setOnClickListener(v -> {
            stopAudio();
            isVideoActive = true;
            loadVideo();
        });

        // Control Listeners
        btnPlay.setOnClickListener(v -> {
            if (isVideoActive) {
                videoView.start();
                statusText.setText("Video Playing");
            } else if (mediaPlayer != null) {
                mediaPlayer.start();
                statusText.setText("Audio Playing");
            } else {
                Toast.makeText(this, "Load media first!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPause.setOnClickListener(v -> {
            if (isVideoActive && videoView.isPlaying()) {
                videoView.pause();
                statusText.setText("Video Paused");
            } else if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                statusText.setText("Audio Paused");
            }
        });

        btnStop.setOnClickListener(v -> {
            if (isVideoActive) {
                videoView.pause();
                videoView.seekTo(0);
                statusText.setText("Video Stopped");
            } else if (mediaPlayer != null) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                statusText.setText("Audio Stopped");
            }
        });

        btnRestart.setOnClickListener(v -> {
            if (isVideoActive) {
                videoView.seekTo(0);
                videoView.start();
                statusText.setText("Video Restarted");
            } else if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                statusText.setText("Audio Restarted");
            }
        });
    }

    private void loadAudio() {
        stopAudio();
        // Pointing to my_song.mp3 in res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.my_song);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            statusText.setText("Playing: my_song.mp3");
            mediaPlayer.setOnCompletionListener(mp -> statusText.setText("Audio Finished"));
        } else {
            statusText.setText("Error: Could not load audio");
            Toast.makeText(this, "Check if my_song.mp3 is in res/raw", Toast.LENGTH_LONG).show();
        }
    }

    private void loadVideo() {
        statusText.setText("Buffering Video...");
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(mp -> {
            statusText.setText("Playing: Online Video");
            videoView.start();
        });
        videoView.setOnErrorListener((mp, what, extra) -> {
            statusText.setText("Failed to load video");
            return true;
        });
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopVideo() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAudio();
        stopVideo();
    }
}