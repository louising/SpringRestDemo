package com.zero.srd.vo;

public class DummyVO {
    private Integer userId;
    private String userName;
    private String loginName;
    private String email;
    private Boolean sex;

    public DummyVO() {
    }

    public DummyVO(String userName) {
        super();
        this.userName = userName;
    }
    
    public DummyVO(Integer userId, String userName, Boolean sex) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.sex = sex;
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

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("DummyVO[userId=%s, userName=%s, loginName=%s, sex=%b]", userId, userName, loginName, sex);
        //return "DummyVO [userId=" + userId + ", userName=" + userName + ", loginName=" + loginName + ", sex=" + sex + "]";
    }

}