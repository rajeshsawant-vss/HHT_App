package com.tatamotors.errorproofing.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.interfaces.DropListener;
import com.tatamotors.errorproofing.interfaces.PartData;
import com.tatamotors.errorproofing.interfaces.ScanData;
import com.tatamotors.errorproofing.interfaces.ScrollToBottom;
import com.tatamotors.errorproofing.response.ChildPartsAndFamily;
import com.tatamotors.errorproofing.response.Validation;
import com.tatamotors.errorproofing.util.AppPreference;

import java.util.List;

public class MultiReportDapter extends RecyclerView.Adapter<MultiReportDapter.ViewHolder> {

    Context mcontext;
    List<ChildPartsAndFamily> familyList;
    PartData partData;
    SubItemAdapter subItemAdapter;
    int selectedPosition = -1;
    boolean hideDrop=true;
    RecyclerView recyclerView;
    ScrollToBottom scrollToBottom;
    DropListener dropListener;
    int postiontoscroll=0;
    boolean isShowSelected=false;
    public MultiReportDapter(Context mcontext, List<ChildPartsAndFamily> familyList) {
        this.mcontext = mcontext;
        this.familyList = familyList;
    }

    public  void getPartData(PartData PartData1){
        this.partData=PartData1;
    }

    public  void getScroll(ScrollToBottom scrollToBottom){

        this.scrollToBottom=scrollToBottom;
    }

    public void getDropPostion(DropListener dropListener){
        this.dropListener=dropListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mcontext).inflate(R.layout.multi_items, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        postiontoscroll= AppPreference.getInstance().getInt(mcontext,AppPreference.isPostion);
        ChildPartsAndFamily childPartsAndFamily=familyList.get(position);
        holder.family.setText(childPartsAndFamily.getFamilyDisplayName()+"("+String.valueOf(childPartsAndFamily.getQuantity())+")");




        if(hideDrop){
            holder.recy.setVisibility(View.GONE);
        }

        if(childPartsAndFamily.getQuantity()>1){
           // holder.radioButton.setVisibility(View.INVISIBLE);

            if(childPartsAndFamily.getQuantity()==childPartsAndFamily.getValidations().size()){
                boolean isMultipending=false;
                for(Validation validation:childPartsAndFamily.getValidations()){

                    if(validation.getDerivedPartNumber()==null){
                        isMultipending=true;
                        break;

                    }


                }

                if(isMultipending){
                    holder.radioButton.setVisibility(View.INVISIBLE);
//                    holder.recy.setVisibility(View.VISIBLE);
//                    subItemAdapter=new SubItemAdapter(mcontext,childPartsAndFamily.getValidations(),childPartsAndFamily.getQuantity());
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mcontext, RecyclerView.VERTICAL,false);
//                    holder.recy.setLayoutManager(mLayoutManager);
//                    holder.recy.setAdapter(subItemAdapter);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(40,0,0,0);
                    holder.reson.setLayoutParams(params);
                    holder.reson.setText("");
                    holder.reson.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_drop, 0, 0, 0);
                    holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_close_small));

                    isShowSelected=true;



