package com.lebo.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.lebo.entity.Comment;
import com.lebo.entity.Notification;
import com.lebo.entity.Post;
import com.lebo.repository.NotificationDao;
import com.lebo.rest.dto.NotificationDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-8-8
 * Time: PM7:17
 */
@Service
public class NotificationService extends AbstractMongoService {
    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private CommentService commentService;

    public static final String FILE_COLLECTION_NAME = "notification";

    public List<Notification> find(String recipientId, Boolean unread, String[] activityTypes, PaginationParam paginationParam) {

        Query query = new Query();
        if (recipientId != null) {
            query.addCriteria(new Criteria(Notification.RECIPIENT_ID_KEY).is(recipientId));
        }

        if (unread != null) {
            query.addCriteria(new Criteria(Notification.UNREAD_KEY).is(unread));
        }

        if (activityTypes != null && activityTypes.length > 0) {
            query.addCriteria(new Criteria(Notification.ACTIVITY_TYPE_KEY).in(activityTypes));
        }

        paginationById(query, paginationParam);

        return mongoTemplate.find(query, Notification.class);
    }

    public List<Notification> findUnread(String recipientId, PaginationParam paginationParam) {
        return notificationDao.findUnread(recipientId, paginationParam.getMaxId(), paginationParam.getSinceId(), paginationParam);
    }

    public Notification create(Notification notification) {
        return notificationDao.save(notification);
    }

    public void delete(String id) {
        notificationDao.delete(id);
    }

    public void markRead(List<Notification> notifications) {
        Collection<String> ids = Collections2.transform(notifications, new Function<Notification, String>() {
            @Override
            public String apply(Notification notification) {
                return notification.getId();
            }
        });

        mongoTemplate.updateMulti(new Query(new Criteria(Notification.ID_KEY).in(ids)),
                new Update().set(Notification.UNREAD_KEY, false), Notification.class);
    }

    /**
     * 将指定用户的所有未读通知标记为已读。
     */
    public void markAllRead(String userId) {
        Query query = new Query();
        query.addCriteria(new Criteria(Notification.RECIPIENT_ID_KEY).is(userId));
        query.addCriteria(new Criteria(Notification.UNREAD_KEY).is(true));

        mongoTemplate.updateMulti(query, new Update().set(Notification.UNREAD_KEY, false), Notification.class);
    }

    public int countUnreadNotifications(String recipientId) {
        Query query = new Query();
        query.addCriteria(new Criteria(Notification.RECIPIENT_ID_KEY).is(recipientId));
        query.addCriteria(new Criteria(Notification.UNREAD_KEY).is(true));
        return ((Long) mongoTemplate.count(query, Notification.class)).intValue();
    }

    public NotificationDto toNotificationDto(Notification notification) {
        NotificationDto dto = BeanMapper.map(notification, NotificationDto.class);
        dto.setSender(accountService.toBasicUserDto(accountService.getUser(notification.getSenderId())));
        if (Notification.OBJECT_TYPE_POST.equals(notification.getObjectType())) {
            Post post = statusService.getPost(notification.getObjectId());
            if (post != null) {
                dto.setRelatedStatus(statusService.toBasicStatusDto(post));
            }
        }
        if (Notification.OBJECT_TYPE_COMMENT.equals(notification.getObjectType())) {
            Comment comment = commentService.getComment(notification.getObjectId());
            if (comment != null) {
                dto.setRelatedComment(commentService.toBasicCommentDto(comment));
                dto.setRelatedStatus(statusService.toBasicStatusDto(statusService.getPost(dto.getRelatedComment().getPostId())));
            }
        }

        return dto;
    }

    public List<NotificationDto> toNotificationDtos(List<Notification> notifications) {
        List<NotificationDto> dtos = new ArrayList<NotificationDto>(notifications.size());
        for (Notification notification : notifications) {
            dtos.add(toNotificationDto(notification));
        }
        return dtos;
    }
}
