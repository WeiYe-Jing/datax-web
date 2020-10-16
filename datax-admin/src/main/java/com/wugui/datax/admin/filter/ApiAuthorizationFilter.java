package com.wugui.datax.admin.filter;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.api.R;
import com.wugui.datax.admin.entity.JobUser;
import com.wugui.datax.admin.exception.TokenIsExpiredException;
import com.wugui.datax.admin.mapper.JobUserMapper;
import com.wugui.datax.admin.util.JwtTokenUtils;
import com.wugui.datax.admin.wrapper.RequestWrapper;
import com.wugui.datax.client.util.AppUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 第三方应用API接入鉴权
 *
 * @author Locki
 * @date 2020/9/28
 */
public class ApiAuthorizationFilter extends BasicAuthenticationFilter {

    private JobUserMapper jobUserMapper;

    public ApiAuthorizationFilter(AuthenticationManager authenticationManager, JobUserMapper jobUserMapper) {
        super(authenticationManager);
        this.jobUserMapper = jobUserMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        //优先取request body参数
        Map<String, String> map = JSON.parseObject(requestWrapper.getBodyString(), new TypeReference<Map<String, String>>() {
        });
        if (map == null) {
            map = new HashMap<>();
            //request body无参数才getParameter
            Enumeration<String> enums = requestWrapper.getParameterNames();
            while (enums.hasMoreElements()) {
                String key = enums.nextElement();
                map.put(key, requestWrapper.getParameter(key));
            }
        }
        if (map == null || map.isEmpty()) {
            chain.doFilter(requestWrapper, response);
            return;
        }
        String accessKey = map.get("accessKey");
        String sign = map.get("sign");
        String timestamp = map.get("timestamp");
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(sign) || StringUtils.isBlank(timestamp)) {
            //没有应用接入标识则直接放行
            chain.doFilter(requestWrapper, response);
            return;
        }
        long time = Long.parseLong(timestamp);
        if (!DateUtil.isExpired(new Date(time), DateField.MINUTE, 15, new Date())) {
            //有效期已过则直接放行
            chain.doFilter(requestWrapper, response);
            return;
        }

        JobUser jobUser = jobUserMapper.getUserByAccesskey(accessKey);
        if (jobUser == null) {
            //应用接入标识无效则直接放行
            chain.doFilter(requestWrapper, response);
            return;
        }
        String checkSign = AppUtil.sign(map, jobUser.getSecretKey());
        if (!sign.equals(checkSign)) {
            //签名验证不通过则直接放行
            chain.doFilter(requestWrapper, response);
            return;
        }

        // 如果请求头中有token，则进行解析，并且设置认证信息
        try {
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(jobUser, requestWrapper));
        } catch (TokenIsExpiredException e) {
            //返回json形式的错误信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(R.failed(e.getMessage())));
            response.getWriter().flush();
            return;
        }
        super.doFilterInternal(requestWrapper, response, chain);
    }

    // 获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(JobUser jobUser, RequestWrapper request) throws TokenIsExpiredException {
        String token = JwtTokenUtils.createToken(jobUser.getId(),jobUser.getUsername(), jobUser.getRole(), false);
        request.putHeader("Authorization", JwtTokenUtils.TOKEN_PREFIX + token);
        return new UsernamePasswordAuthenticationToken(jobUser.getUsername(), null,
                Collections.singleton(new SimpleGrantedAuthority(jobUser.getRole())));
    }

    public static void main(String[] args) {
        String s = "{\"alarmEmail\":\"1\",\"executorParam\":\"{\\\"type\\\":\\\" DB2\\\",\\\"url\\\":\\\"jdbc:db2://10.0.0.65:50000/DATANALY\\\",\\\"user\\\":\\\"db2admin\\\",\\\"code\\\":\\\"db2admin\\\",\\\"pros\\\":[\\\"PD_ZM_K_RLIC_D\\\",\\\"PD_ZM_K_CASEINFO_D(5)\\\"]}\",\"jobJson\":\"{\\\"type\\\":\\\" DB2\\\",\\\"url\\\":\\\"jdbc:db2://10.0.0.65:50000/DATANALY\\\",\\\"user\\\":\\\"db2admin\\\",\\\"code\\\":\\\"db2admin\\\",\\\"pros\\\":[\\\"PD_ZM_K_RLIC_D\\\",\\\"PD_ZM_K_CASEINFO_D(5)\\\"]}\",\"executorBlockStrategy\":\"SERIAL_EXECUTION\",\"author\":\"蒋洋\",\"executorRouteStrategy\":\"FIRST\",\"jobCron\":\"0 0 12 * * ? *\",\"sign\":\"8FEA9EA8FD200F94CF2596AEB071B608\",\"jobGroup\":\"1\",\"jobDesc\":\"system测试任务\",\"userId\":\"1\",\"accessKey\":\"vCTez6oi\",\"glueType\":\"JAVA_BEAN\",\"executorHandler\":\"procedureHandler\",\"executorFailRetryCount\":\"0\",\"executorTimeout\":\"0\",\"projectId\":null,\"timestamp\":\"1602745949221\"}";
        System.out.println(s);
        Map<String, String> map = JSON.parseObject(s, new TypeReference<Map<String, String>>(){});
        map.forEach((k, v) -> System.out.println("key=" + k + ";value=" + v));
    }
}
