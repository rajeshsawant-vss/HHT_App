package com.tatamotors.errorproofing.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.interfaces.ScanData;

public class ScannerReciever extends BroadcastReceiver {
    ScanData scanData;
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(context.getResources().getString(R.string.activity_intent_filter_action))){
            scanData=(ScanData)context;

            String decodedData = intent.getStringExtra(context.getResources().getString(R.string.datawedge_intent_key_data));
            if(decodedData!=null)
            {
                scanData.getScanData(decodedData);
            }

        }
        else {
            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }

    }
}
