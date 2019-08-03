package com.wugui.dataxweb.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.wugui.dataxweb.service.JdbcDatasourceQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 查询数据库表名，字段的控制器
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DatabaseQueryController
 * @Version 1.0
 * @since 2019/7/31 20:48
 */
@RestController
@RequestMapping("api/jdbcDatasourceQuery")
@Api(tags = "jdbc数据库查询控制器")
public class JdbcDatasourceQueryController extends ApiController {

    @Autowired
    private JdbcDatasourceQueryService jdbcDatasourceQueryService;

    /**
     * 根据数据源id获取可用表名
     *
     * @param datasourceId
     * @return
     */
    @GetMapping("/getTables")
    @ApiOperation("根据数据源id获取可用表名")
    public R<List<String>> getTableNames(Long datasourceId) {
        return success(jdbcDatasourceQueryService.getTables(datasourceId));
    }

    /**
     * 根据数据源id和表名获取所有字段
     *
     * @param datasourceId 数据源id
     * @param tableName    表名
     * @return
     */
    @GetMapping("/getColumns")
    @ApiOperation("根据数据源id和表名获取所有字段")
    public R<List<String>> getColumns(Long datasourceId, String tableName) {
        return success(jdbcDatasourceQueryService.getColumns(datasourceId, tableName));
    }

    /**
     * 根据数据源id和sql语句获取所有字段
     *
     * @param datasourceId 数据源id
     * @param querySql     表名
     * @return
     */
    @GetMapping("/getColumnsByQuerySql")
    @ApiOperation("根据数据源id和sql语句获取所有字段")
    public R<List<String>> getColumnsByQuerySql(Long datasourceId, String querySql) {
        return success(jdbcDatasourceQueryService.getColumnsByQuerySql(datasourceId, querySql));
    }
}
