package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobParamMapper {
    /**
     * 根据ID删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 全量字段插入
     */
    int insert(JobParam record);

    /**
     * 根据字段是否为空插入
     */
    int insertSelective(JobParam record);

    /**
     * 根据ID查询
     */
    JobParam selectByPrimaryKey(Integer id);


    /**
     * 根据ID查询
     */
    List<JobParam> selectByJobId(Integer jobId);

    /**
     * 根据ID更新
     */
    int updateByPrimaryKeySelective(JobParam record);

    /**
     * 根据ID更新
     */
    int updateByPrimaryKey(JobParam record);

    /**
     * 批量根据ID按when语句更新
     */
    int batchUpdate(List<JobParam> list);

    /**
     * 批量根据ID按when语句以及字段是否为空更新
     */
    int batchUpdateSelective(List<JobParam> list);

    /**
     * 批量全字段插入
     */
    int batchInsert(List<JobParam> list);

    /**
     * 批量根据ID更新
     */
    int batchUpdateList(List<JobParam> list);
}