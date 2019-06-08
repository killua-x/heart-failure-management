package com.killua.heartfailuremanagement;

public class patient {
    String id;
    int portrait;
    String name;
    String gender;
    int age;
    int heart_rate;
    int pressure;
    String medicine;


    public patient(String tid,String tname, String tgender, int tage,int theart_rate,int tpressure,int tportrait,String tmedicine) {
        id=tid;
        name=tname;
        gender=tgender;
        age=tage;
        heart_rate=theart_rate;
        pressure=tpressure;
        portrait=tportrait;
        medicine=tmedicine;
    }
}
