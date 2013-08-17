package com.lebo.service.account;

import com.lebo.entity.Favorite;
import com.lebo.entity.HotUser;
import com.lebo.entity.User;
import com.lebo.event.AfterUserCreateEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.*;
import com.lebo.service.param.SearchParam;
import com.lebo.util.ImageUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springside.modules.cache.memcached.SpyMemcachedClient;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private UserDao userDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StatusService statusService;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private SpyMemcachedClient spyMemcachedClient;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ApplicationEventBus eventBus;

    public List<User> searchUser(SearchParam param) {
        Query query = new Query();

        if (StringUtils.isNotBlank(param.getQ())) {
            query.addCriteria(new Criteria(User.SCREEN_NAME_KEY).regex(param.getQ(), "i"));
        }

        query.with(param);
        return mongoTemplate.find(query, User.class);
    }

    public User findUserByScreenName(String screenName) {
        return userDao.findByScreenName(screenName);
    }

    public User getUser(String id) {
        return userDao.findOne(id);
    }

    public User saveUser(User user) {
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            entryptPassword(user);
            user.setPlainPassword(null);
        }

        user = userDao.save(user);
        throwOnMongoError();
        return user;
    }

    public User createUser(User user) {
        user.initial();
        user.setCreatedAt(dateProvider.getDate());
        user = saveUser(user);
        eventBus.post(new AfterUserCreateEvent(user));
        return user;
    }

    /**
     * 接收userId和screenName，二者至少一个不为空，返回userId。
     *
     * @throws ServiceException 当userId和screenName都为空，或当根据screenName未找到User时
     */
    public String getUserId(String userId, String screenName) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            throw new ServiceException("Providing either screenName or userId is required.");
        }

        if (!StringUtils.isBlank(userId)) {
            return userId;
        } else {
            User user = findUserByScreenName(screenName);
            if (user == null) {
                throw new ServiceException("Not Found " + screenName);
            }
            return user.getId();
        }
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

    public UserDto toBasicUserDto(User user) {
        return BeanMapper.map(user, UserDto.class);
    }

    public UserDto toUserDto(User user) {
        UserDto dto = BeanMapper.map(user, UserDto.class);

        dto.setStatusesCount(statusService.countUserStatus(user.getId()));
        if (!getCurrentUserId().equals(user.getId())) {
            dto.setFollowing(friendshipService.isFollowing(getCurrentUserId(), user.getId()));
            if (dto.getFollowing()) {
                dto.setBilateral(friendshipService.isBilateral(getCurrentUserId(), user.getId()));
            } else {
                dto.setBilateral(false);
            }
        }
        dto.setFriendsCount(friendshipService.countFollowings(user.getId()));
        dto.setFavoritesCount(favoriteService.countUserFavorites(user.getId()));
        dto.setBlocking(blockService.isBlocking(getCurrentUserId(), user.getId()));

        return dto;
    }

    public List<UserDto> toUserDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<UserDto>();
        for (User user : users) {
            dtos.add(toUserDto(user));
        }
        return dtos;
    }

    public List<User> getUsers(List<String> ids) {
        List<User> users = new ArrayList<User>();
        for (String id : ids) {
            users.add((getUser(id)));
        }
        return users;
    }

    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
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

    //增长粉丝计数
    public void increaseFollowersCount(String userId) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(userId)),
                new Update().inc(User.FOLLOWERS_COUNT_KEY, 1),
                User.class);
    }

    //减少粉丝计数
    public void decreaseFollowersCount(String userId) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(userId)),
                new Update().inc(User.FOLLOWERS_COUNT_KEY, -1),
                User.class);
    }

    //更新粉丝数
    public void updateFollowersCount(String userId) {
        int count = friendshipService.countFollowers(userId);
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(userId)),
                new Update().set(User.FOLLOWERS_COUNT_KEY, count),
                User.class);
    }

    //增长收藏计数
    public void increaseFavoritesCount(String userId) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(userId)),
                new Update().inc(User.BE_FAVORITED_COUNT_KEY, 1),
                User.class);
    }

    /**
     * 指定用户的被收藏计数减少1。
     *
     * @param userId 要减少收藏计数的用户ID
     */
    public void decreaseFavoritesCount(String userId) {
        decreaseFavoritesCount(userId, 1);
    }

    /**
     * 指定用户的被收藏计数减少指定数量。
     *
     * @param userId 要减少收藏计数的用户ID
     * @param count  收藏计数减少多少，应为正数
     */
    public void decreaseFavoritesCount(String userId, int count) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(userId)),
                new Update().inc(User.BE_FAVORITED_COUNT_KEY, count * -1),
                User.class);
    }

    //增长播放计数
    public void increasePlaysCount(String userId) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(userId)),
                new Update().inc(User.VIEW_COUNT_KEY, 1),
                User.class);
    }

    /**
     * 检查对于userId来说，screenName是否可用。
     */
    public boolean isScreenNameAvailable(String screenName, String userId) {
        Assert.hasText(screenName);

        Criteria criteria = new Criteria(User.SCREEN_NAME_KEY).is(screenName);
        if (StringUtils.isNotBlank(userId)) {
            criteria.and("_id").ne(userId);
        }
        return mongoTemplate.count(new Query(criteria), User.class) == 0;
    }

    /**
     * 更新热门用户。
     * <p/>
     * 按24小时内收到的红心数排序。
     */
    public void refreshHotUsers() {
        Date daysAgo = DateUtils.addDays(dateProvider.getDate(), -1);
        String mapFunction = String.format("function(){ emit(this.%s, 1); }", Favorite.POST_USERID_KEY);
        String reduceFunction = "function(key, emits){ var total = 0; for(var i = 0; i < emits.length; i++){ total += emits[i]; } return total; }";

        //因为不需要返回结果，所有不用mongoTemplate#mapReduce
        DBObject dbObject = new BasicDBObject();
        dbObject.put("mapreduce", mongoTemplate.getCollectionName(Favorite.class));
        dbObject.put("map", mapFunction);
        dbObject.put("reduce", reduceFunction);
        dbObject.put("out", mongoTemplate.getCollectionName(HotUser.class));
        dbObject.put("query", QueryBuilder.start().put(Favorite.CREATED_AT_KEY).greaterThan(daysAgo).get());
        dbObject.put("verbose", true);

        logger.debug("正在刷新热门用户: {}", dbObject);

        CommandResult result = mongoTemplate.executeCommand(dbObject);
        result.throwOnError();

        logger.debug("完成刷新热门用户，result: {}", result);
    }

    private static final Sort hotUserSort = new Sort(Sort.Direction.DESC, HotUser.HOT_BE_FAVORITED_COUNT);

    public List<UserDto> getHotUsers(int page, int size) {
        List<HotUser> hotUsers = mongoTemplate.find(new Query().with(new PageRequest(page, size, hotUserSort)), HotUser.class);

        List<UserDto> dtos = new ArrayList<UserDto>(hotUsers.size());
        for (HotUser hotUser : hotUsers) {
            UserDto dto = toUserDto(getUser(hotUser.getId()));
            dto.setHotBeFavoritedCount(hotUser.getHotBeFavoritedCount());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 设置不同大小的用户profileImage。旧图片会被删掉。
     *
     * @param user 设置新profile image的用户
     * @param profileImage InputStream of user's profile image
     * @throws IOException
     */
    public void saveUserWithProfileImage(User user, InputStream profileImage) throws IOException {
        BufferedImage originImage = ImageIO.read(profileImage);
        ByteArrayOutputStream outputStream;
        String fileId;

        //normal size
        BufferedImage normal = ImageUtils.resizeImage(User.PROFILE_IMAGE_NORMAL_SIZE, originImage);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(normal, "png", outputStream);
        fileId = fileStorageService.save(new ByteArrayInputStream(outputStream.toByteArray()),
                "image/png", outputStream.size());
        if(isMongoId(user.getProfileImageNormal())){
            fileStorageService.delete(user.getProfileImageNormal());
        }
        user.setProfileImageNormal(fileId);

        //bigger size
        BufferedImage bigger = ImageUtils.resizeImage(User.PROFILE_IMAGE_BIGGER_SIZE, originImage);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(bigger, "png", outputStream);
        fileId = fileStorageService.save(new ByteArrayInputStream(outputStream.toByteArray()),
                "image/png", outputStream.size());
        if(isMongoId(user.getProfileImageBigger())){
            fileStorageService.delete(user.getProfileImageBigger());
        }
        user.setProfileImageBigger(fileId);

        //origin size
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(originImage, "png", outputStream);
        fileId = fileStorageService.save(new ByteArrayInputStream(outputStream.toByteArray()),
                "image/png", outputStream.size());
        if(isMongoId(user.getProfileImageOriginal())){
            fileStorageService.delete(user.getProfileImageOriginal());
        }
        user.setProfileImageOriginal(fileId);

        saveUser(user);
    }
}
