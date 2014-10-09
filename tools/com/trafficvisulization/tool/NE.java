package com.trafficvisualization.tool;

import org.apache.commons.lang3.StringUtils;

public class NE {

	/**
	 * 如果字符串为空，就返回""(为了避免在MySQL查询时候出现NULL)
	 * **/
	public static String filterNull(String in) {
		boolean flag = StringUtils.isEmpty(in);
		return flag ? "" : in;
	}

}
