package com.bw.movie.home.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.home.bean.AllBean;
import com.dalong.carrousellayout.CarrouselLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilmRecycleAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private AllBean allBean;
    private final int BANNER = 0;
    private final int HOT = 1;
    private final int SHOWING = 2;
    private final int SHOW = 3;
    private final int COUNT = 4;

    public FilmRecycleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private void setAllBean(AllBean bean) {
        allBean = bean;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case BANNER:
                View bannerView = LayoutInflater.from(mContext).inflate(R.layout.item_home_banner, viewGroup, false);
                return new BannerViewHoder(bannerView);
            case HOT:
                View hotView = LayoutInflater.from(mContext).inflate(R.layout.item_home_hot, viewGroup, false);
                return new HotViewHoder(hotView);
            case SHOWING:
                View showingView = LayoutInflater.from(mContext).inflate(R.layout.item_home_showing, viewGroup, false);
                return new ShowingViewHoder(showingView);
            case SHOW:
                View showView = LayoutInflater.from(mContext).inflate(R.layout.item_home_show, viewGroup, false);
                return new ShowViewHoder(showView);
        }
        return null;
    }
    boolean b=true;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case BANNER:
                final BannerViewHoder bannerViewHoder= (BannerViewHoder) viewHolder;
              // ObjectAnimator translationX = new ObjectAnimator().ofFloat(myyuan,"translationX",0,600f);
                setTranslationOut(bannerViewHoder.searchViewGroup,0);
                bannerViewHoder.searchEditText.setVisibility(View.GONE);
                bannerViewHoder.searchText.setVisibility(View.GONE);
                bannerViewHoder.searchImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(b){
                            b=!b;
                           setTranslationInit(bannerViewHoder.searchViewGroup,500);
                            bannerViewHoder.searchEditText.setVisibility(View.VISIBLE);
                            bannerViewHoder.searchText.setVisibility(View.VISIBLE);
                        }
                    }
                });
                bannerViewHoder.searchText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b=!b;
                        setTranslationOut(bannerViewHoder.searchViewGroup,500);
                        bannerViewHoder.searchEditText.setVisibility(View.GONE);
                        bannerViewHoder.searchText.setVisibility(View.GONE);
                    }
                });


                break;
            case HOT:
                break;
            case SHOWING:
                break;
            case SHOW:
                break;
        }
    }
/**
 *  @author Tang
 *  @time 2019/1/26  9:29
 *  @describe 平移动画,使搜索框出现在屏幕右侧,实现隐藏效果
 */
    private void setTranslationOut(View view,int time){
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view,"translationX",0,510f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }
    /**
     *  @author Tang
     *  @time 2019/1/26  9:29
     *  @describe 平移动画,使搜索框出现在屏幕中间实现点击弹出的效果
     */
    private void setTranslationInit(View view,int time){
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view,"translationX",510f,0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationY); //设置动画
        animatorSet.setDuration(time);  //设置动画时间
        animatorSet.start();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == BANNER) {
            return BANNER;
        } else if (position == HOT) {
            return HOT;
        } else if (position == SHOWING) {
            return SHOWING;
        } else if (position == SHOW) {
            return SHOW;
        }
        return position;
    }

    @Override
    public int getItemCount() {
        return COUNT;
    }

    static class BannerViewHoder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.carrousel)
        CarrouselLayout carrousel;

        public BannerViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HotViewHoder extends RecyclerView.ViewHolder {
        public HotViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ShowingViewHoder extends RecyclerView.ViewHolder {
        public ShowingViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ShowViewHoder extends RecyclerView.ViewHolder {
        public ShowViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
