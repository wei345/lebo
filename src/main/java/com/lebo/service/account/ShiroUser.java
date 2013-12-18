package com.lebo.service.account;

import com.google.common.base.Objects;
import org.apache.shiro.SecurityUtils;

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
    public String sessionId;
    private boolean guest;

    public ShiroUser(String id, String screenName, String name, String profileImageUrl, String provider) {
        this.id = id;
        this.screenName = screenName;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        sessionId = String.valueOf(SecurityUtils.getSubject().getSession().getId());
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
        return profileImageUrl;
    }

    public String getProvider() {
        return provider;
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }
}