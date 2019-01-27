package com.bw.movie.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.home.bean.HotBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerCoverFlowAdapter extends RecyclerView.Adapter<RecyclerCoverFlowAdapter.ViewHoder> {

    private Context mContext;
    private List<HotBean.ResultBean> mList;

    public RecyclerCoverFlowAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<HotBean.ResultBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_banner_image, viewGroup, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, final int i) {
        Uri uri = Uri.parse(mList.get(i%mList.size()).getImageUrl());
        viewHoder.image.setImageURI(uri);
        viewHoder.title.setText(mList.get(i%mList.size()).getName());
        viewHoder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleCallBack.imageCallBack(mList.get(i%mList.size()).getId()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.layout)
        ConstraintLayout constraintLayout;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    RecycleCallBack recycleCallBack;
    public interface RecycleCallBack{
        void imageCallBack(String id);
    };
    public void setRecycleCallBack(RecycleCallBack callBack){
        recycleCallBack = callBack;
    }
}
