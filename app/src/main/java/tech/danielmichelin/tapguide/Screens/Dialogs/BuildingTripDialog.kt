package tech.danielmichelin.tapguide.Screens.Dialogs

import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import tech.danielmichelin.tapguide.R


/**
 * Created by Daniel on 11/27/2017.
 */
class BuildingTripDialog: DialogFragment() {
    lateinit var listener: BuildingTripDialogCancelListener

    interface BuildingTripDialogCancelListener {
        fun onDialogCancel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity.layoutInflater
        listener = activity as BuildingTripDialogCancelListener
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_building_trip, null))
                .setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, id -> dialog.cancel()})
        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        listener.onDialogCancel()
    }

}