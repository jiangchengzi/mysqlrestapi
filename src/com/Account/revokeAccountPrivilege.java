package com.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.Dispatcher.OperateSql;
import com.Dispatcher.OperateXml;
import com.Dispatcher.PackageXml;
import com.Dispatcher.connsql;
public class revokeAccountPrivilege {
	public revokeAccountPrivilegeResponse response(String DBInstanceId,String AccountName,String DBName){
		revokeAccountPrivilegeResponse planet=new revokeAccountPrivilegeResponse();
		OperateSql Opt=new OperateSql();
		Map<String,String> dbinfos=Opt.SysDb(DBInstanceId);	
		for (Map.Entry<String, String> entry :dbinfos.entrySet())
		{
			String sql = null;
			String UserId=Opt.SelectUserId(AccountName, DBInstanceId);
			String UserPrivilege=Opt.SelectUserPrivilege(entry.getValue(),UserId, DBInstanceId);
			if(UserPrivilege.equals("ReadWrite")){
			sql="USE"+entry.getValue()+";REVOKE DBA FROM "+AccountName+";";
			}
			else{
			sql="USE"+entry.getValue()+";REVOKE SELECT ANY TABLE FROM "+AccountName+";";	
			}
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
		planet.RequestId="1E43AAE0-BEE8-43DA-860D-EAF2AA0724DC";
		return planet;
		
		
	}

}
@XmlRootElement(name="RevokeAccountPrivilegeResponse")
class revokeAccountPrivilegeResponse extends PackageXml{

	public String RequestId;

	
}

