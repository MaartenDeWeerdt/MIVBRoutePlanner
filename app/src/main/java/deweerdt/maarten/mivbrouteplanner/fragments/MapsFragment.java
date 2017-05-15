package deweerdt.maarten.mivbrouteplanner.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import deweerdt.maarten.mivbrouteplanner.R;

public class MapsFragment extends Fragment implements OnMapReadyCallback{
    private GoogleMap mGoogleMap;
    private MapView mvMap;

    public MapsFragment() {
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mvMap = (MapView) rootView.findViewById(R.id.mv_map);
        mvMap.onCreate(savedInstanceState);
        mvMap.getMapAsync(this);


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

            LatLng zoemCord = new LatLng(51.122707, 4.418044);

            CameraUpdate testUpdate = CameraUpdateFactory.newLatLngZoom(zoemCord, 12);

            mGoogleMap.animateCamera(testUpdate);
        }
}
