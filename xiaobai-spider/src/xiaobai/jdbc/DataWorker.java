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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xiaobai.analyze.Kr36Analyze;
import xiaobai.analyze.SouHuAnalyze;
import xiaobai.analyze.ToutiaoAnalyze;
import xiaobai.channel.Article;
import xiaobai.channel.ArticleDailyData;
import xiaobai.channel.Channel;
import xiaobai.channel.Comment;
import xiaobai.channel.User;
import xiaobai.html.SpiderHttpClient;

import com.alibaba.fastjson.JSON;

public class DataWorker {
	// static CacheManager manager =
	// CacheManager.create(DataWorker.class.getResource("/ehcache.xml"));
	// static Cache cache = manager.getCache("oneday");
	// private String sql =
	// "insert into comment_copy(id,user_name,article_id,comment,created) values (?, ?, ?, ?, ?)";
	private String aql1 = "INSERT INTO comment(id,user_name,article_id,comment,created,status) (SELECT ?,c.user_name,tcar.id,c.comment,FROM_UNIXTIME(c.created,'%Y-%m-%d %h:%i:%s'),? from comment_copy c, t_channel_article_relation tcar where tcar.source_article_id = c.article_id and tcar.source_name='搜狐' and c.article_id =? )";
	private static PreparedStatement ps;
	private DatagramPacket recvPacket;
	private static Connection conn;
	private static int i = 1;
	private static String ArticleCommentUrl = "http://www.toutiao.com/group/%article%/comments/?count=%comment_num%";

	public DataWorker() {
		this.recvPacket = recvPacket;
		conn = C3p0Pool.getConnection();
		// ps = conn.prepareStatement(sql);

	}

