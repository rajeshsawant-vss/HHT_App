package com.tatamotors.errorproofing.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.SerialNovalidation;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.response.ValidationResponse;

import java.util.List;

public class ValidationAdapter extends RecyclerView.Adapter<ValidationAdapter.ViewHolder>  {

    Context mcontext;
    List<ChildPartsAndFamily> familyList;
    String scanValues="";
    String skipReasons="";
    String imageBuilder="";

    public ValidationAdapter(Context mcontext,List<ChildPartsAndFamily> familyList) {
        this.mcontext = mcontext;
        this.familyList = familyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mcontext).inflate(R.layout.val_report_items, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      //  SerialNovalidation novalidation = responseList.get(0);

        ChildPartsAndFamily childPartsAndFamily=familyList.get(position);
        holder.serial.setText(String.valueOf(position+1)+" . ");

        if(childPartsAndFamily.getQuantity()>1){
            for(Validation validation:childPartsAndFamily.getValidations()){
                if(validation.getSkipReason()!=null){

                    scanValues= new StringBuilder().append(scanValues).append("\n").append(validation.getScannedValue()).toString();
                    skipReasons= new StringBuilder().append(skipReasons).append("\n").append(validation.getSkipReason()).toString();
                    imageBuilder=new StringBuilder().append(imageBuilder).append("\n").append("x").toString();

//
//                    scanValues=scanValues+"\n"+validation.getScannedValue();
//                    skipReasons=skipReasons+"\n"+validation.getSkipReason();
//                    imageBuilder=imageBuilder+"\n"+"x";
                }



            }
            SpannableStringBuilder builder = new SpannableStringBuilder();

            String familyValue=childPartsAndFamily.getFamilyDisplayName()+"("+String.valueOf(childPartsAndFamily.getQuantity())+")";
            SpannableString normalSpann= new SpannableString(familyValue);
            normalSpann.setSpan(new ForegroundColorSpan(mcontext.getResources().getColor(R.color.text_color)), 0, familyValue.length(), 0);
            builder.append(normalSpann);


            SpannableString redSpannable= new SpannableString(scanValues);
            redSpannable.setSpan(new ForegroundColorSpan(mcontext.getResources().getColor(R.color.toastError)), 0, scanValues.length(), 0);
            builder.append(redSpannable);




            holder.family.setText(builder, TextView.BufferType.SPANNABLE);

            if(scanValues.equalsIgnoreCase("")){
                holder.reson.setText("--");
                holder.reson.setTextColor(mcontext.getResources().getColor(R.color.colourtextgenealogy));
                holder.img.setTextColor(mcontext.getResources().getColor(R.color.colourtextgenealogy));
               // holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
                holder.img.setText("\u2713");

            }
            else {


                holder.reson.setText(skipReasons);
                holder.reson.setTextColor(mcontext.getResources().getColor(R.color.toastError));
                holder.img.setTextColor(mcontext.getResources().getColor(R.color.toastError));
               // holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                holder.img.setText(imageBuilder);

            }

        }
        else {
            holder.family.setText(childPartsAndFamily.getFamilyDisplayName()+"("+String.valueOf(childPartsAndFamily.getQuantity())+")");
            if(childPartsAndFamily.getValidations().get(0).getDerivedPartNumber()==null){
                holder.reson.setText(childPartsAndFamily.getValidations().get(0).getSkipReason());
                holder.reson.setTextColor(mcontext.getResources().getColor(R.color.toastError));
                holder.img.setTextColor(mcontext.getResources().getColor(R.color.toastError));

               // holder.img.setdr(mcontext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                holder.img.setText("x");


            }
            else {
                holder.reson.setText("--");
                holder.reson.setTextColor(mcontext.getResources().getColor(R.color.colourtextgenealogy));
                holder.img.setTextColor(mcontext.getResources().getColor(R.color.colourtextgenealogy));
               // holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
                holder.img.setText("\u2713");

            }


        }







    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView family,reson,serial,family1,reson1;
        TextView img,img1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            family = itemView.findViewById(R.id.textFamily);

            reson = itemView.findViewById(R.id.textReson);
            img = itemView.findViewById(R.id.imgStatus);
            serial = itemView.findViewById(R.id.textserial);
        }
    }
}
