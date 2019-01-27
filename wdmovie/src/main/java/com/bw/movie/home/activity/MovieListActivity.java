package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.home.adapter.MovieListAdapter;
import com.bw.movie.home.fragment.ListHotFragment;
import com.bw.movie.home.fragment.ListShowFragment;
import com.bw.movie.home.fragment.ListShowingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends BaseActivity {
    @BindView(R.id.locationImage)
    ImageView locationImage;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.searchImage)
    ImageView searchImage;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    @BindView(R.id.searchText)
    TextView searchText;
    @BindView(R.id.searchViewGroup)
    ConstraintLayout searchViewGroup;
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    @BindView(R.id.back)
    ImageView back;
    boolean b = true;
    @BindView(R.id.hotBut)
    RadioButton hotBut;
    @BindView(R.id.showingBut)
    RadioButton showingBut;
    @BindView(R.id.showBut)
    RadioButton showBut;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        //加载搜索框动画
        initSearchClick();
        //加载tablayout
        initRadioButton();
        initViewPage();
        //判断是从哪个组过来的
        initTypeGroup();
    }

    private void initTypeGroup() {
        String type = getIntent().getStringExtra("type");
        switch (type){
            case "hot":
                radioGroup.check(R.id.hotBut);
                break;
            case "showing":
                radioGroup.check(R.id.showingBut);
                break;
            case "show":
                radioGroup.check(R.id.showBut);
                break;
        }
    }

    private void initViewPage() {
        List<Fragment> list = new ArrayList<>();
        if (list.size() <= 0) {
            ListHotFragment listHotFragment = new ListHotFragment();
            ListShowingFragment listShowingFragment = new ListShowingFragment();
            ListShowFragment listShowFragment = new ListShowFragment();
            list.add(listHotFragment);
            list.add(listShowingFragment);
            list.add(listShowFragment);
        }
        MovieListAdapter listAdapter = new MovieListAdapter(getSupportFragmentManager(), list);
        viewPage.setAdapter(listAdapter);

    }



    private void initRadioButton() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.hotBut:
                        hotBut.setTextColor(Color.WHITE);
                        showingBut.setTextColor(Color.BLACK);
                        showBut.setTextColor(Color.BLACK);
                        viewPage.setCurrentItem(0,false);
                        break;
                    case R.id.showingBut:
                        hotBut.setTextColor(Color.BLACK);
                        showingBut.setTextColor(Color.WHITE);
                        showBut.setTextColor(Color.BLACK);
                        viewPage.setCurrentItem(1,false);
                        break;
                    case R.id.showBut:
                        hotBut.setTextColor(Color.BLACK);
                        showingBut.setTextColor(Color.BLACK);
                        showBut.setTextColor(Color.WHITE);
                        viewPage.setCurrentItem(2,false);
                        break;
                }
            }
        });
        viewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        radioGroup.check(R.id.hotBut);
                        break;
                    case 1:
                        radioGroup.check(R.id.showingBut);
                        break;
                    case 2:
                        radioGroup.check(R.id.showBut);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * @author Tang
     * @time 2019/1/27  10:58
     * @describe 加载动画, 并设置点击事件
     */
    private void initSearchClick() {
        setTranslationOut(searchViewGroup, 0);
        searchEditText.setVisibility(View.GONE);
        searchText.setVisibility(View.GONE);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b) {
                    b = !b;
                    setTranslationInit(searchViewGroup, 500);
                    searchEditText.setVisibility(View.VISIBLE);
                    searchText.setVisibility(View.VISIBLE);
                } else {
                    b = !b;
                    setTranslationOut(searchViewGroup, 500);
                    searchEditText.setVisibility(View.GONE);
                    searchText.setVisibility(View.GONE);
                }
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b = !b;
                setTranslationOut(searchViewGroup, 500);
                searchEditText.setVisibility(View.GONE);
                searchText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_movielist;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }

    /**
     * @author Tang
     * @time 2019/1/26  9:29
     * @describe 平移动画, 使搜索框出现在屏幕右侧, 实现隐藏效果
     */
    private void setTranslationOut(View view, int time) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view, "translationX", 0, 510f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }

    /**
     * @author Tang
     * @time 2019/1/26  9:29
     * @describe 平移动画, 使搜索框出现在屏幕中间实现点击弹出的效果
     */
    private void setTranslationInit(View view, int time) {
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view, "translationX", 510f, 0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }


}
