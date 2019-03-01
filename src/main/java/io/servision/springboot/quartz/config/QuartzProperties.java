package io.servision.springboot.quartz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sun 2019/2/28
 */
@Component
@ConfigurationProperties(prefix = "io.servision.quartz")
@Data
public class QuartzProperties {

	private String schedulerInstanceName = "sbom-schedule";
	private String schedulerInstanceId = "auto";
	private String jobStoreClass = "org.quartz.impl.jdbcjobstore.JobStoreTX";
	private String jobStoreDriverDelegateClass = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";

	private String jobStoreTablePrefix = "QRTZ_";
	private String jobStoreIsClustered = "true";
	private String useProperties = "false";
	private String clusterCheckinInterval = "20000";
	private String schedulerSkipUpdateCheck = "false";

	private String threadPoolClass = "org.quartz.simpl.SimpleThreadPool";
	private String threadPoolThreadCount = "10";
	private String threadPoolThreadPriority = "5";
	private String threadPoolThreadsInheritContextClassLoaderOfInitializingThread = "false";


}
