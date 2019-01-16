package link.akademijasqliteasgn.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static link.akademijasqliteasgn.data.StudentContract.StudentEntry;

/**
 * database helper , manage database creation and version menagment
 */
public class StudentDbHelper extends SQLiteOpenHelper {

    //name of the database file
    private static final String DATABASE_NAME = "akademija.db";

    //database version. When we change the database schema we must increment the database version
    private static final int DATABASE_VERSION = 1;


    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * this is called when the database is created for the first time
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a String that contains the SQL statement to create students table
        String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + StudentEntry.TABLE_NAME + "("
                + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentEntry.COLUMN_FIRST_NAME + " TEXT , "
                + StudentEntry.COLUMN_LAST_NAME + " TEXT , "
                + StudentEntry.COLUMN_YEAR + " INTEGER , "
                + StudentEntry.COLUMN_POINTS + " INTEGER );";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_STUDENTS_TABLE);
    }

    /**
     * this is called when the database is needs to be upgraded
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
