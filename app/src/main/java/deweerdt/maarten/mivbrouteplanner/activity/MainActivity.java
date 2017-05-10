package deweerdt.maarten.mivbrouteplanner.activity;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.requests.RawDataRequest;
import deweerdt.maarten.mivbrouteplanner.util.StopsParser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "start download", Toast.LENGTH_SHORT).show();
        downloadZIP();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //done, empty cache
        File cacheDir = getCacheDir();
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

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
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
                    .parseStops(new FileInputStream(getCacheDir()+File.pathSeparator+"stops.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Finished loading data", Toast.LENGTH_LONG).show();
    }

}
