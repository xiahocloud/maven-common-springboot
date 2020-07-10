package ${package}.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * description: 趋势图时间数据对比
 *
 * @author Andy
 * @version 1.0
 * @date 05/28/2019 10:40
 */
public class TrendDataToTimeUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(${package}.util.TrendDataToTimeUtil.class);

	/**
	 * Method Description: Created by whx
	 * 〈 用于将天粒度的数据 与天相对应 〉
	 *
	 * @param times 时间
	 * @param datas 数据
	 * @param key   数据中时间键
	 * @return List<Map<String, String>>	结果
	 * @date 05/28/2019 10:48
	 */
	public static List<Map<String, String>> getDataToDateTime(List<String> times, List<Map<String, String>> datas, String key) {
		List<Map<String, String>> results = new LinkedList<>();
		times.forEach(item -> {

			if (item == null || "".equals(item)) {
				throw new RuntimeException("时间列表数据异常", new Exception());
			}

			Boolean isContain = false;
			for (Map<String, String> innerItem : datas) {
				if (item.equals(innerItem.get(key))) {
					isContain = true;
					results.add(innerItem);
				}
			}
			if (!isContain) {
				Map<String, String> map = new HashMap<>();
				map.put(item, null);
				results.add(map);
			} else {
				isContain = false;
			}
		});
		return results;

	}

	/**
	 * Method Description: Created by whx
	 * 〈 获取时间列表 〉
	 *
	 * @param time          时间
	 * @param offsetDays    时间偏移量
	 * @param beforeOrAfter 向前还是向后偏移， -1: 前， 1： 后
	 * @return java.util.List<java.lang.String>
	 * @throws Exception 异常
	 * @date 05/28/2019 10:52
	 */
	public static List<String> getTimes(String time, int offsetDays, int beforeOrAfter) throws Exception {
		// 多少天， 实际上是计算的是多少天前
		offsetDays -= 1;
		List<String> times = new LinkedList<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDateEndTime = LocalDate.parse(time, dtf);
		String startTime = null;
		String endTime = null;
		Long startTimeLong;
		Long endTimeLong;
		if (beforeOrAfter == -1) {
			startTime = localDateEndTime.minusDays(offsetDays).format(dtf);
			startTimeLong = Long.valueOf(startTime);
			endTimeLong = Long.valueOf(time);
		} else if (beforeOrAfter == 1) {
			endTime = localDateEndTime.plusDays(offsetDays).format(dtf);
			startTimeLong = Long.valueOf(time);
			endTimeLong = Long.valueOf(endTime);
		} else {
			throw new RuntimeException("TrendDataToTimeUtil 时间解析错误", new Exception());
		}


		while (startTimeLong <= endTimeLong) {
			times.add(startTimeLong.toString());
			startTimeLong = Long.valueOf(plusOneDay(startTimeLong));
		}
		return times;
	}

	private static String plusOneDay(Long startTime) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDateEndTime = LocalDate.parse(startTime.toString(), dtf);
		return localDateEndTime.plusDays(1).format(dtf);
	}
}
