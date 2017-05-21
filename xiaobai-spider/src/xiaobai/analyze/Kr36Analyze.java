package xiaobai.analyze;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import com.alibaba.fastjson.JSON;

import xiaobai.channel.Article;
import xiaobai.channel.ArticleDailyData;
import xiaobai.channel.Channel;
import xiaobai.html.SpiderHttpClient;
import xiaobai.jdbc.DataWorker;
import xiaobai.util.SerializableUtil;

public class Kr36Analyze {
	static final Logger logger = LogManager.getLogger(Kr36Analyze.class.getName());
	//渠道信息
	private static Channel channel = new Channel("36kr");
	
	
	private static DataWorker dataWorker  = new DataWorker();
	//基础地址
	private static String RootUrl = "http://www.36kr.com";
	
	private static String ArticleColumnUrl = "http://www.36kr.com/category/column";
	
	private static String ArticleDigestUrl = "http://www.36kr.com/category/digest";
	
	private static String ArticleBreakingUrl = "http://www.36kr.com/category/breaking";
	
	private static String ArticleCN_NewsUrl = "http://www.36kr.com/category/cn-news";
	
	private static String ArticleUS_startupstUrl = "http://www.36kr.com/category/us-startups";
	
	private static String ArticleCN_startupsUrl = "http://www.36kr.com/category/cn-startups";
	
