package com.example.mvvm_notetakingapp_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.example.mvvm_notetakingapp_1.adapter.NoteRecyclerAdapter;
import com.example.mvvm_notetakingapp_1.model.Note;
import com.example.mvvm_notetakingapp_1.utils.VerticalSpacingItemDecorator;
import com.example.mvvm_notetakingapp_1.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteRecyclerAdapter.OnNoteClickListener {

    private static final String COMMON_TAG = "mNoteLog";
    private static final String TAG = "MainActivity";
    private static final int ADD_NOTE_REQUEST = 101;
    private static final int EDIT_NOTE_REQUEST = 102;

    private NoteViewModel mNoteViewModel;

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private NoteRecyclerAdapter mNoteAdapter;
    private volatile int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        mFab = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.recyclerView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Simple Note Taking");
        mFab.setOnClickListener(this);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.d(COMMON_TAG, TAG + " onChanged");
                mNoteAdapter.submitList(notes);
            }
        });
        setRecyclerView();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Are you sure?").setMessage("This action can't be undone").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mNoteViewModel.delete(mNoteAdapter.getNoteAt(position));
                        Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();
                mNoteAdapter.notifyItemChanged(position);
            }
        }).attachToRecyclerView(mRecyclerView);
        hideSoftKeyboard();
    }

    private void setRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setHasFixedSize(true);
        mNoteAdapter = new NoteRecyclerAdapter();
        mNoteAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mNoteAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(NoteActivity.EXTRA_TITLE);
            String content = data.getStringExtra(NoteActivity.EXTRA_CONTENT);
            String timestamp = data.getStringExtra(NoteActivity.EXTRA_TIMESTAMP);
            Note note = new Note(title, content, timestamp);
            mNoteViewModel.insert(note);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(NoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(NoteActivity.EXTRA_TITLE);
            String content = data.getStringExtra(NoteActivity.EXTRA_CONTENT);
            String timestamp = data.getStringExtra(NoteActivity.EXTRA_TIMESTAMP);
            Note note = new Note(title, content, timestamp);
            note.setId(id);
            mNoteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra(NoteActivity.EXTRA_ID, note.getId());
        intent.putExtra(NoteActivity.EXTRA_TITLE, note.getTitle());
        intent.putExtra(NoteActivity.EXTRA_CONTENT, note.getContent());
        intent.putExtra(NoteActivity.EXTRA_TIMESTAMP, note.getTimestamp());
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Are you sure?")
                        .setMessage("This action can't be undone")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mNoteViewModel.deleteAllnotes();
                                Toast.makeText(MainActivity.this, "All notes deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
