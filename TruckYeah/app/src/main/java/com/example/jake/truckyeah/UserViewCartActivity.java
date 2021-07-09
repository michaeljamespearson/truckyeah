
package com.example.jake.truckyeah;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UserViewCartActivity extends AppCompatActivity {

    private List<MenuItem> alItemsInOrder;
    TextView priceButton;
    Button checkOut;
    String truckName;
    Truck finalTruck;
    ImageButton map;
    ImageButton cart;
    ImageButton truckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_cart);
        Intent intentA = getIntent();
        Bundle bundle = getIntent().getExtras();
        final Order userOrder = (Order) bundle.get("order");
        priceButton = findViewById(R.id.priceText);
        checkOut = findViewById(R.id.checkout);
        truckName = userOrder.getTruckOrder();
        map = findViewById(R.id.mapButton);
        cart = findViewById(R.id.myCartButton);
        truckList = findViewById(R.id.truckListButton);



        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewTrucksMapActivity.class);
                startActivity(i);
            }
        });

        truckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TruckListActivity.class);
                i.putExtra("Username", userOrder.getUserOrder());
                startActivity(i);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserViewCurrentOrdersActivity.class);
                i.putExtra("username", userOrder.getUserOrder());
                startActivity(i);

            }
        });


        getMenuItems(userOrder);
        final String username=getIntent().getStringExtra("Username");

        this.setTitle(username);
        ListAdapter truckAdapter = new ArrayAdapter<MenuItem>(this, android.R.layout.simple_list_item_1, alItemsInOrder);
        ListView truckListView = (ListView) findViewById(R.id.truckList);
        truckListView.setAdapter(truckAdapter);
        truckListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String truckName = String.valueOf(adapterView.getItemAtPosition(i));
                    }
                }


        );
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

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //ADD THE ORDER TO A TRUCK
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
                                        if (truck1.getTruckName().equals(truckName)){
                                            finalTruck = truck1;
                                            if(userOrder.size() != 0) {
                                                finalTruck.addOrder(userOrder);
                                                AccessDatabase ad = new AccessDatabase();
                                                ad.putTruckInDatabase(finalTruck.getUsername(), finalTruck);
                                                Toast.makeText(getApplicationContext(),

                                                        "Your order has been sent. Thank you!",
                                                        Toast.LENGTH_LONG).show();
                                                checkOut.setClickable(false);
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),

                                                        "Your order is currently empty. Sorry!",
                                                        Toast.LENGTH_LONG).show();
                                            }
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

    }

    public void getMenuItems(Order curOrder) {
        alItemsInOrder = new LinkedList <MenuItem>();
        alItemsInOrder =  curOrder.getOrder();
        double totalPrice = 0;
        for (int i = 0; i < alItemsInOrder.size(); i++) {
            totalPrice += alItemsInOrder.get(i).getPrice();
        }
        priceButton.setText("Total Price: $" + totalPrice);
    }
}