	public static void main(String[] args) throws ParseException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(date);
		Date time = sdf.parse(now);
		ArticleAfterTimeDate(time);
//		ArticleReds(ArticleBreakingUrl);
//		ArticleReds(ArticleColumnUrl);
//		ArticleReds(ArticleDigestUrl);
//		ArticleReds(ArticleCN_NewsUrl);
//		ArticleReds(ArticleUS_startupstUrl);
//		ArticleReds(ArticleCN_startupsUrl);
	}
	
	/**
	 * @throws ParseException 
	 * 获取day之后的数据
	 *<p>Title: ArticleAfterTimeDate</p> 
	 *<p>Description:TODO</p> 
	 * @param @return 设定文件 
	 * @return  Channel 返回类型 
	 * @throws
	 */
	public static Channel ArticleAfterTimeDate(Date day) throws ParseException{
		ArticleReds(ArticleColumnUrl,day);
		ArticleReds(ArticleDigestUrl,day);
		ArticleReds(ArticleBreakingUrl,day);
		ArticleReds(ArticleCN_NewsUrl,day);
		ArticleReds(ArticleUS_startupstUrl,day);
		ArticleReds(ArticleCN_startupsUrl,day);
		return channel;
	}
	
	/**
	 * 获取所有数据
	 *<p>Title: ArticleAll</p> 
	 *<p>Description:TODO</p> 
	 * @param @return 设定文件 
	 * @return  Channel 返回类型 
	 * @throws
	 */
	public static Channel ArticleAll(){
		ArticleReds(ArticleColumnUrl);
		ArticleReds(ArticleDigestUrl);
		ArticleReds(ArticleBreakingUrl);
		ArticleReds(ArticleCN_NewsUrl);
		ArticleReds(ArticleUS_startupstUrl);
		ArticleReds(ArticleCN_startupsUrl);
		return channel;
	}
	/**
	 * 
	*<p>Title: ArticleReds</p> 
	*<p>Description:获取每篇的整体数据</p> 
	* @param @param url
	* @param @param map
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static int ArticleReds(String url){
		Channel channelCopy =new Channel("36kr");				
		logger.debug(url);
		System.out.println(url);
		String urlConent = "";
		if("".equals(url)){
		   urlConent =new SpiderHttpClient().read_html("http://www.36kr.com/category/cn-startups");
		}else{
			urlConent = new SpiderHttpClient().read_html(url);
		}
		Document doc = Jsoup.parse(urlConent);
		Elements redArticleData = doc.select("article");
		if(redArticleData.size() <= 0){
			logger.error("redArticleData elemnt number error, "+ redArticleData.size());
			return 1;
		}else{
			for( Element article : redArticleData ){
				//创建一个文章对象存储文章的信息
				Article articles = new Article();
				//便利获取每一个元素的组
				Elements element1 = article.select("div").get(0).getElementsByIndexEquals(0).select("div span");
				if(element1.isEmpty() || element1==null ){
					continue;
				}
				
				String sort = element1.get(0).select("a").text();//attr("abs:href"); //获取分类
				Elements element = article.select("div");
				String authorTime = element.get(0).getElementsByIndexEquals(1).select("div").text();
				String author = authorTime.substring(0,authorTime.lastIndexOf("•")); //获取作者
				String sendTime = authorTime.substring(authorTime.lastIndexOf("•")+1).trim();//发表时间
				//获取正确格式的统计时间
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(date);
				if(sendTime.indexOf("/")>0 && sendTime.indexOf(":")>sendTime.indexOf("/") ){
					if(sendTime.startsWith("201")){
						sendTime = sendTime.replaceAll("/", "-");
					}else{
						sendTime = "2014-"+sendTime.replaceAll("/", "-");
					}
				}else{
					if(sendTime.endsWith("小时前")){
						sendTime = sendTime.substring(0, sendTime.indexOf("小"));
						long time=date.getTime()-Long.parseLong(sendTime)*60*60*1000;
						sendTime = sdf.format(new Date(time));
					}else if(sendTime.indexOf(":")<sendTime.indexOf("/")){
						sendTime = now.substring(0,now.lastIndexOf("-")+1)+sendTime.substring(sendTime.indexOf("/"))+" "+sendTime.substring(0, sendTime.indexOf("/"));
					}else{
						sendTime = null;
					}
					
				}
				
				String link = element.select("h1").select("a").attr("href");//文章链接
				String a = link.substring(link.lastIndexOf("/")+1,link.lastIndexOf("."));
				String idIndex = ""; //文章ID
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher match=pattern.matcher(a);
				if(match.matches()){
					idIndex = a;
				}else{
					continue;
				}
				
				String imgUrl = element.get(5).select("a").select("img").attr("src");//文章图片
				String title = element.get(6).select("h1 a").text(); //文章标题
				String summary = element.get(6).select("p").text(); //文章简介
				//文章的id存储
				articles.setId(idIndex);
				//文章的名字
				articles.setTitle(title);
				articles.setSort(sort);
				articles.setAuthor(author);
				articles.setSummary(summary);
				articles.setUrl(RootUrl+link);
				articles.setSourceName("36氪");
				String articleContent = articleContent(RootUrl+link); //文章内容
				//文章的创建时间的存储
				articles.setSendTime(sendTime);
				articles.setContent(articleContent);
				articles.setImgUrl(imgUrl);
				
				channel.putArticle(idIndex, articles);
				
				channelCopy.putArticle(idIndex, articles);
				
			}

			if(channelCopy.articleSize()==0){
				return 0;
			}
			//插入数据
			try {
				logger.error("url:"+url);
				dataWorker.insert36kr(channelCopy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//判断时候还有下一页
			//获取总页数
			String nextUrl = doc.select(".next_page").select("a").attr("href");
			if(!"".equals(nextUrl) && !"#".equals(nextUrl)){
				logger.debug(RootUrl+nextUrl);
				ArticleReds(RootUrl+nextUrl);		
				System.out.println(RootUrl+nextUrl);
			}
//			else{
//				channel.setArticles(map);
//			//	System.out.println("the frist map"+channel.getArticles().size());
//			}
		}
		return 0;
	}
	
	/**
	 * @throws ParseException 
	 * 
	*<p>Title: ArticleReds</p> 
	*<p>Description:获取statTime之后的每篇的文章数据</p> 
	* @param @param url
	* @param @param map
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static int ArticleReds(String url,Date statTime) throws ParseException{
		Channel channelCopy =new Channel("36kr");				
		logger.debug(url);
		System.out.println(url);
		String urlConent = "";
		if("".equals(url)){
		   urlConent = new SpiderHttpClient().read_html("http://www.36kr.com/category/cn-startups");
		}else{
			urlConent = new SpiderHttpClient().read_html(url);
		}
		Document doc = Jsoup.parse(urlConent);
		Elements redArticleData = doc.select("article");
		if(redArticleData.size() <= 0){
			logger.error("redArticleData elemnt number error, "+ redArticleData.size());
			return 1;
		}else{
			for( Element article : redArticleData ){
				//创建一个文章对象存储文章的信息
				Article articles = new Article();
				//便利获取每一个元素的组
				Elements element1 = article.select("div").get(0).getElementsByIndexEquals(0).select("div span");
				if(element1.isEmpty() || element1==null ){
					continue;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				String sort = element1.get(0).select("a").text();//attr("abs:href"); //获取分类
				Elements element = article.select("div");
				String authorTime = element.get(0).getElementsByIndexEquals(1).select("div").text();
				String author = authorTime.substring(0,authorTime.lastIndexOf("•")); //获取作者
				
				String sendTime = element.get(0).getElementsByIndexEquals(1).select("abbr").attr("title");
				
				sendTime = authorTime.substring(authorTime.lastIndexOf("•")+1).trim();//发表时间
				
				
				//获取正确格式的统计时间
				Date date = new Date();
				
				String now = sdf.format(date);
				if(sendTime.indexOf("/")>0 && sendTime.indexOf(":")>sendTime.indexOf("/") ){
					if(sendTime.startsWith("201")){
						sendTime = sendTime.replaceAll("/", "-");
					}else{
						sendTime = "2014-"+sendTime.replaceAll("/", "-");
					}
				}else{
					if(sendTime.endsWith("小时前")){
						sendTime = sendTime.substring(0, sendTime.indexOf("小"));
						long time=date.getTime()-Long.parseLong(sendTime)*60*60*1000;
						sendTime = sdf.format(new Date(time));
					}else if(sendTime.indexOf(":")<sendTime.indexOf("/")){
						sendTime = now.substring(0,now.lastIndexOf("-")+1)+sendTime.substring(sendTime.indexOf("/")+1).trim()+" "+sendTime.substring(0, sendTime.indexOf("/")).trim();
					}else{
						sendTime = null;
					}
					
				}
				if(statTime!=null && null!=sendTime && !"".equals(sendTime)){
					try {
						Date dateTime = sdf.parse(sendTime);
						if(dateTime.after(statTime)){
							break;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String link = element.select("h1").select("a").attr("href");//文章链接
				String a = link.substring(link.lastIndexOf("/")+1,link.lastIndexOf("."));
				String idIndex = ""; //文章ID
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher match=pattern.matcher(a);
				if(match.matches()){
					idIndex = a;
				}else{
					continue;
				}
				
				String imgUrl = element.get(5).select("a").select("img").attr("src");//文章图片
				String title = element.get(6).select("h1 a").text(); //文章标题
				String summary = element.get(6).select("p").text(); //文章简介
				//文章的id存储
				articles.setId(idIndex);
				//文章的名字
				articles.setTitle(title);
				articles.setSort(sort);
				articles.setAuthor(author);
				articles.setSummary(summary);
				articles.setUrl(RootUrl+link);
				articles.setSourceName("36氪");
				String articleContent = articleContent(RootUrl+link); //文章内容
				//文章的创建时间的存储
				articles.setSendTime(sendTime);
				articles.setContent(articleContent);
				articles.setImgUrl(imgUrl);
				
				//插入数据库
				
//				try {
//					dataWorker.insertArticle(articles);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				channel.putArticle(idIndex, articles);
				
				channelCopy.putArticle(idIndex, articles);
				
			}
			// 如果该页没有符合条件的数据，就直接返回不用查下一页的数据
			if(channelCopy.articleSize()==0){
				return 0;
			}
			//插入数据
			try {
				logger.error("url:"+url);
				dataWorker.insertChannelArticles(channelCopy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//判断时候还有下一页
			//获取总页数
			String nextUrl = doc.select(".next_page").select("a").attr("href");
			if(!"".equals(nextUrl) && !"#".equals(nextUrl)){
				logger.debug(RootUrl+nextUrl);
				ArticleReds(RootUrl+nextUrl);		
				System.out.println(RootUrl+nextUrl);
			}
//			else{
//				channel.setArticles(map);
//			//	System.out.println("the frist map"+channel.getArticles().size());
//			}
		}
		return 0;
	}
	
	/**
	 * 
	*<p>Title: articleDetialData</p> 
	*<p>Description:获取每篇文章的内容</p> 
	* @param @param map
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static String articleContent(String url){
		String urlConent = new SpiderHttpClient().read_html(url);
		Document doc = Jsoup.parse(urlConent);
		String articleContent = doc.select("section ").select(".article").html();
		return articleContent;
	}
}
