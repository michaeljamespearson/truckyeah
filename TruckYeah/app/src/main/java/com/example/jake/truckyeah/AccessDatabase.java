package com.example.jake.truckyeah;

/**
 * Created by Aishwarya on 4/3/18.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/*
Role of this class: Creates an offline instance of the database used for this app that allows it to be accessed
by other parts of the app
Dependenecies: Uses the Truck and User class to add these objects to the database when they are created.
 */

public class AccessDatabase {

    final DatabaseReference currDatabase;
    Iterable<DataSnapshot> allTrucks = null;
    List<Truck> truckList = new ArrayList<Truck>();

    public DatabaseReference getCurrDatabase(){
        return currDatabase;
    }


    public AccessDatabase() {
        currDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void putUserInDatabase(String curEmail, User newUser) {
        currDatabase.child("ind user").child(curEmail).push().getKey();
        currDatabase.child("ind user").child(curEmail).setValue(newUser);
    }

    public void putTruckInDatabase(String curEmail, Truck newTruck) {
        currDatabase.child("truck user").child(curEmail).push().getKey();
        currDatabase.child("truck user").child(curEmail).setValue(newTruck);
    }

    public void putOrderInDatabase( Order o) {
        currDatabase.child("orders").child("PUT CURRENT TIME").push().getKey();
        currDatabase.child("orders").setValue(o);
    }

    public void addOrder(final MenuItem item, final String truckName) {
        DatabaseReference ratingReference = currDatabase.child("order");
        ratingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            List<MenuItem> o =  new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot s = null;
                for (DataSnapshot t : dataSnapshot.getChildren()) {
                    if (t.child("truckName").getValue().equals(truckName)) {
                        o = (List<MenuItem>) t.child("orders").getValue();
                        o.add(item);
                        s = t;
                    }
                }
                if(s != null) {
                    currDatabase.child("truck user").child(s.getKey()).child("orders").setValue(o);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void updateRatingOfTruck(final String truckName, final double rating) {
        DatabaseReference ratingReference = currDatabase.child("truck user");
        ratingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot s = null;
                double no = 0;
                double ratingAux = 0;
                for (DataSnapshot t : dataSnapshot.getChildren()) {
                    if (t.child("truckName").getValue().equals(truckName)) {
                        Truck truck = (Truck) t.getValue(Truck.class);
                        double currentRating = truck.getRating();
                        double numRatings = truck.getNumberOfRatings();
                        double totalRating = (currentRating * numRatings) + rating;
                        numRatings++;
                        double newRating = totalRating/numRatings;
                        truck.setRating(newRating);
                        putTruckInDatabase(truck.getUsername(), truck);
/*
                        Long tempLong = (long) t.child("rating").getValue();
                        double r =  tempLong.doubleValue();
                        Long tempLongTwo = (long) t.child("numberOfRatings").getValue();
                        Log.v("what is this temp" ,tempLongTwo.toString());
                        no = tempLongTwo.doubleValue();
                        no++;
                        ratingAux = (r * no) + rating;
                        ratingAux = ratingAux / no;
                        */
                       // s = t;
                    }
                }
                /*
                if(s != null) {
                    currDatabase.child("truck user").child(s.getKey()).child("numberOfRatings").setValue(no);
                    currDatabase.child("truck user").child(s.getKey()).child("rating").setValue(ratingAux);
                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void getAllUsers() {
      //  String whyWork= "IN GET LISTOF TRUCKS";
        //Log.v("CHECKING: ",whyWork);
        //final DatabaseReference currDatabase;
        DatabaseReference truckValues = currDatabase.child("truck user");
        truckValues.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        allTrucks =   dataSnapshot.getChildren();
                        Iterator<DataSnapshot> truckIterator = allTrucks.iterator();
                        while (truckIterator.hasNext()) {
                            DataSnapshot curTruckSnap = truckIterator.next();
                           // String something = curTruckSnap.child("username").getValue().toString();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
       // return allTrucks;
    }

    public void setAllTrucksListView(Adapter a) {
        DatabaseReference truckReference = currDatabase.child("truck user");
        truckReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot t : dataSnapshot.getChildren()) {
                    if(t != null) {
                        Log.v("Adding To TruckList", "0");
                        Truck truck = (Truck) t.getValue(Truck.class);
                        truckList.add(truck);
                        Log.v("TruckListSize", Integer.toString(truckList.size()));
                    }
                }
                String[] truckListAsString = new String[truckList.size()];
                for(int i = 0; i < truckListAsString.length; i++) {
                    truckListAsString[i] = truckList.get(i).getTruckName();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.v("return size", Integer.toString(truckList.size()));
    }

    public List<Truck> getTruckList() {
        Log.v("Size in get", Integer.toString(truckList.size()));
        return truckList;
    }





}
