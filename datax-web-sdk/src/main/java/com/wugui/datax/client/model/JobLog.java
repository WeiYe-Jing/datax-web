package com.wugui.datax.client.model;

import lombok.Data;

import java.util.Date;

/**
 * datax-web log, used to track trigger process
 *
 * @author jingwk  2019-11-17 22:08:11
 */
@Data
public class JobLog {

    private long id;

    // job info
    private int jobGroup;
    private int jobId;
    private String jobDesc;

    // execute info
    private String executorAddress;
    private String executorHandler;
    private String executorParam;
    private String executorShardingParam;
    private int executorFailRetryCount;

    // trigger info
    private Date triggerTime;
    private int triggerCode;
    private String triggerMsg;

    // handle info
    private Date handleTime;
    private int handleCode;
    private String handleMsg;

    // alarm info
    private int alarmStatus;

    private String processId;

    private Long maxId;
}
