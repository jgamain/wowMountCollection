package com.tpalt.upmc.wowmountcollection;

import android.app.Activity;
import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomNavigationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BottomNavigationFragment extends Fragment {

    public enum NavBarItem{
        MY_MOUNTS, SEARCH, FAVORITES
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

        ((BottomNavigationView)view.findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_myMounts:
                                if(currentActivity != NavBarItem.MY_MOUNTS){
                                    Log.d("MENU", "go to My mounts");
                                }
                                break;
                            case R.id.action_search:
                                if(currentActivity != NavBarItem.SEARCH){
                                    Log.d("MENU", "go to Search");
                                }
                                break;
                            case R.id.action_favorites:
                                if(currentActivity != NavBarItem.FAVORITES){
                                    Log.d("MENU", "go to Favorites");
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
        void onFragmentInteraction(Uri uri);

        void registerFragment(BottomNavigationFragment fragment);
    }

    public void setCurrentActivity(NavBarItem currentActivity){
        this.currentActivity = currentActivity;
    }
}
