package xiaobai.util.poi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xiaobai.channel.Article;

public class PoiExcle {
	public static void main(String[] args) {
		//insertExcle(null, null);
	}

	public static int insertExcle(Map articles,String path,String fileName) {
		ExportExcel<Articles> ex = new ExportExcel<Articles>();
		String[] headers = { "文章名称","地址","渠道名" };
		List<Articles> dataset = new ArrayList<Articles>();
		for (Object articeId : articles.keySet()) {
			Article aritlce = (Article) articles.get(articeId);
			 dataset.add(new Articles(aritlce.getTitle(),aritlce.getUrl(),aritlce.getSourceName()));
		}

		try {
			File file = new File(path);
			if(file.exists()){
				file.mkdir();
			}
			OutputStream out = new FileOutputStream(new File(path+fileName+".xls"));
			ex.exportExcel(headers, dataset, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

}
