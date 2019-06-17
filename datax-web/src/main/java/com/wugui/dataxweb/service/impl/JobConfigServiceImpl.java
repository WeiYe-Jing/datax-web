package com.wugui.dataxweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.dataxweb.dao.JobConfigMapper;
import com.wugui.dataxweb.entity.JobConfig;
import com.wugui.dataxweb.service.IJobConfigService;
import org.springframework.stereotype.Service;

@Service
public class JobConfigServiceImpl extends ServiceImpl<JobConfigMapper, JobConfig> implements IJobConfigService {

}
