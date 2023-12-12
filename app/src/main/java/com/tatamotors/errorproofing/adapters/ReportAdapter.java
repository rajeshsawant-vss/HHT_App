package com.tatamotors.errorproofing.adapters;

import android.content.Context;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.interfaces.PartData;
import com.tatamotors.errorproofing.model.PartModel;
import com.tatamotors.errorproofing.model.ReportModel;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.PartResponse;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ItemHolder> {
    Context mContext;
    List<ChildPartsAndFamily> familyList;
    int selectedPosition = -1;
    PartData partData;
    boolean isSkiped=false;
    int pendingCount=0;
    String module;

    public ReportAdapter(Context mContext, List<ChildPartsAndFamily> familyList,String module) {
        this.mContext = mContext;
        this.familyList = familyList;
        this.module = module;


    }
    public  void getPartData(PartData PartData1){
        this.partData=PartData1;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.report_items, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ItemHolder(rootView);
    }

    @Override public long getItemId(int position)
    {
        // pass position
        return position;
    }
    @Override public int getItemViewType(int position)
    {
        // pass position
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
//
        ChildPartsAndFamily reportModel=familyList.get(position);
        isSkiped=false;

        if(reportModel.getValidations().isEmpty()){
            holder.textFamily.setText(reportModel.getFamilyDisplayName()+"("+String.valueOf(reportModel.getQuantity())+")");
            holder.textReason.setText("Pending");
            holder.textReason.setTextColor(mContext.getResources().getColor(R.color.toastError));
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
            holder.radioButton.setEnabled(true);

        }
        else {
            if(reportModel.getQuantity()>1){

                List<Validation> validationList=reportModel.getValidations();


                if(validationList.size()<reportModel.getQuantity()){
                    holder.textFamily.setText(reportModel.getFamilyDisplayName()+"("+String.valueOf(reportModel.getQuantity())+")");
                    holder.textReason.setText("pending");
                    holder.textReason.setTextColor(mContext.getResources().getColor(R.color.toastError));
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                    holder.radioButton.setEnabled(true);
                }
                else {
                    for(int a=0;a<validationList.size();a++){

                        if(validationList.get(a).getSkipReason()!=null){
                            isSkiped=true;
                            break;
                        }
                    }
                }

                if(isSkiped){
                    holder.textFamily.setText(reportModel.getFamilyDisplayName()+"("+String.valueOf(reportModel.getQuantity())+")");
                    holder.textReason.setText("DAMAGED BARCODE");
                    holder.textReason.setTextColor(mContext.getResources().getColor(R.color.toastError));
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                    holder.radioButton.setEnabled(true);

                }
                else {
                    holder.textFamily.setText(reportModel.getFamilyDisplayName());
                    holder.textReason.setText("--");
                    holder.textReason.setTextColor(mContext.getResources().getColor(R.color.colourtextgenealogy));
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
                    holder.radioButton.setEnabled(false);
                }



            }
            else {


                if(reportModel.getValidations().get(0).getSkipReason()!=null){
                    holder.textFamily.setText(reportModel.getFamilyDisplayName()+"("+String.valueOf(reportModel.getQuantity())+")");
                    holder.textReason.setText(reportModel.getValidations().get(0).getSkipReason());
                    holder.textReason.setTextColor(mContext.getResources().getColor(R.color.toastError));
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                    holder.radioButton.setEnabled(true);

                }
                else {
                    holder.textFamily.setText(reportModel.getFamilyDisplayName());
                    holder.textReason.setText("--");
                    holder.textReason.setTextColor(mContext.getResources().getColor(R.color.colourtextgenealogy));
                    holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
                    holder.radioButton.setEnabled(false);
                }


            }
        }



        if(module.equalsIgnoreCase(AppPreference.booking_complete)){
            holder.radioButton.setVisibility(View.INVISIBLE);
        }
        else {
            holder.radioButton.setVisibility(View.VISIBLE);
        }

        holder.radioButton.setChecked(position == selectedPosition);
//
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
//                     PartModel partModel=new PartModel(reportModel.getPartNo()
//                     ,reportModel.getPartFamily(),reportModel.getStatus());
//                   partData.getPartData(partModel);
                    selectedPosition=holder.getAdapterPosition();
                    notifyDataSetChanged();
                }

            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.radioButton.setChecked(true);
                partData.getPartData(reportModel);
            }
        });


//        holder.textReason.setText(reportModel.getStatus());
//
//
//
//        if(reportModel.getStatus().equalsIgnoreCase("SUCCESS")){
//            holder.textReason.setTextColor(mContext.getResources().getColor(R.color.colourtextgenealogy));
//            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
//        }
//        else {
//            holder.textReason.setTextColor(mContext.getResources().getColor(R.color.toastError));
//            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
//
//        }


    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        RadioButton radioButton;
        AppCompatTextView textReason;
        AppCompatTextView textFamily;
        AppCompatImageView imageView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio);
            textReason=itemView.findViewById(R.id.textReson);
            imageView=itemView.findViewById(R.id.imgStatus);
            textFamily=itemView.findViewById(R.id.textFamily);
        }
    }
}
