package br.com.petsus.screen.address.list.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.R
import br.com.petsus.util.tool.dp
import br.com.petsus.util.tool.pixel
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DeleteAddressHelper(
    private val context: Context,
    private val callback: HelperCallback,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    interface HelperCallback {
        suspend fun canDelete(position: Int): Boolean
        fun removeItem(position: Int)
        fun updateItem(position: Int)
    }

    private val coroutineScope = CoroutineScope(SupervisorJob())

    private val iconTrash: Bitmap? by lazy {
        ContextCompat.getDrawable(context, R.drawable.icon_delete)?.toBitmapOrNull(width = 16.dp.pixel, 16.dp.pixel)
    }

    private val colorBackground: ColorDrawable by lazy {
        ColorDrawable(ContextCompat.getColor(context, R.color.md_theme_error))
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        coroutineScope.launch {
            val position = viewHolder.adapterPosition
            when {
                callback.canDelete(position) -> withContext(Dispatchers.Main) { callback.removeItem(position) }
                else -> withContext(Dispatchers.Main) { callback.updateItem(position) }
            }
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        colorBackground.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        colorBackground.draw(c)

        if (iconTrash == null)
            return

        val top = itemView.top + (itemView.height / 2f) - (iconTrash!!.height / 2f)
        val left = itemView.right - (min(abs(dX), iconTrash!!.width.toFloat()))

        c.drawBitmap(iconTrash!!, left, top, null)
    }

}