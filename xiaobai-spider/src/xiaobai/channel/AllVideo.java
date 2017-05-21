package xiaobai.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AllVideo {
	//总的播放量
	private String totalPlayNum;
	//总的播放时长
	private String totalPlayTime;
	//每日的播放量
	//private List<VideoDailyData> allVideoDailys = new ArrayList<VideoDailyData>();
	private Map<String, VideoDailyData> allVideoDailys = new HashMap<String, VideoDailyData>();
	
	public String getTotalPlayNum() {
		return totalPlayNum;
	}
	public void setTotalPlayNum(String totalPlayNum) {
		this.totalPlayNum = totalPlayNum;
	}
	public String getTotalPlayTime() {
		return totalPlayTime;
	}
	public void setTotalPlayTime(String totalPlayTime) {
		this.totalPlayTime = totalPlayTime;
	}
	public Map<String, VideoDailyData> getAllVideoDailys() {
		return allVideoDailys;
	}
	public void setAllVideoDailys(Map<String, VideoDailyData> allVideoDailys) {
		this.allVideoDailys = allVideoDailys;
	}
	public VideoDailyData putAllVideoDailys(String key, VideoDailyData value){
		return allVideoDailys.put(key, value);
	}
	public VideoDailyData getAllVideoDailys(String key) {
		return allVideoDailys.get(key);
	}
	
	
	
}
