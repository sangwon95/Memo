package com.toble.memo.adpter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toble.memo.databinding.MemoListItemBinding
import com.toble.memo.model.MemoItemClickListener
import com.toble.memo.room.MemoEntity


class MemoAdapter(
    private var memoList: MutableList<MemoEntity>,
    private val memoItemClickListener: MemoItemClickListener,

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
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val movedItem = memoList.removeAt(fromPosition)
        memoList.add(toPosition, movedItem)
        for(i in memoList){
            Log.d("로그", "moveItem: ${i.content}")
        }
    }



    fun add(memoEntity: MemoEntity) {
        memoList.add(memoEntity)
        notifyItemInserted(memoList.size - 1)
    }

    fun edit(position: Int, memoEntity: MemoEntity) {
        memoList[position] = memoEntity
        notifyItemChanged(position)
    }

    inner class MemoViewHolder(private var binding: MemoListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: MemoEntity, position: Int){
            binding.titleTextView.text = memo.content
            binding.memoItemRootLayout.setOnClickListener{
                memoItemClickListener.memoItemClickEvent(memo.content, position)
            }

        }
    }
}