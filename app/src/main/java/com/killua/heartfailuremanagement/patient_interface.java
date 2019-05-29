package com.killua.heartfailuremanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class patient_interface extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{
    ViewPager vp;
    fg_pat mAdapter;
    RadioGroup tab;
    RadioButton main;
    RadioButton set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_patient_interface);
        mAdapter = new fg_pat(getSupportFragmentManager());
        bind();
    }

    private void bind(){
        tab=findViewById(R.id.tab);
        main=findViewById(R.id.main);
        set=findViewById(R.id.set);
        tab.setOnCheckedChangeListener(this);

        vp=findViewById(R.id.vp);
        vp.setAdapter(mAdapter);
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(this);

        main.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main:
                vp.setCurrentItem(0);
                break;
            case R.id.set:
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
                    main.setChecked(true);
                    break;
                case 1:
                    set.setChecked(true);
                    break;
            }
        }
    }
}
