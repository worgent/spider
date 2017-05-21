package xiaobai.channel;

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

import com.alibaba.fastjson.JSON;

//import xiaobai.html.HtmlClientUnit;
import xiaobai.html.SpiderHttpClient;
import xiaobai.jdbc.DataWorker;
import xiaobai.util.HttpClientUtil;

public class WeixinSource extends ArticleSource{
	static final Logger loggerAll = LogManager.getLogger("SpiderAll");
	static final Logger loggerError = LogManager.getLogger("SpiderError");
	static final Logger loggerSpider = LogManager.getLogger("Spider");
	
	//微信抓取地址,参数包括openid,页数
	private static final String WeiXinSerchUrl = 
			"http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=%openid%&page=%page%";
	private static DataWorker data = null;
	private static SpiderHttpClient spiderHttpClient;

	public static void main(String[] args) {
		
		//readWeiXinHtml("oIWsFtxo3oqejVy4KaJ4RDMVIrE0");
	}

	public WeixinSource(){
		super();
		spiderHttpClient = new SpiderHttpClient();
		data = new DataWorker();
	}
//	@Override
//	public int updateInfo() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int updateArticles() {
//		// TODO Auto-generated method stub
//		return readWeiXin(featureCode);		
//	}
//
//	@Override
//	public int save() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	protected int loadArticles() {
//		// TODO Auto-generated method stub
//		articles.clear();
//		
//		return 0;
//	}
//
//	@Override
//	protected int loadInfo() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	@Override
	public int fetchArticles()
	{
		readWeiXin(this.featureCode);
		return 0;
	}
	/**
	 * 读取指定微信公众号的所有文章
	 * @param openid
	 */
	@SuppressWarnings("rawtypes")
	private int readWeiXin(String openid){
		loggerAll.info("readWeiXin,"+openid);
		//获取微信的数据
		String url = WeiXinSerchUrl.replace("%openid%", openid).replace("%page%", 1+"")+"&t="+new Date().getTime();
		//loggerAll.info("访问路径"+url);
		loggerSpider.info("readWeixin,openid,"+openid);
		String content = spiderHttpClient.read_html(url);
		//String content = HtmlClientUnit.getPageSource(url);
		//loggerAll.debug("访问结束,抓取回得网页数据:"+content);
		//截取字符串，拼接成json数据
		String stringJson = content.substring(0, content.indexOf("})")+1).replace("sogou.weixin.gzhcb(", "");
		Map weiXinMap = (Map) JSON.parse(stringJson);
		if(weiXinMap ==null ){
			loggerError.error("json parse error, null,"+openid+","+stringJson+","+content);
			return 1;
		}
		
		
		loggerAll.debug("获取文章列表的json数据"+weiXinMap);
		int totalPage = Integer.parseInt(weiXinMap.get("totalPages").toString());
		loggerSpider.info("微信公众号ID:"+openid+",文章总页数:"+totalPage);

		if(totalPage < 1)
		{
			loggerError.error("total page number error,"+openid+","+totalPage);
			return 1;
		}

		for(int i=1; i<=totalPage; i++ )	
		{
			switch(readWeixin(openid, i))
			{
			case 0:
				break;
			//1表示遇到页面返回
			case 1:
				return 0;
			default:
				loggerError.error("readweixin error,openid:"+openid+",page:"+i);
			}
		}
		
		return 0;
	}	
	/**
	 * 读取指定微信公众号的指定页,并将文章按照增量方式插入到数据库中
	 * @param openid 微信公众号ID
	 * @param page 页码
	 */
	@SuppressWarnings("rawtypes")
	private int readWeixin(String openid, int page){
		try{
			Thread.currentThread();
			Thread.sleep(15000);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		//获取微信的数据
		String url = WeiXinSerchUrl.replace("%openid%", openid).replace("%page%", page+"")+"&t="+new Date().getTime();
		//loggerAll.debug("访问路径"+url);
		loggerSpider.info("readWeixin,page,"+openid+","+page);
		
		String content = spiderHttpClient.read_html(url);
		//String content = HtmlClientUnit.getPageSource(url);
		//logger.debug("访问结束");
		//截取字符串，拼接成json数据
		String stringJson = content.substring(0, content.lastIndexOf("})")+1).replace("sogou.weixin.gzhcb(", "");
		Map weiXinMap = (Map) JSON.parse(stringJson);
		
		if(weiXinMap == null ){
			loggerError.error("json parse error, null,"+openid+","+page+","+stringJson+","+content);
			//尝试恢复,清除cookie
			//并设置新的cookie
			return 1;
		}
		
		loggerAll.debug("获取文章列表的json数据"+weiXinMap);
		int totalPage = Integer.parseInt(weiXinMap.get("totalPages").toString());
		
		List list = (List) weiXinMap.get("items");
			
		// 获取微信的文章数据
		//logger.debug("开始循环文章");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();

			Document doc = Jsoup.parse(object.toString());

			Elements docid = doc.select("docid");
			Elements title = doc.select("title1");
			Elements visitUrl = doc.select("url");
			Elements sourceName = doc.select("sourcename");
			Elements sendTime = doc.select("date");
			Elements summary = doc.select("content");
			Elements img = doc.select("imglink");

			//确认文章是否已抓取
			//如果已抓取则直接跳过,文件重复则意味着上次这里已经抓过,直接跳出
			if(findArticleByChannelID(docid.text()) != null)
			{
				loggerSpider.info("文章已存在,抓取结束,文章ID:"+docid.text()+",标题:"+title.text());
				return 1;
			}
			Article article = new Article();
			article.setSourceID(id);
			article.setCategory(category);
			article.setArticleSourceID(docid.text());;
			article.setTitle(title.text());
			article.setUrl(visitUrl.text());
			//article.setCategory("科技");
			loggerAll.debug("文章的访问路径：" + visitUrl.text());
			//String articleHtml = HttpClientUtil.getPageSource("http://weixin.sogou.com"+visitUrl.text());
			try{
				Thread.currentThread();
				Thread.sleep(10000);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			String articleHtml = spiderHttpClient.read_html("http://weixin.sogou.com" + visitUrl.text());

			Document articleDoc = Jsoup.parse(articleHtml.toString());
			Element articleContent = articleDoc.getElementById("js_content");
			try {
				article.setContent(articleContent.toString().replace(
						"data-src", "src"));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			//logger.debug("文章标题" + title.text());
			LogManager.getLogger("File").debug("文章内容" + articleContent.toString());
			article.setSendTime(sendTime.text());
			article.setSummary(summary.text());
			article.setShowImgUrl(img.text());

			try {
				int id = data.insertArticle(article);
				if( id < 0 )
				{
					loggerError.error("insertArticle error");
					return 2;
				}
				article.setId(id);
				//logger.debug("插入数据结束");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//存入
			articles.add(article);
			loggerSpider.info("存入文章,标题:"+title.text()+",ID:"+docid.text());
		}

		return 0;
	}
	
}
