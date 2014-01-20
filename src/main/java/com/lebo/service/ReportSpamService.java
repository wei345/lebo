package com.lebo.service;

import com.lebo.entity.ReportSpam;
import com.lebo.repository.ReportSpamDao;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 14-1-15
 * Time: PM3:14
 */
@Service
public class ReportSpamService extends AbstractMongoService {
    @Autowired
    private ReportSpamDao reportSpamDao;

    public void create(ReportSpam reportSpam) {
        mongoTemplate.insert(reportSpam);
    }

    public void delete(String id) {
        reportSpamDao.delete(id);
    }

    public void setProcessed(String id, boolean processed, String userId) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(ReportSpam.ID_KEY).is(new ObjectId(id))),
                new Update()
                        .set(ReportSpam.PROCESSED_KEY, processed)
                        .set(ReportSpam.PROCESS_USER_ID_KEY, userId),
                ReportSpam.class);
    }

    public Page<ReportSpam> list(Boolean processed,
                                 ReportSpam.ObjectType reportObjectType,
                                 String reportUserId,
                                 String informerUserId,
                                 Pageable pageable) {

        Query query = new Query();

        if (processed != null) {
            query.addCriteria(new Criteria(ReportSpam.PROCESSED_KEY).is(processed));
        }

        if (reportObjectType != null) {
            query.addCriteria(new Criteria(ReportSpam.REPORT_OBJECT_TYPE_KEY).is(reportObjectType));
        }

        if(StringUtils.isNotBlank(reportUserId)){
            query.addCriteria(new Criteria(ReportSpam.REPORT_USER_ID_KEY).is(reportUserId));
        }

        if(StringUtils.isNotBlank(informerUserId)){
            query.addCriteria(new Criteria(ReportSpam.INFORMER_USER_ID_KEY).is(informerUserId));
        }

        query.with(pageable);

        List<ReportSpam> list = mongoTemplate.find(query, ReportSpam.class);

        return new PageImpl<ReportSpam>(list, pageable, mongoTemplate.count(query, ReportSpam.class));
    }

    public ReportSpam findOne(String informerUserId,
                              ReportSpam.ObjectType reportObjectType,
                              String reportObjectId) {

        return mongoTemplate.findOne(new Query(
                new Criteria(ReportSpam.INFORMER_USER_ID_KEY).is(informerUserId)
                        .and(ReportSpam.REPORT_OBJECT_TYPE_KEY).is(reportObjectType)
                        .and(ReportSpam.REPORT_OBJECT_ID_KEY).is(reportObjectId)),
                ReportSpam.class);
    }
}
