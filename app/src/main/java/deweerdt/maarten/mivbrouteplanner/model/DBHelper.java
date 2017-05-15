package deweerdt.maarten.mivbrouteplanner.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maarten De Weerdt on 15/05/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBStop.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE table " + DBStop.TABLE_STOPS + " ( "
                + DBStop.STOP_ID + ", "
                + DBStop.STOP_CODE + ", "
                + DBStop.STOP_NAME + ", "
                + DBStop.STOP_DESC + ", "
                + DBStop.STOP_LAT + ", "
                + DBStop.STOP_LON + ", "
                + DBStop.ZONE_ID + ", "
                + DBStop.STOP_URL + ", "
                + DBStop.LOCATION_TYPE + ", "
                + ")";

        db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
