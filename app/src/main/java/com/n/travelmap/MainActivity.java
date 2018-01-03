package com.n.travelmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.github.clans.fab.FloatingActionMenu;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.n.travelmap.Activity.SearchActivity.SearchFragment;
import com.n.travelmap.Library.BottomSheetInfomation.BottomInformationSheetController;
import com.n.travelmap.Library.BottomSheetInfomation.BottomSheetBehaviorGoogleMapsLike;
import com.n.travelmap.Library.BottomSheetInfomation.BottomSheetInformation;
import com.n.travelmap.Library.BottomSheetInfomation.ItemPagerAdapter;
import com.n.travelmap.Library.BottomSheetInfomation.MergedAppBarLayoutBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    //Actionbar
    ActionBarState actionBarState;
    ActionBar actionBar;
    LinearLayout actionBar_search,actionBar_showonmap,actionBar_direction;
    private EditText searchText;
    private Button btnShowOnMap;

    //MapFragment
    MapsFragment mapsFragment;


    //SelectPlaceMenu
    SelectPlaceMenuFragment selectPlaceMenuFragment;

    //FAB button
    private FloatingActionMenu floatingActionMenu;
    FloatingActionButton floatingActionButton;
    FloatingActionButton btnCarMode, btnWalkMode, btnBikingMode;
    FABProgressCircle btnCarModeCircle, btnWalkModeCircle, btnBikingModeCircle;
    boolean mLockChange;

    //BottomSheet
    boolean flagShowInfo;
    ViewPager viewPager;
    NestedScrollView bottomSheet;
    BottomSheetBehaviorGoogleMapsLike behavior;
    BottomInformationSheetController bottomInformationSheetController;
    BottomSheetInformation bottomSheetInformation;


    //Places API clients
    protected GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;

    //DirectionMode
    private DirectionMenuFragment directionMenu;
    private boolean mIsOnDirectionMode, haveResult;
    private LatLng mMoveFromPos, mMoveToPos;
    AbstractRouting.TravelMode mTravelMode;

    LinearLayout dynamicContent;


    //SearchFragment
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //dynamically include the  current activity      layout into  baseActivity layout.now all the view of baseactivity is   accessible in current activity.
        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);

        View wizard = getLayoutInflater().inflate(R.layout.activity_main, null);
        dynamicContent.addView(wizard);


        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_activity_a);

        flagShowInfo = false;
        mIsOnDirectionMode = false;
        haveResult = false;

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.mainFab);
        floatingActionMenu.setClosedOnTouchOutside(true);


        //Google Place API Instance
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        //Actionnar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(" ");
        }
        actionBar.hide();
        actionBar_search = findViewById(R.id.search_toolbar);
        actionBar_direction = findViewById(R.id.actionbar_direction);
        actionBar_showonmap = findViewById(R.id.show_on_map_layout);
        searchText = findViewById(R.id.search_text);
        searchText.addTextChangedListener(searchFragment.getT());
        btnShowOnMap = findViewById(R.id.btn_show_on_map);
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.OnShowOnMapButtonClick();
            }
        });


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabDirection);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectPlaceMenuFragment)floatingActionButton.getTag()).OnMoveToButton();
            }
        });

        floatingActionButton.hide();

        behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        if (mIsOnDirectionMode) {
                            actionBar.show();

                        }
                        floatingActionMenu.hideMenu(true);
                        Log.d("bottomsheet-", "STATE_COLLAPSED");

                        floatingActionButton.show();

                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        if (mIsOnDirectionMode) {
                            actionBar.hide();

                            //directionMenu.HideMenu();
                        }


                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:


                        Log.d("bottomsheet-", "STATE_EXPANDED");

                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:


                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        floatingActionMenu.showMenu(true);
                        floatingActionButton.hide();


                        //fabDirectionButton.hide();
                        //SetDefaultBottomSheetImg();
                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        AppBarLayout mergedAppBarLayout = (AppBarLayout) findViewById(R.id.merged_appbarlayout);

        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle(" ");
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
            }
        });

        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

        List<Bitmap> img = new ArrayList<>();
        ItemPagerAdapter adapter = new ItemPagerAdapter(this, img);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        bottomInformationSheetController = new BottomInformationSheetController(bottomSheet, viewPager);

        selectPlaceMenuFragment = (SelectPlaceMenuFragment) getSupportFragmentManager().findFragmentById(R.id.select_place_menu);
        floatingActionButton.setTag(selectPlaceMenuFragment);
        //bottomMenuFragment = (direction_menu_fragment) getSupportFragmentManager().findFragmentById(R.id.bottom_menu);
        mapsFragment = (MapsFragment) getFragmentManager().findFragmentById(R.id.map_fragment);


        mTravelMode = AbstractRouting.TravelMode.DRIVING;
        mLockChange = false;

        btnCarMode = findViewById(R.id.btn_mode_car);
        btnWalkMode = findViewById(R.id.btn_mode_walk);
        btnBikingMode = findViewById(R.id.btn_mode_biking);

        btnCarModeCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        btnWalkModeCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircleWalk);
        btnBikingModeCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircleBiking);

        btnCarMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        btnCarMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));


        btnCarMode.setOnClickListener(TravelModeButtonOnClick());
        btnWalkMode.setOnClickListener(TravelModeButtonOnClick());
        btnBikingMode.setOnClickListener(TravelModeButtonOnClick());

        directionMenu = (DirectionMenuFragment) getSupportFragmentManager().findFragmentById(R.id.direction_place_menu);

        com.github.clans.fab.FloatingActionButton fabZoomIn = findViewById(R.id.fab_zoom_in);
        fabZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsFragment.ZoomIn();
            }
        });

        com.github.clans.fab.FloatingActionButton fabZoomOut = findViewById(R.id.fab_zoom_out);
        fabZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsFragment.ZoomOut();
            }
        });

        com.github.clans.fab.FloatingActionButton fabMyLocation = findViewById(R.id.fabLocation);
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsFragment.UpdateMyLocation();
            }
        });

        com.github.clans.fab.FloatingActionButton fabClearMap = findViewById(R.id.fabClearMap);
        fabClearMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsFragment.ClearAllMarker();
            }
        });

    }

    private View.OnClickListener TravelModeButtonOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLockChange)
                    return;
                switch (v.getId()) {
                    case R.id.btn_mode_car:


                        if (mTravelMode == AbstractRouting.TravelMode.DRIVING) {

                        } else {


                            mTravelMode = AbstractRouting.TravelMode.DRIVING;

                            btnCarMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                            btnCarMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

                            btnWalkMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                            btnWalkMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                            btnBikingMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                            btnBikingMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                            if (mMoveFromPos != null && mMoveToPos != null) {
                                UpdateRoutingOnMap();

                            }
                        }


                        break;
                    case R.id.btn_mode_walk:
                        if (mTravelMode == AbstractRouting.TravelMode.WALKING) {

                        } else {
                            mTravelMode = AbstractRouting.TravelMode.WALKING;

                            btnCarMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                            btnCarMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                            btnWalkMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                            btnWalkMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

                            btnBikingMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                            btnBikingMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                            if (mMoveFromPos != null && mMoveToPos != null) {
                                UpdateRoutingOnMap();

                            }
                        }
                        break;
                    case R.id.btn_mode_biking:
                        if (mTravelMode == AbstractRouting.TravelMode.BIKING) {

                        } else {
                            mTravelMode = AbstractRouting.TravelMode.BIKING;

                            btnCarMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                            btnCarMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                            btnWalkMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                            btnWalkMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                            btnBikingMode.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                            btnBikingMode.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

                            if (mMoveFromPos != null && mMoveToPos != null) {
                                UpdateRoutingOnMap();

                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void OnMapClickedCallback() {
        Log.d("MAP:", "Clicked!");

        selectPlaceMenuFragment.HideMenu();
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
        bottomBar.setVisibility(View.VISIBLE);

        flagShowInfo = false;

        if (mIsOnDirectionMode) {
            if (mMoveToPos == null)
                directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedFinishPos);
            else if (mMoveFromPos == null)
                directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedStartPos);
            else if (haveResult)
                directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.Result);
        }

    }

    public void OnMapLongClickedCallback() {
        flagShowInfo = true;

        Log.d("MAP:", "Long Clicked!");


        //behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
        bottomBar.setVisibility(View.INVISIBLE);
        directionMenu.HideMenu();



        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(mapsFragment.GetCurrentSelectedMarker().getPosition().latitude, mapsFragment.GetCurrentSelectedMarker().getPosition().longitude, 1);

            Address obj = address.get(0);

            //latLng = new LatLng(obj.getLatitude(), obj.getLongitude());

            final String addressText = String.format("%s, %s", obj.getMaxAddressLineIndex() > 0 ? obj.getAddressLine(0) : "",
                    obj.getCountryName());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < obj.getMaxAddressLineIndex(); i++) {
                sb.append(obj.getAddressLine(i));
            }

            bottomSheetInformation = new BottomSheetInformation();

            // obj.getAddressLine(0).replaceAll(obj.getFeatureName(), "").substring(1);

            bottomInformationSheetController.SetBottomSheetTitle(obj.getFeatureName());
            bottomInformationSheetController.SetBottomSheetSubTitle(obj.getAddressLine(0).replaceAll(obj.getFeatureName(), "").substring(1));

            bottomInformationSheetController.SetInfoText1(obj.getAddressLine(0).toString());

            bottomInformationSheetController.SetInfoText2(getResources().getString(R.string.NoInformation));
            bottomInformationSheetController.SetInfoText3(getResources().getString(R.string.NoInformation));

            bottomInformationSheetController.UpdateImageAdapter(bottomSheetInformation);

            selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Normal, new MarkerTagObject("",new LatLng(obj.getLatitude(),obj.getLongitude())));
            if (flagShowInfo)
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Normal, null);
        //behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
    }

    public void OnPotClickedCallback(PointOfInterest pointOfInterest) {
        flagShowInfo = true;

        Log.d("MAP:", "Poi Clicked!");
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
        bottomBar.setVisibility(View.INVISIBLE);
        directionMenu.HideMenu();
        selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Normal, new MarkerTagObject(pointOfInterest.placeId,pointOfInterest.latLng) );

        bottomSheetInformation = new BottomSheetInformation();

        //Get Photo
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(pointOfInterest.placeId);


        mGeoDataClient.getPlaceById(pointOfInterest.placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();

                    com.google.android.gms.location.places.Place myPlace = (com.google.android.gms.location.places.Place) places.get(0);

                    bottomInformationSheetController.SetBottomSheetTitle(myPlace.getName().toString());
                    bottomInformationSheetController.SetBottomSheetSubTitle(myPlace.getAddress().toString());

                    Log.i("MAIN", "Place found: " + myPlace.getName());


                    bottomInformationSheetController.SetInfoText1(myPlace.getAddress().toString());

                    if (myPlace.getPhoneNumber().toString().compareTo("") != 0)
                        bottomInformationSheetController.SetInfoText2(myPlace.getPhoneNumber().toString());
                    else
                        bottomInformationSheetController.SetInfoText2(getResources().getString(R.string.NoInformation));

                    if (myPlace.getWebsiteUri() != null)
                        bottomInformationSheetController.SetInfoText3(myPlace.getWebsiteUri().toString());
                    else
                        bottomInformationSheetController.SetInfoText3(getResources().getString(R.string.NoInformation));
                    //myPlace.get

                    if (flagShowInfo)
                        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

                    places.release();
                } else {
                    Log.e("MAIN", "Place not found.");
                }

                photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        // Get the list of photos.
                        PlacePhotoMetadataResponse photos = task.getResult();
                        // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        bottomSheetInformation.setPlacePhotoMetadataBuffer(photoMetadataBuffer);

                        for (int i = 0; i < bottomSheetInformation.getPlacePhotoMetadataBuffer().getCount(); i++) {
                            if (i >= 4)
                                break;

                            final PlacePhotoMetadata photoMetadata = bottomSheetInformation.getPlacePhotoMetadataBuffer().get(i);
                            Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getScaledPhoto(photoMetadata, 640, 480);

                            photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                    PlacePhotoResponse photo = task.getResult();
                                    //Bitmap bitmap = photo.getBitmap();

                                    //bottomInformationSheetController.SetImage(photo.getBitmap());

                                    bottomSheetInformation.getListImage().add(photo.getBitmap());
                                    bottomInformationSheetController.UpdateImageAdapter(bottomSheetInformation);

                                }
                            });
                        }
                    }

                });


            }
        });


    }

    public void OnSetPlaceMoveFrom(MarkerTagObject placeId) {
        //UI
        mIsOnDirectionMode = true;
        selectPlaceMenuFragment.HideMenu();
        bottomSheet.fullScroll(View.FOCUS_UP);
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

        SetActionBarState(ActionBarState.DirectionMode);
        actionBar.show();

        mapsFragment.SetMoveFromMarker(placeId);

        bottomBar.setVisibility(View.VISIBLE);
        //bottomMenuFragment.OnSetMoveFrom();
        haveResult = false;

        if (mMoveFromPos == mapsFragment.GetCurrentMoveFromMarker().getPosition())
            return;
        else
            mMoveFromPos = mapsFragment.GetCurrentMoveFromMarker().getPosition();


        if (mMoveToPos != null) {
            if (mMoveToPos.toString().compareTo(mMoveFromPos.toString()) == 0) {
                mMoveToPos = null;
                mapsFragment.ClearMoveToMarker();
            }
        }


        if (mMoveFromPos != null && mMoveToPos != null) {
            UpdateRoutingOnMap();
            //directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.Result);

        } else {
            directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedFinishPos);
        }

    }


    public void OnSetPlaceMoveTo(MarkerTagObject placeId) {
        //UI
        mIsOnDirectionMode = true;
        selectPlaceMenuFragment.HideMenu();
        bottomSheet.fullScroll(View.FOCUS_UP);
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

        SetActionBarState(ActionBarState.DirectionMode);
        actionBar.show();

        mapsFragment.SetMoveToMarker(placeId);

        haveResult = false;

        bottomBar.setVisibility(View.VISIBLE);
        //bottomMenuFragment.OnSetMoveTo();

        if (mMoveToPos == mapsFragment.GetCurrentMoveToMarker().getPosition())
            return;
        else
            mMoveToPos = mapsFragment.GetCurrentMoveToMarker().getPosition();

        if (mMoveFromPos != null) {
            if (mMoveToPos.toString().compareTo(mMoveFromPos.toString()) == 0) {
                mMoveFromPos = null;
                mapsFragment.ClearMoveFromMarker();
            }
        }

        if (mMoveFromPos != null && mMoveToPos != null) {

            UpdateRoutingOnMap();
            //directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.Result);
        } else {
            directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedStartPos);
        }

    }

    public void UpdateRoutingOnMap() {

        mapsFragment.ClearAllDirection();

        switch (mTravelMode) {
            case DRIVING:
                btnCarModeCircle.show();
                break;
            case WALKING:
                btnWalkModeCircle.show();
                break;
            case BIKING:
                btnBikingModeCircle.show();
                break;
            default:

                break;
        }

        Routing routing = new Routing.Builder()
                .travelMode(mTravelMode)
                .withListener(new RoutingListener() {
                    @Override
                    public void onRoutingFailure(RouteException e) {
                        switch (mTravelMode) {
                            case DRIVING:
                                btnCarModeCircle.hide();
                                break;
                            case WALKING:
                                btnWalkModeCircle.hide();
                                break;
                            case BIKING:
                                btnBikingModeCircle.hide();
                                break;
                            default:
                                break;
                        }

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(MainActivity.this);
                        }

                        builder.setTitle("Không thể xác định tuyến đường")
                                .setMessage(" Không thể tạo tuyến đường.\n Hãy điều chỉnh điểm bắt đầu hoặc điểm đến của bạn.")
                                .setPositiveButton("THOÁT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    @Override
                    public void onRoutingStart() {

                    }

                    @Override
                    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
                        haveResult = true;

                        mapsFragment.AddDirectionRoutes(route, shortestRouteIndex);

                        directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.Result, route.get(0).getDistanceText(), route.get(0).getDurationText());

                        switch (mTravelMode) {
                            case DRIVING:
                                btnCarModeCircle.hide();
                                break;
                            case WALKING:
                                btnWalkModeCircle.hide();
                                break;
                            case BIKING:
                                btnBikingModeCircle.hide();
                                break;
                            default:
                                break;
                        }


                    }

                    @Override
                    public void onRoutingCancelled() {

                    }
                })
                .waypoints(mapsFragment.GetCurrentMoveFromMarker().getPosition(), mapsFragment.GetCurrentMoveToMarker().getPosition())
                .key(getResources().getString(R.string.google_maps_key))
                .build();
        routing.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Log.d("MAIN", "BACK PRESSED");

            switch (actionBarState)
            {
                case DirectionMode:
                    haveResult = false;
                    mIsOnDirectionMode = false;
                    actionBar.hide();
                    SetActionBarState(ActionBarState.Normal);
                    mapsFragment.OnBackPressedCallback();

                    bottomBar.setVisibility(View.VISIBLE);

                    selectPlaceMenuFragment.HideMenu();
                    behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

                    mMoveFromPos = null;
                    mMoveToPos = null;

                    mapsFragment.ClearAllDirection();
                    mapsFragment.ClearMoveToMarker();
                    mapsFragment.ClearMoveFromMarker();

                    directionMenu.HideMenu();
                    break;
                case SearchMode_SearchToolbar:
                    getBottomBar().selectTabAtPosition(0,true);
                    OnMainTabClick();
                case SearchMode_ShowOnMap:
                    searchFragment.switchToA();

            }



        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (mIsOnDirectionMode) {
            haveResult = false;
            mIsOnDirectionMode = false;
            actionBar.hide();
            SetActionBarState(ActionBarState.Normal);

            mapsFragment.OnBackPressedCallback();

            bottomBar.setVisibility(View.VISIBLE);

            selectPlaceMenuFragment.HideMenu();

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

            mMoveFromPos = null;
            mMoveToPos = null;

            mapsFragment.ClearAllDirection();
            mapsFragment.ClearMoveToMarker();
            mapsFragment.ClearMoveFromMarker();

            directionMenu.HideMenu();
        } else
            super.onBackPressed();
    }



    public void OnMarkerClickCallback(MarkerTagObject markerTagObject) {
        if(markerTagObject == null)
            return;

        if (markerTagObject.getPlaceID().compareTo("") == 0) {
            flagShowInfo = true;

            Log.d("MAP:", "Marker Clicked!");

            //behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
            bottomBar.setVisibility(View.INVISIBLE);
            directionMenu.HideMenu();


            selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Remove, markerTagObject);


            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> address = geocoder.getFromLocation(markerTagObject.getLatLng().latitude, markerTagObject.getLatLng().longitude, 1);

                Address obj = address.get(0);

                //latLng = new LatLng(obj.getLatitude(), obj.getLongitude());

                final String addressText = String.format("%s, %s", obj.getMaxAddressLineIndex() > 0 ? obj.getAddressLine(0) : "",
                        obj.getCountryName());

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < obj.getMaxAddressLineIndex(); i++) {
                    sb.append(obj.getAddressLine(i));
                }

                bottomSheetInformation = new BottomSheetInformation();

                // obj.getAddressLine(0).replaceAll(obj.getFeatureName(), "").substring(1);

                bottomInformationSheetController.SetBottomSheetTitle(obj.getFeatureName());
                bottomInformationSheetController.SetBottomSheetSubTitle(obj.getAddressLine(0).replaceAll(obj.getFeatureName(), "").substring(1));

                bottomInformationSheetController.SetInfoText1(obj.getAddressLine(0).toString());

                bottomInformationSheetController.SetInfoText2(getResources().getString(R.string.NoInformation));
                bottomInformationSheetController.SetInfoText3(getResources().getString(R.string.NoInformation));

                bottomInformationSheetController.UpdateImageAdapter(bottomSheetInformation);

                if (flagShowInfo)
                    behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (markerTagObject.getPlaceID().compareTo("MyLocation") == 0) {
            boolean RemoveMenuFlag = false;
            if (mapsFragment.GetCurrentMoveFromMarker() != null) {
                if (((MarkerTagObject) mapsFragment.GetCurrentMoveFromMarker().getTag()) != null) {
                    if (((MarkerTagObject) mapsFragment.GetCurrentMoveFromMarker().getTag()).getPlaceID().compareTo("MyLocation") == 0) {
                        selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Remove, markerTagObject);
                        RemoveMenuFlag = true;
                    }
                }


            }

            if (mapsFragment.GetCurrentMoveToMarker() != null) {
                if (((MarkerTagObject) mapsFragment.GetCurrentMoveToMarker().getTag()) != null) {
                    if (((MarkerTagObject) mapsFragment.GetCurrentMoveToMarker().getTag()).getPlaceID().compareTo("MyLocation") == 0) {
                        selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Remove, markerTagObject);
                        RemoveMenuFlag = true;
                    }
                }
            }

            if (!RemoveMenuFlag)
                selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Normal, markerTagObject);


            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
            bottomBar.setVisibility(View.INVISIBLE);
            directionMenu.HideMenu();
            bottomSheetInformation = new BottomSheetInformation();
            ;
        } else {
            Log.d("MAP:", "Poi Clicked!");
            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

            if(markerTagObject.getTag().compareTo("SearchResult") == 0)
                selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Normal, markerTagObject);
            else
                selectPlaceMenuFragment.ShowMenu(SelectPlaceMenuFragment.SelectPlaceMenuState.Remove, markerTagObject);

            bottomBar.setVisibility(View.INVISIBLE);
            directionMenu.HideMenu();
            bottomSheetInformation = new BottomSheetInformation();
            ;

            //Get Photo
            final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(markerTagObject.getPlaceID());


            mGeoDataClient.getPlaceById(markerTagObject.getPlaceID()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                    if (task.isSuccessful()) {
                        PlaceBufferResponse places = task.getResult();

                        com.google.android.gms.location.places.Place myPlace = (com.google.android.gms.location.places.Place) places.get(0);

                        bottomInformationSheetController.SetBottomSheetTitle(myPlace.getName().toString());
                        bottomInformationSheetController.SetBottomSheetSubTitle(myPlace.getAddress().toString());

                        Log.i("MAIN", "Place found: " + myPlace.getName());

                        bottomInformationSheetController.SetInfoText1(myPlace.getAddress().toString());

                        if (myPlace.getPhoneNumber().toString().compareTo("") != 0)
                            bottomInformationSheetController.SetInfoText2(myPlace.getPhoneNumber().toString());
                        else
                            bottomInformationSheetController.SetInfoText2(getResources().getString(R.string.NoInformation));

                        if (myPlace.getWebsiteUri() != null)
                            bottomInformationSheetController.SetInfoText3(myPlace.getWebsiteUri().toString());
                        else
                            bottomInformationSheetController.SetInfoText3(getResources().getString(R.string.NoInformation));
                        //myPlace.get

                        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

                        places.release();
                    } else {
                        Log.e("MAIN", "Place not found.");
                    }

                    photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                            // Get the list of photos.
                            PlacePhotoMetadataResponse photos = task.getResult();
                            // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                            PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                            bottomSheetInformation.setPlacePhotoMetadataBuffer(photoMetadataBuffer);

                            if(bottomSheetInformation.getPlacePhotoMetadataBuffer().getCount() == 0)
                            {
                                Bitmap icon = BitmapFactory.decodeResource(getResources(),   R.drawable.no_image);
                                bottomSheetInformation.getListImage().add(icon);
                                bottomInformationSheetController.UpdateImageAdapter(bottomSheetInformation);

                            }
                            else
                            {
                                for (int i = 0; i < bottomSheetInformation.getPlacePhotoMetadataBuffer().getCount(); i++) {
                                    if (i >= 4)
                                        break;

                                    final PlacePhotoMetadata photoMetadata = bottomSheetInformation.getPlacePhotoMetadataBuffer().get(i);
                                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getScaledPhoto(photoMetadata, 640, 480);

                                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                                        @Override
                                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                            PlacePhotoResponse photo = task.getResult();
                                            //Bitmap bitmap = photo.getBitmap();

                                            //bottomInformationSheetController.SetImage(photo.getBitmap());

                                            bottomSheetInformation.getListImage().add(photo.getBitmap());
                                            bottomInformationSheetController.UpdateImageAdapter(bottomSheetInformation);

                                        }
                                    });
                                }
                            }

                        }

                    });


                }
            });
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        // bottomBar.selectTabAtPosition(1,false);
        //bottomBar.selectTabAtPosition(0, true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_CHILD) {

