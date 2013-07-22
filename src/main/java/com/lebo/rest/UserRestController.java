package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.GridFsService;
import com.lebo.service.ServiceException;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import com.lebo.service.param.PaginationParam;
import com.lebo.service.param.SearchParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Users.
 * 用户可以修改screenName，关注和粉丝不变。
 *
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:32
 */
@Controller
public class UserRestController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private GridFsService gridFsService;

    //TODO 搜索用户，除了名字还查找位置和公司？
    @RequestMapping(value = "/api/1/users/search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "page", defaultValue = "0") int pageNo,
                         @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size,
                         @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
                         @RequestParam(value = "order", defaultValue = "desc") String order) {
        if(orderBy.equals("id")){
            orderBy = "_id";
        }

        Sort.Direction direction;
        if (order.equals("desc")) {
            direction = Sort.Direction.DESC;
        } else if (order.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            return ErrorDto.newBadRequestError(String.format("参数order值[%s]无效", order));
        }

        if (User.FOLLOWERS_COUNT_KEY.equals(orderBy) || User.FAVORITES_COUNT_KEY.equals(orderBy) ||
                User.PLAYS_COUNT_KEY.equals(orderBy) || "_id".equals(orderBy)) {
            //搜索
            SearchParam param = new SearchParam(q, pageNo, size, direction, orderBy);
            List<User> users = accountService.searchUser(param);
            return accountService.toUserDtos(users);

        } else {
            return ErrorDto.newBadRequestError(String.format("参数orderBy值[%s]无效", orderBy));
        }
    }

    /**
     * 查看用户
     *
     * @param userId
     * @param screenName
     * @return
     */
    @RequestMapping(value = "/api/1/users/show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName) {
        String id = accountService.getUserId(userId, screenName);
        User user = accountService.getUser(id);
        return accountService.toUserDto(user);
    }

    @RequestMapping(value = "/api/1/account/updateProfile", method = RequestMethod.POST)
    @ResponseBody
    public Object set(@RequestParam(value = "screenName", required = false) String screenName,
                      @RequestParam(value = "image", required = false) MultipartFile image,
                      @RequestParam(value = "description", required = false) String description) {
        String userId = accountService.getCurrentUserId();
        User user = accountService.getUser(userId);

        if (StringUtils.isNotBlank(screenName)) {
            if(!accountService.isScreenNameAvailable(screenName, accountService.getCurrentUserId())){
                return ErrorDto.newBadRequestError(String.format("%s 已被占用", screenName));
            }
            user.setScreenName(screenName);
        }

        String oldImageId = null;
        if(image != null && image.getSize() > 0){
            try {
                String imageId = gridFsService.save(image.getInputStream(), image.getOriginalFilename(), image.getContentType());
                oldImageId = user.getProfileImageUrl();
                user.setProfileImageUrl(imageId);
            } catch (IOException e) {
                return ErrorDto.newBadRequestError(new ServiceException("保存图片失败; ", e).getMessage());
            }
        }
        if (StringUtils.isNotBlank(description)) {
            user.setDescription(description);
        }
        accountService.saveUser(user);

        //删除旧图片
        if(accountService.isMongoId(oldImageId)){
            gridFsService.delete(oldImageId);
        }
        //更新ShiroUser
        if(oldImageId != null){
            updateCurrentUser(user);
        }
        return accountService.toUserDto(user);
    }

    @RequestMapping(value = "/api/1/account/checkScreenName", method = RequestMethod.POST)
    @ResponseBody
    public Object checkScreenName(@RequestParam(value = "screenName") String screenName){
        if(StringUtils.isBlank(screenName)){
            return ErrorDto.newBadRequestError("参数screenName不能为空");
        }

        if(accountService.isScreenNameAvailable(screenName, accountService.getCurrentUserId())){
            return "true";
        }else{
            return "false";
        }
    }

    /**
     * 更新Shiro中当前用户的用户名、图片.
     */
    private void updateCurrentUser(User user) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        shiroUser.screenName = user.getScreenName();
        shiroUser.profileImageUrl = gridFsService.getDisplayUrl(user.getProfileImageUrl());
    }

}
