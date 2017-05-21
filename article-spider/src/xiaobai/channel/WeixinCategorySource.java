package xiaobai.channel;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xiaobai.html.HtmlClientUnit;
import xiaobai.html.SpiderHttpClient;
import xiaobai.jdbc.DataWorker;

public class WeixinCategorySource extends ArticleSource{
	static final Logger loggerAll = LogManager.getLogger("SpiderAll");
	static final Logger loggerError = LogManager.getLogger("SpiderError");
	static final Logger loggerSpider = LogManager.getLogger("Spider");
	private static String categoryName = "%E8%82%B2%E5%84%BF";
	private static String page = "1";
	
	//微信抓取地址,参数包括openid,页数
	private static final String WeiXinSerchUrl =
			"http://weixin.sogou.com/weixin?query=%categoryName%&fr=sgsearch&sut=1325&lkt=0%2C0%2C0&type=2&sst0=%datetime%&page=%page%&ie=utf8&w=01019900&dr=1";
	private static DataWorker data = null;
	private static SpiderHttpClient spiderHttpClient;

	

	public WeixinCategorySource(){
		super();
		data = new DataWorker();
	}
	
	public static void run(){
		WeixinCategorySource data= new WeixinCategorySource();
		List<Category> categorys = query();
		for (Category category : categorys) {
			//页数  分类  分类id 查询条数
			try {
				data.searchWeixinArticleByCategory(5,category.getName(),category.getId(),20,category.getCategoryID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<Category> query(){
		List<Category> categorys = null;
			try {
				 categorys= data.queryCategory();
			} catch (Exception e) {
				loggerError.error("query category error");
				e.printStackTrace();
			}
			return categorys;
	}
	
	//读取微信号中指定页数的文章
	public static void searchWeixinArticleByCategory(int num,String categoryNames,int categoryId,int number,int categoryID) throws Exception{
		 categoryName = new String(categoryNames.getBytes(),"ISO8859-1");
		 page = "1";
		 int NumStatus = 0;
		String datetime = String.valueOf(new Date().getTime());
		//获取微信的数据
		String url = WeiXinSerchUrl.replace("%datetime%", datetime);
		for(int i = 1; i<=num;i++){
			page = String.valueOf(i);
			String content = HtmlClientUnit.getPageSource(url.replace("%page%", page).replace("%categoryName%", categoryName));
			Document doc = Jsoup.parse(content);
			String pageNum = doc.select("#pagebar_container a").last().text();
			
			Elements elements = doc.select(".results .wx-rb3");
			for(Element el: elements){
				if(NumStatus == number){
					return;
				}
				Elements els = el.select(".txt-box");
				//简介
				String introduction = els.select("p").first().text();
				//文章详情地址
				String articleUrl = els.select("a").first().attr("href");
				//发布时间时分
				String date = els.select(".s-p").text();
				date = date.substring(date.length()-5);
				//图片的路径
				String imgUrl = el.select(".img_box2 img").first().attr("src");
				imgUrl =imgUrl.substring((imgUrl.indexOf("&url=")+5), imgUrl.length());
				//文章详情页连接
				int status = searchWeixinArticleByCategoryDetials(articleUrl,date,introduction,categoryNames,categoryId,number,categoryID,imgUrl);
				if(status == 1){
					NumStatus += 1;
					loggerSpider.info("这是:"+categoryNames+"的"+NumStatus+"个有效存储记录");
				}
			}
			
			if(!pageNum.trim().equals("下一页")){
				return;
			}
		}
		
	}
	
	
	public static int searchWeixinArticleByCategoryDetials(String articleUrl,String dateTime,String introduction,String categoryName,int categoryId,int number,int categoryID,String imgUrl){
		String detials = HtmlClientUnit.getPageSource(articleUrl);
		Document detialsDoc = Jsoup.parse(detials);
		//文章id
		int  indexS = articleUrl.indexOf("mid=");
		int  indexE = articleUrl.indexOf("&idx=");
		String articleId = articleUrl.substring(indexS+4,indexE);
		//文章的名称
		String articleName = detialsDoc.select(".rich_media_title").first().text();
		//文章的作者
		String author = detialsDoc.select(".profile_nickname").first().text();
		//文章的时间
		String date = detialsDoc.select("#post-date").text();
		date += " "+dateTime+":00";
		//文章的图片
		//Elements imgUrl = detialsDoc.select(".rich_media_thumb");
		//String imgUrlStr = imgUrl.attr("data-src");
		//文章的内容
		Elements content = detialsDoc.select(".rich_media_content");
		int arId= Integer.valueOf(articleId);
		Article article = new Article();
		loggerSpider.info("名称:"+articleName+"__作者:"+author+"___id:"+articleId);
		
		//文章在渠道IDarticleSourceID
		article.setSourceID(arId);
		//标题title
		article.setTitle(articleName);
		//分类category
		article.setCategory(categoryId);
		//发布时间sendTime
		article.setSendTime(date.replace("月", ":").replace("日", ":"));
		//url路径url
		article.setUrl(articleUrl);
		//内容summary
		//System.out.println(content.toString());
		article.setContent(content.toString());
		//摘要summary
		article.setSummary(introduction);
		//图片路径showImgUrl
		article.setShowImgUrl(imgUrl);
		//父分类的id
		article.setCategoryID(categoryID);
		try {
			int id ;
			
			List<Article> articleSize = data.queryWeixinrticles(arId);
				if(articleSize.size() == 0){ 
					id = data.insertWeixinArticle(article);
				}else{
					loggerSpider.info(articleName+"已经存在"+"_________id:"+arId);
					return 2;
					
				}
				
				if(id < 0){
					return 2;
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
		return 1;
		
	}
	
	
}
