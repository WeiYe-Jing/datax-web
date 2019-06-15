package com.wugui.dataxweb.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableList;
import com.wugui.dataxweb.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础参数辅助类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/5/15
 */
@Slf4j
public class BaseForm<T> {
    /**
     * 查询参数对象
     */
    protected Map<String, Object> values = new LinkedHashMap<String, Object>();

    /**
     * 当前页码
     */
    private int pageNo = 1;

    /**
     * 页大小
     */
    private int pageSize = 10;

    /**
     * 构造方法
     */
    public BaseForm() {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            Enumeration<String> params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String name = params.nextElement();
                String value = StrUtil.trim(request.getParameter(name));
                this.set(name, URLDecoder.decode(value, "UTF-8"));
            }
            this.parsePagingQueryParams();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("BaseControlForm initialize parameters setting error：" + e);
        }
    }

    /**
     * 获取页码
     *
     * @return
     */
    public int getPageNo() {
        String pageNum = StrUtil.emptyToDefault(StrUtil.toString(this.get("pageNum")), StrUtil.toString(this.get("page")));

        if (!StrUtil.isEmpty(pageNum) && NumberUtil.isNumber(pageNum)) {
            this.pageNo = Integer.parseInt(pageNum);
        }
        return this.pageNo;
    }

    /**
     * 获取页大小
     *
     * @return
     */
    public int getPageSize() {
        String pageSize = StrUtil.emptyToDefault(StrUtil.toString(this.get("pageSize")), StrUtil.toString(this.get("rows")));

        if (!StrUtil.isEmpty(pageSize) || NumberUtil.isNumber(pageSize)) {
            this.pageSize = Integer.parseInt(pageSize);
        }
        return this.pageSize;
    }

    /**
     * 获得参数信息对象
     *
     * @return
     */
    public Map<String, Object> getParameters() {
        return values;
    }

    /**
     * 根据key获取values中的值
     *
     * @param name
     * @return
     */
    public Object get(String name) {
        if (values == null) {
            values = new LinkedHashMap<String, Object>();
            return null;
        }
        return this.values.get(name);
    }

    /**
     * 根据key获取values中String类型值
     *
     * @param key
     * @return String
     */
    public String getString(String key) {
        return StrUtil.toString(get(key));
    }

    /**
     * 获取排序字段
     *
     * @return
     */
    public String getSort() {
        return StrUtil.toString(this.values.get("sort"));
    }

    /**
     * 获取排序
     *
     * @return
     */
    public String getOrder() {
        return StrUtil.toString(this.values.get("order"));
    }

    /**
     * 获取排序
     *
     * @return
     */
    public String getOrderby() {
        return StrUtil.toString(this.values.get("orderby"));
    }

    /**
     * 解析出mybatis plus分页查询参数
     */
    public Page getPlusPagingQueryEntity() {
        Page page = new Page();
        //如果无current，默认返回1000条数据
        page.setCurrent(Long.valueOf(StrUtil.toString(ObjectUtil.defaultIfNull(this.get("current"), "0"))));
        page.setSize(Long.valueOf(StrUtil.toString(ObjectUtil.defaultIfNull(this.get("size"), "1000"))));
        if (ObjectUtil.isNotNull(this.get("ifCount"))) {
            page.setSearchCount(BooleanUtil.toBoolean(this.getString("ifCount")));
        } else {
            //默认给true
            page.setSearchCount(true);
        }

        //排序字段
        //需要转为驼峰
        List<String> ascs = CollUtil.toList(this.getString("ascs").split(","));
        ImmutableList.Builder finalBuilderAscs = new ImmutableList.Builder();
        ascs.forEach(e -> finalBuilderAscs.add(StrUtil.replace(e, "([A-Z])", parameters -> "_" + parameters.group().toLowerCase())));
        page.setAscs(finalBuilderAscs.build());

        ImmutableList.Builder finalBuilderDescs = new ImmutableList.Builder();
        List<String> descs = CollUtil.toList(this.getString("descs").split(","));
        descs.forEach(e -> finalBuilderDescs.add(StrUtil.replace(e, "([A-Z])", parameters -> "_" + parameters.group().toLowerCase())));
        page.setDescs(finalBuilderDescs.build());
        return page;
    }

    /**
     * 解析分页排序参数（pageHelper）
     */
    public void parsePagingQueryParams() {
        // 排序字段解析
        String orderby = StrUtil.toString(this.get("orderby")).trim();
        String sortName = StrUtil.toString(this.get("sort")).trim();
        String sortOrder = StrUtil.toString(this.get("order")).trim().toLowerCase();

        if (StrUtil.isEmpty(orderby) && !StrUtil.isEmpty(sortName)) {
            if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
                sortOrder = "asc";
            }
            this.set("orderby", sortName + " " + sortOrder);
        }
    }


    /**
     * 设置参数
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public void set(String name, Object value) {
        if (ObjectUtil.isNotNull(value)) {
            this.values.put(name, value);
        }
    }

    /**
     * 移除参数
     *
     * @param name
     */
    public void remove(String name) {
        this.values.remove(name);
    }

    /**
     * 清除所有参数
     */
    public void clear() {
        if (values != null) {
            values.clear();
        }
    }

}