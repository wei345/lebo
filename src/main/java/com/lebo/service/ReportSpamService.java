package com.lebo.service;

import com.lebo.entity.ReportSpam;
import com.lebo.repository.ReportSpamDao;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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

    public Page<ReportSpam> list(Pageable pageable) {
        return reportSpamDao.findAll(pageable);
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
