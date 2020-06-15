package com.wugui.datax.admin.service;

import com.wugui.datax.admin.dto.DataXJsonBuildDto;

/**
 * com.wugui.datax json构建服务层接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/8/1
 */
public interface DataxJsonService {

    /**
     * build datax json
     *
     * @param dto
     * @return
     */
    String buildJobJson(DataXJsonBuildDto dto);
}
