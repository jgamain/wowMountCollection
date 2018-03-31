package com.tpalt.upmc.wowmountcollection.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFillFragment;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WMCApplication;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener
        ,MountListFragment.OnFragmentInteractionListener {

    private List<Mount> searchResult = new ArrayList<>();
    private MountListFragment mountListFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResult = WMCApplication.getALLMountList();
        mountListFragement.refresh();

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView) ;
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery(SearchEngine.name, false);

/*
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchEngine.name = query;
            preformSearch();
        }
*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                preformSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchEngine.name = s;
                return false;
            }
        });


        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if ("preformSearch".equals(extras.getString("methodName"))){
                    preformSearch();
                }
            }
        }
    }

    public void preformSearch(){
        searchResult = SearchEngine.perform();
        Log.d("FRAGMENT", "result: "+searchResult.size());
        for(Mount m : searchResult){
            Log.d("RESEARCH", m.getName());
        }
        mountListFragement.refresh();
    }

    public void onFiltersClick(View view){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public List<Mount> getMountList() {
        return this.searchResult;
    }

    @Override
    public void registerFragment(MountListFragment fragment) {
        this.mountListFragement = fragment;
    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.SEARCH);
    }

}
