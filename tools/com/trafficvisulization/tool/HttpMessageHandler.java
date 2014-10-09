package com.trafficvisualization.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.trafficvisualization.db.DBUtil;

public class HttpMessageHandler {

	public static Connection connection;

	static {
		connection = DBUtil.getConnection();
	}

	public static void saveAnalysisedLogsToMySQL(String root) {

		List<String> dataFiles = DataFileUtility.getAllDataFilePath(root);
		File temp = null;
		BufferedReader br = null;
		Map<String, String> data4colums = null;

		String sqlTemplate = "INSERT INTO `HTTP_MESSAGE`(`Type`, `BeginTime`, `BeginMicrosecond`, "
				+ "`EndTime`, `EndMicrosecond`, `SourceIP`, `SourcePort`, `DestinationIP`, "
				+ "`DestinationPort`, `Method`, `RequestUrl`, `Version`, `StatusCode`, "
				+ "`ReasonPhrase`, `Host`, `UserAgent`, `Server`, `ContentType`) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement pr = null;
		
		String line = null;

		long begin =System.currentTimeMillis();
		
		for (String dataFile : dataFiles) {
			temp = new File(dataFile);
			try {
				br = new BufferedReader(new FileReader(temp));
				while ((line = br.readLine()) != null) {
					System.out.println("line:::" + line);
					try {
						data4colums = SpiltUtility.spilt(line);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					pr = connection.prepareStatement(sqlTemplate);
					
					pr.setInt(1, Integer.parseInt(data4colums.get("Type")));
					pr.setLong(2, Long.parseLong(data4colums.get("BeginTime")));
					pr.setInt(3, Integer.parseInt(data4colums.get("BeginMicrosecond")));
					pr.setLong(4, Long.parseLong(data4colums.get("EndTime")));
					pr.setInt(5, Integer.parseInt(data4colums.get("EndMicrosecond")));
					pr.setString(6, NE.filterNull(data4colums.get("SourceIP")));
					pr.setInt(7, Integer.parseInt(data4colums.get("SourcePort")));
					pr.setString(8, NE.filterNull(data4colums.get("DestinationIP")));
					pr.setInt(9, Integer.parseInt(data4colums.get("DestinationPort")));
					pr.setString(10, NE.filterNull(data4colums.get("Method")));
					pr.setString(11, NE.filterNull(data4colums.get("RequestUrl")));
					pr.setString(12, "HTTP/1.1");
					pr.setInt(13, StatusCodeUtility.getCodeNumber(data4colums.get("StatusCode")));
					pr.setString(14, NE.filterNull(data4colums.get("ReasonPhrase")));
					pr.setString(15, NE.filterNull(data4colums.get("Host")));
					pr.setString(16, UserAgentUtility.getUserAgentAndVersion(data4colums.get("UserAgent")));
					pr.setString(17, NE.filterNull(data4colums.get("Server")));
					pr.setString(18, NE.filterNull(data4colums.get("ContentType")));
					
					int num = pr.executeUpdate();
					System.out.println("添加:" + (num == 1 ? "成功" : "失败"));

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		System.out.println("总共执行耗时 : "+(System.currentTimeMillis()-begin)/1000f+" 秒 ");
		
		if (pr != null) {
			try {
				pr.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
