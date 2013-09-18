package com.lebo.rest.dto;

import java.util.Collections;
import java.util.List;

/**
 * 红人榜按钮设置 & 推荐用户.
 *
 * @author: Wei Liu
 * Date: 13-9-18
 * Time: PM1:30
 */
public class HotUserListDto {
    //红人榜 -> 粉丝最多
    private String hotuser_button1_backgroundColor;
    private String hotuser_button1_imageUrl;
    private String hotuser_button1_text;

    //红人榜 -> 最受喜欢
    private String hotuser_button2_backgroundColor;
    private String hotuser_button2_imageUrl;
    private String hotuser_button2_text;

    //红人榜 -> 导演排行
    private String hotuser_button3_backgroundColor;
    private String hotuser_button3_imageUrl;
    private String hotuser_button3_text;

    //推荐用户
    private List<UserDto> users = Collections.emptyList();

    public String getHotuser_button1_backgroundColor() {
        return hotuser_button1_backgroundColor;
    }

    public void setHotuser_button1_backgroundColor(String hotuser_button1_backgroundColor) {
        this.hotuser_button1_backgroundColor = hotuser_button1_backgroundColor;
    }

    public String getHotuser_button1_text() {
        return hotuser_button1_text;
    }

    public void setHotuser_button1_text(String hotuser_button1_text) {
        this.hotuser_button1_text = hotuser_button1_text;
    }

    public String getHotuser_button2_backgroundColor() {
        return hotuser_button2_backgroundColor;
    }

    public void setHotuser_button2_backgroundColor(String hotuser_button2_backgroundColor) {
        this.hotuser_button2_backgroundColor = hotuser_button2_backgroundColor;
    }

    public String getHotuser_button2_text() {
        return hotuser_button2_text;
    }

    public void setHotuser_button2_text(String hotuser_button2_text) {
        this.hotuser_button2_text = hotuser_button2_text;
    }

    public String getHotuser_button3_backgroundColor() {
        return hotuser_button3_backgroundColor;
    }

    public void setHotuser_button3_backgroundColor(String hotuser_button3_backgroundColor) {
        this.hotuser_button3_backgroundColor = hotuser_button3_backgroundColor;
    }

    public String getHotuser_button3_text() {
        return hotuser_button3_text;
    }

    public void setHotuser_button3_text(String hotuser_button3_text) {
        this.hotuser_button3_text = hotuser_button3_text;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public String getHotuser_button1_imageUrl() {
        return hotuser_button1_imageUrl;
    }

    public void setHotuser_button1_imageUrl(String hotuser_button1_imageUrl) {
        this.hotuser_button1_imageUrl = hotuser_button1_imageUrl;
    }

    public String getHotuser_button3_imageUrl() {
        return hotuser_button3_imageUrl;
    }

    public void setHotuser_button3_imageUrl(String hotuser_button3_imageUrl) {
        this.hotuser_button3_imageUrl = hotuser_button3_imageUrl;
    }

    public String getHotuser_button2_imageUrl() {
        return hotuser_button2_imageUrl;
    }

    public void setHotuser_button2_imageUrl(String hotuser_button2_imageUrl) {
        this.hotuser_button2_imageUrl = hotuser_button2_imageUrl;
    }
}
