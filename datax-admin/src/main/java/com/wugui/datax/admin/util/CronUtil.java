package com.wugui.datax.admin.util;

import com.wugui.datax.admin.dto.TaskScheduleDto;

public class CronUtil {


    public static String createCronExpression(TaskScheduleDto dto) {
        StringBuffer cronExp = new StringBuffer();

        if (null == dto.getJobType()) {
            System.out.println("执行周期未配置");//执行周期未配置
        }

        if (null != dto.getSecond()
                && null == dto.getMinute()
                && null == dto.getHour()) {
            //每隔几秒
            if (dto.getJobType().intValue() == 0) {
                cronExp.append("0/").append(dto.getSecond());
                cronExp.append(" * * * * ?");
            }

        }

        if (null != dto.getSecond()
                && null != dto.getMinute()
                && null == dto.getHour()) {
            //每隔几分钟
            if (dto.getJobType().intValue() == 4) {
                cronExp.append("* ");
                cronExp.append("0/").append(dto.getMinute());
                cronExp.append(" * * * ?");
            }
        }

        if (null != dto.getSecond()
                && null != dto.getMinute()
                && null != dto.getHour()) {
            //秒
            cronExp.append(dto.getSecond()).append(" ");
            //分
            cronExp.append(dto.getMinute()).append(" ");
            //小时
            cronExp.append(dto.getHour()).append(" ");

            //每天
            if (dto.getJobType().intValue() == 1) {
                cronExp.append("* ");//日
                cronExp.append("* ");//月
                cronExp.append("?");//周
            }

            //按每周
            else if (dto.getJobType().intValue() == 3) {
                //一个月中第几天
                cronExp.append("? ");
                //月份
                cronExp.append("* ");
                //周
                Integer[] weeks = dto.getDayOfWeeks();
                for (int i = 0; i < weeks.length; i++) {
                    if (i == 0) {
                        cronExp.append(weeks[i]);
                    } else {
                        cronExp.append(",").append(weeks[i]);
                    }
                }

            }

            //按每月
            else if (dto.getJobType().intValue() == 2) {
                //一个月中的哪几天
                Integer[] days = dto.getDayOfMonths();
                for (int i = 0; i < days.length; i++) {
                    if (i == 0) {
                        cronExp.append(days[i]);
                    } else {
                        cronExp.append(",").append(days[i]);
                    }
                }
                //月份
                cronExp.append(" * ");
                //周
                cronExp.append("?");
            }

        } else {
            System.out.println("时或分或秒参数未配置");//时或分或秒参数未配置
        }
        return cronExp.toString();
    }

    /**
     * 方法摘要：生成计划的详细描述
     *
     * @param dto
     * @return String
     */
    public static String createDescription(TaskScheduleDto dto) {
        StringBuffer description = new StringBuffer();
        //计划执行开始时间

        if (null != dto.getSecond()
                && null != dto.getMinute()
                && null != dto.getHour()) {
            //按每天
            if (dto.getJobType().intValue() == 1) {
                description.append("每天");
                description.append(dto.getHour()).append("时");
                description.append(dto.getMinute()).append("分");
                description.append(dto.getSecond()).append("秒");
                description.append("执行");
            }

            //按每周
            else if (dto.getJobType().intValue() == 3) {
                if (dto.getDayOfWeeks() != null && dto.getDayOfWeeks().length > 0) {
                    String days = "";
                    for (int i : dto.getDayOfWeeks()) {
                        days += "周" + i;
                    }
                    description.append("每周的").append(days).append(" ");
                }
                if (null != dto.getSecond()
                        && null != dto.getMinute()
                        && null != dto.getHour()) {
                    description.append(",");
                    description.append(dto.getHour()).append("时");
                    description.append(dto.getMinute()).append("分");
                    description.append(dto.getSecond()).append("秒");
                }
                description.append("执行");
            }

            //按每月
            else if (dto.getJobType().intValue() == 2) {
                //选择月份
                if (dto.getDayOfMonths() != null && dto.getDayOfMonths().length > 0) {
                    String days = "";
                    for (int i : dto.getDayOfMonths()) {
                        days += i + "号";
                    }
                    description.append("每月的").append(days).append(" ");
                }
                description.append(dto.getHour()).append("时");
                description.append(dto.getMinute()).append("分");
                description.append(dto.getSecond()).append("秒");
                description.append("执行");
            }

        }
        return description.toString();
    }

    //参考例子
    public static void main(String[] args) {
        //执行时间：每天的12时12分12秒 start
        TaskScheduleDto dto = new TaskScheduleDto();

        dto.setJobType(0);//按每秒
        dto.setSecond(30);
        String cronExp = createCronExpression(dto);
        System.out.println(cronExp);

        dto.setJobType(4);//按每分钟
        dto.setMinute(8);
        String cronExpp = createCronExpression(dto);
        System.out.println(cronExpp);

        dto.setJobType(1);//按每天
        Integer hour = 12; //时
        Integer minute = 12; //分
        Integer second = 12; //秒
        dto.setHour(hour);
        dto.setMinute(minute);
        dto.setSecond(second);
        String cropExp = createCronExpression(dto);
        System.out.println(cropExp + ":" + createDescription(dto));
        //执行时间：每天的12时12分12秒 end

        dto.setJobType(3);//每周的哪几天执行
        Integer[] dayOfWeeks = new Integer[3];
        dayOfWeeks[0] = 1;
        dayOfWeeks[1] = 2;
        dayOfWeeks[2] = 3;
        dto.setDayOfWeeks(dayOfWeeks);
        cropExp = createCronExpression(dto);
        System.out.println(cropExp + ":" + createDescription(dto));

        dto.setJobType(2);//每月的哪几天执行
        Integer[] dayOfMonths = new Integer[3];
        dayOfMonths[0] = 1;
        dayOfMonths[1] = 21;
        dayOfMonths[2] = 13;
        dto.setDayOfMonths(dayOfMonths);
        cropExp = createCronExpression(dto);
        System.out.println(cropExp + ":" + createDescription(dto));

    }

}
