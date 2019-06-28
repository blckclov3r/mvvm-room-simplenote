package com.example.mvvm_notetakingapp_1.async;

import android.os.AsyncTask;
import android.util.Log;
import com.example.mvvm_notetakingapp_1.model.Note;
import com.example.mvvm_notetakingapp_1.persistence.NoteDao;
import com.example.mvvm_notetakingapp_1.persistence.NoteDatabase;

public class PopulateDbAsyncTask extends AsyncTask<Note,Void,Void> {
    private static final String TAG = "PopulateDbAsyncTask";
    public static final String COMMON_TAG = "mNoteLog";
    private NoteDao noteDao;

    public PopulateDbAsyncTask(NoteDatabase db){
        noteDao = db.noteDao();
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(COMMON_TAG,TAG+" doInBackground: "+Thread.currentThread().getName());
        noteDao.insertNotes(new Note("Title1","Content1","TimeStamp1"));
        noteDao.insertNotes(new Note("Title2","Content2","TimeStamp2"));
        noteDao.insertNotes(new Note("Title3","Content3","TimeStamp3"));
        noteDao.insertNotes(new Note("Title4","Content4","TimeStamp4"));
        noteDao.insertNotes(new Note("Title5","Content5","TimeStamp5"));
        noteDao.insertNotes(new Note("Title6","Content6","TimeStamp6"));
        noteDao.insertNotes(new Note("Title7","Content7","TimeStamp7"));
        return null;
    }
}
