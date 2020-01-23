package com.example.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.model.Meeting;

import java.util.List;

/**
 * The type User list adapter.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {
    private final List<Meeting> meetings;
    private final Context appContext;

    /**
     * Instantiates a new User list adapter.
     *
     * @param appContext the app context
     * @param arraylist  the arraylist
     */
    public MeetingAdapter(Context appContext, List<Meeting> arraylist) {
        this.meetings = arraylist;
        this.appContext = appContext;
    }

    @Override
    public MeetingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MeetingAdapter.ViewHolder holder, final int position) {
        Meeting meeting = meetings.get(position);
        if (meeting != null) {
            holder.description.setText(meeting.description);
            holder.time.setText(meeting.startTime + " - " + meeting.endTime);
        }
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }


    public void setData(List<Meeting> meeting) {
        meetings.clear();
        meetings.addAll(meeting);
        notifyDataSetChanged();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description, time;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.desc);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}