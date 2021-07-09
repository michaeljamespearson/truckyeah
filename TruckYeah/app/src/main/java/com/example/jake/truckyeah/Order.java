package com.example.jake.truckyeah;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
/*
Role of this class: Holds a list of MenuItems that a user has ordered as well as the username of the
user that has made the order and the name of the truck from which they have ordered.
Dependencies: MenuItem
 */
/**
 * Created by jake on 3/28/18.
 */


public class Order implements Parcelable{

    private List<MenuItem> order;
    private String userOrder;
    private String truckOrder;

    public Order(String u, String t) {
        order = new ArrayList<MenuItem>();
        this.userOrder = u;
        this.truckOrder = t;
    }

    public Order(Parcel in ) {
        readFromParcel(in);
    }

    public Order() {

    }

    public void addMenuItem(MenuItem newItem) {
        order.add(newItem);
    }

    public List<MenuItem> getOrder() {
        return order;
    }

    public void setOrder (List<MenuItem> m) {
        this.order = m;
    }



    public String getUserOrder() {
        return userOrder;
    }





    public void setUserOrder(String userOrder) {
        this.userOrder = userOrder;
    }

    public String getTruckOrder() {
        return truckOrder;
    }

    public void setTruckOrder(String truckOrder) {
        this.truckOrder = truckOrder;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }


    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>(){

        @Override
        public Order createFromParcel(Parcel parcel) {
            return new Order(parcel);
        }

        @Override
        public Order[] newArray(int i) {
            return new Order[i];
        }
    };

    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeParcelable(this.getUser(), i);
        //parcel.writeParcelable(this.getTruck(), i);
        parcel.writeString(userOrder);
        parcel.writeString(truckOrder);
        parcel.writeTypedList(order);
        parcel.writeList(order);

    }

    public void readFromParcel(Parcel in){

     //   this.userOrder = in.readParcelable(getClass().getClassLoader());
       // this.truckOrder = in.readParcelable(getClass().getClassLoader());
        this.userOrder = in.readString();
        this.truckOrder = in.readString();
        order = new ArrayList<MenuItem>();
        in.readTypedList(order, MenuItem.CREATOR);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString(){
        return userOrder + "'s Order";
    }

    public int size() {
        return order.size();
    }

    public String userToString() {
      //  return "Truck: " + truckOrder + " Items: " + getOrder().toString();
        String t = "";
        for(MenuItem m : getOrder()) {
            t += m.getItemName() + ", ";
        }
        String temp = "Truck: " + truckOrder +  "\n" + " Items: " + t;
        return temp;
    }

    @Override
    public boolean equals(Object obj) {
        Order newOrder = (Order) obj;
        if (!this.getUserOrder().equals(newOrder.getUserOrder())) {
            return false;
        }
        if (this.size() == newOrder.size()) {
            for (int i = 0 ; i < this.size(); i++) {
                if (!this.order.get(i).equals(newOrder.order.get(i))) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
}