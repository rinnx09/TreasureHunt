package com.example.rinnxyii.treasurehunt;

public class Events {
    private String event_name;
    private String event_date;
    private String event_description;
    private String event_location;
    private String event_time;
    private String handler_contactNo;
    private String handler_email;
    private String handler_name;

    public Events() {
    }

    public Events(String event_name, String event_date, String event_description, String event_location, String event_time, String handler_contactNo, String handler_email, String handler_name) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_description = event_description;
        this.event_location = event_location;
        this.event_time = event_time;
        this.handler_contactNo = handler_contactNo;
        this.handler_email = handler_email;
        this.handler_name = handler_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getHandler_contactNo() {
        return handler_contactNo;
    }

    public void setHandler_contactNo(String handler_contactNo) {
        this.handler_contactNo = handler_contactNo;
    }

    public String getHandler_email() {
        return handler_email;
    }

    public void setHandler_email(String handler_email) {
        this.handler_email = handler_email;
    }

    public String getHandler_name() {
        return handler_name;
    }

    public void setHandler_name(String handler_name) {
        this.handler_name = handler_name;
    }






}
