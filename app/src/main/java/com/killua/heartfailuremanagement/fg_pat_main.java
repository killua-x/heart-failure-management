package com.killua.heartfailuremanagement;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class fg_pat_main extends Fragment {
    View view;
    private ListView list;
    private TextView name;
    private RelativeLayout info;
    private TextView hint;

    private TextView title1;
    private TextView title2;
    private View div1;
    private View div2;

    private ArrayList<doctor> doctors;
    private doctor mine;
    private String medicine;
    private Data app;
    private Bundle bd;
    private int status;

    public fg_pat_main() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_pat_main, container, false);
        app=(Data)getActivity().getApplication();
        bd=app.bd;
        status=bd.getInt("status");
        doctors=app.doctors;
        if(status==0){  //已绑定医生
            bind2();
        }
        else{           //未绑定医生
            if(doctors.size()==0){
                bind0();
            }
            else{
                bind1();
            }
        }
        return view;
    }
    private void bind1(){
        list= view.findViewById(R.id.list);
        title1=view.findViewById(R.id.title1);
        div1=view.findViewById(R.id.div1);
        title1.setVisibility(View.VISIBLE);
        div1.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);

        MyAdapter<doctor> myAdapter=new MyAdapter<doctor>(doctors,R.layout.frag_pat_main_list) {
            @Override
            public void bindView(ViewHolder holder, doctor obj) {
                holder.setText(R.id.name,obj.name);
                holder.setText(R.id.con,obj.gender.equals("Male")?"Choose Him!":"Choose Her!");
                holder.setText(R.id.age, "Age: "+obj.age);
                holder.setText(R.id.id,"id: "+obj.id);
                holder.setImageResource(R.id.portrait,obj.portrait);
                holder.setOnClickListener(R.id.con, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BindRequest(obj);
                    }
                });
            }
        };
        list.setAdapter(myAdapter);
    }
    private void hide1(){
        title1.setVisibility(View.GONE);
        div1.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
    }
    private void bind2(){
        mine=doctors.get(0);
        medicine=app.bd.getString("medicine");

        name=view.findViewById(R.id.name);
        info=view.findViewById(R.id.info);
        title2=view.findViewById(R.id.title2);
        div2=view.findViewById(R.id.div2);
        name.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        title2.setVisibility(View.VISIBLE);
        div2.setVisibility(View.VISIBLE);

        ImageView portrait=view.findViewById(R.id.portrait);
        TextView id=view.findViewById(R.id.doc_id);
        TextView age=view.findViewById(R.id.age);
        TextView gender=view.findViewById(R.id.gender);
        TextView medicine=view.findViewById(R.id.medicine);
        Button unbind=view.findViewById(R.id.unbind);
        Button mes=view.findViewById(R.id.mes);

        name.setText(mine.name);
        portrait.setBackgroundResource(mine.portrait);
        id.setText("Id: "+mine.id);
        age.setText("Age: "+mine.age);
        gender.setText("Gender: "+mine.gender);
        medicine.setText("Medicine: "+ this.medicine);
        unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Attention")
                        .setMessage("Really want to unbind with your current doctor?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UnbindRequest(mine.id);
                            }
                        }).show();
            }
        });
        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),chat.class);
                intent.putExtra("to",mine.name);
                intent.putExtra("toid",mine.id);
                startActivity(intent);
            }
        });

    }
    private void hide2(){
        name.setVisibility(View.GONE);
        info.setVisibility(View.GONE);
        title2.setVisibility(View.GONE);
        div2.setVisibility(View.GONE);
    }
    private void bind0(){
        hint=view.findViewById(R.id.hint);
        hint.setVisibility(View.VISIBLE);
    }
    private void hide0(){
        hint.setVisibility(View.GONE);
    }

    //向服务器发送绑定请求
    private void BindRequest(doctor doc) {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "Bind";    //注②

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
                                case 0:{
                                    mine=doc;
                                    bd.putInt("status",0);
                                    app.doctors.clear();
                                    app.doctors.add(doc);
                                    hide1();
                                    bind2();
                                    Toast.makeText(getContext(),"Successfully bind!",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case 1:Toast.makeText(getContext(),"You have no right to add!",Toast.LENGTH_SHORT).show();break;
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
                params.put("did",doc.id);
                params.put("RequestType",tag);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //向服务器发送解绑请求
    private void UnbindRequest(String did) {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "Unbind";    //注②

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
                                case 0:{
                                    mine=null;
                                    bd.putInt("status",1);
                                    app.doctors.clear();
                                    hide2();
                                    bind0();
                                    Toast.makeText(getContext(),"Successfully Unbind!",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case 1:Toast.makeText(getContext(),"You have no right to add!",Toast.LENGTH_SHORT).show();break;
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
                params.put("did",did);
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
