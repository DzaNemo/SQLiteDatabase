package link.akademijasqliteasgn;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import link.akademijasqliteasgn.data.StudentContract;

public class StudentCursorAdapter extends CursorAdapter {

    public StudentCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //find views that we want to modify in the list item layout
        TextView firsNameTextView = view.findViewById(R.id.first_name_lv);
        TextView lastNameTextView = view.findViewById(R.id.last_name_lv);
        TextView pointsTextView = view.findViewById(R.id.points_lv);

        //find the columns of student attributes that we are interested in
        int fnColumnIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_FIRST_NAME);
        int lnColumnIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_LAST_NAME);
        int pointColumnIndex = cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_POINTS);

        //read student attributes from the Cursor for the current student
        String studentFirstName = cursor.getString(fnColumnIndex);
        String studentLastName = cursor.getString(lnColumnIndex);
        String studentPoints = cursor.getString(pointColumnIndex);

        //update the TextViews with the attributes for the current student
        firsNameTextView.setText(studentFirstName);
        lastNameTextView.setText(studentLastName);
        pointsTextView.setText(studentPoints);
    }
}
