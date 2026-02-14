package helper;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.taxiapp.R;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer mediaPlayer;
    private Context context;
    private static final String TAG = "SoundManager";

    private SoundManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    public void playPanicSound() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(context, R.raw.panic);

            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> {
                    Log.d(TAG, "Sound playback completed");
                    mp.release();
                    mediaPlayer = null;
                });

                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    Log.e(TAG, "Error playing sound: what=" + what + ", extra=" + extra);
                    mp.release();
                    mediaPlayer = null;
                    return true;
                });

                mediaPlayer.start();
                Log.d(TAG, "Playing panic sound");
            } else {
                Log.e(TAG, "Failed to create MediaPlayer - sound file not found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            } catch (Exception e) {
                Log.e(TAG, "Error stopping sound: " + e.getMessage());
            } finally {
                mediaPlayer = null;
            }
        }
    }

    public void release() {
        stopSound();
        instance = null;
    }
}
