package xiaobai.analyze;

import java.util.List;

import xiaobai.channel.*;
import xiaobai.html.SpiderHttpClient;
import xiaobai.jdbc.DataWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
/*
 * @desc 分析头条统计数据
 */
public class ToutiaoAnalyze {
	static final Logger logger = LogManager.getLogger(ToutiaoAnalyze.class.getName());

	//渠道信息
	private static Channel channel = new Channel("toutiao");
	
	//基础地址http://mp.toutiao.com/
	private static String RootUrl = "http://mp.toutiao.com/";
	//概览地址
	private static String OverViewUrl = "http://mp.toutiao.com/profile/";
	//每日访问数据地址,目前是查2014-06-14,
	private static String DailyDataUrl = "http://mp.toutiao.com/media_stats/reading/?start_date=2014-05-01&end_date=";
	//暂时不处理每日订阅数据
	//文章访问详细数据
	private static String ArticleDataUrl = "http://mp.toutiao.com/articles/";
	//文章评论数据
	private static String ArticleCommentUrl = "http://www.toutiao.com/group/%article%/comments/?count=%comment_num%";
	//文章的详细数据接口
	public static String AllDatasUrl = "http://mp.toutiao.com/media_stats/article_list/?start_date=%start_date%&end_date=%end_date%&pagenum=%pagenum%";
	//订阅用户
	private static String SubscriptionUserUrl = "http://mp.toutiao.com/media_stats/user/?start_date=%start_date%&end_date=%end_date%";
	
	private static DataWorker dataWorker = new DataWorker();
	
	public static void main(String[] args) throws Exception{
		getArticleAllDatas();
		analyzeComment();
		//globalAnalyze();
		//logger.error(ToutiaoAnalyze.class.getName());
		//logger.debug("i'm here");
		//logger.error("i'm here");
		//这个json信息太大了,输出到console会导致eclipse死掉
		//LogManager.getLogger("File").info("fastjson:"+JSON.toJSONString(channel));
		//Channel c2 = JSON.parseObject(JSON.toJSONString(channel), Channel.class);
		//System.out.println("fastjson:"+JSON.toJSONString(c2));
	
	}	
	/**
	 * @desc 全局分析函数,分析渠道所有数据;
	 * 		 该分析抓取页面过多,耗时较长,请慎用
	 * @return
	 */
	public static int globalAnalyze()
	{	
		//获取整体的数据平均阅读量，月活跃粉丝数，累计阅读量
		//if( 0 != getOverallData() ) return 1;
		//文章分析 文章名称 发布时间 推荐量 阅读量 评论数 收藏数 转发数
		//if( 0 != getArticleAllDatas() ) return 1;
		//查询订阅用户的数据TODO[暂时不完成]
		//if( 0 != getSubscriptionAllDatas() ) return 1;
		
		
		/*if( 0 != analyzeOverView() ) return 1;
		if( 0 != analyzeDailyVisit() ) return 1;
		if( 0 != analyzeArticleData() ) return 1;
		if( 0 != analyzeComment() ) return 1;*/
		return 0;
	}
	/**
	 * @desc 定制分析函数,所有分析函数没有依赖关系,可单独抓取分析;
	 * 		 
	 * @return
	 * @throws SQLException 
	 */
	public static int customAnalyze() throws SQLException
	{
		//if( 0 != analyzeOverView() ) return 1;
		if( 0 != analyzeDailyVisit() ) return 1;
		if( 0 != analyzeArticleData() ) return 1;
		if( 0 != analyzeComment() ) return 1;
		//if( 0 != analyzeComment(3547120711L, 1000) ) return 1;
		return 0;		
	}
	
	/**
	 * 
	*<p>Title: getSubscriptionAllDatas</p> 
	*<p>Description:订阅用户的数据TODO</p> 
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	/*private static int getSubscriptionAllDatas(){
		String url = SubscriptionUserUrl.replace("%start_date%", "2014-06-26");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			   url = url.replace("%end_date%", sdf.format(new Date()));
	    String strDoc = SpiderHttpClient.simulateCookieLoginJson(url);
	    Document doc = Jsoup.parse(strDoc);
	    Elements element = doc.select("tbody tr");
	    for(Element el : element){
	    	System.out.println(el.select("td").get(0).text());
	    	System.out.println(el.select("td").get(1).text());
	    	System.out.println(el.select("td").get(2).text());
	    	System.out.println(el.select("td").get(3).text());
	    	
	    }
		return 0;
	}*/
	
