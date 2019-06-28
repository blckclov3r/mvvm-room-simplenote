package com.example.mvvm_notetakingapp_1.persistence;

import android.content.Context;
import com.example.mvvm_notetakingapp_1.async.DeleteAllNotesAsyncTask;
import com.example.mvvm_notetakingapp_1.async.DeleteAsyncTask;
import com.example.mvvm_notetakingapp_1.async.InsertAsyncTask;
import com.example.mvvm_notetakingapp_1.async.UpdateAsyncTask;
import com.example.mvvm_notetakingapp_1.model.Note;
import java.util.List;
import androidx.lifecycle.LiveData;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context){
        mNoteDatabase = NoteDatabase.getInstance(context);
        noteDao = mNoteDatabase.noteDao();
        allNotes = noteDao.getNotes();
    }

    public void insertNotes(Note notes){
        new InsertAsyncTask(noteDao).execute(notes);
    }

    public LiveData<List<Note>> getNotes(){
        return allNotes;
    }



    public void delete(Note notes){
        new DeleteAsyncTask(noteDao).execute(notes);
    }

    public void update(Note notes){
        new UpdateAsyncTask(noteDao).execute(notes);
    }

    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

}
