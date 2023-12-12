package com.tatamotors.errorproofing.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tatamotors.errorproofing.model.LoginModel;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginModel> loginModelLiveData;

    public LoginViewModel() {
        loginModelLiveData=new MutableLiveData<>();
    }

    public MutableLiveData<LoginModel> getUser() {

        if (loginModelLiveData == null) {
            loginModelLiveData = new MutableLiveData<>();
        }
        return loginModelLiveData;

    }

    public void setEditValue(LoginModel value){
        loginModelLiveData.postValue(value);

    }





}
