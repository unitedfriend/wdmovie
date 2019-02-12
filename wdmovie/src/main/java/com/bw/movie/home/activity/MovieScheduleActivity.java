package com.bw.movie.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.bean.FilmDetailsBean;

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
    ImageView movieImage;
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

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String movieName = intent.getStringExtra("movieName");
        String cinemaId = intent.getStringExtra("cinemaId");
        String movieId = intent.getStringExtra("movieId");
        String cinemaAddress = intent.getStringExtra("cinemaAddress");
        String cinemaName = intent.getStringExtra("cinemaName");
        cinemaNameText.setText(cinemaName);
        cinemaAddressText.setText(cinemaAddress);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
      //  doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST,cinemaId,movieId),);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_movieschedule;
    }

    @Override
    protected void netSuccess(Object object) {

    }

    @Override
    protected void netFail(String s) {

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
