package io.github.chkrb.pqcompanion.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val iconRemoveShoppingCart: ImageVector
  get() {
    if (_remove_shopping_cart != null) {
      return _remove_shopping_cart!!
    }
    _remove_shopping_cart =
      ImageVector.Builder(
          name = "remove_shopping_cart",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(9f, 8f)
            verticalLineTo(6f)
            horizontalLineToRelative(6f)
            verticalLineTo(8f)
            horizontalLineTo(9f)
            close()
            moveTo(5.59f, 21.41f)
            quadTo(5f, 20.83f, 5f, 20f)
            reflectiveQuadTo(5.59f, 18.59f)
            reflectiveQuadTo(7f, 18f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(9f, 19.18f, 9f, 20f)
            reflectiveQuadTo(8.41f, 21.41f)
            quadTo(7.83f, 22f, 7f, 22f)
            reflectiveQuadTo(5.59f, 21.41f)
            close()
            moveToRelative(10f, 0f)
            quadTo(15f, 20.83f, 15f, 20f)
            reflectiveQuadToRelative(0.59f, -1.41f)
            reflectiveQuadTo(17f, 18f)
            reflectiveQuadToRelative(1.41f, 0.59f)
            quadTo(19f, 19.18f, 19f, 20f)
            reflectiveQuadToRelative(-0.59f, 1.41f)
            reflectiveQuadTo(17f, 22f)
            reflectiveQuadTo(15.59f, 21.41f)
            close()
            moveTo(1f, 4f)
            verticalLineTo(2f)
            horizontalLineTo(4.28f)
            lineToRelative(4.25f, 9f)
            horizontalLineToRelative(7f)
            lineToRelative(3.9f, -7f)
            horizontalLineTo(21.7f)
            lineToRelative(-4.4f, 7.95f)
            quadToRelative(-0.27f, 0.5f, -0.74f, 0.78f)
            reflectiveQuadTo(15.55f, 13f)
            horizontalLineTo(8.1f)
            lineTo(7f, 15f)
            horizontalLineTo(19f)
            verticalLineToRelative(2f)
            horizontalLineTo(7f)
            quadTo(5.88f, 17f, 5.29f, 16.02f)
            reflectiveQuadTo(5.25f, 14.05f)
            lineTo(6.6f, 11.6f)
            lineTo(3f, 4f)
            horizontalLineTo(1f)
            close()
          }
        }
        .build()
    return _remove_shopping_cart!!
  }

private var _remove_shopping_cart: ImageVector? = null
