package xiaobai.base;
import java.util.List;
import java.util.ArrayList;
import xiaobai.channel.*;
/*
 * 文章渠道类型定义,微信,36kr,传送门等
 * 包含渠道名称,渠道描述
 */
public class Channel {
	//来源类型
	private int		id;
	//来源名称
	private String	name;
	//来源介绍
	private String  desc;

	private List<ArticleSource> sources = new ArrayList<ArticleSource>();
	//根据ID查找source
	public ArticleSource findSourceByID(int id){
		for(ArticleSource as: sources){
			if( as.getId() == id )
			{
				return as;
			}
		}
		return null;
	}
	//根据名称查找source
	public ArticleSource findSourceByName(String name){
		for(ArticleSource as: sources){
			if( as.getName().equals(name) )
			{
				return as;
			}
		}
		return null;		
	}
	//根据特征码查找渠道
	public ArticleSource findSourceByFeatureCode(String featureCode){
		for(ArticleSource as: sources){
			if( as.getFeatureCode().equals(featureCode) )
			{
				return as;
			}
		}
		return null;		
	}
	//插入源
	public Boolean addSource(ArticleSource e)
	{
		return sources.add(e);
	}
	
	public Channel()
	{
	}
	//根据名字初始化
	public Channel(String name)
	{
		this.name = name;
		//如果数据库没有该源类型,先执行插入
		
		//从数据库查找源类型
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<ArticleSource> getSources() {
		return sources;
	}
	public void setSources(List<ArticleSource> sources) {
		this.sources = sources;
	}

	





}
