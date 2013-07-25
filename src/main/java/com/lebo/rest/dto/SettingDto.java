package com.lebo.rest.dto;

import com.lebo.entity.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM12:38
 */
public class SettingDto {
    private List<Setting.Channel> channels = new ArrayList<Setting.Channel>();
    private String officialAccountId;
    private Integer jingHuaDays;
    private Integer reMenDays;
    private String guanZhu;
    private String reMen;
    private String jingHua;
    private String fenSiZuiDuo;
    private String zuiShouXiHuan;
    private String piaoFangZuiGao;

    public List<Setting.Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Setting.Channel> channels) {
        this.channels = channels;
    }

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public String getGuanZhu() {
        return guanZhu;
    }

    public void setGuanZhu(String guanZhu) {
        this.guanZhu = guanZhu;
    }

    public String getReMen() {
        return reMen;
    }

    public void setReMen(String reMen) {
        this.reMen = reMen;
    }

    public String getJingHua() {
        return jingHua;
    }

    public void setJingHua(String jingHua) {
        this.jingHua = jingHua;
    }

    public String getFenSiZuiDuo() {
        return fenSiZuiDuo;
    }

    public void setFenSiZuiDuo(String fenSiZuiDuo) {
        this.fenSiZuiDuo = fenSiZuiDuo;
    }

    public String getZuiShouXiHuan() {
        return zuiShouXiHuan;
    }

    public void setZuiShouXiHuan(String zuiShouXiHuan) {
        this.zuiShouXiHuan = zuiShouXiHuan;
    }

    public String getPiaoFangZuiGao() {
        return piaoFangZuiGao;
    }

    public void setPiaoFangZuiGao(String piaoFangZuiGao) {
        this.piaoFangZuiGao = piaoFangZuiGao;
    }

    public Integer getJingHuaDays() {
        return jingHuaDays;
    }

    public void setJingHuaDays(Integer jingHuaDays) {
        this.jingHuaDays = jingHuaDays;
    }

    public Integer getReMenDays() {
        return reMenDays;
    }

    public void setReMenDays(Integer reMenDays) {
        this.reMenDays = reMenDays;
    }
}
