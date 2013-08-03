package com.lebo.service;

import com.lebo.entity.Block;
import com.lebo.entity.User;
import com.lebo.repository.BlockDao;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-23
 * Time: PM3:56
 */
@Service
public class BlockService {
    @Autowired
    private BlockDao blockDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FriendshipService friendshipService;

    /**
     * 把指定用户拉黑，如果这两个用户之间存在关注与被关注关系，也会被删除。
     * <p/>
     * 返回被拉黑的用户。
     *
     * @param userId    当前用户ID
     * @param blockedId 被拉入黑名单的用户ID
     */
    public User block(String userId, String blockedId) {
        Block block = blockDao.findByUserIdAndBlockingId(userId, blockedId);
        if (block == null) {
            blockDao.save(new Block(userId, blockedId));
            friendshipService.unfollow(userId, blockedId);
            friendshipService.unfollow(blockedId, userId);
        }
        return accountService.getUser(blockedId);
    }

    public User unblock(String userId, String blockedId) {
        Block block = blockDao.findByUserIdAndBlockingId(userId, blockedId);
        if (block != null) {
            blockDao.delete(block);
        }
        return accountService.getUser(blockedId);
    }

    public List<User> list(String userId, PageRequest pageRequest) {
        List<Block> blocks = blockDao.findByUserId(userId, pageRequest);
        List<User> users = new ArrayList<User>();
        for (Block block : blocks) {
            users.add(accountService.getUser(block.getBlockingId()));
        }
        return users;
    }

    public boolean isBlocking(String userId, String blockedId) {
        return blockDao.findByUserIdAndBlockingId(userId, blockedId) != null;
    }
}
