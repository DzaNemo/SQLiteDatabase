package link.akademijasqliteasgn.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StudentContract {

    //empty constructor (for preventing someone from instantiating the contract class)
    private StudentContract(){}


    /**
     * CONTENT_AUTHORITY is a name for the entire content provider,and convenient string to use fr the content authority
     * is the package name for the app, whis is guaranteed to be unique on the device
     */
    public static final String CONTENT_AUTHORITY = "link.akademijasqliteasgn";

    /**
     * Use CONTENT_AUTHORITY to create a base of all URI's which apps will use to contact the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * path for looking at student data
     */
    public static final String PATH_STUDENTS = "polaznik";

    /**
     * inner class that defines constant values for the student database table
     * each entry in the table represent a single student
     */
    public static final class StudentEntry implements BaseColumns{

        /**
         * content URI to access the student data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_STUDENTS);

        //name of database table
        public final static String TABLE_NAME = "polaznik";

        //unique ID number for the student
        public final static String _ID = BaseColumns._ID;
        //first name of the student
        public final static String COLUMN_FIRST_NAME = "first_name";
        //last name of the student
        public final static String COLUMN_LAST_NAME = "lastt_name";
        //year of students enrollment
        public final static String COLUMN_YEAR = "year";
        //points that students have
        public final static String COLUMN_POINTS = "points";
    }

}
