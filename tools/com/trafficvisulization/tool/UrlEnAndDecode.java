package com.trafficvisualization.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 用于URL的编码和解码
 * 
 * @author yzh
 * **/
public class UrlEnAndDecode {

	/**
	 * 进行URL的解码操作
	 * 
	 * @param url
	 *            要解码的url
	 * @param charset
	 *            指定的解码格式,支持utf-8和gb2312
	 * @return 返回解码后的url
	 * */
	public static String decode(String url, String charset) {
		String decodedUrl = null;
		try {
			decodedUrl = URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodedUrl;
	}

	/**
	 * 进行URL的解码操作,解码格式为utf-8
	 * 
	 * @param url
	 *            要解码的url
	 * @return 返回解码后的url
	 * */
	public static String decode(String url) {
		return UrlEnAndDecode.decode(url, "utf-8");
	}

	/**
	 * 进行URL的编码操作
	 * 
	 * @param url
	 *            要编码的url
	 * @param charset
	 *            指定的编码格式,支持utf-8和gb2312
	 * @return 返回编码后的url
	 * */
	public static String encode(String url, String charset) {
		String encodedUrl = null;
		try {
			encodedUrl = URLEncoder.encode(url, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedUrl;
	}

	/**
	 * 进行URL的编码操作，编码格式为utf-8
	 * 
	 * @param url
	 *            要编码的url
	 * @return 返回编码后的url
	 * */
	public static String encode(String url) {
		return UrlEnAndDecode.encode(url, "utf-8");
	}
	
	public static void main(String[] args) {
		String encodedUrl = "/movie/%E7%94%B5%E8%A7%86%E5%89%A7/%E7%A5%9E%E7%9B%BE%E5%B1%80%E7%89%B9%E5%B7%A5/%E7%A5%9E%E7%9B%BE%E5%B1%80%E7%89%B9%E5%B7%A5.Marvels.Agents.of.S.H.I.E.L.D.S01E04.%E4%B8%AD%E8%8B%B1%E5%AD%97%E5%B9%95.HDTVrip.1024X576.x264-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.mkv";
		System.out.println(UrlEnAndDecode.decode(encodedUrl));
		
		String encodedUrl2 = UrlEnAndDecode.encode("神盾局特工.Marvels.Agents.of.S.H.I.E.L.D.S01E04.中英字幕.HDTVrip.1024X576.x264-YYeTs人人影视.mkv", "gb2312");
		
		System.out.println(UrlEnAndDecode.decode(encodedUrl2, "gb2312"));
	}

}
