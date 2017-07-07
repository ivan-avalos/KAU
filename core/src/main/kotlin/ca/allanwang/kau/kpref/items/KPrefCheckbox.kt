package ca.allanwang.kau.kpref.items

import android.view.View
import android.widget.CheckBox
import ca.allanwang.kau.R
import ca.allanwang.kau.kpref.KPrefMarker
import ca.allanwang.kau.utils.tint

/**
 * Created by Allan Wang on 2017-06-07.
 *
 * Checkbox preference
 * When clicked, will toggle the preference and the apply the result to the checkbox
 */
open class KPrefCheckbox(builder: BaseContract<Boolean>) : KPrefItemBase<Boolean>(builder) {

    override fun defaultOnClick(itemView: View, innerContent: View?): Boolean {
        pref = !pref
        (innerContent as CheckBox).isChecked = pref
        return true
    }

    override fun onPostBindView(viewHolder: ViewHolder, textColor: Int?, accentColor: Int?) {
        super.onPostBindView(viewHolder, textColor, accentColor)
        val checkbox = viewHolder.bindInnerView<CheckBox>(R.layout.kau_preference_checkbox)
        if (accentColor != null) checkbox.tint(accentColor)
        checkbox.isChecked = pref
        checkbox.jumpDrawablesToCurrentState() //Cancel the animation
    }

    override fun getType(): Int = R.id.kau_item_pref_checkbox

}