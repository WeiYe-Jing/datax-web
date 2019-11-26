package com.wugui.admin.dao;

import com.wugui.datax.admin.entity.XxlJobGroup;
import com.wugui.datax.admin.mapper.XxlJobGroupMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobGroupMapperTest {

    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;

    @Test
    public void test(){
        List<XxlJobGroup> list = xxlJobGroupMapper.findAll();

        List<XxlJobGroup> list2 = xxlJobGroupMapper.findByAddressType(0);

        XxlJobGroup group = new XxlJobGroup();
        group.setAppName("setAppName");
        group.setTitle("setTitle");
        group.setOrder(1);
        group.setAddressType(0);
        group.setAddressList("setAddressList");

        int ret = xxlJobGroupMapper.save(group);

        XxlJobGroup group2 = xxlJobGroupMapper.load(group.getId());
        group2.setAppName("setAppName2");
        group2.setTitle("setTitle2");
        group2.setOrder(2);
        group2.setAddressType(2);
        group2.setAddressList("setAddressList2");

        int ret2 = xxlJobGroupMapper.update(group2);

        int ret3 = xxlJobGroupMapper.remove(group.getId());
    }

}
