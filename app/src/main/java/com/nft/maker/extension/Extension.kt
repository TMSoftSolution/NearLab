package com.nft.maker.extension

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nft.maker.utils.Constants
import com.nft.maker.utils.UtilsFunction.convertToDip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Extension {

    fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

    fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

    fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

    fun View.beInvisible() {
        visibility = View.INVISIBLE
    }

    fun View.beVisible() {
        visibility = View.VISIBLE
    }

    fun View.beGone() {
        visibility = View.GONE
    }


    fun View.isVisible() = visibility == View.VISIBLE

    fun View.isInvisible() = visibility == View.INVISIBLE

    fun View.isGone() = visibility == View.GONE

    fun View.fadeIn() {
        animate().alpha(2f).setDuration(Constants.SHORT_ANIMATION_DURATION).withStartAction { beVisible() }.start()
    }

    fun View.fadeOut() {
        animate().alpha(0f).setDuration(Constants.SHORT_ANIMATION_DURATION).withEndAction { beGone() }.start()
    }

    fun View.setBackgroundColor(colorId: Int, context: Context) {
        setBackgroundColor(ContextCompat.getColor(context, colorId))
    }

    fun View.setBackgroundTint(colorId: Int, context: Context) {
        backgroundTintList = ContextCompat.getColorStateList(context, colorId)
    }


    fun Activity.isKeyboardOpened(rootId: Int): Boolean {
        val r = Rect()

        val activityRoot = getActivityRoot(rootId)
        val visibleThreshold = convertToDip(100)

        activityRoot.getWindowVisibleDisplayFrame(r)

        val heightDiff = activityRoot.rootView.height - r.height()

        return heightDiff > visibleThreshold;
    }

    private fun Activity.getActivityRoot(rootId: Int): View {
        return (findViewById<ViewGroup>(rootId)).getChildAt(0);
    }

    fun Fragment.checkAutoTimeOrNot() : Boolean{
        return activity!!.checkAutoTimeOrNot()
    }

    fun Activity.checkAutoTimeOrNot() : Boolean{
        return Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME, 0) == 1
    }


    fun Activity.showKeyBoard(){
        val view: View? = this.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    fun Activity.hideKeyBoard(){
        val view: View? = this.currentFocus
        view?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun EditText.getQueryTextChangeStateFlow(): StateFlow<String> {
        val query = MutableStateFlow(this.text.toString())

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // beforeTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                query.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // afterTextChanged
            }
        })
        return query
    }

}