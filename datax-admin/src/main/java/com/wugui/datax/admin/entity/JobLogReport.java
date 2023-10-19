package com.wugui.datax.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@KeySequence(value = "SEQ_ID")
@Data
public class JobLogReport {
    @TableId(type = IdType.INPUT)
    private int id;

    private Date triggerDay;

    private int runningCount;
    private int sucCount;
    private int failCount;
}
