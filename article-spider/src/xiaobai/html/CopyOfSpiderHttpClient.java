package xiaobai.html;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.helper.StringUtil;

import com.sun.jna.platform.win32.Crypt32Util;

 

/**
 * @desc 实现抓起指定网页内容功能,并且可以利用chrome浏览器访问需要登录状态的页面;
 * 		 加载chrome浏览器对应的cookie,注意chrome版本需要低于33,高于33版本的浏览器cookie进行了加密.
 * 		 
 * @author worgen
 *
 */

public class CopyOfSpiderHttpClient {
	static final Logger logger = LogManager.getLogger(CopyOfSpiderHttpClient.class.getName());

	//网页编码
	private static String charset = "utf-8";
	//存储网页文件目录
	private static String filepath = "/Users/worgen/Desktop";
	//chrome浏览器cookie存储文件路径
	//private static String DB_FILE = "C:\\Users\\Lenovo\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Cookies";
	private static String DB_FILE = "/Users/worgen/Library/Application Support/Google/Chrome/Default/Cookies";
	//
	//private 
	private static CookieStore  simulateCookieLogin(String url, Boolean isMac){
		String DB_URL = "jdbc:sqlite:" + DB_FILE;
		Connection conn = null;
		Statement stmt = null;
		ResultSet 	 rs = null;
		CookieStore cstore = new BasicCookieStore();
		logger.info("DB_URL,"+DB_URL);
		try{
			Class.forName("org.sqlite.JDBC");
			conn= DriverManager.getConnection(DB_URL);
			System.out.println("opened database successfully");
			
			stmt = conn.createStatement();
			String sql = "select host_key, name, value, path from cookies";
			rs = stmt.executeQuery(sql);
			int i = 0;
			while(rs.next()){
				
				String host_key = rs.getString("host_key");
				
//				if(StringUtil.isBlank(host_key)){
//					System.out.println("host-key 是空值");
//					continue;
//				}
//				if(!(host_key.indexOf("toutiao")  >0) && !(host_key.indexOf("sohu")  >0)){
//					System.out.println("不是搜狐或者头条的网站");
//					continue;
//				};
				String name = rs.getString("name");
				String path = rs.getString("path");
				String isoString = null;
				if( !isMac ){
					
					byte[] Buffer_s = rs.getBytes("encrypted_value");
				
					BufferedInputStream is = null;
		        
				//调用window本地函数，对cookie解密
				
					Buffer_s = Crypt32Util.cryptUnprotectData(Buffer_s);
					isoString = new String(Buffer_s,"ISO-8859-1");
				
					BasicClientCookie cookie = new BasicClientCookie(name, isoString);
					cookie.setDomain(host_key);
					cookie.setVersion(0);
					cookie.setPath(path);
					cstore.addCookie(cookie);
				}
				else
				{
					String value = rs.getString("value");
					BasicClientCookie cookie = new BasicClientCookie(name, value);
					cookie.setDomain(host_key);
					cookie.setVersion(0);
					cookie.setPath(path);
					cstore.addCookie(cookie);	
					logger.debug("addcookie,"+host_key+","+path+","+name+","+value);
				}
			}
			return cstore;
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+":"+e.getMessage());
		//	System.exit(0);
		}finally{
			try {
				rs.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * @return 
	 * simulateCookieLoginJson 模拟cookie登录，获取json数据
	*<p>Title: simulateCookieLogin</p> 
	*<p>Description:TODO</p> 
	* @param @param url 设定文件 
	* @return  void 返回类型 
	* @throws
	 */
	public static String simulateCookieLoginJson(String url){
		CookieStore cstore = simulateCookieLogin(url, false);
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cstore).build();
		HttpContext hc = new BasicHttpContext();
		hc.setAttribute("http.cookie-store", cstore);
		HttpGet hg = new HttpGet(url);
		HttpResponse response;
		try {
			response = httpclient.execute(hg, hc);
			HttpEntity entity = response.getEntity();
			InputStream htm_in = null;
			if( entity != null ){
				System.out.println(entity.getContentLength());
				htm_in = entity.getContent();
				return InputStream2String(htm_in, charset);
			}	
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		return null;
	}
		
	/**
	 * 目前只支持google的 3.2半杯
	 * @desc 访问指定url读取内容
	 * @param url-要访问网址
	 * @return 返回该网址对应的网页内容
	 * 
	 */
	public static String read_html(String url) {
		
		try{
			CookieStore cstore = simulateCookieLogin(url, true); 
			//cstore.clear();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cstore).build();
			HttpContext hc = new BasicHttpContext();
			hc.setAttribute("http.cookie-store", cstore);
			
			HttpGet hg = new HttpGet(url);
			//hg.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.102 Safari/537.37");    
			hg.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36 QQBrowser/3.1.2824.400");    
			HttpResponse response = httpclient.execute(hg, hc);
			HttpEntity entity = response.getEntity();
			
			InputStream htm_in = null;
			if( entity != null ){
				htm_in = entity.getContent();
				String htm_str = InputStream2String(htm_in, charset);
				String urlTrans = url.replace("/", "\\/");
				saveHtml(filepath+"test.html", htm_str);
				return htm_str;
			}			
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		} 
		System.out.println("operation done successfully");
		return "";
	}
	/**
	 * 
	 */
	public static void saveHtml(String filepath, String str)
	{
		try{
			//
			File file = new File(filepath);
			if( file.exists() )
			{
				file.delete();
			}
			file.createNewFile();
			
			OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
			outs.flush();
			outs.write(str);
			outs.close();
		}catch(IOException e){
			System.out.println("Error at save html");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public static String InputStream2String(InputStream in_st, String charset) throws IOException{
		BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
		StringBuffer res = new StringBuffer();
		String line = "";
		
		while((line = buff.readLine()) != null){
			res.append(line);
			res.append('\n');
		}
		
		return res.toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(new CopyOfSpiderHttpClient().read_html("http://chuansong.me/n/2216336"));
//		CopyOfSpiderHttpClient.read_html("http://mp.toutiao.com/articles/");
	}
}
