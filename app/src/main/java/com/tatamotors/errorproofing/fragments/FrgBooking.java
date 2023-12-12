package com.tatamotors.errorproofing.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.databinding.FrgBookingBinding;
import com.tatamotors.errorproofing.databinding.LayoutSkipBinding;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.util.AppPreference;

import java.util.HashMap;
import java.util.List;

public class FrgBooking extends Fragment {

    String partSerialNo;
    String key;
    HashMap<String, List<PartResponse>> listHashMap;
    FrgBookingBinding binding;
    public FrgBooking() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FrgBookingBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        key=AppPreference.getInstance().get(getActivity(),AppPreference.username);
        listHashMap=null;
        AppPreference.getInstance().set(getActivity(),key,listHashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        partSerialNo= AppPreference.getInstance().get(getContext(),AppPreference.part_serial);

        binding.text.setText("Booking request for Serial number "+partSerialNo+" has been initiated successfully");

        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key=AppPreference.getInstance().get(getActivity(),AppPreference.username);
                listHashMap=null;
                AppPreference.getInstance().set(getActivity(),key,listHashMap);
                Intent i=new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });


    }
}
