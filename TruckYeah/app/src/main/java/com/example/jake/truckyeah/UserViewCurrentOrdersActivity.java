package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UserViewCurrentOrdersActivity extends AppCompatActivity {

    String username;
    List<Order> stringOrders = new ArrayList<>();

    List<Truck> truckList = new ArrayList<Truck>();
    ListView truckLV;
    ArrayList<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_current_orders);

        username = getIntent().getStringExtra("username");
        truckLV =(ListView)findViewById(R.id.TruckListView);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        truckLV.setAdapter(adapter);
        DatabaseReference curr = FirebaseDatabase.getInstance().getReference();
        DatabaseReference truckReference = curr.child("truck user");
        truckReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot t : dataSnapshot.getChildren()) {
                    if(t != null) {
//                        Log.v("Adding To TruckList", "0");
                        Truck truck = (Truck) t.getValue(Truck.class);

                        for (Order o: truck.getOrders()){
                            // Log.v("what is order", o.toString());
                            stringOrders.add(o);
                            if (o.getUserOrder().equals(username)) {
                                if (o != null) {
                                    list.add(o.userToString());
                                }
                            }

                        }

                        truckList.add(truck);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ImageButton map = findViewById(R.id.mapButton);
        ImageButton  truckList = findViewById(R.id.truckListButton);

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
                i.putExtra("Username", username);
                startActivity(i);

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

    }
}
