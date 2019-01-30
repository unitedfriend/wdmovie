package com.bw.movie.home.bean;

import java.util.List;
/**
 *  @author Tang
 *  @time 2019/1/28  9:10
 *  @describe 首页多条目用,封装所有的数据
 */
public class AllBean {
    HotBean hot;
    ShowingBean showing;
   ShowBean show;

    public HotBean getHot() {
        return hot;
    }

    public void setHot(HotBean hot) {
        this.hot = hot;
    }

    public ShowingBean getShowing() {
        return showing;
    }

    public void setShowing(ShowingBean showing) {
        this.showing = showing;
    }

    public ShowBean getShow() {
        return show;
    }

    public void setShow(ShowBean show) {
        this.show = show;
    }
}
