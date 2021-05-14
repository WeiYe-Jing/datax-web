package com.wugui.datax.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DatasourceQueryService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.util.AesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

import static com.wugui.datax.admin.tool.query.DriverConnectionFactory.buildParameter;


/**
 * jdbc数据源配置控制器层
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-07-30
 */
@RestController
@RequestMapping("/api/jobJdbcDatasource")
@Api(tags = "jdbc数据源配置接口")
public class JobDatasourceController extends BaseController {

    @Autowired
    private JobDatasourceService jobDatasourceService;

    @Autowired
    private DatasourceQueryService datasourceQueryService;

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation("分页查询所有数据")
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "query", dataType = "String", name = "current", value = "当前页", defaultValue = "1", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "size", value = "一页大小", defaultValue = "10", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "Boolean", name = "ifCount", value = "是否查询总数", defaultValue = "true"),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "ascs", value = "升序字段，多个用逗号分隔"),
                    @ApiImplicitParam(paramType = "query", dataType = "String", name = "descs", value = "降序字段，多个用逗号分隔")
            })
    public R<IPage<JobDatasource>> selectAll() {
        BaseForm form = new BaseForm();
        QueryWrapper<JobDatasource> query = (QueryWrapper<JobDatasource>) form.pageQueryWrapperCustom(form.getParameters(), new QueryWrapper<JobDatasource>());
        return success(jobDatasourceService.page(form.getPlusPagingQueryEntity(), query));
    }

    /**
     * 获取所有数据源
     *
     * @return
     */
    @ApiOperation("获取所有数据源")
    @GetMapping("/all")
    public R<List<JobDatasource>> selectAllDatasource() {
        return success(this.jobDatasourceService.selectAllDatasource());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public R<JobDatasource> selectOne(@PathVariable Serializable id) {
        JobDatasource datasource = this.jobDatasourceService.getById(id);
        BaseDataSource baseDataSource = JSONUtils.parseObject(datasource.getConnectionParams(), datasource.getType().getClazz());
        baseDataSource.setUser(AesUtil.decrypt(baseDataSource.getUser()));
        baseDataSource.setPassword(AesUtil.decrypt(baseDataSource.getPassword()));
        datasource.setConnectionParams(JSONUtils.toJsonString(baseDataSource));
        return success(datasource);
    }

    /**
     * 新增数据
     *
     * @param user
     * @param password
     * @param type
     * @param jdbcUrl
     * @param principal
     * @param database
     * @param comments
     * @param other
     * @return
     */
    @ApiOperation("新增数据")
    @PostMapping("/create")
    public R<Integer> createDataSource(@RequestParam(value = "datasourceName", required = false) String datasourceName,
                                       @RequestParam(value = "datasourceGroup", required = false) String datasourceGroup,
                                       @RequestParam(value = "status", required = false, defaultValue = "1") int status,
                                       @RequestParam(value = "user", required = false) String user,
                                       @RequestParam(value = "password", required = false) String password,
                                       @RequestParam(value = "type", required = false) DbType type,
                                       @RequestParam(value = "jdbcUrl", required = false) String jdbcUrl,
                                       @RequestParam(value = "principal", required = false) String principal,
                                       @RequestParam(value = "database", required = false) String database,
                                       @RequestParam(value = "comments", required = false) String comments,
                                       @RequestParam(value = "other", required = false) String other) {
        String parameter = buildParameter(user, password, type, database, jdbcUrl, principal, comments);
        return success(jobDatasourceService.createDataSource(datasourceName, datasourceGroup, type, status, comments, parameter));
    }

    /**
     * 修改数据
     *
     * @param datasourceName
     * @param datasourceGroup
     * @param status
     * @param user
     * @param password
     * @param type
     * @param jdbcUrl
     * @param principal
     * @param database
     * @param comments
     * @param other
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("修改数据")
    public R<Integer> updateDataSource(@RequestParam("id") long id,
                                       @RequestParam(value = "datasourceName", required = false) String datasourceName,
                                       @RequestParam(value = "datasourceGroup", required = false) String datasourceGroup,
                                       @RequestParam(value = "status", required = false,defaultValue = "1") int status,
                                       @RequestParam(value = "user", required = false) String user,
                                       @RequestParam(value = "password", required = false) String password,
                                       @RequestParam(value = "type", required = false) DbType type,
                                       @RequestParam(value = "jdbcUrl", required = false) String jdbcUrl,
                                       @RequestParam(value = "principal", required = false) String principal,
                                       @RequestParam(value = "database", required = false) String database,
                                       @RequestParam(value = "comments", required = false) String comments,
                                       @RequestParam(value = "other", required = false) String other) {
        String parameter = buildParameter(user, password, type, database, jdbcUrl, principal, comments);
        return success(this.jobDatasourceService.updateDataSource(id, datasourceName, datasourceGroup, type, status, comments, parameter));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation("删除数据")
    public R<Boolean> delete(@RequestParam("idList") List<Long> idList) {
        return success(this.jobDatasourceService.removeByIds(idList));
    }


    /**
     * 测试数据源
     *
     * @param
     * @return
     */
    @PostMapping("/test")
    @ApiOperation("测试数据")
    public R<Boolean> dataSourceTest(@RequestParam(value = "user", required = false) String user,
                                     @RequestParam(value = "password", required = false) String password,
                                     @RequestParam(value = "type", required = false) DbType type,
                                     @RequestParam(value = "jdbcUrl", required = false) String jdbcUrl,
                                     @RequestParam(value = "principal", required = false) String principal,
                                     @RequestParam(value = "database", required = false) String database,
                                     @RequestParam(value = "comments", required = false) String comments,
                                     @RequestParam(value = "other", required = false) String other) {
        String parameter = buildParameter(user, password, type, database, jdbcUrl, principal, comments);
        return success(datasourceQueryService.checkConnection(type, parameter));
    }

}