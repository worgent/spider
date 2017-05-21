package xiaobai.channel;
/*
 * @desc 渠道用户
 * 
 */
public class User{
	//
	private String channel;
	//用户ID
	private long id;
	//用户名称
	private String name;
	//用户头像URL
	private String headUrl;
	//用户主页URL
	private String homeUrl;
	//
	public User()
	{
		super();
	}
	public User(String channel){
		super();
		this.channel = channel;
	}

	public void dump()
	{
		System.out.println("------USER DUMP-------:");

		System.out.println("channel:"+channel);
		System.out.println("userid:"+id);
		System.out.println("name:"+name);
		System.out.println("headUrl:"+headUrl);
		System.out.println("homeUrl:"+homeUrl);
		
	}
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	
	
}
