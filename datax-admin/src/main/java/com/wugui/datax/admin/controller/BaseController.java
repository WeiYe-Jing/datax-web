package com.wugui.datax.admin.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.wugui.datax.admin.util.JwtTokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static com.wugui.datatx.core.util.Constants.STRING_BLANK;

/**
 * base controller
 */
public class BaseController extends ApiController {

    public Integer getCurrentUserId(HttpServletRequest request) {
        Enumeration<String> auth = request.getHeaders(JwtTokenUtils.TOKEN_HEADER);
        String token = auth.nextElement().replace(JwtTokenUtils.TOKEN_PREFIX, STRING_BLANK);
        return JwtTokenUtils.getUserId(token);
    }
}