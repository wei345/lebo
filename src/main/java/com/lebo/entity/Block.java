package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: Wei Liu
 * Date: 13-7-23
 * Time: PM3:46
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "block_unique", def = "{'userId': 1, 'blockedId': 1}", unique = true)
})
public class Block extends IdEntity {
    //a把b加入黑名单，a为userId，b为blockedId
    private String userId;
    private String blockedId;

    public Block(){}

    public Block(String userId, String blockedId) {
        this.userId = userId;
        this.blockedId = blockedId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(String blockedId) {
        this.blockedId = blockedId;
    }
}
