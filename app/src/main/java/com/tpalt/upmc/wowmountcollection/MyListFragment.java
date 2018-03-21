package com.tpalt.upmc.wowmountcollection;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vanessa on 17-Mar-18.
 */

public class MyListFragment extends ListFragment implements OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private List<Mount> mounts;
    private MountArrayAdapter adapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list, container, false);
        listView = view.findViewById(R.id.listView);
        mounts = mListener.getMounts();
        adapter =new MountArrayAdapter(getActivity(),R.layout.item, mounts);
        listView.setAdapter(adapter);

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

    public void refresh(){
       adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener{
        public List<Mount> getMounts();
        void onFragmentInteraction(Uri uri);
        void registerFragment(MyListFragment myListFragment);

    }
}