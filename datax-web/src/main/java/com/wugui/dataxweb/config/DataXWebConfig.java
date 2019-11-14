package com.wugui.dataxweb.config;

import com.wugui.dataxweb.service.IJobLogService;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * datax-web config
 *
 * @author jingwk 2019-11-10
 */
@Component
@Getter
public class DataXWebConfig implements InitializingBean {
    private static DataXWebConfig dataXWebConfig = null;

    public static DataXWebConfig getDataXWebConfig() {
        return dataXWebConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dataXWebConfig=this;
    }

    @Autowired
    private IJobLogService jobLogService;
}
