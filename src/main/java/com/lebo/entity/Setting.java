package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用选项。
 *
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM4:18
 */
@Document(collection = "settings")
public class Setting extends IdEntity {
    private String officialAccountId;
    private String editorAccountId;//编辑id，编辑发的视频将显示在小编制作频道
    private String digestAccountId; //该账号专门用来指定精品视频，不允许被关注，该账号转发的视频会出现在精华页

    private Integer digestDays = 2;
    private Integer hotDays = 2;

    private String appStoreLeboUrl = "https://itunes.apple.com/cn/app/le-bo-6miao-shi-pin/id598266288?mt=8";
    private String leboAppAndroidDownloadUrl = "http://www.lebooo.com/lebo_1.1_20130802.apk";

    //红人榜 -> 粉丝最多
    private String hotuser_button1_backgroundColor = "#7E5CDA";
    private String hotuser_button1_imageKey = "images/hotuser/btn1.png";
    private String hotuser_button1_text = "粉丝最多";

    //红人榜 -> 最受喜欢
    private String hotuser_button2_backgroundColor = "#D67E89";
    private String hotuser_button2_imageKey = "images/hotuser/btn2.png";
    private String hotuser_button2_text = "最受喜欢";

    //红人榜 -> 导演排行
    private String hotuser_button3_backgroundColor = "#30B5F0";
    private String hotuser_button3_imageKey = "images/hotuser/btn3.png";
    private String hotuser_button3_text= "导演排行";

    //TODO 让新版客户端做banner功能，去掉在"九九重阳节"置顶视频
    private String jiuJiuChongYangJieTopPostId;

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public Integer getDigestDays() {
        return digestDays;
    }

    public void setDigestDays(Integer digestDays) {
        this.digestDays = digestDays;
    }

    public Integer getHotDays() {
        return hotDays;
    }

    public void setHotDays(Integer hotDays) {
        this.hotDays = hotDays;
    }

    public String getAppStoreLeboUrl() {
        return appStoreLeboUrl;
    }

    public void setAppStoreLeboUrl(String appStoreLeboUrl) {
        this.appStoreLeboUrl = appStoreLeboUrl;
    }

    public String getLeboAppAndroidDownloadUrl() {
        return leboAppAndroidDownloadUrl;
    }

    public void setLeboAppAndroidDownloadUrl(String leboAppAndroidDownloadUrl) {
        this.leboAppAndroidDownloadUrl = leboAppAndroidDownloadUrl;
    }

    public String getHotuser_button1_backgroundColor() {
        return hotuser_button1_backgroundColor;
    }

    public void setHotuser_button1_backgroundColor(String hotuser_button1_backgroundColor) {
        this.hotuser_button1_backgroundColor = hotuser_button1_backgroundColor;
    }

    public String getHotuser_button1_imageKey() {
        return hotuser_button1_imageKey;
    }

    public void setHotuser_button1_imageKey(String hotuser_button1_imageKey) {
        this.hotuser_button1_imageKey = hotuser_button1_imageKey;
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

    public String getHotuser_button2_imageKey() {
        return hotuser_button2_imageKey;
    }

    public void setHotuser_button2_imageKey(String hotuser_button2_imageKey) {
        this.hotuser_button2_imageKey = hotuser_button2_imageKey;
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

    public String getHotuser_button3_imageKey() {
        return hotuser_button3_imageKey;
    }

    public void setHotuser_button3_imageKey(String hotuser_button3_imageKey) {
        this.hotuser_button3_imageKey = hotuser_button3_imageKey;
    }

    public String getHotuser_button3_text() {
        return hotuser_button3_text;
    }

    public void setHotuser_button3_text(String hotuser_button3_text) {
        this.hotuser_button3_text = hotuser_button3_text;
    }

    public String getHotuser_button1_imageUrl(){
        return FileContentUrlUtils.getContentUrl(hotuser_button1_imageKey);
    }
    public String getHotuser_button2_imageUrl(){
        return FileContentUrlUtils.getContentUrl(hotuser_button2_imageKey);
    }
    public String getHotuser_button3_imageUrl(){
        return FileContentUrlUtils.getContentUrl(hotuser_button3_imageKey);
    }

    public String getEditorAccountId() {
        return editorAccountId;
    }

    public void setEditorAccountId(String editorAccountId) {
        this.editorAccountId = editorAccountId;
    }

    public String getJiuJiuChongYangJieTopPostId() {
        return jiuJiuChongYangJieTopPostId;
    }

    public void setJiuJiuChongYangJieTopPostId(String jiuJiuChongYangJieTopPostId) {
        this.jiuJiuChongYangJieTopPostId = jiuJiuChongYangJieTopPostId;
    }

    public String getDigestAccountId() {
        return digestAccountId;
    }

    public void setDigestAccountId(String digestAccountId) {
        this.digestAccountId = digestAccountId;
    }
}
