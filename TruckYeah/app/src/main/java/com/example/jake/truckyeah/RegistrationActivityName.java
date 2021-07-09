package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.security.MessageDigest;
/*
Role of this class: When making a new account, this is the page they are redirected to. They
select if they want to make a truck or user account, input information and are redirected accordingly.
Dependencies: None
 */
public class RegistrationActivityName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_name);

    final Button submit = (Button) findViewById(R.id.bSubmitRegistration);
    final RadioButton user = (RadioButton) findViewById(R.id.rbNewUser);
    final RadioButton owner = (RadioButton) findViewById(R.id.rbNewTruck);
    final RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
    final EditText username = (EditText) findViewById(R.id.etUsernameRegistration);
    final EditText confirmUser = (EditText) findViewById(R.id.etUsernameConfirmation);
    final EditText password = (EditText) findViewById(R.id.etPasswordRegistration);


    //signUp Intent
        submit.setOnClickListener(new View.OnClickListener() {

        //int i = group.getCheckedRadioButtonId();
        @Override
        public void onClick(View view) {
            String curUser = username.getText().toString();
            String curUserConfirm = confirmUser.getText().toString();
            //String curPas = password.getText().toString();

            String nonEncryptedPas = password.getText().toString();
            String curPas = getSHA256Hash(nonEncryptedPas);

            if (curUser.length() == 0 || curUserConfirm.length() == 0
                    || curPas.length() ==0) {
                Toast.makeText(getApplicationContext(),
                        "Information missing. Please fill out all fields.",
                        Toast.LENGTH_LONG).show();
            }
            else if (!curUser.equals(curUserConfirm)) {
                Toast.makeText(getApplicationContext(),
                        "Usernames do not match", Toast.LENGTH_LONG).show();
            }
            else  if(group.getCheckedRadioButtonId() == user.getId()){
                Toast.makeText(getApplicationContext(),
                        "Your account has been successfully created!",
                        Toast.LENGTH_LONG).show();
                //submit.setClickable(true);
                User newUser = new User(curUser, curPas);
                AccessDatabase curDatabse = new AccessDatabase();
                curDatabse.putUserInDatabase(curUser, newUser);
                    /*
                    String userID = currDatabase.child("user").push().getKey();
                   // currDatabase.child("user").push().setValue(newUser);
                   currDatabase.child(userID).setValue(newUser);
                   */
//                currDatabase.child("ind user").child(curEmail).push().getKey();
//                currDatabase.child("ind user").child(curEmail).setValue(newUser);

                Intent UserMain = new Intent(getApplicationContext(), TruckListActivity.class);
                UserMain.putExtra("Username", curUser);
                startActivity(UserMain);
            }
            else if(group.getCheckedRadioButtonId() == owner.getId()){

                //  TruckUser newTruck = new TruckUser(curEmail, curPas);
                String[] signInfo = new String[2];
                signInfo [0] = curUser;
                signInfo [1] = curPas;
                //  currDatabase.child("truck user").child(curEmail).push().getKey();
                //currDatabase.child("truck user").child(curEmail).setValue(newTruck);

                Toast.makeText(getApplicationContext(),
                        "Your account has been successfully created!",
                        Toast.LENGTH_LONG).show();
                submit.setClickable(true);

                    /*
                    instead of taking them to truck main, want to take it to new page that allows them
                    to add truck name, truck address and truck hours, which is them stored in the database
                    so maybe should just add to the database there
                     */

                Intent TruckRegister = new Intent(RegistrationActivityName.this.getApplicationContext(), RegistrationActivityTruck.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("truckInfo", signInfo);
                TruckRegister.putExtras(bundle);
                startActivity(TruckRegister);
                // Intent TruckMain = new Intent(getApplicationContext(), TruckMainActivity.class);
                //startActivity(TruckMain);

            }
            else{
                //submit.setClickable(false);
                Toast.makeText(getApplicationContext(),
                        "Please select an option", Toast.LENGTH_LONG).show();
            }
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

    //xml
        //Username
        //Confirm Username
        //Password
        //User or Truck Checkbox
        //Next button
    //backend
        //if user clicked
            //validate credentials
            //add username to database
            //add password to database
            //On submit, go to trucklist activity
        //if truck is clicked
            //validate credentials
            //pass username and password to Registration 2 activity



