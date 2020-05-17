package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobPermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionMapper {

    List<JobPermission> findAll();

    List<JobPermission> findByAdminUserId(int userId);
}