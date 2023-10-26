package com.example.e_kart.dialog

import androidx.fragment.app.Fragment
import com.example.e_kart.R
import com.example.e_kart.databinding.ResetPasswordDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setUpBootomSheetDialog(
    onSendClick :(String) -> Unit
){

    val dialog=BottomSheetDialog(requireContext(), R.style.DialogStyle)
    var bottomSheetBinding:ResetPasswordDialogBinding = ResetPasswordDialogBinding.inflate(layoutInflater)
    dialog.setContentView(bottomSheetBinding.root)
    dialog.behavior.state= BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    bottomSheetBinding.sendButton.setOnClickListener {
        val email=bottomSheetBinding.etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }
    bottomSheetBinding.cancelButton.setOnClickListener {
        dialog.dismiss()
    }

}

