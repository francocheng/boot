package com.gdczwlkj.common;

import java.io.Serializable;

/**
 * Created by franco on 2017/7/14.
 */
public class SecurityUser implements Serializable {

    private Integer userId;

    private String userName;

    private String sessionId;

    private Long refreshTokenExpireIn;

    public SecurityUser(Integer userId, String userName, String sessionId, Long accessTokenExpireIn) {
        this.userId = userId;
        this.userName = userName;
        this.sessionId = sessionId;
        this.refreshTokenExpireIn = accessTokenExpireIn;
    }
    public SecurityUser(){}

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getRefreshTokenExpireIn() {
        return refreshTokenExpireIn;
    }

    public void setRefreshTokenExpireIn(Long refreshTokenExpireIn) {
        this.refreshTokenExpireIn = refreshTokenExpireIn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
