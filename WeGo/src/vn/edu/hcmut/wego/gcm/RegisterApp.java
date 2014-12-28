package vn.edu.hcmut.wego.gcm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterApp extends AsyncTask<Void, Void, Void> {
	
	private Context context;
	private GoogleCloudMessaging gcm;
	private String registerId;
	private int appVersion;

	public RegisterApp(Context context, GoogleCloudMessaging gcm, int appVersion) {
		this.context = context;
		this.gcm = gcm;
		this.appVersion = appVersion;
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			// Check GCM instance
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			
			// Register with GCM server
			registerId = gcm.register(Constant.GCM_SENDER_ID);

			// Send registered id to server
			sendRegistrationIdToBackend(Utils.getValueFromSharedPreferences(context, Constant.PREFS_USER_ID, Integer.class));

			// Persist the regID - no need to register again.
			storeRegistrationId(context, registerId);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void storeRegistrationId(Context context, String registerId) {
		final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("registration_id", registerId);
		editor.putInt("appVersion", appVersion);
		editor.commit();
	}

	private void sendRegistrationIdToBackend(Object userId) {
		URI url = null;
		try {
			url = new URI("http://wego.journeymanager.esy.es/db_device_register.php?regId=" + registerId + "&userId=" + userId.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		request.setURI(url);
		try {
			httpclient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
