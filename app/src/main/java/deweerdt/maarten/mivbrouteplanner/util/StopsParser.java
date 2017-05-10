package deweerdt.maarten.mivbrouteplanner.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import deweerdt.maarten.mivbrouteplanner.entities.Stops;

/**
 * Created by Maarten De Weerdt on 10/05/2017.
 */

public class StopsParser {
    private static final StopsParser ourInstance = new StopsParser();

    public static StopsParser getInstance() {
        return ourInstance;
    }

    private StopsParser() {
    }

    //needed stuff
    private ArrayList<Stops> mCalendarList = new ArrayList<Stops> ();
    private final String TAG = "GtfsDemo";

    public void parseCalendar(FileInputStream rid) {
        BufferedReader rawReader = new BufferedReader(new InputStreamReader(rid));
        String line = "";
        try {
            while((line = rawReader.readLine()) != null) {
                mCalendarList.add(new Stops(line));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //first row in file are columns
        mCalendarList.remove(0);

        printCalendar();
    }

    private void printCalendar() {
        for (Stops stops : mCalendarList)
            Log.i(TAG, "stop_id " + stops.stop_id + "\n"
                    + "stop_code " + stops.stop_code + "\n"
                    + "stop_name " + stops.stop_name + "\n"
                    + "stop_desc " + stops.stop_desc + "\n"
                    + "stop_lat " + stops.stop_lat + "\n"
                    + "stop_lon " + stops.stop_lon + "\n"
                    + "zone_id " + stops.zone_id + "\n"
                    + "stop_url " + stops.stop_url + "\n"
                    + "location_type " + stops.location_type);
    }
}
