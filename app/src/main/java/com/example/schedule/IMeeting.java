package com.example.schedule;

import com.example.schedule.model.Meeting;

import java.util.List;

public interface IMeeting {

    interface MainView {
        void mainValidateError();

        void showProgressBar();

        void hideProgressBar();

        //void mainSuccess(Response<MeetingsResponse> meetings);

        void mainError(Throwable throwable);

        boolean checkInternet();
    }

    interface MainPresenter {
        void dataFromApi(List<Meeting> meetings);

        void showNetworkError();

        void onStop();

        void showProgressBar();

        void hideProgressBar();
    }

}
