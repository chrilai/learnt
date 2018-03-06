package lai.christine.hw6;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class UFOServiceImpl extends Service {
	
	private int n = 0;
	private static final String URI = "http://javadude.com/aliens/";
	private static final String JSON = ".json";
	
	private class ServiceThread extends Thread {
		
		@Override public void run() {
			while(!isInterrupted()) {
				List<UFOServiceReporter> targets;
				synchronized (reporters) {
					targets = new ArrayList<UFOServiceReporter>(reporters);
				}
				// retrieve UFO Positions
				if (targets.size() > 0) {
					try {
						n++;
						Log.d("UFOServiceImpl", "fetching data... " + n);
						HttpUriRequest request = new HttpGet(URI + n + JSON);
				        HttpClient httpclient = new DefaultHttpClient();
				        HttpResponse response = httpclient.execute(request);
			            StatusLine statusLine = response.getStatusLine();
			            HttpEntity entity = response.getEntity();
			            
			            if (statusLine.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
			            	break; // stop polling on HTTP error status code 404
			            } else if (entity != null) {
			            	ByteArrayOutputStream out = new ByteArrayOutputStream();
			           		entity.writeTo(out);
			            	out.close();
			            	String resultString = out.toString();
							JSONArray jsonArray = new JSONArray(resultString);
							Log.d("UFOServiceImpl", jsonArray.toString());
							for(UFOServiceReporter reporter : targets) {
								reporter.report(Util.getUFOList(jsonArray));
							}
			            }
					} catch (Exception e) {
						Log.e("UFOServiceImpl", "report", e);
					}
				}
				try {
					sleep(1000); // poll once per second
				} catch (InterruptedException e) {
					interrupt();
				}
			}
			Log.d("UFOServiceImpl", "interrupted");
		}
	}
	
	private ServiceThread serviceThread;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("UFOServiceImpl", "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("UFOServiceImpl", "onStartCommand(" + intent + ", " + flags + ", " + startId + ")");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (serviceThread != null) {
			serviceThread.interrupt();
			serviceThread = null;
		}
		Log.d("UFOServiceImpl", "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		serviceThread = new ServiceThread();
		serviceThread.start();
		return binder;
	}

	private UFOService.Stub binder = new UFOService.Stub() {
		@Override public void add(UFOServiceReporter reporter) throws RemoteException {
			synchronized (reporters) {
				reporters.add(reporter);
			}
		}
		@Override public void remove(UFOServiceReporter reporter) throws RemoteException {
			synchronized (reporters) {
				reporters.remove(reporter);
			}
		}
	};
	
	private List<UFOServiceReporter> reporters = new ArrayList<UFOServiceReporter>();
}
