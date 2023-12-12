package com.tatamotors.errorproofing.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.networkcall.ApiInterface;
import com.tatamotors.errorproofing.networkcall.RetroInstance;
import com.tatamotors.errorproofing.requests.SkipRequest;
import com.tatamotors.errorproofing.requests.UpdateValidation;
import com.tatamotors.errorproofing.requests.ValidationRequest;
import com.tatamotors.errorproofing.response.Error;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.ValidationResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartViewModel extends ViewModel {

    public MutableLiveData<Integer> sequence_no;
    public MutableLiveData<Response> statusCode;
    public MutableLiveData<ValidationResponse> responseMutableLiveData;
    public MutableLiveData<SerialNovalidation> partResponse;
   // public MutableLiveData<SerialNovalidation> partResponse1;
    public MutableLiveData<PartError> partErrorMutableLiveData;

    public MutableLiveData<Error> errorMutableLiveData;

    public MutableLiveData<Integer> getSequence_no() {

        if(sequence_no==null){
            sequence_no=new MutableLiveData<>();
        }
        return sequence_no;
    }

    public MutableLiveData<Response> getStatusCode() {
        if(statusCode==null){
            statusCode=new MutableLiveData<>();
        }
        return statusCode;
    }

    public MutableLiveData<SerialNovalidation> getPartResponse() {

        if(partResponse==null){
            partResponse=new MutableLiveData<>();
        }
        return partResponse;
    }


    public MutableLiveData<PartError> getErrorMutableLiveData() {
        if(partErrorMutableLiveData==null){
            partErrorMutableLiveData=new MutableLiveData<>();
        }
        return partErrorMutableLiveData;
    }

    public MutableLiveData<ValidationResponse> getResponseMutableLiveData() {
        if(responseMutableLiveData==null){
            responseMutableLiveData=new MutableLiveData<>();
        }
        return responseMutableLiveData;
    }

    public void setSequence_no(int value){
        sequence_no.postValue(value);
    }
    public  void setPartResponse(SerialNovalidation value){
        partResponse.postValue(value);
    }

    public void setResponseMutableLiveData(ValidationResponse validationResponse){
        responseMutableLiveData.postValue(validationResponse);
    }
    public void setPartErrorMutableLiveData(PartError partError){
        partErrorMutableLiveData.postValue(partError);
    }

    public void setErrorMutableLiveData(Error error){
        errorMutableLiveData.postValue(error);
    }


    public void validatePartNo(Context context,String token,String serialno,Integer familyId, String scanvalue,String partNo){
        Common.showdialogue(context);
        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        Call<SerialNovalidation> call=apiServices.validateSerialno(token,serialno,new ValidationRequest(familyId,scanvalue,partNo,""));
        call.enqueue(new Callback<SerialNovalidation>() {
            @Override
            public void onResponse(Call<SerialNovalidation> call, @NonNull Response<SerialNovalidation> response) {
                Common.dismiss();
                if(response.code()== AppPreference.success_response){
                    partResponse.postValue(response.body());
                }
                else {
                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
                    setPartErrorMutableLiveData(message);
                }

            }

            @Override
            public void onFailure(Call<SerialNovalidation> call, Throwable t) {
                Common.dismiss();
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(context,t.getMessage());

            }
        });

    }

    public void bookSerial(String token,String serialno){
        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        Call call=apiServices.book(token,serialno);
       call.enqueue(new Callback() {
           @Override
           public void onResponse(Call call, Response response) {
               statusCode.postValue(response);
           }

           @Override
           public void onFailure(Call call, Throwable t) {

           }
       });

    }


//    public void validatePartNoDromEdit(String token,String serialno,Integer familyId, String scanvalue,String partNo){
//        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
//        Call<Integer> call=apiServices.validateSerialno(token,serialno,new ValidationRequest(familyId,scanvalue,partNo));
//        call.enqueue(new Callback<Integer>() {
//            @Override
//            public void onResponse(Call<Integer> call, @NonNull Response<Integer> response) {
//                if(response.code()== AppPreference.success_response){
//                    setPartResponse(String.valueOf(response.body()));
//                }
//                else {
//                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
//                    setPartErrorMutableLiveData(message);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Integer> call, Throwable t) {
//
//
//            }
//        });
//
//    }



    public void updateValidation(String token,String serialno,String id,Integer familyId, String scanvalue,String partNo){
        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        Call<SerialNovalidation> call=apiServices.updateValidation(token,serialno,id,new UpdateValidation(familyId,scanvalue,partNo));
        call.enqueue(new Callback<SerialNovalidation>() {
            @Override
            public void onResponse(Call<SerialNovalidation> call, @NonNull Response<SerialNovalidation> response) {
                if(response.code()== AppPreference.success_response){

                    setPartResponse(response.body());
                }
                else {
                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
                    setPartErrorMutableLiveData(message);
                }

            }

            @Override
            public void onFailure(Call<SerialNovalidation> call, Throwable t) {


            }
        });

    }

    public void skipPartNo(Context context, String token, String serialno, SkipRequest skipRequest){
        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        Call<SerialNovalidation> call=apiServices.skipPartNo(token,serialno,skipRequest);

        call.enqueue(new Callback<SerialNovalidation>() {
            @Override
            public void onResponse(Call<SerialNovalidation> call, Response<SerialNovalidation> response) {

                if(response.code()== AppPreference.success_response)
                {
                    if(response.isSuccessful()){

                        setPartResponse(response.body());
                    }

                }
                else {

                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
                    setPartErrorMutableLiveData(message);
                    CustomeToast customeToast=new CustomeToast();
                    customeToast.showErrorToast(context,message.getMessage());

                }

            }

            @Override
            public void onFailure(Call<SerialNovalidation> call, Throwable t) {
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(context,t.toString());
            }
        });

    }

}
