package com.lebo.rest.dto;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author: Wei Liu
 * Date: 13-8-23
 * Time: PM5:36
 */
public class JsonMapperTest {

    @Test
    public void fromJson() throws IOException {
        //json数组映射为List
        List<StatusDto> dtos = JsonMapper.fromJson(getJsonString1(), List.class, StatusDto.class);

        assertTrue(dtos.size() == 4);
        assertEquals("涛涛_IT", dtos.get(0).getUser().getScreenName());
        assertEquals("http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png", dtos.get(0).getUser().getProfileImageUrl());
        System.out.println((System.currentTimeMillis() - dtos.get(0).getCreatedAt().getTime()) / 86400000 + "天 前");

        //json对象映射为dto
        StatusDto status = JsonMapper.fromJson(getJsonString2(), StatusDto.class);
        assertEquals("明丫丫是个爷们", status.getComments().get(0).getUser().getScreenName());
    }

    private String getJsonString1() {
        return "\n" +
                "[ {\n" +
                "  \"id\" : \"5211ed840cf2bd294d22e412\",\n" +
                "  \"user\" : {\n" +
                "    \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "    \"screenName\" : \"涛涛_IT\",\n" +
                "    \"description\" : \"签名\",\n" +
                "    \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "    \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "    \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "    \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "    \"following\" : null,\n" +
                "    \"followersCount\" : 2,\n" +
                "    \"friendsCount\" : 2,\n" +
                "    \"statusesCount\" : 6,\n" +
                "    \"favoritesCount\" : 4,\n" +
                "    \"beFavoritedCount\" : 8,\n" +
                "    \"viewCount\" : 242,\n" +
                "    \"digestCount\" : 3,\n" +
                "    \"hotBeFavoritedCount\" : null,\n" +
                "    \"weiboVerified\" : false,\n" +
                "    \"blocking\" : false,\n" +
                "    \"bilateral\" : null\n" +
                "  },\n" +
                "  \"createdAt\" : \"Mon Aug 19 18:03:48 +0800 2013\",\n" +
                "  \"text\" : \"\",\n" +
                "  \"files\" : [ {\n" +
                "    \"length\" : 671888,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5211ed830cf2bd294d22e410\"\n" +
                "  }, {\n" +
                "    \"length\" : 64332,\n" +
                "    \"contentType\" : \"image/jpeg\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5211ed840cf2bd294d22e411\"\n" +
                "  } ],\n" +
                "  \"video\" : {\n" +
                "    \"length\" : 671888,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5211ed830cf2bd294d22e410\"\n" +
                "  },\n" +
                "  \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/5211ed840cf2bd294d22e411\",\n" +
                "  \"source\" : \"iOS\",\n" +
                "  \"favorited\" : true,\n" +
                "  \"favoritesCount\" : 2,\n" +
                "  \"repostsCount\" : 0,\n" +
                "  \"reposted\" : false,\n" +
                "  \"commentsCount\" : 1,\n" +
                "  \"viewCount\" : 132,\n" +
                "  \"hotFavoritesCount\" : null,\n" +
                "  \"originStatus\" : null,\n" +
                "  \"comments\" : [ {\n" +
                "    \"id\" : \"5213318d0cf2c9ce05d16df3\",\n" +
                "    \"postId\" : \"5211ed840cf2bd294d22e412\",\n" +
                "    \"createdAt\" : \"Tue Aug 20 17:06:21 +0800 2013\",\n" +
                "    \"text\" : \"\",\n" +
                "    \"files\" : [ {\n" +
                "      \"length\" : 120523,\n" +
                "      \"contentType\" : \"video/mp4\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5213318d0cf2c9ce05d16df1\"\n" +
                "    }, {\n" +
                "      \"length\" : 25196,\n" +
                "      \"contentType\" : \"image/jpeg\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5213318d0cf2c9ce05d16df2\"\n" +
                "    } ],\n" +
                "    \"video\" : {\n" +
                "      \"length\" : 120523,\n" +
                "      \"contentType\" : \"video/mp4\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5213318d0cf2c9ce05d16df1\"\n" +
                "    },\n" +
                "    \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/5213318d0cf2c9ce05d16df2\",\n" +
                "    \"hasVideo\" : true,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ed070cf2bd294d22e40c\",\n" +
                "      \"screenName\" : \"丶敗镓灬叁爺\",\n" +
                "      \"description\" : \"goooood\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5215c64e0cf2bcbe08ec8dbc.png\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5215c64e0cf2bcbe08ec8dbd.png\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5215c64e0cf2bcbe08ec8dbe.png\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 18:01:43 +0800 2013\",\n" +
                "      \"following\" : false,\n" +
                "      \"followersCount\" : 1,\n" +
                "      \"friendsCount\" : 1,\n" +
                "      \"statusesCount\" : null,\n" +
                "      \"favoritesCount\" : 3,\n" +
                "      \"beFavoritedCount\" : 0,\n" +
                "      \"viewCount\" : 111,\n" +
                "      \"digestCount\" : 0,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : false\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  } ],\n" +
                "  \"userMentions\" : [ ],\n" +
                "  \"digest\" : false\n" +
                "}, {\n" +
                "  \"id\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "  \"user\" : {\n" +
                "    \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "    \"screenName\" : \"涛涛_IT\",\n" +
                "    \"description\" : \"签名\",\n" +
                "    \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "    \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "    \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "    \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "    \"following\" : null,\n" +
                "    \"followersCount\" : 2,\n" +
                "    \"friendsCount\" : 2,\n" +
                "    \"statusesCount\" : 6,\n" +
                "    \"favoritesCount\" : 4,\n" +
                "    \"beFavoritedCount\" : 8,\n" +
                "    \"viewCount\" : 242,\n" +
                "    \"digestCount\" : 3,\n" +
                "    \"hotBeFavoritedCount\" : null,\n" +
                "    \"weiboVerified\" : false,\n" +
                "    \"blocking\" : false,\n" +
                "    \"bilateral\" : null\n" +
                "  },\n" +
                "  \"createdAt\" : \"Wed Aug 21 15:29:57 +0800 2013\",\n" +
                "  \"text\" : \"#新人报到#test\",\n" +
                "  \"files\" : [ {\n" +
                "    \"length\" : 343898,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/52146c750cf2bcbe08ec88f4.mp4\"\n" +
                "  }, {\n" +
                "    \"length\" : 62987,\n" +
                "    \"contentType\" : \"image/jpeg\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/52146c760cf2bcbe08ec88f5.jpg\"\n" +
                "  } ],\n" +
                "  \"video\" : {\n" +
                "    \"length\" : 343898,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/52146c750cf2bcbe08ec88f4.mp4\"\n" +
                "  },\n" +
                "  \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/52146c760cf2bcbe08ec88f5.jpg\",\n" +
                "  \"source\" : \"iOS\",\n" +
                "  \"favorited\" : true,\n" +
                "  \"favoritesCount\" : 1,\n" +
                "  \"repostsCount\" : 2,\n" +
                "  \"reposted\" : false,\n" +
                "  \"commentsCount\" : 3,\n" +
                "  \"viewCount\" : 231,\n" +
                "  \"hotFavoritesCount\" : null,\n" +
                "  \"originStatus\" : null,\n" +
                "  \"comments\" : [ {\n" +
                "    \"id\" : \"5216da740cf2029d486b7c88\",\n" +
                "    \"postId\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "    \"createdAt\" : \"Fri Aug 23 11:43:48 +0800 2013\",\n" +
                "    \"text\" : \"1234\",\n" +
                "    \"files\" : [ ],\n" +
                "    \"video\" : null,\n" +
                "    \"videoFirstFrameUrl\" : null,\n" +
                "    \"hasVideo\" : false,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ebb90cf2bd294d22e3ff\",\n" +
                "      \"screenName\" : \"明丫丫是个爷们\",\n" +
                "      \"description\" : \"abc\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e402\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 17:56:09 +0800 2013\",\n" +
                "      \"following\" : true,\n" +
                "      \"followersCount\" : 10,\n" +
                "      \"friendsCount\" : 3,\n" +
                "      \"statusesCount\" : 3,\n" +
                "      \"favoritesCount\" : 0,\n" +
                "      \"beFavoritedCount\" : 0,\n" +
                "      \"viewCount\" : 2,\n" +
                "      \"digestCount\" : null,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : true\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  }, {\n" +
                "    \"id\" : \"521609ef0cf2e24a645eda3c\",\n" +
                "    \"postId\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "    \"createdAt\" : \"Thu Aug 22 20:54:07 +0800 2013\",\n" +
                "    \"text\" : \"文字评论\",\n" +
                "    \"files\" : [ ],\n" +
                "    \"video\" : null,\n" +
                "    \"videoFirstFrameUrl\" : null,\n" +
                "    \"hasVideo\" : false,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ebb90cf2bd294d22e3ff\",\n" +
                "      \"screenName\" : \"明丫丫是个爷们\",\n" +
                "      \"description\" : \"abc\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e402\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 17:56:09 +0800 2013\",\n" +
                "      \"following\" : true,\n" +
                "      \"followersCount\" : 10,\n" +
                "      \"friendsCount\" : 3,\n" +
                "      \"statusesCount\" : 3,\n" +
                "      \"favoritesCount\" : 0,\n" +
                "      \"beFavoritedCount\" : 0,\n" +
                "      \"viewCount\" : 2,\n" +
                "      \"digestCount\" : null,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : true\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  }, {\n" +
                "    \"id\" : \"5215c0e60cf2bcbe08ec8da0\",\n" +
                "    \"postId\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "    \"createdAt\" : \"Thu Aug 22 15:42:30 +0800 2013\",\n" +
                "    \"text\" : \"\",\n" +
                "    \"files\" : [ {\n" +
                "      \"length\" : 148313,\n" +
                "      \"contentType\" : \"video/mp4\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9e.mp4\"\n" +
                "    }, {\n" +
                "      \"length\" : 40332,\n" +
                "      \"contentType\" : \"image/jpeg\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9f.jpg\"\n" +
                "    } ],\n" +
                "    \"video\" : {\n" +
                "      \"length\" : 148313,\n" +
                "      \"contentType\" : \"video/mp4\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9e.mp4\"\n" +
                "    },\n" +
                "    \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9f.jpg\",\n" +
                "    \"hasVideo\" : true,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "      \"screenName\" : \"涛涛_IT\",\n" +
                "      \"description\" : \"签名\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "      \"following\" : null,\n" +
                "      \"followersCount\" : 2,\n" +
                "      \"friendsCount\" : 2,\n" +
                "      \"statusesCount\" : 6,\n" +
                "      \"favoritesCount\" : 4,\n" +
                "      \"beFavoritedCount\" : 8,\n" +
                "      \"viewCount\" : 242,\n" +
                "      \"digestCount\" : 3,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : null\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  } ],\n" +
                "  \"userMentions\" : [ ],\n" +
                "  \"digest\" : true\n" +
                "}, {\n" +
                "  \"id\" : \"5216ced30cf28385ca71ef45\",\n" +
                "  \"user\" : {\n" +
                "    \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "    \"screenName\" : \"涛涛_IT\",\n" +
                "    \"description\" : \"签名\",\n" +
                "    \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "    \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "    \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "    \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "    \"following\" : null,\n" +
                "    \"followersCount\" : 2,\n" +
                "    \"friendsCount\" : 2,\n" +
                "    \"statusesCount\" : 6,\n" +
                "    \"favoritesCount\" : 4,\n" +
                "    \"beFavoritedCount\" : 8,\n" +
                "    \"viewCount\" : 242,\n" +
                "    \"digestCount\" : 3,\n" +
                "    \"hotBeFavoritedCount\" : null,\n" +
                "    \"weiboVerified\" : false,\n" +
                "    \"blocking\" : false,\n" +
                "    \"bilateral\" : null\n" +
                "  },\n" +
                "  \"createdAt\" : \"Fri Aug 23 10:54:10 +0800 2013\",\n" +
                "  \"text\" : \"\",\n" +
                "  \"files\" : [ {\n" +
                "    \"length\" : 281152,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5216ced20cf28385ca71ef43.mp4\"\n" +
                "  }, {\n" +
                "    \"length\" : 48497,\n" +
                "    \"contentType\" : \"image/jpeg\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5216ced30cf28385ca71ef44.jpg\"\n" +
                "  } ],\n" +
                "  \"video\" : {\n" +
                "    \"length\" : 281152,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5216ced20cf28385ca71ef43.mp4\"\n" +
                "  },\n" +
                "  \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/5216ced30cf28385ca71ef44.jpg\",\n" +
                "  \"source\" : \"iOS\",\n" +
                "  \"favorited\" : true,\n" +
                "  \"favoritesCount\" : 1,\n" +
                "  \"repostsCount\" : 0,\n" +
                "  \"reposted\" : false,\n" +
                "  \"commentsCount\" : 0,\n" +
                "  \"viewCount\" : 93,\n" +
                "  \"hotFavoritesCount\" : null,\n" +
                "  \"originStatus\" : null,\n" +
                "  \"comments\" : [ ],\n" +
                "  \"userMentions\" : [ ],\n" +
                "  \"digest\" : false\n" +
                "}, {\n" +
                "  \"id\" : \"5216d0270cf28385ca71ef4e\",\n" +
                "  \"user\" : {\n" +
                "    \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "    \"screenName\" : \"涛涛_IT\",\n" +
                "    \"description\" : \"签名\",\n" +
                "    \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "    \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "    \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "    \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "    \"following\" : null,\n" +
                "    \"followersCount\" : 2,\n" +
                "    \"friendsCount\" : 2,\n" +
                "    \"statusesCount\" : 6,\n" +
                "    \"favoritesCount\" : 4,\n" +
                "    \"beFavoritedCount\" : 8,\n" +
                "    \"viewCount\" : 242,\n" +
                "    \"digestCount\" : 3,\n" +
                "    \"hotBeFavoritedCount\" : null,\n" +
                "    \"weiboVerified\" : false,\n" +
                "    \"blocking\" : false,\n" +
                "    \"bilateral\" : null\n" +
                "  },\n" +
                "  \"createdAt\" : \"Fri Aug 23 10:59:50 +0800 2013\",\n" +
                "  \"text\" : \"10：50\",\n" +
                "  \"files\" : [ {\n" +
                "    \"length\" : 449691,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5216d0260cf28385ca71ef4c.mp4\"\n" +
                "  }, {\n" +
                "    \"length\" : 49019,\n" +
                "    \"contentType\" : \"image/jpeg\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5216d0260cf28385ca71ef4d.jpg\"\n" +
                "  } ],\n" +
                "  \"video\" : {\n" +
                "    \"length\" : 449691,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/5216d0260cf28385ca71ef4c.mp4\"\n" +
                "  },\n" +
                "  \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/5216d0260cf28385ca71ef4d.jpg\",\n" +
                "  \"source\" : \"iOS\",\n" +
                "  \"favorited\" : true,\n" +
                "  \"favoritesCount\" : 1,\n" +
                "  \"repostsCount\" : 1,\n" +
                "  \"reposted\" : false,\n" +
                "  \"commentsCount\" : 0,\n" +
                "  \"viewCount\" : 135,\n" +
                "  \"hotFavoritesCount\" : null,\n" +
                "  \"originStatus\" : null,\n" +
                "  \"comments\" : [ ],\n" +
                "  \"userMentions\" : [ ],\n" +
                "  \"digest\" : true\n" +
                "} ]";
    }

