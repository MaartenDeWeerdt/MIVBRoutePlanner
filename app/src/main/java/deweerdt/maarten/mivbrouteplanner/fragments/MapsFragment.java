package deweerdt.maarten.mivbrouteplanner.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.entities.Stop;
import deweerdt.maarten.mivbrouteplanner.util.DirectionFinder;
import deweerdt.maarten.mivbrouteplanner.util.DirectionFinderListener;

public class MapsFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener {


    final int LOCATIEPERMISSIE = 100;
    Stop selectedStop;
    private GoogleMap mGoogleMap;
    private MapView mvMap;
    private Button btnRoute;
    private TextView tvAfstand;
    private TextView tvTijd;
    private double selectedLat, selectedLong;
    private ProgressDialog progressDialog;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private boolean gotLocationPermission = false;

    public MapsFragment() {
    }

    public static MapsFragment newInstance(Stop s) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putSerializable("stop", s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mvMap = (MapView) rootView.findViewById(R.id.mv_map);
        mvMap.onCreate(savedInstanceState);
        mvMap.getMapAsync(this);

        btnRoute = (Button) rootView.findViewById(R.id.btn_route);
        tvAfstand = (TextView) rootView.findViewById(R.id.tv_afstand);
        tvTijd = (TextView) rootView.findViewById(R.id.tv_tijd);

        btnRoute.setEnabled(false);

        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        selectedStop = (Stop) getArguments().getSerializable("stop");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);


        selectedLat = Double.parseDouble(selectedStop.getStop_lat());
        selectedLong = Double.parseDouble(selectedStop.getStop_lon());

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(selectedLat, selectedLong))
                .title(selectedStop.getStop_name()));


        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(selectedLat, selectedLong)).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        updateMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("UPDATE", "check permission granted");
        switch (requestCode) {
            case LOCATIEPERMISSIE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gotLocationPermission = true;
                    updateMap();
                    Log.d("UPDATE", "Hoera");
                }
        }
    }

    private void sendRequest() {
        Geocoder geocoder = new Geocoder(getActivity());
        List huidigeLocatie = null;
        List destination = null;
        String startpunt = null;
        String eindpunt = null;

        try {
            huidigeLocatie = geocoder.getFromLocation(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude(), 1);
            if (huidigeLocatie != null && huidigeLocatie.size() > 0) {
                Address address = (Address) huidigeLocatie.get(0);
                // sending back first address line and locality
                startpunt = address.getAddressLine(0) + ", " + address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            destination = geocoder.getFromLocation(selectedLat, selectedLong, 1);
            if (destination != null && destination.size() > 0) {
                Address address = (Address) destination.get(0);
                // sending back first address line and locality
                eindpunt = address.getAddressLine(0) + ", " + address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new DirectionFinder(this, startpunt, eindpunt).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait.",
                "Finding direction..!", true);
    }

    @Override
    public void onDirectionFinderSuccess(List<deweerdt.maarten.mivbrouteplanner.util.Route> routes) {

        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (deweerdt.maarten.mivbrouteplanner.util.Route newRoute : routes) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newRoute.startLocation, 16));

            originMarkers.add(mGoogleMap.addMarker(new MarkerOptions()
                    .title("huidige locatie")
                    .position(new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude()))));
            destinationMarkers.add(mGoogleMap.addMarker(new MarkerOptions()
                    .title(selectedStop.getStop_name())
                    .position(new LatLng(selectedLat, selectedLong))));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < newRoute.points.size(); i++)
                polylineOptions.add(newRoute.points.get(i));

            polylinePaths.add(mGoogleMap.addPolyline(polylineOptions));
            tvTijd.setText(newRoute.duration.toString());
            tvAfstand.setText(newRoute.distance.toString());
        }
    }

    public void updateMap() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gotLocationPermission = true;
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATIEPERMISSIE);
        }

        if (gotLocationPermission) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            btnRoute.setEnabled(true);

        } else {
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }


}