package com.example.ckbur.tracking_friend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TestActivity extends AppCompatActivity {

    private String emaill;
    DatabaseReference locations;

    private double lat,lng;
    private TextView textView;

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        textView=(TextView)findViewById(R.id.txt);

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Locations");

        Intent intent=getIntent();

        emaill=intent.getStringExtra("email");

        Query query=databaseReference.orderByChild("email").equalTo(emaill);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Tracking tracking=dataSnapshot.getValue(Tracking.class);
                String laat=tracking.getLat();
                String laan=tracking.getLng();

                textView.setText("lat is "+laat+"lan is "+laan);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // lat=intent.getDoubleExtra("lat",0.0);
        // lng=getIntent().getDoubleExtra("lon",0.00);







    }
}
