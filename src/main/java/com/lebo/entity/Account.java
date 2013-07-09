package com.lebo.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 账号。存储多数情况用不到的或隐私的用户信息。
 *
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM6:26
 */
@Document
public class Account extends IdEntity {
    // 性别 -1 未知，0 女，1 男
    private int gender = -1;
    @Indexed(unique = true)
    private String email;
    private String userId;
    private Date createdAt;
    private Date lastSignInAt;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastSignInAt() {
        return lastSignInAt;
    }

    public void setLastSignInAt(Date lastSignInAt) {
        this.lastSignInAt = lastSignInAt;
    }
}
