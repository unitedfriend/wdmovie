package com.bw.movie.home.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MovieListAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList ;
    private String [] pageName ={"热门推荐","正在热播","即将上映"};
    public MovieListAdapter(FragmentManager fm,List<Fragment>list) {
        super(fm);
        mList=list;
    }

    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageName[position];
    }
}
