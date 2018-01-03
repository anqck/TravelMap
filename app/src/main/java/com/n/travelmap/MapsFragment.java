package com.n.travelmap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.directions.route.Route;
import com.github.pengrad.mapscaleview.MapScaleView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.n.travelmap.Database.FavoritesDA;
import com.n.travelmap.Database.FavoritesDTO;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Khanh An on 12/23/17.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnPoiClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {


    View view;
    MapView mMapView;
    private GoogleMap mGoogleMap;
    MapScaleView scaleView;

    Marker moveFromMarker, moveToMarker;

    Marker currentSelectedMarker;

    LatLng uit = new LatLng(10.870249, 106.803735);

    List<Polyline> polylines = new ArrayList<>();

    //GPS
    private static final long UPDATE_INTERVAL = 5000;
    private static final long FASTEST_INTERVAL = 5000;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    LatLng lastGPSLocation;
    Circle myLocationCircle;
    Marker myLocationMarker;
    LocationManager locationManager;
    LocationListener locationListenerGPS;


    //SearchMarker
    List<Marker> searchMarker;

    //FavoriteMarker
    List<Marker> favoriteMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchMarker = new ArrayList<>();
        favoriteMarker = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.maps_fragment, container, false);

        mMapView = (MapView) view.findViewById(R.id.map_dashBoard);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        // mGoogleMap = mMapView.getMapAsync(this);

        scaleView = (MapScaleView) view.findViewById(R.id.scaleView);

        myLocationCircle= null;

        //GPS
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastGPSLocation = new LatLng(location.getLatitude(),location.getLongitude());

                if (myLocationMarker != null)
                    myLocationMarker.remove();

                boolean RemoveMenuFlag = false;
                if(GetCurrentMoveFromMarker() != null)
                {
                    if(((MarkerTagObject)GetCurrentMoveFromMarker().getTag())!= null)
                    {
                        if(((MarkerTagObject)GetCurrentMoveFromMarker().getTag()).getPlaceID().compareTo("MyLocation")  == 0)
                        {
                            SetMoveFromMarker(new MarkerTagObject("MyLocation"));

                            if(GetCurrentMoveToMarker() != null)
                                ((MainActivity)getActivity()).UpdateRoutingOnMap();

                            RemoveMenuFlag = true;
                        }
                    }


                }

                if(GetCurrentMoveToMarker() != null)
                {
                    if (((MarkerTagObject) GetCurrentMoveToMarker().getTag()) != null)
                    {
                        if (((MarkerTagObject) GetCurrentMoveToMarker().getTag()).getPlaceID().compareTo("MyLocation")  == 0)
                        {
                            SetMoveToMarker(new MarkerTagObject("MyLocation"));

                            if(GetCurrentMoveFromMarker() != null)
                                ((MainActivity)getActivity()).UpdateRoutingOnMap();

                            RemoveMenuFlag = true;
                        }
                    }
                }

                if(!RemoveMenuFlag)
                {
                    myLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                    myLocationMarker.setTag(new MarkerTagObject("MyLocation",myLocationMarker.getPosition()));
                }


                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .radius(location.getAccuracy())
                        .strokeWidth(2)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.parseColor("#500084d3"));

                if(myLocationCircle!= null)
                    myLocationCircle.remove();

                myLocationCircle = mGoogleMap.addCircle(circleOptions);

