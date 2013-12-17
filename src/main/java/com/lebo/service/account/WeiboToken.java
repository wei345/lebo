package com.lebo.service.account;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: AM11:39
 */
public class WeiboToken extends AbstractOAuthToken {

    public static final String PROVIDER = "weibo";

    private String token;

    public WeiboToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getProvider() {
        return PROVIDER;
    }

    @Override
    public String toString() {
        return "WeiboToken{" +
                "token='" + token + '\'' +
                "} " + super.toString();
    }
}
