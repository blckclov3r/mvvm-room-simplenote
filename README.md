# Simple Note Taking App
## Notes Taking App using Room and MVVM Architecture

* Room
  * Database: It is an abstract class annotated with @Database and extending RoomDatabase.
  * Entity: It represents table in our database.
  * DAO: It is an interface needed for every entity to access the database.

## What i learned
* Developed a user interface within the xml file using CoordinatorLayout, AppBarLayout, RelativeLayout, LinearLayout, 
  RecyclerView, ListAdapter, Custom LineEditText, EditText, Spinner & Button etc.
* Integrated setOnClick, setOnItemLongClick, OnTouch, Gesture, DoubleTapListener.
* This repository merging what i've learned by watching tutorials via codingwithmitch(SQLite 2019) and codinginflow(MVVM).

## Dependencies
* AndroidX 
* Android support design
  * implementation 'com.android.support:design:28.0.0'
  * implementation 'com.google.android.material:material:1.0.0-beta01'
* Room components
  * implementation "androidx.room:room-runtime:2.1.0"
  * annotationProcessor "androidx.room:room-compiler:2.1.0"
* Lifecycle components
  * implementation "androidx.lifecycle:lifecycle-extensions:2.0.0"
  * annotationProcessor "androidx.lifecycle:lifecycle-compiler:2.0.0"
