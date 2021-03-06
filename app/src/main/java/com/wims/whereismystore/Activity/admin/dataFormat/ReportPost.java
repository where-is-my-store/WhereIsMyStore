package com.wims.whereismystore.Activity.admin.dataFormat;

import java.io.Serializable;

public class ReportPost implements Serializable {
        private String reportCode;
    private String reportName;
    private String reason;
    private String postID;
    private String state;
    private String Key;
    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }


    public ReportPost(){}
        public ReportPost(String postID, String reportCode, String reportName, String reason, String state){
            this.postID=postID;
            this.reportCode=reportCode;
            this.reportName=reportName;
            this.reason=reason;
            this.state=state;
        }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

        public String getPostID() {
            return postID;
        }

        public void setPostID(String postID) {
            this.postID = postID;
        }


        public String getReportCode() {
            return reportCode;
        }

        public void setReportCode(String reportCode) {
            this.reportCode = reportCode;
        }

        public String getReportName() {
            return reportName;
        }

        public void setReportName(String reportName) {
            this.reportName = reportName;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

}
