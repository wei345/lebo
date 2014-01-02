package com.lebo.service;

import com.lebo.entity.Robot;
import com.lebo.entity.User;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void unsetRobot(String userId){
        mongoTemplate.updateFirst(
                new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().unset(User.ROBOT_KEY),
                User.class);
    }

    public List<User> getAllRobots(){
        return mongoTemplate.find(
                new Query(new Criteria(User.ROBOT_KEY).ne(null)),
                User.class);
    }


}
