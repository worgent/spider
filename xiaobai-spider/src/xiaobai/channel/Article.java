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
public class Article extends BasicStatistics{
	//文章渠道ID
	private String	id;
	//标题
	private String	title;
	//分类
	private String	sort;
	//发布时间
	private String	sendTime;
	//url路径
	private String	url;
	//评论
	private List<Comment> comments = new ArrayList<Comment>();
	//访问详细数据
	private List<ArticleDailyData> visitDetails;
	//推荐量
	private int recommendNum;
	//内容
	private String  content;
	//摘要
	private String summary;
	//作者
	private String author;
	//来源
	private String sourceName;
	
	//图片路径
	private String	imgUrl;
	
	public Article()
	{
		super();
	}
	
	public Article(String id, String title)
	{
		this.id = id;
		this.title = title;
	}

	public void dump()
	{
		System.out.println("------USER DUMP-------:");

		System.out.println("article ID:"+id);
		System.out.println("title:"+title);
		System.out.println("sendtime:"+sendTime);
		System.out.println("url:"+url);
		super.dump();
		
		//输出评论
		for( Comment c : comments )
		{
			c.dump();
		}
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public int commentSize() {
		return comments.size();
	}
	
	public void clearComments(){
		comments.clear();
	}

	public boolean add(Comment e) {
		return comments.add(e);
	}


	public boolean add(ArticleDailyData e) {
		return visitDetails.add(e);
	}

	public Iterator<Comment> commentsIterator() {
		return comments.iterator();
	}
	
	public Iterator<ArticleDailyData> ArticleDataIterator() {
		return visitDetails.iterator();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	//fastjson需要
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<ArticleDailyData> getVisitDetails() {
		return visitDetails;
	}
	public void setVisitDetails(List<ArticleDailyData> visitDetails) {
		this.visitDetails = visitDetails;
	}
	public int getRecommendNum() {
		return recommendNum;
	}
	public void setRecommendNum(int recommendNum) {
		this.recommendNum = recommendNum;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}
