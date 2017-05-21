package xiaobai.spider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;

import xiaobai.channel.*;
import xiaobai.jdbc.DataWorker;
import xiaobai.base.*;
/*
 * 不眠大蜘蛛
 * 按照指定规则抓取文章
 */
public class ForeverSpider {
	static final Logger loggerAll = LogManager.getLogger("SpiderAll");
	static final Logger loggerError = LogManager.getLogger("SpiderError");
	static final Logger loggerSpider = LogManager.getLogger("Spider");
	//渠道列表
	List<Channel> channels = new ArrayList<Channel>();
	private DataWorker db = new DataWorker();
	
	public ForeverSpider(){
		
	}
	//检查渠道是否已存在
	public Channel findChannelByName(String channelName){
		for(Channel c: channels){
			if( c.getName().equals(channelName) )
			{
				return c;
			}
		}
		return null;
	}
	//检查文章源是否已存在
	public ArticleSource findArticleSource(String channelName, String featureCode){
		Channel c = findChannelByName(channelName);
		if( c == null ) return null;
		return c.findSourceByFeatureCode(featureCode);
	}

	//初始化加载数据库数据
	public int init() throws Exception{
		loggerSpider.info("信息初始化,加载数据库信息");
		//首先加载渠道信息;
		channels = db.queryChannels();
		loggerSpider.info("渠道信息加载完毕,一共"+channels.size()+"渠道");
		for( int i = 0; i < channels.size(); i++ ){
			loggerSpider.info("渠道"+i+":"+JSON.toJSONString(channels.get(i)));
		}
		//然后根据渠道信息加载源信息
		for( Channel c : channels )
		{
			loggerSpider.info("加载渠道文章源:"+JSON.toJSONString(c));
			c.setSources(db.querySourcesByChannel(c));
			//
			for(ArticleSource as : c.getSources()){
				loggerSpider.info("文章源:"+JSON.toJSONString(as));
			}
		}		
		//最后加载文章数据
//		for( Channel c : channels )
//		{	
//			for(ArticleSource as : c.getSources()){
//				loggerSpider.info("加载文章源文章:"+JSON.toJSONString(as));
//				as.setArticles(db.queryArticles(as.getId()));
//				for(Article a: as.getArticles()){
//					loggerAll.info("文章:"+JSON.toJSONString(a));
//				}
//			}
//		}			
		return 0;
	}
	
	
	//执行一次循环
	public int runOnce(){
		loggerSpider.info("开始一次文章抓取逻辑");
		//遍历channels的每一个文章源进行抓取
		for( Channel c : channels )
		{	
			for(ArticleSource as : c.getSources()){
				loggerSpider.info("抓取"+c.getName()+"渠道"+as.getName()+"文章源的文章");
				try {
					as.setArticles(db.queryArticles(as.getId()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return -1;
				}
				
				int ret = as.fetchArticles();
				if(0 != ret)
				{
					loggerError.error("文章抓取失败,错误原因:"+ret);
					return ret;
				}
				
				//
				as.unsetArticles();
			}
		}			
		
		loggerSpider.info("本次文章抓取逻辑结束");

		return 0;
	}
	//序列化
	public int run() throws Exception{
		loggerSpider.info("----------No.1 牛逼的永不睡眠的爬虫开始与行了---------");
		loggerSpider.info("----------No.2 开始初始化-------------------------");
		init();
		loggerSpider.info("----------No.3 开始争取抓取了----------------------");

		//每隔一段时间执行一次
		runOnce();
		
		loggerSpider.info("----------No.* 牛逼的永不睡眠的爬虫睡觉了* ---------");
		return 0;
	}
	public static void main(String[] args) throws Exception{
		ForeverSpider fs = new ForeverSpider();
		fs.run();
	}
}
