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
import com.bw.movie.my.bean.AttentionMovieBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
  * @作者 GXY
  * @创建日期 2019/1/28 19:21
  * @描述 查询用户关注的影片列表Adaper
  *
  */
public class AttentionMovieAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AttentionMovieBean.ResultBean> mResult;
    private Context mContext;

    public AttentionMovieAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }

    //下拉刷新
    public void setmResult(List<AttentionMovieBean.ResultBean> results) {
        mResult.clear();
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    //上拉加载
    public void addmResult(List<AttentionMovieBean.ResultBean> results) {
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.attention_movie_recycleview_item, viewGroup, false);
        ViewHolderMovie holderMovie = new ViewHolderMovie(view);
        return holderMovie;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderMovie holderMovie = (ViewHolderMovie) viewHolder;
        holderMovie.movieimage.setImageURI(Uri.parse(mResult.get(i).getImageUrl()));
        holderMovie.moviename.setText(mResult.get(i).getName());
        holderMovie.moviebrief.setText("简介:"+mResult.get(i).getSummary());
        //设置时间类型
        String date = new SimpleDateFormat("yyyy-MM-dd").format(
                new java.util.Date(mResult.get(i).getReleaseTime()));
        holderMovie.moviedate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderMovie extends RecyclerView.ViewHolder {
        @BindView(R.id.movieimage)
        SimpleDraweeView movieimage;
        @BindView(R.id.moviename)
        TextView moviename;
        @BindView(R.id.moviedate)
        TextView moviedate;
        @BindView(R.id.moviebrief)
        TextView moviebrief;
        public ViewHolderMovie(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
