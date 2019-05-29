package com.killua.heartfailuremanagement;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class fg_pat extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private fg_pat_main fg1 ;
    private fg_pat_set fg2 ;

    public fg_pat(FragmentManager fm) {
        super(fm);
        fg1 = new fg_pat_main();
        fg2= new fg_pat_set();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = fg1;
                break;
            case 1:
                fragment = fg2;
                break;
        }
        return fragment;
    }
}
