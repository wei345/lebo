package com.lebo.service.account;

import com.lebo.entity.OpenUser;
import com.lebo.entity.User;
import com.lebo.repository.OpenUserDao;
import com.lebo.repository.UserDao;
import com.lebo.service.param.SearchParam;
import com.lebo.service.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.DateProvider;

import java.util.List;

/**
 * 用户管理类.
 *
 * @author Wei Liu
 */
// Spring Service Bean的标识.
@Component
//TODO Transactional对Mongo没有作用，移除？
@Transactional(readOnly = true)
public class AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    private UserDao userDao;
    private OpenUserDao openUserDao;
    private DateProvider dateProvider = DateProvider.DEFAULT;


    public List searchUser(SearchParam param) {
        return userDao.searchUser(param.getQ(), param).getContent();
    }

    public User getUserByScreenName(String screenName) {
        return userDao.findByScreenName(screenName);
    }

    public User getUser(String id) {
        return userDao.findOne(id);
    }

    public OpenUser findOpenUserByProviderUid(String provider, String uid) {
        return openUserDao.findByProviderAndUid(provider, uid);
    }

    @Transactional(readOnly = false)
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Transactional(readOnly = false)
    public OpenUser saveOpenUser(OpenUser openUser) {
        return openUserDao.save(openUser);
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

    public String getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setOpenUserDao(OpenUserDao openUserDao) {
        this.openUserDao = openUserDao;
    }

    public void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }
}
