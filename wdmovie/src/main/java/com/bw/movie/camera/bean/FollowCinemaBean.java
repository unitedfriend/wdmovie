package com.bw.movie.camera.bean;
/**
  * @作者 GXY
  * @创建日期 2019/1/29 15:00
  * @描述 关注影院
  *
  */
public class FollowCinemaBean {

    /**
     * message : 关注成功
     * status : 0000
     */

    private String message;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
