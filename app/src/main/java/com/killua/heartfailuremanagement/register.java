package com.killua.heartfailuremanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button register_button_back;
    Calendar now;
    DatePickerDialog dpd;

    EditText reg_id;
    EditText reg_name;
    EditText reg_password;
    EditText reg_confirm;
    RadioGroup reg_gender;
    Button reg_birthday;
    RadioGroup reg_type;
    Button reg_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        bindview();

        register_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        reg_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });
        reg_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_sub();
            }
        });
    }
    private void bindview(){
        register_button_back=findViewById(R.id.register_back);
        now=Calendar.getInstance();
        dpd=DatePickerDialog.newInstance(
                register.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        reg_id=findViewById(R.id.reg_id);
        reg_name=findViewById(R.id.reg_name);
        reg_password=findViewById(R.id.reg_password);
        reg_confirm=findViewById(R.id.reg_confirm_password);
        reg_gender=findViewById(R.id.reg_gender);
        reg_birthday=findViewById(R.id.reg_birthday);
        reg_type=findViewById(R.id.reg_type);
        reg_sub=findViewById(R.id.reg_sub);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        reg_birthday.setText(date);
    }

    //向服务器发送注册请求
    private void SignupRequest(Account user) {
        //请求地址
        String url = ((Data)getApplication()).bd.getString("url");  //注①
        String tag = "Register";    //注②

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
                                case 0:setResult(0);finish();break;     //注册成功
                                case 1:Toast.makeText(getApplicationContext(),"The id has already existed.",Toast.LENGTH_SHORT).show();
                                        reg_id.requestFocus();break;   //id已存在
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
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
                params.put("id", user.getid());  //注⑥
                params.put("password", user.getpassword());
                params.put("name",user.getname());
                params.put("gender",String.valueOf(user.getgender()));
                params.put("birthday",user.getbirth());
                params.put("type",String.valueOf(user.gettype()));
                params.put("RequestType",tag);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    private void check_sub(){
        Account user=new Account();
        //检测id长度
        String id=reg_id.getText().toString().trim();
        if(id.length()>=2&&id.length()<=20){
            user.setid(id);
        }
        else{
            reg_id.setFocusable(true);
            Toast.makeText(getApplicationContext(),"id should have a length range between 6 and 20",Toast.LENGTH_SHORT).show();
            return;
        }
        //检测姓名长度
        String name=reg_name.getText().toString().trim();
        if(name.length()>0&&name.length()<=20){
            user.setname(name);
        }
        else {
            reg_name.setFocusable(true);
            Toast.makeText(getApplicationContext(), "name should have a length range between 1 and 20", Toast.LENGTH_SHORT).show();
            return;
        }
        //检测密码
        String password=reg_password.getText().toString().trim();
        String confirm=reg_confirm.getText().toString().trim();
        if(password.length()>=2&&password.length()<=20){
            if(password.equals(confirm)){
                user.setpassword(password);
            }
            else{
                reg_confirm.setFocusable(true);
                Toast.makeText(getApplicationContext(), "the password doesn't match!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            reg_password.setFocusable(true);
            Toast.makeText(getApplicationContext(), "password should have a length range between 6 and 20", Toast.LENGTH_SHORT).show();
            return;
        }
        //检测生日

        user.setgender(reg_gender.getCheckedRadioButtonId()==R.id.reg_male?1:0);
        user.setbirth(reg_birthday.getText().toString());
        user.settype(reg_type.getCheckedRadioButtonId()==R.id.reg_doctor?1:0);
        SignupRequest(user);
    }
}
