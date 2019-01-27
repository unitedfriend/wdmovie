package com.bw.movie.my.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.my.bean.FeedBackBean;
import com.bw.movie.util.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/27 14:52
 * @描述 意见反馈
 */
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.editfeedback)
    EditText editfeedback;
    @BindView(R.id.feedbacklayoyt)
    ConstraintLayout feedbacklayoyt;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.successlayout)
    ConstraintLayout successlayout;
    @BindView(R.id.returnbutton)
    ImageView returnbutton;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof FeedBackBean){
            FeedBackBean backBean = (FeedBackBean) object;
            if(backBean==null || !backBean.isSuccess()){
                ToastUtil.showToast(backBean.getMessage());
            }else{
                ToastUtil.showToast(backBean.getMessage());
                successlayout.setVisibility(View.VISIBLE);
                feedbacklayoyt.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }

    @OnClick({R.id.submit, R.id.returnbutton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit:
                String content = editfeedback.getText().toString();
                Map<String,String> map = new HashMap<>();
                try {
                    String encode = URLEncoder.encode(content, "UTF-8");
                    map.put("content",encode);
                    doNetWorkPostRequest(Apis.URL_RECORD_FEED_BACK_POST,map,FeedBackBean.class);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.returnbutton:
                finish();
                break;
                default:
                    break;
        }
    }
}
