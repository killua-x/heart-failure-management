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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class chat extends AppCompatActivity {
    Button back;
    Button send;
    TextView title;
    EditText mes;
    Intent intent;
    String to;
    String from;

    ListView con;
    ArrayList<message> mdata;
    MessageAdapter madapter;

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
                    madapter.add(new message(1,R.mipmap.portrait,from,new Date(System.currentTimeMillis()),text));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mes.clearFocus();
                    con.setSelection(madapter.getCount()-1);
                }
            }
        });
    }
    private void bind(){
        back=findViewById(R.id.btn_back);
        send=findViewById(R.id.btn_send);
        title=findViewById(R.id.title);
        mes=findViewById(R.id.chat_mes);
        con=findViewById(R.id.chat_con);
        intent=getIntent();

        to=intent.getStringExtra("to");
        from=intent.getStringExtra("from");
        title.setText(to);

    }
    private void list(){
        mdata=new ArrayList<>();
        mdata.add(new message(1,R.mipmap.portrait,from,new Date(System.currentTimeMillis()),"Hi"));
        mdata.add(new message(0,R.mipmap.portrait,to,new Date(System.currentTimeMillis()),"Are you Ok?"));
        mdata.add(new message(0,R.mipmap.portrait,to,new Date(System.currentTimeMillis()),"Hi"));
        madapter=new MessageAdapter(chat.this,mdata);
        con.setAdapter(madapter);
    }
}
