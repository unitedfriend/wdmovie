package com.bw.movie.my.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.my.adaper.SystemMessageAdapter;
import com.bw.movie.my.bean.PushTokenBean;
import com.bw.movie.my.bean.SystemMessageBean;
import com.bw.movie.my.bean.UnreadBean;
import com.bw.movie.my.bean.UnreadChangeBean;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private int count = 15;

    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_ALL_SYS_MSG_LIST, page, count), SystemMessageBean.class);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        page = 1;
        doNetWorkGetRequest(Apis.URL_FIND_UNREAD_MESSAGE_COUNT_GET, UnreadBean.class);
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
                page = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        adapter.setBack(new SystemMessageAdapter.CallBack() {
            @Override
            public void callback(SystemMessageBean.ResultBean resultBean, final int n) {
                final int id = resultBean.getId();
                String content = resultBean.getContent();
                String pushTime = resultBean.getPushTime();
                String title = resultBean.getTitle();
                final AlertDialog.Builder builder = new AlertDialog.Builder(SystemMessageActivity.this);
                View view = View.inflate(SystemMessageActivity.this, R.layout.systemmessage_alertdialog, null);
                builder.setView(view);
                TextView messageName = view.findViewById(R.id.messageName);
                TextView messageContent = view.findViewById(R.id.messageContent);
                TextView timeText = view.findViewById(R.id.timeText);
                messageName.setText(title);
                messageContent.setText(content);
                String time = stampToDate(pushTime);
                timeText.setText(time);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        postion = n;
                        doNetWorkGetRequest(String.format(Apis.URL_CHABGE_SYS_MSG_STATUS_GET, id), UnreadChangeBean.class);
                    }
                });
                builder.show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_message;
    }

    /**
     * @作者 GXY
     * @创建日期 2019/2/20 14:07
     * @描述 查看详细信息
     */
    public void setDetail() {

    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof SystemMessageBean) {
            SystemMessageBean object1 = (SystemMessageBean) object;
            if (page == 1) {
                adapter.setmList(object1.getResult());
            } else {
                adapter.addmList(object1.getResult());
            }
            page++;
            xRecycle.refreshComplete();
            xRecycle.loadMoreComplete();
        } else if (object instanceof UnreadBean) {
            UnreadBean object1 = (UnreadBean) object;
            unreadNum.setText("(" + object1.getCount() + "条未读");
        } else if (object instanceof UnreadChangeBean) {
            UnreadChangeBean object1 = (UnreadChangeBean) object;
            boolean b = object1.getMessage().equals("状态改变成功");
            if (b) {
                doNetWorkGetRequest(Apis.URL_FIND_UNREAD_MESSAGE_COUNT_GET, UnreadBean.class);
                adapter.setChange(postion);
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }


}
