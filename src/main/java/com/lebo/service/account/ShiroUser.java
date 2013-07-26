package com.lebo.service.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.lebo.service.GridFsService;

import java.io.Serializable;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    public String id;
    public String screenName;
    public String name;
    public String profileImageUrl;
    public String provider;
    public String uid;
    public String token;

    public ShiroUser(String id, String screenName, String name, String profileImageUrl, String provider, String uid, String token) {
        this.id = id;
        this.screenName = screenName;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.uid = uid;
        this.token = token;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return screenName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(toString());
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShiroUser other = (ShiroUser) obj;
        if (!toString().equals(other.toString()))
            return false;
        return true;
    }

    public String getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return GridFsService.getContentUrl(profileImageUrl);
    }

    public String getProvider() {
        return provider;
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }
}