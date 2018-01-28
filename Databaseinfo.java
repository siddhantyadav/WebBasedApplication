package edu.uic.ids.database;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class Databaseinfo {

	private String userName = "f16gxxx";
	private String password = "f16gxxxR02S";

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	private String host="131.193.209.57";
	private String error;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setErrorMessage(String error) {
		this.error = error;
	}
	
	private String dbSchema = "world";
	private String dbms = "MySQL";
	
	
	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getDbSchema(){
		return dbSchema;
	}

	public String getDbms() {
		return dbms;
	}
	
	
	
	
}
