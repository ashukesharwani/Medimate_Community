package com.example.medimate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class send_email extends AppCompatActivity {
    EditText receiver, subject, text;
    Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        receiver = findViewById(R.id.to);
        subject = findViewById(R.id.subject);
        text = findViewById(R.id.message);
        submit = findViewById(R.id.Submit);

        String txt=getIntent().getExtras().getString("fullName");

        receiver.setText(txt);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + receiver.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, text.getText().toString());
                startActivity(intent);
            }
        });


    }


}
