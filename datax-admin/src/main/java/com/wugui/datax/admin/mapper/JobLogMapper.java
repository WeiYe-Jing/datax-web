package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface JobLogMapper {


    /**
     * exist jobId not use jobGroup, not exist use jobGroup
     *
     * @param offset
     * @param pagesize
     * @param jobGroup
     * @param jobId
     * @param triggerTimeStart
     * @param triggerTimeEnd
     * @param logStatus
     * @return
     */
    List<JobLog> pageList(@Param("offset") int offset,
                          @Param("pagesize") int pagesize,
                          @Param("jobGroup") int jobGroup,
                          @Param("jobId") int jobId,
                          @Param("triggerTimeStart") Date triggerTimeStart,
                          @Param("triggerTimeEnd") Date triggerTimeEnd,
                          @Param("logStatus") int logStatus);

    /**
     * @param offset
     * @param pagesize
     * @param jobGroup
     * @param jobId
     * @param triggerTimeStart
     * @param triggerTimeEnd
     * @param logStatus
     * @return
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pagesize") int pagesize,
                      @Param("jobGroup") int jobGroup,
                      @Param("jobId") int jobId,
                      @Param("triggerTimeStart") Date triggerTimeStart,
                      @Param("triggerTimeEnd") Date triggerTimeEnd,
                      @Param("logStatus") int logStatus);

    /**
     * @param id
     * @return
     */
    JobLog load(@Param("id") long id);

    /**
     * @param jobLog
     * @return
     */
    long save(JobLog jobLog);

    /**
     * @param jobLog
     * @return
     */
    int updateTriggerInfo(JobLog jobLog);

    /**
     * @param jobLog
     * @return
     */
    int updateHandleInfo(JobLog jobLog);

    /**
     * @param id
     * @param processId
     * @return
     */
    int updateProcessId(@Param("id") long id,
                        @Param("processId") String processId);

    int delete(@Param("jobId") int jobId);

    Map<String, Object> findLogReport(@Param("from") Date from,
                                      @Param("to") Date to);

    List<Long> findClearLogIds(@Param("jobGroup") int jobGroup,
                               @Param("jobId") int jobId,
                               @Param("clearBeforeTime") Date clearBeforeTime,
                               @Param("clearBeforeNum") int clearBeforeNum,
                               @Param("pagesize") int pagesize);

    int clearLog(@Param("logIds") List<Long> logIds);

    List<Long> findFailJobLogIds(@Param("pagesize") int pagesize);

    int updateAlarmStatus(@Param("logId") long logId,
                          @Param("oldAlarmStatus") int oldAlarmStatus,
                          @Param("newAlarmStatus") int newAlarmStatus);

}
