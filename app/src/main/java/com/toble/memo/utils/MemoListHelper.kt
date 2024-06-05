package com.toble.memo.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.toble.memo.adpter.MemoAdapter

class MemoListHelper(
    private var favoriteAdapter: MemoAdapter,
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

        // Exclude the first item from movement
        if (fromPosition == 0 || toPosition == 0) {
            return false
        }

        favoriteAdapter.moveItem(fromPosition, toPosition)

        //Collections.swap(updatedList, fromPosition, toPosition)
        recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.layoutPosition
        favoriteAdapter.removeDataAt(position)
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

        Log.d("TAG", actionState.toString())


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

            // 삭제 버튼을 그립니다.
            // 여기서는 간단하게 "Delete" 텍스트를 그렸습니다.
            paint.color = Color.WHITE
            paint.textSize = 40f
            val text = "삭제"
            val x = itemView.right - 150f
            val y = itemView.top + (itemView.height + paint.textSize) / 2
            c.drawText(text, x, y, paint)
        } else {

            itemView.translationX = 0f
            paint.color = Color.BLACK
            paint.textSize = 40f
            val text = ""
            val x = itemView.right - 150f
            val y = itemView.top + (itemView.height + paint.textSize) / 2
            c.drawText(text, x, y, paint)
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