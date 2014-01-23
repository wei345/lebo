package com.lebo.service.account;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: AM11:41
 */
public class RenrenToken extends AbstractOAuthToken {

    public static String PROVIDER = "renren";

    private String token;

    public RenrenToken(String token) {
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RenrenToken{" +
                "token='" + token + '\'' +
                "} " + super.toString();
    }
}
