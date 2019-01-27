package com.bw.movie.my.bean;

import java.io.Serializable;
/**
  * @作者 GXY
  * @创建日期 2019/1/27 11:32
  * @描述 我的信息bean
  *
  */
public class MyMessageBean implements Serializable {

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

    public static class ResultBean implements Serializable{

        private String birthday;
        private String headPic;
        private int id;
        private long lastLoginTime;
        private String nickName;
        private String phone;
        private int sex;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(long lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
