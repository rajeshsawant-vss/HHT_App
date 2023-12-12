package com.tatamotors.errorproofing;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tatamotors.errorproofing.activities.Login;
import com.tatamotors.errorproofing.activities.PartValidation;
import com.tatamotors.errorproofing.activities.ValidationStatus;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.ActivityLoginBinding;
import com.tatamotors.errorproofing.databinding.ActivityMainBinding;
import com.tatamotors.errorproofing.fragments.DrawerFragment;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.reciever.ScannerReciever;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.RecieveError;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.LoginViewModel;
import com.tatamotors.errorproofing.viewmodel.MainViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements ScanData {
    //
    String module;
    ActivityMainBinding mainBinding;
    String  booked_serial="";
    ScannerReciever scannerReciever=new ScannerReciever();
    List<PartResponse> partResponseList;
    HashMap<String,List<PartResponse>> listHashMap;
    String key;
    String partSerial_no;
    MainViewModel mainViewModel;
    boolean isCompleted=true;
    boolean ispartvalidationCompleted=true;
    String scanData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);
        mainViewModel= new ViewModelProvider(MainActivity.this).get(MainViewModel.class);


        if(AppPreference.getInstance().get(MainActivity.this,AppPreference.module).equalsIgnoreCase("booking")){

            mainBinding.textView.setText("Booking");
        }

        mainViewModel.getStatusCode().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(Response response) {

                if(response.code()==200)
                {
                    mainViewModel.setSerialNo(scanData);
                }
                else {
                    RecieveError message = new Gson().fromJson(response.errorBody().charStream(), RecieveError.class);
                    CustomeToast customeToast=new CustomeToast();
                    customeToast.showErrorToast(getApplicationContext(),message.getMessage());
                }
            }
        });
        mainViewModel.getSerialLiveData().observe(this, new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation serialNovalidation) {

                if(AppPreference.getInstance().get(MainActivity.this,AppPreference.module).equalsIgnoreCase("booking"))
                {
                    validation(serialNovalidation);
                }
                else {
                    partvalidation(serialNovalidation);
                }


            }
        });

        mainViewModel.getSerialNo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mainViewModel.getPartNos(MainActivity.this,s);
            }
        });

        mainBinding.include.imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DrawerFragment newFragment = DrawerFragment.newInstance();
                newFragment.show(ft, "dialog");
            }
        });
        mainBinding.editSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>4){
                    mainBinding.image.setVisibility(View.VISIBLE);
                }
                else {
                    mainBinding.image.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
//
            }
        });

        mainBinding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //

                if(mainBinding.editSerial.getText().toString().equalsIgnoreCase(booked_serial)){
                    Intent i=new Intent(MainActivity.this, ValidationStatus.class);
                    PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_report);
                    i.putExtra("part_info",partModel);
                    startActivity(i);
                    finish();
                }else {

                    if(Common.isInternetConnected(MainActivity.this)){
                        scanData=mainBinding.editSerial.getText().toString();
                        mainViewModel.recieve(MainActivity.this,AppPreference.getInstance().get(getApplicationContext(),AppPreference.token),scanData);

                    }
                    else {
                        CustomeToast customeToast=new CustomeToast();
                        customeToast.showErrorToast(MainActivity.this,getString(R.string.internet_error));
                    }


                }

//                scanData=mainBinding.editSerial.getText().toString();
//                mainViewModel.recieve(AppPreference.getInstance().get(getApplicationContext(),AppPreference.token),scanData);


//                if(AppPreference.getInstance().get(MainActivity.this,AppPreference.module).equalsIgnoreCase("booking"))
//                {
//                    mainViewModel.setSerialNo(mainBinding.editSerial.getText().toString());
//                }
//                else {
//                    scanData=mainBinding.editSerial.getText().toString();
//                    mainViewModel.recieve(AppPreference.getInstance().get(getApplicationContext(),AppPreference.token),scanData);
//
//                }


               // mainViewModel.setSerialNo(mainBinding.editSerial.getText().toString());


            }
        });
        mainBinding.txtLable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction("com.symbol.datawedge.api.ACTION");
                i.putExtra("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER","TOGGLE_SCANNING");
                sendBroadcast(i);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        partResponseList=new ArrayList<>();
        module=AppPreference.getInstance().get(getApplicationContext(),AppPreference.module);
        booked_serial=AppPreference.getInstance().get(getApplicationContext(),AppPreference.booked_serial);
        mainBinding.include.textStation.setText("Dharwad/FES/"+AppPreference.getInstance().get(getApplicationContext(),AppPreference.station));
        System.out.println("mymoduleid "+module);

        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(scannerReciever, filter);
    }

    @Override
    public void getScanData(String data) {

        if(data.equalsIgnoreCase(booked_serial)){

            Intent i=new Intent(MainActivity.this, ValidationStatus.class);
            PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_report);
            i.putExtra("part_info",partModel);
            startActivity(i);
            finish();
        }else {

            if(Common.isInternetConnected(MainActivity.this)){
                scanData=Common.replaceString(data);
                mainViewModel.recieve(MainActivity.this,AppPreference.getInstance().get(getApplicationContext(),AppPreference.token),scanData);

            }
            else {
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(MainActivity.this,getString(R.string.internet_error));
            }

        }


