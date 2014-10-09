package com.trafficvisualization.tool;

import org.apache.commons.lang3.StringUtils;

public class StatusCodeUtility {
	
	public static int getCodeNumber(String info){
		if(StringUtils.isEmpty(info)){
			// 对于只有请求的报文的StatusCode为-1
			return -1;
		}else{
			return Integer.parseInt(info);
		}
	}

}
