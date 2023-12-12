package com.tatamotors.errorproofing.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.networkcall.ApiInterface;
import com.tatamotors.errorproofing.networkcall.RetroInstance;
import com.tatamotors.errorproofing.requests.SkipRequest;
import com.tatamotors.errorproofing.response.ValidationResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkipViewModel extends ViewModel {

    public MutableLiveData<ValidationResponse> responseMutableLiveData;

    public MutableLiveData<ValidationResponse> getResponseMutableLiveData() {
        if(responseMutableLiveData==null){
            responseMutableLiveData=new MutableLiveData<>();
        }
        return responseMutableLiveData;
    }

    public void setResponseMutableLiveData(ValidationResponse validationResponse){
        responseMutableLiveData.postValue(validationResponse);
    }

//    public void skipPartNo(Context context, String token, String serialno, SkipRequest skipRequest){
//        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
//        Call<ValidationResponse> call=apiServices.skipPartNo(token,serialno,skipRequest);
//
//        call.enqueue(new Callback<ValidationResponse>() {
//            @Override
//            public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {
//
//                if(response.code()== AppPreference.success_response)
//                {
//                    if(response.isSuccessful()){
//                        setResponseMutableLiveData(response.body());
//                    }
//
//                }
//                else {
//                    CustomeToast customeToast=new CustomeToast();
//                    customeToast.showErrorToast(context,response.errorBody().toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ValidationResponse> call, Throwable t) {
//                CustomeToast customeToast=new CustomeToast();
//                customeToast.showErrorToast(context,t.toString());
//            }
//        });
//
//    }

}
