package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
/*
 */

public class EditMenuItemTruckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item_truck);
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

    }
    /*
    XML
        edit text box for : name of item; price, description
     */
    /*
    Back end
        get set depending on what is changed
            only set fields that have information in them
         edit the actual menu item
            push it back to the database
     */
}
