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


import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class fg_pat_main extends Fragment {
    View view;
    private ListView list;
    private TextView name;
    private RelativeLayout info;
    private doctor mine;
    private String medicine;
    private TextView hint;

    private TextView title1;
    private TextView title2;
    private View div1;
    private View div2;

    public fg_pat_main() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_pat_main, container, false);
        medicine="None";
        bind1();
        return view;
    }
    private void bind1(){
        list= view.findViewById(R.id.list);
        title1=view.findViewById(R.id.title1);
        div1=view.findViewById(R.id.div1);
        title1.setVisibility(View.VISIBLE);
        div1.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);

        List<doctor> mData=new ArrayList<doctor>();
        mData.add(new doctor("dasda","Mr Yang","Male",20,R.mipmap.portrait));
        mData.add(new doctor("sdfdas","Mr Liu","Female",20,R.mipmap.portrait));
        mData.add(new doctor("dfdfa","Mr Li","Male",20,R.mipmap.portrait));

        MyAdapter<doctor> myAdapter=new MyAdapter<doctor>((ArrayList<doctor>) mData,R.layout.frag_pat_main_list) {
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
                        mine=obj;
                        hide1();
                        bind2();
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
        name=view.findViewById(R.id.name);
        info=view.findViewById(R.id.info);
        title2=view.findViewById(R.id.title2);
        div2=view.findViewById(R.id.div2);
        name.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        title2.setVisibility(View.VISIBLE);
        div2.setVisibility(View.VISIBLE);

        ImageView portrait=view.findViewById(R.id.portrait);
        TextView id=view.findViewById(R.id.id);
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
        medicine.setText("Medicine: "+this.medicine);
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
                                hide2();
                                bind0();
                            }
                        }).show();
            }
        });
        mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),chat.class);
                intent.putExtra("to",mine.name);
                intent.putExtra("from","Dr Li");
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
}
