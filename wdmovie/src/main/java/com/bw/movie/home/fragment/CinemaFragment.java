package com.bw.movie.home.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
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
import com.bw.movie.camera.fragment.NearCinemaFragment;
import com.bw.movie.camera.fragment.RecommendCinemaFragment;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.my.adaper.RecordViewPagerAdaper;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CinemaFragment extends BaseFragment {
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
    @BindView(R.id.recommend)
    RadioButton recommend;
    @BindView(R.id.near)
    RadioButton near;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;

    @Override
    protected void initData() {
        initCliick();
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        List<Fragment> list = new ArrayList<>();
        RecommendCinemaFragment recommendCinemaFragment = new RecommendCinemaFragment();
        NearCinemaFragment nearCinemaFragment = new NearCinemaFragment();
        list.add(recommendCinemaFragment);
        list.add(nearCinemaFragment);
        RecordViewPagerAdaper recordViewPagerAdaper = new RecordViewPagerAdaper(getChildFragmentManager(),list);
        viewpager.setAdapter(recordViewPagerAdaper);
        initSearchClick();
    }
    /**
     * 点击切换和滑动切换
     * */
    private void initCliick(){
        //点击切换
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.recommend:
                        recommend.setTextColor(Color.WHITE);
                        near.setTextColor(Color.BLACK);
                        viewpager.setCurrentItem(0,false);
                        break;
                    case R.id.near:
                        recommend.setTextColor(Color.BLACK);
                        near.setTextColor(Color.WHITE);
                        viewpager.setCurrentItem(1,false);
                        break;
                    default:
                        break;
                }
            }
        });
        //滑动切换
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        radioGroup.check(R.id.recommend);
                        break;
                    case 1:
                        radioGroup.check(R.id.near);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    /**
      * @作者 GXY
      * @创建日期 2019/1/29 10:56
      * @描述 平移动画, 使搜索框出现在屏幕右侧, 实现隐藏效果
      *
      */
    private void setTranslationOut(View view, int time) {
        ObjectAnimator translationY =ObjectAnimator.ofFloat(view, "translationX", 0, 510f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }

   /**
     * @作者 GXY
     * @创建日期 2019/1/29 10:57
     * @描述 平移动画, 使搜索框出现在屏幕中间实现点击弹出的效果
     *
     */
    private void setTranslationInit(View view, int time) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationX", 510f, 0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }
  /**
    * @作者 GXY
    * @创建日期 2019/1/29 11:02
    * @描述 加载动画, 并设置点击事件
    *
    */
  private boolean b = true;
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
        return R.layout.fragment_cinema;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
