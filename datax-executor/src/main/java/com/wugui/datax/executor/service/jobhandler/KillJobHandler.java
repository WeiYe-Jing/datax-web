package com.wugui.datax.executor.service.jobhandler;


import cn.hutool.core.io.FileUtil;
import com.wugui.datatx.core.biz.model.ReturnT;
import com.wugui.datatx.core.biz.model.TriggerParam;
import com.wugui.datatx.core.handler.AbstractJobHandler;
import com.wugui.datatx.core.handler.annotation.JobHandler;
import com.wugui.datatx.core.util.ProcessUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;

/**
 * DataX任务终止
 *
 * @author jingwk 2019-12-16
 */

@JobHandler(value = "killJobHandler")
@Component
public class KillJobHandler extends AbstractJobHandler {

    @Override
    public ReturnT<String> execute(TriggerParam tgParam) {
        final String processId = tgParam.getProcessId();
        boolean result = ProcessUtil.killProcessByPid(processId);
        //  删除临时文件
        if (!CollectionUtils.isEmpty(JOB_TEM_FILES)) {
            String pathname = JOB_TEM_FILES.get(processId);
            if (pathname != null) {
                FileUtil.del(new File(pathname));
                JOB_TEM_FILES.remove(processId);
            }
        }
        return result ? AbstractJobHandler.SUCCESS : AbstractJobHandler.FAIL;
    }
}
