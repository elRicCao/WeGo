package vn.edu.hcmut.wego.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CurrentTripActivity;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.utility.GPSTracker;
import vn.edu.hcmut.wego.utility.Utils;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
				String location = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();

				// Prepare the response message
				String response = Constant.RECEIVE_USER_LOCATION + "|" + String.valueOf(userId) + "|" + location;

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
				Log.i("Debug receive reply", message);
				Intent broadcastIntent = new Intent(Constant.BROADCAST_LOCATION_ACTION);
				broadcastIntent.putExtra("message", message);
				sendOrderedBroadcast(broadcastIntent, null);
			}

			else {
				Log.i("Debug receive", message);
				String header = contents[0];
				String sender = contents[3];
				String notification = new String();
				if (header.equalsIgnoreCase(Constant.WARNING_WAIT_LOST)) {
					notification = sender + " gets lost! Slow down!";
				} else if (header.equalsIgnoreCase(Constant.WARNING_WAIT_VEHICLE)) {
					notification = sender + "'s vehicle is broken! Slow down!";
				} else if (header.equalsIgnoreCase(Constant.WARNING_WAIT_GAS)) {
					notification = sender + " needs to refuel! Slow down!";
				} else if (header.equalsIgnoreCase(Constant.WARNING_POLICE_ALERT)) {
					notification = sender + " warns police ahead! Be careful!";
				} else if (header.equalsIgnoreCase(Constant.WARNING_POLICE_CAPTURE)) {
					notification = sender + " is captured at police checkpoint!";
				} else if (header.equalsIgnoreCase(Constant.WARNING_REGROUP)) {
					notification = sender + " wants everyone to regroup at his/her location!";
				} else if (header.equalsIgnoreCase(Constant.WARNING_ACCIDENT)) {
					notification = sender + " gets involved in an accident! Turn back!";
				}
				sendNotification(notification, message);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String notiContent, String message) {
		NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		String[] contents = message.split("\\|");
		
		Intent intent = new Intent(this, CurrentTripActivity.class);
		intent.putExtra("isWarning", true);
		intent.putExtra("tripId", Integer.parseInt(contents[1]));
		intent.putExtra("message", message);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("WeGo Warning");
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notiContent));
		mBuilder.setContentText(notiContent);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);

		Notification notification = mBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
}
