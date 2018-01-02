package com.n.travelmap.Activity.SearchActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import  com.n.travelmap.BaseActivity;
import com.n.travelmap.Database.SavedPlace;
import com.n.travelmap.Database.SearchHistoryDA;
import com.n.travelmap.Library.PlaceAPI.NRPlaces;
import com.n.travelmap.Library.PlaceAPI.Place;
import com.n.travelmap.Library.PlaceAPI.PlacesException;
import com.n.travelmap.Library.PlaceAPI.PlacesListener;
import com.n.travelmap.Library.PlaceAPI.SearchType;
import com.n.travelmap.MarkerTagObject;
import com.n.travelmap.R;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Khanh An on 01/02/18.
 */

public class SearchActivity extends BaseActivity {
    String TAG = "TAG";

    EditText editText;
    String mCurrentFragmentOnScreen ;
    SearchMainMenuFragment fragA;
    SearchAutoCompleteFragment fragB;
    NearbyResultFragment fragC;

    Button btnFilter;
    int currentFilter;
    CharSequence items[] ;

    LinearLayout searchToolbar, showOnMap;

    LinearLayout dynamicContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);

        //dynamically include the  current activity      layout into  baseActivity layout.now all the view of baseactivity is   accessible in current activity.
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        //BottomNavigationView bottomBar = (BottomNavigationView) findViewById(R.id.navigation);
        View wizard = getLayoutInflater().inflate(R.layout.activity_search, null);
        dynamicContent.addView(wizard);

        //NOTE
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bottomBar.selectTabAtPosition(1,true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentFragmentOnScreen = "MAIN_MENU";


        searchToolbar = findViewById(R.id.search_toolbar);
        showOnMap  = findViewById(R.id.show_on_map_layout);
        showOnMap.setVisibility(View.INVISIBLE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragA = new SearchMainMenuFragment();
        fragB =  new SearchAutoCompleteFragment();
        fragC =  new NearbyResultFragment();

        fragmentTransaction.add(R.id.contain, fragA, "MAIN_MENU");
        fragmentTransaction.add(R.id.contain,fragB, "SEARCH_AUTOCOMPLETE");
        fragmentTransaction.add(R.id.contain,fragC, "NEARBY_RESULT");
        fragmentTransaction.commit();


        editText = (EditText) findViewById(R.id.search_text);
        editText.clearFocus();
//        ((LinearLayout) findViewById(R.id.contain)).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                editText.clearFocus();
//                hideKeyboard(v);
//                return true;
//            }
//        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after == 0 && mCurrentFragmentOnScreen.compareTo("SEARCH_AUTOCOMPLETE") == 0)
                {
                    switchToA();
                }
                else
                {
                    switchToB();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() != 0)
                    fragB.UpdateOnTextChange(s.toString(),null);
            }
        });

//        currentFilter = 0;
//       items = new CharSequence[] {"Tất cả", "Ẩm thực", "Khách sạn", "Mua sắm","Xe buýt", "Cafe","Gym", "ATM", "Sân bay", "Bệnh viện", "Rạp phim","Công viên","Spa","Sở thú","Sòng bạc"};
//
//
//        btnFilter = (Button) findViewById(R.id.btn_filter);
//        btnFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder adb = new AlertDialog.Builder(SearchActivity.this);
//
//                adb.setSingleChoiceItems(items, currentFilter, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface d, int n) {
//                        currentFilter = n;
//                        adb.setSingleChoiceItems(items, currentFilter,this);
//                        d.dismiss();
//                    }
//
//                });
//               ;
//                adb.setNegativeButton("Hủy bỏ ", null);
//                adb.setTitle("Chọn bộ lọc:");
//                adb.show();
//            }
//        });
//        final MyAutoCompleteTextView autocompleteView = (MyAutoCompleteTextView) findViewById(R.id.autocomplete);
//        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));
//        autocompleteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 switchToB();
//            }
//        });
//        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PlacesAutoCompleteAdapter adapter = (PlacesAutoCompleteAdapter) parent.getAdapter();
//                //Toast.makeText(getBaseContext(),adapter.getItem(position).toString(),Toast.LENGTH_SHORT);
//                if(adapter.getPlace(0).getName().compareTo("My Location") == 0)
//                {
//
//                }
//                else
//                {
//                    SearchHistoryDA searchHistoryDA = new SearchHistoryDA(getBaseContext());
//                    searchHistoryDA.AddSearchHistory(adapter.getPlace(position));
//                    //searchHistoryDA.GetSearchHistory();
//                }
//
//            }
//        });
//        autocompleteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(autocompleteView.getText().toString().compareTo("") == 0)
//                {
//
//                    autocompleteView.showDropDown();
//                }
//            }
//        });

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            if(mCurrentFragmentOnScreen.compareTo("MAIN_MENU") == 0)
            {

            }
            else if(mCurrentFragmentOnScreen.compareTo("NEARBY_RESULT") == 0)
            {
                searchToolbar.setVisibility(View.VISIBLE);
                showOnMap.setVisibility(View.INVISIBLE);
                switchToA();
            }
            else
            {
                editText.setText("");
                //switchToA();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragmentOnScreen.compareTo("MAIN_MENU") == 0)
        {

        }
        else if(mCurrentFragmentOnScreen.compareTo("NEARBY_RESULT") == 0)
        {
            searchToolbar.setVisibility(View.VISIBLE);
            showOnMap.setVisibility(View.INVISIBLE);
            switchToA();

        }
        else
        {
            editText.setText("");
            //switchToA();
        }
    }

    private void switchToA() {

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.detach(getSupportFragmentManager().findFragmentByTag("SEARCH_AUTOCOMPLETE"));
//        fragmentTransaction.attach(fragA);
//        fragmentTransaction.addToBackStack("MENU_TAG");
//
//        fragmentTransaction.commit();
//        getSupportFragmentManager().executePendingTransactions();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("MAIN_MENU") != null)
        {
            //if the fragment exists, show it.
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("MAIN_MENU")).commit();
        }
        else
        {
            //if the fragment does not exist, add it to fragment manager.
            fragmentManager.beginTransaction().add(R.id.container, new SearchAutoCompleteFragment(), "MAIN_MENU").commit();
        }

        if(fragmentManager.findFragmentByTag("SEARCH_AUTOCOMPLETE") != null){
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("SEARCH_AUTOCOMPLETE")).commit();
        }

        if(fragmentManager.findFragmentByTag("NEARBY_RESULT") != null){
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("NEARBY_RESULT")).commit();
        }
        mCurrentFragmentOnScreen = "MAIN_MENU";



        //fragA.RefeshView();
    }

    private void switchToB() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("SEARCH_AUTOCOMPLETE") != null) {
            //if the fragment exists, show it.
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("SEARCH_AUTOCOMPLETE")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            fragmentManager.beginTransaction().add(R.id.container, new SearchAutoCompleteFragment(), "SEARCH_AUTOCOMPLETE").commit();
        }
        if(fragmentManager.findFragmentByTag("MAIN_MENU") != null){
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("MAIN_MENU")).commit();
        }

        if(fragmentManager.findFragmentByTag("NEARBY_RESULT") != null){
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("NEARBY_RESULT")).commit();
        }

