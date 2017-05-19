package deweerdt.maarten.mivbrouteplanner.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import deweerdt.maarten.mivbrouteplanner.R;
import deweerdt.maarten.mivbrouteplanner.entities.Route;
import deweerdt.maarten.mivbrouteplanner.entities.Stop;
import deweerdt.maarten.mivbrouteplanner.util.DirectionFinder;
import deweerdt.maarten.mivbrouteplanner.util.DirectionFinderListener;

public class MapsFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener{

    

    private GoogleMap mGoogleMap;
    private MapView mvMap;
    private Button btnRoute;
    private double selectedLat, selectedLong;
    private ProgressDialog progressDialog;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();



    Stop selectedStop;

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


        selectedLat = Double.parseDouble(selectedStop.getStop_lat());
        selectedLong = Double.parseDouble(selectedStop.getStop_lon());

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(selectedLat, selectedLong))
                .title(selectedStop.getStop_name()));


        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(selectedLat, selectedLong)).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

    }

    private void sendRequest(){
        LatLng origin = new LatLng(41, 20);
        LatLng destination = new LatLng(selectedLat, selectedLong);
        try {
            new DirectionFinder(this, origin, destination).execute();
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
                        .position(new LatLng(10, 10))));
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
            }
        }

    }