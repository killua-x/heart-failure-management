package com.killua.heartfailuremanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class change_password extends AppCompatActivity {
    EditText old_password;
    EditText new_password;
    EditText confirm_password;
    TextView sub;
    Button back;
    //全局变量
    Bundle bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_password);
        bind();
    }
    private void bind(){
        bd=((Data)getApplication()).bd;
        back=findViewById(R.id.btn_back);
        sub=findViewById(R.id.sub);
        old_password=findViewById(R.id.old_password);
        new_password=findViewById(R.id.new_password);
        confirm_password=findViewById(R.id.confirm_password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检测密码
                String old=old_password.getText().toString().trim();
                String password=new_password.getText().toString().trim();
                String confirm=confirm_password.getText().toString().trim();
                if(password.length()>=6&&password.length()<=20){
                    if(password.equals(confirm)){
                        if(old.equals(bd.getString("password"))){
                            Intent it=new Intent();
                            it.putExtra("password",password);
                            setResult(0,it);
                        }
                        else{
                            old_password.setFocusable(true);
                            Toast.makeText(getApplicationContext(), "the current_password isn't correct!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else{
                        confirm_password.setFocusable(true);
                        Toast.makeText(getApplicationContext(), "the password doesn't match!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    new_password.setFocusable(true);
                    Toast.makeText(getApplicationContext(), "password should have a length range between 6 and 20", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
            }
        });
    }
}
