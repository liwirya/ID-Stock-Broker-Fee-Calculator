package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class NeoBrutalistColors(
    val canvas: Color = CanvasCream,
    val card: Color = CardWhite,
    val ink: Color = InkBlack,
    val mutedInk: Color = MutedInk,
    val yellow: Color = AccentYellow,
    val green: Color = AccentGreen,
    val greenPressed: Color = AccentGreenHover,
    val blue: Color = AccentBlue,
    val pink: Color = AccentPink,
    val purple: Color = AccentPurple,
    val orange: Color = AccentOrange,
    val infoBox: Color = InfoBoxBlue,
    val bepRow: Color = BepRowBlue,
    val noteYellow: Color = NoteYellow,
    val checkboxSurface: Color = CheckboxSurface,
    val disabledSurface: Color = DisabledSurface
)

val LocalNeoBrutalistColors = staticCompositionLocalOf { NeoBrutalistColors() }

private val NeoBrutalistColorScheme = lightColorScheme(
    primary = AccentGreen,
    onPrimary = InkBlack,
    primaryContainer = AccentYellow,
    onPrimaryContainer = InkBlack,
    secondary = AccentBlue,
    onSecondary = InkBlack,
    secondaryContainer = InfoBoxBlue,
    onSecondaryContainer = InkBlack,
    tertiary = AccentPurple,
    onTertiary = InkBlack,
    tertiaryContainer = AccentOrange,
    onTertiaryContainer = InkBlack,
    background = CanvasCream,
    onBackground = InkBlack,
    surface = CardWhite,
    onSurface = InkBlack,
    surfaceVariant = CheckboxSurface,
    onSurfaceVariant = InkBlack,
    outline = InkBlack,
    outlineVariant = InkBlack,
    error = AccentPink,
    onError = InkBlack,
    errorContainer = AccentPink,
    onErrorContainer = InkBlack
)

private val NeoBrutalistShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(0.dp),
    large = RoundedCornerShape(0.dp),
    extraLarge = RoundedCornerShape(0.dp)
)

@Composable
fun FeeSahamTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNeoBrutalistColors provides NeoBrutalistColors()) {
        MaterialTheme(
            colorScheme = NeoBrutalistColorScheme,
            typography = FeeSahamTypography,
            shapes = NeoBrutalistShapes,
            content = content
        )
    }
}
