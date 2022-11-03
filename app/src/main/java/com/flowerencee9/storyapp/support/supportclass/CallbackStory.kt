package com.flowerencee9.storyapp.support.supportclass

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.flowerencee9.storyapp.models.response.Story

class CallbackStory(private val oldList: ArrayList<Story>, private val newList: ArrayList<Story>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }
    override fun areContentsTheSame(oldCourse: Int, newPosition: Int): Boolean {
        val (_, value0, nameOld) = oldList[oldCourse]
        val (_, value1, nameNew) = newList[newPosition]
        return nameOld == nameNew && value0 == value1
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}