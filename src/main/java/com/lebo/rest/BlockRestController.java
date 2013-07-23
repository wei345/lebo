package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.BlockService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Wei Liu
 * Date: 13-7-23
 * Time: PM4:15
 */
@Controller
@RequestMapping("/api/1/blocks")
public class BlockRestController {

    @Autowired
    private BlockService blockService;
    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "screenName", required = false) String screenName) {
        try {
            String blockedId = accountService.getUserId(userId, screenName);
            User user = blockService.block(accountService.getCurrentUserId(), blockedId);
            return accountService.toUserDto(user);
        } catch (Exception e) {
            return ErrorDto.newBadRequestError(e.getMessage());
        }
    }

    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "screenName", required = false) String screenName) {
        try {
            String blockedId = accountService.getUserId(userId, screenName);
            User user = blockService.unblock(accountService.getCurrentUserId(), blockedId);
            return accountService.toUserDto(user);
        } catch (Exception e) {
            return ErrorDto.newBadRequestError(e.getMessage());
        }
    }
}
