package xiaobai.jdbc;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0Pool {
	private static ComboPooledDataSource ds = null;
	static {
		try {
			ds = new ComboPooledDataSource();
			// 设置JDBC的Driver类
			ds.setDriverClass("com.mysql.jdbc.Driver");
			// 类根据配置文件读取
			// 设置JDBC的URL
			// ds.setJdbcUrl("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true");
//			ds.setJdbcUrl("jdbc:mysql://192.168.1.113:3306/channel?rewriteBatchedStatements=true&characterEncoding=UTF-8");
//			ds.setJdbcUrl("jdbc:mysql://192.168.1.113:3306/xiaobai_sns?rewriteBatchedStatements=true&characterEncoding=UTF-8");
			ds.setJdbcUrl("jdbc:mysql://115.28.203.21:3306/xiaobai_sns?characterEncoding=UTF-8");
			// 设置数据库的登录用户名
			ds.setUser("root");
			// 设置数据库的登录用户密码
//			ds.setPassword("data123456");
			ds.setPassword("Ovrn123456");
			// ds.setPassword("123456");
			// 设置连接池的最大连接数
			ds.setMaxPoolSize(300);
			ds.setAcquireIncrement(50);
			ds.setMinPoolSize(20);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return con;
	}
}