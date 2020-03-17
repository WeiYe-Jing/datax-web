package com.wugui.datax.admin.config;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlus配置类 Spring boot方式
 *
 * @author huzekang
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.wugui.datax.admin.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor.setOverflow(true);
    }

    /**
     * MyBatisPlus逻辑删除 ，需要在 yml 中配置开启
     * 3.0.7.1版本的LogicSqlInjector里面什么都没做只是 extends DefaultSqlInjector
     * 以后版本直接去的了LogicSqlInjector
     *
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }


}
