package contacts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 一个连接数据库的类的封装，该类借鉴了单例模式的思想。
 * 
 * 类里有两个方法可以用来执行sql语句，分别是 update(String sql,String...args) 和 query(String sql,String...args)
 * 
 * int result=JDBCUtil.update("insert into user values('1')");//数据插入示例
 * int result=JDBCUtil.update("insert into user values(?)","1");//使用参数的数据插入示例
 * 
 * ResultSet rs=JDBCUtil.query("select * from user");//数据查询示例
 * ResultSet rs=JDBCUtil.query("select * from user where id=?","1");//使用参数的数据查询示例
 * 
 * JDBCUtil.close();//使用结束后关闭该连接
 */

public class JDBCUtil {
	private static String driver="org.mariadb.jdbc.Driver";
	private static String url="jdbc:mariadb://localhost:3306/contacts?useUnicode=true&amp;characterEncoding=utf8";
	private static String username="root";
	private static String password="root";
	
	private static Connection conn=null;
	private PreparedStatement psmt=null;
	
	private JDBCUtil()
	{
		JDBCUtil.getConnection();
	}
	
	/**
	 * 供外部直接调用的静态方法，执行查询相关的操作
	 * @param sql sql语句，可以包含?
	 * @param args varargs，用于填充sql中的?
	 * @return null 数据库连接失败; ResultSet rs 查询得到的结果集;
	 */
	public static ResultSet query(String sql,String...args)
	{
		JDBCUtil.getConnection();
		ResultSet rs=null;
		if(JDBCUtil.conn==null) return rs;
		JDBCUtil jdbc=new JDBCUtil();
		jdbc.prepare(sql,args);
		try {
			rs=jdbc.psmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				jdbc.psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	
	/**
	 * 供外部直接调用的静态方法，执行更新相关的操作
	 * @param sql sql语句，可以包含?
	 * @param args varargs，用于填充sql中的?
	 * @return -1 数据库连接失败; int result 受影响的行数;
	 */
	public static int update(String sql,String...args)
	{
		JDBCUtil.getConnection();
		int result=-1;
		if(JDBCUtil.conn==null) return result;
		JDBCUtil jdbc=new JDBCUtil();
		jdbc.prepare(sql,args);
		try {
			result=jdbc.psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				jdbc.psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	private void prepare(String sql,String...args)
	{
		try {
			this.psmt=JDBCUtil.conn.prepareStatement(sql);
			for(int i=0;i<args.length;++i)
			{
				this.psmt.setString(i+1,args[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭当前使用的连接
	 */
	public static void close()
	{
		try{
			if(JDBCUtil.conn!=null)
			{
				JDBCUtil.conn.close();
				JDBCUtil.conn=null;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	

	private static synchronized void getConnection()
	{
		if(JDBCUtil.conn!=null) return;
		//JDBCUtil.loadConfig(JDBCUtil.class.getResource("/"+"db_config.xml").getFile());
		try {
			Class.forName(JDBCUtil.driver);
			JDBCUtil.conn=DriverManager.getConnection(JDBCUtil.url,JDBCUtil.username,JDBCUtil.password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
//		String[] strs={"driver",driver,"url",url,"username",username,"password",password};
//		XMLUtil xu=new XMLUtil();
//		xu.init();
//		xu.createXml("db_config.xml",strs);
	}

}
