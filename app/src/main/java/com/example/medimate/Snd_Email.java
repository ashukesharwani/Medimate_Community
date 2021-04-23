package com.example.medimate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.Authenticator;
import java.util.Properties;

public class Snd_Email extends AppCompatActivity {
    EditText em_To,em_Subject,em_Topic;
    Button em_Send;
    String FullName,Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snd__email);

        Intent intent=getIntent();
        FullName=intent.getStringExtra("fullName");
        Email=intent.getStringExtra("email");

        em_To=findViewById(R.id.em_To);
        em_Subject=findViewById(R.id.em_Subject);
        em_Topic=findViewById(R.id.em_Topic);
        em_Send=findViewById(R.id.em_send);

       em_To.setText(Email);

        em_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+ em_To.getText().toString()));
                i.putExtra(Intent.EXTRA_SUBJECT,em_Subject.getText().toString());
                i.putExtra(Intent.EXTRA_TEXT,em_Topic.getText().toString());
                startActivity(i);
            }
        });








    }
}