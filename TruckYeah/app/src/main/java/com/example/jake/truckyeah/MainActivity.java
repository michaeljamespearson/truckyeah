package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
/*
Role of this class: This is the first page that indivduals see when they run the app. It
takes in user input if they want to log in to an existing account for a truck or a user or
if they want to create a new acccount and redirects them to a page accordingly.
Dependencies: None
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    final Button bContinue = (Button) findViewById(R.id.bContinue);
    final RadioButton rbUser = (RadioButton) findViewById(R.id.rbUser);
    final RadioButton rbNewUser = (RadioButton) findViewById(R.id.rbNewUser);
    final RadioButton rbOwner = (RadioButton) findViewById(R.id.rbTruckOwner);
    final RadioGroup rbgSelect = (RadioGroup) findViewById(R.id.radioGroup);

    bContinue.setOnClickListener(new View.OnClickListener() {

        //int i = group.getCheckedRadioButtonId();
        @Override
        public void onClick(View view) {
           if (rbgSelect.getCheckedRadioButtonId() == rbNewUser.getId()){
               Intent Register = new Intent(getApplicationContext(), RegistrationActivityName.class);
               startActivity(Register);
           }
           else if (rbgSelect.getCheckedRadioButtonId() == rbUser.getId()){
               Intent loginUser = new Intent(getApplicationContext(), LoginUserActivity.class);
               startActivity(loginUser);

            }
           else if (rbgSelect.getCheckedRadioButtonId() == rbOwner.getId()){
               Intent loginTruck = new Intent(getApplicationContext(), LoginTruckActivity.class);
               startActivity(loginTruck);
            }
        }

        //XML things
        //Image N/a
        //Set Username(Save in field)
        //Set Password(Save in field)
        //Truck or User Checkbox (Save in field)
        //Registration Button (Goes to Registration Activity)
        //Login Button (Goes to Truck or User)

        //Backend
        //Get Username Text
        //Get Password Text
        //Get Checkbox Information
        //Validate Credentials in Database


    });
    }
}