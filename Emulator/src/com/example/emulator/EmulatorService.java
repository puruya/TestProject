package com.example.emulator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class EmulatorService extends Service {

	public static final String TAG = "EmulatorService";
	public final static int SCREEN_ON = 1;
	final RemoteCallbackList<EmulatorAIDLCallback> mCallbacks = new RemoteCallbackList<EmulatorAIDLCallback>();
	NotificationManager mNM;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	private final EmulatorAIDL.Stub mBinder = new EmulatorAIDL.Stub() {
		@Override
		public void unregisterCallback(EmulatorAIDLCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if (cb != null)
				mCallbacks.unregister(cb);
		}

		@Override
		public void registerCallback(EmulatorAIDLCallback cb)
				throws RemoteException {
			if (cb != null)
				mCallbacks.register(cb);
		}

		@Override
		public void openfile() throws RemoteException {
		}

		@Override
		public void closefile() throws RemoteException {
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		NanoHttpd();
	}

	public static final String from = "sdcard/index.html";
	public static final String to = "/data/data/com.example.emulator/";
	public static final String copy_name = "index.html";

	public File doCopy() {

		File folder = new File(to + "files/");
		folder.mkdirs();
		File outfile = new File(to + "files/" + copy_name);
		try {
			// AssetManager assetManager = getResources().getAssets();
			// InputStream is = assetManager.open(HTML_NAME,
			// AssetManager.ACCESS_BUFFER);
			InputStream is = new FileInputStream(from);
			long filesize = is.available();
			byte[] tempdata = new byte[(int) filesize];
			is.read(tempdata);
			is.close();
			outfile.createNewFile();
			FileOutputStream fo = new FileOutputStream(outfile);
			fo.write(tempdata);
			fo.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return folder;
	}
	private NanoHTTPD mHttpd = null;
	private void NanoHttpd() {
		File wwwroot = doCopy();
		try {
			mHttpd = new NanoHTTPD(8091, wwwroot);
		 //mHttpd = new NanoHTTPD(this, 8091, wwwroot);
			mHttpd.registerCommandReceiver(mCommandReceiver);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private NanoHTTPD.CommandReceiver mCommandReceiver = new NanoHTTPD.CommandReceiver() {
		@Override
		public void onCommandReceived(String cmd, String value) {
			Log.d(TAG, "onCommandReceived cmd = " + cmd + " value = " + value);
			if (cmd.equalsIgnoreCase("screen")) {
				PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
				if (value.equalsIgnoreCase("1")) {
					//screen on
					pm.userActivity(SystemClock.uptimeMillis(), false);			
				} else if (value.equalsIgnoreCase("0")) {
					//screen off
					pm.goToSleep(2000);
					pm.wakeUp(2000);
				}
			}	
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		mNM.cancel(R.string.remote_service_started);
		mCallbacks.kill();
	}
	
	public void screenOnOff(String value) throws RemoteException {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	
		if (value.equalsIgnoreCase("1")) {
			//screen on
			pm.userActivity(SystemClock.uptimeMillis(), false);			
		} else if (value.equalsIgnoreCase("0")) {
			//screen off
			pm.goToSleep(2000);
			pm.wakeUp(2000);
		}
	}

	private void showNotification() {
		// TODO Auto-generated method stub
		CharSequence text = getText(R.string.remote_service_started);

		Notification notification = new Notification(R.drawable.stat_sample,
				text, System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Emulator.class), 0);

		notification.setLatestEventInfo(this,
				getText(R.string.remote_service_label), text, contentIntent);

		mNM.notify(R.string.remote_service_started, notification);
	}

	
	
}
