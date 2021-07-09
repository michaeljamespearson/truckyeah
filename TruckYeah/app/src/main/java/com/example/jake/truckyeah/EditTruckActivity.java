package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
/*
Role of this class: Allows the Truck to edit their information including their password,
address and phone number
Dependencies: Uses the AccessDatabase class to get a reference of the database and also the Truck
class to cast objects as they are pulled from the database.
 */

public class EditTruckActivity extends AppCompatActivity {
    Truck finalTruck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_truck);
        final EditText address = (EditText) findViewById(R.id.etAddressEdit);
        final EditText phoneNumber = (EditText) findViewById(R.id.etPhoneNumberEdit);
        final EditText password = (EditText) findViewById(R.id.etPasswordEdit);
        final String username = getIntent().getStringExtra("truckUsername");
        final Button submit =(Button) findViewById(R.id.bSubmitEdit);
        ImageButton logoutButton= (ImageButton) findViewById(R.id.bLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finalTruck.setUsername(usernameNew.getText().toString());
                AccessDatabase a = new AccessDatabase();
                DatabaseReference currDatabase = a.getCurrDatabase();
                DatabaseReference truckData = currDatabase.child("truck user");
                truckData.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> truck = dataSnapshot.getChildren();
                                for (DataSnapshot t: truck){
                                    Truck truck1 = t.getValue(Truck.class);
                                    if (truck1 != null) {
                                        if (truck1.getUsername().equals(username)){
                                            finalTruck = truck1;
                                            if (password.getText().toString().length() != 0) {
                                                String nonEncryptedPas = password.getText().toString();
                                                String curPas = getSHA256Hash(nonEncryptedPas);
                                                finalTruck.setPassword(curPas);
                                            }
                                            if(phoneNumber.getText().toString().length() != 0) {
                                                finalTruck.setPhoneNumber(phoneNumber.getText().toString());
                                            }
                                            if(address.getText().toString().length() != 0) {
                                                finalTruck.setAddress(address.getText().toString());
                                            }
                                            AccessDatabase ad= new AccessDatabase();
                                            ad.putTruckInDatabase(finalTruck.getUsername(),finalTruck);
                                            Toast.makeText(getApplicationContext(),
                                                    "Truck information updated successfully.",
                                                    Toast.LENGTH_LONG).show();
                                            Intent UserMain = new Intent(getApplicationContext(),
                                                    CurrentOrdersTruckActivity.class);
                                            UserMain.putExtra("Username",finalTruck.getUsername());
                                            startActivity(UserMain);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

            }
        });



        ImageButton goToCurrentOrders = (ImageButton) findViewById(R.id.currentOrdersButton);
        ImageButton menuBut = (ImageButton) findViewById(R.id.menuButton);

        //link other pages in toolbar
        goToCurrentOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CurrentOrdersTruckActivity.class);
                i.putExtra("Username", username);
                startActivity(i);
            }
        });

        menuBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TrucksMenuActivity.class);
                i.putExtra("truckUsername", username);
                startActivity(i);
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

    /*
    XML:
        display Truck name and user name at top
        new phone number edit text box
        new hours edit text box
        new address edit text box
        submit button at bottom
        still have toolbar that takes you to current orders and my menu
     */

    /*
    Back end
        use setter to change the truck object
        push this object back onto the database
     */
}