package com.example.foody.util

import androidx.recyclerview.widget.DiffUtil
import com.example.foody.model.Result

class RecipesDiffUtil<T>(
    private val oldList : List<T>,
    private val newList : List<T>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
        /**  Referential Equality(===)
         *  === its check reference(location in memory) of the particular item
         * For referential equality, we use the === symbol which allows us to evaluate the
         * reference of an object (if it’s pointing to the same object).
          */
    }



    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
        /**  Structural Equality(==)
         * == its check the values of the particular item
         * it is only works because our class is dataclass
         */
    }
}