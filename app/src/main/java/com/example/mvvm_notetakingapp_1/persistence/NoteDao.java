package com.example.mvvm_notetakingapp_1.persistence;

import com.example.mvvm_notetakingapp_1.model.Note;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    @Insert
    void insertNotes(Note notes);

    @Query("SELECT * FROM notes_table ORDER BY uid DESC")
    LiveData<List<Note>> getNotes();

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

}
