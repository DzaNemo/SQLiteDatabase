package link.akademijasqliteasgn.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import link.akademijasqliteasgn.data.StudentContract.StudentEntry;

/**
 * Content provider for the app
 * StudentProvider extends ContentProvider and because of that it will need to implement five method (we used query,insert an delete)
 */
public class StudentProvider extends ContentProvider {

    public static final String LOG_TAG = StudentProvider.class.getSimpleName();

    //URI matcher for the content Uri for the students table
    private static final int STUDENTS = 100;
    //URI matcher for the content URI for a single student in the table (if we want to delete or update single student)
    private static final int STUDENT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(StudentContract.CONTENT_AUTHORITY, StudentContract.PATH_STUDENTS, STUDENTS);
        sUriMatcher.addURI(StudentContract.CONTENT_AUTHORITY, StudentContract.PATH_STUDENTS + "/#", STUDENT_ID);
    }

    //database helper object
    private StudentDbHelper studentDbHelper;

    /**
     * initialize the provider and the database helper object
     * @return
     */
    @Override
    public boolean onCreate() {
        studentDbHelper = new StudentDbHelper(getContext());
        return true;
    }

    /**
     * perform the query for thr given URI
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //Get readable database
        SQLiteDatabase database = studentDbHelper.getReadableDatabase();

        //this cursor will hold result of the query
        Cursor cursor;

        //figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match){
            case STUDENTS:
                //query table with the given projection, selection, selectoinArgs and sort order
                //cursor could contain multiple rows of the table
                cursor = database.query(StudentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case STUDENT_ID:
                //in this situation we extract ID from the URI
                selection = StudentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StudentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

             default:
                throw new IllegalArgumentException("Can not query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case STUDENTS:
                return insertStudent(uri,contentValues);

            default:
                    throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertStudent(Uri uri, ContentValues contentValues){

        String firstName = contentValues.getAsString(StudentEntry.COLUMN_FIRST_NAME);
        if (firstName.equals("")){
            throw new IllegalArgumentException("Students requires a first name");
        }
        String lastName = contentValues.getAsString(StudentEntry.COLUMN_LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("Students requires a last name");
        }
        Integer year = contentValues.getAsInteger(StudentEntry.COLUMN_YEAR);
        if (year != null && year > 2018){
            throw new IllegalArgumentException("Enter a regular year");
        }
        Integer points = contentValues.getAsInteger(StudentEntry.COLUMN_POINTS);
        if (points != null && points > 100 ){
            throw new IllegalArgumentException("Enter your points");
        }


        SQLiteDatabase database = studentDbHelper.getWritableDatabase();

        long id = database.insert(StudentEntry.TABLE_NAME,null,contentValues);
        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert the row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selctionArgs) {
        //get writable database
        SQLiteDatabase database = studentDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case STUDENTS:
                //delete all rows that match the selection and selectionArgs
                //return database.delete(StudentEntry.TABLE_NAME,selection,selectionArgs);
                rowsDeleted = database.delete(StudentEntry.TABLE_NAME,selection,selctionArgs);
                break;

            case STUDENT_ID:
                //delete a single row by the ID in the URI`
                selection = StudentEntry._ID + "=?";
                selctionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StudentEntry.TABLE_NAME,selection,selctionArgs);
                break;

            default:
                    throw new IllegalArgumentException("Deletion is not supported for" + uri);
        }

        if (rowsDeleted != 0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

      /*  final int match = sUriMatcher.match(uri);
        switch (match){
            case STUDENTS:
                return updateStudent(uri,contentValues,selection,selectionArgs);

            case STUDENT_ID:
                selection  = StudentContract.StudentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateStudent(uri,contentValues,selection,selectionArgs);

            default:
                    throw new IllegalArgumentException("Update is not supported for "  + uri);
        }*/
      return 0;
    }

    private int updateStudent(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
      /* if (contentValues.containsKey(StudentContract.StudentEntry.COLUMN_FIRST_NAME)){
            String firstName = contentValues.getAsString(StudentContract.StudentEntry.COLUMN_FIRST_NAME);
            if (firstName == null){
                throw new IllegalArgumentException("Insert first name...");
            }
        }
        if (contentValues.containsKey(StudentContract.StudentEntry.COLUMN_LAST_NAME)) {
            String lastName = contentValues.getAsString(StudentContract.StudentEntry.COLUMN_LAST_NAME);
            if (lastName == null){
                throw new IllegalArgumentException("Insert last name...");
            }
        }
        if (contentValues.containsKey(StudentEntry.COLUMN_YEAR)) {
            Integer year = contentValues.getAsInteger(StudentEntry.COLUMN_YEAR);
            if (year != null && year < 1983 && year > 2018){
                throw new IllegalArgumentException("Enter a regular year");
            }
        }
        if (contentValues.containsKey(StudentEntry.COLUMN_POINTS)) {
            Integer points = contentValues.getAsInteger(StudentEntry.COLUMN_POINTS);
            if (points != null && points > 100 ){
                throw new IllegalArgumentException("Enter your points");
            }
        }
        if (contentValues.size() == 0){
            return 0;
        }
        SQLiteDatabase database = studentDbHelper.getWritableDatabase();
        return database.update(StudentEntry.TABLE_NAME,contentValues,selection,selectionArgs);
    }*/
      return 0;
    }
}
