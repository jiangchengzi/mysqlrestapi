package com.Dispatcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


//根据实例名称确定实例对应的IP：端口
public class connsql {
	public String drv;
	public String url ;
	public String user;
	public String pwd ;
	public String path;
	public String IP;
	public String port;
	public String name;
	public String DbInstanceId;
	public connsql(){
		this.IP="10.11.1.81";
		this.name="pdb";
		this.port="6688";
		this.DbInstanceId="kunlun";
		this.drv = "com.kunlun.jdbc.Driver";
		this.url = "jdbc:kunlun://"+this.IP+":"+this.port+"/system";
		this.user = "sysdba";
		this.pwd = "SYSDBA";
		this.path="/opt/pdb/server/pdb-ls3ab";
	}
}
