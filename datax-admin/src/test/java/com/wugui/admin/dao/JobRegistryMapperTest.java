package com.wugui.admin.dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wugui.datatx.core.enums.RegistryConfig;
import com.wugui.datax.admin.entity.JobRegistry;
import com.wugui.datax.admin.mapper.JobRegistryMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobRegistryMapperTest {

    @Resource
    private JobRegistryMapper jobRegistryMapper;

    @Test
    public void test(){
        int ret = jobRegistryMapper.registryUpdate("g1", "k1", "v1",0,0,0, new Date());

        DateTime dateTime = DateUtil.offsetSecond(new Date(), 0 - 1);
        List<JobRegistry> list = jobRegistryMapper.findAll(dateTime.toJdkDate());

        int ret2 = jobRegistryMapper.removeDead(Arrays.asList(1));
    }

}
