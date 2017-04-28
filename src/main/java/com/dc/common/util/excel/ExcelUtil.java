package com.dc.common.util.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 该类实现了将一组对象转换为Excel表格，并且可以从Excel表格中读取到一组List对象中 该类利用了BeanUtils框架中的反射完成
 * 使用该类的前提，在相应的实体对象上通过ExcelReources来完成相应的注解
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "rawtypes" })
public class ExcelUtil {
	public static final String POSITION_TITLE = "title";
	public static final String POSITION_BODY = "body";
	public static final String POSITION_BODY_INT = "bodyInt";
	public static final String POSITION_BODY_DECIMAL = "bodyDecimal";

	private static ExcelUtil eu = new ExcelUtil();

	private ExcelUtil() {
	}

	public static ExcelUtil getInstance() {
		return eu;
	}

	/**
	 * 根据标题获取相应的方法名称
	 * 
	 * @param eh
	 * @return
	 */
	private String getMethodName(ExcelHeader eh) {
		String mn = eh.getMethodName().substring(3);
		mn = mn.substring(0, 1).toLowerCase() + mn.substring(1);
		return mn;
	}

	/**
	 * 从类路径读取相应的Excel文件到对象列表
	 * 
	 * @param path
	 *            类路径下的path
	 * @param clz
	 *            对象类型
	 * @param readLine
	 *            开始行，注意是标题所在行
	 * @param tailLine
	 *            底部有多少行，在读入对象时，会减去这些行
	 * @return
	 */
	public List<?> readExcel2ObjsByClasspath(String path, Class clz, int readLine, int tailLine) {
		Workbook wb = null;
		InputStream is = null;
		try {
			is = ExcelUtil.class.getResourceAsStream(path);
			wb = WorkbookFactory.create(is);
			return handlerExcel2Objs(wb, clz, readLine, tailLine);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 从文件路径读取相应的Excel文件到对象列表
	 * 
	 * @param path
	 *            文件路径下的path
	 * @param clz
	 *            对象类型
	 * @param readLine
	 *            开始行，注意是标题所在行
	 * @param tailLine
	 *            底部有多少行，在读入对象时，会减去这些行
	 * @return
	 */
	public List<?> readExcel2ObjsByPath(String path, Class clz, int readLine, int tailLine) {
		Workbook wb = null;
		InputStream is = null;
		try {
			is = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(is);
			return handlerExcel2Objs(wb, clz, readLine, tailLine);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 从类路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * 
	 * @param path
	 *            路径
	 * @param clz
	 *            类型
	 * @return 对象列表
	 */
	public List<?> readExcel2ObjsByClasspath(String path, Class clz) {
		return this.readExcel2ObjsByClasspath(path, clz, 0, 0);
	}

	/**
	 * 从文件路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * 
	 * @param path
	 *            路径
	 * @param clz
	 *            类型
	 * @return 对象列表
	 */
	public List<?> readExcel2ObjsByPath(String path, Class clz) {
		return this.readExcel2ObjsByPath(path, clz, 0, 0);
	}

	private String getCellValue(Cell c) {
		String o = null;
		switch (c.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			o = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			o = String.valueOf(c.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			o = String.valueOf(c.getCellFormula());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			o = String.valueOf(c.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			o = c.getStringCellValue().trim();
			break;
		default:
			o = null;
			break;
		}
		return o;
	}

	private List<?> handlerExcel2Objs(Workbook wb, Class clz, int readLine, int tailLine) {
		Sheet sheet = wb.getSheetAt(0);
		List<Object> objs = null;
		try {
			Row row = sheet.getRow(readLine);
			objs = new ArrayList<Object>();
			Map<Integer, String> maps = getHeaderMap(row, clz);
			if (maps == null || maps.size() <= 0)
				throw new RuntimeException("要读取的Excel的格式不正确，检查是否设定了合适的行");
			for (int i = readLine + 1; i <= sheet.getLastRowNum() - tailLine; i++) {
				row = sheet.getRow(i);
				Object obj = clz.newInstance();
				for (Cell c : row) {
					try {
						if (StringUtils.isBlank(c.getStringCellValue())) {
							break;
						}
						int ci = c.getColumnIndex();
						String mn = maps.get(ci).substring(3);
						mn = mn.substring(0, 1).toLowerCase() + mn.substring(1);
						BeanUtils.copyProperty(obj, mn, this.getCellValue(c));
					} catch (Exception e) {
						throw new RuntimeException("excel导入出错，行数: " + row.getRowNum() + ", 列数:" + c.getColumnIndex()
								+ ", 值: " + this.getCellValue(c));
					}
				}
				objs.add(obj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return objs;
	}

	private List<ExcelHeader> getHeaderList(Class clz) {
		List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
		Method[] ms = clz.getDeclaredMethods();
		for (Method m : ms) {
			String mn = m.getName();
			if (mn.startsWith("get")) {
				if (m.isAnnotationPresent(ExcelResources.class)) {
					ExcelResources er = m.getAnnotation(ExcelResources.class);
					headers.add(new ExcelHeader(er.title(), er.order(), mn, m.getReturnType()));
				}
			}
		}
		return headers;
	}

	private Map<Integer, String> getHeaderMap(Row titleRow, Class clz) {
		List<ExcelHeader> headers = getHeaderList(clz);
		Map<Integer, String> maps = new HashMap<Integer, String>();
		for (Cell c : titleRow) {
			String title = c.getStringCellValue();
			for (ExcelHeader eh : headers) {
				if (eh.getTitle().equals(title.trim())) {
					maps.put(c.getColumnIndex(), eh.getMethodName().replace("get", "set"));
					break;
				}
			}
		}
		return maps;
	}

	/**
	 * 
	 * @param outPath
	 *            输出路径
	 * @param objs
	 *            数据
	 * @param clz
	 *            数据类
	 * @param sheetName
	 *            表单名称
	 * @param pageSize
	 *            每页数量
	 */
	public void exportObj2Excel(String outPath, List objs, Class clz, String sheetName, int pageSize) {
		Workbook wb = handleObj2Excel(objs, clz, sheetName, pageSize);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导出对象到Excel，不是基于模板的，直接新建一个Excel完成导出，基于路径的导出
	 *
	 * @param outPath
	 *            输出路径
	 * @param objs
	 *            数据源
	 * @param clz
	 *            类
	 * @param sheetName
	 *            分sheet导出是sheet的名字 ， 如 “sheet” -> sheet1,sheet2...
	 * @param pageSize
	 *            每个sheet要显示多少条数据
	 */
	public HSSFWorkbook handleObj2Excel(List objs, Class clz, String sheetName, int pageSize) {
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook();
			// 获取表头
			List<ExcelHeader> headers = getHeaderList(clz);
			Collections.sort(headers);
			if (null != objs && objs.size() > 0) {
				int sheetCount = objs.size() % pageSize == 0 ? objs.size() / pageSize : objs.size() / pageSize + 1;
				for (int i = 1; i <= sheetCount; i++) {
					HSSFSheet sheet = null;
					if (!StringUtils.isEmpty(sheetName)) {
						sheet = wb.createSheet(sheetName + i);
					} else {
						sheet = wb.createSheet();
					}
					HSSFRow row = sheet.createRow(0);
					// 写标题
					CellStyle titleStyle = setCellStyle(wb, POSITION_TITLE);
					for (int m = 0; m < headers.size(); m++) {
						HSSFCell cell = row.createCell(m);
						cell.setCellStyle(titleStyle);
						cell.setCellValue(headers.get(m).getTitle());
						sheet.setColumnWidth(m, 5000); // 设置每列的宽度
					}
					// 写数据
					Object obj = null;
					CellStyle bodyStyle = setCellStyle(wb, POSITION_BODY);
					CellStyle bodyIntStyle = setCellStyle(wb, POSITION_BODY_INT);
					CellStyle bodyDecimalStyle = setCellStyle(wb, POSITION_BODY_DECIMAL);
					int begin = (i - 1) * pageSize;
					int end = (begin + pageSize) > objs.size() ? objs.size() : (begin + pageSize);
					int rowCount = 1;
					for (int n = begin; n < end; n++) {
						row = sheet.createRow(rowCount);
						rowCount++;
						obj = objs.get(n);
						for (int x = 0; x < headers.size(); x++) {
							Cell cell = row.createCell(x);
							ExcelHeader header = headers.get(x);
							String value = BeanUtils.getProperty(obj, getMethodName(header));
							if (BigDecimal.class.isAssignableFrom(header.getReturnType())
									|| Double.class.isAssignableFrom(header.getReturnType())
									|| Float.class.isAssignableFrom(header.getReturnType())) {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								if (!StringUtils.isEmpty(value)) {
									cell.setCellValue(new Double(value));
								}
								cell.setCellStyle(bodyDecimalStyle);
							} else if (Integer.class.isAssignableFrom(header.getReturnType())) {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								if (!StringUtils.isEmpty(value)) {
									cell.setCellValue(new Integer(value));
								}
								cell.setCellStyle(bodyIntStyle);
							} else {
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(value);
								cell.setCellStyle(bodyStyle);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成excel失败");
		}
		return wb;
	}

	/**
	 * 
	 * @param outPath
	 *            输出路径
	 * @param objs
	 *            数据
	 * @param clz
	 *            数据类
	 * @param sheetName
	 *            表单名称
	 * @param pageSize
	 *            每页数量
	 */
	public void exportObj2ExcelByTemplate(String outPath, String templatePath, List objs, Class clz, int startRow) {
		Workbook wb = handleObj2ExcelByTemplate(templatePath, objs, clz, startRow);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导出对象到Excel，不是基于模板的，直接新建一个Excel完成导出，基于路径的导出
	 *
	 * @param outPath
	 *            输出路径
	 * @param objs
	 *            数据源
	 * @param clz
	 *            类
	 * @param sheetName
	 *            分sheet导出是sheet的名字 ， 如 “sheet” -> sheet1,sheet2...
	 * @param pageSize
	 *            每个sheet要显示多少条数据
	 */
	public HSSFWorkbook handleObj2ExcelByTemplate(String templatePath, List objs, Class clz, int startRow) {
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(templatePath));
			// 获取表头
			List<ExcelHeader> headers = getHeaderList(clz);
			Collections.sort(headers);
			int totalRow = 0;
			if (!headers.isEmpty()) {
				ExcelHeader header = headers.get(headers.size() - 1);
				totalRow = header.getOrder();
			}
			if (null != objs && objs.size() > 0) {
				HSSFSheet sheet = wb.getSheetAt(0);
				// 写数据
				Object obj = null;
				CellStyle bodyStyle = setCellStyle(wb, POSITION_BODY);
				CellStyle bodyIntStyle = setCellStyle(wb, POSITION_BODY_INT);
				CellStyle bodyDecimalStyle = setCellStyle(wb, POSITION_BODY_DECIMAL);
				for (int n = 0; n < objs.size(); n++) {
					HSSFRow row = sheet.createRow(startRow);
					if (n == 196) {
						System.out.println(n);
					}
					startRow++;
					obj = objs.get(n);
					int blankNum = 0;
					for (int x = 0; x < totalRow; x++) {
						Cell cell = row.createCell(x);
						ExcelHeader header = headers.get(x - blankNum);
						if (x + 1 != header.getOrder()) {
							blankNum++;
							cell.setCellStyle(bodyStyle);
							continue;
						}
						String value = BeanUtils.getProperty(obj, getMethodName(header));
						if (BigDecimal.class.isAssignableFrom(header.getReturnType())
								|| Double.class.isAssignableFrom(header.getReturnType())
								|| Float.class.isAssignableFrom(header.getReturnType())) {
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							if (!StringUtils.isEmpty(value)) {
								cell.setCellValue(new Double(value));
							} else {
								cell.setCellValue("");
							}
							cell.setCellStyle(bodyDecimalStyle);
						} else if (Integer.class.isAssignableFrom(header.getReturnType())) {
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							if (!StringUtils.isEmpty(value)) {
								cell.setCellValue(new Integer(value));
							}
							cell.setCellStyle(bodyIntStyle);
						} else {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellStyle(bodyStyle);
							cell.setCellValue(value);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成excel失败");
		}
		return wb;
	}

	/**
	 * 设置单元格样式
	 *
	 * @param position
	 *            ["body","title"]
	 */
	public CellStyle setCellStyle(Workbook workBook, String position) {

		CellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 设置单元格字体
		Font headerFont = workBook.createFont(); // 字体
		if (POSITION_TITLE.equals(position)) {
			headerFont.setFontHeightInPoints((short) 12);
		} else if (POSITION_BODY.equals(position)) {
			headerFont.setFontHeightInPoints((short) 10);
			HSSFDataFormat format = (HSSFDataFormat) workBook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("TEXT"));
		} else if (POSITION_BODY_INT.equals(position)) {
			headerFont.setFontHeightInPoints((short) 10);
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		} else if (POSITION_BODY_DECIMAL.equals(position)) {
			headerFont.setFontHeightInPoints((short) 10);
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		}
		headerFont.setFontName("宋体");
		if (POSITION_TITLE.equals(position)) {
			headerFont.setBoldweight((short) 10);
		}
		cellStyle.setFont(headerFont);
		cellStyle.setWrapText(true);

		cellStyle.setFillBackgroundColor(HSSFCellStyle.THICK_FORWARD_DIAG);
		// 设置单元格边框及颜色
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setWrapText(true);

		cellStyle.setLeftBorderColor(HSSFColor.BLACK.index); // 设置边框颜色
		cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);

		return cellStyle;
	}

	/**
	 * 创建空单元格
	 * 
	 * @param row
	 * @param style
	 * @param index
	 */
	public void createBlankCell(HSSFRow row, CellStyle style, int index) {
		HSSFCell cell = row.createCell(index);
		cell.setCellStyle(style);
	}

	/**
	 * 从文件路径读取相应的Excel文件到对象列表
	 * 
	 * @param path
	 *            文件路径下的path
	 * @param clz
	 *            对象类型
	 * @param readLine
	 *            开始行，注意是标题所在行
	 * @param tailLine
	 *            底部有多少行，在读入对象时，会减去这些行
	 * @return
	 */
	public List<?> readExcel2ObjsByPath(InputStream inputStream, Class clz, int readLine, int tailLine) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(inputStream);
			return handlerExcel2Objs(wb, clz, readLine, tailLine);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 从文件路径读取相应的Excel文件到对象列表，标题行为0，没有尾行
	 * 
	 * @param path
	 *            路径
	 * @param clz
	 *            类型
	 * @return 对象列表
	 */
	public List<?> readExcel2ObjsByPath(InputStream inputStream, Class clz) {
		return this.readExcel2ObjsByPath(inputStream, clz, 0, 0);
	}

	/**
	 * 
	 * @Title: download
	 * @Description: 导出
	 * @param wb
	 * @param fileName
	 * @param response
	 * @throws IOException
	 * @return: void
	 */
	public void download(HSSFWorkbook wb, String fileName, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] content = os.toByteArray();
		InputStream is = new ByteArrayInputStream(content);

		// 设置response的参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesread;

			while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}

	}

}
