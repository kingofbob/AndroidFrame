package com.template.project.objects;

public class UserPayload {
	int statusCode;
	String statusMessage;
	t_user data;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public t_user getData() {
		return data;
	}
	public void setData(t_user data) {
		this.data = data;
	}
	
}
