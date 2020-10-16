package com.wugui.datax.client.model;

public class ReturnT {

	private boolean isSuccess;
	private String msg;
	private String content;

	public ReturnT() {
	}

	public ReturnT(boolean isSuccess, String msg, String content) {
		this.isSuccess = isSuccess;
		this.msg = msg;
		this.content = content;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReturnT [isSuccess=" + isSuccess + ", msg=" + msg + ", content=" + content + "]";
	}
}
