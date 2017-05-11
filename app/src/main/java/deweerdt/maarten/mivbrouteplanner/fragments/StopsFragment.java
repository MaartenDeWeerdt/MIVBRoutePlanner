package deweerdt.maarten.mivbrouteplanner.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.requests.RawDataRequest;
import deweerdt.maarten.mivbrouteplanner.util.StopsAdapter;
import deweerdt.maarten.mivbrouteplanner.util.StopsParser;


public class StopsFragment extends Fragment {

    private StopsAdapter mAdapter;
    private ListView lvStops;
    private ProgressBar pbMain;

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




        /*
        lvStops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, StopsFragment.newInstance()).commit();
            }
        });
        */

        return rootView;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getActivity(), "start download", Toast.LENGTH_SHORT).show();
        downloadZIP();

        pbMain = (ProgressBar) getActivity().findViewById(R.id.pb_main);

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

    private Response.Listener<byte[]> responseGETListener = new Response.Listener<byte[]>() {
        @Override
        public void onResponse(byte[] response) {

            //http://stackoverflow.com/questions/8367126/how-can-i-convert-byte-array-to-zip-file
            //https://techstricks.com/download-file-using-android-volley/

            try {
                //set the path where we want to save the file
                //in this case, going to save it on the cache directory of the project
                File cacheDir = getActivity().getCacheDir();

                ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(response));
                ZipEntry entry;

                while ((entry = zipStream.getNextEntry()) != null) {
                    //gets filenames from zip -> party
                    String entryName = entry.getName();

                    File f = new File(cacheDir + File.pathSeparator + entryName);
                    FileOutputStream out = new FileOutputStream(f);

                    byte[] byteBuff = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = zipStream.read(byteBuff)) != -1)
                    {
                        out.write(byteBuff, 0, bytesRead);
                    }
                    out.close();

                    zipStream.closeEntry();
                }
                zipStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            parseFiles();
        }
    };
    private Response.ErrorListener responseGETErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //TODO catch errors
        }
    };

    private void downloadZIP() {

        RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //params voor header
        HashMap<String, String> Headerparams = new HashMap<>();
        Headerparams.put("Authorization:", "Bearer f21253836326beabd212118261992e6f");

        //headers kan je niet setten, fast and dirty de klasse overschrijven
        RawDataRequest getRequest = new RawDataRequest(Request.Method.GET,
                "https://opendata-api.stib-mivb.be/Files/1.0/Gtfs",
                responseGETListener,
                responseGETErrorListener,
                Headerparams
        );
        mQueue.add(getRequest);
    }

    private void parseFiles()
    {
        try {
            StopsParser.getInstance()
                    .parseStops(new FileInputStream(getActivity().getCacheDir()+File.pathSeparator+"stops.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(getActivity().getApplicationContext(), "Finished loading data", Toast.LENGTH_LONG).show();

        try {
            mAdapter = new StopsAdapter(getActivity(), StopsParser.getInstance().parseStops(new FileInputStream(getActivity().getCacheDir()+ File.pathSeparator + "stops.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pbMain.setEnabled(false);
        pbMain.setVisibility(View.INVISIBLE);
        lvStops.setAdapter(mAdapter);
    }
}
