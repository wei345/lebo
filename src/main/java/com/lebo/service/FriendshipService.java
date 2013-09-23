package com.lebo.service;

import com.lebo.entity.Friendship;
import com.lebo.event.AfterFollowingCreatEvent;
import com.lebo.event.AfterFollowingDestroyEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.ErrorDto;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM4:51
 */
@Service
public class FriendshipService extends AbstractMongoService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ApplicationEventBus eventBus;
    /**
     * 允许关注的人数上限，太多可能会很慢。
     */
    private int maxFriendsCount = 2000;

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

            if (countFriends(userId) > maxFriendsCount) {
                throw new ServiceException(ErrorDto.CAN_NOT_FOLLOW_BECAUSE_TOO_MANY);
            }

            //保存关系
            Query query = new Query();
            Update update = new Update();
            if (userId.compareTo(followingId) < 0) {
                query.addCriteria(new Criteria(Friendship.A_KEY).is(userId));
                query.addCriteria(new Criteria(Friendship.B_KEY).is(followingId));
                update.set(Friendship.AFB_KEY, true);
            } else {
                query.addCriteria(new Criteria(Friendship.A_KEY).is(followingId));
                query.addCriteria(new Criteria(Friendship.B_KEY).is(userId));
                update.set(Friendship.BFA_KEY, true);
            }
            mongoTemplate.upsert(query, update, Friendship.class);
            throwOnMongoError();
            eventBus.post(new AfterFollowingCreatEvent(userId, followingId));
        } else {
            throw new ServiceException(String.format("%s or %s is not exists.", userId, followingId));
        }
    }

    //TODO 定时任务清理AFB_KEY和BFA_KEY都为false的数据，每天一次
    public void unfollow(String userId, String followingId) {
        Assert.hasText(userId);
        Assert.hasText(followingId);

        Query query = new Query();
        Update update = new Update();
        if (userId.compareTo(followingId) < 0) {
            query.addCriteria(new Criteria(Friendship.A_KEY).is(userId));
            query.addCriteria(new Criteria(Friendship.B_KEY).is(followingId));
            query.addCriteria(new Criteria(Friendship.AFB_KEY).is(true));
            update.set(Friendship.AFB_KEY, false);
        } else {
            query.addCriteria(new Criteria(Friendship.A_KEY).is(followingId));
            query.addCriteria(new Criteria(Friendship.B_KEY).is(userId));
            query.addCriteria(new Criteria(Friendship.BFA_KEY).is(true));
            update.set(Friendship.BFA_KEY, false);
        }
        WriteResult result = mongoTemplate.updateFirst(query, update, Friendship.class);

        if (result.getN() == 1) {
            eventBus.post(new AfterFollowingDestroyEvent(userId, followingId));
        }
    }

    public boolean isFollowing(String userId, String followingId) {
        Query query = new Query();
        if (userId.compareTo(followingId) < 0) {
            query.addCriteria(new Criteria(Friendship.A_KEY).is(userId));
            query.addCriteria(new Criteria(Friendship.B_KEY).is(followingId));
            query.addCriteria(new Criteria(Friendship.AFB_KEY).is(true));
        } else {
            query.addCriteria(new Criteria(Friendship.A_KEY).is(followingId));
            query.addCriteria(new Criteria(Friendship.B_KEY).is(userId));
            query.addCriteria(new Criteria(Friendship.BFA_KEY).is(true));
        }
        return mongoTemplate.count(query, Friendship.class) == 1;
    }

    public int countFollowers(String userId) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.BFA_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.AFB_KEY).is(true));
        Query query = new Query(orOperator(criterias));
        return (int) mongoTemplate.count(query, Friendship.class);
    }

    public int countFriends(String userId) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.AFB_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.BFA_KEY).is(true));
        Query query = new Query(orOperator(criterias));
        return (int) mongoTemplate.count(query, Friendship.class);
    }

    public List<String> getFriends(String userId, Pageable pageable) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.AFB_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.BFA_KEY).is(true));
        Query query = new Query(orOperator(criterias)).with(pageable);

        List<Friendship> friendships = mongoTemplate.find(query, Friendship.class);
        return getIds(userId, friendships);
    }

    /**
     * 查询双向关注好友。
     *
     * @param userId   查询该用户的双向关注好友
     * @param pageable 分页参数
     * @return 双向关注好友的ID
     */
    public List<String> getBilateralFriends(String userId, Pageable pageable) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.AFB_KEY).is(true).and(Friendship.BFA_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.BFA_KEY).is(true).and(Friendship.AFB_KEY).is(true));
        Query query = new Query(orOperator(criterias)).with(pageable);

        List<Friendship> friendships = mongoTemplate.find(query, Friendship.class);
        return getIds(userId, friendships);
    }

    public boolean isBilateral(String userId, String followingId) {
        Query query = new Query();
        if (userId.compareTo(followingId) < 0) {
            query.addCriteria(new Criteria(Friendship.A_KEY).is(userId));
            query.addCriteria(new Criteria(Friendship.B_KEY).is(followingId));
        } else {
            query.addCriteria(new Criteria(Friendship.A_KEY).is(followingId));
            query.addCriteria(new Criteria(Friendship.B_KEY).is(userId));
        }
        query.addCriteria(new Criteria(Friendship.AFB_KEY).is(true));
        query.addCriteria(new Criteria(Friendship.BFA_KEY).is(true));

        return mongoTemplate.count(query, Friendship.class) == 1;
    }

    public List<String> getAllFriends(String userId) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.AFB_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.BFA_KEY).is(true));
        Query query = new Query(orOperator(criterias));

        List<Friendship> friendships = mongoTemplate.find(query, Friendship.class);
        return getIds(userId, friendships);
    }

    public List<String> getFollowers(String userId, Pageable pageable) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.BFA_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.AFB_KEY).is(true));
        Query query = new Query(orOperator(criterias)).with(pageable);

        List<Friendship> friendships = mongoTemplate.find(query, Friendship.class);
        return getIds(userId, friendships);
    }

    /**
     * 返回双向关注好友数量。
     *
     * @param userId 查询该用户的双向关注好友数量
     */
    public int countBilateralFriends(String userId) {
        List<Criteria> criterias = new ArrayList<Criteria>(2);
        criterias.add(new Criteria(Friendship.A_KEY).is(userId).and(Friendship.AFB_KEY).is(true).and(Friendship.BFA_KEY).is(true));
        criterias.add(new Criteria(Friendship.B_KEY).is(userId).and(Friendship.BFA_KEY).is(true).and(Friendship.AFB_KEY).is(true));

        return ((Long) mongoTemplate.count(new Query(orOperator(criterias)), Friendship.class)).intValue();
    }

    private List<String> getIds(String userId, List<Friendship> friendships) {
        List<String> ids = new ArrayList<String>(friendships.size());
        for (Friendship friendship : friendships) {
            if (userId.equals(friendship.getA())) {
                ids.add(friendship.getB());
            } else {
                ids.add(friendship.getA());
            }
        }
        return ids;
    }
}
