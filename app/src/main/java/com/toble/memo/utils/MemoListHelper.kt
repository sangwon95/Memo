package com.toble.memo.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.toble.memo.adapter.MemoAdapter

class MemoListHelper(
    private var memoAdapter: MemoAdapter,
    private var removeEvent: (Int) -> Unit
): ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
) {
    private val paint = Paint()
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition

        memoAdapter.moveItem(fromPosition, toPosition)

        //Collections.swap(updatedList, fromPosition, toPosition)
        //recyclerView.adapter!!.

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.layoutPosition
        memoAdapter.removeDataAt(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        if (dX > -150.0f && !isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 스와이프 중일 때만 배경과 삭제 버튼을 그립니다.
            // 빨강 배경을 그립니다.
            paint.color = Color.RED
            c.drawRect(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                paint
            )

            if(dX < -150.0f) {
                // 삭제 버튼을 그립니다.
                // 여기서는 간단하게 "Delete" 텍스트를 그렸습니다.
                paint.color = Color.WHITE
                paint.textSize = 40f
                val text = "삭제"
                val x = itemView.right - 150f
                val y = itemView.top + (itemView.height + paint.textSize) / 2
                c.drawText(text, x, y, paint)
            }
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}