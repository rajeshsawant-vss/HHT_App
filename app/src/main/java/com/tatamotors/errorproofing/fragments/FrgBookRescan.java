package com.tatamotors.errorproofing.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.PartBooking;
import com.tatamotors.errorproofing.activities.PartValidation;
import com.tatamotors.errorproofing.activities.ValidationStatus;
import com.tatamotors.errorproofing.common.Common;
import com.tatamotors.errorproofing.databinding.LayoutRescanBinding;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.util.CustomeToast;
import com.tatamotors.errorproofing.viewmodel.MainViewModel;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.List;

public class FrgBookRescan extends Fragment {

    LayoutRescanBinding binding;
    PartModel partModel;
    PartViewModel partViewModel;
    String partSerial_no;
    ValidationStatus validationStatus;
    String sequneceNumber;
    MainViewModel mainViewModel;
    boolean isCompleted=true;
    String prevScannValue="";
    ChildPartsAndFamily childPartsAndFamily;
    int part_book_sub_serial;
    public FrgBookRescan() {
    }


    public static FrgBookRescan newInstance(PartModel partModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("part_info", partModel);
        FrgBookRescan fragment = new FrgBookRescan();
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
        super.onViewCreated(view, savedInstanceState);
        partViewModel=new ViewModelProvider(getActivity()).get(PartViewModel.class);
        mainViewModel= new ViewModelProvider(getActivity()).get(MainViewModel.class);
        binding.lnrSubQuan.setVisibility(View.GONE);

        mainViewModel.getSerialLiveData().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation serialNovalidation) {
                getViewModelStore().clear();
                validation(serialNovalidation);

            }
        });



        partViewModel.getPartResponse().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation s) {
                getViewModelStore().clear();
                mainViewModel.getPartNos(getActivity(),partSerial_no);
//                if(partModel.getSub_part_total()>1){
//                    if(partModel.getSub_part_total()>partModel.getSub_part_serial()){
//                        AppPreference.getInstance().set(getActivity(),AppPreference.booking_serial,partModel.getSub_part_serial()+1);
//                        Intent i=new Intent(getActivity(), PartBooking.class);
//                        getActivity().startActivity(i);
//                        getActivity().finish();
//                    }
//                    else {
//                        mainViewModel.getPartNos(getActivity(),partSerial_no);
//                    }
//
//                }
//                else {
//                    mainViewModel.getPartNos(getActivity(),partSerial_no);
//                }




            }
        });
        partViewModel.getErrorMutableLiveData().observe(getActivity(), new Observer<PartError>() {
            @Override
            public void onChanged(PartError partError) {
                CustomeToast customeToast=new CustomeToast();
                customeToast.showErrorToast(getActivity(),partError.getMessage());

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        readBundle(getArguments());

        binding.textError.setText(partModel.getMsg());
        binding.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layError.setVisibility(View.INVISIBLE);
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        });
        binding.editSerial.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown( binding.editSerial.getRootView())) {

                } else {
                    binding.editSerial.clearFocus();

                }
            }
        });


        childPartsAndFamily= AppPreference.getInstance().getChildFamilyList(getActivity(),AppPreference.child_part_family);

        part_book_sub_serial=AppPreference.getInstance().getInt(getActivity(),AppPreference.booking_serial);


        partSerial_no= AppPreference.getInstance().get(getActivity(),AppPreference.part_serial);
        validationStatus=(ValidationStatus)getActivity() ;
        binding.lnr1.setVisibility(View.GONE);
        binding.partNo.setText(partModel.getPartNo());
        binding.textFamily.setText(partModel.getPartFamily());
        binding.textSize.setText(String.valueOf(partModel.getPartSize()));
        if(partModel.getSub_part_total()>1){

            prevScannValue=childPartsAndFamily.getValidations().get(part_book_sub_serial).getScannedValue();

//            binding.lnrSubQuan.setVisibility(View.VISIBLE);
//            binding.txtcurrentpart.setText(String.valueOf(partModel.getSub_part_serial()));
//            binding.txttotalPart.setText(String.valueOf(partModel.getSub_part_total()));
//            if(!childPartsAndFamily.getValidations().isEmpty() && childPartsAndFamily.getValidations().get(partModel.getSub_part_total()-1)!=null){
//
//
//            }

        }
        else {
            if(!childPartsAndFamily.getValidations().isEmpty()){
                prevScannValue=childPartsAndFamily.getValidations().get(0).getScannedValue();

            }
         //   binding.lnrSubQuan.setVisibility(View.GONE);
        }

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


        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Fragment fragment=FrgBookSkip.newInstance(partModel);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, fragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.isInternetConnected(getActivity())){
                    getScannData(binding.editSerial.getText().toString().trim());
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
                if(chars.length()>=32){
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


        String  scannValue=data;
        sequneceNumber=AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber);
        String token=AppPreference.getInstance().get(getActivity(),AppPreference.token);


      //  partViewModel.updateValidation(token,sequneceNumber,childPartsAndFamily.getValidations().get(0).getId(),partModel.getFamilyId(),data,partModel.getPartNo(),prevScannValue);
//

        if(childPartsAndFamily.getValidations().isEmpty()){
            partViewModel.validatePartNo(getActivity(),token,sequneceNumber,partModel.getFamilyId(),data,partModel.getPartNo());

        }
        else {
            partViewModel.updateValidation(token,sequneceNumber,childPartsAndFamily.getValidations().get(0).getId(),partModel.getFamilyId(),data,partModel.getPartNo());

        }
//        if(prevScannValue==null||prevScannValue.equalsIgnoreCase("")){
//            partViewModel.validatePartNo(getActivity(),token,sequneceNumber,partModel.getFamilyId(),data,partModel.getPartNo());
//        }
//        else {
//            partViewModel.updateValidation(token,sequneceNumber,childPartsAndFamily.getValidations().get(0).getId(),partModel.getFamilyId(),data,partModel.getPartNo());
//        }

//
//        String token=AppPreference.getInstance().get(getActivity(),AppPreference.token);
//        sequneceNumber=AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber);
//        String scanValue=data;
//        partViewModel.updateValidation(token,sequneceNumber,partModel.getFamilyId(),scanValue,partModel.getPartNo(),prevScannValue);


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
                    AppPreference.getInstance().set(getActivity(),AppPreference.part_serial_no,a);
                    AppPreference.getInstance().set(getActivity(),AppPreference.sub_part_serial_no,0);

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

    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


}