	/**
	 * 
	*<p>Title: getChannel</p> 
	*<p>Description:获取getChannel</p> 
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static Channel getChannel(){
		getArticleAllData(1);
		return channel;
	}
	
	/**
	 * 
	*<p>Title: getArticleAllDatas</p> 
	*<p>Description:文章分析 文章名称 发布时间 推荐量 阅读量 评论数 收藏数 转发数</p> 
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static int getArticleAllDatas(){
		getArticleAllData(1);
		return 0;
	}
	
	private static int getArticleAllData(int pageNum){
			String url = AllDatasUrl.replace("%start_date%", "2014-07-11");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			url = url.replace("%end_date%", sdf.format(new Date()));
			url = url.replace("%pagenum%", pageNum+"");
			String str = new SpiderHttpClient().simulateCookieLoginJson(url);
			Document doc = Jsoup.parse(str);
			if("404错误页".equals(doc.select("title").text().trim())){
				System.out.println("最后一页了");
				return 3;
			}
			Elements element = doc.select(".article1_tablebody .sclearfix");
			Elements elele = doc.select(".page_number-state");
			int num = 0;
			for(Element el : element){
				Article art = new Article();
				String artUrl = el.select(".td1 a").attr("href");
				int idIndex = artUrl.indexOf("/a");
				String articleId = artUrl.substring(idIndex+2, artUrl.trim().length()-1);
				System.out.println("start**************"+num++);
				art.setTitle(el.select(".td1 a").text());
				art.setSendTime(el.select(".td2").text());
				//推荐量
					String num1 = el.select(".td3").text();
					if(num1.contains(",")){
						num1 = num1.replace(",", "");
					}
				art.setExposes(Long.parseLong(num1));
				//阅读量
					String num2 = el.select(".td4").text();
					if(num2.contains(",")){
						num2 = num2.replace(",", "");
					}
				art.setPageViews(Integer.parseInt(num2));
				//评论量
				String num3 = el.select(".td5").text();
				if(num3.contains(",")){
					num3 = num3.replace(",", "");
				}
				art.setCommentNum(Integer.parseInt(num3));
				//收藏量
				String num4 = el.select(".td6").text();
				if(num4.contains(",")){
					num4 = num4.replace(",", "");
				}
				art.setFavorites(Integer.parseInt(num4));
				//转发量
				String num5 = el.select(".td7").text();
				if(num5.contains(",")){
					num5 = num5.replace(",", "");
				}
				art.setShares(Integer.parseInt(num5));
			//	art.setUrl("http://web.toutiao.com/a"+articleId+"/");
				art.setUrl("http:"+artUrl);
				
				channel.getArticles().put(articleId, art);
				
				System.out.println("end**************");
			}
			
			getArticleAllData(pageNum+1);
		return 0;
	}
	
	
	/**
	 * 
	*<p>Title: getOverallData</p> 
	*<p>Description:获取整体</p> 
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	private static int getOverallData(){
		String urlConent = new SpiderHttpClient().read_html(OverViewUrl);
		Document doc = Jsoup.parse(urlConent);
		Elements element = doc.select(".indexsum_btns a");
		if( element.size() <= 0 )
		{
			//个数异常
			System.out.println("whole data elemnt number error, "+ element.size());
			return 2;
		}
		for(Element el : element){
			String st = el.select("b").text();
			String stName = el.select("span").text();
			if(st.contains(",")){
				st = st.replace(",", "");
			}else if (st.contains("万")) {
				st = st.replace("万", "0000");
			}
			if (stName.trim().equals("平均阅读量")) {
				channel.setMonthMeanReadNum(Integer.parseInt(st));
			}else if (stName.trim().equals("月活跃粉丝数")) {
				channel.setMonthMeanFans(Integer.parseInt(st));
			}else if (stName.trim().equals("累计阅读量")) {
				channel.setPageViews(Integer.parseInt(st));
			}
				
		}
		return 0;
	}
	
	
	//分析渠道总量数据
	@SuppressWarnings("unused")
	private static int analyzeOverView()
	{
		String urlConent = new SpiderHttpClient().read_html(OverViewUrl);
		Document doc = Jsoup.parse(urlConent);
		System.out.println(doc.title());
		Elements history = doc.select(".history");
		System.out.println(history.size());
		if( history.size() != 1 )
		{
			//个数异常
			logger.error("history elemnt number error, "+ history.size());;
			return 1;
		}
		Element ele = history.first();
		System.out.println(ele.html());
		
		Elements uls = ele.select("ul");
		System.out.println(uls.size());
		if( uls.size() != 1 )
		{
			//个数异常
			System.out.println("history ul elemnt number error, "+ uls.size());
			return 2;
		}
		Element ul = uls.first();
		System.out.println(ul.html());
		
		for(Element chl : ul.children() )
		{
			String txt = chl.select(".txt").first().text();
			String num = chl.select(".num").first().text();
			System.out.println("txt:"+txt);
			System.out.println("num:"+num);
			
			
			if( txt.equals("文章量") )
			{
				channel.setArticleSum(Integer.parseInt(num));
			}else if( txt.equals("阅读量") )
			{
				num = num.replace("万", "0000");
				channel.setPageViews(Integer.parseInt(num));
			}else if( txt.equals("收藏量") )
			{
				channel.setFavorites(Integer.parseInt(num));
			}else if( txt.equals("评论量") )
			{
				channel.setCommentNum(Integer.parseInt(num));
			}else if( txt.equals("转发量") )
			{
				channel.setShares(Integer.parseInt(num));
			}else if( txt.equals("总订阅用户") )
			{
				channel.setSubscribeNum(Integer.parseInt(num));
			}
			else{
				System.out.println("unknown mark,"+txt);
			}
	
			//System.out.println("text:"+chl.text());
		}
		return 0;
	}
	//分析渠道文章详情页数据
	//分析从5月1日到今天的所有数据
	private static int analyzeDailyVisit()
	{
		//先清空数据
		channel.clearDailyData();
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = dateFormat.format(now);
		System.out.println("today:"+today);

		String urlConent = new SpiderHttpClient().read_html(DailyDataUrl+today);
		//System.out.println(urlConent);
		//int index = urlConent.indexOf("历史数据汇总");
		Document doc = Jsoup.parse(urlConent);
		System.out.println(doc.title());
		Elements analyzeTable = doc.select(".analyse_table_zone");
		System.out.println(analyzeTable.size());
		if( analyzeTable.size() != 1 )
		{
			//个数异常
			System.out.println("analyzeTable elemnt number error, "+ analyzeTable.size());
			return 1;
		}
		Element table = analyzeTable.first();
		Elements trs = table.select("tr");
		for(Element tr : trs)
		{
			System.out.println(tr.html());

			Elements tds = tr.select("td");
			if( tds.size() != 7 )
			{
				//个数异常
				System.out.println("analyzeTable elemnt tds number error, "+ tds.size());
				continue;
			}	
			ChannelDailyData data = new ChannelDailyData();
			data.setDate(tds.get(0).text());
			data.setArticleNum(Integer.parseInt(tds.get(1).text()));
			data.setExposes(Integer.parseInt(tds.get(2).text()));
			data.setPageViews(Integer.parseInt(tds.get(3).text()));
			data.setFavorites(Integer.parseInt(tds.get(4).text()));
			data.setCommentNum(Integer.parseInt(tds.get(5).text()));
			data.setShares(Integer.parseInt(tds.get(6).text()));
				
			channel.putDailyData(data.getDate(), data);
		}
		return 0;
	}
	//分析文章详细数据
	private static int analyzeArticleData()
	{
		//首先清空数据
		channel.clearArticle();
		return analyzeArticleByPage(ArticleDataUrl);
	}
	//分析一页的文章数据,如果有下一页的链接会自动分析下一页
	private static int analyzeArticleByPage(String url)
	{
		String urlConent = new SpiderHttpClient().read_html(url);
		System.out.println("analyzeArticleByPage:"+url);
		//System.out.println(urlConent);
		//int index = urlConent.indexOf("历史数据汇总");
		Document doc = Jsoup.parse(urlConent);
		System.out.println(doc.title());
		Elements articleLists = doc.select(".article_list");
		System.out.println(articleLists.size());
		if( articleLists.size() != 1 )
		{
			//个数异常
			System.out.println("articleList elemnt number error, "+ articleLists.size());
			return 1;
		}
		Element articleList = articleLists.first();
		Elements articles = articleList.select(".article_item");
		for( Element article : articles )
		{
			Article arc = new Article();
			//String id = article.attr("article_id");
			Element eleTitle = article.select(".item_title").first();
			String title = eleTitle.text();
			String articleUrl = eleTitle.select("a").first().attr("href");
			//从url路径里截取文章ID
			Pattern p = Pattern.compile("http://www.toutiao.com/group/(.*)/");
			Matcher m = p.matcher(articleUrl);
			String id = null;
			while(m.find()){
				id = m.group(1);
				System.out.println("regex find id:"+id);
			}
			if( id == null )
			{
				System.out.println("id not found, 审核应该没有通过, url地址:"+articleUrl);				
				continue;
			}
			String sort = article.select(".item_sort").first().text();
			String sendTime = article.select(".item_time").first().text();
			String content = article.select(".item_content").first().text();

			Element stat = article.select(".item_stat").first();
			String expose = stat.select(".display").first().getElementsByTag("i").text();
			String read = stat.select(".read").first().getElementsByTag("i").text();
			String voteUp = stat.select(".vote_up").first().getElementsByTag("i").text();
			String voteDown = stat.select(".vote_down").first().getElementsByTag("i").text();
			String favorite = stat.select(".favorite").first().getElementsByTag("i").text();
			String comment = stat.select(".comment").first().getElementsByTag("i").text();
			String shares = stat.select(".repin").first().getElementsByTag("i").text();
			
			arc.setId(id);
			arc.setTitle(title);
			arc.setUrl(articleUrl);;
			arc.setSort(sort);
			arc.setSendTime(sendTime);
			arc.setExposes(Integer.parseInt(expose));
			arc.setPageViews(Integer.parseInt(read));
			arc.setFavorites(Integer.parseInt(favorite));
			arc.setCommentNum(Integer.parseInt(comment));
			arc.setShares(Integer.parseInt(shares));
			arc.setVoteDowns(Integer.parseInt(voteUp));
			arc.setVoteUps(Integer.parseInt(voteDown));
			
			channel.putArticle(id, arc);
		}
		
		//如果有下一页,继续分析
		Elements pagers = doc.select(".pager");
		if( pagers.size() == 0 )
		{
			System.out.println("没有下一页了,全部文章分析完毕.");
			return 0;
		}
		Element pager = pagers.first();
		Element next = pager.select("li").get(1);
		if( next.attr("class").equals("disabled") )
		{
			System.out.println("*没有下一页了,全部文章分析完毕.");
			return 0;
		}
		String nextPageUrl = next.select("a").first().attr("href");
		nextPageUrl = RootUrl +"articles/"+ nextPageUrl;
		System.out.println("nextPageUrl:"+nextPageUrl);

		//延时1秒
		System.out.println("analyzeArticleByPage next delay one second.");
		try {
			Thread.currentThread();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		analyzeArticleByPage(nextPageUrl);
		return 0;			
	}
	
	/**
	 * @throws SQLException 
	 * @desc 获取渠道评论
	 * @desc 
	 */
	private static int analyzeComment() throws SQLException
	{
		for(String id : channel.articlekeySet())
		{
			analyzeComment(id, channel.getArticle(id).getCommentNum());
		}
		return 0;
	}
	/**
	 * @desc 获得指定文章ID的评论,取前commentNum条
	 * 		 评论数据加载到渠道信息中的文章数据里面
	 * @param articleID 文章ID
	 * @param commentNum 评论数据
	 * @return
	 * @throws SQLException 
	 */
	private static int analyzeComment(String articleID, int commentNum) throws SQLException
	{
		//查找文章是否存在
		if( ! channel.containsArticle(articleID) )
		{
			System.out.println("渠道没有该篇文章,请确认,"+articleID);
			return 0;
		}
		Article arc = channel.getArticle(articleID);
		
		String url = ArticleCommentUrl.replace("%article%", articleID.toString());
		url = url.replace("%comment_num%", Integer.toString(commentNum));
		String urlConent = new SpiderHttpClient().read_html(url);
		System.out.println("analyzeComment:"+url);
		//System.out.println(urlConent);

		Document doc = Jsoup.parse(urlConent);
		System.out.println(doc.title());
		Elements comments = doc.select(".comment-item");
		System.out.println(comments.size());	
		for(Element comment : comments){
			Long id = Long.parseLong(comment.attr("data-comment-id"));
			Element eleAvatar = comment.select("div a").first();
			Element eleImg = eleAvatar.select("img").first();
			String headUrl = eleImg.attr("src");
			
			String sendTime = "2014-"+ comment.select("ul .time").first().text();
			Element eleContent = comment.select("ul li").first();
			String content = eleContent.select(".content").first().text();
			String userName = eleContent.select(".name a").first().text();
			String userUrl = "http://www.toutiao.com" + eleContent.select("span a").first().attr("href");
			String userID = null;
			//从地址里解析用户ID
			Pattern p = Pattern.compile("http://www.toutiao.com/user/(.*)/");
			Matcher m = p.matcher(userUrl);
			while(m.find()){
				userID = m.group(1);
				System.out.println("regex find userid:"+userID);
			}
			User user = new User("toutiao");
			user.setId(Long.parseLong(userID));
			user.setName(userName);
			user.setHeadUrl(headUrl);
			user.setHomeUrl(userUrl);
			
			user.dump();
			//如果用户不存在,插入到用户结构
			if( !channel.containsUser(user.getId()) )
			{
				channel.putUser(user.getId(), user);
			}
			Comment comm = new Comment(id, content, sendTime, user.getId(), articleID);
			
			comm.dump();
			//文章刷新评论
			arc.clearComments();
			arc.add(comm);
			Comment cm = new Comment();
			cm.setArticleID(articleID);
			cm.setContent(content);
			cm.setSendTime(sendTime);
			cm.setUserID(user.getId());
			cm.setAuthorName(user.getName());
			dataWorker.insertTouTiaoCommentData(cm);
		}
		
		return 0;
	}
}
