package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "TAG";
    EditText mFullName, mEmail, mPassword, mPhone, mLocality, mCity, mAadhar;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    Spinner spinBloodGroup;
    String[] Blood = { "A-"," A+ "," B+ "," B- "," AB+ ", " AB- ", " O+ "," O- "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mLocality = findViewById(R.id.locality);
        mCity = findViewById(R.id.city);
        mAadhar = findViewById(R.id.aadhar);

        mRegisterBtn = findViewById(R.id.loginBtn);
        mLoginBtn = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        // working for the  Blood spinner
        spinBloodGroup = findViewById(R.id.bloodgroup);
        spinBloodGroup.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having Blood list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Blood);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinBloodGroup.setAdapter(aa);

        // to Check whether user is already logined or not
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        final String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String phone = mPhone.getText().toString();
                String locality = mLocality.getText().toString();
                String city = mCity.getText().toString();
                String aadhar = mAadhar.getText().toString();
                String blood=spinBloodGroup.getSelectedItem().toString();

                //Checking email and password fields are empty are not
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password  is Required.");
                    return;
                }

                //Password must be more then six character
                if (password.length() < 6) {
                    mPassword.setError("Password must be more then six Characters");
                    return;
                }
                if(aadhar.length()!=12){
                    mAadhar.setError("Please Enter Valid aadhar NO.");
                    return;

                }


                if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Phone Number is required is Required.");
                    return;
                }
                if(TextUtils.isEmpty(locality)){
                    mLocality.setError("Locality is required is Required.");
                    return;
                }
                if(TextUtils.isEmpty(city)){
                    mCity.setError("City is required is Required.");
                    return;
                }
                if(TextUtils.isEmpty(aadhar)){
                    mAadhar.setError("AAdhar  is required is Required.");
                    return;
                }

                if(phone.matches(regexStr))
                {

                    progressBar.setVisibility(View.VISIBLE);

                    //Register the User in the firebase
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(Login.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                //Inserting the user data into firebase
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                // creating  data using Hash map method
                                Map<String, Object> user = new HashMap<>();
                                user.put("FName", fullName);
                                user.put("Email", email);
                                user.put("Phone", phone);
                                user.put("Locality", locality);
                                user.put("City", city);
                                user.put("Aadhar", aadhar);
                                user.put("BloodGroup", blood);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: User Profile is Created for" + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());

                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), Verify_Email.class));

                            } else {
                                Toast.makeText(Login.this, "ERROR !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    mPhone.setError("Please Enter Valid Phone number NO.");
                    return;
                }
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        //methods for spinner
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //to rectify the error
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}