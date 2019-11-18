package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.mapper.JobConfigMapper;
import com.wugui.datax.admin.entity.JobConfig;
import com.wugui.datax.admin.service.IJobConfigService;
import org.springframework.stereotype.Service;

@Service
public class JobConfigServiceImpl extends ServiceImpl<JobConfigMapper, JobConfig> implements IJobConfigService {

}
