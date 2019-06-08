package com.killua.heartfailuremanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat extends AppCompatActivity {
    Button back;
    Button send;
    TextView title;
    EditText mes;
    Intent intent;

    String toid;
    String to;
    String from;
    Bundle bd;

    ListView con;
    ArrayList<message> mdata;
    MessageAdapter madapter;

    SimpleDateFormat ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);
        bind();
        list();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=mes.getText().toString();
                mes.setText("");
                if(!text.isEmpty()){
                    String time=ft.format(new Date(System.currentTimeMillis()));
                    ChatRequest(text,time);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mes.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mes.clearFocus();
                    con.setSelection(madapter.getCount()-1);
                }
            }
        });
    }
    private void bind(){
        ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        back=findViewById(R.id.btn_back);
        send=findViewById(R.id.btn_send);
        title=findViewById(R.id.title);
        mes=findViewById(R.id.chat_mes);
        con=findViewById(R.id.chat_con);
        intent=getIntent();
        mdata=new ArrayList<>();
        bd=((Data)getApplication()).bd;
        from=bd.getString("name");
        toid=intent.getStringExtra("toid");
        to=intent.getStringExtra("to");
        title.setText(toid);

        MessageGetRequest();
    }
    private void list(){
        madapter=new MessageAdapter(chat.this,mdata);
        con.setAdapter(madapter);
    }

    //向服务器发送修改信息请求
    private void ChatRequest(String text,String time) {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "Chat";    //注②
        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(chat.this);

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
                                    madapter.add(new message(1,R.mipmap.portrait,from,time,text));
                                    madapter.notifyDataSetChanged();
                                    break;
                                }
                                case 1:Toast.makeText(chat.this,"Account Error!",Toast.LENGTH_SHORT).show();break;
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            e.printStackTrace();
                            Toast.makeText(chat.this,"Syntax Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(chat.this,"Failed to connect!.",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_id", bd.getInt("type")==0?bd.getString("id"):toid);  //注⑥
                params.put("doctor_id",bd.getInt("type")==1?bd.getString("id"):toid);
                params.put("password", bd.getString("password"));
                params.put("type",String.valueOf(bd.getInt("type")));
                params.put("text",text);
                params.put("time",time);
                params.put("RequestType",tag);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //向服务器发送获取历史消息请求
    private void MessageGetRequest() {
        //请求地址
        String url = bd.getString("url");    //注①
        String tag = "MessageGet";    //注②
        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(chat.this);

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
                                    int type=bd.getInt("type");
                                    JSONArray messages=jsonObject.getJSONArray("messages");
                                    for(int i=0;i<messages.length();i++){
                                        JSONObject message=messages.getJSONObject(i);
                                        message m=new message(type==message.getInt("type")?1:0,R.mipmap.portrait,
                                                type==message.getInt("type")?from:to,
                                                message.getString("time"),
                                                message.getString("text"));
                                        mdata.add(m);
                                    }
                                    list();
                                    break;
                                }
                                case 1:Toast.makeText(chat.this,"Account Error!",Toast.LENGTH_SHORT).show();break;
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            e.printStackTrace();
                            Toast.makeText(chat.this,"Syntax Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(chat.this,"Failed to connect!.",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("patient_id", bd.getInt("type")==0?bd.getString("id"):toid);  //注⑥
                params.put("doctor_id",bd.getInt("type")==1?bd.getString("id"):toid);
                params.put("password", bd.getString("password"));
                params.put("type",String.valueOf(bd.getInt("type")));
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
