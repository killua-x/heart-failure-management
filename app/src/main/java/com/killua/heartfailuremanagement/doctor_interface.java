package com.killua.heartfailuremanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class doctor_interface extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{
    ViewPager vp;
    fg_doc mAdapter;
    RadioGroup doc_tab;
    RadioButton doc_main;
    RadioButton doc_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor_interface);
        mAdapter = new fg_doc(getSupportFragmentManager());
        bindview();
    }

    private void bindview(){
        doc_tab=findViewById(R.id.doc_tab);
        doc_main=findViewById(R.id.doc_main);
        doc_set=findViewById(R.id.doc_set);
        doc_tab.setOnCheckedChangeListener(this);

        vp=findViewById(R.id.vp);
        vp.setAdapter(mAdapter);
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.doc_main:
                vp.setCurrentItem(0);
                break;
            case R.id.doc_set:
                vp.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vp.getCurrentItem()) {
                case 0:
                    doc_main.setChecked(true);
                    break;
                case 1:
                    doc_set.setChecked(true);
                    break;
            }
        }
    }
}
