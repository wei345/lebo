package com.lebo.service;

import com.lebo.entity.Following;
import com.lebo.entity.User;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.UserDao;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM4:51
 */
@Service
public class FriendshipService extends AbstractMongoService {
    @Autowired
    private FollowingDao followingDao;

    @Autowired
    private UserDao userDao;

    /**
     * userId关注followingId。
     *
     * @throws ServiceException
     * @throws com.mongodb.MongoException
     */
    public void follow(String userId, String followingId) {
        Assert.hasText(userId);
        Assert.hasText(followingId);
        if (userId.equalsIgnoreCase(followingId)) {
            throw new ServiceException("You can not follow yourself.");
        }

        if (userDao.exists(userId) && userDao.exists(followingId)) {
            followingDao.save(new Following(userId, followingId));
            throwOnMongoError();
        } else {
            throw new ServiceException(String.format("%s or %s is not exists.", userId, followingId));
        }
    }

    /**
     * @throws ServiceException 当未关注时
     */
    public void unfollow(String userId, String followingId) {
        Assert.hasText(userId);
        Assert.hasText(followingId);

        Following following = followingDao.findByUserIdAndFollowingId(userId, followingId);
        if (following == null) {
            throw new ServiceException("Not following");
        }
        followingDao.delete(following);
    }

    public boolean isFollowing(String userId, String followingId) {
        return followingDao.findByUserIdAndFollowingId(userId, followingId) != null;
    }

    public int countFollowers(String userId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Following.FOLLOWING_ID_KEY).is(userId)), Following.class);
    }

    public int countFollowings(String userId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Following.USER_ID_KEY).is(userId)), Following.class);
    }

    public List<Following> getFollows(String userId, PaginationParam  param){
        return followingDao.findByUserId(userId, param);
    }

    public List<Following> getFans(String userId, PaginationParam  param){
        return followingDao.findByFollowingId(userId, param);
    }
}
