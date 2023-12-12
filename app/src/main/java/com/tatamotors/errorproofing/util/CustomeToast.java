package com.tatamotors.errorproofing.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tatamotors.errorproofing.R;

public class CustomeToast {


        public void showSuccessToast(Context context,String messege){

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customToastroot = inflater.inflate(R.layout.custom_toast, null);
                LinearLayoutCompat error_lnr=customToastroot.findViewById(R.id.lnrError);
                LinearLayoutCompat sucess_lnr=customToastroot.findViewById(R.id.lnrSuccess);
                error_lnr.setVisibility(View.GONE);
                sucess_lnr.setVisibility(View.VISIBLE);
                TextView mText=customToastroot.findViewById(R.id.textMessege);
                mText.setText(messege);
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setView(customToastroot);//setting the view of custom toast layout
                toast.show();

        }

        public void showErrorToast(Context context,String messege){

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customToastroot = inflater.inflate(R.layout.custom_toast, null);
                LinearLayoutCompat error_lnr=customToastroot.findViewById(R.id.lnrError);
                LinearLayoutCompat sucess_lnr=customToastroot.findViewById(R.id.lnrSuccess);
                error_lnr.setVisibility(View.VISIBLE);
                sucess_lnr.setVisibility(View.GONE);
                TextView mText=customToastroot.findViewById(R.id.textError);
                mText.setText(messege);
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setView(customToastroot);//setting the view of custom toast layout
                toast.show();

        }

}
