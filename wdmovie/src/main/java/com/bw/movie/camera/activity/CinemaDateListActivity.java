package com.bw.movie.camera.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.adaper.CineamaMovieAdaper;
import com.bw.movie.camera.adaper.FindMovieListAdaper;
import com.bw.movie.camera.bean.CinemaMovieListBean;
import com.bw.movie.camera.bean.FindCinemaInfoBean;
import com.bw.movie.camera.bean.FindMovieListByCinemaIdBean;
import com.bw.movie.camera.bean.RecommendBean;
import com.bw.movie.seat.activity.SeatActivity;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import recycler.coverflow.CoverFlowLayoutManger;
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
    private int cameraId;
    private FindMovieListAdaper movieListAdaper;
    private CineamaMovieAdaper movieAdaper;
    private List<FindMovieListByCinemaIdBean.ResultBean> result;
    private int i;
    private int movieId;
    private String movieName;
    private String name;
    private String address;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        //影院id
        cameraId = intent.getIntExtra("id", 0);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET,cameraId),FindCinemaInfoBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_LIST_BY_CINEMAID_GET,cameraId),FindMovieListByCinemaIdBean.class);
        cinemaFilm.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {

            @Override
            public void onItemSelected(int position) {
                i=position;
                movieName = result.get(position).getName();
                movieId = result.get(position).getId();
                doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST,cameraId, movieId),CinemaMovieListBean.class);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        movieListAdaper = new FindMovieListAdaper(this);
        cinemaFilm.setAdapter(movieListAdaper);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        cinemaFilmScheduling.setLayoutManager(layoutManager);
        movieAdaper = new CineamaMovieAdaper(this);
        cinemaFilmScheduling.setAdapter(movieAdaper);
        movieAdaper.setCallBackCinemaMovie(new CineamaMovieAdaper.CallBackCinemaMovie() {
            @Override
            public void CallBack(CinemaMovieListBean.ResultBean resultBean) {
                Intent intent = new Intent(CinemaDateListActivity.this,SeatActivity.class);
                intent.putExtra("movieName",movieName);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("resultBean",resultBean);
                startActivity(intent);
            }
        });
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
                address = result.getAddress();
                String logo = result.getLogo();
                name = result.getName();
                cinemaName.setText(name);
                cinemaAddress.setText(address);
                cinemaLogo.setImageURI(Uri.parse(logo));
            }
        }else if(object instanceof FindMovieListByCinemaIdBean){
            FindMovieListByCinemaIdBean movieListByCinemaIdBean = (FindMovieListByCinemaIdBean) object;
            result = movieListByCinemaIdBean.getResult();
            cinemaFilm.smoothScrollToPosition(result.size()/2);
            if(movieListByCinemaIdBean == null || !movieListByCinemaIdBean.isSuccess()){
                ToastUtil.showToast(movieListByCinemaIdBean.getMessage());
            }else{
                movieListAdaper.setmResult(movieListByCinemaIdBean.getResult());
            }
        }else if(object instanceof CinemaMovieListBean){
            CinemaMovieListBean movieListBean = (CinemaMovieListBean) object;
            if(movieListBean==null || !movieListBean.isSuccess()){
                ToastUtil.showToast(movieListBean.getMessage());
            }else{
                movieAdaper.setmResult(movieListBean.getResult());
            }

        }
    }

    @Override
    protected void netFail(String s) {

    }
    @OnClick(R.id.cinema_film_return)
    public void onViewClicked() {
        finish();
    }
}
