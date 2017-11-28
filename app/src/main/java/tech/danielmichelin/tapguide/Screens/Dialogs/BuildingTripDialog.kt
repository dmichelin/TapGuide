package tech.danielmichelin.tapguide.Screens.Dialogs

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.R.string.cancel
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import tech.danielmichelin.tapguide.R


/**
 * Created by Daniel on 11/27/2017.
 */
class BuildingTripDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity.layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_building_trip, null))
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, id -> dialog.cancel()})
        return builder.create()
    }
}