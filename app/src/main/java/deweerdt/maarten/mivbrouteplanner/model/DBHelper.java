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
                + DBStop.STOP_ID + " text primary key , "
                + DBStop.STOP_CODE + " text , "
                + DBStop.STOP_NAME + " text , "
                + DBStop.STOP_DESC + " text , "
                + DBStop.STOP_LAT + " text , "
                + DBStop.STOP_LON + " text , "
                + DBStop.ZONE_ID + " text , "
                + DBStop.STOP_URL + " text , "
                + DBStop.LOCATION_TYPE + " text "
                + ")";

        db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
