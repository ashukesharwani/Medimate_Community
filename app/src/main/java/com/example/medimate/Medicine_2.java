package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Medicine_2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText MedicineName,MedicineMg,MedicineExpiry;
    Button MedicineNext;
    ImageButton  imageButton;
    Spinner MedicineSpinner;
    TextView termsbtn;
    CheckBox checkBox;
    String UserID;
    FirebaseAuth fAuth;
    Date expiryDate = null;
    Date currentDate = null;

    ProgressDialog progressDialog;

    private static  final int Gallery_Code=1;
    Uri imageUrl=null;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;

    String[] Quanti  = { "1"," 2 "," 3 "," 4 "," 5 ", " 6 ", " 7 "," 8 ", " 9 ", " 10 "," 11 ", " 12 ", " 13 "," 14 ", " 15 ", " 16 "," 17", " 18 ", " 19 "," 20 "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_2);

        //underlinetext
        termsbtn = findViewById(R.id.createText) ;
        SpannableString content = new SpannableString( "Terms and Condition" ) ;
        content.setSpan( new UnderlineSpan() , 0 , content.length() , 0 ) ;
        termsbtn.setText(content) ; //

        MedicineName=findViewById(R.id.medicineName);
        MedicineMg=findViewById(R.id.medicineMg);
        MedicineExpiry=findViewById(R.id.medicineExpry);
        MedicineNext=findViewById(R.id.medicineNext);
        imageButton=findViewById(R.id.medicineImage);

        mDatabase=FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        UserID=fAuth.getCurrentUser().getUid();
        mRef=mDatabase.getReference().child("Student");
      //  MedicineId=mRef.push().getKey();
        mStorage=FirebaseStorage.getInstance();





        progressDialog =new ProgressDialog(this);

        //spinner
        MedicineSpinner=findViewById(R.id.medicineSpinner);
        MedicineSpinner.setOnItemSelectedListener(this);

        checkBox=findViewById(R.id.checkAccepts);


        //Creating the ArrayAdapter instance having Blood list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Quanti);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        MedicineSpinner.setAdapter(aa);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Code);
            }
        });

        termsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.custom_dialog,null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
            }
        });

        //Initialize and Assign Varible
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        //set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.medicine_3);

        //Perform  ItemSelected listner
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.medicine_1:
                        startActivity(new Intent(getApplicationContext(),Medicine_Donation.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.medicine_2:
                        startActivity(new Intent(getApplicationContext(),Medicine_1.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.medicine_3:
                        return true;
                }
                return false;
            }
        });





    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Code && resultCode==RESULT_OK){
            imageUrl=data.getData();
            imageButton.setImageURI(imageUrl);
        }
        MedicineNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = MedicineName.getText().toString().trim();
                String mg = MedicineMg.getText().toString().trim();
                String expiry = MedicineExpiry.getText().toString();
                String qun=MedicineSpinner.getSelectedItem().toString();


                if(!(name.isEmpty()  && mg.isEmpty() && expiry.isEmpty() && imageUrl!=null)){

                    if(checkBox.isChecked()) {

                        //for checking medicine expiry
                        Calendar cal=Calendar.getInstance();
                        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                        String month_name = month_date.format(cal.getTime());

                        DateFormat month_year = new SimpleDateFormat("yy"); // Just the year, with 2 digits
                        String year = month_year.format(Calendar.getInstance().getTime());
                        String currentdate=month_name.toUpperCase()+"."+" "+year;
                        SimpleDateFormat format = new SimpleDateFormat("MMM.yy");
                        try {
                            expiryDate = format.parse(expiry);
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

                            MedicineExpiry.setError("Your Medicine is Expired");
                        }

                        else{

                        progressDialog.setTitle("Loading..");
                        progressDialog.show();


                        StorageReference filepath = mStorage.getReference().child("ImagePost").child(imageUrl.getLastPathSegment());
//                    StorageReference profileRef=storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+"profile.jpg");
                        filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String t = task.getResult().toString();

                                        DatabaseReference newPost = mRef.push();
                                        newPost.child("MedicineName").setValue(name);
                                        newPost.child("MG").setValue(mg);
                                        newPost.child("ExpiryDate").setValue(expiry);
                                        newPost.child("Quantity").setValue(qun);
                                        newPost.child("Image").setValue(task.getResult().toString());
                                        newPost.child("ID").setValue(UserID);
                                        newPost.child("MedicineId").setValue(newPost.getKey());
                                        progressDialog.dismiss();
                                        startActivity(new Intent(getApplicationContext(),Fetch_Expiry_Image.class));


                                    }
                                });
                            }
                        });

                        }
                     }
                    else{
                        checkBox.setError("Please Accept the Terms and condition");

                    }
                }
                else{
                    Toast.makeText(Medicine_2.this, "Check all the required filed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}