package xiaobai.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA. User: zt Date: 2011-6-1 Time: 15:37:45
 */
public class StringUtil extends StringUtils {
	 
	/**
	 * 该方法是将 valeu = "name:aa;phone:15000000000"格式字符转换为map
	 * 
	 * @param mapText
	 * @return
	 */
	public static Map stringToMap(String mapText) {
		Map map = new HashMap();
		if (StringUtils.isNotEmpty(mapText) && mapText.split(";").length > 0) {
			String[] array = mapText.split(";");
			for (String str : array) {
				String[] keyText = str.split("@"); // 转换key与value的数组
				if (keyText.length < 1) {
					continue;
				}
				String key = keyText[0]; // key
				String value = keyText[1]; // value
				map.put(key, value);
			}
		}
		return map;
	}

	public static String hidePartUserPin(String userPin) {
		if (userPin != null) {
			if (userPin.length() == 1) {
				return userPin + "****";
			} else if (userPin.length() == 2) {
				return userPin.substring(0, 1) + "****";
			} else if (userPin.length() > 2) {
				return userPin.substring(0, 2) + "***";
			}
		}
		return null;
	}

	//
	// /**
	// * 比较老个MONEY是否相等，不相等返回true
	// * @param money1
	// * @param money2
	// * @return
	// */
	// public static boolean isNotEqualMoney(Money money1,Money money2){
	// if(money1.compareTo(money2)!=0){
	// return true;
	// }
	// return false;
	// }
	public static Object getValue(Map map, Object key) {
		Object value = map.get(key);
		return value;
	}

	// public static void main(String[] args) {
	// System.out.println(StringUtil.getErpOrderTokenValue("D45A448A7D952F1F88CCE5EBE551FE9AA6FF322A21210B0D;E40D832CAFF2C90C95685C28630EFEAC3DC8EC05B4A83EE26F5B18252B6CEE09A0CAD88C91A774E1D3197F7C5D91BDBF343FAD801CF08E4B5C651264A01520DD"));
	// }
	/**
	 * 查看机器名
	 * 
	 * @param name
	 * @return
	 */
	public static String convertMachine(String name) {
		if (StringUtils.isBlank(name)) {
			return "";
		}
		name = name.replaceAll("^.*-(.+)\\..*\\..*$", "$1");
		return name;
	}

	// 字符串的长度
	public static int getStringSize(String str) {
		return str.length();
	}

	// 截取字符串指定长度
	public static String getSubString(String str, int length) {
		return str.substring(0, length);
	}

	// 是否包含指定string
	public static boolean isContain(String str, String sub) {
		if(StringUtil.isBlank(str)){
			return false;
		}
		return str.contains(sub);
	}

	/**
	 * 获取内容中第一张图片路径
	 * 
	 * @param content
	 *            字符串，活动或者文章内容
	 * @return picIndex 第一张图片路径
	 */
	public static String getPicUrl(String content) {

		String picIndex = "";
		// 获得第一张图片的src路径
		if (isNotBlank(content)) {

			String p_str = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
			Pattern p_image = Pattern.compile(p_str);
			Matcher m_image = p_image.matcher(content);
			if (m_image.find()) {
				String image = m_image.group();
				Matcher m_src = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)")
						.matcher(image);
				if (m_src.find()) {
					picIndex = m_src.group(1);
				}
			}

		}
		return picIndex;
	}

	/**
	 * 去除html标签
	 * 
	 * @param content
	 *            字符串，活动或者文章内容
	 * @return summary 摘要信息
	 */
	public static String getSummary(String content) {
		// 获得内容中非图片部分的固定截取字符
		String summary = "";
		if (isNotBlank(content)) {
			// 替换图片
			// String p_str = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
			// 替换所有的html标签
		//	String p_str = "<[a-zA-Z][^>]*?(>|/>)|</[a-zA-Z][^>]*?>";
			String p_str = "<[^>]+>";
			summary = content.replaceAll(p_str, "").replaceAll("\\s*", "");
			int len = summary.trim().length();
		}
		return summary;
	}
 
}
