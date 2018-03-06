package lai.christine.hw5;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
//import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	
	private ViewGroup main;
	private MazeView mazeView;
	private Chronometer chronometer;
	private ImageView imageView;
	private long time = 0L;
	private Display display;
	private SensorManager sensorManager;
	private float[] acceleration;
	private float[] magnetism;
	private float degree = 0f;
	
	SensorEventListener accelListener = new SensorEventListener() {
		@Override public void onSensorChanged(final SensorEvent event) {
			runOnUiThread(new Runnable() {
				@Override public void run() {
					float x = 0f;
					float y = 0f;
					switch (display.getRotation()) {
						case Surface.ROTATION_0:
							x = event.values[0];
							y = event.values[1];
							break;
						case Surface.ROTATION_90:
							x = -event.values[1];
							y = event.values[0];
							break;
						case Surface.ROTATION_180:
							x = -event.values[0];
							y = -event.values[1];
							break;
						case Surface.ROTATION_270:
							x = event.values[1];
							y = -event.values[0];
							break;
					}
					mazeView.accelerate((float)(-x*2), (float)(y*2));
					acceleration = event.values;
				}});
		}
		@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};
	SensorEventListener magListener = new SensorEventListener() {
		@Override public void onSensorChanged(final SensorEvent event) {
			runOnUiThread(new Runnable() {
				@Override public void run() {
					magnetism = event.values;
					if (acceleration != null && magnetism != null) {
					    float R[] = new float[9];
					    float I[] = new float[9];
					    if (SensorManager.getRotationMatrix(R, I, acceleration, magnetism)) {
					        float orientation[] = new float[3];
					        SensorManager.getOrientation(R, orientation);
					        float azimuth = orientation[0];
					    	float newDegree = (float)(-Math.toDegrees((double)azimuth));
					    	RotateAnimation ra = new RotateAnimation(degree, newDegree, 
													    			 Animation.RELATIVE_TO_SELF, 0.5f, 
													                 Animation.RELATIVE_TO_SELF, 0.5f);
					        ra.setDuration(35);
					        ra.setFillAfter(true);
					        imageView.startAnimation(ra);
					        degree = newDegree;
					        mazeView.rotate(-newDegree);
					    }
					}
				}});
		}
		@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};
	private Sensor accelerometer;
	private Sensor magnetometer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main = (ViewGroup) findViewById(R.id.main);
		mazeView = new MazeView(this);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		mazeView.setLayoutParams(params);
		main.addView(mazeView);
		
		chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        imageView = (ImageView) findViewById(R.id.imageView);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	public void restartPressed(View view) {
		mazeView.stop();
    	sensorManager.unregisterListener(accelListener);
    	sensorManager.unregisterListener(magListener);
    	
		main.removeView(mazeView);
		mazeView = new MazeView(this);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		mazeView.setLayoutParams(params);
		main.addView(mazeView);
		mazeView.start();
		
		chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        
        sensorManager.registerListener(accelListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(magListener, magnetometer, SensorManager.SENSOR_DELAY_GAME);
		mazeView.start();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		chronometer.setBase(SystemClock.elapsedRealtime() + time);
		sensorManager.registerListener(accelListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(magListener, magnetometer, SensorManager.SENSOR_DELAY_GAME);
		mazeView.start();
	}
	
	@Override
	protected void onPause() {
		mazeView.stop();
    	sensorManager.unregisterListener(accelListener);
    	sensorManager.unregisterListener(magListener);
    	time = chronometer.getBase() - SystemClock.elapsedRealtime();
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
			case R.id.action_instructions:
				Intent intent = new Intent(this, InstructionActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
	}
	
}
