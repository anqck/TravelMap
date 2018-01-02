package com.n.travelmap;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;



import com.n.travelmap.R;
import com.roughike.bottombar.OnTabSelectListener;

public class TempActivity extends BaseActivity {

    LinearLayout dynamicContent,bottonNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_temp);

        //dynamically include the  current activity      layout into  baseActivity layout.now all the view of baseactivity is   accessible in current activity.
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        //BottomNavigationView bottomBar = (BottomNavigationView) findViewById(R.id.navigation);
        View wizard = getLayoutInflater().inflate(R.layout.activity_temp, null);
        dynamicContent.addView(wizard);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(int tabId) {
//
//            }
//        });
//        bottomBar.selectTabAtPosition(1);
//        bottomBar.setOnTabSelectListener(onTabSelectListener,false);

        bottomBar.selectTabAtPosition(1,true);
//        navigation.setOnNavigationItemSelectedListener(null);
//
//        navigation.setSelectedItemId(R.id.navigation_dashboard);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }



}
