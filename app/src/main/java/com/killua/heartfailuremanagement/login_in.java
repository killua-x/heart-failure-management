package com.killua.heartfailuremanagement;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.drawable.Drawable;
import android.widget.Toast;


public class login_in extends AppCompatActivity {
    EditText id;
    EditText password;
    Button button_register;
    Button button_log_in;

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
                startActivity(new Intent(login_in.this,patient_interface.class));
            }
        });
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

        //button_register.setOnClickListener(new );
    }
    private void iconsize(){
        Drawable id_icon[]=id.getCompoundDrawables();
        id_icon[0].setBounds(0,0,40,40);
        id.setCompoundDrawables(id_icon[0],id_icon[1],id_icon[2],id_icon[3]);
        Drawable password_icon[]=password.getCompoundDrawables();
        password_icon[0].setBounds(0,0,40,40);
        password.setCompoundDrawables(password_icon[0],password_icon[1],password_icon[2],password_icon[3]);
    }
}
