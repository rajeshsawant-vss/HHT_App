package com.tatamotors.errorproofing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.PartValidation;
import com.tatamotors.errorproofing.activities.ValidationStatus;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.LayoutRescanBinding;
import com.tatamotors.errorproofing.databinding.LayoutSkipBinding;
import com.tatamotors.errorproofing.interfaces.FragmentListenr;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.requests.SkipRequest;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.ValidationResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;
import com.tatamotors.errorproofing.viewmodel.SkipViewModel;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;

public class FragmentSkip extends Fragment {
    SerialNovalidation serialNovalidation;
    LayoutSkipBinding skipBinding;
    FragmentListenr fragmentListenr;
    String part_serial_no;

    int familyId;
    SkipViewModel skipViewModel;
    PartViewModel partViewModel;
    String sequenceNumber;
    int partSerialNO=0;
    String selectedReson;
    PartModel partModel;
    int serial_no=0;

    String [] resons={"DUPLICATE SERIAL NO","MISMATCHED PART","SERIAL NO. IS BLANK","AGGR MATERIAL IS BLANK","DAMAGED BARCODE"};
    public FragmentSkip() {
    }


    public static FragmentSkip newInstance(PartModel partModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("part_info", partModel);
//        bundle.putInt("familyId", familyId);
//        bundle.putString("part_serialNo", part_serial_no);
//        bundle.putString("serial_no", serial_no);

        FragmentSkip fragment = new FragmentSkip();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {

            partModel= (PartModel) bundle.getSerializable("part_info");
            skipBinding.textpartFamily.setText(partModel.getPartFamily());
            skipBinding.textPartNo.setText(partModel.getPartNo());
//            part_no = bundle.getString("part_no");
//            familyId = bundle.getInt("familyId");
//            part_serial_no = bundle.getString("part_serialNo");
//            serial_no = bundle.getString("serial_no");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        skipBinding = LayoutSkipBinding.inflate(inflater,container,false);
        return skipBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        partViewModel=new ViewModelProvider(getActivity()).get(PartViewModel.class);
        partSerialNO=AppPreference.getInstance().getInt(getActivity(),AppPreference.part_serial_no);
        serialNovalidation=AppPreference.getInstance().getList(getActivity(),AppPreference.part_response);
        serial_no=AppPreference.getInstance().getInt(getActivity(),AppPreference.part_serial_no);

        partViewModel.getPartResponse().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation s) {
                CustomeToast customeToast=new CustomeToast();
                customeToast.showSuccessToast(getActivity(),getString(R.string.partskipped));

                if(partModel.getSub_part_total()>1){

                    if(partModel.getSub_part_total()> partModel.getSub_part_serial()+1){
                        AppPreference.getInstance().set(getActivity(),AppPreference.sub_part_serial_no,partModel.getSub_part_serial()+1);
                        AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,partModel.getSerialNo());
                        Intent i=new Intent(getActivity(),PartValidation.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                    else {
                        if(serialNovalidation.getChildPartsAndFamily().size()==partModel.getSerialNo()+1){
                            moveStatus();
                        }
                        else {

                            serial_no++;
                            AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,partModel.getSerialNo()+1);
                            Intent i=new Intent(getActivity(),PartValidation.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    }
                }else {

                    if(serialNovalidation.getChildPartsAndFamily().size()==partModel.getSerialNo()+1){
                        moveStatus();
                    }
                    else {

                        serial_no++;
                        AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,partModel.getSerialNo()+1);
                        Intent i=new Intent(getActivity(),PartValidation.class);
                        startActivity(i);
                        getActivity().finish();
                    }


                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        getViewModelStore().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        partViewModel.getPartResponse().removeObservers(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        readBundle(getArguments());


        fragmentListenr=(FragmentListenr)getActivity();
        fragmentListenr.getFragment(1);

        String key= AppPreference.getInstance().get(getActivity(),AppPreference.username);
        sequenceNumber= AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber);
        init();

    }

    private void init(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_item,resons);
        skipBinding.autoReason.setAdapter(adapter);
        part_serial_no=AppPreference.getInstance().get(getActivity(),AppPreference.part_serial);
        partSerialNO=AppPreference.getInstance().getInt(getActivity(),AppPreference.part_serial_no);
        System.out.println("part_serial_no "+partSerialNO);
        skipBinding.autoReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipBinding.autoReason.showDropDown();
            }
        });

        skipBinding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(skipBinding.autoReason.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "PLEASE SELECT REASON", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(Common.isInternetConnected(getActivity())){
                        selectedReson=skipBinding.autoReason.getText().toString();
                        partViewModel.skipPartNo(getActivity(),AppPreference.getInstance().get(getActivity(),AppPreference.token),sequenceNumber,new SkipRequest(partModel.getFamilyId(),partModel.getPartNo(),partModel.getScannedValue(),selectedReson));

                    }
                    else {
                        CustomeToast customeToast=new CustomeToast();
                        customeToast.showErrorToast(getActivity(),getActivity().getString(R.string.internet_error));
                    }







                }


            }
        });

    }

    private  void moveStatus(){


        AppPreference.getInstance().set(getContext(),AppPreference.part_serial_no,0);
        AppPreference.getInstance().set(getContext(),AppPreference.sub_part_serial_no,0);
        Intent i=new Intent(getActivity(), ValidationStatus.class);
        PartModel partModel=new PartModel("",0,0,0,"",AppPreference.complete);
        i.putExtra("part_info",partModel);
        startActivity(i);
        getActivity().finish();


    }
}
