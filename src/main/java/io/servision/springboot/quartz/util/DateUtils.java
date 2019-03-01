package io.servision.springboot.quartz.util;

import org.joda.time.DateTime;

/**
 * @author sun 2019/3/1
 */
public class DateUtils {
	public static String getNowDateStr() {
		return DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
	}
}
