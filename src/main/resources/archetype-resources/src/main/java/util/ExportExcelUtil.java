package ${package}.util;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Font;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * description: 导出Excel方法
 *
 * @author Andy
 * @version 1.0
 * @date 05/14/2019 10:22
 */
public class ExportExcelUtil {
	/**
	 * Method Description: Created by whx
	 * 〈 导出带有多个Sheet页的Excel， 三个map的key值相同〉
	 *
	 * @param response                Http响应对象
	 * @param dataSheetNameMap        数据sheet名 <——> 数据List
	 * @param filename                文件名
	 * @param sheetNameAndMetaTypeMap 数据sheet名 <——> 数据对象类型
	 * @param sheetNameAndHeadLineMun 数据sheet名 <——> sheet表头部行数
	 * @return void
	 * @throws IOException io 异常
	 * @date 05/20/2019 16:41
	 */
	public void exportMultiSheetExcel(
			HttpServletResponse response,
			Map<String, List<? extends BaseRowModel>> dataSheetNameMap,
			String filename,
			Map<String, Class<? extends BaseRowModel>> sheetNameAndMetaTypeMap,
			Map<String, Integer> sheetNameAndHeadLineMun) throws IOException {

		OutputStream out = response.getOutputStream();
		this.responseHeaderSetting(response, filename);
		Integer index = 0;
		ExcelWriter writer = this.sheetSettingStart(out);
		for (Map.Entry<String, List<? extends BaseRowModel>> entry : dataSheetNameMap.entrySet()) {
			index++;
			this.sheetSetting(writer, sheetNameAndMetaTypeMap.get(entry.getKey()),
					dataSheetNameMap.get(entry.getKey()), entry.getKey(), index, sheetNameAndHeadLineMun.get(entry.getKey()));
		}
		this.sheetSettingFinish(writer, out);
	}

	/**
	 * Method Description: Created by whx
	 * 〈导出单个sheet页的excel〉
	 *
	 * @param response    HttpServletResponse
	 * @param list        数据列表, 需要继承阿里巴巴 -> BaseRowModel
	 * @param filename    文件名称
	 * @param sheetName   sheet页名称
	 * @param clazz       导出实体类型
	 * @param headLineMun 头部占用几行
	 * @return void 无返回值
	 * @throws Exception 可能发生异常
	 * @date 05/14/2019 14:36
	 */
	public void exportSingleSheetExcel(HttpServletResponse response, List<? extends BaseRowModel> list,
	                                   String filename,
	                                   String sheetName,
	                                   Class<? extends BaseRowModel> clazz,
	                                   int headLineMun) throws Exception {
		OutputStream out = response.getOutputStream();
		this.responseHeaderSetting(response, filename);
		ExcelWriter writer = this.sheetSettingStart(out);
		this.sheetSetting(writer, clazz, list, sheetName, 1, headLineMun);
		this.sheetSettingFinish(writer, out);
	}


	/**
	 * Method Description: Created by whx
	 * 〈 Sheet 页设置 〉
	 *
	 * @param writer      writer
	 * @param clazz       导出实体类型
	 * @param list        数据列表, 需要继承阿里巴巴 -> BaseRowModel
	 * @param sheetName   sheet页名称
	 * @param sheetNo     sheet 第几个sheet页
	 * @param headLineMun 表头占几行
	 * @return void 无
	 * @throws IOException io异常
	 * @date 05/20/2019 15:09
	 */
	private void sheetSetting(ExcelWriter writer,
	                          Class<? extends BaseRowModel> clazz,
	                          List<? extends BaseRowModel> list,
	                          String sheetName,
	                          int sheetNo,
	                          int headLineMun) throws IOException {

		Sheet sheet = new Sheet(sheetNo, headLineMun, clazz, sheetName, null);
		TableStyle ts = new TableStyle();
		// 表头字体
		Font headFont = new Font();
		Font contentFont = new Font();
		headFont.setBold(true);
		headFont.setFontHeightInPoints((short) 10);
		ts.setTableHeadFont(headFont);
		// 内容字体
		contentFont.setBold(false);
		contentFont.setFontHeightInPoints((short) 10);

		ts.setTableContentBackGroundColor(IndexedColors.WHITE);
		ts.setTableHeadFont(headFont);
		ts.setTableContentFont(contentFont);
		sheet.setTableStyle(ts);
		//设置自适应宽度
		sheet.setAutoWidth(Boolean.TRUE);
		writer.write(list, sheet);
	}

	/**
	 * Method Description: Created by whx
	 * 〈 根据输出流返回 ExcelWriter 对象〉
	 *
	 * @param out 输出流
	 * @return com.alibaba.excel.ExcelWriter
	 * @date 05/20/2019 16:46
	 */
	private ExcelWriter sheetSettingStart(OutputStream out) {
		return new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
	}

	/**
	 * Method Description: Created by whx
	 * 〈 writer 写完后关闭 〉
	 *
	 * @param writer 写文件流
	 * @return void
	 * @date 05/20/2019 16:47
	 */
	private void sheetSettingFinish(ExcelWriter writer, OutputStream out) throws IOException {
		writer.finish();
		out.flush();
	}

	/**
	 * Method Description: Created by whx
	 * 〈 文件导出结束对响应头部的封装 〉
	 *
	 * @param response HttpResponse
	 * @param filename 文件名
	 * @return void 空
	 * @throws IOException io异常
	 * @date 05/20/2019 14:46
	 */
	private void responseHeaderSetting(HttpServletResponse response, String filename) throws IOException {
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		filename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
		//通知浏览器以附件的形式下载处理
		response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

	}


}
