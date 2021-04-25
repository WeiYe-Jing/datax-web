import com.wugui.datax.client.JobClient;
import com.wugui.datax.client.enums.ExecutorBlockStrategyEnum;
import com.wugui.datax.client.enums.ExecutorRouteStrategyEnum;
import com.wugui.datax.client.model.JobInfo;
import org.junit.Test;

public class ClientTest {
	@Test
	public void addJob() {
		JobInfo jobInfo = new JobInfo();
		//jobInfo.setAlarmEmail("Locki@xxx.com");
		jobInfo.setAuthor("Locki");
		jobInfo.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
		jobInfo.setExecutorFailRetryCount(0);
		jobInfo.setExecutorHandler("procedureHandler");
		jobInfo.setExecutorParam("{\"type\":\" DB2\",\"url\":\"jdbc:db2://10.0.0.65:50000/DATANALY\",\"user\":\"db2admin\",\"code\":\"db2admin\",\"pros\":[\"PD_ZM_K_RLIC_D\",\"PD_ZM_K_CASEINFO_D(5)\"]}");
		jobInfo.setExecutorRouteStrategy(ExecutorRouteStrategyEnum.FIRST);
		jobInfo.setExecutorTimeout(0);
		jobInfo.setJobCron("0 0 12 * * ? *");
		jobInfo.setJobDesc("system测试任务");
		jobInfo.setJobGroup(1);
		jobInfo.setProjectId("1");
		
		JobClient client = new JobClient();
		System.out.println(client.createJob(jobInfo));
	}
	
	@Test
	public void updateJob() {
		JobInfo jobInfo = new JobInfo();
		jobInfo.setId(38);
		jobInfo.setAlarmEmail("Locki@xxx.com");
		jobInfo.setAuthor("Locki");
		jobInfo.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
		jobInfo.setExecutorFailRetryCount(0);
		jobInfo.setExecutorHandler("procedureHandler");
		jobInfo.setExecutorParam("{\"type\":\"DB2\",\"url\":\"jdbc:db2://10.0.0.65:50000/DATANALY\",\"user\":\"db2admin\",\"code\":\"db2admin\",\"pros\":[\"PD_ZM_K_RLIC_D\",\"PD_ZM_K_CASEINFO_D(5)\"]}");
		jobInfo.setExecutorRouteStrategy(ExecutorRouteStrategyEnum.FIRST);
		jobInfo.setExecutorTimeout(0);
		jobInfo.setJobCron("0 0/2 * * * ? *");
		jobInfo.setJobDesc("system测试任务");
		jobInfo.setJobGroup(1);
		jobInfo.setProjectId("1");
		
		JobClient client = new JobClient();
		System.out.println(client.updateJob(jobInfo));
	}
	
	@Test
	public void startJob() {
		JobClient client = new JobClient();
		System.out.println(client.startJob(37 + ""));
	}
	
	@Test
	public void stopJob() {
		JobClient client = new JobClient();
		System.out.println(client.stopJob(37 + ""));
	}
	
	@Test
	public void removeJob() {
		JobClient client = new JobClient();
		System.out.println(client.deleteJob(37 + ""));
	}
	
	@Test
	public void loadJob() {
		JobClient client = new JobClient();
		System.out.println(client.getJob(37 + ""));
	}
	
	@Test
	public void jobLog() {
		JobClient client = new JobClient();
		System.out.println(client.jobLog(36 + ""));
	}
}
