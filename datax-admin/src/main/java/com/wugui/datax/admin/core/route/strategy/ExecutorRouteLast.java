package com.wugui.datax.admin.core.route.strategy;

import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datax.admin.core.route.AbstractExecutorRouter;

import java.util.List;

/**
 * @author  xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends AbstractExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
