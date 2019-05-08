package com.wugui.dataxweb.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 通用的字段填充，如createBy createDate这些字段的自动填充
 * @author huzekang
 */
@Component
@Slf4j
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        //todo createBy
        Date date = new Date();
        this.setInsertFieldValByName("createDate", date, metaObject);
        this.setInsertFieldValByName("updateDate", date, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //todo updateBY
        log.debug("start update fill ....");
        Date date = new Date();
        this.setUpdateFieldValByName("updateDate", date, metaObject);
    }
}