package com.konifar.confsched.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.konifar.confsched.R;
import com.konifar.confsched.databinding.FragmentMapBinding;
import com.konifar.confsched.model.Map;

import java.util.List;

public class MapFragment extends Fragment {

    public static final String TAG = MapFragment.class.getSimpleName();
    public static final LatLng LAT_LNG_CENTER = new LatLng(35.605358, 139.683552);
    private static final int DEFAULT_ZOOM = 17;

    private FragmentMapBinding binding;
    private List<Map> mapList;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapList = Map.createList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        initGoogleMap();
        setHasOptionsMenu(true);
        return binding.getRoot();
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
            binding.loadingView.setVisibility(View.GONE);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setIndoorEnabled(true);
            googleMap.setBuildingsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LAT_LNG_CENTER, DEFAULT_ZOOM));
            UiSettings mapUiSettings = googleMap.getUiSettings();
            mapUiSettings.setCompassEnabled(true);
            renderMarkers(mapList, googleMap);
        });
    }

    private void renderMarkers(List<Map> maps, GoogleMap googleMap) {
        for (Map map : maps) {
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(map.latitude, map.longitude))
                    .title(getString(map.nameRes))
                    .icon(BitmapDescriptorFactory.fromResource(map.markerRes))
                    .snippet(getString(map.buildingNameRes));
            googleMap.addMarker(options);
        }
    }

}
