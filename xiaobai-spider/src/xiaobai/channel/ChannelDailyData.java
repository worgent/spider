package xiaobai.channel;

import xiaobai.base.BasicStatistics;

/**
 * @desc   渠道每日访问数据
 * @author worgen
 * 
 */
public class ChannelDailyData extends BasicStatistics  implements java.io.Serializable{
	//日期
	private String date;	
	//新增订阅数
	private	int	subscribeNum;
	//文章量
	private int articleNum;
	
	public ChannelDailyData()
	{
		super();
		this.subscribeNum = 0;
	}
	
	public void dump()
	{
		System.out.println("------CHANNEL DAILY DUMP-------:");

		System.out.println("date:"+date);		
		super.dump();
		System.out.println("subscribeNum:"+subscribeNum);
		System.out.println("articleNum:"+articleNum);
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSubscribeNum() {
		return subscribeNum;
	}
	public void setSubscribeNum(int subscribeNum) {
		this.subscribeNum = subscribeNum;
	}

	public int getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}
	
	
}
