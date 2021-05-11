package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于启动任务接收的实体
 *
 * @author jingwk
 * @ClassName TriggerJobDto
 * @Version 1.0
 * @since 2019/12/01 16:12
 */
@Data
public class TriggerJobDto implements Serializable {

    private String executorParam;

    private int jobId;

    /**
     * 任务链触发  组id
     */
    private int groupId;
}