//                final CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
//                        .zoom(18)
//                        .bearing(90)
//                        .tilt(30)
//                        .build();
//
//                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return view;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, locationListenerGPS);
        isLocationEnabled();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        UpdateFavoriteList();

        //mGoogleMap.addMarker(new MarkerOptions().position(uit).title("Marker in UIT").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(uit)
                .zoom(18)
                .bearing(90)
                .tilt(30)
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnPoiClickListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnMarkerClickListener(this);

        scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentSelectedMarker != null)
            currentSelectedMarker.remove();

        ((MainActivity) getActivity()).OnMapClickedCallback();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (currentSelectedMarker != null) {
            currentSelectedMarker.remove();
            //currentSelectedMarker = null;
        }


        currentSelectedMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Selected").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));


        ((MainActivity) getActivity()).OnMapLongClickedCallback(latLng);
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {

        if (currentSelectedMarker != null) {
            if (currentSelectedMarker.getTag() != null)
                if (((MarkerTagObject) currentSelectedMarker.getTag()).getPlaceID().compareTo(pointOfInterest.placeId) == 0)
                    return;
        }


        ((MainActivity) getActivity()).OnPotClickedCallback(pointOfInterest);

        if (currentSelectedMarker != null)
            currentSelectedMarker.remove();

        currentSelectedMarker = mGoogleMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title("Marker in Selected").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
        currentSelectedMarker.setTag(new MarkerTagObject(pointOfInterest.placeId, pointOfInterest.latLng));
    }

    public void SetMoveFromMarker(MarkerTagObject markerObject) {
        if (moveFromMarker != null)
            ClearMoveFromMarker();

        if(markerObject.getPlaceID().compareTo("MyLocation") == 0)
        {
            moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(lastGPSLocation).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));
        }
        else
        {
            if(markerObject.getTag().compareTo("SearchResult") == 0)
            {

                markerObject.setTag("");
                for(int i = 0;i < searchMarker.size(); i++)
                {
                    if(markerObject.getLatLng().toString().compareTo(searchMarker.get(i).getPosition().toString()) == 0)
                    {
                        moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(searchMarker.get(i).getPosition()).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));

                        searchMarker.get(i).remove();

                        searchMarker.get(i).setTag(new MarkerTagObject(markerObject.getPlaceID(),markerObject.getLatLng(),"SearchResult"));
                        break;
                    }
                }

            }
            else
            {
//                if(markerObject.getTag().toString().compareTo("Favourite") == 0)
//                {
//                    for(int i = 0;i < favoriteMarker.size(); i++)
//                    {
//                        markerObject.setTag("");
//                        if(markerObject.getLatLng().toString().compareTo(favoriteMarker.get(i).getPosition().toString()) == 0)
//                        {
//                            moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(markerObject.getLatLng().latitude,markerObject.getLatLng().longitude)).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));
//
//                            favoriteMarker.get(i).remove();
//
//                            favoriteMarker.get(i).setTag(new MarkerTagObject(markerObject.getPlaceID(),markerObject.getLatLng(),"Favourite"));
//                            break;
//                        }
//                    }
//                }
                if(markerObject.getTag().toString().compareTo("Favourite") == 0)
                    moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(markerObject.getLatLng().latitude,markerObject.getLatLng().longitude)).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));
                else
                    moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(currentSelectedMarker.getPosition()).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));

                if (currentSelectedMarker != null)
                    currentSelectedMarker.remove();
            }

        }

        markerObject.setLatLng(moveFromMarker.getPosition());
        moveFromMarker.setTag(markerObject);
        moveFromMarker.setZIndex(999);



    }

    public void SetMoveToMarker(MarkerTagObject markerObject) {
        if (moveToMarker != null)
            ClearMoveToMarker();


        if(markerObject.getPlaceID().compareTo("MyLocation") == 0)
        {
            moveToMarker = mGoogleMap.addMarker(new MarkerOptions().position(lastGPSLocation).title("Move To").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_to)));
        }
        else
        {
            if(markerObject.getTag().compareTo("SearchResult") == 0)
            {

                markerObject.setTag("");
                for(int i = 0;i < searchMarker.size(); i++)
                {
                    if(markerObject.getLatLng().toString().compareTo(searchMarker.get(i).getPosition().toString()) == 0)
                    {
                        moveToMarker = mGoogleMap.addMarker(new MarkerOptions().position(searchMarker.get(i).getPosition()).title("Move To").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_to)));

                        searchMarker.get(i).remove();

                        searchMarker.get(i).setTag(new MarkerTagObject(markerObject.getPlaceID(),markerObject.getLatLng(),"SearchResult"));
                        break;
                    }
                }

            }
            else
            {
//                if(markerObject.getTag().toString().compareTo("Favourite") == 0)
//                {
//                    for(int i = 0;i < favoriteMarker.size(); i++)
//                    {
//                        markerObject.setTag("");
//                        if(markerObject.getLatLng().toString().compareTo(favoriteMarker.get(i).getPosition().toString()) == 0)
//                        {
//                            moveToMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(markerObject.getLatLng().latitude,markerObject.getLatLng().longitude)).title("Move To").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_to)));
//
//                            favoriteMarker.get(i).remove();
//
//                            favoriteMarker.get(i).setTag(new MarkerTagObject(markerObject.getPlaceID(),markerObject.getLatLng(),"Favourite"));
//                            break;
//                        }
//                    }
//                }
                if(markerObject.getTag().toString().compareTo("Favourite") == 0)
                    moveToMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(markerObject.getLatLng().latitude,markerObject.getLatLng().longitude)).title("Move To").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_to)));
                else
                    moveToMarker = mGoogleMap.addMarker(new MarkerOptions().position(currentSelectedMarker.getPosition()).title("Move To").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_to)));

                if (currentSelectedMarker != null)
                    currentSelectedMarker.remove();
            }

        }



        markerObject.setLatLng(moveToMarker.getPosition());
        moveToMarker.setTag(markerObject);
        moveToMarker.setZIndex(999);

    }

    public void OnBackPressedCallback() {
        if (currentSelectedMarker != null)
            currentSelectedMarker.remove();

        if (moveFromMarker != null)
            moveFromMarker.remove();
    }

    public Marker GetCurrentMoveFromMarker() {
        return this.moveFromMarker;
    }

    public Marker GetCurrentMoveToMarker() {
        return this.moveToMarker;
    }

    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.primary_dark_material_light};

    public void AddDirectionRoutes(ArrayList<Route> route, int shortestRouteIndex) {


        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mGoogleMap.addPolyline(polyOptions);
            polylines.add(polyline);


            //Toast.makeText(this.getActivity(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }


    public void ClearMoveFromMarker() {
        if (moveFromMarker != null)
        {
            if (((MarkerTagObject) moveFromMarker.getTag()) != null)
            {
                if (((MarkerTagObject) moveFromMarker.getTag()).getPlaceID().compareTo("MyLocation")  == 0)
                {
                    myLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lastGPSLocation.latitude, lastGPSLocation.longitude)).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                    myLocationMarker.setTag(new MarkerTagObject("MyLocation"));
                }


                for(int i = 0;i < searchMarker.size(); i++)
                {
                    if(moveFromMarker.getPosition().toString().compareTo(searchMarker.get(i).getPosition().toString()) == 0)
                    {
                        Marker temp = mGoogleMap.addMarker(new MarkerOptions().position(searchMarker.get(i).getPosition()).title("Searched").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue)));
                        temp.setTag(new MarkerTagObject(((MarkerTagObject)searchMarker.get(i).getTag()).getPlaceID(),((MarkerTagObject)searchMarker.get(i).getTag()).getLatLng(), "SearchResult"));

                        searchMarker.remove(i);
                        searchMarker.add(temp);

                        break;
                    }
                }

