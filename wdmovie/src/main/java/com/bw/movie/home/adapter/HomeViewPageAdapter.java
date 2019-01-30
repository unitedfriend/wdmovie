package com.bw.movie.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
/**
 *  @author Tang
 *  @time 2019/1/28  9:05
 *  @describe 首页的三个fragment
 */
public class HomeViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment>mList ;

    public HomeViewPageAdapter(FragmentManager fm,List<Fragment>list) {
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
}
