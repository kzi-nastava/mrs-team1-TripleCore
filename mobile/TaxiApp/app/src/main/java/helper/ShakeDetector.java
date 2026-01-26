package helper;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// listens for shake events using the device's accelerometer
public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 12.0f; // how strong the shake must be
    private static final int SHAKE_INTERVAL_MS = 1000; // minimum time between shakes

    private long lastShakeTime = 0;

    public interface OnShakeListener {
        void onShake();
    }

    private OnShakeListener listener;

    public ShakeDetector(OnShakeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float acceleration =
                (float) Math.sqrt(x * x + y * y + z * z)
                        - SensorManager.GRAVITY_EARTH;

        if (acceleration > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();
            if (now - lastShakeTime > SHAKE_INTERVAL_MS) {
                lastShakeTime = now;
                listener.onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
