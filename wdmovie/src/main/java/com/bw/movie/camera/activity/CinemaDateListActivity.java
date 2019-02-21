package com.bw.movie.camera.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.BaseActivity;
import com.bw.movie.api.Apis;
import com.bw.movie.camera.adaper.CineamaMovieAdaper;
import com.bw.movie.camera.adaper.FindMovieListAdaper;
import com.bw.movie.camera.bean.CinemaMovieListBean;
import com.bw.movie.camera.bean.FindCinemaInfoBean;
import com.bw.movie.camera.bean.FindMovieListByCinemaIdBean;
import com.bw.movie.camera.bean.RecommendBean;
import com.bw.movie.home.adapter.FindAllCinemaCommentAdapter;
import com.bw.movie.home.bean.CommentPraiseBean;
import com.bw.movie.home.bean.FindAllCinemaComment;
import com.bw.movie.login.activity.LoginActivity;
import com.bw.movie.seat.activity.SeatActivity;
import com.bw.movie.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

/**
 * @作者 GXY
 * @创建日期 2019/1/29 16:58
 * @描述 影院排期列表Activity
 */
public class CinemaDateListActivity extends BaseActivity {
    @BindView(R.id.cinema_logo)
    SimpleDraweeView cinemaLogo;
    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.cinema_navigation)
    ImageView cinemaNavigation;
    @BindView(R.id.cinema_film)
    RecyclerCoverFlow cinemaFilm;
    @BindView(R.id.cinema_film_scheduling)
    RecyclerView cinemaFilmScheduling;
    @BindView(R.id.cinema_film_return)
    ImageView cinemaFilmReturn;
    private int cameraId;
    private FindMovieListAdaper movieListAdaper;
    private CineamaMovieAdaper movieAdaper;
    private List<FindMovieListByCinemaIdBean.ResultBean> result;
    private int i;
    private int movieId;
    private String movieName;
    private String name;
    private String address;
    private View popupView;
    private int page;
    private FindAllCinemaCommentAdapter adapter;
    private int d;
    private XRecyclerView xRecyclerView;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        //影院id
        cameraId = intent.getIntExtra("id", 0);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET, cameraId), FindCinemaInfoBean.class);
        doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_LIST_BY_CINEMAID_GET, cameraId), FindMovieListByCinemaIdBean.class);
        cinemaFilm.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {

            @Override
            public void onItemSelected(int position) {
                i = position;
                movieName = result.get(position).getName();
                movieId = result.get(position).getId();
                doNetWorkGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST, cameraId, movieId), CinemaMovieListBean.class);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        movieListAdaper = new FindMovieListAdaper(this);
        cinemaFilm.setAdapter(movieListAdaper);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        cinemaFilmScheduling.setLayoutManager(layoutManager);
        movieAdaper = new CineamaMovieAdaper(this);
        cinemaFilmScheduling.setAdapter(movieAdaper);
        movieAdaper.setCallBackCinemaMovie(new CineamaMovieAdaper.CallBackCinemaMovie() {
            @Override
            public void CallBack(CinemaMovieListBean.ResultBean resultBean) {
                Intent intent = new Intent(CinemaDateListActivity.this, SeatActivity.class);
                intent.putExtra("movieName", movieName);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("resultBean", resultBean);
                startActivity(intent);
            }
        });
        getView();
    }

    private void getView() {
        popupView = View.inflate(this, R.layout.popup_movie_schedule, null);
        final PopupWindow popuoWindow = getPopView(popupView);
        cinemaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popuoWindow.showAsDropDown(v);
            }
        });
        popupView.findViewById(R.id.hideButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuoWindow.dismiss();
            }
        });
        TextView datesButton = popupView.findViewById(R.id.datesButton);
        TextView commentButton = popupView.findViewById(R.id.commentButton);
        datesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.findViewById(R.id.datesLine).setVisibility(View.VISIBLE);
                popupView.findViewById(R.id.commentLine).setVisibility(View.INVISIBLE);
                popupView.findViewById(R.id.datesLayout).setVisibility(View.VISIBLE);
                popupView.findViewById(R.id.commentLayout).setVisibility(View.INVISIBLE);

            }
        });
        xRecyclerView = popupView.findViewById(R.id.xRecycle);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new FindAllCinemaCommentAdapter(this);
        xRecyclerView.setLayoutManager(layoutManager1);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter.setClick(new FindAllCinemaCommentAdapter.Click() {
            @Override
            public void onClick(int commentId, XRecyclerView film_comment_recyclerview) {

            }
        });
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                doNet(cameraId, page);
            }

            @Override
            public void onLoadMore() {
                doNet(cameraId, page);
            }
        });
        adapter.setLucky(new FindAllCinemaCommentAdapter.Lucky() {
            @Override
            public void onLucky(int commentId, int position) {
                d = position;
                Map<String, String> map = new HashMap<>();
                map.put("commentId", String.valueOf(commentId));
                doNetWorkPostRequest(Apis.URL_MOVIE_COMMENT_GREAT_POST, map, CommentPraiseBean.class);
            }
        });
        page = 1;
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNet(cameraId, page);
                popupView.findViewById(R.id.datesLine).setVisibility(View.INVISIBLE);
                popupView.findViewById(R.id.commentLine).setVisibility(View.VISIBLE);
                popupView.findViewById(R.id.datesLayout).setVisibility(View.INVISIBLE);
                popupView.findViewById(R.id.commentLayout).setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cinema_datelist;
    }


    private void doNet(int c, int p) {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET, c, p), FindAllCinemaComment.class);
    }

    /**
     * @author Tang
     * @time 2019/2/12  20:29
     * @describe popupwindo
     */
    private PopupWindow getPopView(View view) {
        PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        popupWindow.setFocusable(true);
        //设置是否可以触摸
        popupWindow.setTouchable(true);
        //关闭
        int color = getResources().getColor(R.color.popup_bg);
        popupWindow.setBackgroundDrawable(new ColorDrawable(color));
        return popupWindow;
    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof FindCinemaInfoBean) {
            FindCinemaInfoBean cinemaInfoBean = (FindCinemaInfoBean) object;
            if (cinemaInfoBean == null || !cinemaInfoBean.isSuccess()) {
                ToastUtil.showToast(cinemaInfoBean.getMessage());
            } else {
                FindCinemaInfoBean.ResultBean result = cinemaInfoBean.getResult();
                address = result.getAddress();
                String logo = result.getLogo();
                name = result.getName();
                cinemaName.setText(name);
                cinemaAddress.setText(address);
                cinemaLogo.setImageURI(Uri.parse(logo));
                TextView addressText = popupView.findViewById(R.id.addressText);
                addressText.setText(result.getAddress());
                TextView phoneText = popupView.findViewById(R.id.phoneText);
                phoneText.setText(result.getPhone());
                TextView pathText = popupView.findViewById(R.id.pathText);
                pathText.setText(result.getVehicleRoute());
            }
        } else if (object instanceof FindMovieListByCinemaIdBean) {
            FindMovieListByCinemaIdBean movieListByCinemaIdBean = (FindMovieListByCinemaIdBean) object;
            result = movieListByCinemaIdBean.getResult();

            if(movieListByCinemaIdBean.getMessage().equals("无数据")){
                ToastUtil.showToast(movieListByCinemaIdBean.getMessage());
                cinemaFilm.setVisibility(View.INVISIBLE);
                return;
            }
            cinemaFilm.smoothScrollToPosition(result.size() / 2);
            if (movieListByCinemaIdBean == null || !movieListByCinemaIdBean.isSuccess()) {
                ToastUtil.showToast(movieListByCinemaIdBean.getMessage());
            } else {
                movieListAdaper.setmResult(movieListByCinemaIdBean.getResult());
            }
        } else if (object instanceof CinemaMovieListBean) {
            CinemaMovieListBean movieListBean = (CinemaMovieListBean) object;
            if (movieListBean == null || !movieListBean.isSuccess()) {
                ToastUtil.showToast(movieListBean.getMessage());
            } else {
                movieAdaper.setmResult(movieListBean.getResult());
            }

        } else if (object instanceof FindAllCinemaComment) {
            FindAllCinemaComment object1 = (FindAllCinemaComment) object;
            if (page == 1) {
                adapter.setList(object1.getResult());
            } else {
                adapter.addList(object1.getResult());
            }
            ToastUtil.showToast(object1.getMessage());
            page++;
            xRecyclerView.loadMoreComplete();
            xRecyclerView.refreshComplete();
        } else if (object instanceof CommentPraiseBean) {
            CommentPraiseBean object1 = (CommentPraiseBean) object;
            ToastUtil.showToast(object1.getMessage());
            if (object1.getMessage().equals("点赞成功")) {
                adapter.addWhetherGreat(i);
            }else if(object1.getMessage().equals("请先登陆")){
                startActivity(new Intent(this,LoginActivity.class));
            }
        }
    }

    @Override
    protected void netFail(String s) {

    }

    @OnClick(R.id.cinema_film_return)
    public void onViewClicked() {
        finish();
    }
}
