package com.example.jake.truckyeah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
/*
Role of this class: Reads in the log in information the truck user has provided, verifies that it is correct,
and passes the truck to a page that displays their current orders
Dependencies: None
 */

public class LoginTruckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_login);
        final Button bTruckLogin = (Button) findViewById(R.id.bTruckLogin);
        final EditText etTruckUsername = (EditText) findViewById(R.id.etTruckEmail);
        final EditText etTruckPassword = (EditText) findViewById(R.id.etTruckPassword);
        final DatabaseReference currDatabase;
        currDatabase = FirebaseDatabase.getInstance().getReference();
        bTruckLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currDatabase.child("truck user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.child("username").getValue().equals(etTruckUsername.getText().toString())) {
                                if (data.child("password").getValue().equals(getSHA256Hash(etTruckPassword.getText().toString()))) {
                                    Toast.makeText(getApplicationContext(),
                                            "Welcome!",
                                            Toast.LENGTH_LONG).show();
                                    Intent TruckMain = new Intent(getApplicationContext(), CurrentOrdersTruckActivity.class);
                                    TruckMain.putExtra("Username", etTruckUsername.getText().toString());
                                    startActivity(TruckMain);
                                    }
                                else {
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid Credentials - Please Try Again",
                                            Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        Toast.makeText(getApplicationContext(),
                                "Invalid Credentials - Please Try Again",
                                Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });
        }

    private static String getSHA256Hash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash);
        }catch(Exception ex) {
            System.out.println("hi");
            ex.printStackTrace();
        }
        return result;
    }


    private static String bytesToHex(byte[] hash) {
        StringBuffer res = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) res.append('0');
            res.append(hex);
        }
        return res.toString();
    }
    }
