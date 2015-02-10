package magicbox.asia.magicboxtest;

import java.util.ArrayList;
import java.util.List;

import magicbox.asia.magicboxtest.util.IabHelper;
import magicbox.asia.magicboxtest.util.IabHelper.OnConsumeFinishedListener;
import magicbox.asia.magicboxtest.util.IabHelper.OnIabPurchaseFinishedListener;
import magicbox.asia.magicboxtest.util.IabHelper.QueryInventoryFinishedListener;
import magicbox.asia.magicboxtest.util.IabResult;
import magicbox.asia.magicboxtest.util.Inventory;
import magicbox.asia.magicboxtest.util.Purchase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InAppActivity extends Activity {
	
	final String TAG = "InApp";
//	final String PRODUCT_ID = "android.test.purchased";
	final String PRODUCT_ID = "magicboxtest1";
	IabHelper mHelper;
	Purchase purchaseItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inapp_layout);
		
		Button queryItem = (Button) findViewById(R.id.inapp_query_item);
		queryItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				inApp();
			}
		});
		
		Button purchase = (Button) findViewById(R.id.inapp_purchase_item);
		purchase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				purchase();
			}
		});
		
		Button queryPurchase = (Button) findViewById(R.id.inapp_query_purchased);
		queryPurchase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				queryPurchased();
			}
		});
		
		Button consumeItem = (Button) findViewById(R.id.inapp_consume_item);
		consumeItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				consumeItem(InAppActivity.this.purchaseItem);
			}
			
		});
		
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxogrtvO+FS1E3fPDQPGukjhRzzyQQqS+bZSkDb2YdACnIkjbDwm406LvQ3ZELFc06s0ZCYx8N58hHWRCKVgJ93ZT3x/S6UM+TdNsjSlA5VUNE1HPD5ntwa/U5QBCMpdFRbGy1gz0XHq7QLpi3mlLQnaq/lPS5GKrh3k9gWDj1pUUWB8MF/tkr6e9GHBMmMzrlm1ypCktK3SNqm/l7z9RT9wjrLx5+fXk2fb4FksokZSMBdLrPWw1uBJZDpfeWInlTzJvQX4JXbMpH7PksusnA/jyrRi+eGx1PoQ9jXM56YewPK7yJXUanTN+DdcIa11ogRoXnbr+UIVvg/uZIFBPNQIDAQAB";
		
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Log.d(TAG, "problem setting up in-app billing: "+result);
				}else{
					Log.d(TAG, "result in-app billing: "+result);
					inApp();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}else{
			Log.d(TAG, "OAR handled");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHelper!=null) {
			mHelper.dispose();
		}
		mHelper = null;
	}
	
	private void inApp(){
		List<String> skuList = new ArrayList<String>();
		skuList.add(PRODUCT_ID);
		mHelper.queryInventoryAsync(true, skuList, mQueryFinishedListener);
	}
	
	private void purchase(){
		mHelper.launchPurchaseFlow(this, PRODUCT_ID, 999, mPurchaseFinishedListener);
	}
	
	private void queryPurchased(){
		mHelper.queryInventoryAsync(mQueryInventoryFinishedListener);
	}
	
	private void consumeItem(Purchase purchase){
		if (purchase!=null) {			
			mHelper.consumeAsync(purchase, mConsumeFinishedListener);
		}
	}
	
	QueryInventoryFinishedListener mQueryFinishedListener = new QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			if (result.isFailure()) {
				Log.d(TAG, result.toString());
				return ;
			}
			
			String itemPrice = inv.getSkuDetails(PRODUCT_ID).getPrice();
			Log.d(TAG, "item price: "+itemPrice);
			
//			purchase();
		}
	};
	
	OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
		
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase info) {
			if (result.isFailure()) {
				Log.d(TAG, "Error purchasing: "+result);
				if (info!=null) {					
					Log.d(TAG, info.toString());
				}else{
					Log.i(TAG, "no purchase info");
				}
				return;
			}else if (info.getSku().equals(PRODUCT_ID)) {
				Log.i(TAG, result.toString());
				Log.i(TAG, "Purchased item: "+info.getPackageName());
				Log.i(TAG, "Purchased item: "+info.toString());
				
				purchaseItem = info;
				consumeItem(purchaseItem);
			}
		}
	};
	
	QueryInventoryFinishedListener mQueryInventoryFinishedListener = new QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			if (result.isFailure()) {
				Log.e(TAG, result.toString());
				return;
			}else{
				Log.i(TAG, result.toString());
				if (inv.hasPurchase(PRODUCT_ID)) {	
					purchaseItem = inv.getPurchase(PRODUCT_ID);
					Log.i(TAG, "has item"+purchaseItem.toString());
				}
			}
		}
	};
	
	OnConsumeFinishedListener mConsumeFinishedListener = new OnConsumeFinishedListener() {
		
		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			if (result.isSuccess()) {
				Log.i(TAG, result.toString());
				Log.i(TAG, purchase.toString());
				purchaseItem = null;
			}else{
				Log.e(TAG, result.toString());
			}
		}
	};
}
