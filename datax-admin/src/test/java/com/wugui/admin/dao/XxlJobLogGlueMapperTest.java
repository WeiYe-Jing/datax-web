package com.wugui.admin.dao;

import com.wugui.datax.admin.entity.JobLogGlue;
import com.wugui.datax.admin.mapper.JobLogGlueMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobLogGlueMapperTest {

    @Resource
    private JobLogGlueMapper xxlJobLogGlueMapper;

    @Test
    public void test(){
        JobLogGlue logGlue = new JobLogGlue();
        logGlue.setJobId(1);
        logGlue.setGlueType("1");
        logGlue.setGlueSource("1");
        logGlue.setGlueRemark("1");

        logGlue.setAddTime(new Date());
        logGlue.setUpdateTime(new Date());
        int ret = xxlJobLogGlueMapper.save(logGlue);

        List<JobLogGlue> list = xxlJobLogGlueMapper.findByJobId(1);

        int ret2 = xxlJobLogGlueMapper.removeOld(1, 1);

        int ret3 = xxlJobLogGlueMapper.deleteByJobId(1);
    }

}
