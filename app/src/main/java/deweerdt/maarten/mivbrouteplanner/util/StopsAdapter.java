package deweerdt.maarten.mivbrouteplanner.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.entities.Stop;

/**
 * Created by Maarten De Weerdt on 10/05/2017.
 */

public class StopsAdapter extends BaseAdapter implements Filterable {

    private Viewholder holder;
    private ArrayList<Stop> stops;
    private Activity context;

    public StopsAdapter(Activity context, ArrayList<Stop> stops) {
        this.context = context;
        this.stops = stops;
    }

    @Override
    public int getCount() {
        return stops.size();
    }

    @Override
    public Object getItem(int position) {
        return stops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(stops.get(position).getStop_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = context.getLayoutInflater().inflate(R.layout.stops_row, parent, false);

            holder = new Viewholder();

            holder.tvRowStops = (TextView) convertView.findViewById(R.id.tv_row_stop);


            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        Stop stop = stops.get(position);

        holder.tvRowStops.setText(stop.getStop_name().replace("\"", ""));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private class Viewholder {
        public TextView tvRowStops;
    }
}
