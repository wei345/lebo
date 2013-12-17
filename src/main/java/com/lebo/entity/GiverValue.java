package com.lebo.entity;

/**
 * 贡献者排名。
 * <p/>
 * <p>
 * 虚拟货币系统，对于任意用户U，有一个排名页面，所有给U送礼物的人，按礼物价值排名
 * </p>
 *
 * @author: Wei Liu
 * Date: 13-12-4
 * Time: PM6:42
 */
public class GiverValue {
    private String userId;
    private String giverId;
    private Integer giveValue;

    public GiverValue() {
    }

    public GiverValue(String userId, String giverId) {
        this.userId = userId;
        this.giverId = giverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGiverId() {
        return giverId;
    }

    public void setGiverId(String giverId) {
        this.giverId = giverId;
    }

    public Integer getGiveValue() {
        return giveValue;
    }

    public void setGiveValue(Integer giveValue) {
        this.giveValue = giveValue;
    }
}
