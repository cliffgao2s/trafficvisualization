import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by yzh on 14-10-30 01:19.
 * ProjectInfo NTV
 * ClassInfo PcapUtility
 * 该类提供pcap的分割操作
 */
public class PcapUtility {

	// pcap包的首部信息，这24个字节的内容不会发生变化
	private static final byte[] pcapHeaderInfo = {
			(byte) 0xD4, (byte) 0xC3, (byte) 0xB2,
			(byte) 0xA1, (byte) 0x02, (byte) 0x00,
			(byte) 0x04, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0xFF, (byte) 0xFF,
			(byte) 0x00, (byte) 0x00, (byte) 0x01,
			(byte) 0x00, (byte) 0x00, (byte) 0x00
	};

	/**
	 * 读到的4位字节，第一位字节应该放在最低位(小端序)
	 */
	public static int getInt(byte[] bytes) {
		return Ints.fromBytes(bytes[3], bytes[2], bytes[1], bytes[0]);
	}

	/**
	 * 将距离1970年1月1日0时0分的秒数化为对人类友好的形式
	 *
	 * @param seconds 距离1970年1月1日0时0分的秒数
	 * @return yyyy-MM-dd HH:mm:ss的格式
	 */
	public static String getPacketDateTime(int seconds) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 注意北京时间要+8小时
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(8));
		return localDateTime.format(dtf);
	}

	/**
	 * 将读出来的16进制的字节数转化为10进制，得到保存下来的包长度
	 *
	 * @param one 十六进制的字节数
	 * @return 该16进制对应的十进制数
	 */
	public static int hexToDec(byte one) {
		return Integer.parseInt(String.valueOf(one), 10);
	}

	/**
	 * 读取pcap包的内容，将pcap包转换为字节数组（这里最大读取size M的pcap包的内容）
	 *
	 * @param path pcap包的路径
	 * @return pcap的字节数组内容
	 */
	public byte[] readPcap(String path, int size) {

		// 数组的大小为size M
		byte[] bytes = new byte[size * 1024 * 1024];
		int used = 0;
		FileInputStream inputStream = null;
		BufferedInputStream bis = null;
		try {
			inputStream = new FileInputStream(path);
			bis = new BufferedInputStream(inputStream);
			int data = -1;
			while ((data = bis.read()) != -1) {
				bytes[used++] = (byte) data;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		byte[] need = new byte[used];

		// 只将读取的部分返回
		System.arraycopy(bytes, 0, need, 0, used);

		return need;
	}

	/**
	 * 将byte[]字节数组写入到文件形成pcap包
	 *
	 * @param path 写成的pcap的路径
	 * @param pkt  字节数组
	 */
	public void writePcap(String path, byte[] pkt) {
		File f = new File(path);
		FileOutputStream out = null;
		BufferedOutputStream buffer = null;
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			// 注意覆盖文件
			out = new FileOutputStream(f, true);
			buffer = new BufferedOutputStream(out);
			buffer.write(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 将大的pcap划分为小的pcap包
	 *
	 * @param basePcap       大的pcap的路径
	 * @param subPcap        划分的子pcap的名字
	 * @param packetsPerFile 每个文件划分的最多个pcap包
	 */
	public List<String> divideBigPcapByPacketsNumber(String basePcap, int packetsPerFile, String subPcap) {

		List<String> pcaps = Lists.newArrayList();

		StringBuilder sb = new StringBuilder();
		// 前提条件是安装了Wireshark
		sb.append("editcap -c ")
				.append(packetsPerFile)
				.append(" ")
				.append(basePcap)
				.append(" ")
				.append(subPcap);
		System.out.println(sb);
		try {
			Process pro = Runtime.getRuntime().exec(sb.toString());
			pro.waitFor();
			int lastIndex = basePcap.lastIndexOf('/');
			File[] files = new File(basePcap.substring(0, lastIndex)).listFiles();

			for (File f : files) {
				if (f.getPath().endsWith("pcap") && !f.getPath().equals(basePcap)) {
					System.out.println(f.getPath());
					pcaps.add(f.getPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return pcaps;
	}

	/**
	 * 只导出时间戳在beginTime之后，endTime之前的包。时间格式为YYYY-MM-DD hh:mm:ss
	 *
	 * @param basePcap  大的pcap的路径
	 * @param subPcap   划分的子pcap的名字
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 */
	public List<String> divideBigPcapByTimeSpace(String basePcap, String beginTime, String endTime, String subPcap) {
		List<String> pcaps = Lists.newArrayList();

		StringBuilder sb = new StringBuilder();
		// 前提条件是安装了Wireshark
		sb.append("editcap");
		if (beginTime != null) {
			sb.append(" -A " + beginTime);
		}

		if (endTime != null) {
			sb.append(" -B " + endTime);
		}

		sb.append(" ")
				.append(basePcap)
				.append(" ")
				.append(subPcap);

		System.out.println(sb);
		try {
			Process pro = Runtime.getRuntime().exec(sb.toString());
			pro.waitFor();
			int lastIndex = basePcap.lastIndexOf('/');
			File[] files = new File(basePcap.substring(0, lastIndex)).listFiles();

			for (File f : files) {
				if (f.getPath().endsWith("pcap") && !f.getPath().equals(basePcap)) {
					System.out.println(f.getPath());
					pcaps.add(f.getPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return pcaps;
	}

	/**
	 * 按照最大的秒数为单位间隔分割Pcap包
	 *
	 * @param basePcap        大的pcap的路径
	 * @param subPcap         划分的子pcap的名字
	 * @param maxTimeInterval 最大时间间隔
	 */
	public List<String> divideBigPcapByTimeInterval(String basePcap, int maxTimeInterval, String subPcap) {
		List<String> pcaps = Lists.newArrayList();

		StringBuilder sb = new StringBuilder();
		// 前提条件是安装了Wireshark
		sb.append("editcap -i ")
				.append(maxTimeInterval)
				.append(" ")
				.append(basePcap)
				.append(" ")
				.append(subPcap);
		System.out.println(sb);
		try {
			Process pro = Runtime.getRuntime().exec(sb.toString());
			pro.waitFor();
			int lastIndex = basePcap.lastIndexOf('/');
			File[] files = new File(basePcap.substring(0, lastIndex)).listFiles();

			for (File f : files) {
				if (f.getPath().endsWith("pcap") && !f.getPath().equals(basePcap)) {
					System.out.println(f.getPath());
					pcaps.add(f.getPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return pcaps;
	}
	
	/**
	 * 将多个pcap包合并为同一个pcap包
	 *
	 * @param mergedPcaps 待合并的pcap包
	 * @param toPcap      合并后的包
	 * @return 合并之后的包名
	 */
	public String mergePcaps(List<String> mergedPcaps, String toPcap) {

		StringBuilder sb = new StringBuilder();
		// 前提条件是安装了Wireshark
		sb.append("mergecap -w").append(" ").append(toPcap);
		for (String pcap : mergedPcaps) {
			sb.append(" ").append(pcap);
		}

		System.out.println(sb);
		try {
			Process pro = Runtime.getRuntime().exec(sb.toString());
			pro.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return toPcap;
	}

	/**
	 * 获得一个文件的字节数大小
	 *
	 * @param f 需要获得的文件的引用
	 * @return 该文件的字节数大小
	 */
	private long getBytesLength(File f) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			return in.available();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return 0;
	}

	public static void main(String[] args) {
		// 注意第一个字节在3号索引
		byte[] bytes = {(byte) 0x63, (byte) 0xcf, (byte) 0x3f, (byte) 0x54};
		byte[] bytes2 = {(byte) 0x66, (byte) 0x44, (byte) 0x03, (byte) 0x00};
		byte[] bytes3 = {(byte) 0xD3, (byte) 0x36, (byte) 0x05, (byte) 0x00};
		PcapUtility pu = new PcapUtility();
		byte[] bs = pu.readPcap("/Users/xxx/Desktop/pcap包/login.pcap", 10);
		// System.out.println(bs.length);
		// 获得一个报文的数据长度
		byte[] lengthArray = {bs[32], bs[33], bs[34], bs[35]};
		int v = PcapUtility.getInt(lengthArray);
		// System.out.println("第一个数据包的长度:" + v);
		// 前24字节用于存放pcap的首部，16字节用于存放数据包的首部信息，v字节用于存放数据报信息
		byte[] pkt = new byte[24 + 16 + v];
		// 字节数组首先填充pcap首部信息
		System.arraycopy(PcapUtility.pcapHeaderInfo, 0, pkt, 0, 24);
		// 然后填充数据包的首部信息和数据包信息
		System.arraycopy(bs, 24, pkt, 24, v + 16);

		pu.writePcap("/Users/xxx/Desktop/pcap包/new.pcap", pkt);
		pu.divideBigPcapByPacketsNumber("/Users/xxx/Desktop/download-fast.pcap", 20000, "/Users/xxx/Desktop/yan.pcap");

	}

}
