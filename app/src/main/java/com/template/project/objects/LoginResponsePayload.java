package com.template.project.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by 00020443 on 19/10/2016.
 */

public class LoginResponsePayload {
    int statusCode;
    String statusMessage;
    String sessionKey;
    LoginObj loginObj;
    UserDetailsObj userDetailsObj;

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

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public LoginObj getLoginObj() {
        return loginObj;
    }

    public void setLoginObj(LoginObj loginObj) {
        this.loginObj = loginObj;
    }

    public UserDetailsObj getUserDetailsObj() {
        return userDetailsObj;
    }

    public void setUserDetailsObj(UserDetailsObj userDetailsObj) {
        this.userDetailsObj = userDetailsObj;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
