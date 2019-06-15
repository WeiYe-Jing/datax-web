package com.wugui.dataxweb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wugui.dataxweb.entity.JobConfig;
import com.wugui.dataxweb.service.JobConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;



/**
 * 作业配置表控制层
 * @author huzekang@gz-yibo.com
 * @since 2019-05-20
 * @version v1.0
 */
@RestController
@RequestMapping("api/jobConfig")
@Api(tags = "作业配置表控制层")
public class JobConfigController extends ApiController {
    /**
     * 服务对象
     */
    @Autowired
    private JobConfigService jobConfigServiceImpl;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param jobConfig 查询实体
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation("分页查询所有数据")
    public R selectAll(Page<JobConfig> page, JobConfig jobConfig) {
        return success(this.jobConfigServiceImpl.page(page, new QueryWrapper<>(jobConfig)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public R<JobConfig> selectOne(@PathVariable Serializable id) {
        return success(this.jobConfigServiceImpl.getById(id));
    }

    /**
     * 新增数据
     *
     * @param jobConfig 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R<Boolean> insert(@RequestBody JobConfig jobConfig) {
        return success(this.jobConfigServiceImpl.save(jobConfig));
    }

    /**
     * 修改数据
     *
     * @param jobConfig 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PutMapping
    public R<Boolean> update(@RequestBody JobConfig jobConfig) {
        return success(this.jobConfigServiceImpl.updateById(jobConfig));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @DeleteMapping
    public R<Boolean> delete(@RequestParam("idList") List<Long> idList) {
        return success(this.jobConfigServiceImpl.removeByIds(idList));
    }
}