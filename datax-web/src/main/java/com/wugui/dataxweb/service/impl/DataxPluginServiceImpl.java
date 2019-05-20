package com.wugui.dataxweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.dataxweb.dao.DataxPluginDao;
import com.wugui.dataxweb.entity.DataxPlugin;
import com.wugui.dataxweb.service.DataxPluginService;
import org.springframework.stereotype.Service;

/**
 * datax插件信息表服务实现类
 * @author huzekang@gz-yibo.com
 * @since 2019-05-20
 * @version v1.0
 */
@Service("dataxPluginService")
public class DataxPluginServiceImpl extends ServiceImpl<DataxPluginDao, DataxPlugin> implements DataxPluginService {

}