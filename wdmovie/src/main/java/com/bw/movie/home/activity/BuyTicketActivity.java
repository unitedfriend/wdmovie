package com.bw.movie.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.bean.FollowCinemaBean;
import com.bw.movie.home.adapter.BuyTicketAdapter;
import com.bw.movie.home.bean.BuyTicketCinameList;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyTicketActivity extends BaseActivity {
    @BindView(R.id.movieName)
    TextView movieName;
    @BindView(R.id.xrecycleView)
    XRecyclerView xrecycleView;
    private BuyTicketAdapter adapter;
    private String name;
    private String movieId;

    @Override
    protected void initData() {

    }
private int position;
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("name");
        movieName.setText(name);
        movieId = getIntent().getStringExtra("movieId");
        doNetWorkGetRequest(Apis.URL_FIND_CINEMAS_LIST_BY_MOVIE_ID_GET + "?movieId=" + movieId, BuyTicketCinameList.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecycleView.setLayoutManager(layoutManager);
        adapter = new BuyTicketAdapter(this);
        xrecycleView.setAdapter(adapter);
        xrecycleView.setLoadingMoreEnabled(false);
        xrecycleView.setPullRefreshEnabled(false);
        adapter.setTicketCallBcak(new BuyTicketAdapter.BuyTicketCallBcak() {
            @Override
            public void onClick(String id,String address,String cinemaName) {
                //TODO 得到名字和地址,跳转时传过去
                startActivity(new Intent(BuyTicketActivity.this,MovieScheduleActivity.class).putExtra("movieName",name).putExtra("movieId",movieId).putExtra("cinemaId",id).putExtra("cinemaAddress",address).putExtra("cinemaName",cinemaName));
            }
            /*@Override
            public void onAttention(int p,boolean b,String id) {
                position=p;
                if(b) {
                    doNetWorkGetRequest(String.format(Apis.URL_FOLLOW_MOVIE_GET,id),FollowCinemaBean.class);
                }else{
                    doNetWorkGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_MOVIE_GET,id),FollowCinemaBean.class);
                }
            }*/

        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_buyticket;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof BuyTicketCinameList){
            BuyTicketCinameList object1 = (BuyTicketCinameList) object;

            adapter.setmList(object1.getResult());
        }

        /*else if(object instanceof FollowCinemaBean){
            FollowCinemaBean object1 = (FollowCinemaBean) object;
            ToastUtil.showToast(object1.getMessage());
            if(object1.getMessage().equals("关注成功")){
                adapter.setCheckBox(position,true);
            }else{
                adapter.setCheckBox(position,false);
            }
        }*/
    }

    @Override
    protected void netFail(String s) {
        String s1 = s;
    }


}
