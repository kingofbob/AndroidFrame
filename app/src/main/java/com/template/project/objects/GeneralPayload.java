package com.template.project.objects;

import java.util.List;

public class GeneralPayload {
int statusCode;
String statusMessage;
List<Object> data;
/**
 * @return the statusCode
 */
public int getStatusCode() {
	return statusCode;
}
/**
 * @param statusCode the statusCode to set
 */
public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}
/**
 * @return the statusMessage
 */
public String getStatusMessage() {
	return statusMessage;
}
/**
 * @param statusMessage the statusMessage to set
 */
public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
}
public List<Object> getData() {
	return data;
}
public void setData(List<Object> data) {
	this.data = data;
}



}
