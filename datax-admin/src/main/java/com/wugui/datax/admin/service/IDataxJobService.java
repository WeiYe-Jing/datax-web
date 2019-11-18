package com.wugui.datax.admin.service;

import com.wugui.datax.admin.log.LogResult;

/**
 * @program: com.wugui.datax-all
 * @author: huzekang
 * @create: 2019-06-17 11:25
 **/
public interface IDataxJobService {

    /**
     * 启动datax
     * @param jobJson
     * @param jobId
     * @return
     */
    String startJob(String jobJson,Long jobId);

    /**
     * 根据JobId获取最近一次运行日志
     * @param id
     * @param fromLineNum
     * @return
     */
    LogResult viewJogLog(Long id, int fromLineNum);

    /**
     * 结束datax进程
     * @param pid
     * @param id
     * @return
     */
    Boolean killJob(String pid,Long id);
}
