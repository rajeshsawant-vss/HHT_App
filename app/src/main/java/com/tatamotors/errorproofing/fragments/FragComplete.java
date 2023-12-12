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

import com.google.gson.Gson;
import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.databinding.LayoutCompleteBinding;
import com.tatamotors.errorproofing.databinding.ReportLayoutBinding;
import com.tatamotors.errorproofing.interfaces.FragmentListenr;
import com.tatamotors.errorproofing.response.PartError;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.util.AppPreference;
import com.tatamotors.errorproofing.viewmodel.MainViewModel;
import com.tatamotors.errorproofing.viewmodel.PartViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class FragComplete extends Fragment {
    LayoutCompleteBinding binding;
    String module;
    HashMap<String, List<PartResponse>> listHashMap;
    String sequenceNo;
    boolean isPending=false;
    FragmentListenr fragmentListenr;
    MainViewModel mainViewModel;
    String serialNo;
    PartViewModel partViewModel;
    public FragComplete() {
    }


    public static FragComplete newInstance(String module) {
        Bundle bundle = new Bundle();
        bundle.putString("module", module);
        FragComplete fragment = new FragComplete();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            module = bundle.getString("module");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = LayoutCompleteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }



    @Override
    public void onStop() {
        super.onStop();
//        if(!module.equalsIgnoreCase(AppPreference.booking)){
//            listHashMap=null;
//            String key= AppPreference.getInstance().get(getActivity(),AppPreference.username);
//            AppPreference.getInstance().aset(getActivity(),key,listHashMap);
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentListenr=(FragmentListenr)getActivity();
        fragmentListenr.getFragment(2);
        serialNo=AppPreference.getInstance().get(getActivity(),AppPreference.part_serial);
        readBundle(getArguments());
        sequenceNo=AppPreference.getInstance().get(getActivity(),AppPreference.sequenceNumber);
        partViewModel=new ViewModelProvider(getActivity()).get(PartViewModel.class);

        partViewModel.getStatusCode().observe(getActivity(), new Observer<Response>() {
            @Override
            public void onChanged(Response response) {

                if(response.code()==200){
                    AppPreference.getInstance().set(getContext(),AppPreference.booked_serial,serialNo);
                    binding.text.setText(getString(R.string.booking_request)+" "+serialNo+" "+getString(R.string.booking_initiated));
                    binding.btnHome.setText(getString(R.string.home));
                    binding.textReport.setVisibility(View.GONE);
                    module="";
                }else {
                    PartError message = new Gson().fromJson(response.errorBody().charStream(), PartError.class);
                    Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });


        switch (module){
            case  AppPreference.booking_pending:
                binding.textReport.setVisibility(View.VISIBLE);
                binding.text.setText(getString(R.string.part_genology_error));
                binding.text.setTextColor(getActivity().getResources().getColor(R.color.toastError));
                binding.image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_close_small));
                break;
            case AppPreference.booking_complete:
                binding.btnHome.setText(getString(R.string.proceed_to_book));
               // binding.textReport.setVisibility(View.GONE);
                binding.text.setText(getString(R.string.final_initiated));
                break;
            case AppPreference.complete:
                binding.textReport.setVisibility(View.VISIBLE);
                break;
            case AppPreference.booking_report:
                binding.textReport.setVisibility(View.GONE);
                binding.btnHome.setText(getString(R.string.home));
                binding.text.setText(getString(R.string.booking_request)+" "+serialNo+" "+getString(R.string.booking_initiated));
                break;
        }
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(module.equalsIgnoreCase(AppPreference.booking_complete)){

                    partViewModel.bookSerial(AppPreference.getInstance().get(getActivity(),AppPreference.token),sequenceNo);

//                    binding.text.setText(getString(R.string.booking_request)+" "+serialNo+" "+getString(R.string.booking_initiated));
//                    binding.btnHome.setText(getString(R.string.home));
//                    binding.textReport.setVisibility(View.GONE);
//                    module="";
                }
                else {
                    Intent i=new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }



            }
        });
    }

    //
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=ReportFrg.newInstance(module);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container,fragment, null)
                        .commit();
            }
        });
    }
}
