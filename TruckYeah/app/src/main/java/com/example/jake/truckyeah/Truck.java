
package com.example.jake.truckyeah;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/*
Role of this class: Creates a Truck object that holds the log in information as well as the details
of the truck. It also has a list of MenuItems that a truck can add to. There is also a rating that
is calculated dynamically based on input from the users ordering from the truck.
Dependencies: MenuItem
 */
/**
 * Created by jake on 3/28/18.
 */

public class Truck implements Parcelable {

    private String username;
    private String password;
    private String truckName;
    private String address;
    private String phoneNumber;
    private double startHour;
    private double endHour;
    private double rating;
    private double numberOfRatings;
    private List<MenuItem> truckMenu;
    private String comments;
    //private List<MenuItem> orders;

    private List<Order> orders;

    public Truck(){

    }

    protected Truck(Parcel in) {
        username = in.readString();
        password = in.readString();
        truckName = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        startHour = in.readDouble();
        endHour = in.readDouble();
        rating = in.readDouble();
        numberOfRatings = in.readDouble();
        truckMenu = in.createTypedArrayList(MenuItem.CREATOR);
        comments = in.readString();
      //  orders = in.createTypedArrayList(MenuItem.CREATOR);
        orders = in.createTypedArrayList(Order.CREATOR);
    }

    public static final Creator<Truck> CREATOR = new Creator<Truck>() {
        @Override
        public Truck createFromParcel(Parcel in) {
            return new Truck(in);
        }

        @Override
        public Truck[] newArray(int size) {
            return new Truck[size];
        }
    };

    public void addMenuItem(MenuItem m){
        truckMenu.add(m);
    }

    public Truck(String username, String password, String truckName){
        this.username = username;
        this.password = password;
        this.truckName = username;
        this.address = "default address";
        this.phoneNumber = "default phone number";
        this.startHour = 1;
        this.endHour = 2;
        this.rating = 3.0;
        this.numberOfRatings = 1.0;

        List<MenuItem> defaultMenu = new ArrayList<MenuItem>() {
        };
        defaultMenu.add(new MenuItem("DEFAULT", 3.50));
        defaultMenu.add(new MenuItem("DEFAULT", 2.50));
        this.truckMenu = defaultMenu;

        //this.orders = new ArrayList<MenuItem>();
        orders = new LinkedList<Order>();
        Order newOrder = new Order("DEFAULT", "DEFAULT");
        MenuItem m = new MenuItem("DEFAULT", 2.50);
        newOrder.addMenuItem(m);
        orders.add(newOrder);
    }

    // Getter and Setter methods

    public List<Order> getOrders() {
        return orders;
    }


    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order o) {
        this.orders.add(o);
    }


    public void setUsername(String s){
        this.username = s;
    }

    public void setPassword(String s){
        this.password = s;
    }

    public void setTruckName(String s){
        this.truckName = s;
    }

    public void setAddress(String addr){
        this.address = addr;
    }

    public void setPhoneNumber(String s){
        this.phoneNumber = s;
    }

    public void setStartHour(int i){
        this.startHour = i;
    }

    public void setEndHour(int i){
        this.endHour = i;
    }

    public void setRating(double i){
        this.rating = i;
    }

    public void setNumberOfRatings(int i){
        this.numberOfRatings = i;
    }

    public void setTruckMenu(List<MenuItem> m){
        this.truckMenu = m;
    }

    public String getUsername() { return this.username; }

    public String getPassword(){
        return this.password;
    }

    public String getTruckName(){
        return this.truckName;
    }

    public String getAddress(){
        return this.address;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public double getStartHour(){
        return this.startHour;
    }

    public double getEndHour(){
        return this.endHour;
    }

    public double getRating() {
        return this.rating;
    }

    public double getNumberOfRatings(){
        return this.numberOfRatings;
    }

    public List<MenuItem> getTruckMenu(){
        return this.truckMenu;
    }

    public void incrementNumberOfRatings() {
        this.numberOfRatings++;
    }

    @Override
    public String toString(){
        return username + password + address;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean deleteOrder(Order o) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).equals(o)) {
                orders.remove(o);
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(truckName);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeDouble(startHour);
        dest.writeDouble(endHour);
        dest.writeDouble(rating);
        dest.writeDouble(numberOfRatings);
        dest.writeTypedList(truckMenu);
        dest.writeString(comments);
        dest.writeTypedList(orders);
    }
}
