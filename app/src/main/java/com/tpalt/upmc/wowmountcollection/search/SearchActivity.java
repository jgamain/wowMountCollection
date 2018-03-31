package com.tpalt.upmc.wowmountcollection.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.tpalt.upmc.wowmountcollection.DetailsActivity;
import com.tpalt.upmc.wowmountcollection.fragments.BottomNavigationFragment;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.fragments.MountListFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements BottomNavigationFragment.OnFragmentInteractionListener
        ,MountListFragment.OnFragmentInteractionListener {

    private List<Mount> searchResult = new ArrayList<>();
    private MountListFragment mountListFragement;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        header = findViewById(R.id.search_header);

        initSearchView();

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if ("performSearch".equals(extras.getString("methodName"))){
                    performSearch();
                }
            }
        }
    }

    public void performSearch(){
        searchResult = SearchEngine.perform();
        Log.d("RESEARCH", "result: "+searchResult.size());
        mountListFragement.refresh();
        if(searchResult.size() > 1){
            header.setText("Search results: "+searchResult.size()+" hits");
        } else {
            header.setText("Search results: "+searchResult.size()+" hit");
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
    public List<Mount> getMountList() {
        return this.searchResult;
    }

    @Override
    public void registerFragment(MountListFragment fragment) {
        this.mountListFragement = fragment;
    }

    @Override
    public void setViewStatus(int status) {
        if(header != null) header.setVisibility(status);
    }

    @Override
    public void registerFragment(BottomNavigationFragment fragment) {
        fragment.setCurrentActivity(BottomNavigationFragment.NavBarItem.SEARCH);
    }

    private void initSearchView(){
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView) ;

        //init cursor adapter for suggestions
        String[] columnNames = {"_id","text"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        String[] from = {"text"};
        int[] to = {R.id.suggestion};
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.suggestion_entry, cursor, from, to);
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                MatrixCursor matrixCursor = new MatrixCursor(new String[] { "_id", "text" });
                String query = constraint.toString();
                if(TextUtils.isEmpty(query)) return matrixCursor;
                List<String> suggestions = SearchEngine.getSuggestions(query);

                for (int i = 0; i < suggestions.size(); i++) {
                    matrixCursor.addRow(new Object[]{(i + 1), suggestions.get(i)});
                }

                return matrixCursor;
            }
        });
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                String selectedMount = SearchEngine.getSuggestedMount(i);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("mountName",selectedMount);
                startActivity(intent);
                return true;
            }
        });

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        //init the searchView with the previous search
        searchView.setQuery(SearchEngine.name, false);
        performSearch();

/*
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchEngine.name = query;
            performSearch();
        }
*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                performSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchEngine.name = s;
                return false;
            }
        });
    }
}
