package com.example.mvvm_notetakingapp_1.async;

import android.os.AsyncTask;
import com.example.mvvm_notetakingapp_1.persistence.NoteDao;

public class DeleteAllNotesAsyncTask extends AsyncTask<Void,Void,Void> {

    private NoteDao noteDao;

    public DeleteAllNotesAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Void... notes) {
        noteDao.deleteAllNotes();
        return null;
    }
}
