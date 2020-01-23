package com.example.schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule.model.Meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleMeetingActivity extends AppCompatActivity implements IMeeting.MainPresenter {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Date startTime, endTime;
    private MeetingPresenter meetingPresenter;

    @BindView(R.id.prev)
    TextView prev;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.date)
    TextView dateBox;
    @BindView(R.id.start_time)
    TextView startTimeBox;
    @BindView(R.id.end_time)
    TextView endTimeBox;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_meeting);
        ButterKnife.bind(this);
        next.setVisibility(View.GONE);
        prev.setText("BACK");
    }

    @OnClick(R.id.prev)
    public void backPress() {
        super.onBackPressed();
    }

    @OnClick(R.id.date)
    public void dateClick() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateBox.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.start_time)
    public void startTimeClick() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    startTimeBox.setText(hourOfDay + ":" + minute);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 1, true);
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.end_time)
    public void endTimeClick() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    endTimeBox.setText(hourOfDay + ":" + minute);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + 1, true);
        timePickerDialog.show();
    }

    @OnClick(R.id.submit_btn)
    public void submitClick() {
        String dateValue = dateBox.getText().toString().trim();
        String startTimeValue = startTimeBox.getText().toString().trim();
        String endTimeValue = endTimeBox.getText().toString().trim();

        if (dateValue.equalsIgnoreCase("Meeting Date") || dateValue.length() == 0 ||
                startTimeValue.equalsIgnoreCase("Start Time") || startTimeValue.length() == 0 ||
                endTimeValue.equalsIgnoreCase("End Time") || endTimeValue.length() == 0) {
            Toast.makeText(this, "Filling date, start time and end time is compulsory", Toast.LENGTH_LONG).show();
            return;
        }

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            startTime = sdf.parse(startTimeBox.getText().toString());
            endTime = sdf.parse(endTimeBox.getText().toString());

            if (endTime.before(startTime)) {
                Toast.makeText(this, "End time must be after start time", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        meetingPresenter = new MeetingPresenter(this, this);
        meetingPresenter.getMeetings(this, dateValue);
    }

    @Override
    public void dataFromApi(List<Meeting> meetings) {
        for (int i = 0; i < meetings.size(); i++) {
            String pattern = "HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                Date date1 = sdf.parse(meetings.get(i).startTime);
                Date date2 = sdf.parse(meetings.get(i).endTime);
                if (!((startTime.before(date1) && endTime.before(date2)) || (startTime.after(date1) && endTime.after(date2)))) {
                    Toast.makeText(this, "Slot Not Available", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "Slot Available", Toast.LENGTH_SHORT).show();
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
}
