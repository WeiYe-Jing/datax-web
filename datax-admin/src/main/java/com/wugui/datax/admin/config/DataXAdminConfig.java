package com.wugui.datax.admin.config;

import com.wugui.datax.admin.service.IJobLogService;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * com.wugui.datax-web config
 *
 * @author jingwk 2019-11-10
 */
@Component
@Getter
public class DataXAdminConfig implements InitializingBean {
    private static DataXAdminConfig dataXAdminConfig = null;

    public static DataXAdminConfig getDataXAdminConfig() {
        return dataXAdminConfig;
    }

    @Override
    public void afterPropertiesSet() {
        dataXAdminConfig =this;
    }

    @Autowired
    private IJobLogService jobLogService;
}
