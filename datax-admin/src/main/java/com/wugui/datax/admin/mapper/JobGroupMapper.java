package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface JobGroupMapper {

    public List<JobGroup> findAll();

    public List<JobGroup> find(@Param("appName") String appName,
                               @Param("title") String title,
                               @Param("addressList") String addressList);

    public List<JobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(JobGroup xxlJobGroup);

    public int update(JobGroup xxlJobGroup);

    public int remove(@Param("id") int id);

    public JobGroup load(@Param("id") int id);
}
