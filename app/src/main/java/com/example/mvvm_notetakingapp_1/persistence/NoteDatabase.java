package com.example.mvvm_notetakingapp_1.persistence;

import android.content.Context;
import com.example.mvvm_notetakingapp_1.async.PopulateDbAsyncTask;
import com.example.mvvm_notetakingapp_1.model.Note;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASENAME = "notes_db.db";

    private static NoteDatabase instance = null;

    public synchronized static NoteDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,DATABASENAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    public abstract NoteDao noteDao();

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
}
