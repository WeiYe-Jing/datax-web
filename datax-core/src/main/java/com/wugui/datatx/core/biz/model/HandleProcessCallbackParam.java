package com.wugui.datatx.core.biz.model;

import java.io.Serializable;

/**
 * Created by jingwk on 2019/12/14.
 */
public class HandleProcessCallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private long logId;
    private String processId;
    private long logDateTime;


    public HandleProcessCallbackParam(){}

    public HandleProcessCallbackParam(long logId,long logDateTime, String processId) {
        this.logId = logId;
        this.processId = processId;
        this.logDateTime=logDateTime;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public long getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(long logDateTime) {
        this.logDateTime = logDateTime;
    }

    @Override
    public String toString() {
        return "HandleCallbackParam{" +
                "logId=" + logId +
                ", processId=" + processId +
                ", logDateTime=" + logDateTime +
                '}';
    }

}