//
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.detach(getSupportFragmentManager().findFragmentByTag("MAIN_MENU"));
//        fragmentTransaction.attach(fragB);
//        fragmentTransaction.addToBackStack(null);
//
//        fragmentTransaction.commitAllowingStateLoss();
//
//        getSupportFragmentManager().executePendingTransactions();
//
        mCurrentFragmentOnScreen = "SEARCH_AUTOCOMPLETE";


    }

    private void switchToC() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchToolbar.setVisibility(View.INVISIBLE);
                showOnMap.setVisibility(View.VISIBLE);
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("NEARBY_RESULT") != null) {
            //if the fragment exists, show it.
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("NEARBY_RESULT")).commit();
        } else {
            //if the fragment does not exist, add it to fragment manager.
            fragmentManager.beginTransaction().add(R.id.container, new SearchAutoCompleteFragment(), "NEARBY_RESULT").commit();
        }
        if(fragmentManager.findFragmentByTag("MAIN_MENU") != null){
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("MAIN_MENU")).commit();
        }

        if(fragmentManager.findFragmentByTag("SEARCH_AUTOCOMPLETE") != null){
            //if the other fragment is visible, hide it.
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("SEARCH_AUTOCOMPLETE")).commit();
        }

        mCurrentFragmentOnScreen = "NEARBY_RESULT";

    }

    public void onSearchItemClick(final SearchPlaceObject searchPlaceObject) {
        editText.setText("");

        //Get Img
        final GeoDataClient mGeoDataClient=   Places.getGeoDataClient(this, null);

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(searchPlaceObject.getPlaceID());

        mGeoDataClient.getPlaceById(searchPlaceObject.getPlaceID()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {

            }
        });

        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();


                if(photoMetadataBuffer.getCount() != 0)
                {
                    // Get the first photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getScaledPhoto(photoMetadata,75,75);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            //Bitmap bitmap = photo.getBitmap();

                            SearchHistoryDA searchHistoryDA = new SearchHistoryDA(SearchActivity.this);
                            searchHistoryDA.AddSearchHistory(new SavedPlace(searchPlaceObject,new LatLng(0,0),photo.getBitmap()));
                        }
                    });
                }
                else
                {
                    SearchHistoryDA searchHistoryDA = new SearchHistoryDA(SearchActivity.this);
                    searchHistoryDA.AddSearchHistory(new SavedPlace(searchPlaceObject,new LatLng(0,0),null));
                }


            }

        });

    }

    AlertDialog.Builder builder;
    boolean dialogShown;
    public void OnNearbyMenuItemClick(String s) {


        new NRPlaces.Builder().searchtype(SearchType.NearbySearch)
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {

                        if(e.getMessage().compareTo("ZERO_RESULTS") == 0)
                        {
                            SearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    if(dialogShown)
                                    {
                                        return;
                                    }
                                    else
                                    {
                                        dialogShown = true;
                                        builder = new AlertDialog.Builder(SearchActivity.this);
                                        builder.setTitle("Lỗi")
                                                .setMessage("Không có kết quả")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialogShown = false;
                                                    }
                                                }).create().show();
                                    }


                                }
                            });

                        }

                    }

                    @Override
                    public void onPlacesStart() {

                    }

                    @Override
                    public void onPlacesSuccess(List<Place> places) {
                        switchToC();

                        fragC.ShowResult(places);

                    }

                    @Override
                    public void onPlacesFinished() {

                    }
                })
                .key(getString(R.string.google_maps_key))
                .latlng(10.870249,106.803735)
                .type(s)
                .radius(1500)
                .build()
                .execute();
    }

    public void ReturnResult(List<MarkerTagObject> result) {
        Intent resultIntent = new Intent(this,SearchActivity.class);

        //resultIntent.putExtra("LIST PLACES", "AAAA");
       resultIntent.putExtra("LIST PLACES", (Serializable) result);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
