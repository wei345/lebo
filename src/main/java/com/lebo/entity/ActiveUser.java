package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * 日活跃用户统计.
 *
 * id: yyyy-MM-dd
 *
 * @author: Wei Liu
 * Date: 13-12-20
 * Time: PM10:25
 */
@Document(collection = "statistics.activeuser")
public class ActiveUser extends IdEntity{

    private Integer total;
    public static final String TOTAL_KEY = "total";

    private Integer days1;
    public static final String DAYS_1_KEY = "days1";

    private Integer days2;
    public static final String DAYS_2_KEY = "days2";

    private Integer days3;
    public static final String DAYS_3_KEY = "days3";

    private Integer days4;
    public static final String DAYS_4_KEY = "days4";

    private Integer days5;
    public static final String DAYS_5_KEY = "days5";

    private Integer days6To10;
    public static final String DAYS_6_TO_10_KEY = "days6To10";

    private Integer days11To20;
    public static final String DAYS_11_TO_20_KEY = "days11To20";

    private Integer days21To50;
    public static final String DAYS_21_TO_50_KEY = "days21To50";

    private Integer days51To100;
    public static final String DAYS_51_TO_100_KEY = "days51To100";

    private Integer days101To3000;
    public static final String DAYS_101_TO_3000_KEY = "days101To3000";

    private Integer daysGte3001;
    public static final String DAYS_GTE_3001_KEY = "daysGte3001";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getDays1() {
        return days1;
    }

    public void setDays1(Integer days1) {
        this.days1 = days1;
    }

    public Integer getDays2() {
        return days2;
    }

    public void setDays2(Integer days2) {
        this.days2 = days2;
    }

    public Integer getDays3() {
        return days3;
    }

    public void setDays3(Integer days3) {
        this.days3 = days3;
    }

    public Integer getDays4() {
        return days4;
    }

    public void setDays4(Integer days4) {
        this.days4 = days4;
    }

    public Integer getDays5() {
        return days5;
    }

    public void setDays5(Integer days5) {
        this.days5 = days5;
    }

    public Integer getDays6To10() {
        return days6To10;
    }

    public void setDays6To10(Integer days6To10) {
        this.days6To10 = days6To10;
    }

    public Integer getDays11To20() {
        return days11To20;
    }

    public void setDays11To20(Integer days11To20) {
        this.days11To20 = days11To20;
    }

    public Integer getDays21To50() {
        return days21To50;
    }

    public void setDays21To50(Integer days21To50) {
        this.days21To50 = days21To50;
    }

    public Integer getDays51To100() {
        return days51To100;
    }

    public void setDays51To100(Integer days51To100) {
        this.days51To100 = days51To100;
    }

    public Integer getDays101To3000() {
        return days101To3000;
    }

    public void setDays101To3000(Integer days101To3000) {
        this.days101To3000 = days101To3000;
    }

    public Integer getDaysGte3001() {
        return daysGte3001;
    }

    public void setDaysGte3001(Integer daysGte3001) {
        this.daysGte3001 = daysGte3001;
    }
}
