package xiaobai.util.timing;

import java.sql.SQLException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import xiaobai.analyze.SouHuAnalyze;
import xiaobai.analyze.ToutiaoAnalyze;
import xiaobai.channel.Channel;
import xiaobai.jdbc.DataWorker;

public class TimingDealData implements Job{
	
	@Override  
    public void execute(JobExecutionContext arg0) throws JobExecutionException {  
        System.out.println("执行.......");  
        DataWorker a = new DataWorker();
	
			//获取搜狐的渠道数据
			
        boolean isClear = a.deleteSourceData();
        if(isClear){
			 System.out.println("成功清除原来数据.......");  
		}
        
//		Channel channel1 = SouHuAnalyze.getSouhuDatas();
//		a.insertSouhu(channel1);
//		boolean isSecceed = a.callProcedure();
//		if(isSecceed){
//			 System.out.println("搜狐成功.......");  
//		}else{
//			System.out.println("失败统计.......");  
//		}
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
    
	
}
