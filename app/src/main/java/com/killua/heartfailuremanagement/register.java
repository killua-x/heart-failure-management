package com.killua.heartfailuremanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;


public class register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button register_button_back;
    Calendar now;
    DatePickerDialog dpd;
    EditText reg_id;
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
                setResult(2);
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
                setResult(0);
                finish();
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
        reg_password=findViewById(R.id.password);
        reg_confirm=findViewById(R.id.reg_confirm_password);
        reg_gender=findViewById(R.id.reg_gender);
        reg_birthday=findViewById(R.id.reg_birthday);
        reg_type=findViewById(R.id.reg_type);
        reg_sub=findViewById(R.id.reg_sub);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        reg_birthday.setText(date);
    }
}
