package lai.christine.hw6;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {
	
	private static final int GOOGLE_PLAY_SETUP = 42;
	private static final int PADDING = 48;
	private static final double CENTER_LAT = 38.9073;
	private static final double CENTER_LON = -77.0365;
	
	private BitmapDescriptor redUFO;
	private LatLngBounds bounds;
	private List<Marker> ufoMarkerList;
	private GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		redUFO = BitmapDescriptorFactory.fromResource(R.drawable.red_ufo);
	  	
	  	// bind service here to avoid restarts
//		bounds = new LatLngBounds(new LatLng(CENTER_LAT - 0.05, CENTER_LON - 0.05), 
//				new LatLng(CENTER_LAT + 0.05, CENTER_LON + 0.05));
//		ufoMarkerList = new ArrayList<Marker>();
//	  	if (!bindService(new Intent("lai.christine.hw6.UFOService"), serviceConnection, Context.BIND_AUTO_CREATE)) {
//	  		Toast.makeText(getBaseContext(), "Could not bind to service", Toast.LENGTH_LONG).show();
//	  	}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
    	Log.d("MainActivity", "onStart");
		bounds = new LatLngBounds(new LatLng(CENTER_LAT - 0.05, CENTER_LON - 0.05), 
				new LatLng(CENTER_LAT + 0.05, CENTER_LON + 0.05));
		ufoMarkerList = new ArrayList<Marker>();
	  	if (!bindService(new Intent("lai.christine.hw6.UFOService"), serviceConnection, Context.BIND_AUTO_CREATE)) {
	  		Toast.makeText(getBaseContext(), "Could not bind to service", Toast.LENGTH_LONG).show();
	  	}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("MainActivity", "onResume");
		int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		switch (googlePlayServicesAvailable) {
			case ConnectionResult.SUCCESS:
				break;
			case ConnectionResult.SERVICE_MISSING:
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			case ConnectionResult.SERVICE_DISABLED:
				GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, this, GOOGLE_PLAY_SETUP).show();
				return;
			default:
				throw new RuntimeException("Unexpected result code from isGooglePlayServicesAvailable: " + googlePlayServicesAvailable + " (" + GooglePlayServicesUtil.getErrorString(googlePlayServicesAvailable) + ")");
		}
		SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = map.getMap();
//		googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, PADDING));
	}
	
	@Override
	protected void onStop() {
		Log.d("MainActivity", "onStop");
		try {
			ufoService.remove(reporter);
		} catch (RemoteException e) {
			Log.e("MainActivity", "removeReporter", e);
		}
		unbindService(serviceConnection);
		googleMap.clear();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.d("MainActivity", "onDestroy");
		// unbind service here to avoid restarts
//		try {
//			ufoService.remove(reporter);
//		} catch (RemoteException e) {
//			Log.e("MainActivity", "removeReporter", e);
//		}
//		unbindService(serviceConnection);
//		googleMap.clear();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
			case R.id.action_restart:
				Log.d("MainActivity", "restart");
				try {
					ufoService.remove(reporter);
				} catch (RemoteException e) {
					Log.e("MainActivity", "removeReporter", e);
				}
				unbindService(serviceConnection);
				googleMap.clear();
				ufoMarkerList.clear();
				bounds = new LatLngBounds(new LatLng(CENTER_LAT - 0.02, CENTER_LON - 0.02), 
						  new LatLng(CENTER_LAT + 0.02, CENTER_LON + 0.02));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, PADDING));
			  	if (!bindService(new Intent("lai.christine.hw6.UFOService"), serviceConnection, Context.BIND_AUTO_CREATE)) {
			  		Toast.makeText(getBaseContext(), "Could not bind to service", Toast.LENGTH_LONG).show();
			  	}
				return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
	}

    private UFOService ufoService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override public void onServiceDisconnected(ComponentName name) {
			ufoService = null;
		}
		@Override public void onServiceConnected(ComponentName name, IBinder service) {
			ufoService = UFOService.Stub.asInterface(service);
			try {
				ufoService.add(reporter);
			} catch (RemoteException e) {
				Log.e("MainActivity", "addReporter", e);
			}
		}
	};
	
	private UFOServiceReporter reporter = new UFOServiceReporter.Stub() {
		@Override public void report(final List<UFOPosition> ufoPositionList) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override public void run() {
					updateUFOPositions(ufoPositionList);
				}});
		}
	};
		
	private void updateUFOPositions(List<UFOPosition> ufoPositionList) {
		for (UFOPosition ufoPosition : ufoPositionList) {
			// modify camera bounds to include new UFO position
			bounds = bounds.including(new LatLng(ufoPosition.getLat(), ufoPosition.getLon()));
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, PADDING));
			boolean known = false;
			for (Marker ufoMarker : ufoMarkerList) {
				try {
					if (ufoPosition.getShipNumber() == Integer.parseInt(ufoMarker.getTitle())) {
						known = true;
						// draw line to new UFO location
						googleMap.addPolyline(new PolylineOptions()
											  .add(ufoMarker.getPosition(), new LatLng(ufoPosition.getLat(), ufoPosition.getLon()))
											  .color(Color.BLUE)
											  .width(5));
						ufoMarker.setPosition(new LatLng(ufoPosition.getLat(), ufoPosition.getLon()));
					}
				} catch (NumberFormatException e) {
					Log.e("MainActivity", "update", e);
				}
			}
			if (!known) {
				// add marker representing new UFO
				Marker marker = googleMap.addMarker(new MarkerOptions()
													.position(new LatLng(ufoPosition.getLat(), ufoPosition.getLon()))
													.icon(redUFO)
													.anchor(0.5f, 0.5f)
													.title(Integer.toString(ufoPosition.getShipNumber())));
				ufoMarkerList.add(marker);
				Log.d("MainActivity", marker.getPosition().toString());
			}
		}
	}
}
