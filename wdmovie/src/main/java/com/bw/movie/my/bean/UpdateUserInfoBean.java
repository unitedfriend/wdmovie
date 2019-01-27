package com.bw.movie.my.bean;
/**
  * @作者 GXY
  * @创建日期 2019/1/27 11:33
  * @描述 修改用户信息bean
  *
  */
public class UpdateUserInfoBean {

    private ResultBean result;
    private String message;
    private String status;
    private final String SUCCESS_STATUS = "0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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

    public static class ResultBean {

        private int id;
        private String nickName;
        private int sex;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
