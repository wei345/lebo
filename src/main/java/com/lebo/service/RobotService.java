package com.lebo.service;

import com.lebo.entity.Robot;
import com.lebo.entity.User;
import com.lebo.redis.RedisKeys;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author: Wei Liu
 * Date: 13-12-31
 * Time: PM3:53
 */
@Service
public class RobotService extends AbstractMongoService {

    public void setRobot(String userId) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().set(User.ROBOT_KEY, new Robot()),
                User.class);
    }

    public void unsetRobot(String userId) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().unset(User.ROBOT_KEY),
                User.class);
    }


    @Cacheable(value = RedisKeys.CACHE_NAME_DEFAULT, key = RedisKeys.ROBOT_GROUP_SPEL)
    public List<RobotGroup> getGroups() {

        Query query = new Query(new Criteria(User.ROBOT_KEY).ne(null));
        query.fields().include(User.ROBOT_KEY);

        List<User> users = mongoTemplate.find(query, User.class);

        Map<String, RobotGroup> name2group = new TreeMap<String, RobotGroup>();

        for (User user : users) {
            if (user.getRobot().getGroups() != null) {
                for (String group : user.getRobot().getGroups()) {

                    RobotGroup robotGroup = name2group.get(group);
                    if (robotGroup == null) {
                        name2group.put(group, new RobotGroup(group, 1));
                    } else {
                        robotGroup.setMemberCount(robotGroup.getMemberCount() + 1);
                    }

                }
            }
        }

        return new ArrayList<RobotGroup>(name2group.values());
    }

    public static class RobotGroup {
        private String name;
        public static final String NAME_KEY = "name";
        private int memberCount;

        @SuppressWarnings("unused")
        public RobotGroup() {
        }

        public RobotGroup(String name, int count) {
            this.name = name;
            this.memberCount = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }
    }

    @CacheEvict(value = RedisKeys.CACHE_NAME_DEFAULT, key = RedisKeys.ROBOT_GROUP_SPEL)
    public void renameGroup(String oldName, String newName) {

        Query query = new Query(new Criteria(User.ROBOT_GROUPS_KEY).is(oldName));

        mongoTemplate.updateMulti(
                query,
                new Update().addToSet(User.ROBOT_GROUPS_KEY, newName),
                User.class);

        mongoTemplate.updateMulti(
                query,
                new Update().pull(User.ROBOT_GROUPS_KEY, oldName),
                User.class);

    }

    @CacheEvict(value = RedisKeys.CACHE_NAME_DEFAULT, key = RedisKeys.ROBOT_GROUP_SPEL)
    public void deleteGroup(String name) {
        mongoTemplate.updateMulti(
                new Query(new Criteria(User.ROBOT_GROUPS_KEY).is(name)),
                new Update().pull(User.ROBOT_GROUPS_KEY, name),
                User.class);
    }

    @CacheEvict(value = RedisKeys.CACHE_NAME_DEFAULT, key = RedisKeys.ROBOT_GROUP_SPEL)
    public void updateGroup(String userId, List<String> groups) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().set(User.ROBOT_GROUPS_KEY, groups),
                User.class);
    }


    public List<User> getAllRobots() {
        return mongoTemplate.find(
                new Query(new Criteria(User.ROBOT_KEY).ne(null)),
                User.class);
    }


}
