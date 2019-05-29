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


import java.util.ArrayList;
import java.util.List;

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

    public fg_doc_main() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_doc_main, container, false);
        bind();
        list.setAdapter(myAdapter);
        return view;
    }
    private void bind(){
        list= view.findViewById(R.id.fg_doc_main_list);
        search_bar=view.findViewById(R.id.search_bar);
        search=view.findViewById(R.id.search);
        add=view.findViewById(R.id.add);

        mData=new ArrayList<patient>();
        mData.add(new patient("Mr Yang","Male",20,133,80,R.mipmap.portrait,"1,23,4,5,6"));
        mData.add(new patient("Mr Liu","Male",20,133,80,R.mipmap.portrait,"4..6.7.2"));
        mData.add(new patient("Mr Li","Male",20,133,80,R.mipmap.portrait,"5.3.7.,7,4"));

        myAdapter=new MyAdapter<patient>((ArrayList<patient>) mData,R.layout.frag_doc_main_list) {
            @Override
            public void bindView(ViewHolder holder, patient obj) {
                holder.setText(R.id.doc_list_name,obj.name);
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
                                        myAdapter.remove(holder.getItemPosition());
                                        myAdapter.add(holder.getItemPosition(),new patient(obj.name,obj.gender,obj.age,obj.heart_rate,obj.pressure,obj.portrait,med));

                                    }
                                }).setNegativeButton("cancel",null).show();
                    }
                });
                holder.setOnClickListener(R.id.doc_list_mes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),chat.class);
                        intent.putExtra("to",obj.name);
                        intent.putExtra("from","Dr Li");
                        startActivity(intent);
                    }
                });
            }
        };

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                search_bar.clearFocus();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
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

                            }
                        }).setNegativeButton("cancel",null).show();
            }
        });
    }
}
