package com.wugui.datax.admin.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @author kai.long
 * @Date: 2020-11-28
 * 
 */
@Data
public class JobParam implements Serializable {
    private Integer id;

    private Integer jobId;

    private String jobParam;

    private String jobValue;

    private String remark;

    private static final long serialVersionUID = 1L;
}