	// public void insertDatas(){
	public static void main(String[] args) {
		try {
			
//			Channel channel = Kr36Analyze.ArticleAll();
//			new DataWorker().insert36kr(channel);
//			 Channel channel = SouHuAnalyze.getSouhuDatas();
//			 int status = new DataWorker().insertData(channel);
//			 System.out.println(status);
			
			// readFileByChars("");
			// jsonToObject();
			// int status = new DataWorker().insertData(channel);
			// System.out.println("cheng gong le ");
			// readFileByChars("");
			
			//获取搜狐的渠道数据
			DataWorker a = new DataWorker();
			Channel channel = SouHuAnalyze.getSouhuDatas();
//			Channel channel = ToutiaoAnalyze.getChannel();
			a.insertSouhu(channel);
//			a.insertToutiao(channel);
			// intserArticle();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	
	public void insertArticle(Article aritlce) throws Exception {
		String sql = "insert into kr36_article(source_article_id,article_title,article_sort,send_time,source_url,content,summary,author,source_name,img_url) values (?,?,?,?,?,?,?,?,?,?)";
		ps = conn.prepareStatement(sql);
		ps.setString(1, aritlce.getId());
		ps.setString(2, aritlce.getTitle());
		ps.setString(3, aritlce.getSort());
		ps.setString(4,aritlce.getSendTime());
		ps.setString(5, aritlce.getUrl());
		ps.setString(6, aritlce.getContent());
		ps.setString(7, aritlce.getSummary());
		ps.setString(8, aritlce.getAuthor());
		ps.setString(9, aritlce.getSourceName());
		ps.setString(10, aritlce.getImgUrl());
		ps.execute();
	}
	
	public void insert36kr(Channel channel) throws Exception {
		Map articleMap = channel.getArticles();
		for (Object articeId : articleMap.keySet()) {
			Article aritlce = (Article) articleMap.get(articeId);
		
			
			try {
				String sql = "insert into kr36_article(source_article_id,article_title,article_sort,send_time,source_url,content,summary,author,source_name,img_url) values (?,?,?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, aritlce.getId());
				ps.setString(2, aritlce.getTitle());
				ps.setString(3, aritlce.getSort());
				ps.setString(4,aritlce.getSendTime());
				ps.setString(5, aritlce.getUrl());
				ps.setString(6, aritlce.getContent());
				ps.setString(7, aritlce.getSummary());
				ps.setString(8, aritlce.getAuthor());
				ps.setString(9, aritlce.getSourceName());
				ps.setString(10, aritlce.getImgUrl());
				ps.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			i++;
		}
	}
	
	
	public void insertChannelArticles(Channel channel) throws Exception {
		Map articleMap = channel.getArticles();
		for (Object articeId : articleMap.keySet()) {
			Article aritlce = (Article) articleMap.get(articeId);
		
			
			try {
				String sql = "insert into kr36_article(source_article_id,article_title,article_sort,send_time,source_url,content,summary,author,source_name,img_url) values (?,?,?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, aritlce.getId());
				ps.setString(2, aritlce.getTitle());
				ps.setString(3, aritlce.getSort());
				ps.setString(4,aritlce.getSendTime());
				ps.setString(5, aritlce.getUrl());
				ps.setString(6, aritlce.getContent());
				ps.setString(7, aritlce.getSummary());
				ps.setString(8, aritlce.getAuthor());
				ps.setString(9, aritlce.getSourceName());
				ps.setString(10, aritlce.getImgUrl());
				ps.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			i++;
		}
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
	public void insertToutiao(Channel channel){
		Map articleMap;
		try {
			articleMap = channel.getArticles();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		for (Object articeId : articleMap.keySet()) {
			Article aritlce = (Article) articleMap.get(articeId);
			List<ArticleDailyData> visitDetails = aritlce.getVisitDetails();
			
			
			
			String sql = "insert into source_message_copy (article_id,source_name,exposure,read_num,agree_num,disagree_num,collect_num,comment_num,transmit_num,source_link,item_title) values (?,?,?,?,?,?,?,?,?,?,?)";
			try {
				ps = conn.prepareStatement(sql);
				
				ps.setLong(1, Long.parseLong(articeId.toString()));
				ps.setString(2, "头条");
				ps.setLong(3, aritlce.getExposes());
				ps.setInt(4,aritlce.getPageViews());
				ps.setInt(5,aritlce.getVoteUps());
				ps.setInt(6,aritlce.getVoteDowns());
				ps.setInt(7,aritlce.getFavorites());
				ps.setInt(8,aritlce.getCommentNum());
				ps.setInt(9,aritlce.getShares());
				ps.setString(10,aritlce.getUrl());
				ps.setString(11,aritlce.getTitle());
				ps.execute();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			i++;
		}
	}
	
	public void insertSouhu(Channel channel){
		Map articleMap;
		try {
			articleMap = channel.getArticles();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		for (Object articeId : articleMap.keySet()) {
			Article aritlce = (Article) articleMap.get(articeId);
			List<ArticleDailyData> visitDetails = aritlce.getVisitDetails();
			if(visitDetails == null){
				continue;
			}
			int tatalNum = 0;
			int commentNum = 0;
			for (ArticleDailyData data : visitDetails) {
				tatalNum+=data.getAddRedNum();
				commentNum += data.getCommentNum();
			}
			
			String sql = "insert into source_message_copy (article_id,source_name,read_num,comment_num,source_link) values (?,?,?,?,?)";
			try {
				ps = conn.prepareStatement(sql);
				ps.setLong(1, Long.parseLong(articeId.toString()));
				ps.setString(2, "搜狐");
				ps.setLong(3, tatalNum);
				ps.setLong(4,commentNum);
				ps.setString(5, "http://3g.k.sohu.com/t/n" + articeId);
				ps.execute();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			i++;
		}
	}

	/*
	 * 插入数据
	 */
	public int insertData(Channel channel) {
		try {

			Map articleMap = channel.getArticles();
			for (Object articeId : articleMap.keySet()) {
				Article aritlce = (Article) articleMap.get(articeId);
				List<Comment> commentList = aritlce.getComments();
				for (Comment comment : commentList) {
					System.out.println("start db o" + i);
					ps.setInt(1, i);
					ps.setString(2, comment.getAuthorName());
					ps.setInt(3, Integer.parseInt(aritlce.getId()));
					ps.setString(4, comment.getContent());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					ps.setString(5, comment.getSendTime());
					ps.execute();
					System.out.println("end db o" + i);
					i++;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("插入数据出错");

			return -1;
		}
		return 1;
	}
	
	
	public void insertTouTiaoCommentData(Comment comment) throws SQLException{
		String sql = "insert into comment_toutiao(user_name,article_id,comment,created) values (?, ?, ?, ?)";
		ps = conn.prepareStatement(sql);
		ps.setString(1, comment.getAuthorName());
		ps.setString(2, comment.getArticleID());
		ps.setString(3, comment.getContent());
		ps.setString(4, comment.getSendTime());
		ps.execute();
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

	@SuppressWarnings("rawtypes")
	public static void intserArticle() throws NumberFormatException,
			SQLException {
		Map articlesMap = (Map) jsonToObject();
		Map comentMap = (Map) articlesMap.get("articles");
		for (Object articleId : comentMap.keySet()) {
			System.out.println(articleId.toString());
			analyzeComment(articleId.toString(), 1000);
		}

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

	public static List<Comment> analyzeComment(String articleID, int commentNum)
			throws SQLException {

		List<Comment> commentList = new ArrayList<Comment>();
		String url = ArticleCommentUrl.replace("%article%",
				articleID.toString());
		url = url.replace("%comment_num%", Integer.toString(commentNum));
		String urlConent = new SpiderHttpClient().read_html(url);

		Document doc = Jsoup.parse(urlConent);
		System.out.println(doc.title());
		Elements comments = doc.select(".comment-item");
		System.out.println(comments.size());
		for (Element comment : comments) {
			Long id = Long.parseLong(comment.attr("data-comment-id"));
			Element eleAvatar = comment.select("div a").first();
			Element eleImg = eleAvatar.select("img").first();
			String headUrl = eleImg.attr("src");

			String sendTime = comment.select("ul .time").first().text();
			Element eleContent = comment.select("ul li").first();
			String content = eleContent.select(".content").first().text();
			String userName = eleContent.select(".name a").first().text();
			String userUrl = "http://www.toutiao.com"
					+ eleContent.select("span a").first().attr("href");
			String userID = null;
			// 从地址里解析用户ID
			Pattern p = Pattern.compile("http://www.toutiao.com/user/(.*)/");
			Matcher m = p.matcher(userUrl);
			while (m.find()) {
				userID = m.group(1);
				System.out.println("regex find userid:" + userID);
			}
			User user = new User("toutiao");
			user.setId(Long.parseLong(userID));
			user.setName(userName);
			user.setHeadUrl(headUrl);
			user.setHomeUrl(userUrl);

			Comment comm = new Comment(id, content, sendTime, user.getId(),
					articleID.toString());
			comm.setAuthorName(userName);
			commentList.add(comm);
			comm.dump();
		}
		for (Comment comment : commentList) {
			String sql = "insert into comment_copy(id,user_name,article_id,comment,created) values (?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, i);
			ps.setString(2, comment.getAuthorName());
			ps.setString(3, comment.getArticleID());
			ps.setString(4, comment.getContent());
			ps.setString(5, comment.getSendTime());
			ps.execute();
			i++;
		}
		System.out.println("zong gong zhi xing le " + i + "tiao");
		return commentList;
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