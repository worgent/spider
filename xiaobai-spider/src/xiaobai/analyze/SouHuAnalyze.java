package xiaobai.analyze;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xiaobai.channel.AllVideo;
import xiaobai.channel.Article;
import xiaobai.channel.ArticleDailyData;
import xiaobai.channel.Channel;
import xiaobai.channel.Comment;
import xiaobai.channel.PayVideo;
import xiaobai.channel.Video;
import xiaobai.channel.VideoDailyData;
import xiaobai.html.SpiderHttpClient;
import xiaobai.util.HttpClientUtil;

import com.alibaba.fastjson.JSON;
/*
 * @desc 分析头条统计数据
 */
public class SouHuAnalyze {
	static final Logger logger = LogManager.getLogger(SouHuAnalyze.class.getName());
	
	static SpiderHttpClient  SpiderHttp = new SpiderHttpClient();
	
	//渠道信息
	private static Channel channel = new Channel("souhu");
	//基础地址
	private static String RootUrl = "http://mp.k.sohu.com/";
	//整体访问数据
	private static String OverViewUrl = "http://mp.k.sohu.com/server/admin/opencms/stats.go?m=visit&1425007774591";
	//文章阅读数据
	private static String ArticleRedUrl = "http://mp.k.sohu.com/server/admin/opencms/stats.go?m=news&1425007905077";
	//文章评论
	private static String ArticleCommentUrl = "http://3g.k.sohu.com/api/comment/getCommentListByCursor.go?rollTYpe=1&id=%aritlceId%&busiCode=2";
	//用户订阅数据
	private static String subscriptionUrl = "http://mp.k.sohu.com/server/admin/opencms/stats.go?m=sub&1418871399906";
	
	public static String AllAnalyzeUrl = "http://mp.k.sohu.com/server/admin/opencms/stats.go?m=visitJson&1418812662734&startTime=%startTime%&endTime=%endTime%";
	//2014-06-07视频分成效果总览[付费]
	private static String statementShowUrl = "http://lm.tv.sohu.com/my/statement/show.do?p=%pageNum%&startTime=%startTime%&endTime=%endTime%&detail=";
	//视频单日数据[付费]
	private static String DayDatasUrl = "http://lm.tv.sohu.com/my/statement/day.do?p=%pageNum%&dtime=%dateTime%&type=0";
	
	//视频每日播放数据,总量[所有视频]
	private static String allVideoDataUrl = "http://my.tv.sohu.com/user/wm/udata/trends.do?startDate=%startTime%&endDate=%endTime%";
	//观看设备
	private static String allEquipmentUrl = "http://my.tv.sohu.com/user/wm/udata/source.do?startDate=%startTime%&endDate=%endTime%";
	//获取单个视频每日的播放量
	private static String AllVideoDayUrl = "http://my.tv.sohu.com/user/wm/vdata/list.do?pg=%pageNum%";
	
	public static void main(String[] args) {
		//Channel channel = SouHuAnalyze.getSouhuDatas();
		/*//获取视频效果纵览
		 getStatementShow("1","2014-12-22");
		//视频单日数据
		 getDayDatas("1");*/
		 
		/*//获取每天视频的播放量
		 getAllVideoData("2014-08-22"); 
		 getDayAllEquipment();*/
		
		/*getAllVideoDay("1");
		getEverydayVideo("2014-06-07");*/
	}
	//获取所有文章中单片文章的总数据
	public static Channel getSingleVideoTotalPlay(){
		getAllVideoDay("1");
		return channel;
	}
	
	//获取所有文章的信息及每天的访问
	public static Channel getSingleVideoDayPlay(){
		getAllVideoDay("1");
		getEverydayVideo("2014-06-07");
		return channel;
	}
	
