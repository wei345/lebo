package com.lebo.service.account;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: AM11:42
 */
public class QQToken extends AbstractOAuthToken {

    public static final String PROVIDER = "qq";

    private String token;

    private String uid;

    public QQToken(String token, String uid) {
        this.token = token;
        this.uid = uid;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "QQToken{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                "} " + super.toString();
    }

    @Override
    public String getProvider() {
        return PROVIDER;
    }
}
