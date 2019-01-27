package com.bw.movie.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.home.bean.HotBean;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListHotAdapter extends RecyclerView.Adapter<MovieListHotAdapter.ViewHoder> {

    private Context mContext;
    private List<HotBean.ResultBean> mList;
    private final int SUCCESS=1;
    private final int CANCEL=2;

    public MovieListHotAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<HotBean.ResultBean> list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addmList(List<HotBean.ResultBean> list) {
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_lmovieist_hot, viewGroup, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder viewHoder, final int i) {
        Uri uri = Uri.parse(mList.get(i).getImageUrl());
        viewHoder.image.setImageURI(uri);
        viewHoder.name.setText(mList.get(i).getName());
        viewHoder.contentText.setText(mList.get(i).getSummary());
        final boolean followMovie = mList.get(i).isFollowMovie();
        if(followMovie){
            viewHoder.attentionImage.setChecked(true);
        }else{
            viewHoder.attentionImage.setChecked(false);
        }
        viewHoder.attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHoder.attentionImage.isChecked()){
                    if(!followMovie){
                       hotCallBack.hotCallBack(mList.get(i).getId()+"",i,true);
                    }
                }else{
                    if(followMovie){
                        hotCallBack.hotCallBack(mList.get(i).getId()+"",i,false);
                    }
                }
            }
        });
    }
public void setAttentionScccess(int p){
        mList.get(p).setFollowMovie(SUCCESS);
        notifyDataSetChanged();
}
public void setCancelAttention(int p){
    mList.get(p).setFollowMovie(CANCEL);
    notifyDataSetChanged();
}
    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.attentionImage)
        CheckBox attentionImage;
        @BindView(R.id.contentText)
        TextView contentText;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    MovieListHotCallBack hotCallBack;
    public interface MovieListHotCallBack{
        void hotCallBack(String id,int p,boolean b);
    }

    public void setHotCallBack(MovieListHotCallBack hotCallBack) {
        this.hotCallBack = hotCallBack;
    }
}
