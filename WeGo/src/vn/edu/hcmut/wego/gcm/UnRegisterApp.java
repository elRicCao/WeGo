package vn.edu.hcmut.wego.gcm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import vn.edu.hcmut.wego.constant.Constant;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;

public class UnRegisterApp extends AsyncTask<Void, Void, Void> {

	private Context context;
	private GoogleCloudMessaging gcm;
	private String regid;

	public UnRegisterApp(Context ctx, GoogleCloudMessaging gcm) {
		this.context = ctx;
		this.gcm = gcm;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regid = gcm.register(Constant.GCM_SENDER_ID);

			deleteRegistrationIdFromBackend();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void deleteRegistrationIdFromBackend() {
		URI url = null;
		try {
			url = new URI("http://wego.journeymanager.esy.es/db_device_unregister.php?regId=" + regid);
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
