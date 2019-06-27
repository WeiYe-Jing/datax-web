package com.wugui.dataxweb.service;

import com.wugui.dataxweb.dto.RunJobDto;

/**
 * @program: datax-all
 * @author: huzekang
 * @create: 2019-06-17 11:25
 **/
public interface IDataxJobService {
    /** 
    * 根据json字符串用线程池启动一个datax作业 
    *
    * @author: huzekang
     * @Date: 2019-06-17
     * @param jobJson
    */ 
   String startJobByJsonStr(String jobJson);

    String startJobLog(RunJobDto runJobDto);
}
