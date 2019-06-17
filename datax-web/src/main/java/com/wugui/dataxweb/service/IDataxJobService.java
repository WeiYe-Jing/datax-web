package com.wugui.dataxweb.service;

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
    */ 
   String startJobByJsonStr(String jobJson);
}