	//获取所有视频每天的数据和在移动设备上的播放数量
	public static Channel getAllVideoData(){
		getAllVideoData("2014-06-07");
		return channel;
	}
	
	
	//视频总览和单日数据
	public static Channel getResultShow(){
		//获取视频效果纵览
		SouHuAnalyze.getStatementShow("1","2014-06-07");
		//视频单日数据
		SouHuAnalyze.getDayDatas("1");
		return channel;
	}
	
	public static Channel getSouhuDatas(){
	//public static void main(String[] args) {
		globalAnalyze();
		return channel;
	}
	
	/**
	 * @desc 全局分析函数,分析渠道所有数据;
	 * 		 该分析抓取页面过多,耗时较长,请慎用
	 * @return
	 */
	public static int globalAnalyze()
	{
		//分析渠道总量数据
		//if( 0 != getAllAnalyze("2014-06-07","2014-11-11") ) return 1;
		//订阅数的抓取
		//if( 0 != getSubscription() ) return 1;
		//获取文章的阅读数据
		if( 0 != getArticleRed() ) return 1;
		return 0;
	}
	/**
	 * @desc 定制分析函数,所有分析函数没有依赖关系,可单独抓取分析;
	 * 		 
	 * @return
	 */
	public static int customAnalyze()
	{
		if( 0 != getSubscription() ) return 1;
		/*if( 0 != analyzeDailyVisit() ) return 1;
		if( 0 != analyzeArticleData() ) return 1;
		if( 0 != analyzeComment() ) return 1;
		if( 0 != analyzeComment(3547120711L, 1000) ) return 1;*/
		return 0;		
	}
	
