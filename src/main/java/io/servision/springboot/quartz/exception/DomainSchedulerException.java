package io.servision.springboot.quartz.exception;

/**
 * @author sun 2019/3/1
 */
public class DomainSchedulerException extends RuntimeException {

	public DomainSchedulerException(String message) {
		super(message);
	}
}
