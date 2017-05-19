package deweerdt.maarten.mivbrouteplanner.util;

import java.util.List;

import deweerdt.maarten.mivbrouteplanner.entities.Route;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
