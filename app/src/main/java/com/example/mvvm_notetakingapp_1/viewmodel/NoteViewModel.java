package com.example.mvvm_notetakingapp_1.viewmodel;

import android.app.Application;

import com.example.mvvm_notetakingapp_1.model.Note;
import com.example.mvvm_notetakingapp_1.persistence.NoteRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mNoteRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        allNotes = mNoteRepository.getNotes();
    }

    public void insert(Note note){
        mNoteRepository.insertNotes(note);
    }

    public void update(Note note){
        mNoteRepository.update(note);
    }
    public void delete(Note note){
        mNoteRepository.delete(note);
    }

    public void deleteAllnotes(){
        mNoteRepository.deleteAllNotes();
    }
    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}
