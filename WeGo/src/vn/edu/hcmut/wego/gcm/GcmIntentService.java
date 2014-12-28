package vn.edu.hcmut.wego.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.GPSTracker;
import vn.edu.hcmut.wego.utility.Utils;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "GcmIntentService";

	public GcmIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

			String message = extras.getString("Notice");
			String[] contents = message.split("\\|");

			int userId = Utils.getUserId(this);

			// GET_USER_LOCATION format: header | senderId
			if (contents[0].equalsIgnoreCase(Constant.GET_USER_LOCATION)) {

				// Get user location and convert to string
				GPSTracker gpsTracker = new GPSTracker(this);
				String location = gpsTracker.getLongitude() + "," + gpsTracker.getLatitude();

				// Prepare the response message
				String response = Constant.RECEIVE_USER_LOCATION + "|" + location + "|" + String.valueOf(userId);

				// Prepair the condition
				String senderId = contents[1];
				String condition = " where user_id=" + senderId;

				try {
					// Prepare parameters
					JSONObject params = new JSONObject();
					params.put("message", response);
					params.put("condition", condition);

					// TODO: Reply message
					Log.i("Debug reply GCM", params.toString());

					// Create and execute server request
					ServerRequest.newServerRequest(RequestType.PUSH_TO_SELECTED_USER, params, null).executeAsync();

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			// RECEIVE_USER_LOCATION format: header | location | senderId
			else if (contents[0].equalsIgnoreCase(Constant.RECEIVE_USER_LOCATION)) {

				// Get user location and user id
				String location = contents[1];
				String sender = contents[2];

				// TODO: receive message
				Log.i("Debug receiver reply GCM", message);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("WeGo");
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
		mBuilder.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
