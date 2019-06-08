package com.killua.heartfailuremanagement;

import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;

public class Data extends Application {
    /*bd
     *病人包含id, name, password, gender, birth, type, pressure, heart_rate, medicine, doctor_id,
     *          status(0,1分别表示有医生和没有绑定医生)
     *医生包含id, name, password, gender, birth, type
     */
    public Bundle bd;
    public ArrayList<doctor> doctors;
    public ArrayList<patient> patients;
    @Override
    public void onCreate() {
        bd=new Bundle();
        bd.putString("url","http://192.168.199.163:8080/hfm/*");
        doctors=new ArrayList<>();
        patients=new ArrayList<>();
        super.onCreate();
    }
}
