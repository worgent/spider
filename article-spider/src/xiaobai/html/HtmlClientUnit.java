package xiaobai.html;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.JavaScriptPage;

/*
 * 使用htmlunit实现
 */
public class HtmlClientUnit {
	static final Logger loggerAll = LogManager.getLogger("SpiderAll");
	static final Logger loggerError = LogManager.getLogger("SpiderError");
	static final Logger loggerSpider = LogManager.getLogger("Spider");

	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		loggerSpider.info(
				getPageSource("http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=oIWsFt3kD3sk1S96_3DknllqqOYg&page=1&t=1421838941585"));
		return;
	}

	  public static String getPageSource(String url) {
			WebClient webClient = new WebClient();
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setTimeout(35000);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			Page page = null;
			try {
				page = webClient.getPage(url);
				if( page.isHtmlPage() ){
					loggerAll.info(((HtmlPage)page).asXml());
					return ((HtmlPage)page).asXml();
				}
				else
				{
					loggerAll.info(((JavaScriptPage)page).getContent());
					return ((JavaScriptPage)page).getContent();
				}

			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loggerError.info("getPageSource,FailingHttpStatusCodeException");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loggerError.info("getPageSource,MalformedURLException");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loggerError.info("getPageSource,IOException");
			}
			return "";
	  }
}
