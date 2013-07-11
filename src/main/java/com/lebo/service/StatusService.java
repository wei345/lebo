package com.lebo.service;

import com.google.common.collect.Lists;
import com.lebo.entity.Following;
import com.lebo.entity.Post;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.PostDao;
import com.lebo.service.param.FileInfo;
import com.lebo.service.param.TimelineParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:32
 */
@Service
public class StatusService extends MongoService {

    @Autowired
    private FollowingDao followingDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private GridFsService gridFsService;

    public Post update(String userId, String text, List<FileInfo> fileInfos) throws IOException {
        List<String> fileIds = Lists.newArrayList();
        for (FileInfo fileInfo : fileInfos) {
            fileIds.add(gridFsService.save(fileInfo.getContent(), fileInfo.getFilename(), fileInfo.getMimeType()));
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setCreatedAt(new Date());
        post.setText(text);
        post.setFiles(fileIds);
        post = postDao.save(post);
        throwOnMongoError();

        for (String id : fileIds) {
            gridFsService.increaseReferrerCount(id);
        }
        return post;
    }

    public List<Post> userTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");

        if (param.canIgnoreIdCondition()) {
            return postDao.findByUserId(param.getUserId(), param).getContent();
        } else {
            return postDao.userTimeline(param.getUserId(), param.getMaxId(), param.getSinceId(), param).getContent();
        }
    }

    public List<Post> homeTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");
        List<Following> followingList = followingDao.findByUserId(param.getUserId());

        List<String> followingIdList = new ArrayList<String>(followingList.size() + 1);
        for (Following following : followingList) {
            followingIdList.add(following.getFollowingId());
        }

        followingIdList.add(param.getUserId());

        return postDao.homeTimeline(followingIdList, param.getMaxId(), param.getSinceId(), param).getContent();
    }
}