	//分析渠道总量数据
	@SuppressWarnings("unused")
	public static int getAllAnalyze(String startTime,String endTime){
		String url = AllAnalyzeUrl.replace("%startTime%",startTime).replace("%endTime%", endTime);
		try {
			//url="http://mp.k.sohu.com/server/admin/opencms/stats.go?m=news&1418812660125";
			String stringJson = SpiderHttp.simulateCookieLoginJson(url);
			System.out.println(stringJson);
			//获取统计数据
			List analyzeList = (List) JSON.parse(stringJson);
			int num = 0;
			for (Object analyzeAay : analyzeList) {
				Map map = (Map) JSON.parse(analyzeAay.toString());
				String sdate = (String) map.get("sdate");
				if (sdate.equals(xiaobai.util.DateUtil.getDate2String())) {
					channel.setYesterdayVisitSum(Integer.parseInt(map.get("pv")
							.toString()));
					channel.setYesterdayCommentSum(Integer.parseInt(map.get("comment")
							.toString()));
					channel.setYesterdayNewVisitSum((Integer.parseInt(map.get("uv")
							.toString())));
					channel.setArticleSum((Integer.parseInt(map.get("totalPv")
							.toString())));
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		return 1;
	}


	//订阅数的抓取
	public static int getSubscription(){
		String urlConent = new SpiderHttpClient().read_html(subscriptionUrl);
		Document doc = Jsoup.parse(urlConent);
		Elements element = doc.select("tbody tr td");
		channel.setSubscribeNum(Integer.parseInt(element.first().text()));
		return 0;
	}
	
	/**
	 * 
	*<p>Title: ArticleRed</p> 
	*<p>Description:获取文章的阅读数据</p> 
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static int getArticleRed(){
		//获取每篇文章总的数据
		Map map = new HashMap<Object, Object>();
		int status = ArticleReds("",map);
		//获取没骗文章的详细数据
		articleDetialData(map);
		//获取每篇文章的阅读数
		getAllComments(map);
		return status;
	}
	
	/**
	 * 
	*<p>Title: getAllComments</p> 
	*<p>Description:获取每篇文章的阅读数</p> 
	* @param @param map
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static int getAllComments(Map map){
 		Map map1 = channel.getArticles();
 		for(Object dataKey : map1.keySet())   {  
 			System.out.println("wang xiongl lei chuan de "+Long.parseLong(dataKey.toString()));
 			getArticleCommentData(dataKey.toString(), 0);       
 		}
		 
		 
		 return 0;
	}
	
	/**
	 * 
	*<p>Title: articleDetialData</p> 
	*<p>Description:获取每篇文章每天的详细数据</p> 
	* @param @param map
	* @param @return 设定文件 
	* @return  int 返回类型 
	* @throws
	 */
	public static int articleDetialData(Map map){
		Iterator it =  channel.getArticles().keySet().iterator();
		while (it.hasNext()) {
			List list = new ArrayList<Object>();
			Object articleId = it.next();
			Article art = channel.getArticle(articleId.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String str = SpiderHttp
					.simulateCookieLoginJson("http://mp.k.sohu.com/server/admin/opencms/stats.go?m=newsDetail&startTime="
							+ art.getSendTime()
							+ "&endTime="
							+ sdf.format(new Date())
							+ "&newsId="
							+ art.getId()
							+ "");
			if (!"{}".equals(str.trim())) {
				List li = (List) JSON.parse(str);
				for (Object object : li) {
					ArticleDailyData ard = new ArticleDailyData();
					Map map1 = (Map) object;
					ard.setDate(map1.get("stat_date").toString());
					ard.setAddRedNum(Integer
							.parseInt(map1.get("pv").toString()));
					ard.setUserViews(Integer
							.parseInt(map1.get("uv").toString()));
					ard.setCommentNum(Integer.parseInt(map1.get("cc")
							.toString()));
					list.add(ard);
				}
				art.setVisitDetails(list);
			}
		}
		return 0;
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
	public static int ArticleReds(String url,Map map){
		String urlConent = "";
		if("".equals(url)){
		   urlConent = SpiderHttp.read_html(ArticleRedUrl);
		}else{
			urlConent = SpiderHttp.read_html(url);
		}
		Document doc = Jsoup.parse(urlConent);
		Elements redArticleData = doc.select("tbody tr");
		if(redArticleData.size() <= 0){
			logger.error("redArticleData elemnt number error, "+ redArticleData.size());
			return 1;
		}else{
			for( Element article : redArticleData ){
				//创建一个文章对象存储文章的信息
				Article articles = new Article();
				//便利获取每一个元素的组
				Elements element = article.select("td");
				//获取文章的名字与id
				String aUrl = element.get(0).select("div a").attr("abs:href");
				int idIndex = aUrl.indexOf("n");
				//文章的id存储
				articles.setId(aUrl.substring(idIndex+1));
				//文章的名字
				articles.setTitle(element.get(0).select("div a").text());
				//文章的创建时间的存储
				articles.setSendTime(element.get(1).select("p").text());
				channel.putArticle(aUrl.substring(idIndex+1), articles);
			}
		//	System.out.println("the frist map"+ map.size());
			
			//判断时候还有下一页
			//获取总页数
			String totalPages = doc.select(".total").text();
			//获取总页数int 
			int totalNum = Integer.parseInt(totalPages.substring(1,totalPages.length()-1));
			//获取当前页数
			int indexNum = Integer.parseInt(doc.select(".pagination .cur").text());
			if(indexNum < totalNum){
				int index = indexNum+1;
				ArticleReds("http://mp.k.sohu.com/server/admin/opencms/stats.go?m=news&keyword=&startTime=&endTime=&pageNo="+index,map);
			}
//			else{
//				channel.setArticles(map);
//			//	System.out.println("the frist map"+channel.getArticles().size());
//			}
		}
		return 0;
	}

	
	/**
	 * 解析评论数据,暂时只支持抓取搜狐的热门评论
	* @param @param id
	* @param @param commentNum 设定文件 
	* @return  void 返回类型 
	* @throws
	 */
	public static  int getArticleCommentData(String articleId,int commentNum){
		try {
			//查找文章是否存在
			if (!channel.containsArticle(articleId)) {
				System.out.println("渠道没有该篇文章,请确认," + articleId);
				return 0;
			}
			List<Comment> commentList = new ArrayList<Comment>();
			//获取文章数据
			Article arc = channel.getArticle(articleId);
			String url = ArticleCommentUrl.replace("%aritlceId%",
					articleId.toString());
			//获取请求过来的json数据，评论的数据
			String string = HttpClientUtil.getPageSource(url);
			Map map = (Map) ((Map) JSON.parse(string)).get("response");
			List list = (List) map.get("commentList");
			for (Object comment : list) {
				Comment commentInfo = new Comment();
				Map commentMap = (Map) JSON.parse(comment.toString());
				System.out.println(commentMap.get("commentId"));
				commentInfo.setArticleID(articleId);
				commentInfo.setContent(commentMap.get("content").toString());
				commentInfo.setSendTime(commentMap.get("ctime").toString());
				commentInfo.setAuthorName(commentMap.get("author").toString());
				commentList.add(commentInfo);
				
			}
//			commentList.addAll(list);
			arc.setComments(commentList);
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//视频效果分成纵览
	public static  int getStatementShow(String pageNum,String startTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = statementShowUrl.replace("%startTime%",startTime).replace("%endTime%", sdf.format(new Date())).replace("%pageNum%", pageNum);
		//Document doc = Jsoup.parse(url);   
		String str = SpiderHttp.simulateCookieLoginJson(url);
		Document  doc = Jsoup.parse(str);
		
		//判断当前页数是否是最后一页
		String totalNumStr = doc.select(".fuPages .next").attr("href");
		System.out.println("总页数。。。。。。。"+totalNumStr);
		//获取当前页数
		String thisNumStr = doc.select(".fuPages .current").text();
		int thisNum = Integer.parseInt(thisNumStr);
		System.out.println("当前页数"+thisNumStr);
		
		//内容
		Elements element = doc.select("tbody tr");
		if(element.size() <= 0){
			logger.debug("this data is "+element.size());
		}
		for(Element els: element){
			PayVideo vi = new PayVideo();
			Elements el =els.select("td");
			if(el.size() > 0){
				//日期
				String dateTime = el.get(0).text();
				System.out.println("日期"+dateTime);
				vi.setDateTime(dateTime);
				//分成播放数
				String playNum = el.get(1).text();
				System.out.println("分成播放数"+playNum);
				vi.setPlayNum(playNum);
				//总收益
				String totalIncome = el.get(2).text();
				System.out.println("总收益"+totalIncome);
				vi.setTotalIncome(totalIncome);
				//详情地址
				//String detailsUrl = el.get(2).attr("href");
				String detailsUrls = "http://lm.tv.sohu.com/my/statement/day.do?dtime="+dateTime;
				System.out.println("详情地址"+detailsUrls);
				vi.setDetailUrl(detailsUrls);
				channel.getVideos().put(dateTime, vi);
			}
		}
		
		if(!"#".equals(totalNumStr.trim())){
			getStatementShow(String.valueOf(thisNum+1),startTime);
		}
		
		
		return 0;
		
	}
	
	//获取视频单日的数据
	public static  int getDayDatas(String pageNum){
		Map videoMap = channel.getVideos();
		for (Object videoId : videoMap.keySet()) {
			PayVideo vi = (PayVideo) videoMap.get(videoId);
			getDayDatas(pageNum,vi.getDateTime());
		}
		
		return 0;
	}
	//获取视频单日的数据回调方法
	public static  int getDayDatas(String pageNum,String dateTime){
		String url = DayDatasUrl.replace("%pageNum%", pageNum).replace("%dateTime%", dateTime);
		String str = SpiderHttp.simulateCookieLoginJson(url);
		Document  doc = Jsoup.parse(str);
		
		//获取当前页数
		String thisNumStr = doc.select(".fuPages .current").text();
		int thisNum = Integer.parseInt(thisNumStr);
		System.out.println("当前页数"+thisNum);
		
		//判断下一页有没有标识
		String totalNumStr = doc.select(".fuPages .next").attr("href");
		System.out.println("判断是否还有下一页表示"+totalNumStr);
		
		Elements element = doc.select(".fuTables tr");
		
		if(element.size() > 0){
			
		
		
		for(Element eles :element){
			VideoDailyData vid =  new VideoDailyData();
			Elements ele = eles.select("td");
				if(ele.size() > 0){
					//视频名称
					String videoName = ele.get(0).text();
					System.out.println("视频名称"+videoName);
					vid.setVideoName(videoName);
					
					//分成播放数
					String videoPlayNum = ele.get(1).text();
					System.out.println("视频分成播放数"+videoPlayNum);
					vid.setVideoPlayNum(videoPlayNum);
					
					//总收益
					String videoTotalIncome = ele.get(2).text();
					System.out.println("视频总收益"+videoTotalIncome);
					vid.setVideoTotalIncome(videoTotalIncome);
					
					//详细地址
					String videoDetailUrl = ele.get(3).select("a").attr("href");
					videoDetailUrl = "http://lm.tv.sohu.com"+videoDetailUrl;
					System.out.println("视频详细地址"+videoDetailUrl);
					vid.setVideoDetailUrl(videoDetailUrl);
					
					//视频id
					int idStart = videoDetailUrl.indexOf("vid=");
					int idEnd = videoDetailUrl.indexOf("&startTime");
					String videoId = videoDetailUrl.substring(idStart+4,idEnd);
					System.out.println("视频id"+videoId);
					vid.setVideoId(videoId);
					//时间
					vid.setDateTimes(dateTime);
					channel.getVideos(dateTime).putVideos(videoId, vid);
					System.out.println(channel.getVideos(dateTime).getVideoDailyData(videoId).getVideoDetailUrl());
					
					
				}
			}
		}
		
		if(!"#".equals(totalNumStr.trim())){
			getDayDatas(String.valueOf(++thisNum),dateTime);
		}
		
		return 0;
	}

	//获取每日的播放量
	public static int getAllVideoData(String startTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = allVideoDataUrl.replace("%startTime%", startTime).replace("%endTime%", sdf.format(new Date()));
		String str = SpiderHttp.read_html(url);
		Document doc = Jsoup.parse(str);
		 
		Element eleTotal = doc.select(".mb45 li").last();
		AllVideo allVideo = new AllVideo();
		//总的播放量
		String totalStr = eleTotal.select(".lc2").text().replace(",", "");
		System.out.println("总的播放量"+totalStr);
		allVideo.setTotalPlayNum(totalStr);
		//总的播放时长
		String totalVideoTimeStr = eleTotal.select(".lc3").text().replace(",", "");
		System.out.println("总的播放时长"+totalVideoTimeStr);
		allVideo.setTotalPlayTime(totalVideoTimeStr);
		
		
		//每日播放量
		Elements  element = doc.select(".trendList dl");
		for(Element eles: element){
			VideoDailyData allVideoDailyData = new VideoDailyData();
			//日期
			String dateTimes = eles.select(".ff-m").text().replace(",", "");
			System.out.println("日期"+dateTimes);
			allVideoDailyData.setDateTimes(dateTimes);
			//播放量
			String palyNum = eles.select("dd").text().replace(",", "");
				   palyNum = palyNum.substring(7, palyNum.length());
			System.out.println("播放量"+palyNum);
			allVideoDailyData.setPalyNum(palyNum);
			
			//获取设备上的数据
			String url1 = allEquipmentUrl.replace("%startTime%", dateTimes).replace("%endTime%",dateTimes);
			String str1 = SpiderHttp.read_html(url1);
			Document doc1 = Jsoup.parse(str1);
			Elements element1 = doc1.select(".sourceTable .ff-a");
			String pcData = element1.first().text().trim().replace(",", "");
			System.out.println(pcData);
			allVideoDailyData.setPcPlayNum(pcData);
			
			String phoneData = element1.last().text().trim().replace(",", "");
			System.out.println(phoneData);
			allVideoDailyData.setPhonePlayNum(phoneData);
			
			
			allVideo.putAllVideoDailys(dateTimes, allVideoDailyData);
			
		}
		channel.setAllVideo(allVideo);
		
		
		return 0;
	}
	

	
	//单视频数据,每日播放量
	public static int getAllVideoDay(String pageNum){
		String str = SpiderHttp.read_html(AllVideoDayUrl.replace("%pageNum%", pageNum));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Document doc = Jsoup.parse(str);
		Elements element = doc.select(".vlist_con li");
		
		//获取当前页数
		Elements eleme = doc.select(".Z_page");
		int thisNum = Integer.parseInt(eleme.select(".on").text());
		int sizeNum = element.size();
		
		for(Element eles : element){
			Video vi = new Video();
			//视频的播放路径
			String videoUrl = eles.select(".lc0a a").attr("abs:href");
			vi.setVideoUrl(videoUrl);
			//视频的图片路径
			String videoImageURL = eles.select(".lc0a img").attr("src");
			vi.setVideoImageURL(videoImageURL);
			//视频的时长
			String videoTime = eles.select(".lc0a .Z_maskTx").text();
			vi.setVideoTime(videoTime);
			//视频的名称
			String videoName = eles.select(".lc1a a").text();
			vi.setVideoName(videoName);
			//视频上传的时间
			String videoSendTime = eles.select(".lc1a p").text();
			videoSendTime = videoSendTime.substring(0,videoSendTime.length()-2);
			vi.setVideoSendTime(videoSendTime);
			//播放完成度
			String videoPlayFinish = eles.select(".lc2a").text();
			vi.setVideoPlayFinish(videoPlayFinish);
			//总播放量
			String videoTotalPlay = eles.select(".lc3a").text();
			vi.setVideoTotalPlay(videoTotalPlay);
			//昨日播放量
			String videoYesPlay = eles.select(".lc4a").text();
			vi.setVideoYesPlay(videoYesPlay);
			//详情数据路径
			String detailsUrl = eles.select(".lc5a a").attr("href");
			detailsUrl = "http://my.tv.sohu.com/user/wm/vdata/"+detailsUrl;
			vi.setDetailsUrl(detailsUrl);
			//视频的id
			String videoId = detailsUrl.substring(detailsUrl.indexOf("videoId=")+8, detailsUrl.length());
			vi.setVideoId(videoId);
			channel.putVideo(videoId, vi);
		}
		if(sizeNum == 10 ){
			getAllVideoDay(String.valueOf(++thisNum));
		}
		return 0;
	}
	
	//单个视频每天详细数据
	public static int getEverydayVideo(String startDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map videoMap = channel.getVideo();
		for (Object videoId : videoMap.keySet()) {
			Video vi = (Video) videoMap.get(videoId);
			String videoName = vi.getVideoName();
			String str1 = SpiderHttp.read_html(vi.getDetailsUrl()+"&startDate="+startDate+"&endDate="+sdf.format(new Date()));
			Document doc1 = Jsoup.parse(str1);
			Elements eles1 = doc1.select(".trendList dl");
			for(Element el: eles1){
				VideoDailyData vid = new VideoDailyData();
				String dateTimes = el.select(".ff-m").text();
				//时间
				vid.setDateTimes(dateTimes);
				//播放次数
				String playNum = el.select("dd").text();
				playNum = playNum.substring(7, playNum.length()).replace(",", "").trim();
				System.out.println(playNum);
				vid.setPalyNum(playNum);
				//视频的id
				vid.setVideoId(videoId.toString());
				//视频的名称
				vid.setVideoName(videoName);
				
				vi.putAllVideoDailys(dateTimes, vid);
			}
			
			
		}
		return 0;
	}
	
	
}
