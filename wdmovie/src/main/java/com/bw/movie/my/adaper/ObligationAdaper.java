package com.bw.movie.my.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * @创建日期 2019/1/28 19:28
 * @描述 待付款适配器
 */
public class ObligationAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ObligationBean.ResultBean> mResult;
    private Context mContext;

    public ObligationAdaper(Context mContext) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.obligation_recycleview_item, viewGroup, false);
        ViewHolderObligation holderObligation = new ViewHolderObligation(view);
        return holderObligation;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolderObligation holderObligation = (ViewHolderObligation) viewHolder;
        holderObligation.name.setText(mResult.get(i).getMovieName());
        holderObligation.movie.setText("影院:"+mResult.get(i).getCinemaName());
        holderObligation.ordernumber.setText("订单号:"+mResult.get(i).getOrderId());
        holderObligation.movieroom.setText("影厅:"+mResult.get(i).getScreeningHall());
        String date = new SimpleDateFormat("yyyy-MM-dd").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        String begintime = new SimpleDateFormat("HH:mm:ss").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        String endtime = new SimpleDateFormat("HH:mm:ss").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        holderObligation.time.setText("时间:"+date+" "+begintime+"-"+endtime);
        holderObligation.num.setText("数量:"+mResult.get(i).getAmount()+"张");
        holderObligation.price.setText("金额:"+mResult.get(i).getPrice()+"元");
        holderObligation.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackObligation!=null){
                    callBackObligation.callBack(mResult.get(i).getOrderId(),mResult.get(i).getAmount(),mResult.get(i).getPrice());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderObligation extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.ordernumber)
        TextView ordernumber;
        @BindView(R.id.movie)
        TextView movie;
        @BindView(R.id.movieroom)
        TextView movieroom;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.payment)
        Button payment;
        public ViewHolderObligation(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBackObligation callBackObligation;
    public void setCallBackObligation(CallBackObligation callBackObligation){
        this.callBackObligation = callBackObligation;
    }
    public interface CallBackObligation{
        void callBack(String orderId,int amount,double price);
    }
}
