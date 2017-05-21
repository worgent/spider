package xiaobai.analyze;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xiaobai.channel.Article;
import xiaobai.html.SpiderHttpClient;
import xiaobai.jdbc.DataWorker;
import xiaobai.util.HttpClientUtil;
import xiaobai.util.ReadExcelUtil;
import xiaobai.util.SerializableUtil;
import xiaobai.util.StringUtil;
import xiaobai.util.poi.ExportExcel;
import xiaobai.util.poi.PoiExcle;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.log.Log;

public class WeiXinSpiders {
	static final Logger logger = LogManager.getLogger(SouHuAnalyze.class
			.getName());
	private static Map<String, Article> articleMap = new HashMap<String, Article>();
	private static String WinXinSerchUrl = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=%openid%&page=%page%";
	private static int page = 1;
	private static DataWorker data = null;
	private static SpiderHttpClient spiderHttpClient;

	public WeiXinSpiders(String DB_FILE) {
		// data = new DataWorker();
		spiderHttpClient = new SpiderHttpClient();
	}
	
	public WeiXinSpiders() {
		// data = new DataWorker();
		spiderHttpClient = new SpiderHttpClient();
	}

	public static void main(String[] args) {
		// getArticles();
//		WeiXinSpiders weiXinSpiders = new WeiXinSpiders();
		getArticles("C:\\Users\\Lenovo\\Desktop\\html");
		// String stringJson = JSON.toJSONString(articleMap);
		// SerializableUtil.write(stringJson, "","");
	}

	public int getWinXinArticle(String url, String date) {
		try {
			String openId = getOpenId(url);
			readWinXinHtml(openId, date);
			String stringJson = JSON.toJSONString(articleMap);
			SerializableUtil.write(stringJson, "C:\\winxin\\", openId
					+ ".pojo");
			logger.debug(" 读取了"+articleMap.size()+"条数据");
			logger.debug(" 开始导出excel数据");
			PoiExcle.insertExcle(articleMap,"C:\\winxin\\",openId);
			logger.debug(" excel导出成功");
			articleMap = new HashMap<String, Article>();
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}

	public static void getArticles(String path) {
		String[] paths = getExcelPath(path);
		for (int i = 0; i < paths.length; i++) {
			ReadExcelUtil readExcelUtil = new ReadExcelUtil(paths[i]);
			Map<Integer, String> urlMap = readExcelUtil
					.readWinXinExcelContent(0);
			for (int j = 1; j < urlMap.size() + 1; j++) {
				logger.debug("*************************************************");
				logger.debug("读取第" + j + "个opendId" + urlMap.get(j));
				page = 1;
				readWinXinHtml(getOpenId(urlMap.get(j)), "");

				// 数据本地化
				String stringJson = JSON.toJSONString(articleMap);
				SerializableUtil.write(stringJson, "f:\\winxin\\",
						getOpenId(urlMap.get(j)) + ".pojo");
				articleMap = new HashMap<String, Article>();
				// 保存到数据库

			}
		}

	}

	public static String getOpenId(String url) {
		// http://weixin.sogou.com/gzh?openid=oIWsFt4iyUi5tPnnbIrRQLi2BQOY
		return url.replace("http://weixin.sogou.com/gzh?openid=", "");
	}

	/**
	 * 可以单个文件，也可以是文件目录，返回string数组，数组中是单个文件的路径
	 * <p>
	 * Title: getExcelPath
	 * </p>
	 * <p>
	 * Description:TODO
	 * </p>
	 * 
	 * @param @param path
	 * @param @return 设定文件
	 * @return String[] 返回类型
	 * @throws
	 */
	public static String[] getExcelPath(String path) {
		File file = new File(path);

		if (!file.isDirectory()) {
			return new String[] { path };
		}
		File[] files = file.listFiles();
		String[] string = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			string[i] = files[i].getAbsolutePath();
		}
		return string;
	}

	@SuppressWarnings("rawtypes")
	public static void readWinXinHtml(String openid, String date) {
		// 获取微信的数据
		boolean dateFlag = true;
		logger.debug("开始访问路径");
		String url = WinXinSerchUrl.replace("%openid%", openid).replace(
				"%page%", page + "")
				+ "&t=" + new Date().getTime();
		logger.debug("访问路径" + url);

		String sougouJsonString = spiderHttpClient.simulateCookieLoginJson(url);
		// String sougouJsonString = HttpClientUtil.getPageSource(url);
		logger.debug("访问结束");
		// 截取字符串，拼接成json数据
		String stringJson = sougouJsonString.substring(0,
				sougouJsonString.indexOf("})") + 1).replace(
				"sogou.weixin.gzhcb(", "");
		Map weiXinMap = (Map) JSON.parse(stringJson);
		int totalPage = 0;

		if (weiXinMap == null) {
			SpiderHttpClient.saveHtml("F:/test.html", stringJson);
			// 执行cmd命令，打开网页
			try {
				Process process = Runtime.getRuntime().exec(
						"cmd /c F:/test.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			url = WinXinSerchUrl.replace("%openid%", openid).replace("%page%",
					page + "")
					+ "&t=" + new Date().getTime();
			sougouJsonString = new SpiderHttpClient()
					.simulateCookieLoginJson(url);
			stringJson = sougouJsonString.substring(0,
					sougouJsonString.indexOf("})") + 1).replace(
					"sogou.weixin.gzhcb(", "");
			weiXinMap = (Map) JSON.parse(stringJson);
		}

		if (weiXinMap != null) {

			logger.debug("获取文章列表的json数据" + weiXinMap);
			totalPage = Integer
					.parseInt(weiXinMap.get("totalPages").toString());

			if (page == 1) {
				logger.debug("总共有" + totalPage
						+ "**************************************");
			}

			List list = (List) weiXinMap.get("items");

			// 获取微信的文章数据
			logger.debug("开始循环文章");
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Article article = new Article();
				Object object = (Object) iterator.next();

				Document doc = Jsoup.parse(object.toString());

				Elements docid = doc.select("docid");
				Elements title = doc.select("title1");
				Elements visitUrl = doc.select("url");
				Elements sourceName = doc.select("sourcename");
				Elements sendTime = doc.select("date");
				Elements summary = doc.select("content");
				Elements img = doc.select("img");

				if (sendTime.text().compareTo(date) > 0) {

					article.setId(docid.text());
					article.setTitle(title.text());
					article.setUrl(visitUrl.text());
					article.setSort("科技");
					logger.debug("获取文章内容");
					logger.debug("文章的访问路径：" + visitUrl.text());
					String articleHtml = HttpClientUtil.getPageSource(visitUrl
							.text());

					logger.debug("获取文章内容结束");
					Document articleDoc = Jsoup.parse(articleHtml.toString());
					Element articleContent = articleDoc
							.getElementById("js_article");
					try {
						article.setContent(articleContent.toString().replace(
								"data-src", "src"));
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					logger.debug("文章标题" + title.text());
					logger.debug("文章内容" + articleContent.toString());
					article.setSourceName(sourceName.text());
					article.setSendTime(sendTime.text());
					article.setSummary(summary.text());
					article.setImgUrl(img.attr("src"));
					try {
						// data.insertArticle(article);
						logger.debug("插入数据结束");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					articleMap.put(docid.text(), article);
				}else{
					dateFlag = false;
					break;
				}
			}
		}
		if (dateFlag && page <= totalPage) {
			page++;
			logger.debug("读第" + (page) + "页");
			readWinXinHtml(openid, date);
		}
	}

}
