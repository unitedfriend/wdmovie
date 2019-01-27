package com.bw.movie.my.bean;
/**
  * @作者 GXY
  * @创建日期 2019/1/27 11:32
  * @描述 修改密码的bean
  *
  */
public class UpdatePwdBean {

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
