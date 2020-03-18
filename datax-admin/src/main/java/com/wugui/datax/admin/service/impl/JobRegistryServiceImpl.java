package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.entity.JobRegistry;
import com.wugui.datax.admin.mapper.JobRegistryMapper;
import com.wugui.datax.admin.service.JobRegistryService;
import org.springframework.stereotype.Service;

/**
 * JobRegistryServiceImpl
 * @author jingwk
 * @since 2019-03-15
 * @version v2.1.1
 */
@Service("jobRegistryService")
public class JobRegistryServiceImpl extends ServiceImpl<JobRegistryMapper, JobRegistry> implements JobRegistryService {

}