package com.example.medimate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fetch_Expiry_Image extends AppCompatActivity {
    Button getImage_btn;
    Button getText_btn,uploadbtn;
    ImageView showImage_img;
    TextView showText_txt;
    static final int REQUEST_IMAGE_CAMERA=1;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch__expiry__image);

        //Bind your XML view here
        getImage_btn=findViewById(R.id.btn_takePic);
        getText_btn=findViewById(R.id.btn_gettext);
        showText_txt=findViewById(R.id.txt_show_text);
        showImage_img=findViewById(R.id.img_imageview);
        uploadbtn=findViewById(R.id.uploadBtn);

        //Set OnClick event for getImage_btn Button to take image from camera
        getImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeImage(); //Function To capture image
            }
        });
        //Set OnClick event for getText_btn Button to get Text from image

        getText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTextFromImageFunction();
            }
        });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Fetch_Expiry_Image.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    private void GetTextFromImageFunction() {

        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational())
        {
            Toast.makeText(this, "Error occur", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame=new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray=textRecognizer.detect(frame);
            StringBuilder stringBuilder=new StringBuilder();
            for (int i=0; i<textBlockSparseArray.size(); i++)
            {
                TextBlock textBlock=textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            //Show the text to TextView
            // showText_txt.setText(stringBuilder.toString());
            //Thats All
            String expiry = stringBuilder.toString().trim();
            String regex = "(EXP(.|,)(\\s|)[A-Z]{0,3}(.|,)[0-9]{0,4}+)";
//            (EXP?+.[A-Z]{0,3}?.[0-9]{0,4}+)

            //Creating a pattern object
            Pattern pattern = Pattern.compile(regex);
            //Creating a Matcher object
            Matcher matcher = pattern.matcher(expiry);
            Log.e("value",expiry);

            // System.out.println("Digits in the given string are: ");
            if(matcher.find()) {

              //  Toast.makeText(this, "this is error", Toast.LENGTH_SHORT).show();
                showText_txt.setText(matcher.group());
                uploadbtn.setVisibility(View.VISIBLE);

            }
            else{
                Toast.makeText(this, "Try Again !! ERROR ", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void TakeImage() {
        Intent takeImageIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //
        if (takeImageIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takeImageIntent,REQUEST_IMAGE_CAMERA);
            getText_btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK && data != null) {
            //Crop image
            CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {//Here change reultCode to requestCode
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)//Please Change requestCode to resultCode
            {
                Uri resultURI = result.getUri();

                showImage_img.setImageURI(resultURI);//IT show image to image view
                bitmapDrawable = (BitmapDrawable) showImage_img.getDrawable();
                bitmap = bitmapDrawable.getBitmap();

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

//

    }
}