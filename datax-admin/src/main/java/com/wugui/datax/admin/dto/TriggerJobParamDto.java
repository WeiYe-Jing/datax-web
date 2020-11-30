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
public class TriggerJobParamDto implements Serializable {

    private String startTime;

    private String endTime;

    private  String addressIp;

}
