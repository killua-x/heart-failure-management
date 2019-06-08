package com.killua.heartfailuremanagement;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


public class fg_doc_main extends Fragment {
    View view;
    private ListView list;
    private List<patient> mData;
    private MyAdapter<patient> myAdapter;

    EditText search_bar;
    Button search;
    Button add;
    TextView hint;
    Data app;
    Bundle bd;

    public fg_doc_main() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_doc_main, container, false);
        bind();
        list.setAdapter(myAdapter);
        if(mData.size()==0){
            hint.setVisibility(View.VISIBLE);
        }
        else{
            list.setVisibility(View.VISIBLE);
        }
        return view;
    }
    private void bind(){
        app=(Data)getActivity().getApplication();
        bd=app.bd;
        list= view.findViewById(R.id.fg_doc_main_list);
        search_bar=view.findViewById(R.id.search_bar);
        search=view.findViewById(R.id.search);
        add=view.findViewById(R.id.add);
        hint=view.findViewById(R.id.hint);


        mData=app.patients;
        myAdapter=new MyAdapter<patient>((ArrayList<patient>) mData,R.layout.frag_doc_main_list) {
            @Override
            public void bindView(ViewHolder holder, patient obj) {
                holder.setText(R.id.doc_list_name,obj.name+"("+obj.id+")");
                holder.setText(R.id.doc_list_gender,"Gender: "+obj.gender);
                holder.setText(R.id.doc_list_age, "Age: "+obj.age);
                holder.setText(R.id.doc_list_heart_rate,"Heart Rate: "+obj.heart_rate);
                holder.setText(R.id.doc_list_pressure,"Pressure: "+obj.pressure);
                holder.setText(R.id.doc_list_medicine,"Medicine: "+obj.medicine);
                holder.setImageResource(R.id.doc_list_portrait,obj.portrait);
                holder.setOnClickListener(R.id.doc_list_med, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText et = new EditText(getContext());
                        et.setSingleLine();
                        et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        et.setPadding(20,20,20,20);
                        new AlertDialog.Builder(getContext()).setTitle("Input the medicine")
                                .setView(et)
                                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //change medicine
                                        String med=et.getText().toString();
                                        MedicineChangeRequest(obj.id,med,holder.getItemPosition());
                                    }
                                }).setNegativeButton("cancel",null).show();
                    }
                });
                holder.setOnClickListener(R.id.doc_list_mes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),chat.class);
                        intent.putExtra("to",obj.name);
                        intent.putExtra("toid",obj.id);
                        startActivity(intent);
                    }
                });
            }
        };

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=search_bar.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_bar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                search_bar.clearFocus();
                for(int i=0;i<myAdapter.getCount();i++){
                    patient t=myAdapter.getItem(i);
                    if(t.id.equals(id)) {
                        list.setSelection(i);
                        break;
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getContext());
                et.setSingleLine();
                et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                et.setPadding(20,20,20,20);
                new AlertDialog.Builder(getContext()).setTitle("Input the patient's id")
                        .setView(et)
                        .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //change medicine
                                String pid=et.getText().toString();
                                AddRequest(pid);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });
    }
    //向服务器发送添加请求
    private void AddRequest(String pid) {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "Add";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

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
                                case 0:Toast.makeText(getContext(),"Your add request has been sent!",Toast.LENGTH_SHORT).show();break;
                                case 1:Toast.makeText(getContext(),"You have no right to add!",Toast.LENGTH_SHORT).show();break;
                                case 2:Toast.makeText(getContext(),"The p-id doesn't exist!",Toast.LENGTH_SHORT).show();break;
                                case 3:Toast.makeText(getContext(),"The patient already has a doctor!",Toast.LENGTH_SHORT).show();break;
                                case 4:Toast.makeText(getContext(),"Request exist!",Toast.LENGTH_SHORT).show();break;
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
                params.put("pid",pid);
                params.put("RequestType",tag);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //向服务器发送修改药物请求
    private void MedicineChangeRequest(String pid,String medicine,int pos) {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "MedicineChange";    //注②
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
                                    mData.get(pos).medicine=medicine;
                                    myAdapter.getItem(pos).medicine=medicine;
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(),"Changed Successfully!",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case 1:Toast.makeText(getContext(),"You have no right to modify!",Toast.LENGTH_SHORT).show();break;
                                case 2:Toast.makeText(getContext(),"The patient doesn't exist!",Toast.LENGTH_SHORT).show();break;
                                case 3:Toast.makeText(getContext(),"The patient doesn't bind to you!",Toast.LENGTH_SHORT).show();break;
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
                params.put("pid",pid);
                params.put("medicine",medicine);
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
