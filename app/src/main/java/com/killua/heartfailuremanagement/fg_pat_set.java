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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Date;

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

    public fg_pat_set() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_pat_set, container, false);
        bind();

        return view;
    }
    private void bind(){
        portrait_bar=view.findViewById(R.id.portrait_bar);
        portrait_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        name_bar=view.findViewById(R.id.name_bar);
        name=view.findViewById(R.id.name);
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
                                //change medicine
                                String med=et.getText().toString();
                                name.setText(med);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });
        gender_bar=view.findViewById(R.id.gender_bar);
        gender=view.findViewById(R.id.gender);
        gender_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] choice = new String[]{"Male","Female"};
                new AlertDialog.Builder(getContext()).setTitle("Choose Your Gender")
                        .setItems(choice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gender.setText(choice[which]);
                            }
                        }).show();
            }
        });
        birth_bar=view.findViewById(R.id.birth_bar);
        birth=view.findViewById(R.id.birth);
        birth_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd=DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        birth.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
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
                startActivity(new Intent(getActivity(),change_password.class));
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
                                //change medicine
                                String med=et.getText().toString();
                                heart_rate.setText(med);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });

        pressure_bar=view.findViewById(R.id.pressure_bar);
        pressure=view.findViewById(R.id.pressure);
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
                                //change medicine
                                String med=et.getText().toString();
                                pressure.setText(med);
                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });
    }
}
