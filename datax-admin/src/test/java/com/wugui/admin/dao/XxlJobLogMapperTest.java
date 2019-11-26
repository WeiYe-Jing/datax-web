package com.wugui.admin.dao;

import com.wugui.datax.admin.entity.XxlJobLog;
import com.wugui.datax.admin.mapper.XxlJobLogMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobLogMapperTest {

    @Resource
    private XxlJobLogMapper xxlJobLogMapper;

    @Test
    public void test(){
        List<XxlJobLog> list = xxlJobLogMapper.pageList(0, 10, 1, 1, null, null, 1);
        int list_count = xxlJobLogMapper.pageListCount(0, 10, 1, 1, null, null, 1);

        XxlJobLog log = new XxlJobLog();
        log.setJobGroup(1);
        log.setJobId(1);

        long ret1 = xxlJobLogMapper.save(log);
        XxlJobLog dto = xxlJobLogMapper.load(log.getId());

        log.setTriggerTime(new Date());
        log.setTriggerCode(1);
        log.setTriggerMsg("1");
        log.setExecutorAddress("1");
        log.setExecutorHandler("1");
        log.setExecutorParam("1");
        ret1 = xxlJobLogMapper.updateTriggerInfo(log);
        dto = xxlJobLogMapper.load(log.getId());


        log.setHandleTime(new Date());
        log.setHandleCode(2);
        log.setHandleMsg("2");
        ret1 = xxlJobLogMapper.updateHandleInfo(log);
        dto = xxlJobLogMapper.load(log.getId());


        List<Long> ret4 = xxlJobLogMapper.findClearLogIds(1, 1, new Date(), 100, 100);

        int ret2 = xxlJobLogMapper.delete(log.getJobId());

    }

}
