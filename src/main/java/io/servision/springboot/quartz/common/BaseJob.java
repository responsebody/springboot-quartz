package io.servision.springboot.quartz.common;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author sun 2019/2/28
 */
public interface BaseJob extends Job {
	@Override
	void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException;
}
