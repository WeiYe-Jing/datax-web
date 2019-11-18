package com.wugui.datax.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.wugui.datax.admin.log.LogResult;
import com.wugui.datax.admin.service.IJobLogService;
import com.wugui.datax.admin.util.PageUtils;
import com.wugui.datax.admin.entity.JobLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ProcessUtil
 *
 * @author jingwk
 * @version 1.0
 * @since 2019/11/10
 */

@RestController
@RequestMapping("log")
@Api(tags = "datax日志接口")
@Slf4j
public class JobLogController extends ApiController {

    @Autowired
    private IJobLogService iJobLogService;

    /**
     * 根据日志路径查看详情
     *
     * @param logFilePath
     * @param fromLineNum
     * @return
     */
    @ApiOperation("查看任务抽取日志,logFilePath为日志路径，fromLineNum为读取的行数")
    @GetMapping("/view")
    public R<LogResult> viewJobLog(String logFilePath, int fromLineNum) {
        return R.ok(iJobLogService.viewJobLog(logFilePath, fromLineNum));
    }

    /**
     * 分页查询所有运行数据
     *
     * @return 列表
     */
    @GetMapping("/list")
    @ApiOperation("分页查询所有运行数据")
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "query", dataType = "String", name = "current", value = "当前页", defaultValue = "1", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "size", value = "一页大小", defaultValue = "10", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "Boolean", name = "ifCount", value = "是否查询总数", defaultValue = "true"),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "ascs", value = "升序字段，多个用逗号分隔"),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "descs", value = "降序字段，多个用逗号分隔")
            })
    public R selectAll() {
        BaseForm<JobLog> baseForm = new BaseForm();
        return success(this.iJobLogService.page(baseForm.getPlusPagingQueryEntity(), pageQueryWrapperCustom(baseForm.getParameters())));
    }

    /**
     * 自定义查询组装
     *
     * @param map
     * @return
     */
    private QueryWrapper<JobLog> pageQueryWrapperCustom(Map<String, Object> map) {
        // mybatis plus 分页相关的参数
        Map<String, Object> pageHelperParams = PageUtils.filterPageParams(map);
        log.info("分页相关的参数: {}", pageHelperParams);
        //过滤空值，分页查询相关的参数
        Map<String, Object> columnQueryMap = PageUtils.filterColumnQueryParams(map);
        log.info("字段查询条件参数为: {}", columnQueryMap);
        QueryWrapper<JobLog> queryWrapper = new QueryWrapper<>();
        //排序 操作
        pageHelperParams.forEach((k, v) -> {
            switch (k) {
                case "ascs":
                    queryWrapper.orderByAsc(StrUtil.toUnderlineCase(StrUtil.toString(v)));
                    break;
                case "descs":
                    queryWrapper.orderByDesc(StrUtil.toUnderlineCase(StrUtil.toString(v)));
                    break;
            }
        });
        //遍历进行字段查询条件组装
        columnQueryMap.forEach((k, v) -> {
            queryWrapper.eq(StrUtil.toUnderlineCase(k), v);
        });
        return queryWrapper;
    }
}
