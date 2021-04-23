package com.example.medimate;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Medicine_Seeker extends AppCompatActivity {


    FirebaseDatabase db;
    DatabaseReference root;
    RecyclerView recyclerView;
    MedicineAdapter adapter;
    List<MedicineModel> list;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_medicine__seeker);

        db= FirebaseDatabase.getInstance();
        root=db.getReference().child("Student");

        recyclerView=findViewById(R.id.recyclerViewMedicine);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();

        list = new ArrayList<MedicineModel>();
        adapter=new MedicineAdapter(Medicine_Seeker.this, list);
        recyclerView.setAdapter(adapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                     String MedicineName=dataSnapshot.child("MedicineName").getValue(String.class);
//                    String MG=dataSnapshot.child("MG").getValue(String.class)
                   // MedicineModel model =new MedicineModel(MedicineName,MG,ExpiryDate,Quantity,Image,ID);
                    MedicineModel model=dataSnapshot.getValue(MedicineModel.class);
                    list.add(model);
                    //Log.d("FbDatabase",model.toString());

                }
                mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem etSearch = menu.findItem(R.id.search_bar);
        android.widget.SearchView searchView = (android.widget.SearchView) etSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}


