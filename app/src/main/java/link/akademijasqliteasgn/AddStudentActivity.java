package link.akademijasqliteasgn;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import link.akademijasqliteasgn.data.StudentContract.StudentEntry;


public class AddStudentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_STUDENT_LOADER = 0;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText yearEditText;
    private EditText pointsEditText;

    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Uri currentUri = intent.getData();

        if (currentUri == null){
            setTitle(R.string.add_activity_title);
        }else{
            setTitle(R.string.add_activity_delete_title);
        }
        */


        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        yearEditText = findViewById(R.id.year_edit_text);
        pointsEditText = findViewById(R.id.points_edit_text);


        Button btnSave = findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertStudent();
                firstNameEditText.setText("");
                lastNameEditText.setText("");
                yearEditText.setText("");
                pointsEditText.setText("");


            }
        });

        Button btnShow = findViewById(R.id.show_list_button);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStudentActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * get user input from editor and save student into database
     */

    private void insertStudent(){

        //read from input fields
        String fnString = firstNameEditText.getText().toString().trim();
        String lnString = lastNameEditText.getText().toString().trim();
        String yString = yearEditText.getText().toString().trim();
        int year = Integer.parseInt(yString);
        String pString = pointsEditText.getText().toString().trim();
        int points = Integer.parseInt(pString);


        //StudentDbHelper studentDbHelper = new StudentDbHelper(this);
        //SQLiteDatabase db = studentDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StudentEntry.COLUMN_FIRST_NAME,fnString);
        values.put(StudentEntry.COLUMN_LAST_NAME,lnString);
        values.put(StudentEntry.COLUMN_YEAR,year);
        values.put(StudentEntry.COLUMN_POINTS,points);

        //long newRowId = db.insert(StudentEntry.TABLE_NAME,null,values);

        Uri newUri = getContentResolver().insert(StudentEntry.CONTENT_URI,values);

        if (newUri == null){
            Toast.makeText(this,getString(R.string.insert_student_failed),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,getString(R.string.insert_student_successful),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