//                for(int i = 0;i < favoriteMarker.size(); i++)
//                {
//                    if(moveFromMarker.getPosition().toString().compareTo(favoriteMarker.get(i).getPosition().toString()) == 0)
//                    {
//                        Marker temp = mGoogleMap.addMarker(new MarkerOptions().position(favoriteMarker.get(i).getPosition()).title("Favourite").icon(BitmapDescriptorFactory.fromResource(R.drawable.star)));
//                        temp.setTag(new MarkerTagObject(((MarkerTagObject)favoriteMarker.get(i).getTag()).getPlaceID(),((MarkerTagObject)favoriteMarker.get(i).getTag()).getLatLng(), "Favourite"));
//
//                        favoriteMarker.remove(i);
//                        favoriteMarker.add(temp);
//
//                        break;
//                    }
//                }
            }


            moveFromMarker.remove();
            moveFromMarker = null;
        }

    }

    public void ClearMoveToMarker() {


        if (moveToMarker != null)
        {
            if (((MarkerTagObject) moveToMarker.getTag()) != null)
            {
                if (((MarkerTagObject) moveToMarker.getTag()).getPlaceID().compareTo("MyLocation")  == 0)
                {
                    myLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lastGPSLocation.latitude, lastGPSLocation.longitude)).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                    myLocationMarker.setTag(new MarkerTagObject("MyLocation"));
                }

                for(int i = 0;i < searchMarker.size(); i++)
                {
                    if(moveToMarker.getPosition().toString().compareTo(searchMarker.get(i).getPosition().toString()) == 0)
                    {
                        Marker temp = mGoogleMap.addMarker(new MarkerOptions().position(searchMarker.get(i).getPosition()).title("Searched").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue)));
                        temp.setTag(new MarkerTagObject(((MarkerTagObject)searchMarker.get(i).getTag()).getPlaceID(),((MarkerTagObject)searchMarker.get(i).getTag()).getLatLng(), "SearchResult"));

                        searchMarker.remove(i);
                        searchMarker.add(temp);

                        break;
                    }
                }

