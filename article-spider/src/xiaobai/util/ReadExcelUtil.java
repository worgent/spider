package xiaobai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadExcelUtil {
	static final Logger logger = LogManager.getLogger(ReadExcelUtil.class.getName());
    private  Workbook wb;
    
    public ReadExcelUtil(){
    	
    }
    
    public ReadExcelUtil(String path){
    	readRxcel(path);
    }
    
    public static  void main(String[] args) {
    	ReadExcelUtil readExcelUtil = new ReadExcelUtil("C:\\Users\\Lenovo\\Desktop\\html\\微信源对应抓取表2014.11.21.xlsx");
    	String[] titles = readExcelUtil.readExcelTitle(0);
    	System.out.println(titles.toString());
    	Map<Integer,Map<Integer, String>> map = readExcelUtil.readXlsxContent(0);
    	System.out.println(map.toString());
	}
	
	public  Sheet readRxcel(String path){
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			wb = WorkbookFactory.create(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return null;
	}
	
	   /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(int sheetIndex) {
         
        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        logger.debug("读取excel,第"+colNum+"列");
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
        	//获取excel内容
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }
    
    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                     
                    cellvalue = DateUtil.getDate2String(date, "yyyy-MM-dd");
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    
    /**
     * 获取Excel单元格的内容，返回Map,key做行号
    *<p>Title: readXlsxContent</p> 
    *<p>Description:TODO</p> 
    * @param @param sheetIndex 设定文件 
    * @return  void 返回类型 
    * @throws
     */
	public  Map<Integer, Map<Integer, String>> readXlsxContent(int sheetIndex){
		 Map<Integer, Map<Integer, String>> content = new HashMap<Integer, Map<Integer, String>>();
	        String str = "";
	        
	        Sheet sheet = wb.getSheetAt(sheetIndex);
	        Row row = sheet.getRow(0);
	        // 得到总行数
	        int rowNum = sheet.getLastRowNum();
	        row = sheet.getRow(0);
	        int colNum = row.getPhysicalNumberOfCells();
	        // 正文内容应该从第二行开始,第一行为表头的标题
	        for (int i = 1; i <= rowNum; i++) {
	            row = sheet.getRow(i);
	            int j = 0;
	            Map<Integer,String> colMap = new HashMap<Integer,String>();
	            while (j < colNum) {
	                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
	                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
	                // str += getStringCellValue(row.getCell((short) j)).trim() +
	                // "-";
	            	colMap.put(j,  getCellFormatValue(row.getCell((short) j)).trim());
	                j++;
	            }
	            content.put(i, colMap);
	            str = "";
	        }
	        return content;
	}
	/**
     * 获取Excel单元格的内容，返回Map,key做行号
    *<p>Title: readXlsxContent</p> 
    *<p>Description:TODO</p> 
    * @param @param sheetIndex 设定文件 
    * @return  void 返回类型 
    * @throws
     */
	public  Map<Integer, String> readWeiXinExcelContent(int sheetIndex){
		 Map<Integer, String> content = new HashMap<Integer,String>();
	        
	        Sheet sheet = wb.getSheetAt(sheetIndex);
	        Row row = sheet.getRow(0);
	        // 得到总行数
	        int rowNum = sheet.getLastRowNum();
	        row = sheet.getRow(0);
	        int colNum = row.getPhysicalNumberOfCells();
	        // 正文内容应该从第二行开始,第一行为表头的标题
	        for (int i = 1; i <= rowNum; i++) {
	            row = sheet.getRow(i);
	            int j = 0;
	            Map<Integer,String> colMap = new HashMap<Integer,String>();
	            while (j < colNum) {
	                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
	                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
	                // str += getStringCellValue(row.getCell((short) j)).trim() +
	                // "-";
	            	if(j==4){
	            		content.put(i, getCellFormatValue(row.getCell((short) j)).trim());
	            		break;
	            	}
	                j++;
	            }
	            
	        }
	        return content;
	}
}
