package xiaobai.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {

	//视频的播放路径
	private String videoUrl;
	//视频的图片路径
	private String videoImageURL;
	//视频的时长
	private String videoTime;
	//视频的名称
	private String videoName;
	//视频上传的时间
	private String videoSendTime;
	//播放完成度
	private String videoPlayFinish;
	//总播放量
	private String videoTotalPlay;
	//昨日播放量
	private String videoYesPlay;
	//详情数据路径
	private String detailsUrl;
	//视频的id
	private String videoId;
	
	//每日的播放量
	private List<VideoDailyData> allVideoDailys = new ArrayList<VideoDailyData>();
	private Map<String, VideoDailyData> allVideoDaily = new HashMap<String, VideoDailyData>(); 
	
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoImageURL() {
		return videoImageURL;
	}
	public void setVideoImageURL(String videoImageURL) {
		this.videoImageURL = videoImageURL;
	}
	public String getVideoTime() {
		return videoTime;
	}
	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public String getVideoSendTime() {
		return videoSendTime;
	}
	public void setVideoSendTime(String videoSendTime) {
		this.videoSendTime = videoSendTime;
	}
	public String getVideoPlayFinish() {
		return videoPlayFinish;
	}
	public void setVideoPlayFinish(String videoPlayFinish) {
		this.videoPlayFinish = videoPlayFinish;
	}
	public String getVideoTotalPlay() {
		return videoTotalPlay;
	}
	public void setVideoTotalPlay(String videoTotalPlay) {
		this.videoTotalPlay = videoTotalPlay;
	}
	public String getVideoYesPlay() {
		return videoYesPlay;
	}
	public void setVideoYesPlay(String videoYesPlay) {
		this.videoYesPlay = videoYesPlay;
	}
	public String getDetailsUrl() {
		return detailsUrl;
	}
	public void setDetailsUrl(String detailsUrl) {
		this.detailsUrl = detailsUrl;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public List<VideoDailyData> getAllVideoDailys() {
		return allVideoDailys;
	}
	public void setAllVideoDailys(List<VideoDailyData> allVideoDailys) {
		this.allVideoDailys = allVideoDailys;
	}
	
	public Map<String, VideoDailyData> getAllVideoDaily() {
		return allVideoDaily;
	}
	public void setAllVideoDaily(Map<String, VideoDailyData> allVideoDaily) {
		this.allVideoDaily = allVideoDaily;
	}
	public VideoDailyData putAllVideoDailys(String key, VideoDailyData value){
		return allVideoDaily.put(key, value);
	}
	public VideoDailyData getAllVideoDaily(String key) {
		return allVideoDaily.get(key);
	}
	
}
