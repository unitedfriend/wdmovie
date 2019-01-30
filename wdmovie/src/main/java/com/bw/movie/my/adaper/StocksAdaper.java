package com.bw.movie.my.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.my.bean.ObligationBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 GXY
 * @创建日期 2019/1/28 20:18
 * @描述 已完成
 */
public class StocksAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ObligationBean.ResultBean> mResult;
    private Context mContext;

    public StocksAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }

    //下拉刷新
    public void setmResult(List<ObligationBean.ResultBean> results) {
        mResult.clear();
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    //上拉加载
    public void addmResult(List<ObligationBean.ResultBean> results) {
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.status_recycleview_item, viewGroup, false);
        ViewHolderStocks holderStocks = new ViewHolderStocks(view);
        return holderStocks;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderStocks holderStocks = (ViewHolderStocks) viewHolder;
        holderStocks.name.setText(mResult.get(i).getMovieName());
        String begintime = new SimpleDateFormat("HH:mm:ss").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        String endtime = new SimpleDateFormat("HH:mm:ss").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        holderStocks.beginend.setText(begintime+"-"+endtime);
        holderStocks.ordernumber.setText("订单号:"+mResult.get(i).getOrderId());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        holderStocks.ordernumber.setText("下单时间:"+date);
        holderStocks.movie.setText("影院:"+mResult.get(i).getCinemaName());
        holderStocks.movieroom.setText(mResult.get(i).getScreeningHall());
        holderStocks.num.setText("数量:"+mResult.get(i).getAmount()+"张");
        holderStocks.price.setText("金额:"+mResult.get(i).getPrice()+"元");
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderStocks extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.beginend)
        TextView beginend;
        @BindView(R.id.endbegintext)
        TextView endbegintext;
        @BindView(R.id.ordernumber)
        TextView ordernumber;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.movie)
        TextView movie;
        @BindView(R.id.movieroom)
        TextView movieroom;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.price)
        TextView price;
        public ViewHolderStocks(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
