package com.bw.movie.camera.bean;
/**
  * @作者 GXY
  * @创建日期 2019/1/29 15:01
  * @描述 取消关注影院
  *
  */
public class CancelFollowCineamBean {

    /**
     * message : 取消关注成功
     * status : 0000
     */

    private String message;
    private String status;
    private final String SUCCESS_STATUS = "0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
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
