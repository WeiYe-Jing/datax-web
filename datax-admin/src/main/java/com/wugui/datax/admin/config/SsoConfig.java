//package com.wugui.datax.admin.config;
//
//import com.ada.sso.core.conf.Conf;
//import com.ada.sso.core.filter.SsoWebFilter;
//import com.ada.sso.core.util.JedisUtil;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author xuxueli 2018-11-15
// */
//@Configuration
//public class SsoConfig implements DisposableBean {
//
//
//    @Value("${ada.sso.server}")
//    private String adaSsoServer;
//
//    @Value("${ada.sso.logout.path}")
//    private String adaSsoLogoutPath;
//
//    @Value("${ada-sso.excluded.paths}")
//    private String adaSsoExcludedPaths;
//
//    @Value("${ada.sso.redis.address}")
//    private String adaSsoRedisAddress;
//
//
//    @Bean
//    public FilterRegistrationBean adaSsoFilterRegistration() {
//
//        JedisUtil.init(adaSsoRedisAddress);
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setName("AdaSsoWebFilter");
//        registration.setOrder(1);
//        registration.addUrlPatterns("/*");
//        registration.setFilter(new SsoWebFilter());
//        registration.addInitParameter(Conf.SSO_SERVER, adaSsoServer);
//        registration.addInitParameter(Conf.SSO_LOGOUT_PATH, adaSsoLogoutPath);
//        registration.addInitParameter(Conf.SSO_EXCLUDED_PATHS, adaSsoExcludedPaths);
//
//        return registration;
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        JedisUtil.close();
//    }
//
//}
