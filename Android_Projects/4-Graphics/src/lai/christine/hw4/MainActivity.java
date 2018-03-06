package lai.christine.hw4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	
	private ViewGroup main;
	private KlotskiView klotskiView;
	private Chronometer chronometer;
	long time = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main = (ViewGroup) findViewById(R.id.main);
		klotskiView = new KlotskiView(this);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		klotskiView.setLayoutParams(params);
		main.addView(klotskiView);
		
		chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
	}

	public void restartPressed(View view) {
		main.removeView(klotskiView);
		klotskiView = new KlotskiView(this);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		klotskiView.setLayoutParams(params);
		main.addView(klotskiView);
		
		chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		chronometer.setBase(SystemClock.elapsedRealtime() + time);
	}
	
	@Override
	protected void onPause() {
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
