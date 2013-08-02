package com.lebo.service;

import com.lebo.entity.Following;
import com.lebo.event.AfterCreatFollowingEvent;
import com.lebo.event.AfterDestroyFollowingEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private AccountService accountService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ApplicationEventBus eventBus;

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
            if (blockService.isBlocking(followingId, userId)) {
                throw new ServiceException(ErrorDto.CAN_NOT_FOLLOW_BECAUSE_BLOCKED);
            }

            if (blockService.isBlocking(userId, followingId)) {
                throw new ServiceException(ErrorDto.CAN_NOT_FOLLOW_BECAUSE_BLOCKING);
            }

            //关注
            Following following = new Following(userId, followingId);
            followingDao.save(following);
            throwOnMongoError();
            eventBus.post(new AfterCreatFollowingEvent(following.getUserId(), following.getFollowingId()));
        } else {
            throw new ServiceException(String.format("%s or %s is not exists.", userId, followingId));
        }
    }

    public void unfollow(String userId, String followingId) {
        Assert.hasText(userId);
        Assert.hasText(followingId);

        Following following = followingDao.findByUserIdAndFollowingId(userId, followingId);
        if (following != null) {
            followingDao.delete(following);
            eventBus.post(new AfterDestroyFollowingEvent(following.getUserId(), following.getFollowingId()));
        }
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

    public List<Following> getFriends(String userId, Pageable pageable) {
        return followingDao.findByUserId(userId, pageable);
    }

    public List<Following> getFollowers(String userId, Pageable pageable) {
        return followingDao.findByFollowingId(userId, pageable);
    }
}
