package xiaobai.util.timing;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzDealData {

	public static void main(String[] args) {
		 try{  
	            SchedulerFactory schedFact  =   new  org.quartz.impl.StdSchedulerFactory();  
	            Scheduler sched  =  schedFact.getScheduler();  
	            sched.start();  
	            JobDetail jobDetail  =   new  JobDetail( " souhu toutiao si detal data" ,  
	                     " souhu toutiao " , TimingDealData.class );  
	           
	            CronTrigger trigger  =   new  CronTrigger( " souhu toutiao si detal data" ,  
	            		" souhu toutiao " );  
	             /**每天早晨9点执行一次 */   
//	            trigger.setCronExpression( "9 * * * * ?" );  //测试每两秒执行一次
	            trigger.setCronExpression( "0 0 10 * * ?" );  
	            sched.scheduleJob(jobDetail, trigger);  
	        }   catch  (Exception e)   {  
	            e.printStackTrace();  
	        }   
	    }  
}
