package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Verify_Email extends AppCompatActivity {

    Button resendCode,verifyNext;
    TextView verifyMsg;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__email);

        resendCode=findViewById(R.id.resendCode);
        verifyMsg=findViewById(R.id.verifyMsg);
        verifyNext=findViewById(R.id.verifyNext);

        fAuth=FirebaseAuth.getInstance();

        // userId=fAuth.getCurrentUser().getUid();
         user=fAuth.getCurrentUser();
        //start form here
       if(!user.isEmailVerified()){
            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email has been sent ", Toast.LENGTH_SHORT).show();
                            resendCode.setVisibility(View.GONE);
                            verifyNext.setVisibility(View.VISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent"+ e.getMessage());
                        }
                    });

                }
            });

        }
        verifyNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.isEmailVerified()){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Verify_Email.this, "Please verify the email first", Toast.LENGTH_SHORT).show();
                    resendCode.setVisibility(View.VISIBLE);
                    verifyNext.setVisibility(View.GONE);
                }
            }
        });


    }

}