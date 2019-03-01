package io.servision.springboot.quartz;

import io.servision.springboot.quartz.config.AutowiringSpringBeanJobFactory;
import io.servision.springboot.quartz.config.QuartzProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

import static org.quartz.impl.StdSchedulerFactory.*;

/**
 * @author sun 2019/2/28
 */
@Configuration
@EnableConfigurationProperties({QuartzProperties.class})
@AutoConfigureAfter(QuartzProperties.class)
@ConditionalOnProperty(value = "quartz.enabled", matchIfMissing = true)
@ComponentScan({"io.servision.springboot.quartz"})
@Slf4j
public class QuartzSchedulerAutoConfiguration {

	private final QuartzProperties quartzProperties;

	public QuartzSchedulerAutoConfiguration(QuartzProperties quartzProperties) {
		this.quartzProperties = quartzProperties;
		log.info("init quartz。。。。。。。。。。。。。。。。。");
	}

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, DataSource dataSource,
			PlatformTransactionManager transactionManager) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setQuartzProperties(quartzProperties());
		factory.setAutoStartup(true);
		factory.setJobFactory(jobFactory);

		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		return factory;
	}

	@Bean
	public Properties quartzProperties() {
		Properties properties = new Properties();
		properties.put(PROP_SCHED_INSTANCE_NAME, quartzProperties.getSchedulerInstanceName());
		properties.put(PROP_SCHED_INSTANCE_ID, quartzProperties.getSchedulerInstanceId());
		properties.put(PROP_JOB_STORE_CLASS, quartzProperties.getJobStoreClass());
		properties.put("org.quartz.jobStore.driverDelegateClass", quartzProperties.getJobStoreDriverDelegateClass());
		properties.put("org.quartz.jobStore.tablePrefix", quartzProperties.getJobStoreTablePrefix());
		properties.put("org.quartz.jobStore.isClustered", quartzProperties.getJobStoreIsClustered());
		properties.put(PROP_JOB_STORE_USE_PROP, quartzProperties.getUseProperties());
		properties.put("org.quartz.jobStore.clusterCheckinInterval", quartzProperties.getClusterCheckinInterval());
		properties.put("org.quartz.scheduler.skipUpdateCheck", quartzProperties.getSchedulerSkipUpdateCheck());
		properties.put(PROP_THREAD_POOL_CLASS, quartzProperties.getThreadPoolClass());
		properties.put("org.quartz.threadPool.threadCount", quartzProperties.getThreadPoolThreadCount());
		properties.put("org.quartz.threadPool.threadPriority", quartzProperties.getThreadPoolThreadPriority());
		properties.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread",
				quartzProperties.getThreadPoolThreadsInheritContextClassLoaderOfInitializingThread());
		return properties;
	}

	/**
	 * 初始化监听器 在工程停止再启动时可以让已有的定时任务继续进行
	 * @return
	 */
	@Bean
	public QuartzInitializerListener quartzInitializerListener() {
		return new QuartzInitializerListener();
	}


}
