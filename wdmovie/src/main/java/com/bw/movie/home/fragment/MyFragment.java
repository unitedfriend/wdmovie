package com.bw.movie.home.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.custom.ZoomInScrollView;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.my.activity.FeedBackActivity;
import com.bw.movie.my.activity.MyAttentionActivity;
import com.bw.movie.my.activity.MyMessageActivity;
import com.bw.movie.my.activity.MyTicketrecordActivity;
import com.bw.movie.my.bean.MyMessageBean;
import com.bw.movie.my.bean.NewVersionBean;
import com.bw.movie.my.bean.UserSignInBean;
import com.bw.movie.util.ActivityCollectorUtil;
import com.bw.movie.util.ToastUtil;
import com.bw.movie.util.VersionUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private final int RESULTCODE_NUM = 200;
    private String mFilePath;
    private boolean mIsUpdate;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        doNetWorkGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, MyMessageBean.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
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
        if (object instanceof MyMessageBean) {
            MyMessageBean myMessageBean = (MyMessageBean) object;
            String headPic = myMessageBean.getResult().getHeadPic();
            String nickName = myMessageBean.getResult().getNickName();
            result = myMessageBean.getResult();
            if (myMessageBean == null || !myMessageBean.isSuccess()) {
                ToastUtil.showToast(myMessageBean.getMessage());
            } else {
                myIcon.setImageURI(Uri.parse(headPic));
                myIconName.setText(nickName);
            }
        } else if (object instanceof UserSignInBean) {
            UserSignInBean signInBean = (UserSignInBean) object;
            if (signInBean == null || !signInBean.isSuccess()) {
                ToastUtil.showToast(signInBean.getMessage());
            } else {
                ToastUtil.showToast(signInBean.getMessage());
            }
        } else if (object instanceof NewVersionBean) {
            NewVersionBean versionBean = (NewVersionBean) object;
            if (versionBean == null || !versionBean.isSuccess()) {
                ToastUtil.showToast(versionBean.getMessage());
            } else {
                int flag = versionBean.getFlag();
                if(flag==1){
                    showAlertDialog(versionBean.getDownloadUrl());
                }else{
                    ToastUtil.showToast("已是最新版本");
                }
            }
        }
    }
    /**
     * 失败
     */
    @Override
    protected void netFail(String s) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.sign, R.id.my_message, R.id.my_concern, R.id.my_ticketinrecords, R.id.my_feedback, R.id.my_latestversion, R.id.my_quit, R.id.system_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //签到
            case R.id.sign:

                if(isLogin()) {
                    doNetWorkGetRequest(Apis.URL_USER_SIGN_IN_GET, UserSignInBean.class);
                }
                break;
            //我的信息
            case R.id.my_message:

                if(isLogin()) {
                    Intent intent = new Intent(getActivity(), MyMessageActivity.class);
                    intent.putExtra("result", result);
                    startActivityForResult(intent, REQUESTCODE_NUM);
                }
                break;
            //我的关注
            case R.id.my_concern:

                if(isLogin()) {
                    startActivity(new Intent(getActivity(), MyAttentionActivity.class));
                    getActivity().overridePendingTransition(0, 0);
                }
                break;
            //购票记录
            case R.id.my_ticketinrecords:

                if(isLogin()) {
                    startActivity(new Intent(getActivity(), MyTicketrecordActivity.class));
                    getActivity().overridePendingTransition(0, 0);
                }
                break;
            //意见反馈
            case R.id.my_feedback:

                if(isLogin()){
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                //屏蔽activity跳转的默认转场效果
                getActivity().overridePendingTransition(0, 0);
                }
                break;
            //最新版本
            case R.id.my_latestversion:
                doNetWorkGetRequest(Apis.URL_FIND_NEW_VERSION_GET, NewVersionBean.class);
                break;
            //退出登录
            case R.id.my_quit:
                ActivityCollectorUtil.finishAllActivity();
                break;
            case R.id.system_message:
                break;
            default:
                break;
        }
    }

    private boolean isLogin() {
        SharedPreferences user = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = user.getString("userId", null);
        if(userId==null){
            startActivity(new Intent(getActivity(),LoginActivity.class));
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_NUM && resultCode == RESULTCODE_NUM) {
            doNetWorkGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, MyMessageBean.class);
        }
    }
    /**
     * 显示AlertDialog
     * */
    private void showAlertDialog(final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("版本升级");
        builder.setMessage("软件更新");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDialog(downloadUrl);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    /**
     * 点击确定获取url下载
     * */
    private void startDialog(final String downloadUrl) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    startDownload(downloadUrl, progressDialog);
                    progressDialog.dismiss();
                } catch (Exception e) {

                }
            }
        }.start();
    }

    private void startDownload(String downloadUrl, ProgressDialog progressDialog) throws Exception {
        URL url = new URL(downloadUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(8000);
        progressDialog.setMax(conn.getContentLength());
        InputStream inputStream = conn.getInputStream();
        mFilePath = VersionUtil.getSaveFilePath(downloadUrl);
        File file = new File(mFilePath);
        writeFile(inputStream, file, progressDialog);
    }
    /**
     * 写入文件
     * */
    public void writeFile(InputStream inputStream, File file, ProgressDialog progressDialog) throws Exception {
//判断下载的文件是否已存在
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int length;
        int total = 0;
        while ((length = inputStream.read(b)) != -1) {
            fos.write(b, 0, length);
            total += length;
            progressDialog.setProgress(total);
        }
        inputStream.close();
        fos.close();
        progressDialog.dismiss();
        installApk(mFilePath);
    }

    private void installApk(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本大于等于7.0
            // "com.ansen.checkupdate.fileprovider"即是在清单文件中配置的authorities
            // 通过FileProvider创建一个content类型的Uri
            data = FileProvider.getUriForFile(getActivity(), "com.bw.movie.fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
