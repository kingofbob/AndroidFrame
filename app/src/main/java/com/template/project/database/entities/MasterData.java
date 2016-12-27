package com.template.project.database.entities;

import java.util.List;

/**
 * Created by 00020443 on 17/11/2016.
 */

public class MasterData {
    List<CMS> cMSDatas;
    List<GeneralMasterData> generalMasterData;
    List<EventMasterData> eventMasterDatas;
    List<OnlineApplicationMasterData> onlineApplicationMasterDatas;
    List<RoomMasterData> roomMasterDatas;
    List<RoomHierarchyLevel> roomHierarchyLevelDatas;
    List<SystemConfig> systemConfigDatas;
    List<UserCourseHierarchyLevel> userCourseHierarchyLevelDatas;

    public List<CMS> getcMSDatas() {
        return cMSDatas;
    }

    public void setcMSDatas(List<CMS> cMSDatas) {
        this.cMSDatas = cMSDatas;
    }

    public List<GeneralMasterData> getGeneralMasterData() {
        return generalMasterData;
    }

    public void setGeneralMasterData(List<GeneralMasterData> generalMasterData) {
        this.generalMasterData = generalMasterData;
    }

    public List<EventMasterData> getEventMasterDatas() {
        return eventMasterDatas;
    }

    public void setEventMasterDatas(List<EventMasterData> eventMasterDatas) {
        this.eventMasterDatas = eventMasterDatas;
    }

    public List<OnlineApplicationMasterData> getOnlineApplicationMasterDatas() {
        return onlineApplicationMasterDatas;
    }

    public void setOnlineApplicationMasterDatas(List<OnlineApplicationMasterData> onlineApplicationMasterDatas) {
        this.onlineApplicationMasterDatas = onlineApplicationMasterDatas;
    }

    public List<RoomMasterData> getRoomMasterDatas() {
        return roomMasterDatas;
    }

    public void setRoomMasterDatas(List<RoomMasterData> roomMasterDatas) {
        this.roomMasterDatas = roomMasterDatas;
    }

    public List<RoomHierarchyLevel> getRoomHierarchyLevelDatas() {
        return roomHierarchyLevelDatas;
    }

    public void setRoomHierarchyLevelDatas(List<RoomHierarchyLevel> roomHierarchyLevelDatas) {
        this.roomHierarchyLevelDatas = roomHierarchyLevelDatas;
    }

    public List<SystemConfig> getSystemConfigDatas() {
        return systemConfigDatas;
    }

    public void setSystemConfigDatas(List<SystemConfig> systemConfigDatas) {
        this.systemConfigDatas = systemConfigDatas;
    }

    public List<UserCourseHierarchyLevel> getUserCourseHierarchyLevelDatas() {
        return userCourseHierarchyLevelDatas;
    }

    public void setUserCourseHierarchyLevelDatas(List<UserCourseHierarchyLevel> userCourseHierarchyLevelDatas) {
        this.userCourseHierarchyLevelDatas = userCourseHierarchyLevelDatas;
    }
}
