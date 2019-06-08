package com.killua.heartfailuremanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class fg_pat_set extends Fragment {
    View view;
    RelativeLayout portrait_bar;
    RelativeLayout name_bar;
    RelativeLayout gender_bar;
    RelativeLayout birth_bar;
    RelativeLayout password_bar;
    RelativeLayout exit_bar;
    RelativeLayout heart_rate_bar;
    RelativeLayout pressure_bar;
    TextView name;
    TextView gender;
    TextView birth;
    TextView heart_rate;
    TextView pressure;

    Date date;

    //全局变量
    Bundle bd;

    public fg_pat_set() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_pat_set, container, false);
        bind();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==122){
            switch (resultCode){
                case 0:SettingChangeRequest("3",data.getStringExtra("password")); break;
                default: break;
            }
        }
    }

    private void bind(){
        bd=((Data)getActivity().getApplication()).bd;
        portrait_bar=view.findViewById(R.id.portrait_bar);
        portrait_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        name_bar=view.findViewById(R.id.name_bar);
        name=view.findViewById(R.id.name);
        name.setText(bd.getString("name"));
        name_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getContext());
                et.setSingleLine();
                et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                et.setPadding(20,20,20,20);
                new AlertDialog.Builder(getContext()).setTitle("Input Your Name")
                        .setView(et)
                        .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //change name
                                String med=et.getText().toString();
                                SettingChangeRequest("0",med);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });
        gender_bar=view.findViewById(R.id.gender_bar);
        gender=view.findViewById(R.id.gender);
        gender.setText(bd.getInt("gender")==1?"Male":"Female");
        gender_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] choice = new String[]{"Female","Male"};
                new AlertDialog.Builder(getContext()).setTitle("Choose Your Gender")
                        .setItems(choice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SettingChangeRequest("1",String.valueOf(which));
                            }
                        }).show();
            }
        });
        birth_bar=view.findViewById(R.id.birth_bar);
        birth=view.findViewById(R.id.birth);
        birth.setText(bd.getString("birth"));
        birth_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String temp=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        SettingChangeRequest("2",temp);
                    }
                });
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        password_bar=view.findViewById(R.id.password_bar);
        password_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),change_password.class),122);
            }
        });
        exit_bar=view.findViewById(R.id.exit_bar);
        exit_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        heart_rate_bar=view.findViewById(R.id.heart_rate_bar);
        heart_rate=view.findViewById(R.id.heart_rate);
        heart_rate.setText(String.valueOf(bd.getInt("heart_rate")));
        heart_rate_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getContext());
                et.setSingleLine();
                et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                et.setPadding(20,20,20,20);
                new AlertDialog.Builder(getContext()).setTitle("Input Your Measured Heart-Rate")
                        .setView(et)
                        .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //change heart_rate
                                int t=Integer.valueOf(et.getText().toString());
                                String med =String.valueOf(t);
                                SettingChangeRequest("4",med);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });

        pressure_bar=view.findViewById(R.id.pressure_bar);
        pressure=view.findViewById(R.id.pressure);
        pressure.setText(String.valueOf(bd.getInt("pressure")));
        pressure_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getContext());
                et.setSingleLine();
                et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                et.setPadding(20,20,20,20);
                new AlertDialog.Builder(getContext()).setTitle("Input Your Measured Pressure")
                        .setView(et)
                        .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //change pressure
                                String med=String.valueOf(Integer.valueOf(et.getText().toString()));
                                SettingChangeRequest("5",med);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });
    }
    //向服务器发送修改信息请求
    private void SettingChangeRequest(String index,String value) {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "SettingChange";    //注②
        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                                    switch(index){
                                        case "0":bd.putString("name",value);name.setText(value);break;
                                        case "1":bd.putInt("gender",Integer.valueOf(value));gender.setText(value.equals("1")?"Male":"Female");break;
                                        case "2":bd.putString("birth",value);birth.setText(value);break;
                                        case "3":bd.putString("password",value);break;
                                        case "4":bd.putString("heart_rate",value);heart_rate.setText(value);break;
                                        case "5":bd.putString("pressure",value);pressure.setText(value);break;
                                    }
                                    Toast.makeText(getContext(),"Changed Successfully!",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case 1:Toast.makeText(getContext(),"You have no right to modify!",Toast.LENGTH_SHORT).show();break;
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Syntax Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getContext(),"Failed to connect!.",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", bd.getString("id"));  //注⑥
                params.put("password", bd.getString("password"));
                params.put("type",String.valueOf(bd.getInt("type")));
                params.put("index",index);
                params.put("value",value);
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
