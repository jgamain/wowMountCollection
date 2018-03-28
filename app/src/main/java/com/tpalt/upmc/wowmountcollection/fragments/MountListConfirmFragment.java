package com.tpalt.upmc.wowmountcollection.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;

import java.util.List;

/**
 * Created by Vaniisha on 28-Mar-18.
 */

public class MountListConfirmFragment extends Fragment {

    private MountListConfirmFragment.OnFragmentInteractionListener mListener;
    private ListView listView;
    private List<Mount> mountList;
    private SimpleMountArrayAdapterConfirm adapter;

    public MountListConfirmFragment() {
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

        mountList = mListener.getMountList();
        Log.d("CONFIRM WISH size = ", " "+ mountList.size());
        adapter = new SimpleMountArrayAdapterConfirm(
                getContext(),
                R.layout.list_item,
                mountList);

        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MountListConfirmFragment.OnFragmentInteractionListener) {
            mListener = (MountListConfirmFragment.OnFragmentInteractionListener) context;
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
        void registerFragment(MountListConfirmFragment fragment);
    }
}