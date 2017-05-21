package xiaobai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializableUtil {
	static final Logger logger = LogManager.getLogger(SerializableUtil.class.getName());
	private static  ObjectOutputStream os = null;
	private static  ObjectInputStream is = null;
	/**
	 * 把 pojo类序列化到本地程序
	* @param @param Object  pojo类
	* @param @param path 设定文件  存放的路径，默认路径为f:/pojo/
	* @return  void 返回类型 
	* @throws
	 */
	public static void write(Object Object,String path,String fileName){
		if(StringUtil.isBlank(path)){
			path = "f:/pojo/";
		}
		File file = new File(path+"/");
		if(!file.exists()){
			file.mkdir();
		}
		try {
			if(StringUtil.isBlank(fileName)){
				fileName = getSerializaFileName()+".pojo";
			}
			os = new ObjectOutputStream(new FileOutputStream(new File(path+"/"+fileName)));
			os.writeObject(Object);
			logger.debug("pojo Serializable success....");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("对象序列化，没有发现文件夹");
		} catch (IOException e) {
			logger.error("对象序列化，IO出现异常");
			e.printStackTrace();
		}finally{
			if(os !=null){
				try {
					os.close();
				} catch (IOException e) {
					logger.error("对象序列化，对象输出流关闭出现异常");
					e.printStackTrace();
				}  
			}
		}
	}
	
	/**
	 * 读取pojo序列化文件
	* @param @param path
	* @param @return 设定文件 
	* @return  String 返回类型 
	* @throws
	 */
	public static Object read(String path){
		
		if(StringUtil.isBlank(path)){
			logger.debug("pojo序列化文件为空");
			return null;
		}
		
		File file = new File(path);
		try {
			is = new ObjectInputStream(new FileInputStream(file));
			Object obj = is.readObject();
			logger.debug("pojo file read success");
			return obj;
		} catch (FileNotFoundException e) {
			logger.error("没有pojo序列化文件");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("流读取异常");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(is !=null){
				try {
					is.close();
				} catch (IOException e) {
					logger.error("对象序列化，对象输出流关闭出现异常");
					e.printStackTrace();
				}  
			}
		}  
		
		return null;
	}
	
	
	public static String getSerializaFileName(){
		String fileName = DateUtil.getDate2String(new Date(), "yyyy-MM-dd-ss");
		return fileName;
	}
	
	public static void main(String[] args) {
		getSerializaFileName();
		System.out.println(getSerializaFileName());
	}
	
}
