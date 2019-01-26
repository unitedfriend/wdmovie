package com.bw.movie.home.fragment;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.custom.ZoomInScrollView;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.my.activity.MyMessageActivity;
import com.bw.movie.my.bean.MyMessageBean;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @作者 GXY
 * @创建日期 2019/1/25 14:14
 * @描述 我的页面fragment
 */
public class MyFragment extends BaseFragment {
    @BindView(R.id.my_icon)
    SimpleDraweeView myIcon;
    @BindView(R.id.my_icon_name)
    TextView myIconName;
    @BindView(R.id.sign)
    Button sign;
    @BindView(R.id.my_message)
    LinearLayout myMessage;
    @BindView(R.id.my_concern)
    LinearLayout myConcern;
    @BindView(R.id.my_ticketinrecords)
    LinearLayout myTicketinrecords;
    @BindView(R.id.my_feedback)
    LinearLayout myFeedback;
    @BindView(R.id.my_latestversion)
    LinearLayout myLatestversion;
    @BindView(R.id.my_quit)
    LinearLayout myQuit;
    @BindView(R.id.zommLayout)
    ZoomInScrollView zommLayout;
    @BindView(R.id.system_message)
    ImageView systemMessage;
    Unbinder unbinder;
    private MyMessageBean.ResultBean result;
    private final int REQUESTCODE_NUM = 100;
    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        doNetWorkGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET,MyMessageBean.class);
    }
    /**
     * 初始化view
     */
    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);

    }

    /**
     * 加载布局
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my;
    }

    /**
     * 成功
     */
    @Override
    protected void netSuccess(Object object) {
        if(object instanceof MyMessageBean){
            MyMessageBean myMessageBean = (MyMessageBean) object;
            String headPic = myMessageBean.getResult().getHeadPic();
            String nickName = myMessageBean.getResult().getNickName();
            result = myMessageBean.getResult();
            if(myMessageBean == null || !myMessageBean.isSuccess()){
                ToastUtil.showToast(myMessageBean.getMessage());
            }else{
                myIcon.setImageURI(Uri.parse(headPic));
                myIconName.setText(nickName);
            }
        }
    }

    /**
     * 失败
     */
    @Override
    protected void netFail(String s) {
        ToastUtil.showToast(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.sign, R.id.my_message, R.id.my_concern, R.id.my_ticketinrecords, R.id.my_feedback, R.id.my_latestversion, R.id.my_quit, R.id.system_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign:
                break;
            case R.id.my_message:
                Intent intent = new Intent(getActivity(),MyMessageActivity.class);
                intent.putExtra("result",result);
                startActivityForResult(intent,REQUESTCODE_NUM);
                break;
            case R.id.my_concern:
                break;
            case R.id.my_ticketinrecords:
                break;
            case R.id.my_feedback:
                break;
            case R.id.my_latestversion:
                break;
            case R.id.my_quit:
                break;
            case R.id.system_message:
                break;
                default:
                    break;
        }
    }
}
