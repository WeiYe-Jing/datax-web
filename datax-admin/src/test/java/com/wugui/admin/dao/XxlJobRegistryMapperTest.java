package com.wugui.admin.dao;

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
public class XxlJobRegistryMapperTest {

    @Resource
    private JobRegistryMapper xxlJobRegistryMapper;

    @Test
    public void test(){
        int ret = xxlJobRegistryMapper.registryUpdate("g1", "k1", "v1", new Date());
        if (ret < 1) {
            ret = xxlJobRegistryMapper.registrySave("g1", "k1", "v1", new Date());
        }

        List<JobRegistry> list = xxlJobRegistryMapper.findAll(1, new Date());

        int ret2 = xxlJobRegistryMapper.removeDead(Arrays.asList(1));
    }

}
