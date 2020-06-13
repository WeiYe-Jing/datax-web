package com.wugui.datax.admin.controller;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wugui.datax.admin.util.PageUtils;
import com.wugui.datax.admin.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基础参数辅助类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/5/15
 */
@Slf4j
public class BaseForm {
    /**
     * 查询参数对象
     */
    protected Map<String, Object> values = new LinkedHashMap<>();

    /**
     * 当前页码
     */
    private Long current = 1L;

    /**
     * 页大小
     */
    private Long size = 10L;

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
    public Long getPageNo() {
        String pageNum = StrUtil.toString(this.get("current"));
        if (!StrUtil.isEmpty(pageNum) && NumberUtil.isNumber(pageNum)) {
            this.current = Long.parseLong(pageNum);
        }
        return this.current;
    }

    /**
     * 获取页大小
     *
     * @return
     */
    public Long getPageSize() {
        String pageSize = StrUtil.toString(this.get("size"));

        if (StrUtil.isNotEmpty(pageSize) && NumberUtil.isNumber(pageSize) && !"null".equalsIgnoreCase(pageSize)) {
            this.size = Long.parseLong(pageSize);
        }
        return this.size;
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
            values = new LinkedHashMap<>();
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
        page.setCurrent(this.getPageNo());
        page.setSize(this.getPageSize());
        if (ObjectUtil.isNotNull(this.get("ifCount"))) {
            page.setSearchCount(BooleanUtil.toBoolean(this.getString("ifCount")));
        } else {
            //默认给true
            page.setSearchCount(true);
        }
        return page;
    }

    /**
     * 解析分页排序参数（pageHelper）
     */
    public void parsePagingQueryParams() {
        // 排序字段解析
        String orderBy = StrUtil.toString(this.get("orderby")).trim();
        String sortName = StrUtil.toString(this.get("sort")).trim();
        String sortOrder = StrUtil.toString(this.get("order")).trim().toLowerCase();

        if (StrUtil.isEmpty(orderBy) && !StrUtil.isEmpty(sortName)) {
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


    /**
     * 自定义查询组装
     *
     * @param map
     * @return
     */
    protected QueryWrapper<?> pageQueryWrapperCustom(Map<String, Object> map, QueryWrapper<?> queryWrapper) {
        // mybatis plus 分页相关的参数
        Map<String, Object> pageParams = PageUtils.filterPageParams(map);
        //过滤空值，分页查询相关的参数
        Map<String, Object> colQueryMap = PageUtils.filterColumnQueryParams(map);
        //排序 操作
        pageParams.forEach((k, v) -> {
            switch (k) {
                case "ascs":
                    queryWrapper.orderByAsc(StrUtil.toUnderlineCase(StrUtil.toString(v)));
                    break;
                case "descs":
                    queryWrapper.orderByDesc(StrUtil.toUnderlineCase(StrUtil.toString(v)));
                    break;
            }
        });

        //遍历进行字段查询条件组装
        colQueryMap.forEach((k, v) -> {
            switch (k) {
                case "pluginName":
                case "datasourceName":
                    queryWrapper.like(StrUtil.toUnderlineCase(k), v);
                    break;
                default:
                    queryWrapper.eq(StrUtil.toUnderlineCase(k), v);
            }
        });

        return queryWrapper;
    }

}