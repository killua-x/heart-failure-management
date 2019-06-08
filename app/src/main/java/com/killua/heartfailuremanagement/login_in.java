package com.killua.heartfailuremanagement;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class login_in extends AppCompatActivity implements View.OnClickListener{
    EditText id;
    EditText password;
    Button button_register;
    Button button_log_in;
    TextView ipconfig;

    Data app;
    Bundle bd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_in);
        bindview();
        iconsize();

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(login_in.this,register.class),123);
            }
        });
        button_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_login();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            switch (resultCode){
                case 0:Toast.makeText(login_in.this,"Register Successfully!",Toast.LENGTH_SHORT).show();break;
                default: break;
            }
        }
    }

    private void bindview(){
        id=findViewById(R.id.id);
        password=findViewById(R.id.password);
        button_register=findViewById(R.id.button_register);
        button_log_in=findViewById(R.id.button_log_in);
        ipconfig=findViewById(R.id.ipconfig);

        app=(Data)getApplication();
        bd=app.bd;

        ipconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(login_in.this);
                et.setSingleLine();
                et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                et.setPadding(20,20,20,20);
                new AlertDialog.Builder(login_in.this).setTitle("Input the ip")
                        .setView(et)
                        .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //change medicine
                                String med=et.getText().toString().trim();
                                bd.putString("url","http://"+med+":8080/hfm/*");
                            }
                        }).setNegativeButton("cancel",null).create().show();
            }
        });
    }
    private void iconsize(){
        Drawable id_icon[]=id.getCompoundDrawables();
        id_icon[0].setBounds(0,0,40,40);
        id.setCompoundDrawables(id_icon[0],id_icon[1],id_icon[2],id_icon[3]);
        Drawable password_icon[]=password.getCompoundDrawables();
        password_icon[0].setBounds(0,0,40,40);
        password.setCompoundDrawables(password_icon[0],password_icon[1],password_icon[2],password_icon[3]);
    }
    private void check_login(){
        //检测id长度
        String sid=id.getText().toString().trim();
        if(!(sid.length()>=2&&sid.length()<=20)){
            id.setFocusable(true);
            Toast.makeText(getApplicationContext(),"id should have a length range between 6 and 20",Toast.LENGTH_SHORT).show();
            return;
        }
        //检测密码
        String spassword=password.getText().toString().trim();
        if(!(spassword.length()>=6&&spassword.length()<=20)){
            password.setFocusable(true);
            Toast.makeText(getApplicationContext(), "password should have a length range between 6 and 20", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginRequest(sid,spassword);
    }

    //向服务器发送登陆请求
    private void LoginRequest(String sid,String spassword) {
        //请求地址
        String url = ((Data)getApplication()).bd.getString("url");    //注①
        String tag = "Login";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            int result = jsonObject.getInt("result");  //注④
                            switch(result){
                                case 0:{
                                    //登陆成功，记录用户信息
                                    final Data app=(Data)getApplication();
                                    Bundle bd =app.bd;
                                    bd.putString("id",sid);
                                    bd.putString("password",spassword);
                                    bd.putString("name",jsonObject.getString("name"));
                                    bd.putString("birth",jsonObject.getString("birth"));
                                    bd.putInt("gender",jsonObject.getInt("gender"));
                                    bd.putInt("type",jsonObject.getInt("type"));
                                    if(jsonObject.getInt("type")==1){
                                        JSONArray patients=jsonObject.getJSONArray("patients");
                                        app.patients.clear();
                                        int year=Calendar.getInstance().get(Calendar.YEAR);
                                        for(int i=0;i<patients.length();i++){
                                            JSONObject patient =patients.getJSONObject(i);
                                            app.patients.add(new patient(patient.getString("id"),
                                                    patient.getString("name"),
                                                    patient.getInt("gender")==1?"Male":"Female",
                                                    year-Integer.valueOf(patient.getString("birth").substring(0,4)),
                                                    patient.getInt("heart_rate"),
                                                    patient.getInt("pressure"),
                                                    R.mipmap.portrait,
                                                    patient.getString("medicine")));
                                        }
                                        Intent it=new Intent(login_in.this,doctor_interface.class);
                                        startActivity(it);
                                    }
                                    else{
                                        bd.putString("medicine",jsonObject.getString("medicine"));
                                        bd.putInt("pressure",jsonObject.getInt("pressure"));
                                        bd.putInt("heart_rate",jsonObject.getInt("heart_rate"));
                                        bd.putInt("status",jsonObject.getInt("status"));

                                        JSONArray doctors=jsonObject.getJSONArray("doctors");
                                        app.doctors.clear();
                                        int year=Calendar.getInstance().get(Calendar.YEAR);
                                        for(int i=0;i<doctors.length();i++){
                                            JSONObject doctor =doctors.getJSONObject(i);
                                            app.doctors.add(new doctor(doctor.getString("id"),
                                                    doctor.getString("name"),
                                                    doctor.getInt("gender")==1?"Male":"Female",
                                                    year-Integer.valueOf(doctor.getString("birth").substring(0,4)),
                                                    R.mipmap.portrait));
                                        }
                                        Intent it=new Intent(login_in.this,patient_interface.class);
                                        startActivity(it);
                                    }
                                    break;
                                }
                                case 1:Toast.makeText(getApplicationContext(),"The id doesn't exist!",Toast.LENGTH_SHORT).show();
                                id.requestFocus();break;   //id不存在
                                case 2:Toast.makeText(getApplicationContext(),"The password doesn't match!",Toast.LENGTH_SHORT).show();
                                    password.requestFocus();break;   //id不存在
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Syntax Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getApplicationContext(),"Failed to connect!.",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", sid);  //注⑥
                params.put("password", spassword);
                params.put("RequestType",tag);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
