package com.example.mvvm_notetakingapp_1.async;

import android.os.AsyncTask;
import com.example.mvvm_notetakingapp_1.model.Note;
import com.example.mvvm_notetakingapp_1.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note,Void,Void> {

    private NoteDao noteDao;

    public InsertAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        noteDao.insertNotes(notes[0]);
        return null;
    }
}
