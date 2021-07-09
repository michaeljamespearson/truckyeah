package com.example.jake.truckyeah;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/*
Role of this class: When logged in as a truck, this page allows you to see all your current MenuItems
that you have added.
Dependencies: Menuitem 
 */
public class TrucksMenuActivity extends AppCompatActivity {

    List<MenuItem> menu;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucks_menu);
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
        username = getIntent().getStringExtra("truckUsername");

        ImageButton goToCurrentOrders = (ImageButton) findViewById(R.id.currentOrdersButton);
        ImageButton goToEditTruck = (ImageButton) findViewById(R.id.truckInfo);
        FloatingActionButton addMenuItem = (FloatingActionButton) findViewById(R.id.AddMenuItemButton);
        ListView truckMenuList = (ListView) findViewById(R.id.TruckMenuListView);

        //link other pages in toolbar
        goToCurrentOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CurrentOrdersTruckActivity.class);
                i.putExtra("Username", username);
                startActivity(i);
            }
        });

        goToEditTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditTruckActivity.class);
                i.putExtra("Username", username);
                startActivity(i);
            }
        });

        //link add menu item button
        addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMenuItemTruckActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //recieve new Menu Item and add to list


        //populate ListView
        AccessDatabase a = new AccessDatabase();
        DatabaseReference currDatabase = a.getCurrDatabase();

        Log.v("Database", currDatabase.child("truck user").getKey());
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
                                    ListView truckMenuList = (ListView) findViewById(R.id.TruckMenuListView);
                                    List<MenuItem> menu = truck1.getTruckMenu();
                                    List<MenuItem> temp = new LinkedList<MenuItem>();
                                    for (MenuItem x : menu) {
                                        if (!x.getItemName().equals("DEFAULT")) {
                                            temp.add(x);
                                        }
                                    }
                                    menu = temp;
                                    ListAdapter truckMenuApadter = new ArrayAdapter<MenuItem>(TrucksMenuActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1, menu){
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent){
                                            View view = super.getView(position, convertView, parent);
                                            TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                            tv.setTextColor(Color.BLACK);
                                            return tv;
                                        }
                                    };
                                    truckMenuList.setAdapter(truckMenuApadter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                final MenuItem newItem = (MenuItem) data.getExtras().get("NewItem");
                AccessDatabase a = new AccessDatabase();
                DatabaseReference currDatabase = a.getCurrDatabase();

                Log.v("Database", currDatabase.child("truck user").getKey());
                DatabaseReference truckData = currDatabase.child("truck user");



                truckData.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> truck = dataSnapshot.getChildren();
                                for (DataSnapshot t: truck){
                                    Truck truck1 = t.getValue(Truck.class);
                                    if (truck1 != null) {
                                        List<MenuItem> men = truck1.getTruckMenu();
                                        if (truck1.getUsername().equals(username)) {
                                            Log.v("What is username ", username);
                                            truck1.addMenuItem(newItem);
                                            AccessDatabase r = new AccessDatabase();
                                            r.putTruckInDatabase(truck1.getUsername(),truck1);
                                            ListView truckMenuList = (ListView) findViewById(R.id.TruckMenuListView);
                                            List<MenuItem> menu = truck1.getTruckMenu();
                                            List<MenuItem> temp = new LinkedList<MenuItem>();
                                            for (MenuItem x : menu) {
                                                if (!x.getItemName().equals("DEFAULT")) {
                                                    temp.add(x);
                                                }
                                            }
                                            menu = temp;
                                            ListAdapter truckMenuApadter = new ArrayAdapter<MenuItem>(TrucksMenuActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1, menu){
                                                @Override
                                                public View getView(int position, View convertView, ViewGroup parent){
                                                    View view = super.getView(position, convertView, parent);
                                                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                                    tv.setTextColor(Color.BLACK);
                                                    return tv;
                                                }
                                            };
                                            truckMenuList.setAdapter(truckMenuApadter);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

                ImageButton currentOrdersTruckButton =  (ImageButton) findViewById(R.id.currentOrdersButton);
                ImageButton editTruckButton =  (ImageButton) findViewById(R.id.truckInfo);
                ImageButton menuItemButton =  (ImageButton) findViewById(R.id.menuButton);
                currentOrdersTruckButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent currentorders = new Intent(getApplicationContext(), CurrentOrdersTruckActivity.class);
                        currentorders.putExtra("Username", username);
                        startActivity(currentorders);
                    }
                });
                editTruckButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent edittruck = new Intent(getApplicationContext(), EditTruckActivity.class);
                        edittruck.putExtra("truckUsername", username);
                        startActivity(edittruck);
                    }
                });


            }
        }
    }
    /*
    XML:
        Truck name at top
        displays a list of menu items - each one is a button that can be clicked
        add menu item button
        toolbar at the bottom that lets you go to : currentOrdersTruck and EditTruck
     */
    /*
    Back End
        when you click on a menu item takes you to page that allows you to get/set info
        add menu item button redirects you to page to add new menu item button
     */



    public List<MenuItem> getTruckMenu(){
        List<MenuItem> defaultMenu = new ArrayList<MenuItem>();
        defaultMenu.add(new MenuItem("salad", 3.50));
        return defaultMenu;
    }
}