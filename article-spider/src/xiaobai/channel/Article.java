package xiaobai.channel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xiaobai.base.BasicStatistics;
/**
 * @desc   文章相关数据
 * @author worgen
 *
 */
public class Article{
	//文章唯一ID,数据库存储用
	private	int		id;
	//文章在的渠道ID
	private int  	sourceID;
	//文章在渠道ID
	private	String		articleSourceID;
	//标题
	private String	title;
	//分类
	private int	category;
	//发布时间
	private String	sendTime;
	//url路径
	private String	url;
	//内容
	private String  content;
	//摘要
	private String summary;
	//图片路径
	private String	showImgUrl;
	//标签
	private List<Integer> tags;
	//父分类的id
	private int categoryID;
	
	public Article()
	{
		
	}
	//已经有唯一ID
//	public Article(int dbArticleID, String articleSourceID, int dbSourceID)
//	{
//		this.dbArticleID = dbArticleID;
//		this.articleSourceID = articleSourceID;
//		this.dbSourceID = dbSourceID;
//	}
//	//没有唯一ID
//	public Article(int dbSourceID, String articleSourceID)
//	{
//		this.dbArticleID = 0;
//		this.dbSourceID = dbSourceID;
//		this.articleSourceID = articleSourceID;
//	}
//	
	//是否相同
	public Boolean equals(Article other)
	{
		if( this.id != 0 
				&& this.id == other.id ) return true;
		if( this.sourceID == other.sourceID ) return true;
		
		return false;
	}
	
	public boolean addTag(Integer e) {
		return tags.add(e);
	}
	public Boolean hasTag(int t)
	{
		return tags.contains(t);
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getArticleSourceID() {
		return articleSourceID;
	}
	public void setArticleSourceID(String articleSourceID) {
		this.articleSourceID = articleSourceID;
	}
	public String getShowImgUrl() {
		return showImgUrl;
	}
	public void setShowImgUrl(String showImgUrl) {
		this.showImgUrl = showImgUrl;
	}
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	public int getSourceID() {
		return sourceID;
	}
	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
	}
	public int getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	



	
}
