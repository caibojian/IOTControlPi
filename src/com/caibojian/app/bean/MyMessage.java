package com.caibojian.app.bean;

import java.io.Serializable;
import java.util.Date;

public class MyMessage implements Serializable {

	private String uuid;
	private String fromId;
	private String toId;
	private String content;
	private Date date;
	private int msgType;

	public MyMessage() {
	}

	public MyMessage(String content, String fromId, String toId, Date date,
					   Integer msgType, String uuid) {
		this.content = content;
		this.fromId = fromId;
		this.toId = toId;
		this.date = date;
		this.msgType = msgType;
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public String getFromId() {
		return fromId;
	}

	public String getToId() {
		return toId;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
}
