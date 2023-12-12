package com.tatamotors.errorproofing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.ActivityLoginBinding;
import com.tatamotors.errorproofing.databinding.ActivityPartValidationBinding;
import com.tatamotors.errorproofing.fragments.DrawerFragment;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.reciever.ScannerReciever;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.Error;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.response.ValidationResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PartValidation extends AppCompatActivity implements ScanData {

//    List<PartResponse> partResponseList;
  private static Random rnd = new Random();


    ActivityPartValidationBinding partValidationBinding;
//    int seriel_no=0;
    int familyId;
    String partFamily;
    String partNo;
    HashMap<String,List<PartResponse>> listHashMap;
    String partSerial_no;
    String sequenceNumber;
    int partSize;
//    String key;
    SerialNovalidation serialNovalidation;
    int serial_part_no=0;
    PartViewModel partViewModel;
    String scannValue;
    int part_sub_serial=0;
    ScannerReciever scannerReciever;
    String msg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partValidationBinding = ActivityPartValidationBinding.inflate(getLayoutInflater());
        View view = partValidationBinding.getRoot();
        setContentView(view);
        partViewModel=new ViewModelProvider(PartValidation.this).get(PartViewModel.class);
        observdata();

        partValidationBinding.include2.imgDrawer.setVisibility(View.INVISIBLE);
        partValidationBinding.editPartNo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown( partValidationBinding.editPartNo.getRootView())) {

                } else {
                   // unregisterObserver();
                  //  observdata();
                    partValidationBinding.editPartNo.clearFocus();

                }
            }
        });


        partValidationBinding.include2.imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DrawerFragment newFragment = DrawerFragment.newInstance();
                newFragment.show(ft, "dialog");
            }
        });
        partValidationBinding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction("com.symbol.datawedge.api.ACTION");
                i.putExtra("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER","TOGGLE_SCANNING");
                sendBroadcast(i);
            }
        });


        partValidationBinding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.isInternetConnected(PartValidation.this)){
                    validateDataFromedit(partValidationBinding.editPartNo.getText().toString());
                    partValidationBinding.editPartNo.setText("");
                    partValidationBinding.editPartNo.clearFocus();
                    hideSoftKeyboard();
                }
                else {
                    CustomeToast customeToast=new CustomeToast();
                    customeToast.showErrorToast(PartValidation.this,"PLEASE CHECK INTERNET CONNECTION");
                }
            }
        });
        partValidationBinding.editPartNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=6){

                    partValidationBinding.image.setVisibility(View.VISIBLE);

                }
                else {
                    partValidationBinding.image.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        init();

    }


    @Override
    protected void onStop() {
        super.onStop();
        getViewModelStore().clear();
//        partViewModel.getPartResponse().removeObservers(PartValidation.this);
//        partViewModel.getErrorMutableLiveData().removeObservers(PartValidation.this);
        unregisterReceiver(scannerReciever);
    }

    @Override
    public void getScanData(String data) {

        if(Common.isInternetConnected(PartValidation.this)){
            validateData(Common.replaceString(data));
        }
        else {
            CustomeToast customeToast=new CustomeToast();
            customeToast.showErrorToast(PartValidation.this,getString(R.string.internet_error));
        }


    }



    private void init(){


        serial_part_no=AppPreference.getInstance().getInt(getApplicationContext(),AppPreference.part_serial_no);
        part_sub_serial=AppPreference.getInstance().getSubInt(getApplicationContext(),AppPreference.sub_part_serial_no);
        partSerial_no=AppPreference.getInstance().get(getApplicationContext(),AppPreference.part_serial);
        partValidationBinding.txtSerailNO.setText(partSerial_no);
        serialNovalidation=AppPreference.getInstance().getList(getApplicationContext(),AppPreference.part_response);
        partValidationBinding.textPartNo1.setText(serialNovalidation.getParentPartNumber());
        partValidationBinding.txtDescription.setText(serialNovalidation.getParentPartDescription());
        sequenceNumber=serialNovalidation.getSequenceNumber();
        System.out.println("here is part no"+serial_part_no);
        getDatafromList(serial_part_no,part_sub_serial);
    }

    private void getDatafromList(int serial,int sub_serial ){

        System.out.println("here is part2"+serial);

        ChildPartsAndFamily childPartsAndFamily=serialNovalidation.getChildPartsAndFamily().get(serial);

        familyId=childPartsAndFamily.getFamilyId();
        partFamily=childPartsAndFamily.getFamilyDisplayName();
        partNo=childPartsAndFamily.getPartNumber();
        partSize=serialNovalidation.getChildPartsAndFamily().size();
        partValidationBinding.textFamily.setText(partFamily);
        partValidationBinding.partNo.setText(partNo);
        partValidationBinding.txtcurrent.setText(String.valueOf(serial+1));
        partValidationBinding.txttotal.setText(String.valueOf(partSize));

        if(childPartsAndFamily.getQuantity()>1){
            partValidationBinding.lnrSubQuan.setVisibility(View.VISIBLE);
            partValidationBinding.txtcurrentpart.setText(String.valueOf(sub_serial+1));
            partValidationBinding.txttotalPart.setText(String.valueOf(childPartsAndFamily.getQuantity()));

        }
        else {
            partValidationBinding.lnrSubQuan.setVisibility(View.GONE);
        }



    }

    private void movetoStatus(String tag){
        if(tag.equalsIgnoreCase(AppPreference.complete)){
            AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,0);
            AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,0);
        }
        ChildPartsAndFamily childPartsAndFamily=serialNovalidation.getChildPartsAndFamily().get(serial_part_no);
        scannValue="";
        Intent i=new Intent(PartValidation.this, ValidationStatus.class);
        PartModel partModel=new PartModel(partNo,serial_part_no,partSize,partFamily,familyId,tag,scannValue,part_sub_serial,childPartsAndFamily.getQuantity(),msg);
        i.putExtra("part_info",partModel);
        startActivity(i);
        finish();



    }

    private void validateData(String data){
        getViewModelStore().clear();
        scannValue=data;
        String token=AppPreference.getInstance().get(PartValidation.this,AppPreference.token);
        partViewModel.validatePartNo(PartValidation.this,token,sequenceNumber,familyId,data,partNo);

    }

    private void validateDataFromedit(String data){

        getViewModelStore().clear();
        scannValue=data;
        String token=AppPreference.getInstance().get(PartValidation.this,AppPreference.token);
        partViewModel.validatePartNo(PartValidation.this,token,sequenceNumber,familyId,data,partNo);


    }

    @Override
    protected void onResume() {
        super.onResume();



        scannerReciever=new ScannerReciever();
        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(scannerReciever, filter);

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
    private void observdata(){
        partViewModel.getPartResponse().observe(PartValidation.this, new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation value) {
                getViewModelStore().clear();
                partValidationBinding.editPartNo.setText("");
                partValidationBinding.editPartNo.clearFocus();
                CustomeToast customeToast=new CustomeToast();
                customeToast.showSuccessToast(getApplicationContext(),getString(R.string.partmatch));


                if(serialNovalidation.getChildPartsAndFamily().get(serial_part_no).getQuantity()>1){
                    if(serialNovalidation.getChildPartsAndFamily().get(serial_part_no).getQuantity()>
                            part_sub_serial+1)
                    {
                        part_sub_serial++;
                        getDatafromList(serial_part_no,part_sub_serial);
                        // getDatafromList(serial_part_no,part_sub_serial);
                    }
                    else {
                        if(serialNovalidation.getChildPartsAndFamily().size()==serial_part_no+1){
                            movetoStatus(AppPreference.complete);
                        }
                        else {
                            serial_part_no++;
                            getDatafromList(serial_part_no,part_sub_serial);
                        }
                    }


                }
                else {
                    if(serialNovalidation.getChildPartsAndFamily().size()==serial_part_no+1){
                        movetoStatus(AppPreference.complete);
                    }
                    else {
                        serial_part_no++;
                        getDatafromList(serial_part_no,part_sub_serial);
                    }
                }







            }
        });

        partViewModel.getErrorMutableLiveData().observe(PartValidation.this, new Observer<PartError>() {
            @Override
            public void onChanged(PartError partError) {
              //
               // unregisterObserver();
              //  CustomeToast customeToast=new CustomeToast();
               // customeToast.showErrorToast(getApplicationContext(),partError.getMessage());
                partValidationBinding.editPartNo.setText("");
                partValidationBinding.editPartNo.clearFocus();
                msg=partError.getMessage();
                movetoStatus(AppPreference.skip);
            }
        });

    }

    private void unregisterObserver(){
        partViewModel.getPartResponse().removeObservers((AppCompatActivity)PartValidation.this);
        partViewModel.getErrorMutableLiveData().removeObservers((AppCompatActivity)PartValidation.this);
    }
    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(partValidationBinding.mainLayout.getWindowToken(), 0);
    }


}