package com.fieyu.netty.dto;

public class ConsumerType {
    //链接服务并登录
    public static final String LINK = "0";
    //退出登录
    public static final String LINK_OUT = "1";
    //聊天
    public static final String USER_CONTENT = "2";
    //将该条信息发送给所有客户端
    public static final String USER_ALL_ME_CONTENT = "3";
    //将该条信息发送给自己
    public static final String USER_ME_CONTENT = "4";
    //将该信息发送给指定
    public static final String USER_TO_CONTENT = "5";
    //将信息发送给多个客户端
    public static final String USER_TO_All_CONTENT = "6";
    //登录冲突
    public static final String LOGIN_CONFLICT = "6";
}
