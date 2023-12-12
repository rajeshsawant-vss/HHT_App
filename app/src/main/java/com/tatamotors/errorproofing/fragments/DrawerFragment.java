package com.tatamotors.errorproofing.fragments;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.activities.Login;
import com.tatamotors.errorproofing.databinding.DrawerBinding;
import com.tatamotors.errorproofing.databinding.LayoutRescanBinding;
import com.tatamotors.errorproofing.util.AppPreference;

import java.util.List;

public class DrawerFragment extends DialogFragment implements View.OnClickListener {


    DrawerBinding drawerBinding;

    public static DrawerFragment newInstance() {

        DrawerFragment f = new DrawerFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        drawerBinding = DrawerBinding.inflate(inflater,container,false);
        return drawerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawerBinding.imgClose.setOnClickListener(this::onClick);
        drawerBinding.lnrLogout.setOnClickListener(this::onClick);
        drawerBinding.lnrMode.setOnClickListener(this::onClick);
        drawerBinding.txtHeader.setText(AppPreference.getInstance().get(getActivity(),AppPreference.username));

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;

        if(nightModeFlags==Configuration.UI_MODE_NIGHT_YES){
            drawerBinding.txtMode.setText(getActivity().getResources().getString(R.string.light_mode));
        }
        else {
            drawerBinding.txtMode.setText(getActivity().getResources().getString(R.string.dark_mode));
        }
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public int getTheme() {
         return R.style.FullScreenDialog;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.imgClose){
            getDialog().dismiss();
        }
        else if(view.getId()==R.id.lnrLogout){

            AppPreference.getInstance().clearPrefernce(getActivity());
            Intent i=new Intent(getActivity(), Login.class);
            startActivity(i);
            getActivity().finish();


        }
        else if(view.getId()==R.id.lnrMode)
        {
            setUiMOde();

//
          //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }

    }

    private void setUiMOde(){
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;


        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                drawerBinding.txtMode.setText(getActivity().getResources().getString(R.string.dark_mode));
                break;

            case Configuration.UI_MODE_NIGHT_NO:

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                drawerBinding.txtMode.setText(getActivity().getResources().getString(R.string.light_mode));
                break;

        }
    }
}
