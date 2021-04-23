package com.example.medimate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Medicine_1_Adapter extends RecyclerView.Adapter<Medicine_1_Adapter.ViewHolder> {

    Context context;
    List<Medicine_1_Model> medicineData;



    public Medicine_1_Adapter(Context context, List<Medicine_1_Model> medicineData) {
        this.context = context;
        this.medicineData = medicineData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.showing_data_perticular_person,parent,false);
        return new  ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Medicine_1_Model medicine1Model=medicineData.get(position);
        holder.Mname.setText(medicine1Model.getMedicineName());
        holder.Mexpiry.setText(medicine1Model.getExpiryDate());

        Date expiryDate = null;
        Date currentDate = null;

        //validation date
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());

        DateFormat month_year = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String year = month_year.format(Calendar.getInstance().getTime());
        String currentdate=month_name.toUpperCase()+"."+" "+year;

        SimpleDateFormat format = new SimpleDateFormat("MMM.yy");
        try {
            expiryDate = format.parse(medicine1Model.getExpiryDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // Log.d("FbDatabase1",expiryDate.toString());
        try {
            currentDate = format.parse(currentdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // Log.d("FbDatabase2",currentDate.toString());


        if(expiryDate.compareTo(currentDate) < 0) {
            holder.Mexpiry.setTextColor(Color.RED);
        }


        String imageUri=null;
        imageUri=medicine1Model.getImage();
        Picasso.get().load(imageUri).into(holder.Mimagecircle);

        holder.editMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.Mname.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_medicine))
                        .setExpanded(true,1100)
                        .setGravity(Gravity.CENTER).setMargin(50,0,50,0)
                        .create();

                View myview=dialogPlus.getHolderView();
                EditText QE=myview.findViewById(R.id.qunityEdit);
                Button UMedi=myview.findViewById(R.id.medicineUpdate);

                QE.setText(medicine1Model.getQuantity());
                dialogPlus.show();

                    UMedi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String,Object> map= new HashMap<>();
                            map.put("Quantity",QE.getText().toString());
                            FirebaseDatabase.getInstance().getReference("Student").child(medicine1Model.getMedicineId()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            notifyItemInserted(position);
                                            dialogPlus.dismiss();


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialogPlus.dismiss();

                                }
                            });
                        }
                    });

            }

        });

        holder.deleteMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.Mname.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                      DatabaseReference  databaseReference=FirebaseDatabase.getInstance().getReference("Student")
                                .child(medicine1Model.getMedicineId());
                              databaseReference.removeValue();
                              notifyItemRemoved(position);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicineData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView Mimagecircle;
        TextView Mname,Mexpiry;
        ImageButton editMedicine,deleteMedicine;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Mname=itemView.findViewById(R.id.Mname);
            Mexpiry=itemView.findViewById(R.id.Mexpiry);
            Mimagecircle=itemView.findViewById(R.id.Mimagecircle);
            editMedicine=itemView.findViewById(R.id.editMedicine);
            deleteMedicine=itemView.findViewById(R.id.deleteMedicine);

           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

           // Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
            View  dialogView=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_dialog_shower,null);

            ImageView dialog_box_medicine_image;
            TextView  dialog_box_medicine_name,dialog_box_medicine_mg,dialog_box_medicine_quantity,dialog_box_medicine_expiry;

            dialog_box_medicine_image=dialogView.findViewById(R.id.dialog_box_medicine_image);

            dialog_box_medicine_name=dialogView.findViewById(R.id.dialog_box_medicine_name);
            dialog_box_medicine_mg=dialogView.findViewById(R.id.dialog_box_medicine_mg);
            dialog_box_medicine_expiry=dialogView.findViewById(R.id.dialog_box_medicine_expiry);
            dialog_box_medicine_quantity=dialogView.findViewById(R.id.dialog_box_medicine_quntity);

            dialog_box_medicine_name.setText(medicineData.get(position).getMedicineName());
            dialog_box_medicine_mg.setText(medicineData.get(position).getMG());
            dialog_box_medicine_expiry.setText(medicineData.get(position).getExpiryDate());

            dialog_box_medicine_quantity.setText(medicineData.get(position).getQuantity());


            String image=null;
            image=medicineData.get(position).getImage();
            Picasso.get().load(image).into(dialog_box_medicine_image);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.show();


        }
    }
}
