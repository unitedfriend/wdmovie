package com.bw.movie.home.bean;

import java.util.List;

public class AllBean {
    List<HotBean>hotList;
    List<ShowingBean>showingList;
    List<ShowBean>showList;

    public AllBean(List<HotBean> hotList, List<ShowingBean> showingList, List<ShowBean> showList) {
        this.hotList = hotList;
        this.showingList = showingList;
        this.showList = showList;
    }

    public List<HotBean> getHotList() {
        return hotList;
    }

    public void setHotList(List<HotBean> hotList) {
        this.hotList = hotList;
    }

    public List<ShowingBean> getShowingList() {
        return showingList;
    }

    public void setShowingList(List<ShowingBean> showingList) {
        this.showingList = showingList;
    }

    public List<ShowBean> getShowList() {
        return showList;
    }

    public void setShowList(List<ShowBean> showList) {
        this.showList = showList;
    }
}
