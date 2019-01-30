package com.bw.movie.my.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.my.bean.AttentionCinemaBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
  * @作者 GXY
  * @创建日期 2019/1/28 19:20
  * @描述 查询用户关注的影院信息Adper
  *
  */
public class AttentionCinemaAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AttentionCinemaBean.ResultBean> mRsult;
    private Context mContext;

    public AttentionCinemaAdaper(Context mContext) {
        this.mContext = mContext;
        mRsult = new ArrayList<>();
    }

    //下拉刷新
    public void setmRsult(List<AttentionCinemaBean.ResultBean> results) {
        mRsult.clear();
        if (results != null) {
            mRsult.addAll(results);
        }
        notifyDataSetChanged();
    }

    //上拉加载
    public void addmResult(List<AttentionCinemaBean.ResultBean> results) {
        if (results != null) {
            mRsult.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.attention_cinema_recycleview_item, viewGroup, false);
        ViewHolderCinema holderCinema = new ViewHolderCinema(view);
        return holderCinema;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderCinema holderCinema = (ViewHolderCinema) viewHolder;
        holderCinema.cinemaaddress.setText(mRsult.get(i).getAddress());
        holderCinema.cinemaimage.setImageURI(Uri.parse(mRsult.get(i).getLogo()));
        holderCinema.cinemaname.setText(mRsult.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mRsult.size();
    }

    class ViewHolderCinema extends RecyclerView.ViewHolder {
        @BindView(R.id.cinemaimage)
        SimpleDraweeView cinemaimage;
        @BindView(R.id.cinemaname)
        TextView cinemaname;
        @BindView(R.id.cinemaaddress)
        TextView cinemaaddress;
        public ViewHolderCinema(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
