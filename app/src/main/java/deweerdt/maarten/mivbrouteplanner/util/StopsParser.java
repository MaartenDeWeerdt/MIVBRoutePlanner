package deweerdt.maarten.mivbrouteplanner.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import deweerdt.maarten.mivbrouteplanner.entities.Stop;

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
    private ArrayList<Stop> mStopsList = new ArrayList<Stop> ();
    private final String TAG = "GtfsDemo";

    public void parseStops(FileInputStream rid) {
        BufferedReader rawReader = new BufferedReader(new InputStreamReader(rid));
        String line = "";
        try {
            while((line = rawReader.readLine()) != null) {
                mStopsList.add(new Stop(line));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //first row in file are columns
        mStopsList.remove(0);

        printStops();
    }

    private void printStops() {
        for (Stop stop : mStopsList)
            Log.i(TAG, "stop_id " + stop.stop_id + "\n"
                    + "stop_code " + stop.stop_code + "\n"
                    + "stop_name " + stop.stop_name + "\n"
                    + "stop_desc " + stop.stop_desc + "\n"
                    + "stop_lat " + stop.stop_lat + "\n"
                    + "stop_lon " + stop.stop_lon + "\n"
                    + "zone_id " + stop.zone_id + "\n"
                    + "stop_url " + stop.stop_url + "\n"
                    + "location_type " + stop.location_type);
    }
}
