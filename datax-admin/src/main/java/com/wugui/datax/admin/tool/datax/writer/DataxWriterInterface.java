package com.wugui.datax.admin.tool.datax.writer;

import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.DataXPluginInterface;

import java.util.Map;

/**
 * 用于构建writer的接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/7/30
 */
public interface DataxWriterInterface extends DataXPluginInterface {
    /**
     * 构建writer对象接口，各数据源各自实现接口
     *
     * @param dataxJsonDto
     * @param writerDatasource
     * @return {@link Map < String, Object>}
     * @author Locki
     * @date 2020/12/24
     */
    Map<String, Object> buildWriter(DataXJsonBuildDTO dataxJsonDto, JobDatasource writerDatasource);
}
