package com.navigation.edgescreen.data;

import java.util.ArrayList;

public class EventPlanner {
    private String calendar_id;
    private String nameEvent;
    private long startDate;
    private long endDate;
    private String description;

    public EventPlanner() {
    }

    public EventPlanner(String calendar_id, String nameEvent, long startDate, long endDate, String description) {
        this.nameEvent = nameEvent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.calendar_id = calendar_id;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
