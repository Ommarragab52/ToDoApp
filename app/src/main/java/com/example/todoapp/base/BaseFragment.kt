package com.example.todoapp.base

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    private var progressDialog: ProgressDialog? = null
    private var alertDialogBuilder: AlertDialog.Builder? = null
    fun showLoadingDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Loading...")
        progressDialog?.show()
    }

    fun hideLoadingDialog() {
        progressDialog?.hide()
    }
    var alertDialog : AlertDialog?=null
    fun showMessage(
        message: String,
        posActionTitle: String,
        posAction: DialogInterface.OnClickListener? = null,
        negActionTitle: String,
        negAction: DialogInterface.OnClickListener? = null,
        cancelable: Boolean = true
    ) {
        alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder?.setMessage(message)
        if (posActionTitle != null) {
            alertDialogBuilder!!.setPositiveButton(
                posActionTitle,
                posAction ?: DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        }
        if (negActionTitle != null) {
            alertDialogBuilder!!.setNegativeButton(negActionTitle,negAction?:DialogInterface.OnClickListener{
                dialog, which ->   dialog.dismiss()
            })
        }
        alertDialogBuilder!!.setCancelable(cancelable)
        alertDialog = alertDialogBuilder!!.show()
    }
}