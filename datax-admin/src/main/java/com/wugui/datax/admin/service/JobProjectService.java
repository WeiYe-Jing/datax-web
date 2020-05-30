package com.wugui.datax.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.entity.JobProject;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;

/**
 * Job project
 *
 * @author jingwk
 * @version v2.1.2
 * @since 2020-05-24
 */
public interface JobProjectService extends IService<JobProject> {

    /**
     * project page
     * @param pageSize
     * @param pageNo
     * @param searchName
     * @return
     */

    IPage<JobProject> getProjectListPaging(Integer pageSize, Integer pageNo, String searchName);
}