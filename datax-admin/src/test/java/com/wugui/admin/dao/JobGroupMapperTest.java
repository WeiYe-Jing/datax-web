package com.wugui.admin.dao;

import com.wugui.datax.admin.DataXAdminApplication;
import com.wugui.datax.admin.entity.JobGroup;
import com.wugui.datax.admin.mapper.JobGroupMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = DataXAdminApplication.class)
public class JobGroupMapperTest {

    @Resource
    private JobGroupMapper jobGroupMapper;

    @Test
    public void test(){
//        List<JobGroup> list = jobGroupMapper.findAll();
//
//        List<JobGroup> list2 = jobGroupMapper.findByAddressType(0);

        JobGroup group = new JobGroup();
        group.setAppName("setAppName");
        group.setTitle("setTitle");
        group.setOrderSn(1);
        group.setAddressType(0);
        group.setAddressList("setAddressList");

        int ret = jobGroupMapper.insert(group);

//        JobGroup group2 = jobGroupMapper.load(group.getId());
//        group2.setAppName("setAppName2");
//        group2.setTitle("setTitle2");
//        group2.setOrder(2);
//        group2.setAddressType(2);
//        group2.setAddressList("setAddressList2");
//
//        int ret2 = jobGroupMapper.update(group2);
//
//        int ret3 = jobGroupMapper.remove(group.getId());
    }

}
