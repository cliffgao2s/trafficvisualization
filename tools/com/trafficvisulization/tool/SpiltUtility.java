package com.trafficvisualization.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class SpiltUtility {

	private static final String HTTP_VERSION = "HTTP/1.1";

	/** 记录的三种类型 */
	private static final int TYPE_REQUEST_ONLY = 0;
	private static final int TYPE_RESPONSE_ONLY = 1;
	private static final int TYPE_REQUEST_WITH_RESPONSE = 2;

	/** 以下是针对类型2既有请求又有响应和类型0只有请求 **/
	private static final int TYPE = 0; // 类型
	private static final int BEGIN_TIME = 1; // 开始时间，自1970年1月1日0时以来的秒数
	private static final int BEGIN_MICROSECOND = 2; // 结束时间微秒数 1秒 ＝ 1，000，000微秒
	private static final int END_TIME = 3; // 结束时间，自1970年1月1日0时的秒数
	private static final int END_MICROSECOND = 4;// 结束时间微秒数
	private static final int SOURCE_IP = 5; // 源IP地址
	private static final int SOURCE_PORT = 6; // 源端口
	private static final int DESTINATION_IP = 7; // 目的IP
	private static final int DESTINATION_PORT = 8; // 目的端口
	private static final int METHOD = 9; // 请求方法

	// 其余由于首部字段个数未知就无法进行编号，对于类型2还有响应域的内容

	public static void main(String[] args) {

		String requestOnly = "0								1405265650	866579	1405265650	866579	183.55.96.202	15589	180.208.70.74	80								GET /0g/re/AliIM2014_taobao8.00.34C.exe?tmp=1405223507407 HTTP/1.1		Accept: */*		Cache-Control: no-cache		Connection: Keep-Alive		Host: dl-js-jscn-3.pchome.net		Pragma: no-cache		Range: bytes=37739081-38244625		Referer: http://download.pchome.net/internet/communications/message/download-21722.html		User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";
		// String responseOnly = "1								1404356732	889997	1404356732	889997	210.29.232.11	80	183.136.213.224	4410								HTTP/1.1 302 Moved Temporarily		Server: Apache-Coyote/1.1		Location: http://www.jsyzhyxh.com/psp/		Content-Length: 0		Date: Thu, 03 Jul 2014 03:16:32 GMT"
		// TODO Etag的双引号
		// String requestWithResponse = "2								1405265668	337811	1405265668	902774	46.4.72.200	38949	202.119.160.131	80								GET /2012/09/20/%E6%B6%88%E5%A4%B1%E7%9A%84%E6%9D%91%E8%90%BD/trackback/ HTTP/1.0		Connection: close		Host: www.phronesis.cn		User-Agent: Mozilla/5.0 (compatible; MJ12bot/v1.4.5; http://www.majestic12.co.uk/bot.php?+)		Accept: text/html,text/plain,text/xml,text/*,application/xml,application/xhtml+xml,application/rss+xml,application/atom+xml,application/rdf+xml		Accept-Encoding: gzip		Accept-Language: zh								HTTP/1.0 302 Found		Date: Sun, 13 Jul 2014 15:44:14 GMT		Server: Apache/2.2.19 (Win32) PHP/5.2.17		X-Powered-By: PHP/5.2.17		Set-Cookie: PHPSESSID=cc1913c6432866633125d38afc218c91; path=/		Expires: Thu, 19 Nov 1981 08:52:00 GMT		Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0		Pragma: no-cache		P3P: CP=/"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT/"		X-Pingback: http://www.phronesis.cn/xmlrpc.php		Set-Cookie: bp-message=deleted; expires=Sat, 13-Jul-2013 15:44:14 GMT; path=/		Set-Cookie: bp-message-type=deleted; expires=Sat, 13-Jul-2013 15:44:14 GMT; path=/		Link: <http://www.phronesis.cn/?p=123>; rel=shortlink		Location: http://www.phronesis.cn/2012/09/20/%e6%b6%88%e";
		Map<String, String> pairs = SpiltUtility
				.handlerRequestOnly(requestOnly, null); //handlerRequestWithResponse(requestWithResponse);
		for (String key : pairs.keySet()) {
			System.out.println(key + ":" + pairs.get(key));
		}
	}

	public static Map<String, String> spilt(String line) {
		// 每条记录的第一个字符就为类型，类型分别为0,1,2
		char type = line.charAt(0);
		switch (type) {
		case TYPE_REQUEST_ONLY + '0':
			return SpiltUtility.handlerRequestOnly(line, null);
		case TYPE_RESPONSE_ONLY + '0':
			return SpiltUtility.handlerResponseOnly(line);
		case TYPE_REQUEST_WITH_RESPONSE + '0':
			return SpiltUtility.handlerRequestWithResponse(line);
		default:
			return null;
		}
	}

	/**
	 * 处理只有请求的情形 这里的请求目前只有GET和POST两种情形
	 * */
	private static Map<String, String> handlerRequestOnly(String line,
			Map<String, String> pairs) {
		// 如果为空，就创建一个Map容器
		if (pairs == null) {
			pairs = new HashMap<String, String>();
		}

		String[] items = SpiltUtility.spiltByTab(line);
		// 先将常规信息存入到Map
		for (int i = TYPE; i <= DESTINATION_PORT; i++) {
			pairs.put(SpiltUtility.getDescription(i, TYPE_REQUEST_ONLY),
					items[i]);
		}
		// 存入请求方法, 请求URL和HTTP版本
		String[] requests = SpiltUtility
				.spiltMessageWithoutEntityBodyAndHeaders(items[METHOD]);
		SpiltUtility.inRequests(requests, pairs);
		// 存入请求头部信息
		for (int i = METHOD + 1; i < items.length; i++) {
			String[] subItems = SpiltUtility.spiltByColon(items[i]);
			// 后面取子串是因为要去掉最前面的一个空格
			pairs.put(
					SpiltUtility.generalHeader(subItems[0], TYPE_REQUEST_ONLY),
					subItems[1].substring(1));
		}

		return pairs;
	}

	/**
	 * 处理既有请求又有响应的情形
	 * 
	 * @return
	 * */
	private static Map<String, String> handlerRequestWithResponse(String line) {

		// 首先需要找到响应的起始索引，然后进行数据分割，左边当成只有请求类型的，右边当成响应类型的
		int responseBeginIndex = line.lastIndexOf(HTTP_VERSION);
		// 得到请求部分
		String requestPart = line.substring(0, responseBeginIndex - 8);// 注意要去掉8个TAB键
		Map<String, String> pairs = new HashMap<String, String>();
		// 处理完请求部分了
		SpiltUtility.handlerRequestOnly(requestPart, pairs);
		// 现在处理响应部分
		// 得到响应部分
		String responsePart = line.substring(responseBeginIndex);
		// 相应部分使用TAB键进行分割
		String[] responseItems = SpiltUtility.spiltByTab(responsePart);
		// HTTP版本信息，状态码，原因短语的字符串数组
		String[] responses = SpiltUtility
				.spiltMessageWithoutEntityBodyAndHeaders(responseItems[0]);
		SpiltUtility.inResponses(responses, pairs);
		// TODO 存入响应头部信息
		for (int i = 1; i < responseItems.length; i++) {
			String[] subItems = SpiltUtility.spiltByColon(responseItems[i]);
			// 取得格式化后的名字，要判断是因为最后一个字段可能就为内容字段，所以不一定有名字,就判断length要为2，而且解析错误名字就为空
			String formatedName = SpiltUtility.generalHeader(subItems[0],
					TYPE_RESPONSE_ONLY);
			if (formatedName != null && subItems.length == 2) {
				// 后面取子串是因为要去掉最前面的一个空格
				pairs.put(formatedName, subItems[1].substring(1));

			}
		}

		return pairs;

	}

	/**
	 * 处理只有响应的情形
	 * */
	private static Map<String, String> handlerResponseOnly(String line) {

		Map<String, String> pairs = new HashMap<String, String>();
		String[] items = SpiltUtility.spiltByTab(line);
		// 先将常规信息存入到Map
		for (int i = TYPE; i <= DESTINATION_PORT; i++) {
			pairs.put(SpiltUtility.getDescription(i, TYPE_REQUEST_ONLY),
					items[i]);
		}
		// 存入HTTP版本,状态代码和原因短语
		String[] responses = SpiltUtility
				.spiltMessageWithoutEntityBodyAndHeaders(items[METHOD]);
		SpiltUtility.inResponses(responses, pairs);
		// TODO 存入响应头部信息
		for (int i = METHOD + 1; i < items.length; i++) {
			String[] subItems = SpiltUtility.spiltByColon(items[i]);
			// 取得格式化后的名字，要判断是因为最后一个字段可能就为内容字段，所以不一定有名字
			String formatedName = SpiltUtility.generalHeader(subItems[0],
					TYPE_RESPONSE_ONLY);
			if (formatedName != null && subItems.length == 2) {
				// 后面取子串是因为要去掉最前面的一个空格
				pairs.put(formatedName, subItems[1].substring(1));

			}
		}

		return pairs;
	}

	/**
	 * 按Tab键分割字符串
	 * 
	 * @param line
	 *            要分割的字符串
	 * @return 分割好的字符串数组
	 * */
	public static String[] spiltByTab(String line) {
		return line.split("(" + (char) 9 + ")+"); // Tab键的ASCII码是9
	}

	/**
	 * 对于POST方法的请求报文，请求报文中没有包含实体部分和头部信息，只有请求方法，请求URL和HTTP版本信息。
	 * 它们使用空格分隔开，故使用空格进行分割。
	 * 对于响应报文，也不包含实体部分和头部信息，只有HTTP版本信息，状态码和原因短语。它们使用空格分隔开，故也可以使用空格进行分割。
	 * 
	 * @param in
	 *            不包含实体部分和头部的请求报文或者响应包含
	 * @return 请求方法，请求URL和HTTP版本信息或者HTTP版本信息，状态码，原因短语的字符串数组
	 * */
	private static String[] spiltMessageWithoutEntityBodyAndHeaders(String in) {
		return in.split("(" + (char) 32 + ")+", 3); // 空格的ASCII码是32, 且最多分为3部分
	}

	private static String[] spiltByColon(String item) {
		String[] strs = new String[2];
		try {
			strs = item.split(":", 2); // 注意最多分为两部分
		} catch (PatternSyntaxException e) {
			System.out.println("解析出现错误了估计到了内容部分...");
			strs[0] = null; // 解析错误后名字和值都置为空
			strs[1] = null;

			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 将请求方法，请求URL和HTTP版本加入到Map中去
	 * */
	private static Map<String, String> inRequests(String[] requests,
			Map<String, String> maps) {
		if (requests.length != 3) {
			return null;
		} else {
			maps.put("Method", requests[0]);
			String requestUrl = requests[1];
			// 存在%考虑进行url解码
			if (requestUrl.contains("%")) {
				// TODO 如何判断URL是gb2312编码还是UTF-8编码?
				// requestUrl = UrlEnAndDecode.decode(requestUrl, "gb2312");
			}
			maps.put("RequestUrl", requestUrl);
			maps.put("Version", requests[2]);
			return maps;
		}
	}

	/**
	 * 将HTTP版本，状态码和原因短语假如到Map中去
	 * */
	private static Map<String, String> inResponses(String[] responses,
			Map<String, String> maps) {
		if (responses.length != 3) {
			return null;
		} else {
			maps.put("Version", responses[0]);
			maps.put("StatusCode", responses[1]);
			maps.put("ReasonPhrase", responses[2]);
			return maps;
		}
	}

	/**
	 * 对于请求报文和响应报文公有的通用首部加上前缀
	 * */
	private static String generalHeader(String name, int type) {
		// 名字为空的时候直接返回
		if (name == null) {
			return null;
		}

		if (type == TYPE_REQUEST_ONLY) {
			String upperName = name.toUpperCase();
			if (upperName.equals("CONNECTION") || upperName.equals("DATE")
					|| upperName.equals("MIME-VERSION")
					|| upperName.equals("TRAILER")
					|| upperName.equals("TRANSFER-ENCODING")
					|| upperName.equals("UPDATE") || upperName.equals("VIA")
					|| upperName.equals("CACHE-CONTROL")
					|| upperName.equals("PRAGMA")) {
				return "Request" + name.replace("-", "");
			} else {
				return name.replace("-", "");
			}
		} else if (type == TYPE_RESPONSE_ONLY) {
			String upperName = name.toUpperCase();
			if (upperName.equals("CONNECTION") || upperName.equals("DATE")
					|| upperName.equals("MIME-VERSION")
					|| upperName.equals("TRAILER")
					|| upperName.equals("TRANSFERENCODING")
					|| upperName.equals("UPDATE") || upperName.equals("VIA")
					|| upperName.equals("CACHE-CONTROL")
					|| upperName.equals("PRAGMA")) {
				return "Reponse" + name.replace("-", "");
			} else {
				return name.replace("-", "");
			}
		}

		return name;
	}

	private static String getDescription(int id, int whichType) {
		switch (id) {
		case TYPE:
			return "Type";
		case BEGIN_TIME:
			return "BeginTime";
		case BEGIN_MICROSECOND:
			return "BeginMicrosecond";
		case END_TIME:
			return "EndTime";
		case END_MICROSECOND:
			return "EndMicrosecond";
		case SOURCE_IP:
			return "SourceIP";
		case SOURCE_PORT:
			return "SourcePort";
		case DESTINATION_IP:
			return "DestinationIP";
		case DESTINATION_PORT:
			return "DestinationPort";
		default:
			return "其他字段";
		}
	}

}
