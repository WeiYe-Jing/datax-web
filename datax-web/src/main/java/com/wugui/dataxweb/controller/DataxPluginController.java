package com.wugui.dataxweb.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wugui.dataxweb.entity.DataxPlugin;
import com.wugui.dataxweb.service.DataxPluginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * datax插件信息表控制层
 * @author huzekang@gz-yibo.com
 * @since 2019-05-20
 * @version v1.0
 */
@RestController
@RequestMapping("dataxPlugin")
@Api(tags = "datax插件信息表控制层")
public class DataxPluginController extends ApiController {
    /**
     * 服务对象
     */
    @Autowired
    private DataxPluginService dataxPluginService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param dataxPlugin 查询实体
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation("分页查询所有数据")
    public R<IPage<DataxPlugin>> selectAll(Page<DataxPlugin> page, DataxPlugin dataxPlugin) {
        return success(this.dataxPluginService.page(page, new QueryWrapper<>(dataxPlugin)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public R<DataxPlugin> selectOne(@PathVariable Serializable id) {
        return success(this.dataxPluginService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dataxPlugin 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PostMapping
    public R<Boolean> insert(@RequestBody DataxPlugin dataxPlugin) {
        return success(this.dataxPluginService.save(dataxPlugin));
    }

    /**
     * 修改数据
     *
     * @param dataxPlugin 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation("修改数据")
    public R<Boolean> update(@RequestBody DataxPlugin dataxPlugin) {
        return success(this.dataxPluginService.updateById(dataxPlugin));
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
        return success(this.dataxPluginService.removeByIds(idList));
    }
}