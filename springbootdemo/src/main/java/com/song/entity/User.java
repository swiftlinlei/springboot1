package com.song.entity;

import javax.persistence.*;

/**
 * 员工类
 * Created by linlei on 2018/5/26
 */
@Entity
@Table(name = "hrmresource")
public class User {
    @Id
    private String id;

    private String lastname;

    private String loginid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }
}
