package com.wugui.datax.admin.core.thread;

import com.wugui.datax.admin.core.conf.JobAdminConfig;
import com.wugui.datax.admin.entity.JobLogReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * job log report helper
 *
 * @author xuxueli 2019-11-22
 */
public class JobLogReportHelper {
    private static Logger logger = LoggerFactory.getLogger(JobLogReportHelper.class);

    private static JobLogReportHelper instance = new JobLogReportHelper();
    public static JobLogReportHelper getInstance(){
        return instance;
    }


    private Thread logrThread;
    private volatile boolean toStop = false;
    public void start(){
        logrThread = new Thread(() -> {
            // last clean log time
            long lastCleanLogTime = 0;
            while (!toStop) {
                // 1、log-report refresh: refresh log report in 3 days
                try {
                    for (int i = 0; i < 3; i++) {
                        Calendar itemDay = Calendar.getInstance();
                        itemDay.add(Calendar.DAY_OF_MONTH, -i);
                        itemDay.set(Calendar.HOUR_OF_DAY, 0);
                        itemDay.set(Calendar.MINUTE, 0);
                        itemDay.set(Calendar.SECOND, 0);
                        itemDay.set(Calendar.MILLISECOND, 0);
                        Date todayFrom = itemDay.getTime();
                        itemDay.set(Calendar.HOUR_OF_DAY, 23);
                        itemDay.set(Calendar.MINUTE, 59);
                        itemDay.set(Calendar.SECOND, 59);
                        itemDay.set(Calendar.MILLISECOND, 999);
                        Date todayTo = itemDay.getTime();
                        // refresh log-report every minute
                        JobLogReport xxlJobLogReport = new JobLogReport();
                        xxlJobLogReport.setTriggerDay(todayFrom);
                        xxlJobLogReport.setRunningCount(0);
                        xxlJobLogReport.setSucCount(0);
                        xxlJobLogReport.setFailCount(0);
                        Map<String, Object> triggerCountMap = JobAdminConfig.getAdminConfig().getJobLogMapper().findLogReport(todayFrom, todayTo);
                        if (triggerCountMap!=null && triggerCountMap.size()>0) {
                            int triggerDayCount = triggerCountMap.containsKey("triggerDayCount")? Integer.valueOf(String.valueOf(triggerCountMap.get("triggerDayCount"))):0;
                            int triggerDayCountRunning = triggerCountMap.containsKey("triggerDayCountRunning")? Integer.valueOf(String.valueOf(triggerCountMap.get("triggerDayCountRunning"))):0;
                            int triggerDayCountSuc = triggerCountMap.containsKey("triggerDayCountSuc")? Integer.valueOf(String.valueOf(triggerCountMap.get("triggerDayCountSuc"))):0;
                            int triggerDayCountFail = triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;

                            xxlJobLogReport.setRunningCount(triggerDayCountRunning);
                            xxlJobLogReport.setSucCount(triggerDayCountSuc);
                            xxlJobLogReport.setFailCount(triggerDayCountFail);
                        }
                        // do refresh
                        int ret = JobAdminConfig.getAdminConfig().getJobLogReportMapper().update(xxlJobLogReport);
                        if (ret < 1) {
                            JobAdminConfig.getAdminConfig().getJobLogReportMapper().save(xxlJobLogReport);
                        }
                    }
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(">>>>>>>>>>> datax-web, job log report thread error:{}", e);
                    }
                }
                // 2、log-clean: switch open & once each day
                if (JobAdminConfig.getAdminConfig().getLogretentiondays()>0
                        && System.currentTimeMillis() - lastCleanLogTime > 24*60*60*1000) {
                    // expire-time
                    Calendar expiredDay = Calendar.getInstance();
                    expiredDay.add(Calendar.DAY_OF_MONTH, -1 * JobAdminConfig.getAdminConfig().getLogretentiondays());
                    expiredDay.set(Calendar.HOUR_OF_DAY, 0);
                    expiredDay.set(Calendar.MINUTE, 0);
                    expiredDay.set(Calendar.SECOND, 0);
                    expiredDay.set(Calendar.MILLISECOND, 0);
                    Date clearBeforeTime = expiredDay.getTime();
                    // clean expired log
                    List<Long> logIds = null;
                    do {
                        logIds = JobAdminConfig.getAdminConfig().getJobLogMapper().findClearLogIds(0, 0, clearBeforeTime, 0, 1000);
                        if (logIds!=null && logIds.size()>0) {
                            JobAdminConfig.getAdminConfig().getJobLogMapper().clearLog(logIds);
                        }
                    } while (logIds!=null && logIds.size()>0);

                    // update clean time
                    lastCleanLogTime = System.currentTimeMillis();
                }
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            logger.info(">>>>>>>>>>> datax-web, job log report thread stop");
        });
        logrThread.setDaemon(true);
        logrThread.setName("datax-web, admin JobLogReportHelper");
        logrThread.start();
    }

    public void toStop(){
        toStop = true;
        // interrupt and wait
        logrThread.interrupt();
        try {
            logrThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
