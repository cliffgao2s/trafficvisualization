package com.trafficvisualization.tool;

import org.apache.commons.lang3.StringUtils;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 工具类，通过HTTP报文的首部字段中的UserAgent解析出浏览器的相关信息
 * 
 * @see https://github.com/HaraldWalker/user-agent-utils
 * */
public class UserAgentUtility {

	public static void main(String[] args) {
		String info = "Mozilla/5.0";
		System.out.println(UserAgentUtility.getUserAgentAndVersion(info));
	}

	public static String getUserAgentAndVersion(String info) {
		System.out.println("info:" + info);

		if (StringUtils.isEmpty(info)) {
			return "Unkown UserAgent";
		}

		UserAgent ua = UserAgent.parseUserAgentString(info);
		String brower = ua.getBrowser().getName();
		String os = ua.getOperatingSystem().getName();
		String osManufacturer = ua.getOperatingSystem().getManufacturer()
				.getName();

		StringBuffer sb = new StringBuffer(32);
		sb.append(brower).append(" ").append(os).append('(')
				.append(osManufacturer).append(')');

		String agent = sb.toString();

		if (agent == null) {
			return "Unknown UserAgent";
		} else {
			return agent;
		}

	}
}