    private String getJsonString2() {
        return "{\n" +
                "  \"id\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "  \"user\" : {\n" +
                "    \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "    \"screenName\" : \"涛涛_IT\",\n" +
                "    \"description\" : \"签名\",\n" +
                "    \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "    \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "    \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "    \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "    \"following\" : null,\n" +
                "    \"followersCount\" : 2,\n" +
                "    \"friendsCount\" : 2,\n" +
                "    \"statusesCount\" : 6,\n" +
                "    \"favoritesCount\" : 4,\n" +
                "    \"beFavoritedCount\" : 8,\n" +
                "    \"viewCount\" : 242,\n" +
                "    \"digestCount\" : 3,\n" +
                "    \"hotBeFavoritedCount\" : null,\n" +
                "    \"weiboVerified\" : false,\n" +
                "    \"blocking\" : false,\n" +
                "    \"bilateral\" : null\n" +
                "  },\n" +
                "  \"createdAt\" : \"Wed Aug 21 15:29:57 +0800 2013\",\n" +
                "  \"text\" : \"#新人报到#test\",\n" +
                "  \"files\" : [ {\n" +
                "    \"length\" : 343898,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/52146c750cf2bcbe08ec88f4.mp4\"\n" +
                "  }, {\n" +
                "    \"length\" : 62987,\n" +
                "    \"contentType\" : \"image/jpeg\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/52146c760cf2bcbe08ec88f5.jpg\"\n" +
                "  } ],\n" +
                "  \"video\" : {\n" +
                "    \"length\" : 343898,\n" +
                "    \"contentType\" : \"video/mp4\",\n" +
                "    \"contentUrl\" : \"http://file.dev.lebooo.com/52146c750cf2bcbe08ec88f4.mp4\"\n" +
                "  },\n" +
                "  \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/52146c760cf2bcbe08ec88f5.jpg\",\n" +
                "  \"source\" : \"iOS\",\n" +
                "  \"favorited\" : true,\n" +
                "  \"favoritesCount\" : 1,\n" +
                "  \"repostsCount\" : 2,\n" +
                "  \"reposted\" : false,\n" +
                "  \"commentsCount\" : 3,\n" +
                "  \"viewCount\" : 231,\n" +
                "  \"hotFavoritesCount\" : null,\n" +
                "  \"originStatus\" : null,\n" +
                "  \"comments\" : [ {\n" +
                "    \"id\" : \"5216da740cf2029d486b7c88\",\n" +
                "    \"postId\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "    \"createdAt\" : \"Fri Aug 23 11:43:48 +0800 2013\",\n" +
                "    \"text\" : \"1234\",\n" +
                "    \"files\" : [ ],\n" +
                "    \"video\" : null,\n" +
                "    \"videoFirstFrameUrl\" : null,\n" +
                "    \"hasVideo\" : false,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ebb90cf2bd294d22e3ff\",\n" +
                "      \"screenName\" : \"明丫丫是个爷们\",\n" +
                "      \"description\" : \"abc\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e402\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 17:56:09 +0800 2013\",\n" +
                "      \"following\" : true,\n" +
                "      \"followersCount\" : 10,\n" +
                "      \"friendsCount\" : 3,\n" +
                "      \"statusesCount\" : 3,\n" +
                "      \"favoritesCount\" : 0,\n" +
                "      \"beFavoritedCount\" : 0,\n" +
                "      \"viewCount\" : 2,\n" +
                "      \"digestCount\" : null,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : true\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  }, {\n" +
                "    \"id\" : \"521609ef0cf2e24a645eda3c\",\n" +
                "    \"postId\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "    \"createdAt\" : \"Thu Aug 22 20:54:07 +0800 2013\",\n" +
                "    \"text\" : \"文字评论\",\n" +
                "    \"files\" : [ ],\n" +
                "    \"video\" : null,\n" +
                "    \"videoFirstFrameUrl\" : null,\n" +
                "    \"hasVideo\" : false,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ebb90cf2bd294d22e3ff\",\n" +
                "      \"screenName\" : \"明丫丫是个爷们\",\n" +
                "      \"description\" : \"abc\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5211ebba0cf2bd294d22e402\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 17:56:09 +0800 2013\",\n" +
                "      \"following\" : true,\n" +
                "      \"followersCount\" : 10,\n" +
                "      \"friendsCount\" : 3,\n" +
                "      \"statusesCount\" : 3,\n" +
                "      \"favoritesCount\" : 0,\n" +
                "      \"beFavoritedCount\" : 0,\n" +
                "      \"viewCount\" : 2,\n" +
                "      \"digestCount\" : null,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : true\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  }, {\n" +
                "    \"id\" : \"5215c0e60cf2bcbe08ec8da0\",\n" +
                "    \"postId\" : \"52146c770cf2bcbe08ec88f6\",\n" +
                "    \"createdAt\" : \"Thu Aug 22 15:42:30 +0800 2013\",\n" +
                "    \"text\" : \"\",\n" +
                "    \"files\" : [ {\n" +
                "      \"length\" : 148313,\n" +
                "      \"contentType\" : \"video/mp4\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9e.mp4\"\n" +
                "    }, {\n" +
                "      \"length\" : 40332,\n" +
                "      \"contentType\" : \"image/jpeg\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9f.jpg\"\n" +
                "    } ],\n" +
                "    \"video\" : {\n" +
                "      \"length\" : 148313,\n" +
                "      \"contentType\" : \"video/mp4\",\n" +
                "      \"contentUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9e.mp4\"\n" +
                "    },\n" +
                "    \"videoFirstFrameUrl\" : \"http://file.dev.lebooo.com/5215c0e60cf2bcbe08ec8d9f.jpg\",\n" +
                "    \"hasVideo\" : true,\n" +
                "    \"user\" : {\n" +
                "      \"id\" : \"5211ec320cf2bd294d22e405\",\n" +
                "      \"screenName\" : \"涛涛_IT\",\n" +
                "      \"description\" : \"签名\",\n" +
                "      \"profileImageUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c24f.png\",\n" +
                "      \"profileImageBiggerUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c250.png\",\n" +
                "      \"profileImageOriginalUrl\" : \"http://file.dev.lebooo.com/5216d61a0cf233bcbdb9c251.png\",\n" +
                "      \"createdAt\" : \"Mon Aug 19 17:58:10 +0800 2013\",\n" +
                "      \"following\" : null,\n" +
                "      \"followersCount\" : 2,\n" +
                "      \"friendsCount\" : 2,\n" +
                "      \"statusesCount\" : 6,\n" +
                "      \"favoritesCount\" : 4,\n" +
                "      \"beFavoritedCount\" : 8,\n" +
                "      \"viewCount\" : 242,\n" +
                "      \"digestCount\" : 3,\n" +
                "      \"hotBeFavoritedCount\" : null,\n" +
                "      \"weiboVerified\" : false,\n" +
                "      \"blocking\" : false,\n" +
                "      \"bilateral\" : null\n" +
                "    },\n" +
                "    \"replyCommentId\" : null,\n" +
                "    \"replyCommentUser\" : null\n" +
                "  } ],\n" +
                "  \"userMentions\" : [ ],\n" +
                "  \"digest\" : true\n" +
                "}";
    }
}
