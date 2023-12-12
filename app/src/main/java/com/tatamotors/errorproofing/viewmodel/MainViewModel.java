package com.tatamotors.errorproofing.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.PartValidation;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.model.LoginModel;
import com.tatamotors.errorproofing.networkcall.ApiInterface;
import com.tatamotors.errorproofing.networkcall.RetroInstance;
import com.tatamotors.errorproofing.response.SerailError;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.ValidationResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    ProgressDialog dialog;
    public MutableLiveData<Response> statusCode;
    private MutableLiveData<SerialNovalidation> serialLiveData;
    private  MutableLiveData<String> serialNo;
    private  MutableLiveData<SerailError> serailErrorLiveData;

    public MainViewModel() {
        serialLiveData=new MutableLiveData<>();
        serialNo=new MutableLiveData<>();
    }

    public MutableLiveData<Response> getStatusCode() {
        if(statusCode==null){
            statusCode=new MutableLiveData<>();
        }
        return statusCode;
    }

    public MutableLiveData<SerailError> getSerailErrorLiveData() {

        if (serailErrorLiveData == null) {
            serailErrorLiveData = new MutableLiveData<>();

        }
        return serailErrorLiveData;

    }

    public MutableLiveData<SerialNovalidation> getSerialLiveData() {

        if (serialLiveData == null) {
            serialLiveData = new MutableLiveData<>();

        }
        return serialLiveData;

    }

    public MutableLiveData<String> getSerialNo() {

        if (serialNo == null) {
            serialNo = new MutableLiveData<>();
        }
        return serialNo;

    }

    public void setValue(SerialNovalidation serialNovalidation){
        serialLiveData.postValue(serialNovalidation);

    }
    public void setSerialNo(String value){

        serialNo.postValue(value);
    }

    public void setSerialNo(SerailError value){

        serailErrorLiveData.postValue(value);
    }

    public void getPartNos(Context mContext,String serialNo){

        Common.showdialogue(mContext);

        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);

       Call<SerialNovalidation>  call=apiServices.startSerialNo(AppPreference.getInstance().get(mContext,AppPreference.token), serialNo);
       call.enqueue(new Callback<SerialNovalidation>() {
           @Override
           public void onResponse(Call<SerialNovalidation> call, Response<SerialNovalidation> response) {

               Common.dismiss();
               if(response.isSuccessful()){
                   AppPreference.getInstance().set(mContext,AppPreference.part_serial,serialNo);
                   AppPreference.getInstance().set(mContext,AppPreference.sequenceNumber,response.body().getSequenceNumber());
                   setValue(response.body());

               }
               else {
                   if(response.code()==404){

                       SerailError message = new Gson().fromJson(response.errorBody().charStream(), SerailError.class);
                       CustomeToast customeToast=new CustomeToast();
                       customeToast.showErrorToast(mContext,message.getMessage());
                   }

                 //  System.out.println("validation2 "+response.toString());
               }
           }

           @Override
           public void onFailure(Call<SerialNovalidation> call, Throwable t) {
               Common.dismiss();
               CustomeToast customeToast=new CustomeToast();
               customeToast.showErrorToast(mContext,t.getMessage());
           }
       });

        //apiServices.startSerialNo()

    }

    public void recieve(Context context,String token,String serialno){
        Common.showdialogue(context);
        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        Call call=apiServices.recive(token,serialno);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                Common.dismiss();
                statusCode.postValue(response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Common.dismiss();
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(context,t.getMessage());
            }
        });

    }

    public void complete(Context context,String token,String serialno){
        Common.showdialogue(context);
        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        Call call=apiServices.complete(token,serialno);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                Common.dismiss();

                statusCode.postValue(response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Common.dismiss();
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(context,t.getMessage());
            }
        });

    }


}
