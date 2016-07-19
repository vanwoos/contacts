package contacts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Contacts {
	
	/**
	 * 向数据库中添加数据
	 * @param name
	 * @param phone
	 * @return 0 成功; -1 数据库失败; -2 参数不合法;
	 */
	public int add(String name,String phone)
	{
		if(name==null|| phone==null)
			return -2;
		int result=JDBCUtil.update("insert into contacts(name,phone) values(?,?)",name,phone);
		return result==1?0:-1;
	}
	
	/**
	 * 判断某个姓名是否已经存在
	 * @param name
	 * @return 0 不存在; 1 存在; -1 数据库失败; -2 参数不合法;
	 * @throws SQLException 
	 */
	public int exists(String name)
	{
		if(name==null) return -2;
		ResultSet rs=JDBCUtil.query("select * from contacts where name=?",name);
		if(rs==null) return -1;
		try {
			if(rs.next())
			{
				return 1;
			}
			else{
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String[] args)
	{
		Scanner sc=new Scanner(System.in);
		Contacts ct=new Contacts();
		while(true)
		{
			System.out.print("输入姓名：");
			String name=sc.nextLine();
			
			if(ct.exists(name)!=0)
			{
				System.out.println("姓名已经存在\n");
				continue;
			}
			String phone=null;
			do{
				System.out.print("输入手机号：");
				phone=sc.nextLine();
			}while(phone.length()!=11 && phone.length()!=6 && phone.length()!=7);
			
			
			
			int result=ct.add(name,phone);
			if(result==0)
			{
				System.out.println("添加成功\n");
			}
			else{
				System.out.println("添加失败\n");
			}
		}
	}
}
