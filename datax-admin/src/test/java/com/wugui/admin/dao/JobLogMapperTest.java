package com.wugui.admin.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wugui.datax.admin.entity.JobLog;
import com.wugui.datax.admin.mapper.JobLogMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobLogMapperTest {

    @Resource
    private JobLogMapper jobLogMapper;

    @Test
    public void test(){
        Page<JobLog> list = jobLogMapper.pageList(new Page(0,10), 1, 1, null, null, 1);

        JobLog log = new JobLog();
        log.setJobGroup(1);
        log.setJobId(1);

        long ret1 = jobLogMapper.insert(log);
        JobLog dto = jobLogMapper.load(log.getId());

        log.setTriggerTime(new Date());
        log.setTriggerCode(1);
        log.setTriggerMsg("1");
        log.setExecutorAddress("1");
        log.setExecutorHandler("1");
        log.setExecutorParam("1");
        ret1 = jobLogMapper.updateTriggerInfo(log);
        dto = jobLogMapper.load(log.getId());


        log.setHandleTime(new Date());
        log.setHandleCode(2);
        log.setHandleMsg("2");
        ret1 = jobLogMapper.updateHandleInfo(log);
        dto = jobLogMapper.load(log.getId());


        Page<Long> ret4 = jobLogMapper.findClearLogIds(new Page(0, 100), 1, 1, new Date(), 100 );

        int ret2 = jobLogMapper.delete(log.getJobId());

    }

}
