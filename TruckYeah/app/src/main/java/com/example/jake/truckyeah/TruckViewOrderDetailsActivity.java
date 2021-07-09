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

public class TruckViewOrderDetailsActivity extends AppCompatActivity {

    Truck finalTruck;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_view_order_details);
        Bundle bundle = getIntent().getExtras();
        final Order userOrder = (Order) bundle.get("order");
        List<MenuItem> allItems = new LinkedList<MenuItem>();
        allItems = userOrder.getOrder();
        ListView orderScreen = (ListView) findViewById(R.id.ordersList);
        final ArrayAdapter<MenuItem> adapter = new ArrayAdapter<MenuItem>(this,android.R.layout.simple_dropdown_item_1line,allItems);
        orderScreen.setAdapter(adapter);
        TextView tv = (TextView) findViewById(R.id.orderFor);
        tv.setText("Order for: " + userOrder.getUserOrder());

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
                                if (truck1.getTruckName().equals(userOrder.getTruckOrder())){
                                    finalTruck = truck1;
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        b = (Button) findViewById(R.id.completeOrder);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalTruck.deleteOrder(userOrder)) {
                    AccessDatabase a  = new AccessDatabase();
                    a.putTruckInDatabase(finalTruck.getUsername(), finalTruck);
                    Toast.makeText(getApplicationContext(),
                            "Order Completed. Thank you!",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), CurrentOrdersTruckActivity.class);
                    i.putExtra("Username", finalTruck.getUsername());
                    startActivity(i);
                }
            }
        });




    }




}
