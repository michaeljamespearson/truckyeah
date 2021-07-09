package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
Role of this class: Reads in the log in information the user has provided, verifies that it is correct,
and passes the user to the page they see after logging in
Dependencies: None
 */
public class LoginUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        final Button bUserLogin = (Button) findViewById(R.id.bUserLogin);
        final EditText etUserUsername = (EditText) findViewById(R.id.etUserEmail);
        final EditText etUserPassword = (EditText) findViewById(R.id.etUserPassword);
        final DatabaseReference currDatabase;
        currDatabase = FirebaseDatabase.getInstance().getReference();
        bUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currDatabase.child("ind user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.child("username").getValue().equals(etUserUsername.getText().toString())) {
                                if (data.child("password").getValue().equals(getSHA256Hash(etUserPassword.getText().toString()))) {
                                    Toast.makeText(getApplicationContext(),
                                            "Welcome!",
                                            Toast.LENGTH_LONG).show();
                                    Intent UserMain = new Intent(getApplicationContext(), TruckListActivity.class);
                                    UserMain.putExtra("Username", etUserUsername.getText().toString());
                                    startActivity(UserMain);
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
