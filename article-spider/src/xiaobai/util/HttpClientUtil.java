package xiaobai.util;


import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xiaobai.html.SpiderHttpClient;


/**
 * Created by haixingcheng on 2014/7/25.
 */
public class HttpClientUtil {
	static final Logger logger = LogManager.getLogger(HttpClientUtil.class.getName());
    //get方式
  public static String getPageSource(String url) {
      HttpClient httpClient = new HttpClient();
      GetMethod getMethod = new GetMethod(url);
      String html="";
      try {
    	 // logger.debug("开始执行远程访问");
          int statusCode = httpClient.executeMethod(getMethod);
          //logger.debug("访问页面返回状态："+statusCode);
          if (statusCode != HttpStatus.SC_OK) {
              logger.error("Method failed: "+statusCode+"," + getMethod.getStatusLine());
          }
         // logger.debug(" 读取内容");
          // 读取内容
          InputStream is = getMethod.getResponseBodyAsStream();
         // logger.debug(" 读取内容  liu   ");
          String body = SpiderHttpClient.InputStream2String(is, "utf-8");
          //String body = getMethod.getResponseBodyAsString();
      //    byte[] responseBody = getMethod.getResponseBody();
          // 处理内容
         // logger.debug("处理内容");
          html = new String(body);
          //logger.info(html);
       //   logger.debug("处理结束");
      } catch (Exception e) {
    	  e.printStackTrace();
          logger.debug("页面无法访问", e);
      } finally {
          getMethod.releaseConnection();
      }
      return html;
  }
  
    //post方式
    public static String getPostPageSouce(String UrlPath){
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(UrlPath);
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = postMethod.getResponseBody();
                String html = new String(responseBody);
                System.out.println(html);
                return html;
            }
        } catch (Exception e) {
            System.err.println("页面无法访问");
        }finally{
            postMethod.releaseConnection();
        }
        return null;
    }

    public static void main(String args[]){

//        System.out.println(getPageSource("http://politics.gmw.cn/2014-07/25/content_12167565.htm"));
//       System.out.println(getPageSource("http://3g.k.sohu.com/t/n20793306"));
//       System.out.println(getPostPageSouce("http://3g.k.sohu.com/t/n20793306"));
    	for(int i = 0; i<200 ;i++){
    		getPageSource("http://mp.weixin.qq.com/mp/appmsg/show?__biz=MjM5MTI4NTA0MA==&appmsgid=100090136&itemidx=1&sign=6e1cc276026dbc0bf5ab4bff5fac95eb&3rd=MzA3MDU4NTYzMw==&scene=6#wechat_redirect");
    		System.out.println(i);
    	}
    }


}
