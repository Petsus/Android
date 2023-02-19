package br.com.petsus.screen.home.clinics.pager

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import br.com.petsus.util.tool.dp
import br.com.petsus.util.tool.pixel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.ShapeAppearanceModel

class ClinicChips(
    private val names: List<String>
) : Fragment() {

    private val chipGroup: ChipGroup by lazy {
        ChipGroup(requireContext()).apply {
            isSingleLine = false
            chipSpacingHorizontal = 8.pixel
            chipSpacingVertical = 0
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val nestedScrollView = NestedScrollView(requireContext())

        nestedScrollView.isHorizontalScrollBarEnabled = false
        nestedScrollView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        nestedScrollView.addView(chipGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        nestedScrollView.updatePadding(8.pixel, 8.pixel, 8.pixel, 8.pixel)

        return nestedScrollView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        names.forEach { name ->
            val chip = Chip(requireContext()).apply {
                text = name
                shapeAppearanceModel = ShapeAppearanceModel()
                    .withCornerSize(16f.dp)
            }
            chip.background = ColorDrawable(Color.BLACK)
            chipGroup.addView(chip)
        }
    }
}