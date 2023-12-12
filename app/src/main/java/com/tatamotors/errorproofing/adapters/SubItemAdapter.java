package com.tatamotors.errorproofing.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ViewHolder> {
    Context mContext;
    String partNO;
    List<Validation> validationList;
    int selectedPosition = -1;
    ScanData scanData;
    public SubItemAdapter(Context mContext, List<Validation> validationList,String partno) {
        this.mContext = mContext;
        this.validationList = validationList;
        this.partNO = partno;
    }

    public void getRadioSelection(ScanData scanData){
        this.scanData=scanData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.sub_items, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Validation validation=validationList.get(position);
        if(validation.getScannedValue()!=null &&!validation.getScannedValue().isEmpty())
        {
            holder.scanValue.setText(validation.getScannedValue());
        }
        else {
            holder.scanValue.setText(partNO);
        }

//        if(validation.getScannedValue()==null || validation.getScannedValue().equalsIgnoreCase("")){
//            holder.scanValue.setText(partNO);
//        }
//        else {
//            holder.scanValue.setText(validation.getScannedValue());
//        }

        holder.radioButton.setChecked(position == selectedPosition);
//        if(quantity>validationList.size()){
//
//            int pendingItems=quantity-validationList.size();
//            for(int a=0;a<pendingItems;a++){
//                validationList.add(new Validation("","","Pending"));
//            }
//            if(validation.getSkipReason()!=null){
//                holder.reason.setText(validation.getSkipReason());
//                holder.reason.setTextColor(mContext.getResources().getColor(R.color.toastError));
//                holder.img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
//                holder.radioButton.setEnabled(true);
//
//            }
//            else {
//
//                holder.reason.setText("--");
//                holder.reason.setTextColor(mContext.getResources().getColor(R.color.colourtextgenealogy));
//                holder.img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
//
//            }
//
//        }
//        else {
//
//        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreference.getInstance().set(mContext,AppPreference.booking_serial,position);
            }
        });

        if(validation.getSkipReason()!=null){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(40,0,0,0);
            holder.reason.setLayoutParams(params);
            holder.reason.setText(validation.getSkipReason());
            holder.reason.setTextColor(mContext.getResources().getColor(R.color.toastError));
            holder.img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
            holder.radioButton.setVisibility(View.VISIBLE);

        }
        else {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,0,0,0);
            holder.reason.setLayoutParams(params);
            holder.reason.setText("--");
            holder.reason.setTextColor(mContext.getResources().getColor(R.color.colourtextgenealogy));
            holder.img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
           // holder.radioButton.setEnabled(false);
            holder.radioButton.setVisibility(View.INVISIBLE);
        }

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
//                     PartModel partModel=new PartModel(reportModel.getPartNo()
//                     ,reportModel.getPartFamily(),reportModel.getStatus());
//                   partData.getPartData(partModel);
                    scanData.getScanData("");
                    selectedPosition=holder.getAdapterPosition();
                    notifyDataSetChanged();
                }


            }
        });

//
//        holder.scanValue.setText(validation.getScannedValue());
//        holder.reason.setText(validation.getSkipReason());

    }

    @Override
    public int getItemCount() {
        return validationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RadioButton radioButton;
        TextView scanValue,reason;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio);
            scanValue=itemView.findViewById(R.id.textscanValue);
            reason=itemView.findViewById(R.id.textReson);
            img=itemView.findViewById(R.id.imgStatus);
        }
    }
}
