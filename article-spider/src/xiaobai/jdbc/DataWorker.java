package xiaobai.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xiaobai.base.Channel;
import xiaobai.channel.Article;
import xiaobai.channel.ArticleSource;
import xiaobai.channel.Category;
import xiaobai.channel.User;
import xiaobai.channel.WeixinSource;
import xiaobai.html.SpiderHttpClient;

import com.alibaba.fastjson.JSON;

public class DataWorker {
	static final Logger loggerAll = LogManager.getLogger("SpiderAll");
	static final Logger loggerError = LogManager.getLogger("SpiderError");
	static final Logger loggerSpider = LogManager.getLogger("Spider");
	
	private String aql1 = "INSERT INTO comment(id,user_name,article_id,comment,created,status) (SELECT ?,c.user_name,tcar.id,c.comment,FROM_UNIXTIME(c.created,'%Y-%m-%d %h:%i:%s'),? from comment_copy c, t_channel_article_relation tcar where tcar.source_article_id = c.article_id and tcar.source_name='搜狐' and c.article_id =? )";
	private static PreparedStatement ps;
	private DatagramPacket recvPacket;
	private static Connection conn = null;
	private static Connection xiaobai_conn = null;
	private static int i = 1;
	private static String ArticleCommentUrl = "http://www.toutiao.com/group/%article%/comments/?count=%comment_num%";

	public DataWorker() {
		this.recvPacket = recvPacket;
		if( conn == null )
		conn = C3p0Pool.getConnection();
		// ps = conn.prepareStatement(sql);
		if(xiaobai_conn == null)
		xiaobai_conn = C3p0Pool.getXiaoBaiConnection();
	}

