package com.bw.movie.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.R;
import com.bw.movie.api.Apis;
import com.bw.movie.fragmnet.BaseFragment;
import com.bw.movie.home.activity.FilmDetailsActivity;
import com.bw.movie.home.adapter.MovieListHotAdapter;
import com.bw.movie.home.bean.AttentionBean;
import com.bw.movie.home.bean.HotBean;
import com.bw.movie.home.bean.RefreshBean;
import com.bw.movie.home.bean.RefreshHotBean;
import com.bw.movie.home.bean.ShowBean;
import com.bw.movie.home.bean.ShowingBean;
import com.bw.movie.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tang
 * @time 2019/1/28  9:16
 * @describe 电影列表的热门电影
 */
public class ListHotFragment extends BaseFragment {
    @BindView(R.id.hotXRecycleview)
    XRecyclerView hotXRecycleview;
    Unbinder unbinder;
    private MovieListHotAdapter hotAdapter;
    private int postion;

    @Override
    protected void initData() {
        doNetWorkGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET, page, COUNT), HotBean.class);
    }
    //默认加载10条数据
    private final int COUNT = 10;
    private int page;

    @Override
    protected void initView(View view) {
        page = 1;
        unbinder = ButterKnife.bind(this, view);
        hotAdapter = new MovieListHotAdapter(getActivity(), "hot");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hotXRecycleview.setLayoutManager(layoutManager);
        hotXRecycleview.setAdapter(hotAdapter);
        hotXRecycleview.setLoadingMoreEnabled(true);
        hotXRecycleview.setPullRefreshEnabled(true);
        hotXRecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        //点赞的回调,
        hotAdapter.setHotCallBack(new MovieListHotAdapter.MovieListHotCallBack() {
            @Override
            public void hotCallBack(String id, int p, boolean b) {
                postion = p;
                if (b) {
                    doNetWorkGetRequest(String.format(Apis.URL_FOLLOW_MOVIE_GET, id), AttentionBean.class);
                } else {
                    doNetWorkGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_MOVIE_GET, id), AttentionBean.class);
                }
                listHotCallBack.callBack();
            }

            @Override
            public void skipDetails(String id) {
                startActivity(new Intent(getActivity(),FilmDetailsActivity.class).putExtra("id",id));
            }
        });
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_list_hot;
    }

    @Override
    protected void netSuccess(Object object) {
        if (object instanceof HotBean) {
            HotBean object1 = (HotBean) object;
            if (page == 1) {
                hotAdapter.setmList(object1.getResult());
            } else {
                hotAdapter.addmList(object1.getResult());
            }
            page++;
            hotXRecycleview.refreshComplete();
            hotXRecycleview.loadMoreComplete();
        } else if (object instanceof AttentionBean) {
            AttentionBean object1 = (AttentionBean) object;
            if (object1.getMessage().equals("关注成功")) {
                hotAdapter.setAttentionScccess(postion);
            } else if (object1.getMessage().equals("取消关注成功")) {
                hotAdapter.setCancelAttention(postion);
            }
            ToastUtil.showToast(object1.getMessage());
            // doNetWorkGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET,page,COUNT),RefreshHotBean.class);
        }


    }

    @Override
    protected void netFail(String s) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    ListHotCallBack listHotCallBack;
    public interface ListHotCallBack{
        void callBack();
    }
    public void setListHotCallBack(ListHotCallBack callBack){
        listHotCallBack=callBack;
    }

    @Override
    public void onResume() {
        super.onResume();
        page=1;
        doNetWorkGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET, page, COUNT), HotBean.class);
    }
}
