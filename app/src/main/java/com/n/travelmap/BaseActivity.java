package com.n.travelmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;

public class BaseActivity extends AppCompatActivity {

    private TextView mTextMessage;
    protected  com.roughike.bottombar.BottomBar bottomBar;

    Intent mainActivity;


    ProgressDialog waitDialog;

    protected  OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(int tabId) {
            bottomBar.removeOnTabSelectListener();
            bottomBar.selectTabAtPosition(0,false);
           // bottomBar.selectTabWithId(R.id.tab_main);

            bottomBar.setOnTabSelectListener(this,false);
        }
//            switch (tabId)
//            {
//                case R.id.tab_main:
//                    finish();
//                    overridePendingTransition(0, 0);
//                    break;
//                case R.id.tab_favorites:
//                    in =new Intent(getBaseContext(),TempActivity.class);
//
//
//                    bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//                        @Override
//                        public void onTabSelected(int tabId) {
//
//                        }
//                    });
//
//                    bottomBar.selectTabAtPosition(0,false);
//                    bottomBar.setOnTabSelectListener(this,false);
//
//
//                    startActivity(in);
//                    overridePendingTransition(0, 0);
//
//                    break;
//            }
//        }
    };


    public final static int REQ_CODE_CHILD = 1;

    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mTextMessage = (TextView) findViewById(R.id.message);
        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //searchActivity = new Intent(this,SearchFragment.class);

        //searchActivity = findViewById(R.id.search_activity);

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_main);
        bottomBar.setTabSelectionInterceptor(new TabSelectionInterceptor() {
            @Override
            public boolean shouldInterceptTabSelection(int oldTabId, int newTabId) {
                if(oldTabId == newTabId)
                    return true;

                switch (newTabId)
                {
                    case R.id.tab_main:
//                        Intent resultIntent = new Intent();
//
//                        setResult(RESULT_CANCELED, resultIntent);
//                        finish();
//                        overridePendingTransition(0, 0);
                        OnMainTabClick();

                        return false;
                    case R.id.tab_search:
                        OnSearchTabClick();

                        //SwitchToSearchActivity();

                        return false;

                    case R.id.tab_favorites:
                        OnFavoritesTabClick();
//                        in =new Intent(getBaseContext(),TempActivity.class);
//
//
//                        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//                            @Override
//                            public void onTabSelected(int tabId) {
//
//                            }
//                        });
//
//
//                        startActivityForResult(in,REQ_CODE_CHILD);
//                        overridePendingTransition(0, 0);

                        return false;
                }

                return false;
            }
        });
        //bottomBar.setOnTabSelectListener(onTabSelectListener,false);

        //mainActivity  = new Intent(getBaseContext(),MainActivity.class);
        //mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    protected void OnFavoritesTabClick() {
    }


    protected void OnMainTabClick()
    {

    }


    protected void OnSearchTabClick()
    {

    }

    public void SwitchToSearchActivity() {
        OnSearchTabClick();

    }

    public  com.roughike.bottombar.BottomBar getBottomBar()
    {
        return bottomBar;
    }
}
