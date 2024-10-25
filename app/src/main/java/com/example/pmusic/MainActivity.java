package com.example.pmusic;




import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button play;
    private Button pause;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(); // Handler for updating seekbar progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        seekBar = findViewById(R.id.seekBar2);

        // Initialize media player with a local raw resource (e.g., song.mp3 in res/raw)
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        seekBar.setMax(mediaPlayer.getDuration());  // Set max value for the seekbar based on media duration

        // Start playing the media
        mediaPlayer.start();
        Toast.makeText(MainActivity.this, "Ready to play", Toast.LENGTH_SHORT).show();

        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);  // Seek to the selected position if changed by user
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Play button listener
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                updateSeekBar();  // Start updating the SeekBar progress
            }
        });

        // Pause button listener
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                handler.removeCallbacks(updater);  // Stop updating the SeekBar when paused
            }
        });

        // Update SeekBar progress while media is playing
        updateSeekBar();
    }

    // Runnable to update SeekBar
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());  // Update the seekbar with the current position
                handler.postDelayed(this, 1000);  // Update every second
            }
        }
    };

    // Function to start updating the SeekBar
    private void updateSeekBar() {
        handler.post(updater);  // Start the handler
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources and stop the handler when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updater);  // Remove the handler callbacks
    }
}
