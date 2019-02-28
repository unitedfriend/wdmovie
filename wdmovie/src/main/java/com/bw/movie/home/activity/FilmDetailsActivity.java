package com.bw.movie.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.home.adapter.FilmCommentAdapter;
import com.bw.movie.home.adapter.FilmPrevueAdapter;
import com.bw.movie.home.adapter.RevirwAdapter;
import com.bw.movie.home.adapter.StillsAdapter;
import com.bw.movie.home.bean.AttentionBean;
import com.bw.movie.home.bean.CommentPraiseBean;
import com.bw.movie.home.bean.FilmCommentBean;
import com.bw.movie.home.bean.FilmDetailsBean;
import com.bw.movie.home.bean.MovieComment;
import com.bw.movie.home.bean.ReplyBean;
import com.bw.movie.home.view.MySpannableTextView;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;


public class FilmDetailsActivity extends BaseActivity {
    @BindView(R.id.i1)
    ImageView i1;
    @BindView(R.id.attentionImage)
    CheckBox attentionImage;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.movieImage)
    SimpleDraweeView movieImage;
    @BindView(R.id.deteilsButtont)
    Button deteilsButtont;
    @BindView(R.id.prevueButton)
    Button prevueButton;
    @BindView(R.id.stagePhotoButton)
    Button stagePhotoButton;
    @BindView(R.id.filmReviewButton)
    Button filmReviewButton;
    @BindView(R.id.backButton)
    ImageView backButton;
    @BindView(R.id.buyTicketImage)
    Button buyTicketImage;
    @BindView(R.id.layout_bg)
    SimpleDraweeView layoutBg;

    private String id;
    private FilmDetailsBean filmDetailsBean;
    private View detailsView;
    private PopupWindow detailsPopupWindowsss;
