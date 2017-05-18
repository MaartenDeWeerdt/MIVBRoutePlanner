package deweerdt.maarten.mivbrouteplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.entities.Stop;
import deweerdt.maarten.mivbrouteplanner.model.StopDAO;
import deweerdt.maarten.mivbrouteplanner.requests.RawDataRequest;
import deweerdt.maarten.mivbrouteplanner.util.StopsAdapter;
import deweerdt.maarten.mivbrouteplanner.util.StopsParser;


public class StopsFragment extends Fragment {

    private StopsAdapter mAdapter;
    private ListView lvStops;


    StopDAO stopdao = new StopDAO();

    public StopsFragment() {

    }

    public static StopsFragment newInstance() {
        StopsFragment fragment = new StopsFragment();

        return fragment;
    }

    ListView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, MapsFragment.newInstance((Stop) mAdapter.getItem(position))).commit();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stops, container, false);

        lvStops = (ListView) rootView.findViewById(R.id.lv_stops);
        stopdao.openConnection(getActivity());
        mAdapter = new StopsAdapter(getActivity(), stopdao.getAllStops());
        lvStops.setAdapter(mAdapter);
        lvStops.setOnItemClickListener(itemClickListener);
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //done, empty cache
        File cacheDir = getActivity().getCacheDir();
        //get all files in cache
        File[] files = cacheDir.listFiles();

        if (files != null) {
            for (File file : files)
                file.delete();
        }
    }

}
