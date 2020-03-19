package com.wugui.datax.admin.dto;

public class TaskScheduleDto {


    /**
     * 所选作业类型:
     * 1  -> 每天
     * 2  -> 每月
     * 3  -> 每周
     */
    Integer jobType;

    /**
     * 一周的哪几天
     */
    Integer[] dayOfWeeks;

    /**
     * 一个月的哪几天
     */
    Integer[] dayOfMonths;

    /**
     * 秒
     */
    Integer second;

    /**
     * 分
     */
    Integer minute;

    /**
     * 时
     */
    Integer hour;

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer[] getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(Integer[] dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    public Integer[] getDayOfMonths() {
        return dayOfMonths;
    }

    public void setDayOfMonths(Integer[] dayOfMonths) {
        this.dayOfMonths = dayOfMonths;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}
