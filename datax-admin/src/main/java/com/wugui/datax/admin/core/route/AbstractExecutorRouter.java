package com.wugui.datax.admin.core.route;

import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xuxueli on 17/3/10.
 */
public abstract class AbstractExecutorRouter {

    protected static Logger logger = LoggerFactory.getLogger(AbstractExecutorRouter.class);

    /**
     * 执行器路由
     *
     * @param triggerParam
     * @param addressList
     * @return
     */
    public abstract ReturnT<String> route(TriggerParam triggerParam, List<String> addressList);

}
