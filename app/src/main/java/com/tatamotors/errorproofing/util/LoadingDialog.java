package com.tatamotors.errorproofing.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.tatamotors.errorproofing.R;

public class LoadingDialog extends Dialog {


    /*  ProgressBar customProgressBar;*/

    public LoadingDialog(Context context) {
        super(context);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.transparent_progress_layout);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

  /*  public void SetTitleMessage(String MessageTitle){
        ((TextView)findViewById(R.id.customProgressBar_message)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.customProgressBar_message)).setText(MessageTitle);
    }*/

    /*private void MapWidgetIds() {
        customProgressBar = (ProgressBar) findViewById(R.id.customProgressBar);
    }*/


}
