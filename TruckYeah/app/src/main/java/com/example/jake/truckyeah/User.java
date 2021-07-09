
package com.example.jake.truckyeah;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jake on 3/28/18.
 */

public class User implements Parcelable {

    //username
    //password
    private String username;
    private String password;
    

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public User() {

    }


    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername(){return username;}
    public String getPassword(){return password;}

    public void setUsername(String username){this.username = username;}
    public void setPassword(String p){
        this.password = p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
    }
}

