package xiaobai.channel;
import java.util.*;

import com.alibaba.fastjson.JSON;

import xiaobai.base.BasicStatistics;
/**
 * 渠道信息
 * @author worgen
 *
 */
public class Channel extends BasicStatistics{
	//渠道名称
	private String	name;
	//订阅数
	private int subscribeNum;
	//文章总数
	private int articleSum;
	//文章数据,文章ID和
	private Map<String, Article> articles = new HashMap<String, Article>();
	//每日访问数据
	private Map<String, ChannelDailyData> visitDetails = new HashMap<String, ChannelDailyData>();
	//用户数据
	private Map<Long, User> users = new HashMap<Long, User>();
	//较昨日的增长量
	private int yesterdayNewVisitSum;
	//昨日访问人数
	private int yesterdayVisitSum;
	//昨日评论数
	private int yesterdayCommentSum;
	//月平均阅读数
	private int monthMeanReadNum;
	//月平均活跃粉丝
	private int monthMeanFans;
	//视频数据
	private Map<String, PayVideo> videos = new HashMap<String, PayVideo>();
	//所有视频详细数据
	private  AllVideo allVideo;
	//视频的播放具体数据
	private Map<String, Video> video = new HashMap<String, Video>();
	
	
	public Channel()
	{
		super();
	}
	public Channel(String name) {
		super();
		this.setName(name);
		this.subscribeNum = 0;
		this.articleSum = 0;
	}

	//文章操作接口
	public int articleSize() {
		return articles.size();
	}
	public boolean containsArticle(String key) {
		return articles.containsKey(key);
	}
	public Article getArticle(String key) {
		return articles.get(key);
	}
	public Article putArticle(String key, Article value) {
		return articles.put(key, value);
	}
	public void clearArticle()
	{
		articles.clear();
	}
	public Set<String> articlekeySet() {
		return articles.keySet();
	}

	//public 
	//每日数据操作接口
	public int dailyDataSize(){
		return visitDetails.size();
	}
	public boolean containsDay(String key){
		return visitDetails.containsKey(key);
	}
	public ChannelDailyData getDailyData(String key){
		return visitDetails.get(key);
	}
	public ChannelDailyData putDailyData(String key, ChannelDailyData value){
		return visitDetails.put(key, value);
	}
	public void clearDailyData()
	{
		visitDetails.clear();
	}
	//渠道用户操作接口
	public int userSize() {
		return users.size();
	}
	public boolean containsUser(Long key) {
		return users.containsKey(key);
	}
	public User getUser(Long key) {
		return users.get(key);
	}
	public User putUser(Long key, User value) {
		return users.put(key, value);
	}
	public void clearUser()
	{
		users.clear();
	}
	
	public int getSubscribeNum() {
		return subscribeNum;
	}
	public void setSubscribeNum(int subscribeNum) {
		this.subscribeNum = subscribeNum;
	}

	public int getArticleSum() {
		return articleSum;
	}
	public void setArticleSum(int articleSum) {
		this.articleSum = articleSum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//fastjson需要,private对象没有set-get无法转成json
	public Map<String, Article> getArticles() {
		return articles;
	}

	public void setArticles(Map<String, Article> articles) {
		this.articles = articles;
	}

	public Map<String, ChannelDailyData> getVisitDetails() {
		return visitDetails;
	}

	public void setVisitDetails(Map<String, ChannelDailyData> visitDetails) {
		this.visitDetails = visitDetails;
	}

	public Map<Long, User> getUsers() {
		return users;
	}

	public void setUsers(Map<Long, User> users) {
		this.users = users;
	}
	
	public int getYesterdayNewVisitSum() {
		return yesterdayNewVisitSum;
	}
	public void setYesterdayNewVisitSum(int yesterdayNewVisitSum) {
		this.yesterdayNewVisitSum = yesterdayNewVisitSum;
	}
	public int getYesterdayVisitSum() {
		return yesterdayVisitSum;
	}
	public void setYesterdayVisitSum(int yesterdayVisitSum) {
		this.yesterdayVisitSum = yesterdayVisitSum;
	}
	public int getYesterdayCommentSum() {
		return yesterdayCommentSum;
	}
	public void setYesterdayCommentSum(int yesterdayCommentSum) {
		this.yesterdayCommentSum = yesterdayCommentSum;
	}
	public int getMonthMeanReadNum() {
		return monthMeanReadNum;
	}
	public void setMonthMeanReadNum(int monthMeanReadNum) {
		this.monthMeanReadNum = monthMeanReadNum;
	}
	public int getMonthMeanFans() {
		return monthMeanFans;
	}
	public void setMonthMeanFans(int monthMeanFans) {
		this.monthMeanFans = monthMeanFans;
	}
	public Map<String, PayVideo> getVideos() {
		return videos;
	}
	public void setVideos(Map<String, PayVideo> videos) {
		this.videos = videos;
	}
	public AllVideo getAllVideo() {
		return allVideo;
	}
	public void setAllVideo(AllVideo allVideo) {
		this.allVideo = allVideo;
	}
	public Map<String, Video> getVideo() {
		return video;
	}
	public void setVideo(Map<String, Video> video) {
		this.video = video;
	}
	public Video putVideo(String key, Video value){
		return video.put(key, value);
	}
	public PayVideo putVideos(String key, PayVideo value){
		return videos.put(key, value);
	}
	public PayVideo getVideos(String key) {
		return videos.get(key);
	}
}
