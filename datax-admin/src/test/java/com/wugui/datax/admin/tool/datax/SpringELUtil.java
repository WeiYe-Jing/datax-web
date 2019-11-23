package com.wugui.datax.admin.tool.datax;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Date;

public class SpringELUtil {
    public static void main(String[] args) {
        // 定义变量
        EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
        String date="2019-11-22";
        context.setVariable("today", new Date());                        // 为了让表达式可以访问该对象, 先把对象放到上下文中
        ExpressionParser parser = new SpelExpressionParser();
        Date a = parser.parseExpression("#today").getValue(context, Date.class);   // Tom , 使用变量
        System.out.println(a);
    }
}
