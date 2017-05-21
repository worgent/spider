package xiaobai.analyze;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

import xiaobai.channel.Article;
import xiaobai.channel.Channel;
import xiaobai.html.SpiderHttpClient;
import xiaobai.jdbc.DataWorker;
import xiaobai.util.SerializableUtil;

/**
 * 
 * <p>
 * description: 《财经》_社评
 * </p>
 * <p>
 * Date: 2014年12月24日 上午10:20:00
 * </p>
 * <p>
 * modify：
 * </p>
 * 
 * @author: WXL </p>Company: jangt.com</p>
 */
public class FinanceEconomics {

	static final Logger logger = LogManager.getLogger(SouHuAnalyze.class
			.getName());
	// 渠道信息
	private static Channel channel = new Channel("社评");
	//数据户操作类
	private static DataWorker dataWorker  = new DataWorker();
	// 首页的地址
	private static String IndexUrl = "http://magazine.caijing.com.cn/financial_observer/";
	// 分页的地址
	private static String pagingUrl = "http://magazine.caijing.com.cn/financial_observer/index-%pageNum%.html";
	// 经济学家
	private static String EconomyExpertUrl = "http://magazine.caijing.com.cn/economists/";
	// 经济学家分页
	private static String EconomyExpertPagingUrl = "http://magazine.caijing.com.cn/economists/index-%pageNum%.html";
	// 观点评论
	private static String ViewCommentUrl = "http://magazine.caijing.com.cn/view_comment/";
	// 观点评论分页
	private static String ViewCommentPagingUrl = "http://magazine.caijing.com.cn/view_comment/%pageNum%.shtml";
	// 文章具体内容
	private static String ViewCommentPagingDetails = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419400839352&contentid=%articleId%&_=1419400851811";
	// 时事报道
	private static String CoverageUrl = "http://magazine.caijing.com.cn/coverage/";
	// 时事报道分页
	private static String CoveragePagingDetails = "http://magazine.caijing.com.cn/coverage/%pageNum%.shtml";
	// 时事报道详细数据
	private static String CoverageDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419406074335&contentid=%articleId%&_=1419406081835";
	// 封面新闻首页的地址
	private static String CoverNewsUrl = "http://magazine.caijing.com.cn/coverstory/index.html";
	// 封面新闻分页的地址
	private static String CoverNewspageNumUrl = "http://magazine.caijing.com.cn/coverstory/index-%pageNum%.html";
	// 资本与金融根路劲
	private static String financialRootUrl = "http://magazine.caijing.com.cn/financial/";
	// 资本与金融分页路径
	private static String financialPagingDetails = "http://magazine.caijing.com.cn/financial/%pageNum%.shtml";
	// 资本与金融详细数据
	private static String financialDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419433736035&contentid=%articleId%&_=1419433738270";
	// 经济全局跟路径
	private static String EconomyRootUrl = "http://magazine.caijing.com.cn/economy/";
	// 分页路径
	private static String EconomyPagingDetails = "http://magazine.caijing.com.cn/economy/%pageNum%.shtml";
	// 具体数据路劲
	private static String EconomyDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419434877520&contentid=%articleId%&_=1419434879416";
	// 公共政策个路径
	private static String SpecialRootUrl = "http://magazine.caijing.com.cn/special/";
	// 公共政策分页路径
	private static String SpecialPagingDetails = "http://magazine.caijing.com.cn/special/%pageNum%.shtml";
	// 公共政策具体数据路劲
	private static String SpecialDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419435798289&contentid=%articleId%&_=1419435800020";
	// 环境与科技根
	private static String EnvironTechRootUrl = "http://magazine.caijing.com.cn/environ_tech/";
	// 环境与科技分页
	private static String EnvironTechPageUrl = "http://magazine.caijing.com.cn/environ_tech/%pageNum%.shtml";
	// 环境与科技详细
	private static String EnvironTechDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419436453507&contentid=%articleId%&_=1419436455560";
	// 公司与产业根
	private static String IndustryRootUrl = "http://magazine.caijing.com.cn/industry/";
	// 公司与产业分页
	private static String IndustryPageUrl = "http://magazine.caijing.com.cn/industry/%pageNum%.shtml";
	// 公司与产业详细
	private static String IndustryDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419474629381&contentid=%articleId%&_=1419474630455";
	// 市场与法治根
	private static String RuleRootUrl = "http://magazine.caijing.com.cn/rule/";
	// 市场与法治分页
	private static String RulePageUrl = "http://magazine.caijing.com.cn/rule/%pageNum%.shtml";
	// 市场与法治详细
	private static String RuleDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419475280378&contentid=%articleId%&_=1419475282840";
	// 《财经》_投行视线根
	private static String RationalInvestmentRootUrl = "http://magazine.caijing.com.cn/rational_investment/";
	// 《财经》_投行视线分页
	private static String RationalInvestmentPageUrl = "http://magazine.caijing.com.cn/rational_investment/%pageNum%.shtml";
	// 《财经》_投行视线详细
	private static String RationalInvestmentDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419477115688&contentid=%articleId%&_=1419477116943";
	// 《财经》_法眼
	private static String HogenRootUrl = "http://magazine.caijing.com.cn/hogen/";
	// 《财经》_法眼
	private static String HogenPageUrl = "http://magazine.caijing.com.cn/hogen/%pageNum%.shtml";
	// 《财经》_法眼
	private static String HogenDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419477901438&contentid=%articleId%&_=1419477903382";
	// 《财经》_书评
	private static String ReadingRootUrl = "http://magazine.caijing.com.cn/reading/";
	// 《财经》_书评详细
	private static String ReadingDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419478687808&contentid=%articleId%&_=1419478689088";
	// 《财经》_书摘
	private static String BookSummaryRootUrl = "http://magazine.caijing.com.cn/book_summary/";
	// 《财经》_书摘
	private static String BookSummaryDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419479231687&contentid=%articleId%&_=1419479238309";
	// 《财经》_随笔根
	private static String JottingsRootUrl = "http://magazine.caijing.com.cn/jottings/";
	// 《财经》_随笔详细
	private static String JottingsDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419479858500&contentid=%articleId%&_=1419479859760";
	// 《财经》_逝者根
	private static String DeadRootUrl = "http://magazine.caijing.com.cn/dead/";
	// 《财经》_逝者详细
	private static String DeadDetailsUrl = "http://app.caijing.com.cn/?app=article&controller=article&action=fulltext&jsoncallback=jsonp1419480310659&contentid=%articleId%&_=1419480311902";

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			getAllData(sdf.parse("2014-8-22"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static Channel getArticleDatas() {
		// 封面新闻17
		//getArticleData(0, null);
		// 社评16
		//getSocietyComment(0,null);
		// 经济学家15
		// getEconomyExpert(0,EconomyExpertUrl,EconomyExpertPagingUrl);
		// 观点评述14
		// getViewComment(1,ViewCommentUrl,ViewCommentPagingUrl);
		// 时事报道13
		// getCoverageDetails(1,CoverageUrl,CoveragePagingDetails);
		// 资本与金融12
		// getDatas(1,financialRootUrl,financialPagingDetails,financialDetailsUrl);
		// 经济全局 11
		// getDatas(1,EconomyRootUrl,EconomyPagingDetails,EconomyDetailsUrl);
		// 公共政策10
		// getDatas(1,SpecialRootUrl,SpecialPagingDetails,SpecialDetailsUrl);
		// 环境与科技根9
		// getDatas(1,EnvironTechRootUrl,EnvironTechPageUrl,EnvironTechDetailsUrl);
		// 公司与产业8
		// getDatas(1,IndustryRootUrl,IndustryPageUrl,IndustryDetailsUrl);
		// 市场与法7
		// getRules(1,RuleRootUrl,RulePageUrl,RuleDetailsUrl);
		// 投行视线6
		// getDatas(1,RationalInvestmentRootUrl,RationalInvestmentPageUrl,RationalInvestmentDetailsUrl);
		// 法眼5
		// getDatas(1,HogenRootUrl,HogenPageUrl,HogenDetailsUrl);
		// 书评4
		// getReadDatas(1,ReadingRootUrl,"",ReadingDetailsUrl);
		// 书摘3
		// getReadDatas(1,BookSummaryRootUrl,"",BookSummaryDetailsUrl);
		// 随笔2
		// getReadDatas(1,JottingsRootUrl,"",JottingsDetailsUrl);
		// 逝者1
		// getReadDatas(1,DeadRootUrl,"",DeadDetailsUrl);

		/*
		 * String stringJson = JSON.toJSONString(channel);
		 * SerializableUtil.write(stringJson, "");
		 */

		/*
		 * Object a = SerializableUtil.read("G:/pojo/2014-12-24.pojo"); String
		 * stringJsons = (String)
		 * SerializableUtil.read("G:/pojo/2014-12-24.pojo");
		 * System.out.println(stringJsons.substring(3000)); Channel channel =
		 * JSON.parseObject(stringJsons,Channel.class); Map map =
		 * channel.getArticles(); for (Object string : map.keySet()) {
		 * System.out.println(string); }
		 */
		return channel;
	}

	
	public static void getAllData(Date time){
		// 封面新闻17
		getArticleData(0, time);
		// 社评16
		getSocietyComment(0,time);
		// 经济学家15
		getEconomyExpert(0,EconomyExpertUrl,EconomyExpertPagingUrl,time);
		// 观点评述14
		getViewComment(1,ViewCommentUrl,ViewCommentPagingUrl,time);
		// 时事报道13
	    getCoverageDetails(1,CoverageUrl,CoveragePagingDetails,time);
	    // 资本与金融12
	    getDatas(1,financialRootUrl,financialPagingDetails,financialDetailsUrl,time);
	    // 经济全局 11
	 	getDatas(1,EconomyRootUrl,EconomyPagingDetails,EconomyDetailsUrl,time);
	 	// 公共政策10
	 	getDatas(1,SpecialRootUrl,SpecialPagingDetails,SpecialDetailsUrl,time);
	 	// 环境与科技根9
	 	getDatas(1,EnvironTechRootUrl,EnvironTechPageUrl,EnvironTechDetailsUrl,time);
	 	// 公司与产业8
	    getDatas(1,IndustryRootUrl,IndustryPageUrl,IndustryDetailsUrl,time);
	    // 市场与法7
	 	getRules(1,RuleRootUrl,RulePageUrl,RuleDetailsUrl,time);
	    // 投行视线6
	    getDatas(1,RationalInvestmentRootUrl,RationalInvestmentPageUrl,RationalInvestmentDetailsUrl,time);
	 	// 法眼5
	 	getDatas(1,HogenRootUrl,HogenPageUrl,HogenDetailsUrl,time);
	    // 书评4
 		getReadDatas(1,ReadingRootUrl,"",ReadingDetailsUrl,time);
 		// 书摘3
 		getReadDatas(1,BookSummaryRootUrl,"",BookSummaryDetailsUrl,time);
 		// 随笔2
 		getReadDatas(1,JottingsRootUrl,"",JottingsDetailsUrl,time);
 		// 逝者1
 		getReadDatas(1,DeadRootUrl,"",DeadDetailsUrl,time);
 		
		//插入数据
		try {
			logger.error("url:"+1122);
			dataWorker.insertChannelArticles(channel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>
	 * Title: getReadDatas
	 * </p>
	 * <p>
	 * Description:无分页的接口
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @param IndexUrl
	 * @param @param pageUrl
	 * @param @param details
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getReadDatas(int pageNum, String IndexUrl,
			String pageUrl, String details,Date time) {
		Document doc;
		String url;
		url = IndexUrl;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {

			doc = Jsoup.connect(url).get();
			Elements element = doc.select(".list li");
			for (Element el : element) {
				Article art = new Article();
				
				String sendTime = el.select(".time").text();
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				
				if(time != null){
						try {
							if (sdf.parse(sendTime).after(time)) {
								art.setSourceName("财经");
								// 网址
								String articleUrl = el.select(".wzbt a").attr("abs:href");
								System.out.println(articleUrl);
								art.setUrl(articleUrl);
								// id
								int startIndex = articleUrl.lastIndexOf("/");
								int endIndex = articleUrl.lastIndexOf(".shtml");
								System.out.println(startIndex);
								System.out.println(endIndex);
								String articleId = articleUrl.substring(startIndex + 1,
										endIndex);
								System.out.println("id++++++++++" + articleId);
								art.setId(articleId);
								// 标题
								String articleTitle = el.select(".wzbt").text();
								System.out.println("标题" + articleTitle);
								art.setTitle(articleTitle);
								// 简介
								String articleAbstruct = el.select(".subtitle").text();
								System.out.println("简介" + articleAbstruct);
								art.setSummary(articleAbstruct);
								// 作者
								String artcleAuthor = el.select(".author").text();
								System.out.println("作者" + artcleAuthor);
								art.setAuthor(artcleAuthor);
								// 时间
								System.out.println("时间" + sendTime);
								art.setSendTime(sendTime);
								channel.putArticle(articleId.toString(), art);
								String articleStr = new SpiderHttpClient()
										.simulateCookieLoginJson(CoverageDetailsUrl.replace(
												"%articleId%", articleId));

								if (!"{}".equals(articleStr.trim())) {
									int jsonIndexStart = articleStr.indexOf("\"content\"");
									int jsonIndexEnd = articleStr.lastIndexOf(")");
									String articleDetials = articleStr.substring(
											jsonIndexStart + 11, jsonIndexEnd - 4);

									StringBuilder sb = new StringBuilder();
									int i = -1;
									int pos = 0;

									while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
										sb.append(articleDetials.substring(pos, i));
										if (i + 5 < articleDetials.length()) {
											pos = i + 6;
											sb.append((char) Integer.parseInt(
													articleDetials.substring(i + 2, i + 6), 16));
										}

									}
									String content = sb.toString().replace("\\", "");
									content = content.replace("\r\n", "");
									content = content.replace("rn", "");
									content = content.replace("\"n\"", "");
									
									art.setContent(content.trim());
									System.out.println("内容" + sb.toString());
								 }
								
							}
						} catch (ParseException e) {
							e.printStackTrace();
							continue;
						}
				}else{
					art.setSourceName("财经");
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".shtml");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);
				String articleStr = new SpiderHttpClient()
						.simulateCookieLoginJson(CoverageDetailsUrl.replace(
								"%articleId%", articleId));

				if (!"{}".equals(articleStr.trim())) {
					int jsonIndexStart = articleStr.indexOf("\"content\"");
					int jsonIndexEnd = articleStr.lastIndexOf(")");
					String articleDetials = articleStr.substring(
							jsonIndexStart + 11, jsonIndexEnd - 4);

					StringBuilder sb = new StringBuilder();
					int i = -1;
					int pos = 0;

					while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
						sb.append(articleDetials.substring(pos, i));
						if (i + 5 < articleDetials.length()) {
							pos = i + 6;
							sb.append((char) Integer.parseInt(
									articleDetials.substring(i + 2, i + 6), 16));
						}

					}
					String content = sb.toString().replace("\\", "");
					content = content.replace("\r\n", "");
					content = content.replace("rn", "");
					
					art.setContent(content.trim());
					System.out.println("内容" + sb.toString());
				 }
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * if(pageNum != pageSize){
		 * getCoverageDetails(++pageNum,IndexUrl,pageUrl); }else{
		 * System.out.println("打印完毕"); return 0; } if(!"".equals(ele)){
		 * getDatas(++pageNum,IndexUrl,pageUrl,details); }
		 */
		System.out.println("打印完毕");
		return 0;
	}

	/**
	 * 
	 * <p>
	 * Title: getRules
	 * </p>
	 * <p>
	 * Description:市场与法
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @param IndexUrl
	 * @param @param pageUrl
	 * @param @param details
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getRules(int pageNum, String IndexUrl, String pageUrl,
			String details,Date time) {
		System.out.println("***************start" + pageNum
				+ "页***********************");
		Document doc;
		String url;
		String ele = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (pageNum == 1) {
			url = IndexUrl;
		} else {
			url = pageUrl.replace("%pageNum%", pageNum + "");
		}
		try {

			// doc = Jsoup.connect(url).get();
			doc = Jsoup.connect(url).get();
			ele = doc.select(".next").attr("abs:href");
			// String elementd = doc.select("script").attr("maxPage");
			Elements element = doc.select(".list li");
			for (Element el : element) {
				Article art = new Article();
				
				String sendTime = el.select(".time").text();
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				
				if(time != null){
						try {
							if (sdf.parse(sendTime).after(time)) {

								// 网址
								String articleUrl = el.select(".wzbt a").attr("abs:href");
								System.out.println(articleUrl);
								art.setUrl(articleUrl);
								// id
								int startIndex = articleUrl.lastIndexOf("/");
								int endIndex = articleUrl.lastIndexOf(".shtml");
								System.out.println(startIndex);
								System.out.println(endIndex);
								String articleId = articleUrl.substring(startIndex + 1,
										endIndex);
								System.out.println("id++++++++++" + articleId);
								art.setId(articleId);
								// 标题
								String articleTitle = el.select(".wzbt").text();
								System.out.println("标题" + articleTitle);
								art.setTitle(articleTitle);
								// 简介
								String articleAbstruct = el.select(".subtitle").text();
								System.out.println("简介" + articleAbstruct);
								art.setSummary(articleAbstruct);
								// 作者
								String artcleAuthor = el.select(".author").text();
								System.out.println("作者" + artcleAuthor);
								art.setAuthor(artcleAuthor);
								// 时间
								System.out.println("时间" + sendTime);
								art.setSendTime(sendTime);
								channel.putArticle(articleId.toString(), art);
								String articleStr = new SpiderHttpClient()
										.simulateCookieLoginJson(CoverageDetailsUrl.replace(
												"%articleId%", articleId));
								if (articleStr.length() > 120) {
									if (!"{}".equals(articleStr.trim())) {
										int jsonIndexStart = articleStr.indexOf("\"content\"");
										int jsonIndexEnd = articleStr.lastIndexOf(")");
										String articleDetials = articleStr.substring(
												jsonIndexStart + 11, jsonIndexEnd - 4);

										StringBuilder sb = new StringBuilder();
										int i = -1;
										int pos = 0;
										while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
											sb.append(articleDetials.substring(pos, i));
											if (i + 5 < articleDetials.length()) {
												pos = i + 6;
												sb.append((char) Integer.parseInt(
														articleDetials.substring(i + 2, i + 6),
														16));
											}
										}
										art.setContent(sb.toString().trim());
										System.out.println("内容" + sb.toString());
									}
								}
							  
							}
						} catch (ParseException e) {
							e.printStackTrace();
							continue;
						}
					}else{
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".shtml");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);
				String articleStr = new SpiderHttpClient()
						.simulateCookieLoginJson(CoverageDetailsUrl.replace(
								"%articleId%", articleId));
				if (articleStr.length() > 120) {
					if (!"{}".equals(articleStr.trim())) {
						int jsonIndexStart = articleStr.indexOf("\"content\"");
						int jsonIndexEnd = articleStr.lastIndexOf(")");
						String articleDetials = articleStr.substring(
								jsonIndexStart + 11, jsonIndexEnd - 4);

						StringBuilder sb = new StringBuilder();
						int i = -1;
						int pos = 0;
						while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
							sb.append(articleDetials.substring(pos, i));
							if (i + 5 < articleDetials.length()) {
								pos = i + 6;
								sb.append((char) Integer.parseInt(
										articleDetials.substring(i + 2, i + 6),
										16));
							}
						}
						art.setContent(sb.toString().trim());
						System.out.println("内容" + sb.toString());
					}
				}
			  }
			}
			System.out.println("***************end" + pageNum
					+ "页***********************");

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * if(pageNum != pageSize){
		 * getCoverageDetails(++pageNum,IndexUrl,pageUrl); }else{
		 * System.out.println("打印完毕"); return 0; }
		 */
		if(time == null){
			if (!"".equals(ele)) {
				getRules(++pageNum, IndexUrl, pageUrl, details,null);
			}
		}
		System.out.println("打印完毕");
		return 0;
	}

	/**
	 * 
	 * <p>
	 * Title: getDatas
	 * </p>
	 * <p>
	 * Description:封装公共接口
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @param IndexUrl
	 * @param @param pageUrl
	 * @param @param details
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getDatas(int pageNum, String IndexUrl, String pageUrl,
			String details,Date time) {
		System.out.println("***************start" + pageNum
				+ "页***********************");
		Document doc;
		String url;
		String ele = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		url = pageUrl.replace("%pageNum%", pageNum + "");
		try {

			// doc = Jsoup.connect(url).get();
			doc = Jsoup.connect(url).get();
			ele = doc.select(".next").attr("abs:href");
			// String elementd = doc.select("script").attr("maxPage");
			Elements element = doc.select(".list li");
			for (Element el : element) {
				Article art = new Article();
				
				String sendTime = el.select(".time").text();
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				
				if(time != null){
						try {
							if (sdf.parse(sendTime).after(time)) {
								art.setSourceName("财经");


								// 网址
								String articleUrl = el.select(".wzbt a").attr("abs:href");
								System.out.println(articleUrl);
								art.setUrl(articleUrl);
								// id
								int startIndex = articleUrl.lastIndexOf("/");
								int endIndex = articleUrl.lastIndexOf(".shtml");
								System.out.println(startIndex);
								System.out.println(endIndex);
								String articleId = articleUrl.substring(startIndex + 1,
										endIndex);
								System.out.println("id++++++++++" + articleId);
								art.setId(articleId);
								// 标题
								String articleTitle = el.select(".wzbt").text();
								System.out.println("标题" + articleTitle);
								art.setTitle(articleTitle);
								// 简介
								String articleAbstruct = el.select(".subtitle").text();
								System.out.println("简介" + articleAbstruct);
								art.setSummary(articleAbstruct);
								// 作者
								String artcleAuthor = el.select(".author").text();
								System.out.println("作者" + artcleAuthor);
								art.setAuthor(artcleAuthor);
								// 时间
								System.out.println("时间" + sendTime);
								art.setSendTime(sendTime);
								channel.putArticle(articleId.toString(), art);
								String articleStr = new SpiderHttpClient()
										.simulateCookieLoginJson(CoverageDetailsUrl.replace(
												"%articleId%", articleId));

								if (!"{}".equals(articleStr.trim())) {
									int jsonIndexStart = articleStr.indexOf("\"content\"");
									int jsonIndexEnd = articleStr.lastIndexOf(")");
									String articleDetials = articleStr.substring(
											jsonIndexStart + 11, jsonIndexEnd - 4);

									StringBuilder sb = new StringBuilder();
									int i = -1;
									int pos = 0;

									while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
										sb.append(articleDetials.substring(pos, i));
										if (i + 5 < articleDetials.length()) {
											pos = i + 6;
											sb.append((char) Integer.parseInt(
													articleDetials.substring(i + 2, i + 6), 16));
										}

									}
									String content = sb.toString().replace("\\", "");
									content = content.replace("\r\n", "");
									content = content.replace("rn", "");
									
									art.setContent(content.trim());
									System.out.println("内容" + sb.toString());
								 }
								
							
							}
						} catch (ParseException e) {
							e.printStackTrace();
							continue;
						}
				}else{
				art.setSourceName("财经");
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".shtml");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);
				String articleStr = new SpiderHttpClient()
						.simulateCookieLoginJson(CoverageDetailsUrl.replace(
								"%articleId%", articleId));

				if (!"{}".equals(articleStr.trim())) {
					int jsonIndexStart = articleStr.indexOf("\"content\"");
					int jsonIndexEnd = articleStr.lastIndexOf(")");
					String articleDetials = articleStr.substring(
							jsonIndexStart + 11, jsonIndexEnd - 4);

					StringBuilder sb = new StringBuilder();
					int i = -1;
					int pos = 0;

					while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
						sb.append(articleDetials.substring(pos, i));
						if (i + 5 < articleDetials.length()) {
							pos = i + 6;
							sb.append((char) Integer.parseInt(
									articleDetials.substring(i + 2, i + 6), 16));
						}

					}
					String content = sb.toString().replace("\\", "");
					content = content.replace("\r\n", "");
					content = content.replace("rn", "");
					
					art.setContent(content.trim());
					System.out.println("内容" + sb.toString());
					
					
				 }
				
				}
			}
			System.out.println("***************end" + pageNum
					+ "页***********************");

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * if(pageNum != pageSize){
		 * getCoverageDetails(++pageNum,IndexUrl,pageUrl); }else{
		 * System.out.println("打印完毕"); return 0; }
		 */
		if(time == null){
			if (!"".equals(ele)) {
				getDatas(++pageNum, IndexUrl, pageUrl, details,null);
			}
		}
		
		System.out.println("打印完毕");
		return 0;
	}

	/**
	 * 
	 * <p>
	 * Title: getCoverageDetails
	 * </p>
	 * <p>
	 * Description:时事报道
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @param IndexUrl
	 * @param @param pageUrl
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getCoverageDetails(int pageNum, String IndexUrl,
			String pageUrl,Date time) {

		System.out.println("***************start" + pageNum
				+ "页***********************");
		Document doc;
		String url;
		String ele = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		url = pageUrl.replace("%pageNum%", pageNum + "");
		try {

			doc = Jsoup.connect(url).get();
			doc = Jsoup.connect(url).get();
			ele = doc.select(".next").attr("abs:href");
			// String elementd = doc.select("script").attr("maxPage");
			Elements element = doc.select(".list li");
			for (Element el : element) {
				Article art = new Article();
				
				String sendTime = el.select(".time").text();
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				if(time != null){
					try {
						if (sdf.parse(sendTime).after(time)) {

							art.setSourceName("财经");
							// 网址
							String articleUrl = el.select(".wzbt a").attr("abs:href");
							System.out.println(articleUrl);
							art.setUrl(articleUrl);
							// id
							int startIndex = articleUrl.lastIndexOf("/");
							int endIndex = articleUrl.lastIndexOf(".shtml");
							System.out.println(startIndex);
							System.out.println(endIndex);
							String articleId = articleUrl.substring(startIndex + 1,
									endIndex);
							System.out.println("id++++++++++" + articleId);
							art.setId(articleId);
							// 标题
							String articleTitle = el.select(".wzbt").text();
							System.out.println("标题" + articleTitle);
							art.setTitle(articleTitle);
							// 简介
							String articleAbstruct = el.select(".subtitle").text();
							System.out.println("简介" + articleAbstruct);
							art.setSummary(articleAbstruct);
							// 作者
							String artcleAuthor = el.select(".author").text();
							System.out.println("作者" + artcleAuthor);
							art.setAuthor(artcleAuthor);
							// 时间
							System.out.println("时间" + sendTime);
							art.setSendTime(sendTime);
							channel.putArticle(articleId.toString(), art);
							String articleStr = new SpiderHttpClient()
									.simulateCookieLoginJson(CoverageDetailsUrl.replace(
											"%articleId%", articleId));
							if (!"{}".equals(articleStr.trim())) {
								int jsonIndexStart = articleStr.indexOf("\"content\"");
								int jsonIndexEnd = articleStr.lastIndexOf(")");
								String articleDetials = articleStr.substring(
										jsonIndexStart + 11, jsonIndexEnd - 4);

								StringBuilder sb = new StringBuilder();
								int i = -1;
								int pos = 0;
								while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
									sb.append(articleDetials.substring(pos, i));
									if (i + 5 < articleDetials.length()) {
										pos = i + 6;
										sb.append((char) Integer.parseInt(
												articleDetials.substring(i + 2, i + 6), 16));
									}
								}
								String content = sb.toString().replace("\\", "");
								content = content.replace("\r\n", "");
								content = content.replace("rn", "");
								
								art.setContent(content.trim());
								System.out.println("内容" + sb.toString());
							}
							
						
						}
					} catch (ParseException e) {
						e.printStackTrace();
						continue;
					}
				}else{
				art.setSourceName("财经");
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".shtml");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);
				String articleStr = new SpiderHttpClient()
						.simulateCookieLoginJson(CoverageDetailsUrl.replace(
								"%articleId%", articleId));
				if (!"{}".equals(articleStr.trim())) {
					int jsonIndexStart = articleStr.indexOf("\"content\"");
					int jsonIndexEnd = articleStr.lastIndexOf(")");
					String articleDetials = articleStr.substring(
							jsonIndexStart + 11, jsonIndexEnd - 4);

					StringBuilder sb = new StringBuilder();
					int i = -1;
					int pos = 0;
					while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
						sb.append(articleDetials.substring(pos, i));
						if (i + 5 < articleDetials.length()) {
							pos = i + 6;
							sb.append((char) Integer.parseInt(
									articleDetials.substring(i + 2, i + 6), 16));
						}
					}
					String content = sb.toString().replace("\\", "");
					content = content.replace("\r\n", "");
					content = content.replace("rn", "");
					
					art.setContent(content.trim());
					System.out.println("内容" + sb.toString());
				}
				}
			}
			System.out.println("***************end" + pageNum
					+ "页***********************");

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * if(pageNum != pageSize){
		 * getCoverageDetails(++pageNum,IndexUrl,pageUrl); }else{
		 * System.out.println("打印完毕"); return 0; }
		 */
			if(time == null){
				if (!"".equals(ele)) {
					getCoverageDetails(++pageNum, IndexUrl, pageUrl,null);
				}
			}
		System.out.println("打印完毕");
		return 0;
	}

	/**
	 * 
	 * <p>
	 * Title: getViewComment
	 * </p>
	 * <p>
	 * Description:观点评论
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @param IndexUrl
	 * @param @param pageUrl
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getViewComment(int pageNum, String IndexUrl,
			String pageUrl,Date time) {

		System.out.println("***************start" + pageNum
				+ "页***********************");
		Document doc;
		String ele = "";
		String url;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		url = pageUrl.replace("%pageNum%", pageNum + "");
		// url =pageUrl.replace("%pageNum%", "4");
		try {

			doc = Jsoup.connect(url).get();
			ele = doc.select(".next").attr("abs:href");

			Elements element = doc.select(".list li");
			for (Element el : element) {
				Article art = new Article();
				String sendTime = el.select(".time").text();
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				
				if(time != null){
					try {
						if (sdf.parse(sendTime).after(time)) {
							art.setSourceName("财经");
							// 网址
							String articleUrl = el.select(".wzbt a").attr("abs:href");
							System.out.println(articleUrl);
							art.setUrl(articleUrl);
							// id
							int startIndex = articleUrl.lastIndexOf("/");
							int endIndex = articleUrl.lastIndexOf(".shtml");
							System.out.println(startIndex);
							System.out.println(endIndex);
							String articleId = articleUrl.substring(startIndex + 1,
									endIndex);
							System.out.println("id++++++++++" + articleId);
							art.setId(articleId);
							// 标题
							String articleTitle = el.select(".wzbt").text();
							System.out.println("标题" + articleTitle);
							art.setTitle(articleTitle);
							// 简介
							String articleAbstruct = el.select(".subtitle").text();
							System.out.println("简介" + articleAbstruct);
							art.setSummary(articleAbstruct);
							// 作者
							String artcleAuthor = el.select(".author").text();
							System.out.println("作者" + artcleAuthor);
							art.setAuthor(artcleAuthor);
							// 时间
							System.out.println("时间" + sendTime);
							art.setSendTime(sendTime);
							channel.putArticle(articleId.toString(), art);
							String articleStr = new SpiderHttpClient()
									.simulateCookieLoginJson(ViewCommentPagingDetails
											.replace("%articleId%", articleId));
							if (!"{}".equals(articleStr.trim())) {
								int jsonIndexStart = articleStr.indexOf("\"content\"");
								int jsonIndexEnd = articleStr.lastIndexOf(")");
								String articleDetials = articleStr.substring(
										jsonIndexStart + 11, jsonIndexEnd - 4);

								StringBuilder sb = new StringBuilder();
								int i = -1;
								int pos = 0;
								while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
									sb.append(articleDetials.substring(pos, i));
									if (i + 5 < articleDetials.length()) {
										pos = i + 6;
										sb.append((char) Integer.parseInt(
												articleDetials.substring(i + 2, i + 6), 16));
									}
								}
								String content = sb.toString().replace("\\", "");
								content = content.replace("\r\n", "");
								content = content.replace("rn", "");
								
								art.setContent(content.trim());
								System.out.println("内容" + sb.toString());
							}
						  
						}
					} catch (ParseException e) {
						e.printStackTrace();
						continue;
					}
				}else{
					art.setSourceName("财经");
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".shtml");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);
				String articleStr = new SpiderHttpClient()
						.simulateCookieLoginJson(ViewCommentPagingDetails
								.replace("%articleId%", articleId));
				if (!"{}".equals(articleStr.trim())) {
					int jsonIndexStart = articleStr.indexOf("\"content\"");
					int jsonIndexEnd = articleStr.lastIndexOf(")");
					String articleDetials = articleStr.substring(
							jsonIndexStart + 11, jsonIndexEnd - 4);

					StringBuilder sb = new StringBuilder();
					int i = -1;
					int pos = 0;
					while ((i = articleDetials.indexOf("\\u", pos)) != -1) {
						sb.append(articleDetials.substring(pos, i));
						if (i + 5 < articleDetials.length()) {
							pos = i + 6;
							sb.append((char) Integer.parseInt(
									articleDetials.substring(i + 2, i + 6), 16));
						}
					}
					String content = sb.toString().replace("\\", "");
					content = content.replace("\r\n", "");
					content = content.replace("rn", "");
					art.setContent(content.trim());
					System.out.println("内容" + sb.toString());
				}
			  }
			}
			System.out.println("***************end" + pageNum
					+ "页***********************");

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * if(pageNum != pageSize){
		 * getViewComment(++pageNum,pageSize,IndexUrl,pageUrl); }else{
		 * System.out.println("打印完毕"); return 0; }
		 */
		if(time == null){
			if (!"".equals(ele)) {
				getViewComment(++pageNum, IndexUrl, pageUrl,null);
			}
		}
		System.out.println("打印完毕");
		return 0;
	}

