package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    private CardView CardView1, CardView2, CardView3, CardView4, CardView5, CardView6;
    StorageReference storageReference;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth=FirebaseAuth.getInstance();
        userId=fAuth.getCurrentUser().getUid();
        fStore=FirebaseFirestore.getInstance();

        CardView1 = findViewById(R.id.card_View1);
        CardView2 = findViewById(R.id.card_View2);
        CardView3 = findViewById(R.id.card_View3);
        CardView4 = findViewById(R.id.card_View4);
        CardView5 = findViewById(R.id.card_View5);
        CardView6 = findViewById(R.id.card_View6);

        CardView1.setOnClickListener(this);
        CardView2.setOnClickListener(this);
        CardView3.setOnClickListener(this);
        CardView4.setOnClickListener(this);
        CardView5.setOnClickListener(this);
        CardView6.setOnClickListener(this);


        //# Navigation drawer
        // ************** Hooks ***************
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // ************** Tool bar******************
        setSupportActionBar(toolbar);


        // ******************* Navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // to make menu clickable
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        //#

        updateNavHeader();
    }


    // to avoid the close the application on back pressed
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_Blood_Donation:
                Intent intent = new Intent(MainActivity.this, Blood_Donation.class);
                startActivity(intent);

                break;
            case R.id.nav_Blood_Seeker:
                Intent intent_1 = new Intent(MainActivity.this, bloodSeeking.class);
                startActivity(intent_1);
                break;

            case R.id.nav_Profile:
                Intent intent_2 = new Intent(MainActivity.this, Profile.class);
                startActivity(intent_2);
                break;

            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
                break;

            case R.id.nav_Medicine_Donation:
                Intent intent_3 = new Intent(MainActivity.this, Medicine_Donation.class);
                startActivity(intent_3);
                break;

            case R.id.nav_Medicine_Seeker:
                Intent intent_4 = new Intent(MainActivity.this, Medicine_Seeker.class);
                startActivity(intent_4);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //************for the cardView************
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {

            case R.id.card_View1:
                i = new Intent(this, Blood_Donation.class);
                startActivity(i);
                break;
            case R.id.card_View2:
                i = new Intent(this, bloodSeeking.class);
                startActivity(i);
                break;
            case R.id.card_View3:
                i = new Intent(this, Medicine_Donation.class);
                startActivity(i);
                break;
            case R.id.card_View4:
                i = new Intent(this, Medicine_Seeker.class);
                startActivity(i);
                break;
            case R.id.card_View5:
                i = new Intent(this,News.class);
                startActivity(i);
                break;

            case R.id.card_View6:
                i = new Intent(this, MythsAndFacts.class);
                startActivity(i);
                break;

            default:
                break;
        }

    }

    public void updateNavHeader(){
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0 );
        TextView mName = headerView.findViewById(R.id.pro_Name);
       // mName.setText("you text here ");
        TextView mEmail   = headerView.findViewById(R.id.pro_Email);
        ImageView mPic  =headerView.findViewById(R.id.pro_Pic);


        // for Displaying the image
        storageReference= FirebaseStorage.getInstance().getReference();

        StorageReference profileRef=storageReference.child("users/"+ fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mPic);
            }
        });

       // for displaying the name and email
        ///fetching the data from the database
        DocumentReference documentReference=fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                mName.setText(documentSnapshot.getString("FName"));
                mEmail.setText(documentSnapshot.getString("Email"));

            }
        });


    }
}