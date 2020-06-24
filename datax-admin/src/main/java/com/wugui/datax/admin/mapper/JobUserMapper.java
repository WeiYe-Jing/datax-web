package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobRole;
import com.wugui.datax.admin.entity.JobUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
@Repository
public interface JobUserMapper {

    List<JobUser> pageList(@Param("offset") int offset,
                           @Param("pagesize") int pagesize,
                           @Param("username") String username);

    int pageListCount(@Param("offset") int offset,
                      @Param("pagesize") int pagesize,
                      @Param("username") String username);

    JobUser loadByUserName(@Param("username") String username);

    JobRole getRoleByUserId(@Param("userId") int userId);


    JobUser getUserById(@Param("id") int id);

    int save(JobUser jobUser);

    int update(JobUser jobUser);

    int delete(@Param("id") int id);



}
