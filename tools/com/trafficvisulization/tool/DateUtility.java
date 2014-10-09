package com.trafficvisualization.tool;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtility {

	/**
	 * 将距离1970年1月1日0时0分的秒数转换为人类可读的形式
	 * **/
	public void getReadableDate(long seconds) {
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, 0,
				ZoneOffset.UTC);
		System.out.println(localDateTime.format(DateTimeFormatter
				.ofPattern("yyyy-MM-dd k:m:s")));
	}

	public static void main(String[] args) {
		new DateUtility().getReadableDate(1405265678);
		new DateUtility().getReadableDate(1405265151);
	}

}
