package link.akademijasqliteasgn;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import link.akademijasqliteasgn.data.StudentContract.StudentEntry;
import link.akademijasqliteasgn.data.StudentDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

   // private StudentDbHelper studentDbHelper;
    Button addButton;
    Button deleteButton;

    //indentifier for the pet data loader
    private static final int STUDENT_LOADER = 0;

    //adapter for the listView
    StudentCursorAdapter studentCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddStudentActivity.class);
                startActivity(intent);
            }
        });

        deleteButton = findViewById(R.id.delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllStudents();
            }
        });

        //listView which will be populated with the student data
        ListView listView = findViewById(R.id.students_list_view);

        //setup an adapter to create a list item for each row of sudent data in the cursor
        //ther is no data yet so we pass null for the cursor
        studentCursorAdapter = new StudentCursorAdapter(this,null);
        listView.setAdapter(studentCursorAdapter);

       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,AddStudentActivity.class);
                Uri currentUri = ContentUris.withAppendedId(StudentEntry.CONTENT_URI,id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });*/

        getLoaderManager().initLoader(STUDENT_LOADER,null,this);



       // studentDbHelper = new StudentDbHelper(this);
       /* ListView listView = findViewById(R.id.students_list_view);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);*/
    }


    private void deleteAllStudents(){
        int rowsDeleted = getContentResolver().delete(StudentEntry.CONTENT_URI,null,null);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_delete_all:
                deleteAllStudents();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection = {
                StudentEntry._ID,       //always nedeed for the cursor
                StudentEntry.COLUMN_FIRST_NAME,
                StudentEntry.COLUMN_LAST_NAME,
                StudentEntry.COLUMN_POINTS
        };

        String selection = StudentEntry.COLUMN_POINTS + ">=?";
        String[]selectionArgs = {"80"};

        return new CursorLoader(this,
                StudentEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                StudentEntry.COLUMN_POINTS + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        studentCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        studentCursorAdapter.swapCursor(null);
    }
}
