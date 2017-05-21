package xiaobai.channel;

import xiaobai.base.BasicStatistics;

/**
 * @desc 文章每日访问数据
 * @author worgen
 */
public class ArticleDailyData extends BasicStatistics{
	//日期
	private String date;
	//新增阅读量
	private int addRedNum;
	//评论数
	//private	int	commentNum;
	
	public ArticleDailyData()
	{
		super();
		//this.commentNum = 0;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	//public int getCommentNum() {
	//	return commentNum;
	//}
	//public void setCommentNum(int commentNum) {
	//	this.commentNum = commentNum;
	//}

	public int getAddRedNum() {
		return addRedNum;
	}

	public void setAddRedNum(int addRedNum) {
		this.addRedNum = addRedNum;
	}
	
	
}
