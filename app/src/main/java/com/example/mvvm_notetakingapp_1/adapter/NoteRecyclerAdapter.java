package com.example.mvvm_notetakingapp_1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mvvm_notetakingapp_1.R;
import com.example.mvvm_notetakingapp_1.model.Note;
import com.example.mvvm_notetakingapp_1.utils.UtilityDate;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NoteRecyclerAdapter extends ListAdapter<Note,NoteRecyclerAdapter.ViewHolder> {

    private OnNoteClickListener listener;

    public NoteRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getContent().equals(newItem.getContent())
                    && oldItem.getTimestamp().equals(newItem.getTimestamp());
        }
    };


    public Note getNoteAt(int position){
        return getItem(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = getItem(position);

        String month = note.getTimestamp().substring(0,2);
        month = UtilityDate.getMonthFromNumber(month);
        String year = note.getTimestamp().substring(3);
        String timefullText = month +" "+year;

        String title = note.getTitle();
        holder.title_tv.setText(title);
        holder.timestamp_tv.setText(timefullText);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title_tv,timestamp_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.note_title_textView);
            timestamp_tv = itemView.findViewById(R.id.note_timestamp_textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null && position != RecyclerView.NO_POSITION) {
                        listener.onNoteClick(getItem(position));
                    }
                }
            });
        }
    }
    public interface OnNoteClickListener{
        void onNoteClick(Note note);
    }
    public void setOnItemClickListener(OnNoteClickListener listener){
        this.listener = listener;
    }
}