	/**
	 * 
	 * <p>
	 * Title: getEconomyExpert
	 * </p>
	 * <p>
	 * Description:经济学家
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @param IndexUrl
	 * @param @param pageUrl
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getEconomyExpert(int pageNum, String IndexUrl,
			String pageUrl,Date time) {
		System.out.println("***************start" + pageNum
				+ "页***********************");
		Document doc;
		String url;
		String totalNums = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (pageNum == 0) {
			url = IndexUrl;
		} else {
			url = pageUrl.replace("%pageNum%", pageNum + "");
		}
		try {

			doc = Jsoup.connect(url).get();
			// 获取总的页数
			String elemntStr = doc.select(".thepg").html();
			String totalNum = elemntStr.substring(
					elemntStr.indexOf("maxPage ="),
					elemntStr.indexOf("var curPage"));
			totalNums = totalNum.substring((totalNum.lastIndexOf("=") + 1),
					totalNum.lastIndexOf(";"));

			// String elementd = doc.select("script").attr("maxPage");
			Elements element = doc.select(".list li");
			for (Element el : element) {
				Article art = new Article();
				String sendTime = el.select(".time").text();
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				
				if(time != null){
					try {
						if (sdf.parse(sendTime).after(time)) {
							art.setSourceName("财经");
							// 网址
							String articleUrl = el.select(".wzbt a").attr("abs:href");
							System.out.println(articleUrl);
							art.setUrl(articleUrl);
							// id
							int startIndex = articleUrl.lastIndexOf("/");
							int endIndex = articleUrl.lastIndexOf(".html");
							System.out.println(startIndex);
							System.out.println(endIndex);
							String articleId = articleUrl.substring(startIndex + 1,
									endIndex);
							System.out.println("id++++++++++" + articleId);
							art.setId(articleId);
							// 标题
							String articleTitle = el.select(".wzbt").text();
							System.out.println("标题" + articleTitle);
							art.setTitle(articleTitle);
							// 简介
							String articleAbstruct = el.select(".subtitle").text();
							System.out.println("简介" + articleAbstruct);
							art.setSummary(articleAbstruct);
							// 作者
							String artcleAuthor = el.select(".author").text();
							System.out.println("作者" + artcleAuthor);
							art.setAuthor(artcleAuthor);
							// 时间
							System.out.println("时间" + sendTime);
							art.setSendTime(sendTime);
							channel.putArticle(articleId.toString(), art);

							Document docDetails = Jsoup.connect(articleUrl).get();
							Element articleElement = docDetails
									.getElementById("the_content");
							if (articleElement != null) {
								String articleDeatils = articleElement.html();
								System.out.println("内容" + articleDeatils);
								if (StringUtils.isNotBlank(articleDeatils)) {
									art.setContent(articleDeatils);
								}
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
						continue;
					}
				}else{
					art.setSourceName("财经");
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".html");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);

				Document docDetails = Jsoup.connect(articleUrl).get();
				Element articleElement = docDetails
						.getElementById("the_content");
				if (articleElement != null) {
					String articleDeatils = articleElement.html();
					System.out.println("内容" + articleDeatils);
					if (StringUtils.isNotBlank(articleDeatils)) {
						art.setContent(articleDeatils);
					}
				}
			  }
			}
			System.out.println("***************end" + pageNum
					+ "页***********************");

		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * if(pageNum != pageSize){
		 * getEconomyExpert(++pageNum,pageSize,IndexUrl,pageUrl); }else{
		 * System.out.println("打印完毕"); return 0; }
		 */
		if(time == null){
		// 如果小于或等于总页数则回掉自己
			if (pageNum < (Integer.parseInt(totalNums.trim()) - 1)) {
				// if(pageNum < 1){
				getEconomyExpert(pageNum + 1, IndexUrl, pageUrl,null);
			}
		}
		System.out.println("打印完毕");
		return 0;
	}

	/**
	 * 
	 * <p>
	 * Title: getArticleData
	 * </p>
	 * <p>
	 * Description:社评详细操作21页
	 * </p>
	 * 
	 * @param @param pageNum
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	private static int getSocietyComment(int pageNum, Date time) {
		System.out.println("***************start" + pageNum
				+ "页***********************");
		Document doc;
		String url;
		String totalNums = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (pageNum == 0) {
			url = IndexUrl;
		} else {
			url = pagingUrl.replace("%pageNum%", pageNum + "");
		}
		try {

			doc = Jsoup.connect(url).get();
			String elemntStr = doc.select(".thepg").html();
			String totalNum = elemntStr.substring(
					elemntStr.indexOf("maxPage ="),
					elemntStr.indexOf("var curPage"));
			totalNums = totalNum.substring((totalNum.lastIndexOf("=") + 1),
					totalNum.lastIndexOf(";"));
			Elements element = doc.select(".list li");
			for (Element el : element) {
				String sendTime = el.select(".time").text();
				
				if (sendTime != null && !"".equals(sendTime)
						&& sendTime.contains("年") && sendTime.contains("月")
						&& sendTime.contains("日")) {
					sendTime = sendTime.replace("年", "-");
					sendTime = sendTime.replace("月", "-");
					sendTime = sendTime.replace("日", "");
				}
				
				Article art = new Article();
				// 网址
				String articleUrl = el.select(".wzbt a").attr("abs:href");
				System.out.println(articleUrl);
				art.setUrl(articleUrl);
				// id
				int startIndex = articleUrl.lastIndexOf("/");
				int endIndex = articleUrl.lastIndexOf(".html");
				System.out.println(startIndex);
				System.out.println(endIndex);
				String articleId = articleUrl.substring(startIndex + 1,
						endIndex);
				System.out.println("id++++++++++" + articleId);
				if(time != null){
					try {
						art.setSourceName("财经");
						if (sdf.parse(sendTime).after(time)) {
							art.setId(articleId);
							// 标题
							String articleTitle = el.select(".wzbt").text();
							System.out.println("标题" + articleTitle);
							art.setTitle(articleTitle);
							// 简介
							String articleAbstruct = el.select(".subtitle").text();
							System.out.println("简介" + articleAbstruct);
							art.setSummary(articleAbstruct);
							// 作者
							String artcleAuthor = el.select(".author").text();
							System.out.println("作者" + artcleAuthor);
							art.setAuthor(artcleAuthor);
							// 时间
							System.out.println("时间" + sendTime);
							art.setSendTime(sendTime);
							channel.putArticle(articleId.toString(), art);

							Document docDetails = Jsoup.connect(articleUrl).get();
							Element articleElement = docDetails
									.getElementById("the_content");
							if (articleElement != null) {
								String articleDeatils = articleElement.html();
								System.out.println(articleDeatils);
								if (StringUtils.isNotBlank(articleDeatils)) {
									art.setContent(articleDeatils);
								}
							 }
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					
				}else{
				art.setSourceName("财经");
				art.setId(articleId);
				// 标题
				String articleTitle = el.select(".wzbt").text();
				System.out.println("标题" + articleTitle);
				art.setTitle(articleTitle);
				// 简介
				String articleAbstruct = el.select(".subtitle").text();
				System.out.println("简介" + articleAbstruct);
				art.setSummary(articleAbstruct);
				// 作者
				String artcleAuthor = el.select(".author").text();
				System.out.println("作者" + artcleAuthor);
				art.setAuthor(artcleAuthor);
				// 时间
				System.out.println("时间" + sendTime);
				art.setSendTime(sendTime);
				channel.putArticle(articleId.toString(), art);

				Document docDetails = Jsoup.connect(articleUrl).get();
				Element articleElement = docDetails
						.getElementById("the_content");
				if (articleElement != null) {
					String articleDeatils = articleElement.html();
					System.out.println(articleDeatils);
					if (StringUtils.isNotBlank(articleDeatils)) {
						art.setContent(articleDeatils);
					}
				 }
				}
			}
			System.out.println("***************end" + pageNum
					+ "页***********************");

		} catch (IOException e) {
			e.printStackTrace();
		}
		 if(time == null){
			if (pageNum < (Integer.parseInt(totalNums.trim()) - 1)) {
				getSocietyComment(++pageNum,null);
			}
		 }
		System.out.println("打印完毕");
		return 0;
	}

	// 封面文章-财经网
	private static int getArticleData(int pageNum, Date time) {
		System.out.println("***********************start***" + pageNum
				+ "页************************************");
		String urlConent = "";
		String sendTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (pageNum == 0) {
			urlConent = new SpiderHttpClient().read_html(CoverNewsUrl);
		} else {
			urlConent = new SpiderHttpClient().read_html(CoverNewspageNumUrl.replace(
					"%pageNum%", pageNum + ""));
		}
		Document doc = Jsoup.parse(urlConent);
		// 获取总的页数
		String elemntStr = doc.select(".thepg").html();
		String totalNum = elemntStr.substring(elemntStr.indexOf("maxPage ="),
				elemntStr.indexOf("var curPage"));
		String totalNums = totalNum.substring((totalNum.lastIndexOf("=") + 1),
				totalNum.lastIndexOf(";"));

		Elements element = doc.select(".list li");
		if (element.size() <= 0) {
			logger.debug("this error for element not have " + element.size()
					+ "data");
			return 1;
		}

		for (Element el : element) {
			sendTime = el.select(".time").text();
			if (sendTime != null && !"".equals(sendTime)
					&& sendTime.contains("年") && sendTime.contains("月")
					&& sendTime.contains("日")) {
				sendTime = sendTime.replace("年", "-");
				sendTime = sendTime.replace("月", "-");
				sendTime = sendTime.replace("日", "");
			}
			Article art = new Article();
			String articleUrl = el.select(".wzbt a").attr("abs:href");
			// int indexNum = articleUrl.indexOf("-");
			String articleId = articleUrl.substring(
					(articleUrl.lastIndexOf("/") + 1),
					articleUrl.lastIndexOf(".html"));
			// 时间
			// String sendTime = articleUrl.substring(indexNum-4,indexNum+6);
			if (time != null) {
			
					try {
						if (sdf.parse(sendTime).after(time)) {
							art.setSourceName("财经");
							if (StringUtils.isNotBlank(el.select(".time").text())) {
								art.setSendTime(sendTime);
							}
							System.out.println("时间" + sendTime);

							// if(!articleId.matches("[a-zA-Z]*")){
							// 名称
							if (StringUtils.isNotBlank(el.select(".wzbt a").text())) {
								art.setTitle(el.select(".wzbt a").text());
							}
							System.out.println("名称" + el.select(".wzbt a").text());
							// 链接

							if (StringUtils.isNotBlank(articleUrl)) {
								art.setUrl(articleUrl);
							}
							System.out.println("链接" + articleUrl);
							// 获取id

							if (StringUtils.isNotBlank(articleId)) {
								System.out.println(articleId);
								art.setId(articleId);

							}
							System.out.println("文章id" + articleId);
							// 简介
							if (StringUtils.isNotBlank(el.select(".subtitle").text())) {
								art.setSummary(el.select(".subtitle").text());
							}
							System.out.println("简介" + el.select(".subtitle").text());

							// 作者
							if (StringUtils.isNotBlank(el.select(".author a").text())) {
								art.setAuthor(el.select(".author a").text());
							}
							System.out.println("作者" + el.select(".author a").text());
							// 内容
							String detailsUrl = new SpiderHttpClient().read_html(articleUrl);
							Document detailsDoc = Jsoup.parse(detailsUrl);
							// String articleDeatils = detailsDoc.select(".artical").text();
							Element articleElement = detailsDoc
									.getElementById("the_content");

							if (articleElement != null) {
								String articleImg = articleElement.select("img")
										.attr("src");
								if (!"".equals(articleImg) && articleImg != null) {

									// String imgDatas = articleImg.first().attr("src");
									art.setImgUrl(articleImg);
									System.out.println(articleImg);
								}
							}

							if (articleElement != null) {

								String articleDeatils = articleElement.html();
								String fmData = articleElement.select("fm").html();
								articleDeatils.replace(fmData, "");

								if (articleDeatils.contains("fullUrl")) {
									String fullUrlData = articleElement.getElementById(
											"fullUrl").html();
									articleDeatils.replace(fullUrlData, "");
								}
								System.out.println("内容" + articleDeatils);
								if (StringUtils.isNotBlank(articleDeatils)) {
									art.setContent(articleDeatils);
								}
							}
							channel.putArticle(articleId, art);
						}
					} catch (ParseException e) {
						e.printStackTrace();
						continue;
					}

			} else {
				art.setSourceName("财经");
				if (StringUtils.isNotBlank(el.select(".time").text())) {
					art.setSendTime(sendTime);
				}
				System.out.println("时间" + sendTime);

				// if(!articleId.matches("[a-zA-Z]*")){
				// 名称
				if (StringUtils.isNotBlank(el.select(".wzbt a").text())) {
					art.setTitle(el.select(".wzbt a").text());
				}
				System.out.println("名称" + el.select(".wzbt a").text());
				// 链接

				if (StringUtils.isNotBlank(articleUrl)) {
					art.setUrl(articleUrl);
				}
				System.out.println("链接" + articleUrl);
				// 获取id

				if (StringUtils.isNotBlank(articleId)) {
					System.out.println(articleId);
					art.setId(articleId);

				}
				System.out.println("文章id" + articleId);
				// 简介
				if (StringUtils.isNotBlank(el.select(".subtitle").text())) {
					art.setSummary(el.select(".subtitle").text());
				}
				System.out.println("简介" + el.select(".subtitle").text());

				// 作者
				if (StringUtils.isNotBlank(el.select(".author a").text())) {
					art.setAuthor(el.select(".author a").text());
				}
				System.out.println("作者" + el.select(".author a").text());
				// 内容
				String detailsUrl = new SpiderHttpClient().read_html(articleUrl);
				Document detailsDoc = Jsoup.parse(detailsUrl);
				// String articleDeatils = detailsDoc.select(".artical").text();
				Element articleElement = detailsDoc
						.getElementById("the_content");

				if (articleElement != null) {
					String articleImg = articleElement.select("img")
							.attr("src");
					if (!"".equals(articleImg) && articleImg != null) {

						// String imgDatas = articleImg.first().attr("src");
						art.setImgUrl(articleImg);
						System.out.println(articleImg);
					}
				}

				if (articleElement != null) {

					String articleDeatils = articleElement.html();
					String fmData = articleElement.select("fm").html();
					articleDeatils.replace(fmData, "");

					if (articleDeatils.contains("fullUrl")) {
						String fullUrlData = articleElement.getElementById(
								"fullUrl").html();
						articleDeatils.replace(fullUrlData, "");
					}
					System.out.println("内容" + articleDeatils);
					if (StringUtils.isNotBlank(articleDeatils)) {
						art.setContent(articleDeatils);
					}
				}
				channel.putArticle(articleId, art);

			}
		}
		if ("".equals(time) || time == null) {
			// 如果小于或等于总页数则回掉自己
			if (pageNum < (Integer.parseInt(totalNums.trim()) - 1)) {
				// if(pageNum < 1){
				getArticleData(pageNum + 1, null);
			}
		}
		System.out.println("**********************************end" + pageNum
				+ "页end*****************");
		return 0;
	}

}
