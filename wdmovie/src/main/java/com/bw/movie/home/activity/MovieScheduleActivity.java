package com.bw.movie.home.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.adaper.CineamaMovieAdaper;
import com.bw.movie.camera.bean.CinemaMovieListBean;
import com.bw.movie.camera.bean.FindCinemaInfoBean;
import com.bw.movie.home.adapter.FindAllCinemaCommentAdapter;
import com.bw.movie.home.adapter.RevirwAdapter;
import com.bw.movie.home.bean.CommentPraiseBean;
import com.bw.movie.home.bean.FilmCommentBean;
import com.bw.movie.home.bean.FilmDetailsBean;
import com.bw.movie.home.bean.FindAllCinemaComment;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieScheduleActivity extends BaseActivity {
    @BindView(R.id.cinemaName_text)
    TextView cinemaNameText;
    @BindView(R.id.cinemaAddress_text)
    TextView cinemaAddressText;
    @BindView(R.id.gou_cinemaImage)
    ImageView gouCinemaImage;
    @BindView(R.id.movieImage)
    SimpleDraweeView movieImage;
    @BindView(R.id.movieName_text)
    TextView movieNameText;
    @BindView(R.id.movieType)
    TextView movieType;
    @BindView(R.id.movieDirector)
    TextView movieDirector;
    @BindView(R.id.movieTime)
    TextView movieTime;
    @BindView(R.id.movieField)
    TextView movieField;
    @BindView(R.id.c1)
    ConstraintLayout c1;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.backImage)
    ImageView backImage;
    private CineamaMovieAdaper cineamaMovieAdaper;
    private View popupView;
    private int page;
    private FindAllCinemaCommentAdapter adapter;
private int i;
    private XRecyclerView xRecyclerView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String movieName = intent.getStringExtra("movieName");
        final String cinemaId = intent.getStringExtra("cinemaId");
        String movieId = intent.getStringExtra("movieId");
        String cinemaAddress = intent.getStringExtra("cinemaAddress");
        String cinemaName = intent.getStringExtra("cinemaName");
        cinemaNameText.setText(cinemaName);
        cinemaAddressText.setText(cinemaAddress);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST,Integer.parseInt(cinemaId),Integer.parseInt(movieId)),CinemaMovieListBean.class);
        popupView = View.inflate(MovieScheduleActivity.this,R.layout.popup_movie_schedule,null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(layoutManager);
        cineamaMovieAdaper = new CineamaMovieAdaper(this);
        recycle.setAdapter(cineamaMovieAdaper);
        cineamaMovieAdaper.setCallBackCinemaMovie(new CineamaMovieAdaper.CallBackCinemaMovie() {
            @Override
            public void CallBack(CinemaMovieListBean.ResultBean resultBean) {
                int id = resultBean.getId();
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cinemaNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNetWorkGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET,Integer.parseInt(cinemaId)),FindCinemaInfoBean.class);
                PopupWindow popuoWindow = getPopView(popupView);
                popuoWindow.showAsDropDown(v);
            }
        });
        TextView datesButton = popupView.findViewById(R.id.datesButton);
    TextView commentButton = popupView.findViewById(R.id.commentButton);
        datesButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupView.findViewById(R.id.datesLine).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.commentLine).setVisibility(View.INVISIBLE);
            popupView.findViewById(R.id.datesLayout).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.commentLayout).setVisibility(View.INVISIBLE);

        }
    });
    xRecyclerView = popupView.findViewById(R.id.xRecycle);
    LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
    adapter = new FindAllCinemaCommentAdapter(this);
        xRecyclerView.setLayoutManager(layoutManager1);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter.setClick(new FindAllCinemaCommentAdapter.Click() {
        @Override
        public void onClick(int commentId, XRecyclerView film_comment_recyclerview) {

        }
    });
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
        @Override
        public void onRefresh() {
            page=1;
            doNet(Integer.parseInt(cinemaId),page);
        }

        @Override
        public void onLoadMore() {
            doNet(Integer.parseInt(cinemaId),page);
        }
    });
        adapter.setLucky(new FindAllCinemaCommentAdapter.Lucky() {
        @Override
        public void onLucky(int commentId, int position) {
            i = position;
            Map<String, String> map = new HashMap<>();
            map.put("commentId", String.valueOf(commentId));
            doNetWorkPostRequest(Apis.URL_MOVIE_COMMENT_GREAT_POST,map,CommentPraiseBean.class);
        }
    });
    page = 1;
        commentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doNet(Integer.parseInt(cinemaId),page);
            popupView.findViewById(R.id.datesLine).setVisibility(View.INVISIBLE);
            popupView.findViewById(R.id.commentLine).setVisibility(View.VISIBLE);
            popupView.findViewById(R.id.datesLayout).setVisibility(View.INVISIBLE);
            popupView.findViewById(R.id.commentLayout).setVisibility(View.VISIBLE);
        }
    });

}

    private void doNet(int c,int p){
        doNetWorkGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET,c,p),FindAllCinemaComment.class);
    }
    /**
     *  @author Tang
     *  @time 2019/2/12  20:29
     *  @describe popupwindo
     */
    private PopupWindow getPopView(View view) {
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        popupWindow.setFocusable(true);
        //设置是否可以触摸
        popupWindow.setTouchable(true);
        //关闭
        int color = getResources().getColor(R.color.popup_bg);
        popupWindow.setBackgroundDrawable(new ColorDrawable(color));
        /*detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });*/
        return popupWindow;
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_movieschedule;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof FilmDetailsBean){
            FilmDetailsBean object1 = (FilmDetailsBean) object;
            movieNameText.setText(object1.getResult().getName());
            movieType.setText("类型: "+object1.getResult().getMovieTypes());
            movieDirector.setText("导演: "+object1.getResult().getDirector());
            movieTime.setText("时长: "+object1.getResult().getDuration());
            movieField.setText("产地: "+object1.getResult().getPlaceOrigin());
            Uri uri = Uri.parse(object1.getResult().getImageUrl());
            movieImage.setImageURI(uri);
        }else if(object instanceof CinemaMovieListBean){
            CinemaMovieListBean object1 = (CinemaMovieListBean) object;
            cineamaMovieAdaper.setmResult(object1.getResult());
        }else if(object instanceof FindCinemaInfoBean){
            FindCinemaInfoBean object1 = (FindCinemaInfoBean) object;
            TextView addressText =popupView.findViewById(R.id.addressText);
            addressText.setText(object1.getResult().getAddress());
            TextView phoneText = popupView.findViewById(R.id.phoneText);
            phoneText.setText(object1.getResult().getPhone());
            TextView pathText = popupView.findViewById(R.id.pathText);
            pathText.setText(object1.getResult().getVehicleRoute());
        }else if(object instanceof FindAllCinemaComment){
            FindAllCinemaComment object1 = (FindAllCinemaComment) object;
            if(page==1){
                adapter.setList(object1.getResult());
            }else{
                adapter.addList(object1.getResult());
            }
            page++;
            xRecyclerView.loadMoreComplete();
            xRecyclerView.refreshComplete();
        }else if(object instanceof CommentPraiseBean){
            CommentPraiseBean object1 = (CommentPraiseBean) object;
            ToastUtil.showToast(object1.getMessage());
            if(object1.getMessage().equals("点赞成功")){
                adapter.addWhetherGreat(i);
            }


        }
    }

    @Override
    protected void netFail(String s) {
        String s1 = s;
    }



    @OnClick({R.id.gou_cinemaImage, R.id.backImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gou_cinemaImage:
                break;
            case R.id.backImage:
                break;
        }
    }
}
