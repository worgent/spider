package xiaobai.util.poi;

public class Articles {
	 private String name;
	 private String url;
	 private String channelName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	@Override
	public String toString() {
		return "Articles [name=" + name + ", url=" + url + ", channelName="
				+ channelName + "]";
	}
	public Articles() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Articles(String name, String url, String channelName) {
		super();
		this.name = name;
		this.url = url;
		this.channelName = channelName;
	}
	 
	 
}
