package com.example.mvvm_notetakingapp_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.mvvm_notetakingapp_1.model.Note;
import com.example.mvvm_notetakingapp_1.utils.UtilityDate;
import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity implements
        View.OnClickListener, View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String COMMON_TAG = "mNoteLog";
    private static final String TAG = "NoteActivity";
    public static final String EXTRA_ID = "com.example.mvvm_notetakingapp.activity.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.mvvm_notetakingapp.activity.EXTRA_TITLE";
    public static final String EXTRA_CONTENT = "com.example.mvvm_notetakingapp.activity.EXTRA_CONTENT";
    public static final String EXTRA_TIMESTAMP = "com.example.mvvm_notetakingapp.activity.EXTRA_TIMESTAMP";

    //vars
    private boolean editUpdate_notes = false;
    private GestureDetector mGestureDetector;
    private Note initialNote;

    //components
    private RelativeLayout mBackArrowContainer, mCheckContainer;
    private ImageButton mCheck, mBackArrow;

    //toolbar componenets
    private TextView mNote_title_textView;
    private EditText mNote_title_editText;
    //input text
    private EditText mNote_text_editText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mNote_text_editText = findViewById(R.id.note_text_editText);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheckContainer = findViewById(R.id.check_container);
        mCheck = findViewById(R.id.check_imageButton);
        mBackArrow = findViewById(R.id.back_imageButton);
        mNote_title_textView = findViewById(R.id.note_title_textView);
        mNote_title_editText = findViewById(R.id.note_title_editText);

        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mNote_title_textView.setOnClickListener(this);
        //ontouch listener
        mNote_text_editText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        initialNote = new Note();
        if (getIntent().hasExtra(EXTRA_ID)) {

            String title = getIntent().getStringExtra(EXTRA_TITLE);
            String content = getIntent().getStringExtra(EXTRA_CONTENT);
            String timestamp = getIntent().getStringExtra(EXTRA_TIMESTAMP);
            mNote_title_textView.setText(title);
            mNote_title_editText.setText(title);
            mNote_text_editText.setText(content);

            initialNote = new Note(title, content, timestamp);
            disableEditMode();

        } else {
            setNewNoteProperties();
            enableEditMode();
        }

    }

    private void setNewNoteProperties() {
        mNote_title_textView.setText(getString(R.string.note_title));
        mNote_title_editText.setText(getString(R.string.note_title));
    }


    private void enableEditMode() {
        editUpdate_notes = false;
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);
        mNote_title_textView.setVisibility(View.GONE);
        mNote_title_editText.setVisibility(View.VISIBLE);
        enableContentInteraction();
    }

    private void disableEditMode() {
        editUpdate_notes = true;
        mNote_title_textView.setText(mNote_title_editText.getText().toString());
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);
        mNote_title_textView.setVisibility(View.VISIBLE);
        mNote_title_editText.setVisibility(View.GONE);
        disableContentInteraction();
    }


    private void disableContentInteraction() {
        //content text field
        mNote_text_editText.setKeyListener(null);
        mNote_text_editText.setFocusable(false);
        mNote_text_editText.setFocusableInTouchMode(false);
        mNote_text_editText.setCursorVisible(false);
        mNote_text_editText.clearFocus();

    }

    private void enableContentInteraction() {
        mNote_text_editText.setKeyListener(new EditText(this).getKeyListener());
        mNote_text_editText.setFocusable(true);
        mNote_text_editText.setFocusableInTouchMode(true);
        mNote_text_editText.setCursorVisible(true);
        mNote_text_editText.requestFocus();
    }

    private void setClickViewTitle() {
        showHideKeyboard();
        editUpdate_notes = false;
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);
        mNote_title_textView.setVisibility(View.GONE);
        mNote_title_editText.setVisibility(View.VISIBLE);
        mNote_title_editText.setKeyListener(new EditText(this).getKeyListener());
        mNote_title_editText.setFocusable(true);
        mNote_title_editText.setFocusableInTouchMode(true);
        mNote_title_editText.setCursorVisible(true);
        mNote_title_editText.requestFocus();
        mNote_title_textView.setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_imageButton:
                saveNewNote();
                finish();
                break;
            case R.id.check_imageButton:
                disableEditMode();
                saveNewNote();
                break;
            case R.id.note_title_textView:
                setClickViewTitle();
                break;
            default:
                disableEditMode();
                break;
        }
    }

    private void saveNewNote() {
        String title = mNote_title_editText.getText().toString().trim();
        String content = mNote_text_editText.getText().toString().trim();
        String timestamp = UtilityDate.getCurrentTimestamp();
        if (title.equals("Note Title".trim()) && content.equals("".trim())) {
            editUpdate_notes = false;
            Log.d(COMMON_TAG,TAG+"Please rename your note title, and don't leave the content empty");
        } else if(title.equals("") && content.equals("")){
            editUpdate_notes = false;
            Log.d(COMMON_TAG,TAG+"Empty title and content won't saved");
        }
        else {
            editUpdate_notes = true;
            Intent data = getIntent();

            if(title.equals(initialNote.getTitle()) && content.equals(initialNote.getContent())){
                editUpdate_notes = false;
            }else {
                int id = data.getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    data.putExtra(EXTRA_ID, id);

                }
                data.putExtra(EXTRA_TITLE, title);
                data.putExtra(EXTRA_CONTENT, content);
                data.putExtra(EXTRA_TIMESTAMP, timestamp);
                setResult(RESULT_OK, data);
                Log.d(COMMON_TAG,TAG+" saved");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("editable", editUpdate_notes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editUpdate_notes = savedInstanceState.getBoolean("editable");
        if(!editUpdate_notes){
            enableEditMode();
        }else{
            disableEditMode();
        }
    }

    @Override
    public void onBackPressed() {
        if (!editUpdate_notes) {
            disableContentInteraction();
            if(mCheckContainer.getVisibility() == View.VISIBLE) {
                onClick(mCheck);
                editUpdate_notes = true;
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(COMMON_TAG, TAG + " double tap!");
        enableEditMode();
        editUpdate_notes = false;
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
    @Override
    public void onLongPress(MotionEvent e) { }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return false; }

    private void showHideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


}
