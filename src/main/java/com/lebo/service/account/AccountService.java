package com.lebo.service.account;

import com.lebo.entity.OpenUser;
import com.lebo.repository.OpenUserDao;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.lebo.entity.User;
import com.lebo.repository.UserDao;
import org.springside.modules.utils.DateProvider;

/**
 * 用户管理类.
 * 
 * @author liuwei
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class AccountService {

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	private UserDao userDao;
    private OpenUserDao openUserDao;
	private DateProvider dateProvider = DateProvider.DEFAULT;

	public User getUser(String id) {
		return userDao.findOne(id);
	}

    public OpenUser findOpenUserByProviderUid (String provider, String uid) {
        return openUserDao.findByProviderAndUid(provider, uid);
    }

	@Transactional(readOnly = false)
	public User saveUser(User user) {
		return userDao.save(user);
	}

    @Transactional(readOnly = false)
    public OpenUser saveOpenUser(OpenUser openUser){
        return openUserDao.save(openUser);
    }

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.name;
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
