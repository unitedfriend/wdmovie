package com.bw.movie.my.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.my.adaper.SystemMessageAdapter;
import com.bw.movie.my.bean.SystemMessageBean;
import com.bw.movie.my.bean.UnreadBean;
import com.bw.movie.my.bean.UnreadChangeBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 GXY
 * @创建日期 2019/2/14 14:43
 * @描述 系统消息
 */
public class SystemMessageActivity extends BaseActivity {
    @BindView(R.id.unreadNum)
    TextView unreadNum;
    @BindView(R.id.xRecycle)
    XRecyclerView xRecycle;
    @BindView(R.id.back)
    ImageView back;
    private int page;
    private SystemMessageAdapter adapter;
    private int postion;

    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_ALL_SYS_MSG_LIST,page),SystemMessageBean.class);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        page = 1;
        doNetWorkGetRequest(Apis.URL_FIND_UNREAD_MESSAGE_COUNT_GET,UnreadBean.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecycle.setLayoutManager(layoutManager);
        adapter = new SystemMessageAdapter(this);
        xRecycle.setAdapter(adapter);
        xRecycle.setLoadingMoreEnabled(true);
        xRecycle.setPullRefreshEnabled(true);
        xRecycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        adapter.setBack(new SystemMessageAdapter.CallBack() {
            @Override
            public void callback(int p,int pos) {
                postion=pos;
                doNetWorkGetRequest(String.format(Apis.URL_CHABGE_SYS_MSG_STATUS_GET,p),UnreadChangeBean.class);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_message;
    }

    @Override
    protected void netSuccess(Object object) {
        if(object instanceof SystemMessageBean){
            SystemMessageBean object1 = (SystemMessageBean) object;
            if(page==1){
                adapter.setmList(object1.getResult());
            }else{
                adapter.addmList(object1.getResult());
            }
            page++;
            xRecycle.refreshComplete();
            xRecycle.loadMoreComplete();
        }else if(object instanceof UnreadBean){
            UnreadBean object1 = (UnreadBean) object;
            unreadNum.setText("("+object1.getCount()+"条未读");
        }else if(object instanceof UnreadChangeBean){
            UnreadChangeBean object1 = (UnreadChangeBean) object;
            boolean b = object1.getMessage().equals("状态改变成功");
            if(b){
                doNetWorkGetRequest(Apis.URL_FIND_UNREAD_MESSAGE_COUNT_GET,UnreadBean.class);
                adapter.setChange(postion);
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }


}
