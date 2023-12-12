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

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.PartBooking;
import com.tatamotors.errorproofing.activities.PartValidation;
import com.tatamotors.errorproofing.activities.ValidationStatus;
import com.tatamotors.errorproofing.databinding.FrgBookSkipBinding;
import com.tatamotors.errorproofing.databinding.LayoutSkipBinding;
import com.tatamotors.errorproofing.interfaces.FragmentListenr;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.requests.SkipRequest;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.MainViewModel;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.List;

public class FrgBookSkip extends Fragment {
    String [] resons={"DUPLICATE SERIAL NO","PART IS MISMATCHED","SERIAL NO. IS BLANK","AGGR MATERIAL IS BLANK","DAMAGED BARCODE"};
    String selectedReson;
    PartModel partModel;
    SerialNovalidation serialNovalidation;

    FrgBookSkipBinding skipBinding;
    FragmentListenr fragmentListenr;
    String partSerial_no;
    String sequenceNumber;
    PartViewModel partViewModel;
    MainViewModel mainViewModel;
    boolean isCompleted=true;
    public static FrgBookSkip newInstance(PartModel partModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("part_info", partModel);
        FrgBookSkip fragment = new FrgBookSkip();
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
        skipBinding = FrgBookSkipBinding.inflate(inflater,container,false);
        return skipBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        partViewModel=new ViewModelProvider(getActivity()).get(PartViewModel.class);
        mainViewModel= new ViewModelProvider(getActivity()).get(MainViewModel.class);
        partSerial_no= AppPreference.getInstance().get(getActivity(),AppPreference.part_serial);
        sequenceNumber= AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.drop_item,resons);
        skipBinding.autoReason.setAdapter(adapter);
        skipBinding.autoReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipBinding.autoReason.showDropDown();
            }
        });

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

                    selectedReson=skipBinding.autoReason.getText().toString();
                    partViewModel.skipPartNo(getActivity(),AppPreference.getInstance().get(getActivity(),AppPreference.token),sequenceNumber,new SkipRequest(partModel.getFamilyId(),partModel.getPartNo(),partModel.getScannedValue(),selectedReson));

                }


            }
        });

        partViewModel.getPartResponse().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation s) {
                CustomeToast customeToast=new CustomeToast();
                customeToast.showSuccessToast(getActivity(),getString(R.string.partskipped));
                mainViewModel.getPartNos(getActivity(),partSerial_no);


            }
        });

        mainViewModel.getSerialLiveData().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation serialNovalidation) {

                validation(serialNovalidation);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        readBundle(getArguments());
        fragmentListenr=(FragmentListenr)getActivity();
        fragmentListenr.getFragment(1);
    }

    private  void validation(SerialNovalidation serialNovalidation){
        List<ChildPartsAndFamily> familyList=serialNovalidation.getChildPartsAndFamily();

        for(int a=0;a<familyList.size();a++){

            if(familyList.get(a).getQuantity()>1){
                if(familyList.get(a).getValidations().size()<familyList.get(a).getQuantity())
                {
                    AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getActivity(),AppPreference.sub_part_serial_no,familyList.get(a).getValidations().size());
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
                    AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getActivity(),AppPreference.sub_part_serial_no,0);
                    isCompleted=false;
                    break;
                }
                else {

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
        AppPreference.getInstance().set(getActivity(),AppPreference.sequenceNumber,serialNovalidation.getSequenceNumber());

        if(isCompleted){
            Intent i=new Intent(getActivity(), ValidationStatus.class);
            PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_complete);
            i.putExtra("part_info",partModel);
            startActivity(i);
            getActivity().finish();

        }
        else {
            AppPreference.getInstance().set(getActivity(),AppPreference.part_response,serialNovalidation);
            Intent i=new Intent(getActivity(), ValidationStatus.class);
            PartModel partModel=new PartModel("",0,0,0,"",AppPreference.booking_pending_validation);
            i.putExtra("part_info",partModel);
            startActivity(i);
            getActivity().finish();
        }






    }

}
