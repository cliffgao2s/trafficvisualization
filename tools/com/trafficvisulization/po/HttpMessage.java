package com.trafficvisualization.po;

public class HttpMessage {

	private long id;
	private int type;
	private long beginTime;
	private int beginMicrosecond;
	private long endTime;
	private int endMicrosecond;
	private String sourceIP;
	private int sourcePort;
	private String destinationIP;
	private int destinationPort;
	private String method;
	private String requestUrl;
	private String version;
	private int statusCode;
	private String reasonPhrase;
	private String host;
	private String userAgent;
	private String server;
	private String contentType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public int getBeginMicrosecond() {
		return beginMicrosecond;
	}

	public void setBeginMicrosecond(int beginMicrosecond) {
		this.beginMicrosecond = beginMicrosecond;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getEndMicrosecond() {
		return endMicrosecond;
	}

	public void setEndMicrosecond(int endMicrosecond) {
		this.endMicrosecond = endMicrosecond;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public int getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	public String getDestinationIP() {
		return destinationIP;
	}

	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}

	public int getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "HttpMessage [type=" + type + ", sourceIP=" + sourceIP
				+ ", sourcePort=" + sourcePort + ", destinationIP="
				+ destinationIP + ", destinationPort=" + destinationPort
				+ ", method=" + method + ", requestUrl=" + requestUrl
				+ ", statusCode=" + statusCode + ", host=" + host
				+ ", userAgent=" + userAgent + ", server=" + server
				+ ", contentType=" + contentType + "]";
	}
	
	

}
