package io.servision.springboot.quartz.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author sun 2019/3/1
 */
@Data
public class QuartzVo {
	private String jobName;
	private String jobGroup;
	private String jobDescription;
	private String jobStatus;
	private String cronExpression;
	private String createTime;

	private Date previousFireTime;
	private Date nextFireTime;
}
