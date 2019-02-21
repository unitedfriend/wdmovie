package com.bw.movie.camera.adaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.camera.bean.NearBean;
import com.bw.movie.camera.bean.RecommendBean;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 GXY
 * @创建日期 2019/1/29 14:01
 * @描述 附近影院适配器
 */

public class NeardAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NearBean.ResultBean> mResult;
    private Context mContext;
    private final int SUCCESS = 1;
    private final int CANCEL = 2;
    private ViewHolderNear holderNear;
    private SharedPreferences user;

    public NeardAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }

    //下拉刷新
    public void setmResult(List<NearBean.ResultBean> results) {
        mResult.clear();
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    //上拉加载
    public void addmResult(List<NearBean.ResultBean> results) {
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.near_cinema_item, viewGroup, false);
        ViewHolderNear holderNear = new ViewHolderNear(view);
        return holderNear;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        holderNear = (ViewHolderNear) viewHolder;
        holderNear.cinemaimage.setImageURI(Uri.parse(mResult.get(i).getLogo()));
        holderNear.cinemaname.setText(mResult.get(i).getName());
        holderNear.cinemaaddress.setText(mResult.get(i).getAddress());
        holderNear.distance.setText(mResult.get(i).getDistance() + "km");
        boolean followCinema = mResult.get(i).getFollowCinema();
        if (followCinema) {
            holderNear.attentionImageTrue.setVisibility(View.VISIBLE);
            holderNear.attentionImageFalse.setVisibility(View.INVISIBLE);
        } else {
            holderNear.attentionImageTrue.setVisibility(View.INVISIBLE);
            holderNear.attentionImageFalse.setVisibility(View.VISIBLE);
        }
        holderNear.attentionImageFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackNear.callTrueBeak(mResult.get(i).getId(), true);
                /*holderNear.attentionImageFalse.setVisibility(View.INVISIBLE);
                holderNear.attentionImageTrue.setVisibility(View.VISIBLE);*/
                mResult.get(i).setFollowCinema(1);
                notifyDataSetChanged();

            }
        });
        holderNear.attentionImageTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackNear.callFalseBeak(mResult.get(i).getId(), false);
                /*holderNear.attentionImageFalse.setVisibility(View.VISIBLE);
                holderNear.attentionImageTrue.setVisibility(View.INVISIBLE);*/
                mResult.get(i).setFollowCinema(2);
                notifyDataSetChanged();
            }
        });
        //关注

        /*holderNear.attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holderNear.attentionImage.isChecked();

                if (checked) {
                    callBackNear.callTrueBeak(mResult.get(i).getId(), true);
                } else {
                    callBackNear.callFalseBeak(mResult.get(i).getId(), false);
                }
            }

        });*/
       /* holderNear.attentionImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String userId = user.getString("userId", null);
                if(userId!=null) {
                    if (isChecked) {
                        callBackNear.callTrueBeak(mResult.get(i).getId(), true);
                    } else {
                        callBackNear.callFalseBeak(mResult.get(i).getId(), false);
                    }
                }else{
                    callBackNear.callLoging();
                }
            }
        });*/
        //点击跳转
        holderNear.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBackList != null) {
                    callBackList.callBack(mResult.get(i).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }

    class ViewHolderNear extends RecyclerView.ViewHolder {
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
        @BindView(R.id.attentionImage_true)
        ImageView attentionImageTrue;
        @BindView(R.id.attentionImage_false)
        ImageView attentionImageFalse;
        public ViewHolderNear(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (user == null) {
                user = mContext.getSharedPreferences("User", Context.MODE_PRIVATE);
            }
        }
    }

    //定义关注接口
    private CallBackNear callBackNear;

    public void setCallBackNear(CallBackNear callBackNear) {
        this.callBackNear = callBackNear;
    }

    public interface CallBackNear {
        void callTrueBeak(int id, boolean b);

        void callFalseBeak(int id, boolean b);

        void callLoging();
    }

    //根据影院ID查询该影院当前排期的电影列表接口
    private CallBackList callBackList;

    public void setCallBackList(CallBackList callBackList) {
        this.callBackList = callBackList;
    }

    public interface CallBackList {
        void callBack(int id);
    }
}
