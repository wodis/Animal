package com.openwudi.animal.model;

/**
 * Created by diwu on 17/7/14.
 */

public class Account {
    private String UserId;
    private String UserCode;
    private String UserName;
    private String AreaId;
    private String MonitorStationId;
    private int RoleType;
    private String LoginToken;
    private String TerminalId;
    private String UseObjectId;
    private long ExpTime;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getMonitorStationId() {
        return MonitorStationId;
    }

    public void setMonitorStationId(String monitorStationId) {
        MonitorStationId = monitorStationId;
    }

    public int getRoleType() {
        return RoleType;
    }

    public void setRoleType(int roleType) {
        RoleType = roleType;
    }

    public String getLoginToken() {
        return LoginToken;
    }

    public void setLoginToken(String loginToken) {
        LoginToken = loginToken;
    }

    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String terminalId) {
        TerminalId = terminalId;
    }

    public String getUseObjectId() {
        return UseObjectId;
    }

    public void setUseObjectId(String useObjectId) {
        UseObjectId = useObjectId;
    }

    public long getExpTime() {
        return ExpTime;
    }

    public void setExpTime(long expTime) {
        ExpTime = expTime;
    }
}
