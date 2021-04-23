package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileFullName,profileEmail,profilePhone,profileLocality,profileCity;
    ImageView profileImageView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button saveBtn;
    FirebaseUser user;
    StorageReference storageReference;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data=getIntent();
        String fullName=data.getStringExtra("fullName");
        String email=data.getStringExtra("email");
        String phone=data.getStringExtra("phone");
        String locality=data.getStringExtra("locality");
        String city=data.getStringExtra("city");

        profileFullName=findViewById(R.id.profileFullName);
        profileEmail=findViewById(R.id.profileEmailAddress);
        profilePhone=findViewById(R.id.profilePhoneNo);
        profileLocality=findViewById(R.id.profileLocalityL);
        profileCity=findViewById(R.id.profileCityL);
        profileImageView=findViewById(R.id.profileImageView);

        saveBtn=findViewById(R.id.saveProfileInfo);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        user=fAuth.getCurrentUser();
// to display the images
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef=storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });///till here

        profileFullName.setText(fullName);
        profileEmail.setText(email);
        profilePhone.setText(phone);
        profileLocality.setText(locality);
        profileCity.setText(city);


        Log.d(TAG, "onCreate: "+ fullName +" "+ phone +" "+ locality +" "+ city);

        mProgressDialog.dismiss();


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the gallery for the changing image (I1)
                Intent openGalleryIntent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);

            }
        });


        //for updating data
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profilePhone.getText().toString().isEmpty()|| profileLocality.getText().toString().isEmpty()|| profileCity.getText().toString().isEmpty()){

                    Toast.makeText(EditProfile.this, "One or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emails = profileEmail.getText().toString();
                user.updateEmail(emails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef=fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited=new HashMap<>();
                        edited.put("Email",emails);
                        edited.put("FName",profileFullName.getText().toString());
                        edited.put("Phone",profilePhone.getText().toString());
                        edited.put("Locality",profileLocality.getText().toString());
                        edited.put("City",profileCity.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getApplicationContext(),Profile.class));
                                    finish();
                            }
                        });
                        Toast.makeText(EditProfile.this, "Email is changed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    // changing image(I2)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri=data.getData();
                // profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }
    //Chaging image (i3)
    private void uploadImageToFirebase(Uri imageUi) {
        //logic to upload the image to firebase storage
        StorageReference fileRef=storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageUi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed !! ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}