package deweerdt.maarten.mivbrouteplanner.model;

import android.provider.BaseColumns;

/**
 * Created by Maarten De Weerdt on 15/05/2017.
 */

public class DBStop implements BaseColumns {

    public static final String DB_NAME = "dbstops";
    public static final String TABLE_STOPS = "stop";
    public static final String STOP_ID = "stopid";
    public static final String STOP_CODE = "stopcode";
    public static final String STOP_NAME = "stopname";
    public static final String STOP_DESC = "stopdesc";
    public static final String STOP_LAT = "stoplat";
    public static final String STOP_LON = "stoplon";
    public static final String ZONE_ID = "zoneid";
    public static final String STOP_URL = "stopurl";
    public static final String LOCATION_TYPE = "locationtype";

    public static final String[] TABLE_STOPS_COLUMNS = {STOP_ID, STOP_CODE, STOP_NAME, STOP_DESC, STOP_LAT, STOP_LON, ZONE_ID, STOP_URL, LOCATION_TYPE};
}
