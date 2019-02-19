package com.bw.movie.camera.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.camera.bean.RecommendBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 GXY
 * @创建日期 2019/1/29 14:01
 * @描述 推荐影院适配器
 */import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecommendBean.ResultBean> mResult;
    private Context mContext;
    private final int SUCCESS=1;
    private final int CANCEL=2;
    private ViewHolderRecommend holderRecommend;

    public RecommendAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }

    //下拉刷新
    public void setmResult(List<RecommendBean.ResultBean> results) {
        mResult.clear();
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    //上拉加载
    public void addmResult(List<RecommendBean.ResultBean> results) {
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }
    public void setIsOk(boolean t){
        holderRecommend.attentionImage.setChecked(t);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_cinema_item, viewGroup, false);
        ViewHolderRecommend holderRecommend = new ViewHolderRecommend(view);
        return holderRecommend;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        holderRecommend = (ViewHolderRecommend) viewHolder;
        holderRecommend.cinemaimage.setImageURI(Uri.parse(mResult.get(i).getLogo()));
        holderRecommend.cinemaname.setText(mResult.get(i).getName());
        holderRecommend.cinemaaddress.setText(mResult.get(i).getAddress());
        holderRecommend.distance.setText(mResult.get(i).getDistance()+"km");
        boolean followCinema = mResult.get(i).getFollowCinema();
        if(followCinema){
            holderRecommend.attentionImage.setChecked(true);
        }else{
            holderRecommend.attentionImage.setChecked(false);
        }
        //关注

        holderRecommend.attentionImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(callBackRecommend!=null){
                        callBackRecommend.callTrueBeak(mResult.get(i).getId(),true,i);
                    }
                }else{
                    if(callBackRecommend!=null){
                        callBackRecommend.callFalseBeak(mResult.get(i).getId(),false,i);
                    }
                }
            }
        });
        //点击跳转
        holderRecommend.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackList!=null){
                    callBackList.callBack(mResult.get(i).getId());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderRecommend extends RecyclerView.ViewHolder {
        @BindView(R.id.cinemaimage)
        SimpleDraweeView cinemaimage;
        @BindView(R.id.cinemaname)
        TextView cinemaname;
        @BindView(R.id.cinemaaddress)
        TextView cinemaaddress;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.attentionImage)
        CheckBox attentionImage;
        @BindView(R.id.layout)
        ConstraintLayout layout;
        public ViewHolderRecommend(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义关注接口
    private CallBackRecommend callBackRecommend;
    public void setCallBackRecommend(CallBackRecommend callBackRecommend){
        this.callBackRecommend = callBackRecommend;
    }
    public interface CallBackRecommend{
        void callTrueBeak(int id, boolean b, int position);
        void callFalseBeak(int id, boolean b, int position);
    }
    //根据影院ID查询该影院当前排期的电影列表接口
    private CallBackList callBackList;
    public void setCallBackList(CallBackList callBackList){
        this.callBackList = callBackList;
    }
    public interface CallBackList{
        void callBack(int id);
    }
}
