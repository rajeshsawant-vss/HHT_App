package com.tatamotors.errorproofing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.PartBooking;
import com.tatamotors.errorproofing.adapters.MultiReportDapter;
import com.tatamotors.errorproofing.adapters.ReportAdapter;
import com.tatamotors.errorproofing.adapters.ValidationAdapter;
import com.tatamotors.errorproofing.databinding.LayoutRescanBinding;
import com.tatamotors.errorproofing.databinding.ReportLayoutBinding;
import com.tatamotors.errorproofing.interfaces.DropListener;
import com.tatamotors.errorproofing.interfaces.FragmentListenr;
import com.tatamotors.errorproofing.interfaces.PartData;
import com.tatamotors.errorproofing.interfaces.ScrollToBottom;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.viewmodel.MainViewModel;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.AbstractPreferences;

import retrofit2.Response;

public class ReportFrg extends Fragment {

    private  boolean shoBookingbutton=true;
    ReportLayoutBinding binding;

    ReportAdapter reportAdapter;
    MultiReportDapter multiReportDapter;
    String module;
    boolean isContinue=false;
    FragmentListenr fragmentListenr;
    MainViewModel mainViewModel;
    boolean isPending=false;
    PartModel partModel;
    ValidationAdapter validationAdapter;
    ChildPartsAndFamily childPartsAndFamily;
    SerialNovalidation serialNovalid;
    String sequenceNo;
    PartViewModel partViewModel;
    int postionToscroll=0;
    public ReportFrg() {
    }


    public static ReportFrg newInstance(String module) {
        Bundle bundle = new Bundle();
        bundle.putString("module", module);
        ReportFrg fragment = new ReportFrg();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ReportLayoutBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel= new ViewModelProvider(getActivity()).get(MainViewModel.class);
        postionToscroll=AppPreference.getInstance().getInt(getActivity(),AppPreference.isPostion);
        partViewModel=new ViewModelProvider(getActivity()).get(PartViewModel.class);
        sequenceNo=AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber);


