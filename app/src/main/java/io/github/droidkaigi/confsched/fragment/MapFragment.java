package io.github.droidkaigi.confsched.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.FragmentMapBinding;
import io.github.droidkaigi.confsched.model.PlaceMap;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;

import static io.github.droidkaigi.confsched.fragment.MapFragmentPermissionsDispatcher.initGoogleMapWithCheck;

@RuntimePermissions
public class MapFragment extends Fragment {

    public static final LatLng LAT_LNG_CENTER = new LatLng(35.604757, 139.683788);
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
        initGoogleMapWithCheck(this);
        setHasOptionsMenu(true);
        initBackPressed();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment map = (SupportMapFragment) fm.findFragmentById(R.id.map);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(map);
        ft.commit();
        super.onDestroyView();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(googleMap -> {
            //noinspection MissingPermission
            googleMap.setMyLocationEnabled(true);
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

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onFineLocationDenied() {
        Toast.makeText(getContext(), R.string.map_fine_location_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void onFineLocationNeverAskAgain() {
        Toast.makeText(getContext(), R.string.map_fine_location_never_askagain, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForFineLocation(PermissionRequest request) {
        showRationaleDialog(R.string.map_fine_location_rationale, request);
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setPositiveButton(R.string.map_button_allow, (dialog, which) -> {
                    request.proceed();
                })
                .setNegativeButton(R.string.map_button_deny, (dialog, which) -> {
                    request.cancel();
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
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

}