                    //      holder.reson.setDra(mcontext.getResources().getColor(R.color.colourtextgenealogy));


                }
                else {
                    holder.recy.setVisibility(View.GONE);
                  //  holder.radioButton.setEnabled(false);
                    holder.radioButton.setVisibility(View.INVISIBLE);
                    holder.reson.setText("--");
                   // holder.family.setText(childPartsAndFamily.getFamilyDisplayName());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(50,0,0,0);
                    holder.reson.setLayoutParams(params);
                   // holder.reson.setMa(Gravity.CENTER);
                    holder.reson.setTextColor(mcontext.getResources().getColor(R.color.colourtextgenealogy));
                    holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_check_small));

                }



            }
            else
            {

                int pendingItems=childPartsAndFamily.getQuantity()-childPartsAndFamily.getValidations().size();
                for(int a=0;a<pendingItems;a++){
                    childPartsAndFamily.getValidations().add(new Validation("",null,"Pending"));
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(40,0,0,0);
                holder.reson.setLayoutParams(params);
                holder.radioButton.setVisibility(View.INVISIBLE);
                holder.reson.setText("");
                holder.reson.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_drop, 0, 0, 0);
                holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                isShowSelected=true;

//                holder.recy.setVisibility(View.VISIBLE);
//                subItemAdapter=new SubItemAdapter(mcontext,childPartsAndFamily.getValidations(),childPartsAndFamily.getQuantity());
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mcontext, RecyclerView.VERTICAL,false);
//                holder.recy.setLayoutManager(mLayoutManager);
//                holder.recy.setAdapter(subItemAdapter);

            }


        }
        else {
            holder.recy.setVisibility(View.GONE);
            if(!childPartsAndFamily.getValidations().isEmpty()){
                if(childPartsAndFamily.getValidations().get(0).getSkipReason()!=null){
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(40,0,0,0);
                    holder.reson.setLayoutParams(params);
                    holder.reson.setText(childPartsAndFamily.getValidations().get(0).getSkipReason());
                    holder.reson.setTextColor(mcontext.getResources().getColor(R.color.toastError));
                    holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                   // holder.radioButton.setEnabled(true);
                    holder.radioButton.setVisibility(View.VISIBLE);

                }
                else {
                    holder.reson.setText("--");
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(50,0,0,0);
                    holder.reson.setLayoutParams(params);
                    holder.reson.setTextColor(mcontext.getResources().getColor(R.color.colourtextgenealogy));
                    holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_check_small));
                   // holder.radioButton.setEnabled(false);
                    holder.radioButton.setVisibility(View.INVISIBLE);
                }
            }
            else {


                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(40,0,0,0);
                holder.reson.setLayoutParams(params);
                holder.reson.setText("Pending");
                holder.reson.setTextColor(mcontext.getResources().getColor(R.color.toastError));
                holder.img.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.ic_baseline_close_small));
                //holder.radioButton.setEnabled(true);
                holder.radioButton.setVisibility(View.VISIBLE);

            }


        }

        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(true);
                partData.getPartData(childPartsAndFamily);
                dropListener.getPosition(position);
                hideDrop=true;
                notifyDataSetChanged();

            }
        });


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

        if(position==postiontoscroll &&isShowSelected){
            if(childPartsAndFamily.getQuantity()>1){
                holder.lnr.setBackgroundColor(mcontext.getResources().getColor(R.color.selection));
            }

        }
        else {
            holder.lnr.setBackgroundColor(mcontext.getResources().getColor(R.color.itemColour1));
        }

        //holder.reson.performClick();
        holder.reson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDrop=false;

                ChildPartsAndFamily child=familyList.get(position);
                holder.recy.setVisibility(View.VISIBLE);
                subItemAdapter=new SubItemAdapter(mcontext,child.getValidations(),child.getPartNumber());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mcontext, RecyclerView.VERTICAL,false);
                holder.recy.setLayoutManager(mLayoutManager);
                holder.recy.setAdapter(subItemAdapter);
             //   holder.recy.smoothScrollToPosition(child.getValidations().size()-1);

                if(position==getItemCount()-1)
                {
                    scrollToBottom.isScroll(true);
                }

               // selectedPosition=-1;
                //AppPreference.getInstance().set(mcontext,AppPreference.isPostion,-1);


               notifyDataSetChanged();

                if(subItemAdapter!=null){
                    subItemAdapter.getRadioSelection(new ScanData() {
                        @Override
                        public void getScanData(String data) {
                           // selectedPosition=-1;
                            //AppPreference.getInstance().set(mcontext,AppPreference.isPostion,-1);                            postiontoscroll=-1;
                            hideDrop=false;
                            partData.getPartData(child);
                            dropListener.getPosition(position);
                            notifyDataSetChanged();

                        }
                    });
                }



            }
        });
    }

//    @Override public long getItemId(int position)
//    {
//        // pass position
//        return position;
//    }
    @Override public int getItemViewType(int position)
    {
        // pass position
        return position;
    }



    @Override
    public int getItemCount() {
        return familyList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView family,reson,serial;
        RecyclerView recy;
        ImageView img;
        RadioButton radioButton;
        LinearLayoutCompat lnr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            family = itemView.findViewById(R.id.textFamily);
            recy = itemView.findViewById(R.id.subItems);

            reson = itemView.findViewById(R.id.textReson);
            img = itemView.findViewById(R.id.imgStatus);
            serial = itemView.findViewById(R.id.textserial);
            radioButton = itemView.findViewById(R.id.radio);
            lnr = itemView.findViewById(R.id.itemLnr);
        }
    }
}
