package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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

        Button userInfoActivityButton =  (Button) findViewById(R.id.userInfoButton);
        Button truckListActivityButton =  (Button) findViewById(R.id.truckListButton);
        userInfoActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent userinfo = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(userinfo);
            }
        });
        truckListActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trucklist = new Intent(getApplicationContext(), TruckListActivity.class);
                startActivity(trucklist);
            }
        });

    }

    public void goToMap(View curView) {
        Intent i = new Intent(getApplicationContext(), ViewTrucksMapActivity.class);
        startActivity(i);
    }
    /*
    XML
        displays username
     */
    /*
    no back end
     */
}
