package com.wugui.dataxweb.service;

import com.wugui.dataxweb.dto.DataxJsonDto;

/**
 * datax json构建服务层接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/8/1
 */
public interface DataxJsonService {

    /**
     * 用map接收
     *
     * @param dataxJsonDto
     * @return
     */
    String buildJobJson(DataxJsonDto dataxJsonDto);

}
