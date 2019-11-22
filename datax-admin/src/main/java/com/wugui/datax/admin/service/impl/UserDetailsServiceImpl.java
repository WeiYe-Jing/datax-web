package com.wugui.datax.admin.service.impl;

import com.wugui.datax.admin.entity.JwtUser;
import com.wugui.datax.admin.entity.XxlJobUser;
import com.wugui.datax.admin.mapper.XxlJobUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by echisan on 2018/6/23
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private XxlJobUserMapper xxlJobUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        XxlJobUser user = xxlJobUserMapper.loadByUserName(s);
        return new JwtUser(user);
    }

}
