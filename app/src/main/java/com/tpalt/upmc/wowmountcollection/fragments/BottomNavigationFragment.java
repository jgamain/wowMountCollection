package com.tpalt.upmc.wowmountcollection.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tpalt.upmc.wowmountcollection.MyMountsActivity;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WishListActivity;
import com.tpalt.upmc.wowmountcollection.search.SearchActivity;
import com.tpalt.upmc.wowmountcollection.search.SearchEngine;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomNavigationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BottomNavigationFragment extends Fragment {

    public enum NavBarItem{
        MY_MOUNTS, SEARCH, WISH_LIST
    }

    private NavBarItem currentActivity;

    private OnFragmentInteractionListener mListener;

    public BottomNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        BottomNavigationView bottomBar = view.findViewById(R.id.bottom_navigation);
        switch (currentActivity){
            case MY_MOUNTS: bottomBar.setSelectedItemId(R.id.action_myMounts); break;
            case SEARCH: bottomBar.setSelectedItemId(R.id.action_search); break;
            case WISH_LIST: bottomBar.setSelectedItemId(R.id.action_wishlist); break;
        }

        bottomBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_myMounts:
                                if(currentActivity != NavBarItem.MY_MOUNTS){
                                    Log.d("MENU", "go to My mounts");
                                    Intent intent = new Intent(getContext(), MyMountsActivity.class);
                                    startActivity(intent);
                                    //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                break;
                            case R.id.action_search:
                                if(currentActivity != NavBarItem.SEARCH){
                                    Log.d("MENU", "go to Search");
                                    Intent intent = new Intent(getContext(), SearchActivity.class);
                                    startActivity(intent);
                                    //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                break;
                            case R.id.action_wishlist:
                                if(currentActivity != NavBarItem.WISH_LIST){
                                    Log.d("MENU", "go to Wish list");
                                    Intent intent = new Intent(getContext(), WishListActivity.class);
                                    startActivity(intent);
                                    //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                                break;
                        }
                        return true;
                    }
                });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.registerFragment(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void registerFragment(BottomNavigationFragment fragment);
    }

    public void setCurrentActivity(NavBarItem currentActivity){
        this.currentActivity = currentActivity;
    }
}