//                for(int i = 0;i < favoriteMarker.size(); i++)
//                {
//                    if(moveFromMarker.getPosition().toString().compareTo(favoriteMarker.get(i).getPosition().toString()) == 0)
//                    {
//                        Marker temp = mGoogleMap.addMarker(new MarkerOptions().position(favoriteMarker.get(i).getPosition()).title("Favourite").icon(BitmapDescriptorFactory.fromResource(R.drawable.star)));
//                        temp.setTag(new MarkerTagObject(((MarkerTagObject)favoriteMarker.get(i).getTag()).getPlaceID(),((MarkerTagObject)favoriteMarker.get(i).getTag()).getLatLng(), "Favourite"));
//
//                        favoriteMarker.remove(i);
//                        favoriteMarker.add(temp);
//
//                        break;
//                    }
//                }
            }

            moveToMarker.remove();
            moveToMarker= null;
        }

    }

    public void ClearAllDirection() {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
//        ClearMoveFromMarker();
//        ClearMoveToMarker();
    }

    @Override
    public void onCameraIdle() {
        scaleView.update(mGoogleMap.getCameraPosition().zoom, mGoogleMap.getCameraPosition().target.latitude);
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //if(currentSelectedMarker != null)
        {

        }
        //else
        //    ((MainActivity)getActivity()).OnMarkerClickCallback(marker.getTag().toString());

        if (currentSelectedMarker != null) {
            if (currentSelectedMarker.getTag() != null) {
                if (((MarkerTagObject) currentSelectedMarker.getTag()).getPlaceID().compareTo(((MarkerTagObject) marker.getTag()).getPlaceID()) == 0)
                    return true;
                else {
                    currentSelectedMarker.remove();

//                    if(((MarkerTagObject) marker.getTag()).getPlaceID().compareTo("MyLocation") == 0)
//                    {
//                        marker.showInfoWindow();
//                        return true;
//                    }
//                    else
                        ((MainActivity) getActivity()).OnMarkerClickCallback((MarkerTagObject) marker.getTag());
                }

            } else {

                        ((MainActivity) getActivity()).OnMarkerClickCallback((MarkerTagObject) marker.getTag());

            }

        }
        else
        {

                ((MainActivity) getActivity()).OnMarkerClickCallback((MarkerTagObject) marker.getTag());
        }

        return true;
    }

    public void ZoomIn() {
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    public void ZoomOut() {
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomOut());
    }


    private void isLocationEnabled() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = null;
            alertDialog = new AlertDialog.Builder(getActivity());

            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            //alert.show();
        }
    }

    public void UpdateMyLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListenerGPS, null);
    }

    public Marker GetCurrentSelectedMarker() {
        return this.currentSelectedMarker;
    }

    public void SetSearchMarker(List<MarkerTagObject> markerTagObjects)
    {
        if(searchMarker!=null)
        {
            for(Marker m : searchMarker)
            {
                m.remove();
            }

            searchMarker.clear();
        }

        for(MarkerTagObject markerTagObject : markerTagObjects)
        {
            Marker temp = mGoogleMap.addMarker(new MarkerOptions().position(markerTagObject.getLatLng()).title("Searched").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue)));
            temp.setTag(new MarkerTagObject(markerTagObject.getPlaceID(),markerTagObject.getLatLng(), "SearchResult"));
            temp.setZIndex(998);
            searchMarker.add(temp);
        }

        if(markerTagObjects.size() == 1)
        {
            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(searchMarker.get(0).getPosition())
                    .zoom(18)
                    .bearing(90)
                    .tilt(30)
                    .build();

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void ClearAllMarker() {
        for(Marker m : searchMarker)
        {
            m.remove();
        }

        searchMarker.clear();
    }

    public void UpdateFavoriteList()
    {
        for(Marker m : favoriteMarker)
        {
            m.remove();
        }

        favoriteMarker.clear();


        FavoritesDA favoritesDA = new FavoritesDA(getActivity());
        List<FavoritesDTO> favoritesDTOList = favoritesDA.GetFavorites();

        for(FavoritesDTO favoritesDTO : favoritesDTOList)
        {
            Marker temp = mGoogleMap.addMarker(new MarkerOptions().position(favoritesDTO.getLatLng()).title("Favorite").icon(BitmapDescriptorFactory.fromResource(R.drawable.star)));
            temp.setTag(new MarkerTagObject(favoritesDTO.getPlaceID(),favoritesDTO.getLatLng(), "Favourite"));
            favoriteMarker.add(temp);



//            if (moveFromMarker != null) {
//                if (((MarkerTagObject) moveFromMarker.getTag()) != null) {
//                        if(((MarkerTagObject) moveFromMarker.getTag()).getLatLng().toString().compareTo(temp.getPosition().toString()) == 0)
//                        {
//                            //moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(((MarkerTagObject) moveFromMarker.getTag()).getLatLng().latitude,((MarkerTagObject) moveFromMarker.getTag()).getLatLng().longitude)).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));
//
//                            temp.remove();
//
//                            temp.setTag(new MarkerTagObject(((MarkerTagObject) moveFromMarker.getTag()).getPlaceID(),((MarkerTagObject) moveFromMarker.getTag()).getLatLng(),"Favourite"));
//                            break;
//                        }
//                    }
//
//
//            }
//
//            if (moveToMarker != null) {
//                if (((MarkerTagObject) moveToMarker.getTag()) != null) {
//                    if(((MarkerTagObject) moveToMarker.getTag()).getLatLng().toString().compareTo(temp.getPosition().toString()) == 0)
//                    {
//                        //moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(((MarkerTagObject) moveFromMarker.getTag()).getLatLng().latitude,((MarkerTagObject) moveFromMarker.getTag()).getLatLng().longitude)).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));
//
//                        temp.remove();
//
//                        temp.setTag(new MarkerTagObject(((MarkerTagObject) moveToMarker.getTag()).getPlaceID(),((MarkerTagObject) moveToMarker.getTag()).getLatLng(),"Favourite"));
//                        break;
//                    }
//                }
//            }
//
//            if (currentSelectedMarker != null) {
//                if (((MarkerTagObject) currentSelectedMarker.getTag()) != null) {
//                    if(((MarkerTagObject) currentSelectedMarker.getTag()).getLatLng().toString().compareTo(temp.getPosition().toString()) == 0)
//                    {
//                        //moveFromMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(((MarkerTagObject) moveFromMarker.getTag()).getLatLng().latitude,((MarkerTagObject) moveFromMarker.getTag()).getLatLng().longitude)).title("Move From").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_move_from)));
//
//                        currentSelectedMarker.remove();
//                        break;
//                    }
//                }
//            }

        }
    }

    public LatLng GetCurrentCameraPos() {
        return mGoogleMap.getCameraPosition().target;
    }
}

