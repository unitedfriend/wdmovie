package com.bw.movie.util;

import android.text.TextUtils;

/**
  * @作者 GXY
  * @创建日期 2019/1/24 14:42
  * @描述 非空判断工具类
  *
  */
public class EmptyUtil {
    //非空判断
    public static boolean isNull(String name,String password){
        return !TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password);
    }
    //注册非空判断
    public static boolean loginNull(String nick,String sex,String date,String phone,String email,String pwd){
        return !TextUtils.isEmpty(nick)&&!TextUtils.isEmpty(sex)&&!TextUtils.isEmpty(date)&&!TextUtils.isEmpty(phone)
                &&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pwd);
    }
}
