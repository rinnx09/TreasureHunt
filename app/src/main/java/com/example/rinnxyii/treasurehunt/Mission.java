package com.example.rinnxyii.treasurehunt;

public class Mission {
    private String missionIndex;
    private String missionType;
    private String missionValue;
    private String eventMission;
    private String ID;


    public Mission(String missionIndex, String missionType, String missionValue, String eventMission) {
        this.missionIndex = missionIndex;
        this.missionType = missionType;
        this.missionValue = missionValue;
        this.eventMission = eventMission;
        this.ID="";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMissionIndex() {
        return missionIndex;
    }

    public void setMissionIndex(String missionIndex) {
        this.missionIndex = missionIndex;
    }

    public String getEventMission() {
        return eventMission;
    }

    public void setEventMission(String eventMission) {
        this.eventMission = eventMission;
    }

    public String getMissionValue() {
        return missionValue;
    }

    public void setMissionValue(String missionValue) {
        this.missionValue = missionValue;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }
}
