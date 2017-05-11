package deweerdt.maarten.mivbrouteplanner.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.entities.Stop;
import deweerdt.maarten.mivbrouteplanner.util.StopsAdapter;
import deweerdt.maarten.mivbrouteplanner.util.StopsParser;


public class StopsFragment extends Fragment {

    private StopsAdapter mAdapter;

    private ListView lvStops;

    public StopsFragment() {

    }

    public static StopsFragment newInstance() {
        StopsFragment fragment = new StopsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stops, container, false);

        lvStops = (ListView) rootView.findViewById(R.id.lv_stops);

        mAdapter = new StopsAdapter(getActivity(), StopsParser.getInstance().parseStops(new FileInputStream(getCacheDir()+ File.pathSeparator+"stops.txt")));
        lvStops.setAdapter(mAdapter);


        lvStops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, StopsFragment.newInstance((Stop) mAdapter.getItem(position))).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
