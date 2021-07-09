package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
/*
Role of this class: Takes in input from a the truck user. The truck user provides a name, price and
description of the menu item they want to add to their menu. This is then reflected on their menu
from both their and the users perspective.
Dependencies: Uses an instance of the MenuItem class.
 */
public class AddMenuItemTruckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item_truck);


        final EditText itemName = (EditText) findViewById(R.id.ItemNameInput);
        final EditText itemPrice = (EditText) findViewById(R.id.ItemPriceInput);
        final EditText itemDescription = (EditText) findViewById(R.id.DescriptionInput);
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

        Button submit = (Button) findViewById(R.id.CreateItemButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemName.getText() != null){
                    MenuItem m = new MenuItem(itemName.getText().toString());
                    try{
                        m.setPrice(Double.parseDouble(itemPrice.getText().toString()));
                    }
                    catch (final NumberFormatException e){
                        m.setPrice(0.0);
                    }
                    if (itemDescription.getText().toString() != null){
                        m.setDescription(itemDescription.getText().toString());
                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("NewItem",m );
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }
    /*
    XML
        edit text box: item name, price, description
     */
    /*
    Back end
        add the new item to the menu object for that food truck
        push the new menu to the database
     */
}

