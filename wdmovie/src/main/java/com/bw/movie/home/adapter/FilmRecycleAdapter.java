package com.bw.movie.home.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.home.bean.AllBean;
import com.bw.movie.home.bean.HotBean;
import com.bw.movie.home.bean.ShowBean;
import com.bw.movie.home.bean.ShowingBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;
/**
 *  @author Tang
 *  @time 2019/1/28  8:48
 *  @describe 电影首页的多条目
 */
public class FilmRecycleAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private AllBean allBean;
    private final int BANNER = 0;
    private final int HOT = 1;
    private final int SHOWING = 2;
    private final int SHOW = 3;
    private final int COUNT = 4;
    private int current;
    private Handler handler;
    private BannerViewHoder bannerViewHoder;

    public FilmRecycleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setAllBean(AllBean bean) {
        allBean = bean;
        notifyDataSetChanged();
    }

    
    /**
     *  @author Tang
     *  @time 2019/1/28  8:49
     *  @describe 加载不同视图
     *  bannerView 轮播图
     *  hotView 热门电影
     *  showingView 正在热映
     *  showView 即将上映
     */
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
    //搜索框是否弹出的标识
    boolean b = true;

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        //不同类型加载不同数据
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
            case BANNER:
                //设置搜索框进来就被移动到屏幕外
                bannerViewHoder = (BannerViewHoder) viewHolder;
                bannerViewHoder.locationImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.addressCallBack();
                    }
                });
                setTranslationOut(bannerViewHoder.searchViewGroup, 0);
                bannerViewHoder.searchEditText.setVisibility(View.GONE);
                bannerViewHoder.searchText.setVisibility(View.GONE);
                //点击搜索图片时判断是否显示或隐藏
                bannerViewHoder.searchImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b) {
                            b = !b;
                            setTranslationInit(bannerViewHoder.searchViewGroup, 500);
                            bannerViewHoder.searchEditText.setVisibility(View.VISIBLE);
                            bannerViewHoder.searchText.setVisibility(View.VISIBLE);
                        }else{
                            b=!b;
                            setTranslationOut(bannerViewHoder.searchViewGroup,500);
                            bannerViewHoder.searchEditText.setVisibility(View.GONE);
                            bannerViewHoder.searchText.setVisibility(View.GONE);
                        }
                    }
                });

                //加载子条目的recycleview
                RecyclerCoverFlowAdapter recyclerCoverFlowAdapter = new RecyclerCoverFlowAdapter(mContext);
                ((BannerViewHoder) viewHolder).list.setAdapter(recyclerCoverFlowAdapter);
                final HotBean hot1 = allBean.getHot();
                bannerViewHoder.seekBar.setMax(hot1.getResult().size()-1);

                bannerViewHoder.seekBar.setFocusable(false);
                
                recyclerCoverFlowAdapter.setmList(hot1.getResult());
                current=5;
                //轮播图自动轮播
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        int selectedPos = bannerViewHoder.list.getSelectedPos();
                        ((BannerViewHoder) viewHolder).list.smoothScrollToPosition(++selectedPos);
                        current++;
                        handler.sendEmptyMessageDelayed(0,3000);
                    }
                };

                //点击隐藏搜索框
                bannerViewHoder.searchText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b=!b;
                        setTranslationOut(bannerViewHoder.searchViewGroup,500);
                        bannerViewHoder.searchEditText.setVisibility(View.GONE);
                        bannerViewHoder.searchText.setVisibility(View.GONE);
                        String s = ((BannerViewHoder) viewHolder).searchText.getText().toString();
                        filmCallBack.searchCallBack(s);
                    }
                });
                //每次旋转得到条目位置
                ((BannerViewHoder) viewHolder).list.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
                    @Override
                    public void onItemSelected(int position) {
                        current=position;
                        bannerViewHoder.seekBar.setProgress(position%hot1.getResult().size());
                    }
                });

                handler.sendEmptyMessage(0);
                recyclerCoverFlowAdapter.setRecycleCallBack(new RecyclerCoverFlowAdapter.RecycleCallBack() {
                    @Override
                    public void imageCallBack(String id) {
                        filmCallBack.bannerCallBack(id);
                    }
                });

                break;
            case HOT:
                HotViewHoder hotViewHoder = (HotViewHoder) viewHolder;
                LinearLayoutManager hotLayoutManager = new LinearLayoutManager(mContext);
                hotLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                hotViewHoder.hotRecycleView.setLayoutManager(hotLayoutManager);
                HotAdapter hotAdapter = new HotAdapter(mContext);
                HotBean hot = allBean.getHot();
                hotAdapter.setmList(hot.getResult());
                hotViewHoder.hotRecycleView.setAdapter(hotAdapter);
                hotAdapter.setCallBack(new HotAdapter.HotCallBack() {
                    @Override
                    public void skip(int id) {
                       filmCallBack.detailsCallBack(id+"");
                    }
                });
               hotViewHoder.hotTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.hotSkipGroup();
                    }
                });
                hotViewHoder.hotImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.hotSkipGroup();
                    }
                });
                break;
            case SHOWING:
                ShowingViewHoder showingViewHoder = (ShowingViewHoder) viewHolder;
                LinearLayoutManager showingLayoutManager = new LinearLayoutManager(mContext);
                showingLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                showingViewHoder.showingRecycleView.setLayoutManager(showingLayoutManager);
                ShowingAdapter showingAdapter = new ShowingAdapter(mContext);
                showingViewHoder.showingRecycleView.setAdapter(showingAdapter);
                ShowingBean showing = allBean.getShowing();
                showingAdapter.setmList(showing.getResult());
                showingAdapter.setCallBack(new ShowingAdapter.ShowingCallBack() {
                    @Override
                    public void skip(int id) {
                        filmCallBack.detailsCallBack(id+"");
                    }
                });
                showingViewHoder.showingImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.showingSkipGroup();
                    }
                });
                showingViewHoder.showingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.showingSkipGroup();
                    }
                });
                break;
            case SHOW:
                ShowViewHoder showViewHoder = (ShowViewHoder) viewHolder;
                LinearLayoutManager showLayoutManager = new LinearLayoutManager(mContext);
                showLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                showViewHoder.showRecycleView.setLayoutManager(showLayoutManager);
                ShowAdapter showAdapter = new ShowAdapter(mContext);
                ShowBean show = allBean.getShow();
                showAdapter.setmList(show.getResult());
                showViewHoder.showRecycleView.setAdapter(showAdapter);
                showAdapter.setCallBack(new ShowAdapter.ShowCallBack() {
                    @Override
                    public void skip(int id) {
                        filmCallBack.detailsCallBack(id+"");
                    }
                });
                showViewHoder.showTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.showSkipGroup();
                    }
                });
                showViewHoder.showImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filmCallBack.showSkipGroup();
                    }
                });
                break;
        }
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
        return allBean == null ? 0 : COUNT;
    }

    class BannerViewHoder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.list)
        RecyclerCoverFlow list;
        @BindView(R.id.proBar)
        SeekBar seekBar;
        public BannerViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HotViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.hotTextView)
        TextView hotTextView;
        @BindView(R.id.hotImage)
        ImageView hotImage;
        @BindView(R.id.hotRecycleView)
        RecyclerView hotRecycleView;

        public HotViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ShowingViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.showingTextView)
        TextView showingTextView;
        @BindView(R.id.showingImage)
        ImageView showingImage;
        @BindView(R.id.showingRecycleView)
        RecyclerView showingRecycleView;

        public ShowingViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ShowViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.showTextView)
        TextView showTextView;
        @BindView(R.id.showImage)
        ImageView showImage;
        @BindView(R.id.showRecycleView)
        RecyclerView showRecycleView;

        public ShowViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setLocation(String l){
        bannerViewHoder.locationText.setText(l);
    }

    FilmCallBack filmCallBack;
    public interface FilmCallBack{
        void addressCallBack();
        void searchCallBack(String s);
        void bannerCallBack(String id);
        void hotSkipGroup();
        void showingSkipGroup();
        void showSkipGroup();
        void detailsCallBack(String id);
    }
    public void setFilmCallBack(FilmCallBack callBack){
        filmCallBack=callBack;
    }
}
