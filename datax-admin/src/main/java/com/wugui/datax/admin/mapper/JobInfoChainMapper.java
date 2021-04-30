package com.wugui.datax.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobInfoChainMapper {

    List<Integer> getNextJobList(@Param("groupId") int groupId,@Param("jobId") int jobId);

    void removeAll(@Param("groupId") String groupId);

    void insert(@Param("jobId") Integer jobId,@Param("childId") Integer childId,@Param("groupId") String groupId);
}
