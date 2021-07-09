
package com.example.jake.truckyeah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
Role of this class: After a user first logs in with correct credentials, they see this page, which
shows a list of all the food trucks. They are able to click on these food trucks.
Dependencies: Truck
 */

public class TruckListActivity extends AppCompatActivity {

    String[] truckListAsStrings;
    Map<String, Truck> nameToTruck= new HashMap<>();
    List<Truck> truckList = new ArrayList<Truck>();
    ListView truckLV;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String username = getIntent().getStringExtra("Username");
        setContentView(R.layout.activity_truck_list);
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
                        truckList.add(truck);
                        DecimalFormat df = new DecimalFormat("##.##");
                        list.add(truck.getTruckName() + "\n" +  " Rating: " + df.format(truck.getRating()));
                      //  list.add(truck.getTruckName());
//                        Log.v("TruckListSize", Integer.toString(truckList.size()));
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
        this.setTitle(username);

        for (Truck t : truckList) {
            nameToTruck.put(t.getTruckName(), t);
        }

        truckLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String truckName = String.valueOf(adapterView.getItemAtPosition(i));
                        String newLine = "\n";
                        String[] newTruckName = truckName.split(newLine);
                        Intent intent = new Intent(getBaseContext(), UserMenuViewActivity.class);
                        intent.putExtra("TRUCK_NAME", newTruckName[0]);
                       // intent.putExtra("TRUCK_NAME", truckName);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                    }
                }

        );

       ImageButton map = findViewById(R.id.mapButton);
        ImageButton cart = findViewById(R.id.myCartButton);
        ImageButton  truckList = findViewById(R.id.truckListButton);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewTrucksMapActivity.class);
                startActivity(i);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserViewCurrentOrdersActivity.class);
                i.putExtra("username", username);
                startActivity(i);

            }
        });



    }

    //xml
    //username in top right
    //toolbar at bottom with buttons for trucklist and user info
    //List of avaialable, clickable trucks
    //Spinner to sort trucks
    //backend
    //Spinner to reorder trucks on the page
}

