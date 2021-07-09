
package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/*
Role of this class: This class pulls from the database all the orders that have not been completed
 of the current truck user that is signed in, and displays them.
Dependencies: Uses AccessDatabase class to get an instance of the database. Utilizes the Truck class
to cast the objects being pulled from the Database back into Truck objects. Uses the Order class
for the same purpose.
 */
public class CurrentOrdersTruckActivity extends AppCompatActivity {

    String[] fakeorders = new String[] {"Order 1", "Order 2", "Order 3", "Order 4", "Order 5", "Order 6",
            "Order 7", "Order 8", "Order 9", "Order 10", "Order 11", "Order 12"};


    //Order[] orders = hardCodeOrders();

    List<Order> listOrders;
    List<Order> stringOrders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders_truck);

        final String username = getIntent().getStringExtra("Username");
        this.setTitle(username);
        listOrders = new ArrayList<Order>();
        ListView orderScreen = (ListView) findViewById(R.id.CurrentOrder);
        final ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this,android.R.layout.simple_dropdown_item_1line,stringOrders);
        orderScreen.setAdapter(adapter);
        DatabaseReference curr = FirebaseDatabase.getInstance().getReference();
        DatabaseReference truckReference = curr.child("truck user");
        truckReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot t : dataSnapshot.getChildren()) {
                    if(t != null) {
                        if(t.getKey().equals(username)) {
//                        Log.v("Adding To TruckList", "0");
                            Truck truck = (Truck) t.getValue(Truck.class);
                            for (Order o: truck.getOrders()){
                               // Log.v("what is order", o.toString());
                                    if(!o.getUserOrder().equals("DEFAULT")) {
                                        stringOrders.add(o);

                                    }
                            }
//                        Log.v("TruckListSize", Integer.toString(truckList.size()));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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


        ImageButton currentOrdersTruckButton =  (ImageButton) findViewById(R.id.currentOrdersButton);
        ImageButton editTruckButton =  (ImageButton) findViewById(R.id.truckInfo);
        ImageButton menuItemButton =  (ImageButton) findViewById(R.id.menuButton);

        editTruckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent edittruck = new Intent(getApplicationContext(), EditTruckActivity.class);
                edittruck.putExtra("truckUsername", username);
                startActivity(edittruck);
            }
        });
        menuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent menuitem = new Intent(getApplicationContext(), TrucksMenuActivity.class);
                menuitem.putExtra("truckUsername", username);
                startActivity(menuitem);
            }
        });

        orderScreen.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Order curOrder = (Order)(adapterView.getItemAtPosition(i));
                        Intent intent = new Intent(getBaseContext(), TruckViewOrderDetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable("order", curOrder);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }

        );

       // final Bundle b = getIntent().getExtras();
       // final String truckName = b.getString("truckName");



//        ListAdapter adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_activated_1,stringOrders);



//        orderScreen.setOnItemClickListener(
//                new AdapterView.OnItemClickListener(){
//
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Order o = (Order) adapterView.getItemAtPosition(i);
//                        Intent intent = new Intent(getBaseContext(), CurrentOrdersTruckActivity.class);
//                      //  intent.putExtra("ORDER", o);
//                        startActivity(intent);
//                    }
//                }
//        );


    }

    /*
    XML
        list of current orders
        each order has complete button next to it
        Each order is a button. Has name of person who ordered (details about order)
        toolbar at bottom that allows you to go to : Truck information and my menu
     */
    /*
    Back end
        Pull from database current orders allocated to this truck
            if they are completed, display in light grey
        Orders ordered by most recently sent
     */
}

