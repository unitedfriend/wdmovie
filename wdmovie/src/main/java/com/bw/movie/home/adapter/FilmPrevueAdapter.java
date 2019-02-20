package com.bw.movie.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bw.movie.R;
import com.bw.movie.home.bean.FilmDetailsBean;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;


public class FilmPrevueAdapter extends RecyclerView.Adapter<FilmPrevueAdapter.ViewHoder> {

    private Context mContext;
    private List<FilmDetailsBean.ResultBean.ShortFilmListBean> mList;
    private ViewHoder viewHoder;

    public FilmPrevueAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<FilmDetailsBean.ResultBean.ShortFilmListBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_film_prevue, null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder viewHoder, int i) {

        this.viewHoder = viewHoder;
        this.viewHoder.customVideoplayerStandard.setUp(mList.get(i).getVideoUrl(),
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
        Uri uri = Uri.parse(mList.get(i).getImageUrl());
        Glide.with(mContext).load(uri).into(viewHoder.customVideoplayerStandard.thumbImageView);
        Display display = this.viewHoder.customVideoplayerStandard.getDisplay();

    }
public void setCanCle(){

            viewHoder.customVideoplayerStandard.releaseAllVideos();


}


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.custom_videoplayer_standard)
        JZVideoPlayerStandard customVideoplayerStandard;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
