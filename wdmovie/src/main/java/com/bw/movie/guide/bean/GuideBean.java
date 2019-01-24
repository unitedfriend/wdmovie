package com.bw.movie.guide.bean;

public class GuideBean {
    private int image;
    private int title;
    private int title2;

    public GuideBean(int image, int title, int title2) {
        this.image = image;
        this.title = title;
        this.title2 = title2;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getTitle2() {
        return title2;
    }

    public void setTitle2(int title2) {
        this.title2 = title2;
    }
}
