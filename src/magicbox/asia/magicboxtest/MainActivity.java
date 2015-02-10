package magicbox.asia.magicboxtest;

import magicbox.asia.magicboxsdk.DashboardLifeCycleHelper;
import magicbox.asia.magicboxsdk.Device;
import magicbox.asia.magicboxsdk.GooglePlayUtils;
import magicbox.asia.magicboxsdk.GooglePlayUtils.GooglePlayCallback;
import magicbox.asia.magicboxsdk.MagicBoxAPIClient;
import magicbox.asia.magicboxsdk.MagicBoxAPIClientCallback;
import magicbox.asia.magicboxsdk.MagicBoxDashboardButton;
import magicbox.asia.magicboxsdk.MagicBoxError;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

public class MainActivity extends FragmentActivity {

	MagicBoxAPIClient client;
	GooglePlayUtils googleUtil;
	Device deviceInfo;
	Button getUserInfoBtn;
	MagicBoxDashboardButton dashboard;
	DashboardLifeCycleHelper helper;

	boolean isMoving = false;

	PopupWindow popupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		helper = new DashboardLifeCycleHelper(this, new MagicBoxAPIClientCallback() {
			
			@Override
			public void onAuthenticationError(MagicBoxError error) {
			}
			
			@Override
			public void onAuthenticationCompleted(Object data) {
			}
		});
		helper.setBackground(R.drawable.magicbox_header);
		helper.onCreate();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
 
		client = new MagicBoxAPIClient(this, true);

		try {
//			client.openSession(); 
//			client.login("testcocos", "12345678",
//					new MagicBoxAPIClientCallback() {
//						@Override
//						public void onAuthenticationCompleted(Object data) {
//							Log.i("MBXTest", data.toString());
//							startService(new Intent(MainActivity.this,MagicBoxDashboard.class));
//						}
//
//						@Override
//						public void onAuthenticationError(MagicBoxError error) {
//							Log.i("MBXTest", error.getMessage());
//						}
//					});
		} catch (Exception e) {
			e.printStackTrace();
		}

		googleUtil = new GooglePlayUtils(this,"520373653753", new GooglePlayCallback() {

			@Override
			public void onComplete(String registrationId) {
				Log.i("Test", registrationId);
			}
		});
		
		deviceInfo = Device.getInstant();

		Button logoutBtn = (Button) findViewById(R.id.logout_button);
		getUserInfoBtn = (Button) findViewById(R.id.get_userinfo);
		Button payByTrue = (Button) findViewById(R.id.pay_by_true);
		Button payByThaiEPay = (Button) findViewById(R.id.pay_by_thaiepay);
		Button inApp = (Button) findViewById(R.id.inapp);
		Button gcm = (Button) findViewById(R.id.gcm);
		Button device = (Button) findViewById(R.id.device);

		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.logout(new MagicBoxAPIClientCallback() {
					
					@Override
			 		public void onAuthenticationError(MagicBoxError error) {
					}
					
					@Override
					public void onAuthenticationCompleted(Object data) {
						finish();
					}
				});
			}
		});

		getUserInfoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.searchFriend("mbxtest", new MagicBoxAPIClientCallback() {
					
					@Override
					public void onAuthenticationError(MagicBoxError error) {
					}
					
					@Override
					public void onAuthenticationCompleted(Object data) {
					}
				});
			}
		});

		payByTrue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				client.requestFriend(204, new MagicBoxAPIClientCallback() {
					
					@Override
					public void onAuthenticationError(MagicBoxError error) {
					}
					
					@Override
					public void onAuthenticationCompleted(Object data) {
					}
				});
			}
		});
		payByThaiEPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		inApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				inAppPurchase();
			}
		});

		gcm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				googleUtil.getRegistrationId();
			}
		});

		device.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("device", deviceInfo.toString());
				Log.i("device", deviceInfo.os);
				Log.i("device", deviceInfo.OSVersion);
				Log.i("device", deviceInfo.deviceModel);
			}
		});

	}

	private void inAppPurchase() {
		startActivity(new Intent(this, InAppActivity.class));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		helper.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		helper.onResume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		helper.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.onDestroy();
//		stopService(new Intent(MainActivity.this,MagicBoxDashboard.class));
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		Log.i("MBX", "return from activity result code:"+arg0);
		helper.onActivityResult(arg0, arg1, arg2);
	}
}