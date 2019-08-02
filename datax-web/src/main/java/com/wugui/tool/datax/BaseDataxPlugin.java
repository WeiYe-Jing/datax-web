package com.wugui.tool.datax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 抽象实现类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName BaseDataxPlugin
 * @Version 1.0
 * @since 2019/7/31 9:45
 */
public abstract class BaseDataxPlugin implements DataxPluginInterface {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Map<String, Object> extraParams;


    @Override
    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }

}