	// public void insertDatas(){
	public static void main(String[] args) {
		try {
			
			//获取搜狐的渠道数据
			DataWorker db = new DataWorker();
			//Article a = new Article(1000, "bb");
			//loggerAll.debug(db.insertArticle(a));
			//db.queryArticle(1000);
			//loggerAll.debug(arg0);
//			Channel channel = new Channel();
//			channel.setName("微信5");
//			channel.setDesc("微信是个牛逼的渠道");
//			int channel_id = db.insertChannel(channel);
//			loggerAll.debug(channel_id);
//			channel.setId(channel_id);
//			ArticleSource as = new ArticleSource();
//			as.setName("正能量成功学");
//			as.setDesc("每天为你分享正能量~传播成功学~成就中国梦~我们学习了正能量才能更成功.");
//			as.setFeatureCode("oIWsFt3kD3sk1S96_3DknllqqOYg");
//			as.setChannelInfo(channel);
//			loggerAll.debug(db.insertArticleSource(as));
			
			loggerAll.debug(JSON.toJSONString(db.queryChannelByName("微信")));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	//插入渠道并返回渠道ID
	public int insertChannel(Channel channel) throws Exception{
		//检查是否已存在
		Channel dbChannel = queryChannelByName(channel.getName());
		if( dbChannel != null ) return dbChannel.getId();
		
		String sql = "insert into t_channel("
				+ "name,"
				+ "description"
				+ ") values (?,?)";
		ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, channel.getName());
		ps.setString(2, channel.getDesc());
		loggerAll.debug(sql);

		ps.executeUpdate();		
		
		ResultSet rs = ps.getGeneratedKeys(); 
		
		if(rs.next()){
			return rs.getInt(1);
		}
		return -1;
		
	}
	
	//查询子分类
	public List<Category> queryCategory() throws Exception{
		String sql = "select "
				+ "id,"
				+ "name,"
				+ "categoryID"
				+ " from category_subclass where status = 0";
		loggerAll.debug(sql);
		Statement stmt = xiaobai_conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		List<Category> categorys = new ArrayList<Category>();
		while(r.next())
		{
			Category c = new Category();
			c.setId(r.getInt("id"));
			c.setName(r.getString("name"));
			c.setCategoryID(r.getInt("categoryID"));
			categorys.add(c);
		}
		
		return categorys;
	}
	
	//查询
	public Channel queryChannelByName(String channelName) throws Exception{
		String sql = "select "
				+ "id,"
				+ "name,"
				+ "description"
				+ " from t_channel where name = "
				+ channelName;
		loggerAll.debug(sql);
		ResultSet r = ps.executeQuery(sql);
		if(r.next())
		{
			Channel c = new Channel();
			c.setId(r.getInt("id"));
			c.setName(r.getString("name"));
			c.setDesc(r.getString("description"));
			
			return c;
		}
		
		return null;
	}
	public Channel queryChannelByID(String channelID) throws Exception{
		String sql = "select "
				+ "id,"
				+ "name,"
				+ "description"
				+ " from t_channel where id = "
				+ channelID;
		loggerAll.debug(sql);
		Statement stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		if(r.next())
		{
			Channel c = new Channel();
			c.setId(r.getInt("id"));
			c.setName(r.getString("name"));
			c.setDesc(r.getString("description"));
			
			return c;
		}
		
		return null;
	}
	public List<Channel> queryChannels() throws Exception{
		String sql = "select "
				+ "id,"
				+ "name,"
				+ "description"
				+ " from t_channel";
		loggerAll.debug(sql);
		Statement stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		List<Channel> channels = new ArrayList<Channel>();
		while(r.next())
		{
			Channel c = new Channel();
			c.setId(r.getInt("id"));
			c.setName(r.getString("name"));
			c.setDesc(r.getString("description"));
			
			channels.add(c);
		}
		
		return channels;
	}
	/**************文章源接口**************/
	//插入数据源,并返回数据源ID
	public int insertArticleSource(ArticleSource articleSource) throws Exception{
		String sql = "insert into t_article_source("
				+ "channel_id,"
				+ "feature_code,"
				+ "description"
				+ ") values (?,?,?)";
		ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, articleSource.getChannelID());
		ps.setString(2, articleSource.getFeatureCode());
		ps.setString(3, articleSource.getDesc());

		ps.executeUpdate();		
		
		ResultSet rs = ps.getGeneratedKeys(); 
		
		if(rs.next()){
			return rs.getInt(1);
		}
		return -1;
		
	}
	//根据渠道类型和特征码查询渠道
	public ArticleSource queryArticleSource(int channelID, String featureCode) throws Exception{
		String sql = "select "
				+ "source_id,"
				+ "channel_id,"
				+ "feature_code,"
				+ "name,"
				+ "description"
				+ " from t_article_source where"
				+ " source_id = "+ channelID 
				+ " and feature_code = "+featureCode;
		loggerAll.debug(sql);
		Statement stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		if(r.next())
		{
			ArticleSource as = new ArticleSource();
			as.setId(r.getInt("source_id"));
			as.setName(r.getString("name"));
			as.setDesc(r.getString("description"));

			
			return as;
		}
		
		return null;
	}
	//查找渠道相关数据源
	public List<ArticleSource> querySourcesByChannel(Channel channel) throws Exception{
		String sql = "select "
				+ "source_id,"
				+ "channel_id,"
				+ "feature_code,"
				+ "name,"
				+ "description,"
				+ "category"
				+ " from t_article_source where"
				+ " channel_id = "+ channel.getId();
		loggerAll.debug(sql);
		Statement stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		List<ArticleSource> sources = new ArrayList<ArticleSource>();
		while(r.next())
		{
			ArticleSource as = null;
			if( channel.getName().equals("微信") ){
				as = new WeixinSource();
			}else{
				as = new ArticleSource();
			}
			as.setId(r.getInt("source_id"));
			as.setChannelID(channel.getId());
			as.setFeatureCode(r.getString("feature_code"));
			as.setName(r.getString("name"));
			as.setDesc(r.getString("description"));
			as.setCategory(r.getInt("category"));

			sources.add(as);
		}
		
		return sources;
	}
	//截取字符串前
	private String subString(String s, int length){
		if(s.length() <= length) return s;
		return s.substring(0, length);
	}
	/**************文章接口**************/
	//插入文章并返回文章ID
	public int insertArticle(Article article) throws Exception {
		String sql = "insert into t_article("
				+ "source_id,"
				+ "article_source_id,"
				+ "title,"
				+ "category,"
				+ "send_time,"
				+ "url,"
				+ "content,"
				+ "summary,"
				+ "show_img_url"
				+ ") values (?,?,?,?,?,?,?,?,?)";
		loggerAll.debug(sql+","+article.getTitle());

		ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, article.getSourceID());
		ps.setString(2, article.getArticleSourceID());
		ps.setString(3, subString(article.getTitle(), 45));
		ps.setInt(4,article.getCategory());
		ps.setString(5, article.getSendTime());
		ps.setString(6, article.getUrl());
		ps.setString(7, article.getContent());
		ps.setString(8, subString(article.getSummary(),128));
		ps.setString(9, article.getShowImgUrl());
		ps.executeUpdate();		
		
		ResultSet rs = ps.getGeneratedKeys(); 
		
