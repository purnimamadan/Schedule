package com.example.schedule.network;

import com.example.schedule.model.Meeting;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIEndPoint {

    @GET("api/schedule")
    Observable<List<Meeting>> getBookedMeetings(@Query("date") String date);
}
