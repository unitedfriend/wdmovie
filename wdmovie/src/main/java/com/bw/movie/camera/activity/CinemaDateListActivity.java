package com.bw.movie.camera.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.adaper.FindMovieListAdaper;
import com.bw.movie.camera.bean.FindCinemaInfoBean;
import com.bw.movie.camera.bean.FindMovieListByCinemaIdBean;
import com.bw.movie.camera.bean.RecommendBean;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import recycler.coverflow.RecyclerCoverFlow;

/**
 * @作者 GXY
 * @创建日期 2019/1/29 16:58
 * @描述 影院排期列表Activity
 */
public class CinemaDateListActivity extends BaseActivity {
    @BindView(R.id.cinema_logo)
    SimpleDraweeView cinemaLogo;
    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.cinema_navigation)
    ImageView cinemaNavigation;
    @BindView(R.id.cinema_film)
    RecyclerCoverFlow cinemaFilm;
    @BindView(R.id.cinema_film_scheduling)
    RecyclerView cinemaFilmScheduling;
    @BindView(R.id.cinema_film_return)
    ImageView cinemaFilmReturn;
    private int id;
    private FindMovieListAdaper movieListAdaper;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        //影院id
        id = intent.getIntExtra("id", 0);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET,id),FindCinemaInfoBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_LIST_BY_CINEMAID_GET,id),FindMovieListByCinemaIdBean.class);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        movieListAdaper = new FindMovieListAdaper(this);
        cinemaFilm.setAdapter(movieListAdaper);
        cinemaFilm.smoothScrollToPosition(0);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cinema_datelist;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof FindCinemaInfoBean){
            FindCinemaInfoBean cinemaInfoBean = (FindCinemaInfoBean) object;
            if(cinemaInfoBean == null || !cinemaInfoBean.isSuccess()){
                ToastUtil.showToast(cinemaInfoBean.getMessage());
            }else{
                FindCinemaInfoBean.ResultBean result = cinemaInfoBean.getResult();
                String address = result.getAddress();
                String logo = result.getLogo();
                String name = result.getName();
                cinemaName.setText(name);
                cinemaAddress.setText(address);
                cinemaLogo.setImageURI(Uri.parse(logo));
            }
        }else if(object instanceof FindMovieListByCinemaIdBean){
            FindMovieListByCinemaIdBean movieListByCinemaIdBean = (FindMovieListByCinemaIdBean) object;
            if(movieListByCinemaIdBean == null || !movieListByCinemaIdBean.isSuccess()){
                ToastUtil.showToast(movieListByCinemaIdBean.getMessage());
            }else{
                movieListAdaper.setmResult(movieListByCinemaIdBean.getResult());
            }
        }
    }

    @Override
    protected void netFail(String s) {
        String s1 = s;
    }
    @OnClick(R.id.cinema_film_return)
    public void onViewClicked() {
    }
}
