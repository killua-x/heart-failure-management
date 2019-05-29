package com.killua.heartfailuremanagement;

public class doctor {
    int portrait;
    String name;
    String gender;
    String id;
    int age;
    public doctor(String tid,String tname, String tgender, int tage,int tportrait) {
        id=tid;
        name=tname;
        gender=tgender;
        age=tage;
        portrait=tportrait;
    }
}
