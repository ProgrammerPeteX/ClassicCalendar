package com.classic_calendar.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Task_RecyclerViewAdapter extends RecyclerView.Adapter<Task_RecyclerViewAdapter.TaskViewHolder> {
    private OnItemClickListener onItemClickListener;
    Context context;
    List<TaskInformation> recyclerView_Information;


    public Task_RecyclerViewAdapter(Context context, List<TaskInformation> recyclerView_Information) {
        this.context = context;
        this.recyclerView_Information = recyclerView_Information;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new TaskViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.recyclerView_time_TextView.setText(String.format("%s - %s", recyclerView_Information.get(position).getStartTime(), recyclerView_Information.get(position).getEndTime()));
        holder.recyclerView_taskName_TextView.setText(String.format("%s", recyclerView_Information.get(position).getTaskName()));
        holder.recyclerView_row_ConstraintLayout.setBackgroundColor(recyclerView_Information.get(position).getTimeColourCoding());
    }

    @Override
    public int getItemCount() {
        return recyclerView_Information.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout recyclerView_row_ConstraintLayout;
        TextView recyclerView_time_TextView, recyclerView_taskName_TextView;

        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            recyclerView_time_TextView = itemView.findViewById(R.id.recyclerView_time_TextView);
            recyclerView_taskName_TextView = itemView.findViewById(R.id.recyclerView_taskName_TextView);
            recyclerView_row_ConstraintLayout = itemView.findViewById(R.id.recyclerViewRow_ConstraintLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }

    //ONCLICK LISTENER
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
}
