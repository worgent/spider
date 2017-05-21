package xiaobai.run;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xiaobai.analyze.FinanceEconomics;
import xiaobai.analyze.Kr36Analyze;

public class ThirdData {
	public static void getThirdArticleDate(Date date) throws ParseException{
		
		FinanceEconomics.getAllData(date);
		Kr36Analyze.ArticleAfterTimeDate(date);
	}
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			getThirdArticleDate(sdf.parse("2014-8-22"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
