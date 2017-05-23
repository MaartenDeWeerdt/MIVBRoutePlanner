package deweerdt.maarten.mivbrouteplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import deweerdt.maarten.mivbrouteplanner.entities.Stop;

/**
 * Created by Jo on 15/05/2017.
 */

public class StopDAO {

    //singleton
    public static final StopDAO INSTANCE = new StopDAO();
    //variabelen
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public void openConnection (Context context){
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public ArrayList<Stop> getAllStops(){

        ArrayList<Stop> stops = new ArrayList<>();

        //Cursor = resultset met verwijzing naar specifieke rij in resultatenset
        String select = String.format("SELECT * FROM %s GROUP BY %s", DBStop.TABLE_STOPS, DBStop.STOP_NAME);

        Cursor mCursor = mDatabase.rawQuery(select, null);
        //zeker zijn dat we op de eerste rij starten
        mCursor.moveToFirst();
        Log.i("Select", "rows "+mCursor.getCount());
        //alle rijen overlopen
        //loopen zolang de laatste rij nog niet is verwerkt
        while (!mCursor.isAfterLast()){

            //comment maken van huidige rij
            Stop temp = new Stop();

            int idIndex = mCursor.getColumnIndex(DBStop.STOP_ID);
            temp.stop_id = mCursor.getString(idIndex);

            int codeIndex = mCursor.getColumnIndex(DBStop.STOP_CODE);
            temp.stop_code = mCursor.getString(codeIndex);

            int nameIndex = mCursor.getColumnIndex(DBStop.STOP_NAME);
            temp.stop_name = mCursor.getString(nameIndex);

            int descIndex = mCursor.getColumnIndex(DBStop.STOP_DESC);
            temp.stop_desc = mCursor.getString(descIndex);

            int latIndex = mCursor.getColumnIndex(DBStop.STOP_LAT);
            temp.stop_lat = mCursor.getString(latIndex);

            int lonIndex = mCursor.getColumnIndex(DBStop.STOP_LON);
            temp.stop_lon = mCursor.getString(lonIndex);

            int zoneIdIndex = mCursor.getColumnIndex(DBStop.ZONE_ID);
            temp.zone_id = mCursor.getString(zoneIdIndex);

            int urlIndex = mCursor.getColumnIndex(DBStop.STOP_URL);
            temp.stop_url = mCursor.getString(urlIndex);

            int locationTypeIndex = mCursor.getColumnIndex(DBStop.LOCATION_TYPE);
            temp.location_type = mCursor.getString(locationTypeIndex);

            stops.add(temp);

            //niet vergeten naar volgende rij te gaan, anders oneindige loop
            mCursor.moveToNext();
        }
        return stops;
    }

    public boolean insertStops(ArrayList<Stop> stops){

        if(mDatabase == null)
            return false;

        for (Stop newstop: stops ) {
            ContentValues mValues = new ContentValues();

            mValues.put(DBStop.STOP_ID, newstop.getStop_id());
            mValues.put(DBStop.STOP_CODE, newstop.getStop_code());
            mValues.put(DBStop.STOP_NAME, newstop.getStop_name());
            mValues.put(DBStop.STOP_DESC, newstop.getStop_desc());
            mValues.put(DBStop.STOP_LON, newstop.getStop_lon());
            mValues.put(DBStop.STOP_LAT, newstop.getStop_lat());
            mValues.put(DBStop.ZONE_ID, newstop.getZone_id());
            mValues.put(DBStop.STOP_URL, newstop.getStop_url());
            mValues.put(DBStop.LOCATION_TYPE, newstop.getLocation_type());

           long id = mDatabase.insert(DBStop.TABLE_STOPS, null, mValues);
            Log.i("insert", "inserted row "+ id);
        }

        return true;
    }
}
