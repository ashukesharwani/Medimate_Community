package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Medicine_Seeker_final extends AppCompatActivity {

    TextView medicineNameFinal,personNameFinal,medicineQuanityFinal,medicineExpiryFinal,medicineMgFinal;
    ImageView  medicineImageFinal,callMeMedicine,googleUri;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String phoneNumber ,url,personemail ;
    Button mail_Btn;
    Date expiryDate = null;
    Date currentDate = null;
    private static final int REQUEST_CALL=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine__seeker_final);

        medicineNameFinal=findViewById(R.id.medicineNameFinal);
        medicineQuanityFinal=findViewById(R.id.medicineQuanityFinal);
        medicineExpiryFinal=findViewById(R.id.medicineExpiryFinal);
        medicineMgFinal=findViewById(R.id.medicineMgFinal);
        medicineImageFinal=findViewById(R.id.medicineImageFinal);

        personNameFinal=findViewById(R.id.personNameFinal);
        callMeMedicine=findViewById(R.id.callMeMedicine);
        googleUri=findViewById(R.id.googelUri);

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();


        Intent intent=getIntent();
        String MName=intent.getStringExtra("MSName");
        String MMg=intent.getStringExtra("MSMg");
        String MQuantity=intent.getStringExtra("MSQuantity");
        String MExpiry=intent.getStringExtra("MSExpiry");
        String MId=intent.getStringExtra("MSId");


        medicineNameFinal.setText(MName);
        medicineMgFinal.setText(MMg);
        medicineQuanityFinal.setText(MQuantity);
        medicineExpiryFinal.setText(MExpiry);
        String image = intent.getStringExtra("MSImage");
        Picasso.get().load(image).into(medicineImageFinal);


        //validation date
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());

        DateFormat month_year = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String year = month_year.format(Calendar.getInstance().getTime());
        String currentdate=month_name.toUpperCase()+"."+" "+year;

        SimpleDateFormat format = new SimpleDateFormat("MMM.yy");
        try {
            expiryDate = format.parse(MExpiry);
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
            medicineExpiryFinal.setTextColor(Color.RED);
        }

//        if ( MExpiry.compareTo(currentdate) < 0 ) {
//
//            medicineExpiryFinal.setTextColor(Color.parseColor("#FF0000"));
//
//          //  Toast.makeText(this,"Today's Date: " +currentdate, Toast.LENGTH_SHORT).show();
//            //  0 comes when two date are same,
//            //  1 comes when date1 is higher then date2
//            // -1 comes when date1 is lower then date2
//
//        }
        // fetching name and user detial
        DocumentReference documentReference=fStore.collection("users").document(MId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                personNameFinal.setText(documentSnapshot.getString("FName"));
                 phoneNumber=(documentSnapshot.getString("Phone"));
                 personemail=(documentSnapshot.getString("Email"));

            }
        });
        callMeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
      //  Toast.makeText(this, "Name"+phoneNumber , Toast.LENGTH_SHORT).show();
        googleUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://www.google.com/search?q="+MName+"+"+MMg;
//                https://www.google.com/search?q=nicip+plus+100+mg
                gotoUrl(url);


            }
        });
        mail_Btn=findViewById(R.id.mail_Btn);
        mail_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(v.getContext(),Snd_Email.class);
                i.putExtra("fullName",personNameFinal.getText().toString());
                i.putExtra("email",personemail.toString());
                startActivity(i);
            }
        });


    }

    private void gotoUrl(String s) {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void makePhoneCall() {
        if(ContextCompat.checkSelfPermission(Medicine_Seeker_final.this
                        , Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Medicine_Seeker_final.this,new String[]{Manifest.permission.CALL_PHONE}
                    ,REQUEST_CALL);
        }
        else{
            String dial="tel:"+phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
            else{
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        }
        
    }
}