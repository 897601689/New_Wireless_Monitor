package com.rilintech.new_wireless_monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent; 

public class MonitorscreenBootReceiver extends BroadcastReceiver { 

    @Override
    public void onReceive(Context arg0, Intent arg1) {
		if (arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent helloActivityIntent = new Intent(arg0,MonitorscreenActivity.class);
			helloActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			arg0.startActivity(helloActivityIntent);
		}
	}

} 