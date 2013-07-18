package com.lebo.service.account;

import com.lebo.entity.User;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.AbstractMongoService;
import com.lebo.service.FriendshipService;
import com.lebo.service.ServiceException;
import com.lebo.service.StatusService;
import com.lebo.service.param.SearchParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.DateProvider;
import org.springside.modules.utils.Encodes;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理类.
 *
 * @author Wei Liu
 */
@Component
public class AccountService extends AbstractMongoService {

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private UserDao userDao;
    private DateProvider dateProvider = DateProvider.DEFAULT;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StatusService statusService;
    @Autowired
    private FriendshipService friendshipService;


    public List<User> searchUser(SearchParam param) {
        return userDao.search(param.getQ(), param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public User getUserByScreenName(String screenName) {
        return userDao.findByScreenName(screenName);
    }

    public User getUser(String id) {
        return userDao.findOne(id);
    }

    public User saveUser(User user) {
        user = userDao.save(user);
        throwOnMongoError();
        return user;
    }

    /**
     * 接收userId和screenName，二者至少一个不为空，返回userId。
     *
     * @throws IllegalArgumentException 当userId和screenName都为空
     * @throws ServiceException         当根据screenName未找到User时
     */
    public String getUserId(String userId, String screenName) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            throw new IllegalArgumentException("Providing either screenName or userId is required.");
        }

        if (!StringUtils.isBlank(userId)) {
            return userId;
        } else {
            User user = getUserByScreenName(screenName);
            if (user == null) {
                throw new ServiceException("Not Found " + screenName);
            }
            return user.getId();
        }
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.name;
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    public String getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

    public User findByOAuthId(String oAuthId) {
        return userDao.findByOAuthIds(oAuthId);
    }

    public void updateLastSignInAt(User user) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(new ObjectId(user.getId()))),
                new Update().set(User.USER_LAST_SIGN_IN_AT_KEY, dateProvider.getDate()), User.class);
    }

    public UserDto toUserDto(User user) {
        UserDto dto = BeanMapper.map(user, UserDto.class);

        dto.setStatusesCount(statusService.countUserStatus(user.getId()));
        if (!getCurrentUserId().equals(user.getId())) {
            dto.setFollowing(friendshipService.isFollowing(getCurrentUserId(), user.getId()));
        }
        dto.setFollowersCount(friendshipService.countFollowers(user.getId()));
        dto.setFriendsCount(friendshipService.countFollowings(user.getId()));
        return dto;
    }

    public List<UserDto> toUserDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<UserDto>();
        for (User user : users) {
            dtos.add(toUserDto(user));
        }
        return dtos;
    }

    public User findByEmail(String email){
        return userDao.findByEmail(email);
    }

    public void registerUser(User user) {
        entryptPassword(user);
        user.setCreatedAt(dateProvider.getDate());

        userDao.save(user);
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }
}
