package com.bldj.lexiang.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bldj.lexiang.GlobalConfig;



public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	
	ConnectivityManager mConnectivity = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo info = mConnectivity.getActiveNetworkInfo();
	if(info==null ||!info.isAvailable()){//网络无连接
	    
	    GlobalConfig.CURRENT_NETWORK_STATE_TYPE=GlobalConfig.NETWORK_STATE_IDLE;
	    GlobalConfig.GLOBAL_NET_STATE=false;	   
	}else if(info.getTypeName().equalsIgnoreCase("wifi")){//wifi
	    GlobalConfig.CURRENT_NETWORK_STATE_TYPE=GlobalConfig.NETWORK_STATE_WIFI;
	    GlobalConfig.GLOBAL_NET_STATE=true;	   
	}else if(info.getTypeName().equalsIgnoreCase("mobile")){
	    GlobalConfig.CURRENT_NETWORK_STATE_TYPE=GlobalConfig.NETWORK_STATE_MOBILE;
	    GlobalConfig.GLOBAL_NET_STATE=true;
	}else if(info.getTypeName().equalsIgnoreCase("edge")){
	    GlobalConfig.CURRENT_NETWORK_STATE_TYPE=GlobalConfig.NETWORK_STATE_EDGE;
	    GlobalConfig.GLOBAL_NET_STATE=true;
	}
	
    }

}