//        if(AppPreference.getInstance().get(MainActivity.this,AppPreference.module).equalsIgnoreCase("booking"))
//        {
//            mainViewModel.setSerialNo(data);
//        }
//        else {
//            scanData=data;
//            mainViewModel.recieve(AppPreference.getInstance().get(getApplicationContext(),AppPreference.token),data);
//
//        }

    // mainBinding.editSerial.setText(data);
    //    mainViewModel.setSerialNo(data);

        //  setSerial(data);
    }



    private void  partvalidation(SerialNovalidation serialNovalidation){
        List<ChildPartsAndFamily> familyList=serialNovalidation.getChildPartsAndFamily();

        for(int a=0;a<familyList.size();a++){
            if(familyList.get(a).getQuantity()>1){
                if(familyList.get(a).getValidations().size()<familyList.get(a).getQuantity()){
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,familyList.get(a).getValidations().size());
                    break;
                }
            }
            else {
                if(familyList.get(a).getValidations().isEmpty()){
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,0);
                    break;
                }
            }

        }

        for(int a=0;a<familyList.size(); a++){

            if(familyList.get(a).getQuantity()>1){
                if(familyList.get(a).getQuantity()>familyList.get(a).getValidations().size()){
                    ispartvalidationCompleted=false;
                    break;
                }
            }
            else {
                if(familyList.get(a).getValidations().isEmpty()){
                    ispartvalidationCompleted=false;
                    break;
                }
            }
        }

        if(ispartvalidationCompleted){
            Intent i=new Intent(MainActivity.this, ValidationStatus.class);
            PartModel partModel=new PartModel("",0,0,0,"",AppPreference.complete);
            i.putExtra("part_info",partModel);
            startActivity(i);
            finish();

        }
        else {
            AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_response,serialNovalidation);
            Intent i=new Intent(MainActivity.this,PartValidation.class);
            startActivity(i);
            finish();
        }
    }

    private  void validation(SerialNovalidation serialNovalidation){
        List<ChildPartsAndFamily> familyList=serialNovalidation.getChildPartsAndFamily();

        for(int a=0;a<familyList.size();a++){
            if(familyList.get(a).getQuantity()>1){
                if(familyList.get(a).getValidations().size()<familyList.get(a).getQuantity()){
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,familyList.get(a).getValidations().size());
                    break;
                }
            }
            else {
                if(familyList.get(a).getValidations().isEmpty()){
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,0);
                    break;
                }
            }

        }




        for(int a=0;a<familyList.size();a++){

            if(familyList.get(a).getQuantity()>1){
                if(familyList.get(a).getValidations().size()<familyList.get(a).getQuantity())
                {


                    isCompleted=false;
                    break;
                }
                else {
                    for(Validation validation:familyList.get(a).getValidations()){

                        if(validation.getDerivedPartNumber()==null){
                            isCompleted=false;
                            break;
                        }
                    }


                }

            }
            else {
                if(familyList.get(a).getValidations().isEmpty()){
                    System.out.println("reaching here 2 "+a);
                    isCompleted=false;
                    break;
                }
                else {
                    System.out.println("reaching here 3 "+a);
//                    for(Validation validation:familyList.get(a).getValidations()){
//
//                        if(validation.getDerivedPartNumber()==null){
//                            isCompleted=false;
//                            break;
//                        }
//                    }

                    if(familyList.get(a).getValidations().get(0).getDerivedPartNumber()==null){
                        isCompleted=false;
                        break;
                    }
                }
            }


        }

        if(AppPreference.getInstance().get(MainActivity.this,AppPreference.module).equalsIgnoreCase("booking"))
        {
            AppPreference.getInstance().set(MainActivity.this,AppPreference.isPostion,-1);

            AppPreference.getInstance().set(getApplicationContext(),AppPreference.sequenceNumber,serialNovalidation.getSequenceNumber());

            if(isCompleted){
                Intent i=new Intent(MainActivity.this, ValidationStatus.class);
                PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_complete);
                i.putExtra("part_info",partModel);
                startActivity(i);
                finish();

            }
            else {
                AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_response,serialNovalidation);
                Intent i=new Intent(MainActivity.this, ValidationStatus.class);
                PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_pending);
                i.putExtra("part_info",partModel);
                startActivity(i);
                finish();
            }

        }
//        else {
//            if(isCompleted){
//                Intent i=new Intent(MainActivity.this, ValidationStatus.class);
//                PartModel partModel=new PartModel("",0,0,0,"",AppPreference.complete);
//                i.putExtra("part_info",partModel);
//                startActivity(i);
//                finish();
//
//            }
//            else {
//                AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_response,serialNovalidation);
//                Intent i=new Intent(MainActivity.this,PartValidation.class);
//                startActivity(i);
//                finish();
//            }
//        }




    }




    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(scannerReciever);
    }
}