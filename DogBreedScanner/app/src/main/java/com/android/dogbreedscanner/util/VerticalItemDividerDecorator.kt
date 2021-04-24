package com.android.dogbreedscanner.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * <prev>
 * Created by Pedro Machado on 2021-04-24.
 * </prev>
 */
class VerticalItemDividerDecorator(
    private val verticalSpaceHeight: Int,
    private var addOnLastPosition: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val currentPosition = parent.getChildAdapterPosition(view)

        if (addOnLastPosition) {
            // add bottom space to all items
            outRect.bottom = verticalSpaceHeight
        } else if (currentPosition != parent.adapter!!.itemCount - 1) {
            // can't add space on last position
            outRect.bottom = verticalSpaceHeight
        }
    }
}