		if(rs.next()){
			return rs.getInt(1);
		}
		return -1;
	}
	//微信文章插入文章并返回文章ID
	public int insertWeixinArticle(Article article) throws Exception {
			String sql = "insert into t_article("
					+ "source_id,"
					+ "category_superclass_id,"
					+ "title,"
					+ "category,"
					+ "send_time,"
					+ "url,"
					+ "content,"
					+ "summary,"
					+ "show_img_url"
					+ ") values (?,?,?,?,?,?,?,?,?)";
			loggerAll.debug(sql+","+article.getTitle());

			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, article.getSourceID());
			ps.setInt(2, article.getCategoryID());
			ps.setString(3, subString(article.getTitle(), 45));
			ps.setInt(4,article.getCategory());
			ps.setString(5, article.getSendTime());
			ps.setString(6, article.getUrl());
			ps.setString(7, article.getContent());
			ps.setString(8, subString(article.getSummary(),128));
			ps.setString(9, article.getShowImgUrl());
			ps.executeUpdate();		
			
			ResultSet rs = ps.getGeneratedKeys(); 
			
			if(rs.next()){
				return rs.getInt(1);
			}
			return -1;
		}	
	//微信查找文章
		public List<Article> queryWeixinrticles(int sourceID) throws Exception {
			String sql = "select "
					+ "article_id,"
					+ "source_id,"
					+ "category_superclass_id,"
					+ "title,"
					+ "category,"
					+ "send_time,"
					+ "url,"
					+ "content,"
					+ "summary,"
					+ "show_img_url"
					+ " from t_article where source_id = "
					+ sourceID;
			loggerAll.debug(sql);
			Statement stmt = conn.createStatement();
			ResultSet r = stmt.executeQuery(sql);
			List<Article> articles = new ArrayList<Article>();
			while(r.next())
			{
				Article a = new Article();
				a.setId(r.getInt("article_id"));
				a.setSourceID(sourceID);
				a.setCategoryID(r.getInt("category_superclass_id"));
				a.setTitle(r.getString("title"));
				a.setCategory(r.getInt("category"));
				a.setSendTime(r.getString("send_time"));
				a.setUrl(r.getString("url"));
				//a.setContent(r.getString("content"));
				a.setSummary(r.getString("summary"));
				a.setShowImgUrl(r.getString("show_img_url"));
				
				articles.add(a);
			}
			
			return articles;
		}	
	
	
	//查找文章
	public List<Article> queryArticles(int sourceID) throws Exception {
		String sql = "select "
				+ "article_id,"
				+ "source_id,"
				+ "article_source_id,"
				+ "title,"
				+ "category,"
				+ "send_time,"
				+ "url,"
				+ "content,"
				+ "summary,"
				+ "show_img_url"
				+ " from t_article where source_id = "
				+ sourceID;
		loggerAll.debug(sql);
		Statement stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		List<Article> articles = new ArrayList<Article>();
		while(r.next())
		{
			Article a = new Article();
			a.setId(r.getInt("article_id"));
			a.setSourceID(sourceID);
			a.setArticleSourceID(r.getString("article_source_id"));
			a.setTitle(r.getString("title"));
			a.setCategory(r.getInt("category"));
			a.setSendTime(r.getString("send_time"));
			a.setUrl(r.getString("url"));
			//a.setContent(r.getString("content"));
			a.setSummary(r.getString("summary"));
			a.setShowImgUrl(r.getString("show_img_url"));
			
			articles.add(a);
		}
		
		return articles;
	}	
	//查找文章
	public Article queryArticleByID(int articleID) throws Exception {
		String sql = "select "
				+ "article_id,"
				+ "source_id,"
				+ "article_source_id,"
				+ "title,"
				+ "category,"
				+ "send_time,"
				+ "url,"
				+ "content,"
				+ "summary,"
				+ "show_img_url"
				+ " from t_article where article_id = "
				+ articleID;
		loggerAll.debug(sql);
		Statement stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(sql);
		if(r.next())
		{
			Article a = new Article();
			a.setId(r.getInt("article_id"));
			a.setArticleSourceID(r.getString("article_source_id"));
			a.setTitle(r.getString("title"));
			a.setCategory(r.getInt("category"));
			a.setSendTime(r.getString("send_time"));
			a.setUrl(r.getString("url"));
			a.setContent(r.getString("content"));
			a.setSummary(r.getString("summary"));
			a.setShowImgUrl(r.getString("show_img_url"));
			
			return a;
		}
		
		return null;
	}		

	public void insert36kr(ArticleSource channel) throws Exception {
//		Map articleMap = channel.getArticles();
//		for (Object articeId : articleMap.keySet()) {
//			Article article = (Article) articleMap.get(articeId);
//		
//			
//			try {
//				String sql = "insert into kr36_article(source_article_id,article_title,article_sort,send_time,source_url,content,summary,author,source_name,img_url) values (?,?,?,?,?,?,?,?,?,?)";
//				ps = conn.prepareStatement(sql);
//				ps.setString(1, article.getId());
//				ps.setString(2, article.getTitle());
//				ps.setString(3, article.getSort());
//				ps.setString(4,article.getSendTime());
//				ps.setString(5, article.getUrl());
//				ps.setString(6, article.getContent());
//				ps.setString(7, article.getSummary());
//				ps.setString(8, article.getAuthor());
//				ps.setString(9, article.getSourceName());
//				ps.setString(10, article.getImgUrl());
//				ps.execute();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				continue;
//			}
//			i++;
//		}
	}
	
	

	
	public boolean deleteSourceData(){
		String sql = "delete from source_message_copy";
		try {
			ps = conn.prepareStatement(sql);
			ps.execute(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean callProcedure(){
		String sql = "call p_article_statistics";
		try {
			ps = conn.prepareStatement(sql);
			ps.execute(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}




	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static String readFileByChars(String fileName) {

		File file = new File("G:\\index\\toutiao_json.txt");
		InputStream in = null;
		byte[] tempByte = new byte[1024];
		int byteread = 0;
		StringBuffer sb = new StringBuffer();
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			in = new FileInputStream(file);
			while ((byteread = in.read(tempByte)) != -1) {
				// System.out.write(tempByte, 0, byteread);
				sb.append(new String(tempByte, 0, byteread));
			}
			// System.out.println(sb.toString());
			return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/**
		 * fileName = "G:\\index\\toutiao_json.txt"; File file = new
		 * File(fileName); Reader reader = null; byte[] tempchars = new
		 * byte[1024*2]; StringBuffer sb = new StringBuffer(); try {
		 * System.out.println("以字符为单位读取文件内容，一次读一个字节："); // 一次读一个字符 reader = new
		 * InputStreamReader(new FileInputStream(file)); int byteread=0; while
		 * ((byteread = reader..read(tempchars)) != -1 ) { //
		 * 对于windows下，\r\n这两个字符在一起时，表示一个换行。 // 但如果这两个字符分开显示时，会换两次行。 //
		 * 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。 if (((char) tempchar) != '\r') { //
		 * System.out.print((char) tempchar); sb.append(tempchar); } }
		 * reader.close(); return sb.toString(); } catch (Exception e) {
		 * e.printStackTrace(); } try {
		 * System.out.println("以字符为单位读取文件内容，一次读多个字节："); // 一次读多个字符 char[]
		 * tempchars = new char[30]; int charread = 0; reader = new
		 * InputStreamReader(new FileInputStream(fileName)); //
		 * 读入多个字符到字符数组中，charread为一次读取字符数 while ((charread =
		 * reader.read(tempchars)) != -1) { // 同样屏蔽掉\r不显示 if ((charread ==
		 * tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
		 * sb.append(tempchars); //System.out.print(tempchars); } else { for
		 * (int i = 0; i < charread; i++) { if (tempchars[i] == '\r') {
		 * continue; } else { System.out.print(tempchars[i]); } } } }
		 * 
		 * return sb.toString(); } catch (Exception e1) { e1.printStackTrace();
		 * } finally { if (reader != null) { try { reader.close(); } catch
		 * (IOException e1) { } } }
		 */
		return null;
		// return sb;
	}



	/**
	 * @return json数据转换object
	 *         <p>
	 *         Title: jsonToObject
	 *         </p>
	 *         <p>
	 *         Description:TODO
	 *         </p>
	 * @param @return 设定文件
	 * @return Channel 返回类型
	 * @throws
	 */
	public static Map jsonToObject() {

		String channelString = readFileByChars("");

		// System.out.println(channelString);
		// Channel ch = (Channel) JSON.parse(channelString);
		return (Map) JSON.parseObject(channelString);

	}



	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 */
	public static String readFileByCharss(String fileName) {
		fileName = "G:\\index\\toutiao_json.txt";
		StringBuffer sb = new StringBuffer();
		try {
			String encoding = "GBK";
			File file = new File(fileName);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file));// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
				return sb.toString();
			} else {
				System.out.println("找不到指定的文件");
				return null;
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
			return null;
		}

	}

}