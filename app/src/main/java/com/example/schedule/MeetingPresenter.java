package com.example.schedule;

import android.content.Context;

import com.example.schedule.model.Meeting;
import com.example.schedule.network.APIEndPoint;
import com.example.schedule.network.CheckNetworkConnection;
import com.example.schedule.network.RetrofitSetUp;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MeetingPresenter {

    private APIEndPoint mApiEndPoint;
    private IMeeting.MainPresenter mMeetingView;

    MeetingPresenter(Context context, IMeeting.MainPresenter iMeeting) {
        this.mMeetingView = iMeeting;
        mApiEndPoint = RetrofitSetUp.getApiEndPoint();
    }

    public void getMeetings(Context context, String date) {

        if (!CheckNetworkConnection.isConnected(context)) {
            mMeetingView.showNetworkError();
            return;
        }

        mMeetingView.showProgressBar();
        mApiEndPoint.getBookedMeetings(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Meeting>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMeetingView.hideProgressBar();
                    }

                    @Override
                    public void onNext(List<Meeting> meetings) {
                        mMeetingView.hideProgressBar();
                        mMeetingView.dataFromApi(meetings);
                    }
                });
    }
}