//            Bundle bundle = data.getExtras();
//            if (bundle != null)
//            {
//                List<MarkerTagObject> myObject = (List<MarkerTagObject>) bundle.getSerializable("LIST PLACES");
//                mapsFragment.SetSearchMarker(myObject);
//            }

            //int c = 0;
        }
    }

    public void OnSearchResultReturn(List<MarkerTagObject> result)
    {
        if(result.size() > 0)
            mapsFragment.SetSearchMarker(result);
    }

    public void DirectionRemovePlace(MarkerTagObject markerTagObject) {
        if (mapsFragment.GetCurrentMoveToMarker() != null) {
            if (mapsFragment.GetCurrentMoveToMarker().getTag() != null) {
                if (((MarkerTagObject) mapsFragment.GetCurrentMoveToMarker().getTag()).getPlaceID().compareTo(markerTagObject.getPlaceID()) == 0) {
                    if (((MarkerTagObject) mapsFragment.GetCurrentMoveToMarker().getTag()).getPlaceID().compareTo("") == 0) {
                        if (((MarkerTagObject) mapsFragment.GetCurrentMoveToMarker().getTag()).getLatLng().toString().compareTo(markerTagObject.getLatLng().toString()) == 0) {
                            selectPlaceMenuFragment.HideMenu();
                            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
                            bottomBar.setVisibility(View.VISIBLE);

                            flagShowInfo = false;


                            mapsFragment.ClearAllDirection();
                            mapsFragment.ClearMoveToMarker();

                            mMoveToPos = null;

                            if (mMoveFromPos != null) {
                                directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedFinishPos);
                            } else {
                                haveResult = false;
                                mIsOnDirectionMode = false;

                                actionBar.hide();
                                SetActionBarState(ActionBarState.Normal);
                                mapsFragment.OnBackPressedCallback();


                                mapsFragment.ClearAllDirection();
                                mapsFragment.ClearMoveToMarker();
                                mapsFragment.ClearMoveFromMarker();

                                directionMenu.HideMenu();
                            }
                        }

                    } else {
                        selectPlaceMenuFragment.HideMenu();
                        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
                        bottomBar.setVisibility(View.VISIBLE);

                        flagShowInfo = false;


                        mapsFragment.ClearAllDirection();
                        mapsFragment.ClearMoveToMarker();

                        mMoveToPos = null;

                        if (mMoveFromPos != null) {
                            directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedFinishPos);
                        } else {
                            haveResult = false;
                            mIsOnDirectionMode = false;
                            actionBar.hide();
                            SetActionBarState(ActionBarState.Normal);
                            mapsFragment.OnBackPressedCallback();


                            mapsFragment.ClearAllDirection();
                            mapsFragment.ClearMoveToMarker();
                            mapsFragment.ClearMoveFromMarker();

                            directionMenu.HideMenu();
                        }
                    }
                }
            }
        }

        if (mapsFragment.GetCurrentMoveFromMarker() != null) {
            if (mapsFragment.GetCurrentMoveFromMarker().getTag() != null) {
                if (((MarkerTagObject) mapsFragment.GetCurrentMoveFromMarker().getTag()).getPlaceID().compareTo(markerTagObject.getPlaceID()) == 0) {
                    if (((MarkerTagObject) mapsFragment.GetCurrentMoveFromMarker().getTag()).getPlaceID().compareTo("") == 0) {
                        if (((MarkerTagObject) mapsFragment.GetCurrentMoveFromMarker().getTag()).getLatLng().toString().compareTo(markerTagObject.getLatLng().toString()) == 0) {
                            selectPlaceMenuFragment.HideMenu();
                            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
                            bottomBar.setVisibility(View.VISIBLE);

                            flagShowInfo = false;

                            mapsFragment.ClearAllDirection();
                            mapsFragment.ClearMoveFromMarker();


                            mMoveFromPos = null;

                            if (mMoveToPos != null) {
                                directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedStartPos);
                            } else {
                                haveResult = false;
                                mIsOnDirectionMode = false;
                                actionBar.hide();
                                mapsFragment.OnBackPressedCallback();


                                mapsFragment.ClearAllDirection();
                                mapsFragment.ClearMoveToMarker();
                                mapsFragment.ClearMoveFromMarker();

                                directionMenu.HideMenu();
                            }
                        }

                    } else {
                        selectPlaceMenuFragment.HideMenu();
                        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
                        bottomBar.setVisibility(View.VISIBLE);

                        flagShowInfo = false;

                        mapsFragment.ClearAllDirection();
                        mapsFragment.ClearMoveFromMarker();


                        mMoveFromPos = null;

                        if (mMoveToPos != null) {
                            directionMenu.ShowMenu(DirectionMenuFragment.DirectionMenuState.NeedStartPos);
                        } else {
                            haveResult = false;
                            mIsOnDirectionMode = false;
                            actionBar.hide();
                            mapsFragment.OnBackPressedCallback();


                            mapsFragment.ClearAllDirection();
                            mapsFragment.ClearMoveToMarker();
                            mapsFragment.ClearMoveFromMarker();

                            directionMenu.HideMenu();
                        }


                    }

                }
            }
        }
    }


    //BottomMenu
    @Override
    public void OnSearchTabClick()
    {
        searchFragment.ShowView();

        if(searchFragment.getmCurrentFragmentOnScreen().compareTo("NEARBY_RESULT") == 0)
        {
            SetActionBarState(ActionBarState.SearchMode_ShowOnMap);
        }
        else
            SetActionBarState(ActionBarState.SearchMode_SearchToolbar);

        actionBar.show();

        searchText.clearFocus();

        floatingActionMenu.hideMenu(false);
    }

    @Override
    public void OnMainTabClick()
    {
        searchFragment.HideView();

        SetActionBarState(ActionBarState.Normal);
        actionBar.hide();

        floatingActionMenu.showMenu(true);
    }



    //ActionBar
    public enum ActionBarState
    {
        Normal,
        DirectionMode,
        SearchMode_SearchToolbar,
        SearchMode_ShowOnMap
    }


    public void SetActionBarState(ActionBarState state)
    {
        actionBarState = state;
        switch ( state)
        {
            case Normal:
                actionBar_showonmap.setVisibility(View.INVISIBLE);
                actionBar_direction.setVisibility(View.INVISIBLE);
                actionBar_search.setVisibility(View.INVISIBLE);
                break;
            case DirectionMode:
                actionBar_showonmap.setVisibility(View.INVISIBLE);
                actionBar_direction.setVisibility(View.VISIBLE);
                actionBar_search.setVisibility(View.INVISIBLE);
                break;
            case SearchMode_ShowOnMap:
                actionBar_showonmap.setVisibility(View.VISIBLE);
                actionBar_direction.setVisibility(View.INVISIBLE);
                actionBar_search.setVisibility(View.INVISIBLE);
                break;
            case SearchMode_SearchToolbar:
                actionBar_showonmap.setVisibility(View.INVISIBLE);
                actionBar_direction.setVisibility(View.INVISIBLE);
                actionBar_search.setVisibility(View.VISIBLE);
                break;
        }
    }

    public EditText getSearchText()
    {
        return  searchText;
    }

    public SearchFragment getSearchFragment()
    {
        return searchFragment;
    }
}



