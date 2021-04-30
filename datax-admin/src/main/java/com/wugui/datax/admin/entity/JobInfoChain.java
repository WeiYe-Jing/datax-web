package com.wugui.datax.admin.entity;

import lombok.Data;

@Data
public class JobInfoChain {

    private int id;

    private int jobId;

    private int childId;

    private int groupId;
}
