
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserMenuViewActivity extends AppCompatActivity {

    private List<MenuItem> listOfMenuItems;
    private String [] allMenuItems;
    Map<String, MenuItem> nameToItem= new HashMap<>();
  //  Order currentOrder;
   // User currentUser;
    //Truck selectedTruck;
    private Button buttonCart;
    List<String> orders = new ArrayList<>();
    String username;
    String truckName;
    Order curOrder;
    User finalUser;
    Truck finalTruck;
    TextView totalItems;
    // List<Truck> listOfTrucks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu_view);
        username = getIntent().getStringExtra("Username");
        this.setTitle(username);
        truckName = getIntent().getStringExtra("TRUCK_NAME");
        buttonCart = findViewById(R.id.cartButton);
        Button submitRating = findViewById(R.id.submitRating);
        final RatingBar rb = findViewById(R.id.rating_bar);
        totalItems = findViewById(R.id.noOfItems);

        ImageButton logoutButton = (ImageButton) findViewById(R.id.bLogout);
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

        listOfMenuItems = new ArrayList<MenuItem>();
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
                                Log.v("WHAT IS TRUCK ONE", truck1.getUsername());
                                if (truck1.getTruckName().equals(truckName)){
                                    finalTruck = truck1;
                                    Log.v("found a valid truck", "truck ");
                                    ListView truckMenuList = (ListView) findViewById(R.id.truckList);
                                    List<MenuItem> menu = truck1.getTruckMenu();
                                    List<MenuItem> temp = new LinkedList<MenuItem>();
                                    for (MenuItem x : menu) {
                                        if (!x.getItemName().equals("DEFAULT")) {
                                            temp.add(x);

                                        }
                                    }
                                    menu = temp;
                                    ListAdapter truckMenuApadter = new ArrayAdapter<MenuItem>(UserMenuViewActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1, menu){
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent){
                                            View view = super.getView(position, convertView, parent);
                                            TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                            tv.setTextColor(Color.BLACK);
                                            return tv;
                                        }
                                    };
                                    truckMenuList.setAdapter(truckMenuApadter);

                                    //beginning of added code
                                    truckMenuList.setOnItemClickListener(
                                            new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    MenuItem item = (MenuItem) adapterView.getItemAtPosition(i);
                                                    adapterView.getItemAtPosition(i);
                                                    curOrder.addMenuItem(item);
                                                    totalItems.setText("# Items: " + curOrder.size());
                                                    Toast.makeText(UserMenuViewActivity.this,
                                                            "Item " + item.getItemName() +" added to cart",
                                                            Toast.LENGTH_LONG).show();

                                                }
                                            }

                                    );
                                    //end of added code

                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        findCurrentUser(username);

        buttonCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getBaseContext(), UserViewCartActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("order", curOrder);
                intent.putExtras(b);
                startActivity(intent);

            }
        });

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = (double) rb.getRating();
                AccessDatabase ad = new AccessDatabase();
                String nameOfTruck = getIntent().getStringExtra("TRUCK_NAME");
                ad.updateRatingOfTruck(nameOfTruck, rating);
                Toast.makeText(UserMenuViewActivity.this, "Rating has been added", Toast.LENGTH_LONG).show();
                Button submit = findViewById(R.id.submitRating);
                submit.setClickable(false);
            }
        });
    }

    /*
    XML
        list of menu items for that truck
        each menu item is clickable
        cart button in upper right hand corner
     */

    /*
    Back end
        it needs to be
        when you click on menu item it adds to your cart
     */

    /*
    Create dummy menu items. Should be replaced by pulling from the database
    logic on pulling from database: get the name of the truck from what button the user clicked
    pull that truck. pull that trucks menu. get list of menu items from menu object
    add that to this array list.
     */


    public void findCurrentUser(final String userName) {
        Log.v("find current user", "checking for user" );
        AccessDatabase a = new AccessDatabase();
        DatabaseReference currDatabase = a.getCurrDatabase();
        DatabaseReference truckData = currDatabase.child("ind user");
        truckData.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> allUsers = dataSnapshot.getChildren();
                        for (DataSnapshot u : allUsers) {
                            User user1 = u.getValue(User.class);
                            if (user1 != null) {
                                if (user1.getUsername().equals(userName)) {
                                    finalUser = user1;
                                    //setGlobalUser(user1);
                                    Log.v("USERNAME: ", user1.getUsername());
                                    Log.v("make order", "order has been made");

                                    curOrder = new Order(finalUser.getUsername(), finalTruck.getUsername());
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

    public void setGlobalUser(User tempUser) {
        Log.v("setting global user", "in here");
        finalUser = tempUser;
    }
}

