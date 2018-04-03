package com.tpalt.upmc.wowmountcollection.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;

import java.util.List;


/**
 * Fragment for the ListView (without heart icon).
 * (used in my mounts activity)
 */
public class MountListFragment extends Fragment {
    protected OnFragmentInteractionListener mListener;
    protected ListView listView;
    protected List<Mount> mountList;
    protected SimpleMountArrayAdapterFill adapter;

    public MountListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_mount_list, container, false);
        listView = view.findViewById(R.id.listView);
        listView.setOnScrollListener(onScrollListener());
        mountList = mListener.getMountList();
        setAdapter();
        return view;
    }

    protected void setAdapter(){
        adapter = new SimpleMountArrayAdapterFill(
                getContext(),
                R.layout.list_item,
                mountList);

        listView.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private void refreshMounts(){
        adapter.clear();
        mountList.clear();
        mountList.addAll(mListener.getMountList());
    }

    public void refresh(){
        refreshMounts();
        adapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
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
    public interface OnFragmentInteractionListener {        // TODO: Update argument type and name

        void onFragmentInteraction(Uri uri);
        List<Mount> getMountList();
        void registerFragment(MountListFragment fragment);
        void setViewStatus(int status);
    }

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = listView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the top: visible header and footer
                        Log.d("SCROLL", "top reached");
                        mListener.setViewStatus(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem){
                    // on scrolling
                    mListener.setViewStatus(View.GONE);
                    Log.d("SCROLL", "on scroll");
                }
            }
        };
    }
}
