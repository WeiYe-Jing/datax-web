package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobPermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author water
 */
@Mapper
@Repository
public interface PermissionMapper {

    /**
     * 查询所有job权限
     *
     * @return List<JobPermission>
     */
    List<JobPermission> findAll();

    /**
     * 根据userId查询该用户下所有权限
     *
     * @param userId int
     * @return List<JobPermission>
     */
    List<JobPermission> findByAdminUserId(int userId);
}