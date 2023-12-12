package com.tatamotors.errorproofing.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.PartBooking;
import com.tatamotors.errorproofing.activities.PartValidation;
import com.tatamotors.errorproofing.activities.ValidationStatus;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.LayoutRescanBinding;
import com.tatamotors.errorproofing.interfaces.FragmentListenr;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.reciever.ScannerReciever;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.ValidationResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FragmentRescan extends Fragment {

    LayoutRescanBinding binding;
    boolean isreport=false;
    FragmentListenr fragmentListenr;
    HashMap<String, List<PartResponse>> listHashMap;
    ValidationStatus validationStatus;
    PartViewModel partViewModel;
    String partSerial_no;
    int familyId;
    SerialNovalidation serialNovalidation;
    int serial_no;
    PartModel partModel;
    private static Random rnd = new Random();
    public FragmentRescan() {
    }


    public static FragmentRescan newInstance(PartModel partModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("part_info", partModel);
        FragmentRescan fragment = new FragmentRescan();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            partModel= (PartModel) bundle.getSerializable("part_info");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutRescanBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        partViewModel=new ViewModelProvider(getActivity()).get(PartViewModel.class);
        partSerial_no=AppPreference.getInstance().get(getActivity(),AppPreference.part_serial);
        serial_no=AppPreference.getInstance().getInt(getActivity(),AppPreference.part_serial_no);
        serialNovalidation=AppPreference.getInstance().getList(getActivity(),AppPreference.part_response);
        partViewModel.getPartResponse().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation s) {

                CustomeToast customeToast=new CustomeToast();
                customeToast.showSuccessToast(getActivity(),getString(R.string.partmatch));




                if(partModel.getSub_part_total()>1){

                    if(partModel.getSub_part_total()> partModel.getSub_part_serial()+1){
                        AppPreference.getInstance().set(getActivity(),AppPreference.sub_part_serial_no,partModel.getSub_part_serial()+1);
                        AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,partModel.getSerialNo());
                        Intent i=new Intent(getActivity(),PartValidation.class);
                        startActivity(i);

                    }
                    else {
                        if(serialNovalidation.getChildPartsAndFamily().size()==partModel.getSerialNo()+1){
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .add(R.id.fragment_container,FragComplete.class, null)
                                    .commit();
                        }
                        else {

                            serial_no++;
                            AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,partModel.getSerialNo()+1);
                            Intent i=new Intent(getActivity(),PartValidation.class);
                            startActivity(i);

                        }
                    }
                }else {

                    if(serialNovalidation.getChildPartsAndFamily().size()==partModel.getSerialNo()+1){
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_container,FragComplete.class, null)
                                .commit();
                    }
                    else {

                        serial_no++;
                        AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,partModel.getSerialNo()+1);
                        Intent i=new Intent(getActivity(),PartValidation.class);
                        startActivity(i);

                    }


                }


            }

        });
        partViewModel.getErrorMutableLiveData().observe(getActivity(), new Observer<PartError>() {
            @Override
            public void onChanged(PartError partError) {
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(getActivity(),partError.getMessage());

            }
        });





        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment=FragmentSkip.newInstance(partModel);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, fragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getViewModelStore().clear();
        partViewModel.getPartResponse().removeObservers((AppCompatActivity)getActivity());
        partViewModel.getErrorMutableLiveData().removeObservers((AppCompatActivity)getActivity());

    }

    @Override
    public void onStop() {
        super.onStop();
        partViewModel.getPartResponse().removeObservers((AppCompatActivity)getActivity());
        partViewModel.getErrorMutableLiveData().removeObservers((AppCompatActivity)getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();


        isreport=false;
        validationStatus=(ValidationStatus)getActivity() ;

        validationStatus.GetListner(new ScanData() {
            @Override
            public void getScanData(String data) {
                if(Common.isInternetConnected(getActivity())){
                    getScannData(Common.replaceString(data));
                }
                else {
                     CustomeToast customeToast=new CustomeToast();
                     customeToast.showErrorToast(getActivity(),getActivity().getString(R.string.internet_error));
                }

            }
        });


        fragmentListenr=(FragmentListenr)getActivity();
        fragmentListenr.getFragment(0);
        readBundle(getArguments());
        binding.textError.setText(partModel.getMsg());
        binding.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layError.setVisibility(View.INVISIBLE);
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        });
        binding.txtcurrent.setText(String.valueOf(partModel.getSerialNo()+1));
        binding.partNo.setText(partModel.getPartNo());
        binding.textFamily.setText(partModel.getPartFamily());
        binding.textSize.setText(String.valueOf(partModel.getPartSize()));
        if(partModel.getSub_part_total()>1){
            binding.lnrSubQuan.setVisibility(View.VISIBLE);
            binding.txtcurrentpart.setText(String.valueOf(partModel.getSub_part_serial()+1));
            binding.txttotalPart.setText(String.valueOf(partModel.getSub_part_total()));
        }
        else {
            binding.lnrSubQuan.setVisibility(View.GONE);
        }



        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.isInternetConnected(getActivity())){
                    getScannData( binding.editSerial.getText().toString().trim());

                }
                else {
                    CustomeToast customeToast=new CustomeToast();
                    customeToast.showErrorToast(getActivity(),getActivity().getString(R.string.internet_error));
                }

            }
        });
        binding.editSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence chars, int i, int i1, int i2) {
                if(chars.length()>=6){
                    binding.image.setVisibility(View.VISIBLE);
                }
                else {
                    binding.image.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





    }


    private void getScannData(String data){

        if(Common.isInternetConnected(getActivity())){
            String token=AppPreference.getInstance().get(getActivity(),AppPreference.token);
            String scanValue=data;
            partViewModel.validatePartNo(getActivity(),token,serialNovalidation.getSequenceNumber(),partModel.getFamilyId(),scanValue,partModel.getPartNo());

        }
        else {
            CustomeToast customeToast=new CustomeToast();
            customeToast.showErrorToast(getActivity(),getActivity().getString(R.string.internet_error));
        }


    }

}
