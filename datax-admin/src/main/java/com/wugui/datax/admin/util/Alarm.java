package com.wugui.datax.admin.util;

/**
 * Copyright(C),datax-all
 * Author:   凌云[heyf@yoozoo.com]
 * Version:  v1.0.0
 * Date:     2020/4/14 11:07 上午
 * Description: null.java
 */
public interface Alarm {
    String receives = null;
    String subject = null;
    String content = null;
    Boolean send();
}
