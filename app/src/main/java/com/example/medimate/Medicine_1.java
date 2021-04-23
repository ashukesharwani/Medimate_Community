package com.example.medimate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Medicine_1 extends AppCompatActivity {

    RecyclerView recView;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    Medicine_1_Adapter medicine1Adapter;
    List<Medicine_1_Model> medicineMdList;
    String UserID;
    FirebaseAuth fAuth;
    BottomNavigationView bottomNavigationView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog mProgressDialog;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_1);

        fAuth = FirebaseAuth.getInstance();
        UserID=fAuth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("Student");//.orderByChild("ID").equalTo(UserID);
        mStorage= FirebaseStorage.getInstance();

        swipeRefreshLayout=findViewById(R.id.swiperefresh);


        recView=findViewById(R.id.recView);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(this));

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();

        medicineMdList=new ArrayList<Medicine_1_Model>();
        medicine1Adapter=new Medicine_1_Adapter(Medicine_1.this,medicineMdList);
        recView.setAdapter(medicine1Adapter);

        Query query = FirebaseDatabase.getInstance().getReference().child("Student").orderByChild("ID").equalTo((UserID).toString());

        query.addListenerForSingleValueEvent(valueEventListener);
        mProgressDialog.dismiss();


       //for the refresh

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                medicine1Adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });



        //Initialize and Assign Varible
        bottomNavigationView=findViewById(R.id.bottom_navigation);

        //set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.medicine_2);

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

                        return true;

                    case R.id.medicine_3:
                        startActivity(new Intent(getApplicationContext(),Medicine_2.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            medicineMdList.clear();
            if(snapshot.exists()){
                for(DataSnapshot datasnapshot :  snapshot.getChildren()){
                    Medicine_1_Model model=datasnapshot.getValue(Medicine_1_Model.class);
                    medicineMdList.add(model);
                }
                medicine1Adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };




}