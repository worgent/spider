package xiaobai.base;
/**
 * @desc   通用统计数据
 * @author worgen
 *
 */
public class BasicStatistics {
	//总曝光量
	private	long exposes;
	//总阅读数
	private int pageViews;
	//总访问人数
	private int userViews;
	//收藏量
	private int favorites;
	//评论量
	private int commentNum;
	//转发量
	private	int shares;
	//赞
	private int voteUps;
	//踩
	private int voteDowns;
	
	
	public BasicStatistics() {
		super();
		this.exposes = 0;
		this.pageViews = 0;
		this.userViews = 0;
		this.favorites = 0;
		this.commentNum = 0;
		this.shares = 0;
		this.voteDowns = 0;
		this.voteUps = 0;
	}
	
	public void dump()
	{
		System.out.println("exposes:"+exposes);
		System.out.println("pageViews:"+pageViews);
		System.out.println("userViews:"+userViews);
		System.out.println("favorites:"+favorites);
		System.out.println("comments:"+commentNum);
		System.out.println("shares:"+shares);
		System.out.println("voteDowns:"+voteDowns);
		System.out.println("voteUps:"+voteUps);
	}
	
	public long getExposes() {
		return exposes;
	}
	public void setExposes(long exposes) {
		this.exposes = exposes;
	}
	public int getPageViews() {
		return pageViews;
	}
	public void setPageViews(int pageViews) {
		this.pageViews = pageViews;
	}
	public int getUserViews() {
		return userViews;
	}
	public void setUserViews(int userViews) {
		this.userViews = userViews;
	}
	public int getFavorites() {
		return favorites;
	}
	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int comments) {
		this.commentNum = comments;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public int getVoteUps() {
		return voteUps;
	}

	public void setVoteUps(int voteUps) {
		this.voteUps = voteUps;
	}

	public int getVoteDowns() {
		return voteDowns;
	}

	public void setVoteDowns(int voteDowns) {
		this.voteDowns = voteDowns;
	}	
	
}
