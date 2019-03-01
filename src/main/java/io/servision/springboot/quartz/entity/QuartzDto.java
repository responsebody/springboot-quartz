package io.servision.springboot.quartz.entity;

import lombok.Data;

/**
 * @author sun 2019/3/1
 */
@Data
public class QuartzDto {

	private String jobName;
	private String jobGroup;
	private String cronExpression;
	private String jobDescription;

}
