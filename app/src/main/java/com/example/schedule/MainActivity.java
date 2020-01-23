package com.example.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.model.Meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMeeting.MainPresenter {

    @BindView(R.id.rv_meeting)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_date)
    TextView toolbarDate;
    @BindView(R.id.prev)
    TextView prev;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.schedule_btn)
    TextView scheduleBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private List<Meeting> meetingList = new ArrayList<>();
    private MeetingPresenter meetingPresenter;
    private MeetingAdapter meetingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String date = toDateOnlyString(getCurrentDate());
        toolbarDate.setText(date);
        scheduleBtn.setEnabled(true);
        scheduleBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_title_text));
        setupRecycler();
        setupAdapter();
        meetingPresenter = new MeetingPresenter(MainActivity.this, this);
        meetingPresenter.getMeetings(this, date);
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setFitsSystemWindows(true);
    }

    private void setupAdapter() {
        meetingAdapter = new MeetingAdapter(this, meetingList);
        mRecyclerView.setAdapter(meetingAdapter);
    }

    @Override
    public void dataFromApi(List<Meeting> meetings) {
        meetingAdapter.setData(meetings);
    }

    @Override
    public void showNetworkError() {
        hideProgressBar();
        Toast.makeText(this, "Check Network Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void hideProgressBar() {
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressBar() {
        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        }
    }

    @OnClick(R.id.prev)
    public void prevClick() {
        try {
            Calendar cal = Calendar.getInstance();
            String dateFromToolbar = toolbarDate.getText().toString();
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateFromToolbar);
            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            String newDate = toDateOnlyString(cal.getTime());
            toolbarDate.setText(newDate);
            Date currentDate = getCurrentDate();
            Date prevDate = cal.getTime();
            String prevDateString = toDateOnlyString(cal.getTime());
            String currentDateString = toDateOnlyString(getCurrentDate());
            if (prevDate.after(currentDate) || prevDateString.equalsIgnoreCase(currentDateString)) {
                scheduleBtn.setEnabled(true);
                scheduleBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_title_text));
            } else {
                scheduleBtn.setEnabled(false);
                scheduleBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.disabled_grey));
            }
            meetingPresenter.getMeetings(this, newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.next)
    public void nextClick() {
        try {
            Calendar cal = Calendar.getInstance();
            String dateFromToolbar = toolbarDate.getText().toString();
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateFromToolbar);
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            String newDate = toDateOnlyString(cal.getTime());
            toolbarDate.setText(newDate);
            Date currentDate = getCurrentDate();
            Date nextDate = cal.getTime();
            String nextDateString = toDateOnlyString(cal.getTime());
            String currentDateString = toDateOnlyString(getCurrentDate());
            if (nextDate.after(currentDate) || nextDateString.equalsIgnoreCase(currentDateString)) {
                scheduleBtn.setEnabled(true);
                scheduleBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar_title_text));
            } else {
                scheduleBtn.setEnabled(false);
                scheduleBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.disabled_grey));
            }
            meetingPresenter.getMeetings(this, newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.schedule_btn)
    public void scheduleClick() {
        Intent intent = new Intent(this, ScheduleMeetingActivity.class);
        startActivity(intent);
    }

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String toDateOnlyString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return dateFormat.format(date);
    }
}