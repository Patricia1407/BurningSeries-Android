package de.datlag.burningseries.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.datlag.burningseries.R
import de.datlag.burningseries.common.expand
import de.datlag.burningseries.common.isTelevision
import de.datlag.burningseries.common.safeContext
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicReference

class LoadingDialog private constructor(context: Context) : BottomSheetDialog(context, R.style.BottomSheetDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (context.packageManager.isTelevision()) {
            setOnShowListener {
                it.expand()
            }
        }

        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setOnCancelListener {
            currentInstance.compareAndSet(this, null)
        }
        setOnDismissListener {
            cancel()
        }

        setContentView(R.layout.dialog_loading)
    }

    companion object {
        private val currentInstance: AtomicReference<LoadingDialog?> = AtomicReference(null)

        private fun getInstance(context: Context): LoadingDialog {
            return nullableInstance ?: run {
                val newDialog = LoadingDialog(context)
                while (!currentInstance.compareAndSet(null, newDialog)) {
                    dismiss()
                }
                nullableInstance ?: newDialog
            }
        }

        private val nullableInstance
            get() = currentInstance.get()

        fun show(context: Context) {
            val instance = getInstance(context)
            if (instance.isShowing) {
                dismiss()
                instance.dismiss()
            }
            instance.show()
        }

        fun dismiss() {
            nullableInstance?.dismiss()
            nullableInstance?.cancel()
        }
    }
}