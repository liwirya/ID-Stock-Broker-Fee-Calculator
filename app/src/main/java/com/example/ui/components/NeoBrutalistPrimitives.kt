package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

fun Modifier.dottedCanvasBackground(
    backgroundColor: Color,
    dotColor: Color,
    spacing: Dp = 20.dp,
    dotRadius: Dp = 1.dp
): Modifier = this.drawBehind {
    drawRect(color = backgroundColor)
    val stepPx = spacing.toPx()
    val radiusPx = dotRadius.toPx()
    var y = stepPx / 2f
    while (y < size.height) {
        var x = stepPx / 2f
        while (x < size.width) {
            drawCircle(
                color = dotColor.copy(alpha = 0.28f),
                radius = radiusPx,
                center = Offset(x, y)
            )
            x += stepPx
        }
        y += stepPx
    }
}

@Composable
fun NeoCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalNeoBrutalistColors.current.card,
    borderWidth: Dp = 3.dp,
    shadowOffset: Dp = 5.dp,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    Box(
        modifier = modifier
            .padding(end = shadowOffset, bottom = shadowOffset)
            .drawBehind {
                val offsetPx = shadowOffset.toPx()
                drawRect(
                    color = neo.ink,
                    topLeft = Offset(offsetPx, offsetPx),
                    size = Size(size.width, size.height)
                )
            }
            .background(backgroundColor)
            .border(BorderStroke(borderWidth, neo.ink))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            content = content
        )
    }
}

@Composable
fun NeoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalNeoBrutalistColors.current.green,
    pressedColor: Color = backgroundColor,
    shadowOffset: Dp = 5.dp,
    borderWidth: Dp = 3.dp,
    icon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    fontSizeSp: Float = 15f,
    testTag: String? = null
) {
    val neo = LocalNeoBrutalistColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressShift by animateDpAsState(
        targetValue = if (isPressed) (shadowOffset - 2.dp).coerceAtLeast(1.dp) else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "neo_button_press_shift"
    )
    val fill = if (isPressed) pressedColor else backgroundColor

    Box(
        modifier = modifier
            .padding(end = shadowOffset, bottom = shadowOffset)
            .drawBehind {
                val offsetPx = shadowOffset.toPx()
                drawRect(
                    color = neo.ink,
                    topLeft = Offset(offsetPx, offsetPx),
                    size = Size(size.width, size.height)
                )
            }
    ) {
        Surface(
            color = fill,
            border = BorderStroke(borderWidth, neo.ink),
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = pressShift, y = pressShift)
                .defaultMinSize(minHeight = 48.dp)
                .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = neo.ink),
                    onClick = onClick
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = neo.ink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text.uppercase(),
                    color = neo.ink,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = fontSizeSp.sp,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = neo.ink,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NeoNumericField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    testTag: String
) {
    val neo = LocalNeoBrutalistColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val fieldBg = when {
        !enabled -> neo.disabledSurface
        isFocused -> neo.noteYellow
        else -> neo.card
    }
    val shadowDp = if (isFocused && enabled) 4.dp else 3.dp

    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp
            ),
            color = neo.ink,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = shadowDp, bottom = shadowDp)
                .drawBehind {
                    val offsetPx = shadowDp.toPx()
                    drawRect(
                        color = neo.ink,
                        topLeft = Offset(offsetPx, offsetPx),
                        size = Size(size.width, size.height)
                    )
                }
                .background(fieldBg)
                .border(BorderStroke(3.dp, neo.ink))
                .defaultMinSize(minHeight = 48.dp)
                .padding(horizontal = 12.dp, vertical = 11.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(neo.ink),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                textStyle = TextStyle(
                    fontFamily = SpaceMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = neo.ink
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(testTag)
            )
        }
    }
}

@Composable
fun NeoSectionTag(
    text: String,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    Box(
        modifier = modifier
            .background(neo.ink)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = neo.card,
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun NeoMarketPill(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalNeoBrutalistColors.current.yellow
) {
    val neo = LocalNeoBrutalistColors.current
    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(BorderStroke(2.dp, neo.ink))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = neo.ink,
            fontFamily = SpaceMonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}

@Composable
fun NeoDashedDivider(
    modifier: Modifier = Modifier
) {
    val ink = LocalNeoBrutalistColors.current.ink
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
    ) {
        drawLine(
            color = ink,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        )
    }
}
