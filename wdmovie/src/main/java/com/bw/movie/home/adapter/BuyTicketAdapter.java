package com.bw.movie.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    public void onBindViewHolder(@NonNull final ViewHoder viewHoder, final int i) {
        viewHoder.addressText.setText(mList.get(i).getAddress());
        viewHoder.cinemaName.setText(mList.get(i).getName());
        viewHoder.distance.setText(mList.get(i).getDistance() + "km");
        Uri uri = Uri.parse(mList.get(i).getLogo());
        viewHoder.cinemaImage.setImageURI(uri);
        if(mList.get(i).getFollowCinema()==1){
            viewHoder.attentionImage.setChecked(true);
        }else{
            viewHoder.attentionImage.setChecked(false);
        }
        //点关注的方法,接口有问题,返回值都是0,无法判断是否关注
        /*viewHoder.attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( viewHoder.attentionImage.isChecked()){
                    if(mList.get(i).getFollowCinema()==1){
                        ticketCallBcak.onAttention(i,true,mList.get(i).getId()+"");
                    }
                }else{
                    if(mList.get(i).getFollowCinema()==0){
                        ticketCallBcak.onAttention(i,false,mList.get(i).getId()+"");
                    }
                }
            }
        });*/
        viewHoder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketCallBcak.onClick(mList.get(i).getId()+"",mList.get(i).getAddress(),mList.get(i).getName());
            }
        });
    }

    /*public void setCheckBox(int p,boolean b){
        if(b){
            mList.get(p).setFollowCinema(1);
        }else{
            mList.get(p).setFollowCinema(2);
        }
        notifyDataSetChanged();
    }*/
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
        @BindView(R.id.attentionImage)
        CheckBox attentionImage;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    BuyTicketCallBcak ticketCallBcak;

    public interface BuyTicketCallBcak {
        /*void onAttention(int p,boolean b,String id);*/
        void onClick(String id,String address,String cinemaname);
    }

    public void setTicketCallBcak(BuyTicketCallBcak callBcak) {
        ticketCallBcak = callBcak;
    }
}
