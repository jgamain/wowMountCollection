package com.tpalt.upmc.wowmountcollection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import com.tpalt.upmc.wowmountcollection.Mount;
import com.tpalt.upmc.wowmountcollection.R;
import com.tpalt.upmc.wowmountcollection.WMCApplication;

import java.util.List;

/**
 * Created by Vaniisha on 27-Mar-18.
 */

public class SimpleMountArrayAdapterConfirm extends SimpleMountArrayAdapterFill {

    public SimpleMountArrayAdapterConfirm(@NonNull Context context, int resource, @NonNull List<Mount> objects) {
        super(context, resource, objects);
    }

    @Override
    protected void setHeartStatus(final Mount item, ViewHolder holder){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setMessage(R.string.confirmation_delete);
        alert.setTitle(R.string.alert_dialog_deleting);
        alert.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {
                        dialog.cancel();
                    }
                });

        alert.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {

                        WMCApplication.removeFromWishList(item);
                        notifyDataSetChanged();
                    }
                });
        alert.create().show(); // btw show() creates and shows it..
    }
}
