package com.example.medimate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class bloodSeeking extends AppCompatActivity {

    private static final int REQUEST_CALL =1 ;
    Spinner blood_groups;
    String[] Blood = { "A+","A-","B+","B-","O+","O-","AB+","AB-"};

    private RecyclerView mFirestoreList;
    private FirebaseFirestore fStore;
    private FirestorePagingAdapter adapter;
    Map<String,Object> object=null;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_seeking);

        blood_groups=findViewById(R.id.bloodgroup);

        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.users_lists);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Blood);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_groups.setAdapter(aa);
        String x=blood_groups.getSelectedItem().toString();
        //Query

        //CollectionReference colref=fStore.collection("users");
        //        Query query = colref.whereEqualTo("BloodGroup",x);

        Query query = fStore.collection("users");

        PagedList.Config config=new PagedList.Config.Builder()
                .setInitialLoadSizeHint(5)
                .setPageSize(3)
                .build();

        //RecyclerOptions

        FirestorePagingOptions<ModelUsers> options = new FirestorePagingOptions.Builder<ModelUsers>()
                .setQuery(query, config, ModelUsers.class)
                .build();

        adapter = new FirestorePagingAdapter<ModelUsers, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_users,parent,false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull ModelUsers model) {

                holder.user_name.setText(model.getFname());
                holder.user_phno.setText(model.getPhone());
                holder.user_bg.setText(model.getBloodGroup());
                holder.user_email.setText(model.getEmail());

                holder.user_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(bloodSeeking.this, "Call", Toast.LENGTH_SHORT).show();

                        String ph_no=holder.user_phno.getText().toString();
                        String tel="tel:"+ph_no;
                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        startActivity(intent);
                    }
                });

                /*blood_groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CollectionReference bg=fStore.collection("users");
                        String x=blood_groups.getSelectedItem().toString();
                        Query q=bg.whereEqualTo("BloodGroup","x");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

                /*blood_groups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(bloodSeeking.this, "Match found", Toast.LENGTH_SHORT).show();
                        Log.e("Blood Group:",x);
                        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (int i = 0; i < list.size(); i++) {
                                        object=list.get(i).getData();
                                        Log.e("data:",object.toString());
                                        String y= (String) holder.user_bg.getText().toString().trim();
                                        if(y.equals(x)){
                                            Toast.makeText(bloodSeeking.this, "Match found", Toast.LENGTH_SHORT).show();
                                            holder.user_name.setText(model.getFname());
                                            holder.user_phno.setText(model.getPhone());
                                            holder.user_bg.setText(y);
                                        }
                                    }
                                }
                            }
                        });
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        holder.user_name.setText(model.getFname());
                        holder.user_phno.setText(model.getPhone());
                        holder.user_bg.setText(model.getBloodGroup());
                    }
                });/*/


            /*protected void onLoadingStateChanged(@NonNull LoadingState state){
                super.onLoadingStateChanged(state);
                switch (state){
                    case LOADED:
                        Log.d("PAGING_LOG","Total Items Loaded:"+getItemCount());
                        break;
                        case LOADING_INITIAL:
                        break;
                    case FINISHED:
                        break;
                    case LOADING_MORE:
                        break;
                }
            }/*/


            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);


    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        //implements View.OnClickListener

        public TextView user_name;
        public TextView user_phno;
        public TextView user_bg;
        public TextView user_email;
        public ImageButton user_call;
        public Button user_request;

        public UserViewHolder(@NonNull View itemView){
            super(itemView);

            user_name=itemView.findViewById(R.id.donor_name);
            user_phno=itemView.findViewById(R.id.donor_contact);
            user_bg=itemView.findViewById(R.id.donor_bloodGroup);
            user_call=itemView.findViewById(R.id.imageButton);
            user_email=itemView.findViewById(R.id.donor_emailid);
            user_request=itemView.findViewById(R.id.donor_request);


            user_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(bloodSeeking.this, "Call", Toast.LENGTH_SHORT).show();
                    makePhoneCall();

                }
            });

            user_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send_email();
                }
            });

        }

        public void send_email(){
            String email=user_email.getText().toString();
            Intent i=new Intent(getApplicationContext(),send_email.class);
            i.putExtra("fullName",email);
            startActivity(i);
        }

        private void makePhoneCall() {
            if(ContextCompat.checkSelfPermission(bloodSeeking.this
                    , Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(bloodSeeking.this,new String[]{Manifest.permission.CALL_PHONE}
                        ,REQUEST_CALL);
            }
            else{
                phoneNumber=user_phno.getText().toString();
                String dial="tel:"+phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));

            }
        }

    }

    protected void onStop(){
        if(adapter!=null){
            adapter.stopListening();
        }
        super.onStop();

    }

    protected void onStart(){
       super.onStart();
       adapter.startListening();
    }

}