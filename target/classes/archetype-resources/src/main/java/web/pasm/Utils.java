package ${package}.web.pasm;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/10/010.
 */
public class Utils {

	/**
	 * 关闭对象, 调用close.
	 *
	 * @param objs
	 */
	public static void close(AutoCloseable... objs) {
		if (objs == null) {
			return;
		}
		for (AutoCloseable obj : objs) {
			if (obj == null) {
				continue;
			}
			try {
				obj.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 销毁对象, 置为null.
	 *
	 * @param objs
	 */
	public static void destroy(Object... objs) {
		if (objs == null) {
			return;
		}
		for (Object obj : objs) {
			obj = null;
		}
	}

	/**
	 * 获取UUID随机数
	 *
	 * @return
	 */
	public static long getId() {
		return getId(UUID.randomUUID().toString());
	}


	/**
	 * 根据字符串生成Long型的ID
	 *
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static long getId(String value) {
		MessageDigest localMessageDigest = null;
		try {
			localMessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] buffer = localMessageDigest.digest(value.getBytes());
		return Math.abs(bytesToLong(buffer));
	}


	public static long getLong(String value) {
		MessageDigest localMessageDigest = null;
		try {
			localMessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] buffer = localMessageDigest.digest(value.getBytes());
		return bytesToLong(buffer);
	}

	/**
	 * bytes转Long
	 *
	 * @param bytes
	 * @return
	 */
	public static long bytesToLong(byte[] bytes) {
		if (bytes == null || bytes.length < 8) {
			throw new IllegalArgumentException();
		}
		return (((long) bytes[0] & 0xff) << 56)
				| (((long) bytes[1] & 0xff) << 48)
				| (((long) bytes[2] & 0xff) << 40)
				| (((long) bytes[3] & 0xff) << 32)
				| (((long) bytes[4] & 0xff) << 24)
				| (((long) bytes[5] & 0xff) << 16)
				| (((long) bytes[6] & 0xff) << 8)
				| (((long) bytes[7] & 0xff));
	}

	/**
	 * 获取临时文件路径
	 *
	 * @return
	 */
	public static String getTempFolderPath() {
		return System.getProperty("user.dir") + File.separatorChar + "tempfiles";
	}

}
