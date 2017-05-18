package deweerdt.maarten.mivbrouteplanner.entities;

import java.io.Serializable;

/**
 * Created by Maarten De Weerdt on 10/05/2017.
 */

public class Stop implements Serializable {
    public String stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type;

    public Stop(String str) {

        String[] temp = str.split(",");

        this.stop_id = temp[0];
        this.stop_code = temp[1];
        this.stop_name = temp[2];
        this.stop_desc = temp[3];
        this.stop_lat = temp[4];
        this.stop_lon = temp[5];
        this.zone_id = temp[6];
        this.stop_url = temp[7];
        this.location_type = temp[8];
    }

    public Stop() {
    }

    public String getStop_id() {
        return stop_id;
    }

    public String getStop_code() {
        return stop_code;
    }

    public String getStop_name() {
        return stop_name;
    }

    public String getStop_desc() {
        return stop_desc;
    }

    public String getStop_lat() {
        return stop_lat;
    }

    public String getStop_lon() {
        return stop_lon;
    }

    public String getZone_id() {
        return zone_id;
    }

    public String getStop_url() {
        return stop_url;
    }

    public String getLocation_type() {
        return location_type;
    }
}
