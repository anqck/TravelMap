package com.n.travelmap.Activity.SearchActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n.travelmap.R;

/**
 * Created by Khanh An on 12/20/17.
 */

public class SearchMainMenuFragment extends Fragment {
    View view;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;

    SearchHistoryFragment searchHistoryFragment;

    public SearchMainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.search_mainmenu_fragment, container, false);

         viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

         tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view ;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        searchHistoryFragment = new SearchHistoryFragment();
        adapter.addFrag(searchHistoryFragment, "LỊCH SỬ");
        adapter.addFrag(new NearbyMenuFragment(), "KHÁC");

//        adapter.addFrag(new FourFragment(), "FOUR");
//        adapter.addFrag(new FiveFragment(), "FIVE");
//        adapter.addFrag(new SixFragment(), "SIX");
//        adapter.addFrag(new SevenFragment(), "SEVEN");
//        adapter.addFrag(new EightFragment(), "EIGHT");
//        adapter.addFrag(new NineFragment(), "NINE");
//        adapter.addFrag(new TenFragment(), "TEN");

        viewPager.setAdapter(adapter);
    }

    public void RefeshView() {
//        view.invalidate();
//        view.requestLayout();

        adapter.notifyDataSetChanged();
       // adapter.notifyAll();
    }

    public  void UpdateHistoryList()
    {
        searchHistoryFragment.UpdateHistoryList();
    }
}