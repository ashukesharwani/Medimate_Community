package com.example.medimate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder>implements Filterable {


     Context context;
     List<MedicineModel> mList;
    List<MedicineModel> newsListFiltered;

    public MedicineAdapter(Context context, List<MedicineModel> mList) {
        this.context = context;
        this.mList = mList;
        this.newsListFiltered=mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.desigin_now_for_the_medicineseeker,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         MedicineModel medicinemodel=mList.get(position);
         holder.mName.setText(medicinemodel.getMedicineName());
         holder.mMg.setText(medicinemodel.getMG());
         holder.mExpiry.setText(medicinemodel.getExpiryDate());


        String imageUri=null;
        imageUri=medicinemodel.getImage();
        Picasso.get().load(imageUri).into(holder.imageVew);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageVew;
        TextView mName,mMg,mExpiry;
       

        public MyViewHolder(@NonNull View itemView)  {
            super(itemView);
            mName=itemView.findViewById(R.id.medicineNameMedicineSeeker);
            mMg=itemView.findViewById(R.id.medicineMgMedicineSeeker);
            mExpiry=itemView.findViewById(R.id.medicineExpryDate);
            imageVew=itemView.findViewById(R.id.imageMedicineSeeker);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position =getAdapterPosition();
            Intent intent=new Intent(context,Medicine_Seeker_final.class);
            intent.putExtra("MSName",mList.get(position).getMedicineName());
            intent.putExtra("MSMg",mList.get(position).getMG());
            intent.putExtra("MSQuantity",mList.get(position).getQuantity());
            intent.putExtra("MSExpiry",mList.get(position).getExpiryDate());
            intent.putExtra("MSId",mList.get(position).getID());

            intent.putExtra("MSImage",mList.get(position).getImage());
            context.startActivity(intent);


        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charcater = constraint.toString();
                if (charcater.isEmpty()){
                    newsListFiltered = mList ;
                }else {
                    List<MedicineModel> filterList = new ArrayList<>();
                    for (MedicineModel row: mList){
                        if (row.getMedicineName().toLowerCase().contains(charcater.toLowerCase())){
                            filterList.add(row);
                        }
                    }

                    newsListFiltered = filterList ;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = newsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                newsListFiltered = (ArrayList<MedicineModel>) results.values ;
                notifyDataSetChanged();
            }
        };
    }
}

