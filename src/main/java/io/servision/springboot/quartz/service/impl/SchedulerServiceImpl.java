package io.servision.springboot.quartz.service.impl;

import io.servision.springboot.quartz.entity.QuartzVo;
import io.servision.springboot.quartz.exception.DomainSchedulerException;
import io.servision.springboot.quartz.service.SchedulerService;
import io.servision.springboot.quartz.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author sun 2019/3/1
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

	private final Scheduler scheduler;

	public SchedulerServiceImpl(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void add(String jobName, String jobGroup, String cronExpression, String jobDescription) {
		valid(jobName, jobGroup, cronExpression, jobDescription);

		try {
			log.info("添加jobName={},jobGroup={},cronExpression={},jobDescription={}", jobName, jobGroup, cronExpression,
					jobDescription);

			if (exists(jobName, jobGroup)) {
				log.error("Job已经存在, jobName={},jobGroup={}", jobName, jobGroup);
				throw new DomainSchedulerException(String.format("Job已经存在, jobName={},jobGroup={}", jobName, jobGroup));
			}

			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

			CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
					.withMisfireHandlingInstructionDoNothing();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withDescription(DateUtils.getNowDateStr()).withSchedule(schedBuilder).build();

			Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobName);
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException | ClassNotFoundException e) {
			log.error("添加job失败, jobName={},jobGroup={},e={}", jobName, jobGroup, e);
			throw new DomainSchedulerException("类名不存在或执行表达式错误");
		}

	}

	@Override
	public void update(String jobName, String jobGroup, String cronExpression, String jobDescription) {
		valid(jobName, jobGroup, cronExpression, jobDescription);
		try {
			log.info("修改jobName={},jobGroup={},cronExpression={},jobDescription={}", jobName, jobGroup, cronExpression,
					jobDescription);
			if (!exists(jobName, jobGroup)) {
				log.error("Job不存在, jobName={},jobGroup={}", jobName, jobGroup);
				throw new DomainSchedulerException(String.format("Job不存在, jobName=%s,jobGroup=%s", jobName, jobGroup));
			}
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			JobKey jobKey = new JobKey(jobName, jobGroup);
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
					.withMisfireHandlingInstructionDoNothing();
			CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
					.withDescription(DateUtils.getNowDateStr()).withSchedule(cronScheduleBuilder).build();

			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			jobDetail.getJobBuilder().withDescription(jobDescription);
			HashSet<Trigger> triggerSet = new HashSet<>();
			triggerSet.add(cronTrigger);

			scheduler.scheduleJob(jobDetail, triggerSet, true);
		} catch (SchedulerException e) {
			log.error("修改job失败, jobName={},jobGroup={},e={}", jobName, jobGroup, e);
			throw new DomainSchedulerException("类名不存在或执行表达式错误");
		}
	}

	@Override
	public void delete(String jobName, String jobGroup) {
		try {
			log.info("删除jobName={},jobGroup={}", jobName, jobGroup);
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (exists(jobName, jobGroup)) {
				scheduler.pauseTrigger(triggerKey);
				scheduler.unscheduleJob(triggerKey);
			}
		} catch (SchedulerException e) {
			log.error("删除job失败, jobName={},jobGroup={},e={}", jobName, jobGroup, e);
			throw new DomainSchedulerException(e.getMessage());
		}
	}

	@Override
	public void pause(String jobName, String jobGroup) {
		try {
			log.info("暂停jobName={},jobGroup={}", jobName, jobGroup);
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (!exists(jobName, jobGroup)) {
				log.error("Job不存在, jobName={},jobGroup={}", jobName, jobGroup);
				throw new DomainSchedulerException(String.format("Job不存在, jobName=%s,jobGroup=%s", jobName, jobGroup));
			}
			scheduler.pauseTrigger(triggerKey);
		} catch (SchedulerException e) {
			log.error("暂停job失败, jobName={},jobGroup={},e={}", jobName, jobGroup, e);
			throw new DomainSchedulerException(e.getMessage());
		}
	}

	@Override
	public void resume(String jobName, String jobGroup) {
		try {
			log.info("重启jobName={},jobGroup={}", jobName, jobGroup);
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			if (!exists(jobName, jobGroup)) {
				log.error("Job不存在, jobName={},jobGroup={}", jobName, jobGroup);
				throw new DomainSchedulerException(String.format("Job不存在, jobName=%s,jobGroup=%s", jobName, jobGroup));
			}
			scheduler.resumeTrigger(triggerKey);
		} catch (SchedulerException e) {
			log.error("重启job失败, jobName={},jobGroup={},e={}", jobName, jobGroup, e);
			throw new DomainSchedulerException(e.getMessage());
		}
	}

	@Override
	public void trigger(String jobName, String jobGroup) {
		try {
			log.info("立即执行jobName={},jobGroup={}", jobName, jobGroup);
			if (!exists(jobName, jobGroup)) {
				log.error("Job不存在, jobName={},jobGroup={}", jobName, jobGroup);
				throw new DomainSchedulerException(String.format("Job不存在, jobName=%s,jobGroup=%s", jobName, jobGroup));
			}
			JobKey jobKey = new JobKey(jobName, jobGroup);
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			log.error("立即执行job失败, jobName={},jobGroup={},e={}", jobName, jobGroup, e);
			throw new DomainSchedulerException(e.getMessage());
		}
	}

	@Override
	public List<QuartzVo> queryAll() {
		List<QuartzVo> quartzVoArrayList = new ArrayList<>();
		try {
			for (String groupJob : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))) {
					List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
						JobDetail jobDetail = scheduler.getJobDetail(jobKey);

						String cronExpression = "", createTime = "";

						if (trigger instanceof CronTrigger) {
							CronTrigger cronTrigger = (CronTrigger) trigger;
							cronExpression = cronTrigger.getCronExpression();
							createTime = cronTrigger.getDescription();
						}
						QuartzVo info = new QuartzVo();
						info.setJobName(jobKey.getName());
						info.setJobGroup(jobKey.getGroup());
						info.setJobDescription(jobDetail.getDescription());
						info.setJobStatus(triggerState.name());
						info.setCronExpression(cronExpression);
						info.setCreateTime(createTime);
						info.setPreviousFireTime(trigger.getPreviousFireTime());
						info.setNextFireTime(trigger.getNextFireTime());
						quartzVoArrayList.add(info);
					}
				}
			}

		} catch (SchedulerException e) {
			log.error("查询定时任务失败，e={}", e);
		}
		return quartzVoArrayList;
	}

	@Override
	public boolean exists(String jobName, String jobGroup) throws SchedulerException {
		return scheduler.checkExists(TriggerKey.triggerKey(jobName, jobGroup));
	}

	@Override
	public JobDetail query(String jobName, String jobGroup) throws SchedulerException {
		return scheduler.getJobDetail(new JobKey(jobName, jobGroup));
	}


}
