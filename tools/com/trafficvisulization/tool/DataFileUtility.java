package com.trafficvisualization.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataFileUtility {

	/**
	 * 传入数据文件的根目录即可以返回所有的数据子文件
	 * 
	 * */
	public static List<String> getAllDataFilePath(String root) {
		File file = new File(root);
		List<String> filesList = new ArrayList<String>();
		if (file.isDirectory()) {
			String[] files = file.list();
			for (String name : files) {
				filesList.add(root + File.separator + name);
			}
		} else {
			filesList.add(root);
		}

		return filesList;
	}

	public static void main(String[] args) {
		List<String> files = DataFileUtility
				.getAllDataFilePath("/Users/xxx/Documents/Datas/231810Datas");
		for (String string : files) {
			System.out.println(string);
		}
	}
}
