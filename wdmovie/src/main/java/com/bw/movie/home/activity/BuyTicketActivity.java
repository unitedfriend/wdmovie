package com.bw.movie.home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.adapter.BuyTicketAdapter;
import com.bw.movie.home.bean.BuyTicketCinameList;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyTicketActivity extends BaseActivity {
    @BindView(R.id.movieName)
    TextView movieName;
    @BindView(R.id.xrecycleView)
    XRecyclerView xrecycleView;
    private BuyTicketAdapter adapter;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        String name = getIntent().getStringExtra("name");
        movieName.setText(name);
        String id = getIntent().getStringExtra("id");
        doNetWorkGetRequest(Apis.URL_FIND_CINEMAS_LIST_BY_MOVIE_ID_GET + "?movieId=" + id, BuyTicketCinameList.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecycleView.setLayoutManager(layoutManager);
        adapter = new BuyTicketAdapter(this);
        xrecycleView.setAdapter(adapter);

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
    }

    @Override
    protected void netFail(String s) {

    }


}
