package com.wugui.datax.executor.service.jobhandler;

/**
 * DataX启动参数
 *
 * @author jingwk 2019-12-15
 */
class DataXOptionConstant {

    static final String SPLIT_SPACE = " ";

    static final String TRANSFORM_SPLIT_SPACE = "\" \"";

    static final String TRANSFORM_QUOTES = "\"";

    static final String JVM_CM = "-j";

    static final String PARAMS_CM = "-p";

    static final String PARAMS_CM_V_PT = "-Dpartition=%s";

    static final String DEFAULT_JSON = "jsons";

    static final String DEFAULT_DATAX_PY = "datax.py";

    static final String TASK_START_TIME_SUFFIX = "任务启动时刻";
    static final String TASK_END_TIME_SUFFIX = "任务结束时刻";

    static final String TASK_TOTAL_TIME_SUFFIX = "任务总计耗时";
    static final String TASK_AVERAGE_FLOW_SUFFIX = "任务平均流量";
    static final String TASK_RECORD_WRITING_SPEED_SUFFIX = "记录写入速度";
    static final String TASK_RECORD_READER_NUM_SUFFIX = "读出记录总数";
    static final String TASK_RECORD_WRITING_NUM_SUFFIX = "读写失败总数";
}
