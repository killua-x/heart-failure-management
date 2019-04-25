package com.killua.heartfailuremanagement;


import java.util.Date;

public class message {
    int type;
    int portrait;
    String name;
    Date time;
    String text;
    public message(int type,int portrait,String name,Date time, String text){
        this.type=type;
        this.portrait=portrait;
        this.name=name;
        this.time=time;
        this.text=text;
    }
}
