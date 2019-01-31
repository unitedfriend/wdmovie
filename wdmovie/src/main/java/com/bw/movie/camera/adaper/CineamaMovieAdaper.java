package com.bw.movie.camera.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.camera.bean.CinemaMovieListBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CineamaMovieAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CinemaMovieListBean.ResultBean> mResult;
    private Context mContext;

    public CineamaMovieAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }

    public void setmResult(List<CinemaMovieListBean.ResultBean> results) {
        mResult.clear();
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cineama_date_list_recycle_item, viewGroup, false);
        ViewHolderCinemaMovie holderCinemaMovie = new ViewHolderCinemaMovie(view);
        return holderCinemaMovie;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderCinemaMovie holderCinemaMovie = (ViewHolderCinemaMovie) viewHolder;
        holderCinemaMovie.screeningHall.setText(mResult.get(i).getScreeningHall());
        holderCinemaMovie.beginTime.setText(mResult.get(i).getBeginTime());
        holderCinemaMovie.endTime.setText(mResult.get(i).getEndTime());
        holderCinemaMovie.duration.setText(mResult.get(i).getDuration()+"分钟");

    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderCinemaMovie extends RecyclerView.ViewHolder {
        @BindView(R.id.screeningHall)
        TextView screeningHall;
        @BindView(R.id.duration)
        TextView duration;
        @BindView(R.id.beginTime)
        TextView beginTime;
        @BindView(R.id.endTime)
        TextView endTime;
        @BindView(R.id.end)
        TextView end;
        @BindView(R.id.skip)
        ImageButton skip;
        @BindView(R.id.price)
        TextView price;
        public ViewHolderCinemaMovie(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
