package com.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.Account.Accounts;
import com.Dispatcher.OperateSql;
import com.Dispatcher.OperateXml;
import com.Dispatcher.PackageXml;
import com.Dispatcher.connsql;

	//import java.sql.Timestamp;

	/**
	 * 通过jdbc执行SQL实例
	 * 	功能：取得数据库描述信息
	 */

public class DescribeDatabases {
			public DescribeDatabaseResponse response(String DBInstanceId,String DBName,String DBStatus){
					String sql;
					String online;
					String droped;
					String dbstatus = null;
					Map<String,String> UserPrivileges = null;
					 List<Map<String,String>> IdUserPrivileges=null;
					switch(DBStatus){
						case "Running":{online="T";droped="F";break;}
						case "Deleting":{online="F";droped="T";break;}
						case "Creating":{online="F";droped="F";break;}
						default:{online="T";droped="F";break;}
					}
					if(DBName==null)
					{
						 sql= "select db_id,db_name,char_set,online,droped,comments from all_databases where "+"online="+"'"+online+"'"+" and droped= "+"'"+droped+"'"+";";
						 OperateSql OptSql=new OperateSql();
						 Map<String,String> dbidnames=OptSql.SysDb(DBInstanceId); 
						 IdUserPrivileges=new ArrayList<Map<String,String>>();
						for(Map.Entry<String, String> dbidname:dbidnames.entrySet()){
								UserPrivileges=OptSql.UserPrivileges(dbidname.getKey(), DBInstanceId);
								//UserName=OptSql.SelectUserName(., DBInstanceId)
								IdUserPrivileges.add(UserPrivileges);
						}
					}
					else
					{
						 sql = "select db_id,db_name,char_set,online,droped,comments from all_databases where db_name="+"'"+DBName.toUpperCase()+"'"+" and online="+"'"+online+"'"+" and droped= "+"'"+droped+"'"+";";
						 OperateSql OptSql=new OperateSql();
						 String DbId=OptSql.SelectDbId(DBName, DBInstanceId);
						 UserPrivileges=OptSql.UserPrivileges(DbId, DBInstanceId);
					}
					
					//UserPrivileges.put("cdc", "1");
					OperateXml OptXml=new OperateXml();
					Map<String,String> c=OptXml.SelectOpt(DBInstanceId);
					DescribeDatabaseResponse planet=new DescribeDatabaseResponse();
					List<DataBase> databases=new ArrayList<DataBase>();
					Connection conn = null;
					PreparedStatement stm = null;
					ResultSet rs = null; 
	
					try{
						
						Class.forName(c.get("DBInstanceDRV"));
						conn = DriverManager.getConnection(c.get("DBInstanceURL"), c.get("DBInstanceUSER"), c.get("DBInstancePWD"));
						stm = conn.prepareStatement(sql);
						rs = stm.executeQuery();
						Iterator<Map<String, String>> iter=IdUserPrivileges.iterator();
						while(rs.next()){
							DataBase database=new DataBase();
							List<AccountPrivilegeInfo> accountprivilegeinfos=new ArrayList<AccountPrivilegeInfo>();
							AccountPrivilegeInfo accountprivilegeinfo=new AccountPrivilegeInfo();
							database.DBName=rs.getString("db_name");
						database.CharacterSetName=rs.getString("char_set");
						
						if(online.equals("T")&&(droped.equals("F"))){dbstatus="Running";}
						if(online.equals("F")&&(droped.equals("T"))){dbstatus="Deleting";}
						if(online.equals("F")&&(droped.equals("F"))){dbstatus="Creating";}
						
						database.Engine="KunLun";
						database.DBStatus=dbstatus;
						database.DBInstanceId=DBInstanceId;
						if(DBName!=null){
						for(Map.Entry<String, String> ap:UserPrivileges.entrySet()){
							accountprivilegeinfo.Account=ap.getKey();
							accountprivilegeinfo.AccountPrivilege=ap.getValue();
						}
						}
						else{
							for(Map.Entry<String, String> accounts:iter.next().entrySet()){
										accountprivilegeinfo.Account=accounts.getKey();
										accountprivilegeinfo.AccountPrivilege=accounts.getValue();
							}
							
							
						}
						accountprivilegeinfos.add(accountprivilegeinfo);
						database.AccountPrivilegeInfo=accountprivilegeinfos;
						databases.add(database);
						
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
						
					
				planet.setDescribeDatabaseResponse("2603CA96-B17D-4903-BC04-61A2C829CD94", databases);
				
				return planet;

}
}

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="DescribeDatabasesResponse")
class DescribeDatabaseResponse extends PackageXml{
	@XmlElement
	public String RequestId;
	
	@XmlElementWrapper(name="Databases")
	@XmlElement(name="Database")
	public List<DataBase> database;
	
	public void setDescribeDatabaseResponse(String RequestId,List<DataBase> database){
		//super();
		this.database=database;
		this.RequestId=RequestId;
	}
}




