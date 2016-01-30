package com.konifar.confsched.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.konifar.confsched.R;
import com.konifar.confsched.databinding.FragmentMapBinding;
import com.konifar.confsched.model.PlaceMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class MapFragment extends Fragment {

    public static final String TAG = MapFragment.class.getSimpleName();
    public static final LatLng LAT_LNG_CENTER = new LatLng(35.605358, 139.683552);
    private static final int DEFAULT_ZOOM = 17;

    private FragmentMapBinding binding;
    private List<PlaceMap> placeMapList;
    private Map<Integer, Marker> markers = new HashMap<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeMapList = PlaceMap.createList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        initGoogleMap();
        setHasOptionsMenu(true);
        initBackPressed();
        return binding.getRoot();
    }

    private void initBackPressed() {
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && binding.mapSearchView.isVisible()) {
                binding.mapSearchView.revealOff();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                binding.mapSearchView.toggle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(googleMap -> {
            binding.mapSearchView.bindData(placeMapList, placeMap -> {
                LatLng latLng = new LatLng(placeMap.latitude, placeMap.longitude);
                int duration = getResources().getInteger(R.integer.map_camera_move_mills);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), duration, null);

                Marker marker = markers.get(placeMap.nameRes);
                if (marker != null) marker.showInfoWindow();
            });

            binding.loadingView.setVisibility(View.GONE);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setIndoorEnabled(true);
            googleMap.setBuildingsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LAT_LNG_CENTER, DEFAULT_ZOOM));
            UiSettings mapUiSettings = googleMap.getUiSettings();
            mapUiSettings.setCompassEnabled(true);
            renderMarkers(placeMapList, googleMap);
        });
    }

    private void renderMarkers(List<PlaceMap> placeMaps, GoogleMap googleMap) {
        Observable.from(placeMaps).forEach(placeMap -> {
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(placeMap.latitude, placeMap.longitude))
                    .title(getString(placeMap.nameRes))
                    .icon(BitmapDescriptorFactory.fromResource(placeMap.markerRes))
                    .snippet(getString(placeMap.buildingNameRes));
            Marker marker = googleMap.addMarker(options);
            markers.put(placeMap.nameRes, marker);
        });
    }

}
