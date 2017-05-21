package xiaobai.channel;
/**
 * @desc   评论相关信息
 * @author worgen
 *
 */
public class Comment{
	private Long	id;
	//评论内容
	private String	content;
	//评论时间
	private String	sendTime;
	//评论人ID
	private long	userID;
	//评论文章ID
	private String	articleID;
	private String authorName;
	
	
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	//评论点赞等
	public Comment()
	{
		super();
	}
	
	public Comment(Long id, String content, String sendTime, long userID, String articleID) {
		super();
		this.id = id;
		this.content = content;
		this.sendTime = sendTime;
		this.userID = userID;
		this.articleID = articleID;
	}

	public void dump()
	{
		System.out.println("------COMMENT DUMP-------:");
		System.out.println("commentid:"+id);
		System.out.println("articleID:"+articleID);
		System.out.println("userID:"+userID);
		System.out.println("sendTime:"+sendTime);
		System.out.println("content:"+content);
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public String getArticleID() {
		return articleID;
	}
	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
