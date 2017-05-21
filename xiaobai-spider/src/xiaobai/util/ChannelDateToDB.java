package xiaobai.util;

import xiaobai.analyze.SouHuAnalyze;
import xiaobai.analyze.ToutiaoAnalyze;
import xiaobai.channel.Channel;
import xiaobai.jdbc.DataWorker;

public class ChannelDateToDB {
	public static void makeChannelDateToDB(){
		System.out.println("执行.......");  
        DataWorker a = new DataWorker();
	
			//获取搜狐的渠道数据
			
        boolean isClear = a.deleteSourceData();
        if(isClear){
			 System.out.println("成功清除原来数据.......");  
		}
        
		Channel channel1 = SouHuAnalyze.getSouhuDatas();
		a.insertSouhu(channel1);

		//获取头条的渠道数据
		Channel channel2 = ToutiaoAnalyze.getChannel();
		a.insertToutiao(channel2);
			
		
			
		boolean isTSecceed = a.callProcedure();
		if(isTSecceed){
			 System.out.println("成功.......");  
		}else{
			System.out.println("失败统计.......");  
		}
	}
	
	public static void main(String[] args) {
		makeChannelDateToDB();
	}
}