private PopupWindow mPop;
    private PopupWindow nopicePop;
    private PopupWindow stillsPop;
    private PopupWindow reviewPop;
    private TextView class_name;
    private TextView director_name;
    private TextView data_name;
    private TextView address_name;
    private TextView plot_name_text;
    private SimpleDraweeView image_detail_three;
    private ImageView detail_down;
    private int movieId;
    private View review_view;
    private View stills_view;
    private View notice_view;
    private View detail_view;
    private StillsAdapter stillsAdapter;
    private int mpage;
    private final int COUNT = 5;
    private RevirwAdapter revirwAdapter;
    private XRecyclerView film_recyclerview;
    private int i;
    private XRecyclerView film_comment_recycler;
    private FilmCommentAdapter filmCommentAdapter;
    private boolean bool = true;
    private ImageButton write;
    private EditText edit_write;
    private TextView but_write;
    private LinearLayout linearLayout;
    private PopupWindow prevuePopup;
    private FilmPrevueAdapter filmPrevueAdapter;
    @Override
    protected void initData() {
        initAttentionImage();
    }

    /**
     * @author Tang
     * @time 2019/1/28  14:01
     * @describe 点赞或取消点赞
     */
    private void initAttentionImage() {
        attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (attentionImage.isChecked()) {
                    doNetWorkGetRequest(String.format(Apis.URL_FOLLOW_MOVIE_GET, id), AttentionBean.class);
                } else {
                    doNetWorkGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_MOVIE_GET, id), AttentionBean.class);
                }
            }
        });
    }


    /**
     * @author Tang
     * @time 2019/1/28  14:06
     * @describe 得到电影id, 并请求详情
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        //得到id
        mpage=1;
        id = getIntent().getStringExtra("id");
        movieId=Integer.parseInt(id);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET, id), FilmDetailsBean.class);

        getRevirwView();
        getStillsView();
        init();

        initPrevuePop();

    }


int b=0;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        b++;
        if(b==3){
         //   filmPrevueAdapter.setCanCle();
            JZVideoPlayerStandard.releaseAllVideos();
            b=0;
        }


    }

    private void initPrevuePop() {
        View prevueView = View.inflate(this,R.layout.popup_filmdetails_prevue,null);
        prevuePopup = new PopupWindow(prevueView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        prevuePopup.setFocusable(true);
        prevuePopup.setTouchable(true);
        int color = getResources().getColor(R.color.popup_bg);
        prevuePopup.setBackgroundDrawable(new ColorDrawable(color));
        prevuePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                 filmPrevueAdapter.setCanCle();
            }
        });
        ImageView backImage= prevueView.findViewById(R.id.dimisImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevuePopup.dismiss();
            }
        });
        XRecyclerView prevueXRecycle = prevueView.findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        prevueXRecycle.setLayoutManager(layoutManager);
        prevueXRecycle.setPullRefreshEnabled(false);
        prevueXRecycle.setLoadingMoreEnabled(false);
        filmPrevueAdapter = new FilmPrevueAdapter(this);
        prevueXRecycle.setAdapter(filmPrevueAdapter);
    }

    private void initDetailsPop() {
        detailsPopupWindowsss = new PopupWindow(detailsView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        detailsPopupWindowsss.setFocusable(true);
        detailsPopupWindowsss.setTouchable(true);
        int color = getResources().getColor(R.color.popup_bg);
        detailsPopupWindowsss.setBackgroundDrawable(new ColorDrawable(color));
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_filmdetails;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    /**
     * @author Tang
     * @time 2019/1/28  14:06
     * @describe 请求影片详情成功, 赋值
     * 请求关注成功提示
     */
    @Override
    protected void netSuccess(Object object) {
        if (object instanceof FilmDetailsBean) {
            filmDetailsBean = (FilmDetailsBean) object;
            initDetailsView();
            initDetailsPop();
            FilmDetailsBean.ResultBean result = filmDetailsBean.getResult();
            Uri uri = Uri.parse(result.getImageUrl());
            layoutBg.setImageURI(uri);
            attentionImage.setChecked(result.isFollowMovie());
            movieImage.setImageURI(uri);
            nameText.setText(result.getName());
            //传值到剧照适配器
            List<String> posterList = filmDetailsBean.getResult().getPosterList();
            stillsAdapter.setList(posterList);

            List<FilmDetailsBean.ResultBean.ShortFilmListBean> shortFilmList = filmDetailsBean.getResult().getShortFilmList();
            filmPrevueAdapter.setmList(shortFilmList);

        } else if (object instanceof AttentionBean) {
            AttentionBean object1 = (AttentionBean) object;
            ToastUtil.showToast(object1.getMessage());
            if(object1.getMessage().equals("请先登陆")){
                startActivity(new Intent(FilmDetailsActivity.this,LoginActivity.class));
                boolean checked = attentionImage.isChecked();
                attentionImage.setChecked(!checked);
            }
        }else if (object instanceof FilmCommentBean) {
            FilmCommentBean revirwBean = (FilmCommentBean) object;
                if (mpage == 1) {
                    revirwAdapter.setList(revirwBean.getResult());
                } else {
                    revirwAdapter.addList(revirwBean.getResult());
                }
                mpage++;
                film_recyclerview.loadMoreComplete();
                film_recyclerview.refreshComplete();
            ToastUtil.showToast(revirwBean.getMessage());
        } else if (object instanceof ReplyBean) {
            ReplyBean filmCommentBean = (ReplyBean) object;

                /* film_comment_recycler.setVisibility(View.VISIBLE);*/
                //TODO 传值到查看评论回复适配器
                if (mpage == 1) {
                    filmCommentAdapter.setList(filmCommentBean.getResult());
                } else {
                    filmCommentAdapter.addList(filmCommentBean.getResult());
                }
                mpage++;
                film_comment_recycler.loadMoreComplete();
                film_comment_recycler.refreshComplete();
        } else if (object instanceof MovieComment) {
            MovieComment commentBean = (MovieComment) object;
            //清空输入框
            edit_write.setText("");

                mpage = 1;
                init();

        }else if(object instanceof CommentPraiseBean){
            CommentPraiseBean object1 = (CommentPraiseBean) object;
            ToastUtil.showToast(object1.getMessage());
            if(object1.getMessage().equals("点赞成功")){
                revirwAdapter.addWhetherGreat(i);
            }else if(object1.getMessage().equals("请先登陆")){
                startActivity(new Intent(this,LoginActivity.class));
            }


        }

    }

    private void initDetailsView() {
        detailsView = View.inflate(FilmDetailsActivity.this, R.layout.popup_filmdetails_details, null);
        FilmDetailsBean.ResultBean result = filmDetailsBean.getResult();
        TextView typeText =detailsView.findViewById(R.id.typeText);
        TextView directorText =detailsView.findViewById(R.id.directorText);
        TextView timeText =detailsView.findViewById(R.id.timeText);
        TextView addressText =detailsView.findViewById(R.id.addressText);
        TextView actorList =detailsView.findViewById(R.id.actorList);
        ImageView dimensImage= detailsView.findViewById(R.id.dimensImage);
        //图片
        MySpannableTextView synopsisText =detailsView.findViewById(R.id.synopsisText);
        SimpleDraweeView movieImage =detailsView.findViewById(R.id.movieImage);
        Uri uri =Uri.parse(result.getImageUrl());
        movieImage.setImageURI(uri);
        typeText.setText("类型: "+result.getMovieTypes());
        directorText.setText("导演: "+result.getDirector());
        timeText.setText("时长: "+result.getDuration());
        addressText.setText("产地: "+result.getPlaceOrigin());
        synopsisText.setText(result.getSummary());
        actorList.setText(result.getStarring());

        dimensImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsPopupWindowsss.dismiss();
            }
        });
    }

    @Override
    protected void netFail(String s) {
        String s1 = s;
    }


    @OnClick({R.id.deteilsButtont, R.id.prevueButton, R.id.stagePhotoButton, R.id.filmReviewButton, R.id.backButton, R.id.buyTicketImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.deteilsButtont:
                detailsPopupWindowsss.showAsDropDown(nameText, 0, 0 );
                break;
            case R.id.prevueButton:
                prevuePopup.showAsDropDown(nameText, 0, -75);
                break;
            case R.id.stagePhotoButton:
                stillsPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
              //  stillsPop.update();
                break;
            case R.id.filmReviewButton:
                reviewPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
              //  reviewPop.update();
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.buyTicketImage:
                //电影的名字
                String s = nameText.getText().toString();
                //电影的ID传过去,在电影排期页面使用

                startActivity(new Intent(FilmDetailsActivity.this,BuyTicketActivity.class).putExtra("name",s).putExtra("movieId",id));
                break;
        }
    }





    /**
     * 评论布局
     *
     * @author Administrator
     * @time 2019/1/27 0027 11:45
     */
    private void getRevirwView() {
        review_view = View.inflate(this, R.layout.film_pop_review_view, null);
        detail_down = review_view.findViewById(R.id.film_down);
        film_recyclerview = review_view.findViewById(R.id.film_recyclerview);
        write = review_view.findViewById(R.id.write);
        linearLayout = review_view.findViewById(R.id.layout_write);
        edit_write = review_view.findViewById(R.id.edit_write);
        but_write = review_view.findViewById(R.id.but_write);
        getReviewPopView(review_view);
        edit_write.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    but_write.setText("关闭");
                }else{
                    but_write.setText("发送");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reviewPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                writeComment = false;
                linearLayout.setVisibility(View.GONE);
                but_write.setVisibility(View.GONE);
                write.setVisibility(View.VISIBLE);
                edit_write.setText("");




            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        film_recyclerview.setLayoutManager(linearLayoutManager);
        film_recyclerview.setPullRefreshEnabled(true);
        film_recyclerview.setLoadingMoreEnabled(true);
        film_recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage = 1;
                init();
            }

            @Override
            public void onLoadMore() {
                init();
            }
        });
        //TODO 创建适配器
        revirwAdapter = new RevirwAdapter(this);
        film_recyclerview.setAdapter(revirwAdapter);
        //点赞
        revirwAdapter.setLucky(new RevirwAdapter.Lucky() {
            @Override
            public void onLucky(int commentId, int position) {
                i = position;
                Map<String, String> map = new HashMap<>();
                map.put("commentId", String.valueOf(commentId));
                doNetWorkPostRequest(Apis.URL_MOVIE_COMMENT_GREAT_POST,map,CommentPraiseBean.class);
            }
        });
        //点击查看评论回复
        revirwAdapter.setClick(new RevirwAdapter.Click() {
            @Override
            public void onClick(int commentId, XRecyclerView film_comment_recyclerview) {
                film_comment_recycler = film_comment_recyclerview;
                if (bool) {
                    film_comment_recycler.setVisibility(View.VISIBLE);
                    getcommetView(commentId);
                    mpage = 1;
                    doNetWorkGetRequest(String.format(Apis.URL_FIND_COMMENT_REPLY_GET, commentId, mpage, COUNT),ReplyBean.class);
                } else {
                    film_comment_recycler.setVisibility(View.GONE);
                }
                bool = !bool;
            }
        });
        //评论影片
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                but_write.setVisibility(View.VISIBLE);
                write.setVisibility(View.GONE);
                writeComment=true;
            }
        });
        //评论
        but_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComment=false;
                linearLayout.setVisibility(View.GONE);
                but_write.setVisibility(View.GONE);
                write.setVisibility(View.VISIBLE);
                String trim = edit_write.getText().toString().trim();
                if (trim.equals("")) {
                    ToastUtil.showToast("评论为空");
                    //收回软件盘
                    InputMethodManager imm = ( InputMethodManager ) getSystemService( Context.INPUT_METHOD_SERVICE );
                    if ( imm.isActive( ) ) {
                        imm.hideSoftInputFromWindow( v.getWindowToken() , 0 );
                    }
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("movieId", String.valueOf(movieId));
                    map.put("commentContent", trim);
                    doNetWorkPostRequest(Apis.URL_MOVIE_COMMENT_POST, map,MovieComment.class);
                }
            }
        });

    }

    private void getcommetView(final int commentId) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        film_comment_recycler.setLayoutManager(linearLayoutManager);
        //创建评论回复适配器
        filmCommentAdapter = new FilmCommentAdapter(this);
        film_comment_recycler.setAdapter(filmCommentAdapter);
        film_comment_recycler.setPullRefreshEnabled(true);
        film_comment_recycler.setLoadingMoreEnabled(true);
        film_comment_recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage = 1;
                doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_COMMENT_GET, commentId, mpage, COUNT),FilmCommentBean.class);
            }

            @Override
            public void onLoadMore() {
                doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_COMMENT_GET, commentId, mpage, COUNT), FilmCommentBean.class);
            }
        });

    }
    /**
     * 剧照布局
     *
     * @author Administrator
     * @time 2019/1/27 0027 11:45
     */
    private void getStillsView() {
        stills_view = View.inflate(this, R.layout.file_pop_stills_view, null);
        detail_down = stills_view.findViewById(R.id.stills_down);
        getStillsPopView(stills_view);
        RecyclerView stills_recyclerview = stills_view.findViewById(R.id.stills_recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        stills_recyclerview.setLayoutManager(staggeredGridLayoutManager);
        //TODO 创建适配器
        stillsAdapter = new StillsAdapter(this);
        stills_recyclerview.setAdapter(stillsAdapter);

    }
    /**
     * 剧照
     *
     * @author Administrator
     * @time 2019/1/27 0027 11:12
     */
    private void getStillsPopView(View view) {
        stillsPop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        stillsPop.setFocusable(true);
        //设置是否可以触摸
        stillsPop.setTouchable(true);
        //关闭

        int color = getResources().getColor(R.color.popup_bg);
        stillsPop.setBackgroundDrawable(new ColorDrawable(color));
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stillsPop.dismiss();
            }
        });
    }

    /**
     * 剧照
     *
     * @author Administrator
     * @time 2019/1/27 0027 11:12
     */
    private void getReviewPopView(View view) {
        reviewPop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        reviewPop.setFocusable(true);
        //设置是否可以触摸
        reviewPop.setTouchable(true);
        int color = getResources().getColor(R.color.popup_bg);
        reviewPop.setBackgroundDrawable(new ColorDrawable(color));
        //关闭

        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewPop.dismiss();
            }
        });
    }
    /**
     * 请求电影评论接口
     *
     * @author Administrator
     * @time 2019/1/27 0027 13:14
     */
    private void init() {
        mpage = 1;
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_COMMENT_GET, movieId, mpage, COUNT), FilmCommentBean.class);
    }

boolean writeComment=false;


}
