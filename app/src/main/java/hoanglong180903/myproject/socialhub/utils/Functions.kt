package hoanglong180903.myproject.socialhub.utils

import android.app.Dialog
import android.content.Context
import android.widget.LinearLayout
import hoanglong180903.myproject.socialhub.R

class Functions {
    companion object {
        fun showLoadingDialog(context: Context): Dialog {
            val loadingDialog = Dialog(context, R.style.DialogCustomTheme).apply {
                setContentView(R.layout.dialog_loading)
                window!!.setLayout(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                window!!.setBackgroundDrawableResource(android.R.color.transparent)
                setCancelable(false)
            }
            return loadingDialog
        }
    }
}