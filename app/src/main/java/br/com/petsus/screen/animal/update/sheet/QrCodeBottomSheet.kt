package br.com.petsus.screen.animal.update.sheet

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.print.PrintHelper
import br.com.petsus.R
import br.com.petsus.databinding.FragmentQrCodeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QrCodeBottomSheet(
    private val qrCode: Bitmap,
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentQrCodeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentQrCodeBinding.bind(view).apply {
            qrCodeImage.setImageBitmap(qrCode)
            printImage.setOnClickListener {
                activity?.also { context ->
                    PrintHelper(context).apply {
                        scaleMode = PrintHelper.SCALE_MODE_FIT
                        printBitmap(context.getString(R.string.qr_code), qrCode)
                    }
                }
            }
        }

        dialog?.setOnDismissListener {
            qrCode.recycle()
        }
    }

}