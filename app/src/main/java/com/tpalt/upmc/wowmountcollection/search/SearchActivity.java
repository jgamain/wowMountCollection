package com.tpalt.upmc.wowmountcollection.search;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.tpalt.upmc.wowmountcollection.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.MountApplication;
import com.tpalt.upmc.wowmountcollection.MountArrayAdapter;
import com.tpalt.upmc.wowmountcollection.MyListFragment;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WMCApplication;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener
        /*,MyListFragment.OnFragmentInteractionListener*/
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView) ;
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchEngine.name = query;
            preformSearch();
        }

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.getString("methodName").equals("preformSearch")){
                    preformSearch();
                }
            }
        }
    }

    public void preformSearch(){
        List<Mount> result = SearchEngine.perform();
        Log.d("RESEARCH", "result: "+result.size());
        for(Mount m : result){
            Log.d("RESEARCH", m.getName());
        }
    }

    public void onFiltersClick(View view){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.SEARCH);
    }




    /*MyListFragment.OnFragmentInteractionListener
    @Override
    public List<Mount> getMounts() {
        return ((MountApplication)getApplication()).getMounts();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void registerFragment(MyListFragment myListFragment) {

    }
    */
}
