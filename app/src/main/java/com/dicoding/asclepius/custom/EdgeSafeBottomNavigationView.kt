package com.dicoding.asclepius.custom

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class EdgeSafeBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.bottomNavigationStyle
) : BottomNavigationView(context, attrs, defStyleAttr) {

    // Variabel kontrol: ingin padding bawah atau tidak
    var applyBottomInsetPadding: Boolean = false

    init {
        // Cegah padding sistem default
        fitsSystemWindows = false

        // Atur padding manual dari insets
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            val paddingBottom = if (applyBottomInsetPadding) bottomInset else 0

            view.setPaddingRelative(
                view.paddingStart,
                view.paddingTop,
                view.paddingEnd,
                paddingBottom
            )

            insets
        }
    }
}
