package com.tatamotors.errorproofing.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.tv.TvContract;
import android.net.ConnectivityManager;
import android.os.Build;

import com.tatamotors.errorproofing.R;

public class Common {

   public static  ProgressDialog progressDialog;


    public  static void showdialogue(Context context){
        progressDialog=ProgressDialog.show(context,"",context.getString(R.string.loading),true);

    }

    public static  void  dismiss(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return cm.getActiveNetwork() != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null;
        } else {
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
    }

   public static String replaceString(String string) {
     //   return string.replaceAll("[;\\/:*?@\"<>|&']","");
        return string.replaceAll("[^A-Za-z0-9 ]","");
    }

    public static void showLoginError(final Activity cont, String Message)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(cont,R.style.AlertDialogStyle);
        builder.setMessage(Message)
                .setCancelable(false)
                .setPositiveButton(cont.getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {

                      dialog.dismiss();

                    }
                });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
