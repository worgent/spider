package xiaobai.channel;
import java.util.*;

import com.alibaba.fastjson.JSON;

import xiaobai.base.Channel;
import xiaobai.base.BasicStatistics;
/**
 * 渠道类型
 * @author worgen
 *
 */
public class ArticleSource{
	//数据库唯一值
	protected int		id;
	//渠道ID	 
	protected int		channelID;
	//来源名称
	protected String	name;
	//来源介绍
	protected String	desc;
	//分类
	protected int		category;
	//渠道特征码,会根据特征码进行文章的抓取;
	
	
	//不同渠道类型不同;
	//微信的特征码为openid,
	//其他渠道的特征码为url;
	protected String	featureCode;
	//文章列表,key为文章ID,value为文章对象
	protected List<Article> articles = new ArrayList<Article>();
	//根据id查找文章
	public Article findArticleByID(int id){
		for(Article a: articles){
			if( a.getId() == id )
			{
				return a;
			}
		}
		return null;
	}
	//根据渠道查找文章
	public Article findArticleByChannelID(String channelID){
		for(Article a: articles){
			if( a.getArticleSourceID().equals(channelID) )
			{
				return a;
			}
		}
		return null;
	}	
	//插入文章
	public Boolean addArticle(Article e)
	{
		return articles.add(e);
	}

	//抓取文章
	public int fetchArticles()
	{
		return 0;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	public void unsetArticles() {
		this.articles.clear();
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getChannelID() {
		return channelID;
	}
	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}





}
