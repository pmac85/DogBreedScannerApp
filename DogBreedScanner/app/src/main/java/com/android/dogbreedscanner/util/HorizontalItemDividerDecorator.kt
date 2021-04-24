package com.android.dogbreedscanner.util

import android.graphics.Rect
import android.view.View
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-24.
 * </prev>
 */
class HorizontalItemDividerDecorator(private val horizontalSpaceHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val isRtl =
            TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL

        val currentPosition = parent.getChildAdapterPosition(view)

        if (isRtl) {

            if (currentPosition != 0) {
                outRect.right = horizontalSpaceHeight
            }
        } else {
            if (currentPosition != parent.adapter!!.itemCount - 1) {
                outRect.right = horizontalSpaceHeight
            }
        }
    }
}