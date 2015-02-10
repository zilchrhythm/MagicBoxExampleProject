package magicbox.asia.magicboxtest;

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

public class GCMIntentService extends IntentService {
	
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private final String TAG = "GCMDemo";

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
             
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Magicbox Test")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

	/*private GoogleCloudMessaging gcm;
	private String regId;
	private Context context;
	private String SENDER_ID;

	public GCMIntentService() {
		super("942113542424");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("gcmintentservice", "start");
		this.context = this;
		SENDER_ID = "942113542424";
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (checkPlayService()) {
			Log.i("play service", "defined play service");
			gcm = GoogleCloudMessaging.getInstance(context);
//			regId = prefs.getGCMRegistrationId();
//			Log.i("stored reg_id", regId + "," + prefs.containsGCMRegId());

//			if (!prefs.containsGCMRegId()) {
				registerInBackground();
//			} else {
//				
//				if (intent.getExtras() != null) {
//
//					/*if (intent.getExtras().getString("command")
//							.equals("delete")) {
//						unRegisterInBackground();
//					}
//				}
//			}

		}
		return START_NOT_STICKY;
	}

	private boolean checkPlayService() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
			} else {

			}
			return false;
		}
		return true;
	}

	private void registerInBackground() {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				String message = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(SENDER_ID);
					Log.i("new reg_id", regId);
					message = "Device registered, registration ID = " + regId;

//					sendRegistrationIdToBackend();
//					prefs.putGCMRegistrationId(regId);
				} catch (IOException e) {
					message = "Error:" + e.getMessage();
				}
				Log.i("GCM", message);
			}
		};
		
		Thread t = new Thread(runnable);
		t.start();
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
	}*/

}
