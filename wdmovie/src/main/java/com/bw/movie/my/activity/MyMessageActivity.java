package com.bw.movie.my.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.my.bean.LoadHeadPicBean;
import com.bw.movie.my.bean.MyMessageBean;
import com.bw.movie.my.bean.UpdateUserInfoBean;
import com.bw.movie.util.ImageFileUtil;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 GXY
 * @创建日期 2019/1/25 19:07
 * @描述 我的信息Activity
 */
public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.usericon_image)
    SimpleDraweeView usericonImage;
    @BindView(R.id.nickname)
    TextView userNickname;
    @BindView(R.id.sex)
    TextView userSex;
    @BindView(R.id.birth)
    TextView birth;
    @BindView(R.id.phonenumber)
    TextView phonenumber;
    @BindView(R.id.emailnum)
    TextView emailnum;
    @BindView(R.id.resetbutton)
    ImageButton resetbutton;
    @BindView(R.id.returnbutton)
    ImageButton returnbutton;
    @BindView(R.id.updatemessage)
    TextView updatemessage;
    private View camera;
    private View pick;
    private View cancel;
    private PopupWindow window;
    private String path = Environment.getExternalStorageDirectory()
            + "/image.png";
    private String file = Environment.getExternalStorageDirectory()
            + "/file.png";
    private final int REQUESTCODE_CAMERA = 100;
    private final int REQUESTCODE_PICK = 300;
    private final int REQUESTCODE_SUCCESS = 200;
    private AlertDialog dialog;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        Intent intent = getIntent();
        MyMessageBean.ResultBean result = (MyMessageBean.ResultBean) intent.getSerializableExtra("result");
        String headPic = result.getHeadPic();
        String nickName = result.getNickName();
        int sex = result.getSex();
        String phone = result.getPhone();
        usericonImage.setImageURI(Uri.parse(headPic));
        userNickname.setText(nickName);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = sDateFormat.format(Long.valueOf(result.getBirthday()));
        birth.setText(time);
        phonenumber.setText(phone);
        if (sex == 1) {
            userSex.setText("男");
        } else if (sex == 2) {
            userSex.setText("女");
        }
        setResult(REQUESTCODE_SUCCESS, intent);
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        getInitPopupwindow();
    }

    /**
     * 判断sd卡是否挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /***
     *初始化popupwindow
     */
    public void getInitPopupwindow() {
        //加载视图
        View view_p = View.inflate(this, R.layout.my_cmaera_popupwindow_item, null);
        camera = view_p.findViewById(R.id.text_camera);
        pick = view_p.findViewById(R.id.text_pick);
        cancel = view_p.findViewById(R.id.text_cancel);
        //创建PopupWindow
        window = new PopupWindow(view_p, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置焦点
        window.setFocusable(true);
        //设置背景
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置可触摸
        window.setTouchable(true);
        //点击打开相机
        //打开相机
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调取系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 存到sdcard中
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(path)));
                //执行
                startActivityForResult(intent, REQUESTCODE_CAMERA);
                window.dismiss();
            }
        });
        //点击打开相册
        //打开相册
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                //设置图片格式
                intent.setType("image/*");
                //执行
                startActivityForResult(intent, REQUESTCODE_PICK);
                window.dismiss();

            }
        });
        //点击取消
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    /**
     * 加载布局
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_message;
    }

    /**
     * 成功
     */
    @Override
    protected void netSuccess(Object object) {
        if (object instanceof LoadHeadPicBean) {
            LoadHeadPicBean headPicBean = (LoadHeadPicBean) object;
            ToastUtil.showToast(headPicBean.getMessage());
        } else if (object instanceof UpdateUserInfoBean) {
            UpdateUserInfoBean userInfoBean = (UpdateUserInfoBean) object;
            ToastUtil.showToast(userInfoBean.getMessage());
        }
    }

    /**
     * 失败
     */
    @Override
    protected void netFail(String s) {

    }

    @OnClick({R.id.usericon_image, R.id.updatemessage, R.id.resetbutton, R.id.returnbutton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.usericon_image:
                window.showAtLocation(View.inflate(MyMessageActivity.this, R.layout.activity_my_message, null),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.updatemessage:
                getUpdateNickName();
                break;
            case R.id.resetbutton:
                startActivity(new Intent(MyMessageActivity.this, UpdatePasswordActivity.class));
                //屏蔽activity跳转的默认转场效果
                overridePendingTransition(0, 0);
                break;
            case R.id.returnbutton:
                finish();
                break;
            default:
                break;
        }
    }

    public void getUpdateNickName() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyMessageActivity.this);
        View viewname = View.inflate(this, R.layout.alertdialog_nickname_item, null);
        builder.setView(viewname);
        Button update = viewname.findViewById(R.id.updata_btn);
        Button cencal = viewname.findViewById(R.id.cancal_btn);
        final EditText updateName = viewname.findViewById(R.id.updata_edix);
        final RadioButton man = viewname.findViewById(R.id.manbutton);
        final RadioButton woman = viewname.findViewById(R.id.womanbutton);
        final EditText updateEmail = viewname.findViewById(R.id.updata_edix_email);
        //修改
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateName.getText().toString().trim();
                String email = updateEmail.getText().toString().trim();
                if (name.equals("") || email.equals("")) {
                    ToastUtil.showToast("输入不能为空");
                } else {
                    Map<String, String> map = new HashMap<>();
                    boolean checked = man.isChecked();
                    map.put("sex", String.valueOf(2));
                    userSex.setText("女");
                    if (checked) {
                        map.put("sex", String.valueOf(1));
                        userSex.setText("男");
                    }
                    map.put("nickName", name);
                    map.put("email", email);
                    doNetWorkPostRequest(Apis.URL_MODIFY_USER_INFO_POST, map, UpdateUserInfoBean.class);
                    userNickname.setText(name);
                    emailnum.setText(email);
                }
                dialog.dismiss();
            }
        });
        //取消
        cencal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相机
        if (requestCode == REQUESTCODE_CAMERA && resultCode == RESULT_OK) {
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置宽高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUESTCODE_SUCCESS);
        }
        //相册
        if (requestCode == REQUESTCODE_PICK && resultCode == RESULT_OK) {
            //获取相册路径
            Uri uri = data.getData();
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(uri, "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置框高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUESTCODE_SUCCESS);
        }
        if (requestCode == REQUESTCODE_SUCCESS && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            try {
                ImageFileUtil.setBitmap(bitmap, file, 50);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToast(e.getMessage());
            }
            Map<String, String> map = new HashMap<>();
            map.put("image", file);
            doNetWorkPostimagesRequest(Apis.URL_UPLOAD_HEADPIC_POST, map, LoadHeadPicBean.class);
        }
    }
}
