package com.example.schedule.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meeting {
    @SerializedName("start_time")
    public String startTime;
    @SerializedName("end_time")
    public String endTime;
    @SerializedName("description")
    public String description;
    @SerializedName("participants")
    public List<String> participants = null;
}
