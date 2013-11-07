package com.lebo.service;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Im;
import com.lebo.repository.ImDao;
import com.lebo.rest.dto.ImDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import com.lebo.util.ContentTypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM1:17
 */
@Service
public class ImService extends AbstractMongoService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ImDao imDao;
    public static final String FILE_COLLECTION_NAME = "im";

    //目前只保存附件
    public Im create(String fromUserId, String toUserId, String message, List<FileInfo> attachments) {
        Im im = new Im();
        im.setCreatedAt(new Date());
        im.setId(newMongoId(im.getCreatedAt()));
        im.setFrom(fromUserId);
        im.setTo(toUserId);
        im.setMessage(message);

        //保存附件
        for (int i = 0; i < attachments.size(); i++) {
            FileInfo fileInfo = attachments.get(i);
            fileInfo.setKey(generateImFileKey(im.getId(), i, fileInfo.getContentType()));
        }
        fileStorageService.save(attachments.toArray(new FileInfo[]{}));
        im.setAttachments(attachments);

        imDao.save(im);
        return im;
    }

    private String generateImFileKey(String imId, int i, String contentType) {
        return new StringBuilder(FILE_COLLECTION_NAME + "/")
                .append(imId).append("-").append(i)
                .append(".").append(ContentTypeMap.getExtension(contentType))
                .toString();
    }

    public ImDto toDto(Im im) {
        ImDto dto = new ImDto();
        dto.setFrom(accountService.toBasicUserDto(accountService.getUser(im.getFrom())));
        dto.setTo(accountService.toBasicUserDto(accountService.getUser(im.getTo())));
        dto.setCreatedAt(im.getCreatedAt());
        dto.setAttachments(FileInfo.toDtos(im.getAttachments()));
        dto.setId(im.getId());
        return dto;
    }

    public List<ImDto> toDtos(List<Im> ims) {
        List<ImDto> dtos = new ArrayList<ImDto>(ims.size());
        for (Im im : ims) {
            dtos.add(toDto(im));
        }
        return dtos;
    }

    public List<Im> getRecentMessage(Date fromTime, int count) {
        Query query = new Query(new Criteria(Im.CREATED_AT).gt(fromTime));
        query.with(PaginationParam.ID_DESC_SORT);
        query.limit(count);

        return mongoTemplate.find(query, Im.class);
    }
}
