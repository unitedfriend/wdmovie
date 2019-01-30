package com.bw.movie.camera.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.camera.bean.FindMovieListByCinemaIdBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindMovieListAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FindMovieListByCinemaIdBean.ResultBean> mResult;
    private Context mContext;

    public FindMovieListAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }
    public void setmResult(List<FindMovieListByCinemaIdBean.ResultBean> results){
        mResult.clear();
        if(results!=null){
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_banner_image, viewGroup, false);
        ViewHolderFindMovieList holderFindMovieList = new ViewHolderFindMovieList(view);
        return holderFindMovieList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderFindMovieList holderFindMovieList = (ViewHolderFindMovieList) viewHolder;
        holderFindMovieList.image.setImageURI(Uri.parse(mResult.get(i).getImageUrl()));
        holderFindMovieList.title.setText(mResult.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderFindMovieList extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.layout)
        ConstraintLayout layout;
        public ViewHolderFindMovieList(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
