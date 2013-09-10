package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户间关系，好友、粉丝。
 *
 * @author: Wei Liu
 * Date: 13-8-8
 * Time: PM2:06
 */
@Document(collection = "friendships")
@CompoundIndexes({
        @CompoundIndex(name = "a_1_b_1_unique", def = "{a : 1, b : 1}", unique = true),
        @CompoundIndex(name = "a_1_bFa_1", def = "{a : 1, bFa : 1}"),
        @CompoundIndex(name = "b_1_bFa_1", def = "{b : 1, bFa : 1}"),
        @CompoundIndex(name = "a_1_aFb_1_bFa_1", def = "{a : 1, aFb : 1, bFa : 1}"),
        @CompoundIndex(name = "b_1_aFb_1_bFa_1", def = "{b : 1, aFb : 1, bFa : 1}")
})
public class Friendship extends IdEntity {
    // 一个用户id，a < b
    private String a;
    public static final String A_KEY = "a";
    //另一个用户id
    private String b;
    public static final String B_KEY = "b";
    //a 关注(following) b, 也称b是a的好友
    private Boolean aFb;
    public static final String AFB_KEY = "aFb";
    //b 关注(following) a, 也称a是b的好友
    private Boolean bFa;
    public static final String BFA_KEY = "bFa";

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public Boolean getaFb() {
        return aFb;
    }

    public void setaFb(Boolean aFb) {
        this.aFb = aFb;
    }

    public Boolean getbFa() {
        return bFa;
    }

    public void setbFa(Boolean bFa) {
        this.bFa = bFa;
    }
}
