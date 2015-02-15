package com.lee.lastpass.types;

/**
 * Created by universe on 2015/2/14.
 */
public class Data {
    String name;
    String userName;
    String password;
    public Data(String name, String userName, String password){
        this.name = name;
        this.userName = userName;
        this.password = password;
    }
    public String getName(){
        return name;
    }
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public String toString() {
        return name;
    }
}
