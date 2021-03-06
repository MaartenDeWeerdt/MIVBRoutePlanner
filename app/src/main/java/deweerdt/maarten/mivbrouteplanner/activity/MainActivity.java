package deweerdt.maarten.mivbrouteplanner.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.entities.Stop;
import deweerdt.maarten.mivbrouteplanner.fragments.StopsFragment;
import deweerdt.maarten.mivbrouteplanner.model.StopDAO;
import deweerdt.maarten.mivbrouteplanner.requests.RawDataRequest;

public class MainActivity extends AppCompatActivity {


    SharedPreferences prefs;
    private StopDAO stopdao = new StopDAO();
    private ProgressBar pbMain;
    private ArrayList<Stop> mStopsList = new ArrayList<Stop>();
    private Response.Listener<byte[]> responseGETListener = new Response.Listener<byte[]>() {
        @Override
        public void onResponse(byte[] response) {

            //http://stackoverflow.com/questions/8367126/how-can-i-convert-byte-array-to-zip-file
            //https://techstricks.com/download-file-using-android-volley/


            try {
                //set the path where we want to save the file
                //in this case, going to save it on the cache directory of the project
                File cacheDir = getCacheDir();

                ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(response));
                ZipEntry entry;

                while ((entry = zipStream.getNextEntry()) != null) {
                    //gets filenames from zip -> party
                    String entryName = entry.getName();

                    File f = new File(cacheDir + File.pathSeparator + entryName);
                    FileOutputStream out = new FileOutputStream(f);

                    byte[] byteBuff = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = zipStream.read(byteBuff)) != -1) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isFirstTime = prefs.getBoolean("first", true);

        pbMain = (ProgressBar) findViewById(R.id.pb_main);


        if (isFirstTime) {
            downloadZIP();
            Toast.makeText(this, "start download", Toast.LENGTH_SHORT).show();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new StopsFragment()).commit();
            pbMain.setEnabled(false);
            pbMain.setVisibility(View.INVISIBLE);
        }


    }

    private void downloadZIP() {

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        //params voor header
        HashMap<String, String> Headerparams = new HashMap<>();
        Headerparams.put("Authorization:", "Bearer 8476f1884721a7a2340b88a1c27b2c1a");

        //headers kan je niet setten, fast and dirty de klasse overschrijven
        RawDataRequest getRequest = new RawDataRequest(Request.Method.GET,
                "https://opendata-api.stib-mivb.be/Files/1.0/Gtfs",
                responseGETListener,
                responseGETErrorListener,
                Headerparams
        );
        mQueue.add(getRequest);
    }

    private void parseFiles() {
        stopdao.openConnection(this);
        try {

            BufferedReader rawReader = new BufferedReader(new InputStreamReader(new FileInputStream(getCacheDir() + File.pathSeparator + "stops.txt")));
            String line = "";

            while ((line = rawReader.readLine()) != null) {
                mStopsList.add(new Stop(line));
            }

            //first row in file are columns
            mStopsList.remove(0);
            stopdao.insertStops(mStopsList);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Finished loading data", Toast.LENGTH_LONG).show();

        pbMain.setEnabled(false);
        pbMain.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new StopsFragment()).commit();

        prefs.edit().putBoolean("first", false).apply();
    }
}
