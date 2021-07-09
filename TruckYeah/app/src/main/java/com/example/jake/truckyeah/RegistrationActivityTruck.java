package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/*
Role of this class: If a new user has chosen to make a truck account, there is additional information
they have to fill out, and this is the page where they have to give their truck name, address
and phone number. This information is then stored in the database.
Dependencies: Access Database, Truck
 */

public class RegistrationActivityTruck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_truck);
        final EditText address = (EditText) findViewById(R.id.address);
        final EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);


        Button addTruck = (Button) findViewById(R.id.addTruck);

        //signUp Intent
        addTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curAddr = address.getText().toString();
                String curNum = phoneNumber.getText().toString();
                if (curAddr.length() == 0 || curNum.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Information missing. Please fill out all fields.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    AccessDatabase currDatabase = new AccessDatabase();
                    Bundle b = getIntent().getExtras();
                    String[] signInfo = b.getStringArray("truckInfo");
                    String email = signInfo[0];
                    String pw = signInfo[1];
                    Truck newTruck = new Truck(email, pw, email);
                    newTruck.setAddress(address.getText().toString());
                    newTruck.setPhoneNumber(phoneNumber.getText().toString());
                    currDatabase.putTruckInDatabase(email, newTruck);
//                    final DatabaseReference currDatabase;
//                    currDatabase = FirebaseDatabase.getInstance().getReference();
//                    Bundle bundle = getIntent().getExtras();
//                    String[] signInfo = bundle.getStringArray("truckInfo");
//
//                    //creating new truck object
//                    TruckUser newTruck = new TruckUser(signInfo[0], signInfo[1]);
//                    newTruck.addAddress(curAddr);
//                    newTruck.addTruckName(curName);
//                    newTruck.addPhoneNum(curNum);
//                    //add the truck to the database
//                    currDatabase.child("truck user").child(signInfo[0]).push().getKey();
//                    currDatabase.child("truck user").child(signInfo[0]).setValue(newTruck);

                    Intent TruckMain = new Intent(RegistrationActivityTruck.this.getApplicationContext(), CurrentOrdersTruckActivity.class);
                    TruckMain.putExtra("Username", newTruck.getUsername());
                    startActivity(TruckMain);
                }

            }
        });
/*
        final Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logout = new Intent(getApplicationContext(), Login.class);
                startActivity(logout);
            }
        });
        */
    }

}




//xml
        //Get truck name
        //Get location
        //Get phone number
        //submit button
    //backend
        //Create new truck object
        //Add truck object to the database
        //on submit, direct to current orders activity


