package io.servision.springboot.quartz.common;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author sun 2019/2/28
 */
@Slf4j
@DisallowConcurrentExecution
public class DaemonJob implements BaseJob {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("DaemonJob job----PreviousFireTime={},NextFireTime={},FireTime={}", context.getPreviousFireTime(),
				context.getNextFireTime(), context.getFireTime());
	}
}
