package com.caibojian.entity;

import java.util.List;

public class SyncTask {
	private Integer id;
	private String name;
	private String description;
	private String dbName;
	private String dbVersion;
	private String dbIP;
	private String dbPort;
	private String dbUsername;
	private String dbPassword;
	private String dbSid;
	private Integer runFlag;
	private Integer status;
	private String logDelPattern;
	private String triggerPattern;
	private Integer initMirror;	
	private boolean runTemp = false;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbVersion() {
		return dbVersion;
	}
	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}
	public String getDbIP() {
		return dbIP;
	}
	public void setDbIP(String dbIP) {
		this.dbIP = dbIP;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbUsername() {
		return dbUsername;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getDbSid() {
		return dbSid;
	}
	public void setDbSid(String dbSid) {
		this.dbSid = dbSid;
	}
	public Integer getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(Integer runFlag) {
		this.runFlag = runFlag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getLogDelPattern() {
		return logDelPattern;
	}
	public void setLogDelPattern(String logDelPattern) {
		this.logDelPattern = logDelPattern;
	}
	public String getTriggerPattern() {
		return triggerPattern;
	}
	public void setTriggerPattern(String triggerPattern) {
		this.triggerPattern = triggerPattern;
	}
	public Integer getInitMirror() {
		return initMirror;
	}
	public void setInitMirror(Integer initMirror) {
		this.initMirror = initMirror;
	}
	public boolean isRunTemp() {
		return runTemp;
	}
	public void setRunTemp(boolean runTemp) {
		this.runTemp = runTemp;
	}
	@Override
	public String toString() {
		return "SyncTask [id=" + id + ", name=" + name + ", description=" + description + ", dbName=" + dbName
				+ ", dbVersion=" + dbVersion + ", dbIP=" + dbIP + ", dbPort=" + dbPort + ", dbUsername=" + dbUsername
				+ ", dbPassword=" + dbPassword + ", dbSid=" + dbSid + ", runFlag=" + runFlag + ", status=" + status
				+ ", logDelPattern=" + logDelPattern + ", triggerPattern=" + triggerPattern + ", initMirror="
				+ initMirror + ", runTemp=" + runTemp +  "]";
	}		
	
	
}
