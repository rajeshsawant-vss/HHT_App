package com.tatamotors.errorproofing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.databinding.ActivityPartValidationBinding;
import com.tatamotors.errorproofing.databinding.ActivityValidationStatusBinding;
import com.tatamotors.errorproofing.fragments.DrawerFragment;
import com.tatamotors.errorproofing.fragments.FragComplete;
import com.tatamotors.errorproofing.fragments.FragmentRescan;
import com.tatamotors.errorproofing.fragments.FragmentSkip;
import com.tatamotors.errorproofing.fragments.FrgBookRescan;
import com.tatamotors.errorproofing.fragments.ReportFrg;
import com.tatamotors.errorproofing.interfaces.FragmentListenr;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.reciever.ScannerReciever;
import com.tatamotors.errorproofing.util.AppPreference;

public class ValidationStatus extends AppCompatActivity implements FragmentListenr,ScanData {

    ActivityValidationStatusBinding statusBinding;
    ScanData getData;
    ScannerReciever scannerReciever=new ScannerReciever();
    PartModel partModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBinding = ActivityValidationStatusBinding.inflate(getLayoutInflater());
        View view = statusBinding.getRoot();
        setContentView(view);
        init();





       // setContentView(R.layout.activity_validation_status);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(scannerReciever, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(scannerReciever);

    }

    private void init(){

        statusBinding.include2.imgDrawer.setVisibility(View.GONE);
        statusBinding.include2.imgback.setVisibility(View.GONE);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            partModel= (PartModel) b.getSerializable("part_info");
        }


        if(partModel.getTag().equalsIgnoreCase(AppPreference.booking_complete) || partModel.getTag().equalsIgnoreCase(AppPreference.booking_pending)||
                partModel.getTag().equalsIgnoreCase(AppPreference.booking_report)){
            Fragment fragment=FragComplete.newInstance(partModel.getTag());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,fragment, null)
                    .commit();
        }

        if(partModel.getTag().equalsIgnoreCase(AppPreference.booking_pending_validation)){
            Fragment fragment=ReportFrg.newInstance(partModel.getTag());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,fragment, null)
                    .commit();
        }


        if(partModel.getTag().equalsIgnoreCase("skip_part")){
            Fragment fragment=FragmentRescan.newInstance(partModel);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,fragment, null)
                    .commit();
        }

        if(partModel.getTag().equalsIgnoreCase(AppPreference.skip_booking)){
            Fragment fragment= FrgBookRescan.newInstance(partModel);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,fragment, null)
                    .commit();
        }


        if(partModel.getTag().equalsIgnoreCase("complete")){
             Fragment fragment= FragComplete.newInstance(AppPreference.complete);
             getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,fragment, null)
                    .commit();
        }

        if(partModel.getTag().equalsIgnoreCase("report")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, ReportFrg.class, null)
                    .commit();
        }


        statusBinding.include2.imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidationStatus.super.onBackPressed();
            }
        });



        statusBinding.include2.imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DrawerFragment newFragment = DrawerFragment.newInstance();
                newFragment.show(ft, "dialog");
            }
        });


    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if(count!=0 && count!=2){
            super.onBackPressed();
        }

    }

    @Override
    public void getFragment(int fragment) {

        if(fragment==1){
            statusBinding.include2.imgback.setVisibility(View.VISIBLE);
            statusBinding.include2.getRoot().setVisibility(View.VISIBLE);
        }
        else {
            statusBinding.include2.getRoot().setVisibility(View.GONE);
            statusBinding.include2.imgback.setVisibility(View.GONE);
        }
    }


    public void GetListner(ScanData getData){
        this.getData=getData;

    }

    @Override
    public void getScanData(String data) {
        getData.getScanData(data);
    }
}