
package com.example.jake.truckyeah;

import android.os.Parcel;
import android.os.Parcelable;
/*
Role of this class: This defines a MenuItem Object, which holds the name, price and description of
the item. The Truck is the one that is able to make this.
Dependencies: None
 */
/**
 * Created by jake on 3/28/18.
 */


public class MenuItem implements Parcelable{

    private String itemName;
    private Double price;
    private String description;

    public MenuItem(){

    }

    MenuItem(String s, Double p){
        itemName = s;
        price = p;
    }


    MenuItem(String s){
        itemName = s;
        price = 0.0;
    }

    public MenuItem(Parcel in){
        readFromParcel(in);
    }

    String getItemName(){
        return itemName;
    }

    Double getPrice(){
        return price;
    }

    String getDescription(){
        return description;
    }

    void setItemName(String s){
        itemName = s;
    }

    void setPrice(Double p){
        price = p;
    }

    void setDescription(String s){
        description = s;
    }

    @Override
    public String toString(){
        return itemName + ": $" + price + "\n"  + "Description: " + description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.itemName);
        parcel.writeDouble(this.price);
        parcel.writeString(this.description);
    }

    public void readFromParcel(Parcel in){
        this.itemName = in.readString();
        this.price = in.readDouble();
        this.description = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public MenuItem createFromParcel(Parcel parcel) {
            return new MenuItem(parcel);
        }

        @Override
        public MenuItem[] newArray(int i) {
            return new MenuItem[i];
        }
    };

    @Override
    public boolean equals(Object obj) {
        MenuItem curItem = (MenuItem) obj;
        return this.getItemName().equals(curItem.getItemName());
    }
}

