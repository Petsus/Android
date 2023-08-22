package br.com.petsus.util.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.util.global.Action
import br.com.petsus.util.global.ActionSuspend
import kotlinx.coroutines.*

abstract class BaseAdapter<Element, THolder : RecyclerView.ViewHolder> @JvmOverloads constructor(
    private val defaultDispatcher: MainCoroutineDispatcher = Dispatchers.Main
) : RecyclerView.Adapter<THolder>() {

    protected class BaseDiffCallback<Element>(
        private val currentList: List<Element>,
        private val newList: List<Element>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int =
            currentList.size

        override fun getNewListSize(): Int =
            newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            currentList[oldItemPosition] === newList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            currentList[oldItemPosition] == newList[newItemPosition]

    }

    protected val elements: MutableList<Element> = mutableListOf()

    private val coroutineScope = CoroutineScope(SupervisorJob())

    private val onClickListeners: MutableList<Action<Element>> = mutableListOf()
    private val onSuspendClickListeners: MutableList<ActionSuspend<Element>> = mutableListOf()

    override fun getItemCount(): Int = elements.size

    open fun put(elements: Collection<Element>) {
        coroutineScope.launch {
            val calculateDiff = DiffUtil.calculateDiff(BaseDiffCallback(currentList = this@BaseAdapter.elements, newList = this@BaseAdapter.elements.plus(elements)))
            withContext(defaultDispatcher) {
                this@BaseAdapter.elements.addAll(elements)
                calculateDiff.dispatchUpdatesTo(this@BaseAdapter)
            }
        }
    }

    open fun update(elements: Collection<Element>) {
        coroutineScope.launch {
            val calculateDiff = DiffUtil.calculateDiff(BaseDiffCallback(currentList = this@BaseAdapter.elements, newList = elements.toList()))
            withContext(defaultDispatcher) {
                this@BaseAdapter.elements.clear()
                this@BaseAdapter.elements.addAll(elements)
                calculateDiff.dispatchUpdatesTo(this@BaseAdapter)
            }
        }
    }

    open fun delete(elements: Collection<Element>) {
        coroutineScope.launch {
            val calculateDiff = DiffUtil.calculateDiff(BaseDiffCallback(
                currentList = this@BaseAdapter.elements,
                newList = this@BaseAdapter.elements.filter { filter -> elements.find { it === filter } == null }
            ))
            withContext(defaultDispatcher) {
                this@BaseAdapter.elements.removeAll(elements.toSet())
                calculateDiff.dispatchUpdatesTo(this@BaseAdapter)
            }
        }
    }

    open fun find(position: Int): List<Element> {
        return elements.getOrNull(position)?.run { listOf(this) } ?: emptyList()
    }

    open fun clear() {
        coroutineScope.launch {
            val calculateDiff = DiffUtil.calculateDiff(BaseDiffCallback(currentList = this@BaseAdapter.elements, newList = emptyList()))
            withContext(defaultDispatcher) {
                this@BaseAdapter.elements.removeAll(elements.toSet())
                calculateDiff.dispatchUpdatesTo(this@BaseAdapter)
            }
        }
    }

    open fun addClickListener(onClickListener: Action<Element>) {
        this.onClickListeners.add(onClickListener)
    }

    open fun unload() {
        coroutineScope.cancel()
    }

    protected open fun callOnClick(item: Element) {
        onClickListeners.iterator().forEach { it.action(item) }
        onSuspendClickListeners.iterator().forEach { coroutineScope.launch { it.action(item) } }
    }
}