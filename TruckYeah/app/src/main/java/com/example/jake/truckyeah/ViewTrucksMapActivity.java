package com.example.jake.truckyeah;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ViewTrucksMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Truck> listOfTruckObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trucks_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /*
    how to add map with marker : https://developers.google.com/maps/documentation/android-api/map-with-marker#manifest
     */

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseReference curr = FirebaseDatabase.getInstance().getReference();
        DatabaseReference truckReference = curr.child("truck user");
        truckReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot t : dataSnapshot.getChildren()) {
                    if(t != null) {
                        Truck truck = (Truck) t.getValue(Truck.class);
                        List<Address> tempAddr = new LinkedList<Address>();
                        Geocoder currGeo = new Geocoder(getApplicationContext());
                        try {
                            tempAddr = currGeo.getFromLocationName(truck.getAddress(),1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LatLng addrAsLat = new LatLng(tempAddr.get(0).getLatitude(), tempAddr.get(0).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(addrAsLat).title(truck.getTruckName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addrAsLat,14));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);


//        List<LatLng> allLat =  convertAddrToLat(getApplicationContext(),  getListOfAddresses());
//        for (int i = 0; i < allLat.size(); i++) {
//            mMap.addMarker(new MarkerOptions().position(allLat.get(i)).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(allLat.get(i)));
//
//        }

    }

    public List<String> getListOfAddresses() {
        List<String> tempAddresses = new LinkedList<String>();
        tempAddresses.add("4056 Irving Street Philadelphia, PA, 19104");
        tempAddresses.add("3400 Walnut Street Philadelphia, PA, 19104");
        return tempAddresses;

    }

    public List<LatLng> convertAddrToLat(Context context, List<String> allAddr) {
        Geocoder currGeo = new Geocoder(context);
        List<Address> allAddrConv = new LinkedList<Address>();
        List<LatLng> finalMapObj = new LinkedList<LatLng>();
        if (currGeo == null) {
            Log.v("currGeo is null", "nullTruck");
        }
        else {
            for (int i = 0; i < allAddr.size(); i++) {
                List<Address> tempAddr = new LinkedList<Address>();
                try {
                    tempAddr = currGeo.getFromLocationName(allAddr.get(i),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                allAddrConv.add(i,  tempAddr.get(0));
            }
        }

        for (int i = 0; i < allAddrConv.size(); i++) {
            Address currAddr = allAddrConv.get(i);
            currAddr.getLatitude();
            currAddr.getLongitude();
            LatLng addrAsLat = new LatLng( currAddr.getLatitude(), currAddr.getLongitude());
            finalMapObj.add(i, addrAsLat);
        }
        return finalMapObj;
    }


}
