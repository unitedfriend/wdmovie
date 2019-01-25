package com.bw.movie.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FilmRecycleAdapter extends RecyclerView.Adapter {

    private Context mContext;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class BannerViewHoder extends RecyclerView.ViewHolder{
        public BannerViewHoder(@NonNull View itemView) {
            super(itemView);
        }
    }
    static class HotViewHoder extends RecyclerView.ViewHolder{
        public HotViewHoder(@NonNull View itemView) {
            super(itemView);
        }
    }
    static class ShowingViewHoder extends RecyclerView.ViewHolder{
        public ShowingViewHoder(@NonNull View itemView) {
            super(itemView);
        }
    }
    static class ShowViewHoder extends RecyclerView.ViewHolder{
        public ShowViewHoder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
