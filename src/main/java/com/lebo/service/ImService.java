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
import org.springside.modules.mapper.BeanMapper;

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
    private ImDao imDao;
    public static final String FILE_COLLECTION_NAME = "im";

    //目前只保存附件
    public Im create(String fromUserId, String toUserId, String message, List<FileInfo> attachments, Integer type) {
        Im im = new Im();
        im.setCreatedAt(new Date());
        im.setId(newMongoId(im.getCreatedAt()));
        im.setFromUserId(fromUserId);
        im.setToUserId(toUserId);
        im.setMessage(message);
        im.setType(type);

        //保存附件
        if (attachments != null && attachments.size() > 0) {
            for (int i = 0; i < attachments.size(); i++) {
                FileInfo fileInfo = attachments.get(i);
                fileInfo.setKey(generateImFileKey(im.getId(), i, fileInfo.getContentType()));
            }
            fileStorageService.save(attachments.toArray(new FileInfo[]{}));
            im.setAttachments(attachments);
        }

        imDao.save(im);
        return im;
    }

    public Im newMessage(String fromUserId, String toUserId, String message, int type) {
        return create(fromUserId, toUserId, message, null, type);
    }

    public Im completeUpload(String fromUserId, String toUserId, List<FileInfo> attachments) {
        return create(fromUserId, toUserId, null, attachments, null);
    }

    private String generateImFileKey(String imId, int i, String contentType) {
        return new StringBuilder(FILE_COLLECTION_NAME + "/")
                .append(imId).append("-").append(i)
                .append(".").append(ContentTypeMap.getExtension(contentType))
                .toString();
    }

    public ImDto toDto(Im im) {
        return BeanMapper.map(im, ImDto.class);
    }

    public List<ImDto> toDtos(List<Im> ims) {
        List<ImDto> dtos = new ArrayList<ImDto>(ims.size());
        for (Im im : ims) {
            dtos.add(toDto(im));
        }
        return dtos;
    }

    public List<Im> getRecentMessage(String toUserId, Date afterTime, int count) {
        Query query = new Query(new Criteria(Im.CREATED_AT).gt(afterTime));
        query.addCriteria(new Criteria(Im.TO_USER_ID).is(toUserId));
        query.addCriteria(new Criteria(Im.TYPE_KEY).ne(null));
        query.with(PaginationParam.ID_DESC_SORT);
        query.limit(count);

        return mongoTemplate.find(query, Im.class);
    }
}
