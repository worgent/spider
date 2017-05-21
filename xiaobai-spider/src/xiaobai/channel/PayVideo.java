package xiaobai.channel;

import java.util.HashMap;
import java.util.Map;

public class PayVideo {
	
	//时间
	private String	dateTime;
	//playNum分成播放数
	private String playNum;
	//totalIncome总收益
	private String totalIncome;
	//详情地址
	private String detailUrl;
	//详细数据
	private Map<String, VideoDailyData> videoDailyData = new HashMap<String, VideoDailyData>();
	
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getPlayNum() {
		return playNum;
	}
	public void setPlayNum(String playNum) {
		this.playNum = playNum;
	}
	public String getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public Map<String, VideoDailyData> getVideoDailyData() {
		return videoDailyData;
	}
	public void setVideoDailyData(Map<String, VideoDailyData> videoDailyData) {
		this.videoDailyData = videoDailyData;
	}
	public VideoDailyData putVideos(String key, VideoDailyData value){
		return videoDailyData.put(key, value);
	}
	public VideoDailyData getVideoDailyData(String key) {
		return videoDailyData.get(key);
	}
}
