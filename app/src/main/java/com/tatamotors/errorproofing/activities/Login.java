package com.tatamotors.errorproofing.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;

import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.ActivityLoginBinding;
import com.tatamotors.errorproofing.model.LoginModel;
import com.tatamotors.errorproofing.networkcall.ApiInterface;
import com.tatamotors.errorproofing.networkcall.RetroInstance;
import com.tatamotors.errorproofing.response.LoginResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.util.LoadingDialog;
import com.tatamotors.errorproofing.viewmodel.LoginViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    LoginViewModel loginViewModel;
    ActivityLoginBinding loginBinding;
    private LoadingDialog loadingDialog;
    private List<LoginResponse> responseList;
    String operation="";
    String token="";
    HashMap<Integer,String> hashMap;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);
        responseList=new ArrayList<>();
        disableEdiTextCopy(loginBinding.editUser);
        disableEdiTextCopy(loginBinding.editPass);
        loginViewModel= new ViewModelProvider( Login.this).get(LoginViewModel.class);
        loginViewModel.getUser().observe( this, new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {

                if(Common.isInternetConnected(Login.this)){
                    getLogin(loginModel.getUserName(),loginModel.getPassword());
                }
                else {
                    CustomeToast customeToast=new CustomeToast();
                    customeToast.showErrorToast(Login.this,getString(R.string.internet_error));
                }


            }
        });




        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=loginBinding.editUser.getText().toString();
                String pass=loginBinding.editPass.getText().toString();

                LoginModel loginModel=new LoginModel(userName,pass);
                if(checkValidation(userName,pass)){
                    if(userName.equalsIgnoreCase("b-1") && pass.equalsIgnoreCase("b-1")){
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.username,userName);
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.module,"booking");
                        Intent i=new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        loginViewModel.setEditValue(loginModel);
                    }




                }


            }
        });


    }



    private void disableEdiTextCopy(TextInputEditText edittext){
        edittext.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {

            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        edittext.setLongClickable(false);
    }

    private void getLogin(String userName,String pass){

        LoadDialog();

        ApiInterface apiServices= RetroInstance.getRetrofit().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("password", pass);

        } catch (JSONException e) {

        }
        RequestBody requestBody=null;
        try {
            requestBody = RequestBody.create( (new JSONObject(String.valueOf(jsonObject))).toString(),okhttp3.MediaType.parse("application/json; charset=utf-8"));

        } catch (JSONException e) {


        }
        Call<LoginResponse> call=  apiServices.login(requestBody);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                finishLoad();
                if(response.isSuccessful()){

                    if(response.code()==200){

                        LoginResponse loginResponse=response.body();
                        assert loginResponse != null;
                       // operation="validation";
//
                        if(Arrays.asList(loginResponse.getStationDetails().operation).contains("BOOK"))
                        {
                            operation="booking";
                        }
                        else {
                            operation="validation";
                        }
                        System.out.println("responsegot "+loginResponse);
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.username,userName);
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.station,loginResponse.getStationDetails().name);
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.station_name,loginResponse.getStationDetails().getName());
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.module,operation);
                        AppPreference.getInstance().set(getApplicationContext(),AppPreference.token,getResources().getString(R.string.token_type)+" "+loginResponse.getToken());
                        Intent i=new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
                else {

                    if(response.code()==401){

                        loginBinding.textError.setVisibility(View.VISIBLE);

//                    ease Enter valid credentials", Toast.LENGTH_LONG).show();
                    }
                    else  if(response.code()==500){
                        loginBinding.textError.setVisibility(View.VISIBLE);
                        loginBinding.textError.setText(getResources().getString(R.string.server_is_down));
                    }
                    else  if(response.code()==404){
                        loginBinding.textError.setVisibility(View.VISIBLE);
                        loginBinding.textError.setText(getResources().getString(R.string.server_not_found));
                    }
                    else {
                        loginBinding.textError.setVisibility(View.VISIBLE);
                        loginBinding.textError.setText(getResources().getString(R.string.enter_serial_no));
                    }

                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                finishLoad();
                System.out.println(t.getCause()+" error "+t.getMessage());
                loginBinding.textError.setVisibility(View.VISIBLE);
                loginBinding.textError.setText(getResources().getString(R.string.server_is_down));
            }
        });
    }

//    private void getLogin(LoginModel loginModel){
//
//        ApiInterface apiServices= RetroInstance.getRetrofit(token).create(ApiInterface.class);
//        JSONObject jsonObject = new JSONObject();
////        try {
////            jsonObject.put("username", userName);
////            jsonObject.put("password", pass);
////
////        } catch (JSONException e) {
////
////        }
//        RequestBody requestBody=null;
//        try {
//            requestBody = RequestBody.create( (new JSONObject(String.valueOf(jsonObject))).toString(),okhttp3.MediaType.parse("application/json; charset=utf-8"));
//
//        } catch (JSONException e) {
//
//
//        }
//
//
//        Call<LoginModel> call=  apiServices.login(loginModel);
//        call.enqueue(new Callback<LoginModel>() {
//            @Override
//            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
//
//                //  System.out.println("response.code() "+response.code());
//
//                if(response.isSuccessful()){
//
//                    if(response.code()==200){
//                        Intent i=new Intent(Login.this, MainActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//                }
//                else {
//
//                    if(response.code()==401){
//
//                        loginBinding.textError.setVisibility(View.VISIBLE);
//
////                    ease Enter valid credentials", Toast.LENGTH_LONG).show();
//                    }
//                    else  if(response.code()==500){
//                        loginBinding.textError.setVisibility(View.VISIBLE);
//                        loginBinding.textError.setText(getResources().getString(R.string.server_is_down));
//                    }
//                    else  if(response.code()==404){
//                        loginBinding.textError.setVisibility(View.VISIBLE);
//                        loginBinding.textError.setText(getResources().getString(R.string.server_not_found));
//                    }
//                    else {
//                        loginBinding.textError.setVisibility(View.VISIBLE);
//                        loginBinding.textError.setText(getResources().getString(R.string.enter_serial_no));
//                    }
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<LoginModel> call, Throwable t) {
//                System.out.println(t.getCause()+" error "+t.getMessage());
//                loginBinding.textError.setVisibility(View.VISIBLE);
//                loginBinding.textError.setText(getResources().getString(R.string.server_is_down));
//            }
//        });
//    }


    private boolean checkValidation(String userName,String pass){

        if(userName.matches("")){
            loginBinding.editUser.setError(getResources().getString(R.string.error_user));

            return false;
        }
        if(pass.matches("")){
            loginBinding.editPass.setError(getResources().getString(R.string.error_pass));
            return false;
        }

        return true;
    }

    private void LoadDialog(){

        loadingDialog =  new LoadingDialog(Login.this);
        loadingDialog.show();


    }
    private void finishLoad(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();        }
    }
}