        partViewModel.getStatusCode().observe(getActivity(), new Observer<Response>() {
            @Override
            public void onChanged(Response response) {

                if(response.code()==200){
                    AppPreference.getInstance().set(getContext(),AppPreference.booked_serial,AppPreference.getInstance().get(getActivity(),AppPreference.part_serial));

                    Fragment fragment= FragComplete.newInstance(AppPreference.booking_report);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container,fragment, null)
                            .commit();

                }else {
                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
                    Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });


        mainViewModel.getStatusCode().observe(getActivity(), new Observer<Response>() {
            @Override
            public void onChanged(Response response) {

                if(response.isSuccessful()){

                    binding.lnrpartables.setVisibility(View.VISIBLE);
                    binding.lnrbooklables.setVisibility(View.GONE);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                    binding.recyReport.setLayoutManager(mLayoutManager);
                    validationAdapter =new ValidationAdapter(getActivity(),serialNovalid.getChildPartsAndFamily());
                    binding.recyReport.setAdapter(validationAdapter);
                    validationAdapter.notifyDataSetChanged();
                }else {
                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
                    Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
        mainViewModel.getSerialLiveData().observe(getActivity(), new Observer<SerialNovalidation>() {
            @Override
            public void onChanged(SerialNovalidation serialNovalidation) {
                binding.textSerial.setText(AppPreference.getInstance().get(getActivity(),AppPreference.part_serial));
                binding.textpart.setText(serialNovalidation.getParentPartNumber());
                binding.textFamily.setText(serialNovalidation.getParentPartDescription());
                serialNovalid=serialNovalidation;

                if(module.equalsIgnoreCase(AppPreference.complete)){

                    mainViewModel.complete(getActivity(),AppPreference.getInstance().get(getActivity(),AppPreference.token),
                            AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber));

                }
                else {

                    binding.lnrpartables.setVisibility(View.GONE);
                    binding.lnrbooklables.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                  //  list.setLayoutManager(llm);
                    binding.recyReport.setLayoutManager(llm);
                    multiReportDapter =new MultiReportDapter(getActivity(),serialNovalidation.getChildPartsAndFamily());

                   // reportAdapter =new ReportAdapter(getActivity(),serialNovalidation.getChildPartsAndFamily(),module);
                    binding.recyReport.setAdapter(multiReportDapter);
                    binding.appCompatButton.setText(getString(R.string.continue1));

                    if(module.equalsIgnoreCase(AppPreference.booking_complete)){
                        binding.appCompatButton.setEnabled(true);
                        binding.appCompatButton.setText(getString(R.string.proceed_to_book));
                    }
                    else {
                        binding.appCompatButton.setEnabled(false);
                    }

                    showBooking(serialNovalid);

                    if(shoBookingbutton){

                        binding.appCompatButton1.setVisibility(View.VISIBLE);
                    }else {
                        binding.appCompatButton1.setVisibility(View.GONE);
                    }

                    if(multiReportDapter!=null){
                        multiReportDapter.getPartData(new PartData() {
                            @Override
                            public void getPartData(ChildPartsAndFamily partModel) {

                                if(partModel!=null){

                                    childPartsAndFamily=partModel;
                                    binding.appCompatButton.setEnabled(true);
                                }
                            }
                        });

                        multiReportDapter.getScroll(new ScrollToBottom() {
                            @Override
                            public void isScroll(boolean scroll) {

                                if(scroll){
                                    binding.recyReport.smoothScrollToPosition(multiReportDapter.getItemCount()-1);
                                }
                            }
                        });

                        multiReportDapter.getDropPostion(new DropListener() {
                            @Override
                            public void getPosition(int postion) {
                                postionToscroll=postion;
                            }
                        });
                    }
                   // reportAdapter.notifyDataSetChanged();
                }

                if(postionToscroll!=-1){
                    binding.recyReport.smoothScrollToPosition(postionToscroll);
                }


            }
        });

        mainViewModel.getSerialNo().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mainViewModel.getPartNos(getActivity(),s);
            }
        });


        binding.appCompatButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                partViewModel.bookSerial(AppPreference.getInstance().get(getActivity(),AppPreference.token),sequenceNo);

            }
        });
        binding.appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  AppPreference.getInstance().set(getActivity(),key,listHashMap);
                if(module.equalsIgnoreCase(AppPreference.complete)){
                    Intent i=new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
                else if(module.equalsIgnoreCase(AppPreference.booking_complete))
                {
                    partViewModel.bookSerial(AppPreference.getInstance().get(getActivity(),AppPreference.token),sequenceNo);

//                    Fragment fragment= FragComplete.newInstance(AppPreference.booking_report);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .add(R.id.fragment_container,fragment, null)
//                            .commit();
                }
                else {
                    AppPreference.getInstance().set(getActivity(),AppPreference.isPostion,postionToscroll);

                    AppPreference.getInstance().set(getActivity(),AppPreference.part_response,serialNovalid);
                    AppPreference.getInstance().set(getActivity(),AppPreference.child_part_family,childPartsAndFamily);

//                    i.putExtra("part_model",childPartsAndFamily);
//                    i.putExtra("serial_model",serialNovalid);
                    Intent i=new Intent(getActivity(), PartBooking.class);
                    startActivity(i);
                   // getActivity().finish();
                }






            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
//        if(!module.equalsIgnoreCase(AppPreference.booking)){
//            listHashMap=null;
//            String key= AppPreference.getInstance().get(getActivity(),AppPreference.username);
//            AppPreference.getInstance().set(getActivity(),key,listHashMap);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // isPending=false;

        fragmentListenr=(FragmentListenr)getActivity();
        fragmentListenr.getFragment(3);
        mainViewModel.setSerialNo(AppPreference.getInstance().get(getActivity(),AppPreference.part_serial));
        readBundle(getArguments());


      //  init();


//        for(PartResponse partResponse:partResponseList){
//
//            if(!partResponse.getStatus().equalsIgnoreCase("SUCCESS")){
//                isPending=true;
//            }
//        }
//
//        if(!isPending && module.equalsIgnoreCase(AppPreference.booking)){
//            binding.appCompatButton.setText(getActivity().getResources().getString(R.string.proceed_to_book));
//        }
    }



    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            module = bundle.getString("module");

        }
    }

    private void showBooking(SerialNovalidation serialNovalidation){
        List<ChildPartsAndFamily> familyList=serialNovalidation.getChildPartsAndFamily();
        for(ChildPartsAndFamily family:familyList){

            if(family.getQuantity()>1){
                if(family.getValidations().size()<family.getQuantity()){
                    shoBookingbutton=false;
                    break;
                }

            }
            else {
                if(family.getValidations().isEmpty()){
                    shoBookingbutton=false;
                    break;
                }
            }


        }

    }
}
