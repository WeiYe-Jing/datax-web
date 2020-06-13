package com.wugui.datax.admin.util;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.wugui.datatx.core.util.Constants.SPLIT_COMMA;

/**
 * Created by jingwk on 2019/12/01
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "datax_admin";
    private static final String ISS = "admin";

    // 角色的key
    private static final String ROLE_CLAIMS = "rol";

    // 过期时间是3600秒，既是24个小时
    private static final long EXPIRATION = 86400L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 7 * EXPIRATION;

    // 创建token
    public static String createToken(Integer id, String username, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(id + SPLIT_COMMA + username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 从token中获取用户名
    public static String getUsername(String token) {
        List<String> userInfo = Arrays.asList(getTokenBody(token).getSubject().split(SPLIT_COMMA));
        return userInfo.get(1);
    }

    // 从token中获取用户名
    public static Integer getUserId(String token) {
        String s= JSON.toJSONString(getTokenBody(token).getSubject());
        List<String> userInfo = Arrays.asList(getTokenBody(token).getSubject().split(SPLIT_COMMA));
        return Integer.parseInt(userInfo.get(0));
    }

    // 获取用户角色
    public static String getUserRole(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 是否已过期
    public static boolean isExpiration(String token) {
        try {
            return getTokenBody(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
