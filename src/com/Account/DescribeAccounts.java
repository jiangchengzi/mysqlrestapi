package com.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.Dispatcher.OperateSql;
import com.Dispatcher.OperateXml;
import com.Dispatcher.PackageXml;
import com.Dispatcher.connsql;
import com.database.DatabasePrivilege;
import com.kunlun.jdbc.*;
public class DescribeAccounts {
	public DescribeAccountResponse response(String DBInstanceId,String AccountName){
		DescribeAccountResponse planet=new DescribeAccountResponse();
		List<DBInstanceAccount> dbinstanceaccounts=new ArrayList<DBInstanceAccount>();
		OperateSql Opt=new OperateSql();
		Map<String,String> dbinfos=Opt.SysDb(DBInstanceId);	
		String locked="F";
		String expired="F";
		for (Map.Entry<String, String> entry :dbinfos.entrySet())
		{
				String sql = null;
				String UserId=Opt.SelectUserId(AccountName, DBInstanceId);//获取用户ID，便于权限查询用
				Map<String,String> DbPrivileges=Opt.DbPrivileges(UserId,DBInstanceId);//查询到该用户数据库：权限键值对
				sql="USE"+entry.getValue()+";select * from all_users where USER_NAME="+"'"+AccountName+"';";
				Connection conn = null;
				PreparedStatement stm = null;
				ResultSet rs = null;
				OperateXml opt=new OperateXml();
				Map<String,String> c=opt.SelectOpt(DBInstanceId);
				try{
					Class.forName(c.get("DBInstanceDRV"));
					conn = DriverManager.getConnection(c.get("DBInstanceURL"), c.get("DBInstanceUSER"), c.get("DBInstancePWD"));
					stm = conn.prepareStatement(sql);
					rs = stm.executeQuery();
					DBInstanceAccount dbinstanceaccount=new DBInstanceAccount();
					dbinstanceaccount.DatabasePrivilege=new ArrayList<DatabasePrivilege>();
					DatabasePrivilege databaseprivilege=new DatabasePrivilege();
					if(rs.next()&&rs.getString("USER_NAME")!=null){
						
						dbinstanceaccount.AccountName=rs.getString("USER_NAME");
						dbinstanceaccount.DBInstanceId=DBInstanceId;
						dbinstanceaccount.AccountDescription=" ";
						databaseprivilege.DBName=entry.getValue();
						for(Map.Entry<String, String> privileges:DbPrivileges.entrySet())
						{	
							databaseprivilege.AccountPrivilege=privileges.getValue();
							databaseprivilege.DBName=privileges.getKey();
						}
						locked=rs.getString("LOCKED");
						expired=rs.getString("EXPIRED");
						if(locked.equals("F")&&expired.equals("F")){
							dbinstanceaccount.AccountStatus="Available";
						}
						else{ dbinstanceaccount.AccountStatus="Unavailable";}
						
						dbinstanceaccount.DatabasePrivilege.add(databaseprivilege);
						dbinstanceaccounts.add(dbinstanceaccount);	
						sql=rs.getString("USER_NAME");
						
					}
			
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(rs!=null)rs.close();
						if(stm!=null) stm.close();
						if(conn!=null) conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
		}
		
		
		
		/*
		String sql="select * from all_databases;";
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		OperateXml opt=new OperateXml();
		Map<String,String> c=opt.SelectOpt(DBInstanceId);
		String locked="F";
		String expired="F";
		String db_id="1";
		int i;
		try{
			Class.forName(c.get("DBInstanceDRV"));
			conn = DriverManager.getConnection(c.get("DBInstanceURL"), c.get("DBInstanceUSER"), c.get("DBInstancePWD"));
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery(sql);

			List<DbInfo> dbinfos=new ArrayList<DbInfo>();
			while(rs.next())//获取到数据库的ID和NAME
			{
				DbInfo dbinfo =new DbInfo();
				dbinfo.DB_ID=rs.getString("DB_ID");
				dbinfo.DB_NAME=rs.getString("DB_NAME");
				dbinfos.add(dbinfo);
			}
			if(rs!=null)rs.close();
			if(stm!=null) stm.close();
			if(conn!=null) conn.close();
		String[] sqlusers=new String[dbinfos.size()];
		Class.forName(c.get("DBInstanceDRV"));
		conn = DriverManager.getConnection(c.get("DBInstanceURL"), c.get("DBInstanceUSER"), c.get("DBInstancePWD"));
		for(i=0;i<dbinfos.size();i++){//根据数据库NAME查询该数据库下用户名为AccountName的用户，并存成Arraylist
			sqlusers[i]="use "+dbinfos.get(i).DB_NAME+";"+"select * from all_users where USER_NAME="+"'"+AccountName+"';";
			stm = conn.prepareStatement(sqlusers[i]);
			rs = stm.executeQuery();
			DBInstanceAccount dbinstanceaccount=new DBInstanceAccount();
			dbinstanceaccount.DatabasePrivilege=new ArrayList<DatabasePrivilege>();
			DatabasePrivilege databaseprivilege=new DatabasePrivilege();
			if(rs.next()&&rs.getString("USER_NAME")!=null){
				
				dbinstanceaccount.AccountName=rs.getString("USER_NAME");
				dbinstanceaccount.DBInstanceId=DBInstanceId;
				dbinstanceaccount.AccountDescription=" ";
				databaseprivilege.DBName=dbinfos.get(i).DB_NAME;
				databaseprivilege.AccountPrivilege="ReadWrite";
				locked=rs.getString("LOCKED");
				expired=rs.getString("EXPIRED");
				if(locked.equals("F")&&expired.equals("F")){
					dbinstanceaccount.AccountStatus="Available";
				}
				else{ dbinstanceaccount.AccountStatus="Unavailable";}
				
				dbinstanceaccount.DatabasePrivilege.add(databaseprivilege);
				dbinstanceaccounts.add(dbinstanceaccount);	
				sql=rs.getString("USER_NAME");
				
			}
			
		}
			//planet.RequestId=sqlusers[0];
			planet.dbinstanceaccount=dbinstanceaccounts;
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)rs.close();
				if(stm!=null) stm.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		*/
		planet.RequestId="2603CA96-B17D-4903-BC04-61A2C829CD94";
		
		
		
		return planet;
		
		
	}
}

class DbInfo{
	String DB_ID;
	String DB_NAME;
}


@XmlRootElement(name="DescribeAccountsResponse")
class DescribeAccountResponse extends PackageXml{
	public String RequestId;
	@XmlElementWrapper(name="Accounts")
	@XmlElement(name="DBInstanceAccount")
	public List<DBInstanceAccount> dbinstanceaccount;
	
}

@XmlRootElement(name="DBInstanceAccount")
class DBInstanceAccount {
	
	public String AccountName;
	public String DBInstanceId;
	public String AccountStatus;
	public String AccountDescription;
	@XmlElementWrapper(name="DatabasePrivileges")
	public List<DatabasePrivilege> DatabasePrivilege;
	
}
