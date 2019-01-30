package com.bw.movie.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.home.bean.BuyTicketCinameList;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BuyTicketAdapter extends RecyclerView.Adapter<BuyTicketAdapter.ViewHoder> {


    private Context mContext;
    private List<BuyTicketCinameList.ResultBean> mList;

    public BuyTicketAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<BuyTicketCinameList.ResultBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_film_buyticket, null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, int i) {
        viewHoder.addressText.setText(mList.get(i).getAddress());
        viewHoder.cinemaName.setText(mList.get(i).getName());
        viewHoder.distance.setText(mList.get(i).getDistance()+"km");
        Uri uri = Uri.parse(mList.get(i).getLogo());
        viewHoder.cinemaImage.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.cinemaImage)
        SimpleDraweeView cinemaImage;
        @BindView(R.id.cinemaName)
        TextView cinemaName;
        @BindView(R.id.addressText)
        TextView addressText;
        @BindView(R.id.distance)
        TextView distance;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
