package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 举报。
 *
 * @author: Wei Liu
 * Date: 14-1-15
 * Time: PM2:13
 */
@Document(collection = "reportspams")
public class ReportSpam extends IdEntity {

    private String reportUserId; //被举报人
    private ReportType reportType;
    private ObjectType reportObjectType;
    private String reportObjectId;
    private String reportNotes;

    private String informerUserId; //举报人

    private Boolean processed;  //已处理
    public static final String PROCESSED_KEY = "processed";
    private String processUserId;
    public static final String PROCESS_USER_ID_KEY = "processUserId";

    public ReportSpam() {
    }

    public ReportSpam(String reportUserId,
                      ReportType reportType,
                      ObjectType reportObjectType,
                      String reportObjectId,
                      String reportNotes,
                      String informerUserId) {

        this.reportUserId = reportUserId;
        this.reportType = reportType;
        this.reportObjectType = reportObjectType;
        this.reportObjectId = reportObjectId;
        this.reportObjectId = reportObjectId;
        this.reportNotes = reportNotes;
        this.informerUserId = informerUserId;
    }

    public static enum ObjectType {
        POST("帖子"), COMMENT("评论"), IM("私信");

        private String name;

        ObjectType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum ReportType {
        SPAM_AD("垃圾广告"),
        FALSE_WINNING("虚假中奖"),
        PORN("淫秽色情"),
        SENSITIVE_INFORMATION("敏感信息"),
        FALSE_INFORMATION("不实信息"),

        HARASS_ME("骚扰我"),
        PRETEND_ME("冒充我"),
        PLAGIARISM_ME("抄袭我的内容"),
        DISCLOSE_MY_PRIVACY("泄露我的隐私"),
        PERSONAL_ATTACKS_ME("人身攻击我");

        private String name;

        ReportType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public String getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(String reportUserId) {
        this.reportUserId = reportUserId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ObjectType getReportObjectType() {
        return reportObjectType;
    }

    public void setReportObjectType(ObjectType reportObjectType) {
        this.reportObjectType = reportObjectType;
    }

    public String getReportObjectId() {
        return reportObjectId;
    }

    public void setReportObjectId(String reportObjectId) {
        this.reportObjectId = reportObjectId;
    }

    public String getInformerUserId() {
        return informerUserId;
    }

    public void setInformerUserId(String informerUserId) {
        this.informerUserId = informerUserId;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getReportNotes() {
        return reportNotes;
    }

    public void setReportNotes(String reportNotes) {
        this.reportNotes = reportNotes;
    }

    public String getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(String processUserId) {
        this.processUserId = processUserId;
    }

}
