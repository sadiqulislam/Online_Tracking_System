package com.example.ckbur.tracking_friend;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private Button btnlogin;
    private final static int RC_SIGN_IN=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnlogin=(Button)findViewById(R.id.btnlogin);
        auth = FirebaseAuth.getInstance();


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (auth.getCurrentUser() != null) {

                    Intent i=new Intent(MainActivity.this,OnlineFriendsList.class);
                    startActivity(i);
                    finish();

                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            startnewActivity(resultCode,data);
        }
    }

    private void startnewActivity(int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Intent i=new Intent(MainActivity.this,OnlineFriendsList.class);
            startActivity(i);
            finish();

        }else{
            Toast.makeText(MainActivity.this,"Log In Failed",Toast.LENGTH_SHORT).show();
        }

    }
}
