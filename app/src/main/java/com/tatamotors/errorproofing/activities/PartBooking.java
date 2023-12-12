package com.tatamotors.errorproofing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.ActivityPartBookingBinding;
import com.tatamotors.errorproofing.databinding.ActivityPartValidationBinding;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.reciever.ScannerReciever;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.MainViewModel;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.HashMap;
import java.util.List;

public class PartBooking extends AppCompatActivity implements ScanData {


    ActivityPartBookingBinding binding;

    String partFamily;
    String partNo;

    int part_sub_serial;
    int part_book_sub_serial;
    boolean isreport=false;
    ChildPartsAndFamily childPartsAndFamily;
    SerialNovalidation serialNovalidation;
    int familyId, partSize;
    boolean isCompleted=true;
    PartViewModel partViewModel;
    String scannValue;
    MainViewModel mainViewModel;
    String serialNo;
    ScannerReciever scannerReciever;
    String prevScannValue="";
    String msg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartBookingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.include2.imgDrawer.setVisibility(View.GONE);

        partViewModel=new ViewModelProvider(PartBooking.this).get(PartViewModel.class);
        mainViewModel= new ViewModelProvider(PartBooking.this).get(MainViewModel.class);
        mainViewModel.getSerialLiveData().observe(this, new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation serialNovalidation) {

                getViewModelStore().clear();
                System.out.println("");
                validation(serialNovalidation);


            }
        });

        binding.editPartNo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown( binding.editPartNo.getRootView())) {

                } else {
                    binding.editPartNo.clearFocus();

                }
            }
        });


        partViewModel.getPartResponse().observe(PartBooking.this, new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation value) {
                getViewModelStore().clear();
                CustomeToast customeToast=new CustomeToast();
                customeToast.showSuccessToast(getApplicationContext(),getString(R.string.partmatch));
                binding.editPartNo.clearFocus();
                mainViewModel.getPartNos(PartBooking.this,serialNo);
//                if(childPartsAndFamily.getQuantity()>1){
//
//                    if(childPartsAndFamily.getQuantity()>part_book_sub_serial){
//                        part_book_sub_serial++;
//                        getData(childPartsAndFamily);
//                    }
//                    else {
//                        mainViewModel.getPartNos(PartBooking.this,serialNo);
//                    }
//                }
//                else {
//                    mainViewModel.getPartNos(PartBooking.this,serialNo);
//                }
            }
        });

        partViewModel.getErrorMutableLiveData().observe(PartBooking.this, new Observer<PartError>() {
            @Override
            public void onChanged(PartError partError) {
                getViewModelStore().clear();
//                CustomeToast customeToast=new CustomeToast();
//                customeToast.showErrorToast(getApplicationContext(),partError.getMessage());
                msg=partError.getMessage();
                movetoStatus(AppPreference.skip_booking);
            }
        });





        isCompleted=true;
        serialNo=AppPreference.getInstance().get(getApplicationContext(),AppPreference.part_serial);
        childPartsAndFamily= AppPreference.getInstance().getChildFamilyList(getApplicationContext(),AppPreference.child_part_family);
        serialNovalidation=  AppPreference.getInstance().getList(getApplicationContext(),AppPreference.part_response);;
        part_book_sub_serial=AppPreference.getInstance().getInt(getApplicationContext(),AppPreference.booking_serial);
        getData(childPartsAndFamily);

        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDataFromedit(binding.editPartNo.getText().toString());
                binding.editPartNo.setText("");
                binding.editPartNo.clearFocus();
            }
        });


        binding.editPartNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=6){

                    binding.image.setVisibility(View.VISIBLE);

                }
                else {
                    binding.image.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isreport=false;
        scannerReciever=new ScannerReciever();
        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(scannerReciever, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getViewModelStore().clear();
        unregisterReceiver(scannerReciever);
    }

    @Override
    public void getScanData(String data) {

        if(Common.isInternetConnected(PartBooking.this)){
            validateData(data);

        }
        else {
            CustomeToast customeToast=new CustomeToast();
            customeToast.showErrorToast(PartBooking.this,getString(R.string.internet_error));
        }
    }

    private void validateData(String data){
        scannValue=data;
        String token=AppPreference.getInstance().get(PartBooking.this,AppPreference.token);

//        if(prevScannValue==null||prevScannValue.equalsIgnoreCase("")){
//           partViewModel.validatePartNo(PartBooking.this,token,serialNovalidation.getSequenceNumber(),familyId,data,partNo);
//        }
//        else {
//            partViewModel.updateValidation(token,serialNovalidation.getSequenceNumber(),serialNovalidation.getChildPartsAndFamily().get(0).getValidations().get(0).getId(),familyId,data,partNo);
//        }


        if(childPartsAndFamily.getValidations().isEmpty()){
            partViewModel.validatePartNo(this,token,AppPreference.getInstance().get(this,AppPreference.sequenceNumber),familyId,data,partNo);

        }
        else {
            partViewModel.updateValidation(token,AppPreference.getInstance().get(this,AppPreference.sequenceNumber),childPartsAndFamily.getValidations().get(0).getId(),familyId,data,partNo);

        }

    }

    private void validateDataFromedit(String data){
        scannValue=data;
        String token=AppPreference.getInstance().get(PartBooking.this,AppPreference.token);

//        if(prevScannValue==null||prevScannValue.equalsIgnoreCase("")){
//            partViewModel.validatePartNo(PartBooking.this,token,serialNovalidation.getSequenceNumber(),familyId,data,partNo);
//        }
//        else {
//            partViewModel.updateValidation(token,serialNovalidation.getSequenceNumber(),serialNovalidation.getChildPartsAndFamily().get(0).getValidations().get(0).getId(),familyId,data,partNo);
//        }


        if(childPartsAndFamily.getValidations().isEmpty()){
            partViewModel.validatePartNo(this,token,AppPreference.getInstance().get(this,AppPreference.sequenceNumber),familyId,data,partNo);

        }
        else {
            partViewModel.updateValidation(token,AppPreference.getInstance().get(this,AppPreference.sequenceNumber),childPartsAndFamily.getValidations().get(0).getId(),familyId,data,partNo);

        }

    }

    private void movetoStatus(String tag){
//        if(tag.equalsIgnoreCase(AppPreference.complete)){
//            AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,0);
//            AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,0);
//        }

        Intent i=new Intent(PartBooking.this, ValidationStatus.class);
        PartModel partModel=new PartModel(partNo,0,partSize,partFamily,familyId,tag,scannValue,part_book_sub_serial,childPartsAndFamily.getQuantity(),msg);
        i.putExtra("part_info",partModel);
        startActivity(i);
        scannValue="";


    }

    private void getData(ChildPartsAndFamily childPartsAndFamily){

        familyId=childPartsAndFamily.getFamilyId();
        partFamily=childPartsAndFamily.getFamilyDisplayName();
        partNo=childPartsAndFamily.getPartNumber();
        partSize=childPartsAndFamily.getValidations().size();

        binding.txtSerailNO.setText(serialNo);
        binding.txtDescription.setText(serialNovalidation.getParentPartDescription());
        binding.textPartNo1.setText(serialNovalidation.getParentPartNumber());
        binding.partNo.setText(partNo);
        binding.textFamily.setText(partFamily);


        if(childPartsAndFamily.getQuantity()>1){
         //   binding.lnrSubQuan.setVisibility(View.VISIBLE);
           // binding.txtcurrentpart.setText(String.valueOf(part_book_sub_serial));
            //binding.txttotalPart.setText(String.valueOf(childPartsAndFamily.getQuantity()));
            prevScannValue=childPartsAndFamily.getValidations().get(part_book_sub_serial).getScannedValue();

        }
        else {
            if(!childPartsAndFamily.getValidations().isEmpty()){
                prevScannValue=childPartsAndFamily.getValidations().get(0).getScannedValue();

            }
          //  binding.lnrSubQuan.setVisibility(View.GONE);
        }
    }

    private  void validation(SerialNovalidation serialNovalidation){
        List<ChildPartsAndFamily> familyList=serialNovalidation.getChildPartsAndFamily();

        for(int a=0;a<familyList.size();a++){

            if(familyList.get(a).getQuantity()>1){
                if(familyList.get(a).getValidations().size()<familyList.get(a).getQuantity())
                {
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,familyList.get(a).getValidations().size());
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
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getApplicationContext(),AppPreference.sub_part_serial_no,0);
                    isCompleted=false;
                    break;
                }
                else {

                    if(familyList.get(a).getValidations().get(0).getDerivedPartNumber()==null){
                        isCompleted=false;
                        break;
                    }
                }
            }


        }
        AppPreference.getInstance().set(getApplicationContext(),AppPreference.sequenceNumber,serialNovalidation.getSequenceNumber());

        if(isCompleted){
            Intent i=new Intent(getApplicationContext(), ValidationStatus.class);
            PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_complete);
            i.putExtra("part_info",partModel);
            startActivity(i);
            finish();

        }
        else {
            AppPreference.getInstance().set(getApplicationContext(),AppPreference.part_response,serialNovalidation);
            Intent i=new Intent(getApplicationContext(), ValidationStatus.class);
            PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_pending_validation);
            i.putExtra("part_info",partModel);
            startActivity(i);
            finish();
        }





    }


    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }



}