package com.lebo.rest;

import com.lebo.entity.Setting;
import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.HotUserListDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.SettingService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import com.lebo.service.param.PaginationParam;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

import javax.validation.Valid;
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
@RequestMapping("/api/1/users")
public class UserRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private SettingService settingService;

    //TODO 搜索用户，除了名字还查找位置和公司？
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "page", defaultValue = "0") int pageNo,
                         @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size,
                         @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
                         @RequestParam(value = "order", defaultValue = "desc") String order) {
        if (orderBy.equals("id")) {
            orderBy = "_id";
        }

        Sort.Direction direction;
        if (order.equals("desc")) {
            direction = Sort.Direction.DESC;
        } else if (order.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            return ErrorDto.badRequest(String.format("参数order值[%s]无效", order));
        }

        if (User.FOLLOWERS_COUNT_KEY.equals(orderBy) || User.BE_FAVORITED_COUNT_KEY.equals(orderBy) ||
                User.VIEW_COUNT_KEY.equals(orderBy) || "_id".equals(orderBy) || User.DIGEST_COUNT_KEY.equals(orderBy)) {
            //搜索
            SearchParam param = new SearchParam(q, pageNo, size, direction, orderBy);
            List<User> users = accountService.searchUser(param);
            return accountService.toUserDtos(users);

        } else {
            return ErrorDto.badRequest(String.format("参数orderBy值[%s]无效", orderBy));
        }
    }

    /**
     * 查看用户
     */
    @RequestMapping(value = "show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName) {
        String id = accountService.getUserId(userId, screenName);
        User user = accountService.getUser(id);

        if (user == null) {
            return ErrorDto.notFound();
        }

        return accountService.toUserDto(user);
    }

    /**
     * 红人榜页
     *
     * @param btn true - 返回结果带有按钮设置，false - 返回结果不带按钮设置，默认false
     */
    @RequestMapping(value = "hotUserList", method = RequestMethod.GET)
    @ResponseBody
    public Object hotUserList(@Valid PageRequest pageRequest,
                              @RequestParam(value = "btn", defaultValue = "true") boolean btn) {
        //bugfix:总是返回按钮设置，解决ios客户端因为自身bug导致崩溃问题
        btn = true;

        HotUserListDto dto = new HotUserListDto();
        if (btn) {
            Setting setting = settingService.getSetting();
            BeanMapper.copy(setting, dto);
        }

        List<UserDto> users = accountService.getHotUsers(pageRequest.getPage(), pageRequest.getSize());
        dto.setUsers(users);
        return dto;
    }
}
