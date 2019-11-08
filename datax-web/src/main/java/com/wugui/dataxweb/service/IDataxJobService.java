package com.wugui.dataxweb.service;

import com.wugui.dataxweb.dto.RunJobDto;
import com.wugui.dataxweb.log.LogResult;

/**
 * @program: datax-all
 * @author: huzekang
 * @create: 2019-06-17 11:25
 **/
public interface IDataxJobService {
    /**
     * 根据json字符串用线程池启动一个datax作业
     *
     * @param jobJson
     * @author: huzekang
     * @Date: 2019-06-17
     */
    String startJobByJsonStr(String jobJson,Long jobConfigId);

    String startJobLog(RunJobDto runJobDto);

    LogResult viewJogLog(Long id, int fromLineNum);

    Boolean killJob(String pid);
}
