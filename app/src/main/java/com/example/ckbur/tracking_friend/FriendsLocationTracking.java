package com.example.ckbur.tracking_friend;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DecimalFormat;

public class FriendsLocationTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String email;
    DatabaseReference locations;

    private double lat, lng;

    private String UserEmail;

    LatLng friendlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_location_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //reference to database
                locations = FirebaseDatabase.getInstance().getReference("Locations");


        //getting intent value from onlineList


        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
            lat = getIntent().getDoubleExtra("lat", 0.00);
            lng = getIntent().getDoubleExtra("lon", 0.00);
        }
        if (!TextUtils.isEmpty(email)){

            LoadLocationForThisUser(email);



        }


    }

    private void LoadLocationForThisUser(String email) {

        Query user_location = locations.orderByChild("email").equalTo(email);

        user_location.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Tracking tracking=dataSnapshot.getValue(Tracking.class);


                LatLng friendlocation = new LatLng(Double.parseDouble(tracking.getLat()),
                Double.parseDouble(tracking.getLng()));


                //Create location from user cordinates

                Location currentuser = new Location("");
                currentuser.setLatitude(lat);
                currentuser.setLongitude(lng);


                //create location from friend cordinates

                Location friend = new Location("");
                friend.setLatitude(Double.parseDouble(tracking.getLat()));
                friend.setLongitude(Double.parseDouble(tracking.getLng()));

                //creating function for calculating distance between location

                distance(currentuser, friend);

//add current marker in map

                mMap.addMarker(new MarkerOptions()
                        .position(friendlocation)
                        .title(tracking.getEmail())
                        .snippet("Distance " + new DecimalFormat("#.#").format(distance(currentuser, friend)))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));


                //create marker for current user...
                LatLng current = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));


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

    }

    private double distance (Location currentuser, Location friend){

        double theta = currentuser.getLongitude() - friend.getLongitude();
        double dist = Math.sin(deg2rad(currentuser.getLatitude()))
                * Math.sin(deg2rad(friend.getLatitude()))
                * Math.cos(deg2rad(currentuser.getLatitude()))
                * Math.cos(deg2rad(friend.getLatitude()))
                * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);

        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);

    }

    private double rad2deg ( double rad){
        return (rad * 180 / Math.PI);
    }

    private double deg2rad ( double deg){
        return (deg * Math.PI / 180.0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
