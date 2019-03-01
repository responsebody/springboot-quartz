package io.servision.springboot.quartz.service;

import io.servision.springboot.quartz.entity.QuartzVo;
import io.servision.springboot.quartz.exception.DomainSchedulerException;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author sun 2019/3/1
 */
public interface SchedulerService {

	void add(String jobName, String jobGroup, String cronExpression, String jobDescription);

	void update(String jobName, String jobGroup, String cronExpression, String jobDescription);

	void delete(String jobName, String jobGroup);

	/**
	 * 暂停
	 * @param jobName
	 * @param jobGroup
	 */
	void pause(String jobName, String jobGroup);

	/**
	 * 恢复
	 * @param jobName
	 * @param jobGroup
	 */
	void resume(String jobName, String jobGroup);

	/**
	 * 立即执行
	 * @param jobName
	 * @param jobGruop
	 */
	void trigger(String jobName, String jobGruop);

	List<QuartzVo> queryAll() throws SchedulerException;

	JobDetail query(String jobName, String jobGruop) throws SchedulerException;

	boolean exists(String jobName, String jobGroup) throws SchedulerException;

	default void valid(String jobName, String jobGroup, String cronExpression, String jobDescription) {
		if (StringUtils.isAnyBlank(jobName, jobGroup, cronExpression, jobDescription)) {
			throw new DomainSchedulerException(
					String.format("参数错误, jobName=%s,jobGroup=%s,cronExpression=%s,jobDescription=%s", jobName, jobGroup,
							cronExpression, jobDescription));
		}
	}
}
