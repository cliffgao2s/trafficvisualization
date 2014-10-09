package com.trafficvisualization.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	private static final String USER = "用户名";
	private static final String PASSWORD = "密码";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/traffic_visualization";
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

	public static Connection getConnection() {

		Connection connection = null;

		try {
			Class.forName(DRIVER_NAME);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			if (!connection.isClosed()) {
				return connection;
			} else {
				System.out.println("数据库连接已经关闭");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("驱动加载失败");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void closeConnection(Connection connection) {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
