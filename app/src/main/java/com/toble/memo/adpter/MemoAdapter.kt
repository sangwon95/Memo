package com.toble.memo.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toble.memo.databinding.MemoListItemBinding
import com.toble.memo.model.MemoListEventListener
import com.toble.memo.room.MemoEntity


class MemoAdapter(
    private var memoList: MutableList<MemoEntity>,
    private val memoListEventListener: MemoListEventListener,

    ): RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = MemoListItemBinding.inflate(inflater, parent, false)
        return MemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(memoList[position], position)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    fun removeDataAt(pos: Int) {
        memoList.removeAt(pos)
        notifyItemRemoved(pos)
        memoListEventListener.changedMemoListListener(memoList)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val movedItem = memoList.removeAt(fromPosition)
        memoList.add(toPosition, movedItem)
        memoListEventListener.changedMemoListListener(memoList)
    }

    inner class MemoViewHolder(private var binding: MemoListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: MemoEntity, position: Int){
//            if(position == 0){
                binding.titleTextView.text = memo.title
//                binding.swapImageView.visibility = View.GONE
//            } else {
//                binding.addressTextView.text = address
//            }
        }
    }
}