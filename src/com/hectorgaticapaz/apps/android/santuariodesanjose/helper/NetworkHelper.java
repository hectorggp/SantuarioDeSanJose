package com.hectorgaticapaz.apps.android.santuariodesanjose.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkHelper {

	public static boolean WifiNetworkStatus(Context ctx){
		ConnectivityManager connMgr = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connMgr.getActiveNetworkInfo()==null)
			return false;
		else{
			android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifi == null){
				return false;
			}else{
				return wifi.isConnected();
			}

		}

	}

	public static boolean MobileNetworkStatus(Context ctx){
		ConnectivityManager connMgr = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connMgr.getActiveNetworkInfo()==null)
			return false;
		else{
			android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobile == null){
				return false;
			}else{
				return mobile.isConnected();
			}

		}
	}

	public static boolean NetworkStatus(Context ctx){
		ConnectivityManager connMgr = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connMgr.getActiveNetworkInfo()==null){
			return false;
		}else{
			android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			return (mobile != null && mobile.isConnected()) || (wifi != null && wifi.isConnected());
		}

	}

	public static String getMacAddress(Context ctx){
		WifiManager manager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		String address = info.getMacAddress();
		return address;
	}
}