package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun StockCandlestickLoader(
    modifier: Modifier = Modifier,
    width: Dp = 156.dp,
    height: Dp = 76.dp,
    bullColor: Color = LocalNeoBrutalistColors.current.green,
    accentColor: Color = LocalNeoBrutalistColors.current.yellow,
    outlineColor: Color = LocalNeoBrutalistColors.current.ink
) {
    val transition = rememberInfiniteTransition(label = "candlestick_loader")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "candle_phase"
    )
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "trend_sweep"
    )

    Canvas(modifier = modifier.size(width = width, height = height)) {
        val candleCount = 5
        val slotW = size.width / candleCount
        val bodyW = slotW * 0.54f
        val strokePx = 2.2.dp.toPx()
        val trendPoints = mutableListOf<Offset>()

        for (i in 0 until candleCount) {
            val offsetPhase = phase - i * 0.72f
            val wave = ((sin(offsetPhase.toDouble()) + 1.0) / 2.0).toFloat()
            val baseHeightRatio = 0.24f + (i * 0.08f) + wave * 0.34f
            val bodyHeight = (size.height * baseHeightRatio).coerceIn(size.height * 0.2f, size.height * 0.64f)
            val centerX = slotW * i + slotW / 2f
            val bottomY = size.height * (0.86f - i * 0.03f)
            val topY = (bottomY - bodyHeight).coerceAtLeast(size.height * 0.14f)

            val wickTop = (topY - size.height * 0.11f).coerceAtLeast(2f)
            val wickBottom = (bottomY + size.height * 0.09f).coerceAtMost(size.height - 2f)

            // Wick
            drawLine(
                color = outlineColor,
                start = Offset(centerX, wickTop),
                end = Offset(centerX, wickBottom),
                strokeWidth = strokePx,
                cap = StrokeCap.Round
            )

            // Hard Neo-Brutalist shadow behind each candle body
            val leftX = centerX - bodyW / 2f
            drawRect(
                color = outlineColor,
                topLeft = Offset(leftX + 2.5.dp.toPx(), topY + 2.5.dp.toPx()),
                size = Size(bodyW, bottomY - topY)
            )

            // Candle body fill
            val fill = if (i % 2 == 0) bullColor else accentColor
            drawRect(
                color = fill,
                topLeft = Offset(leftX, topY),
                size = Size(bodyW, bottomY - topY)
            )
            drawRect(
                color = outlineColor,
                topLeft = Offset(leftX, topY),
                size = Size(bodyW, bottomY - topY),
                style = Stroke(width = strokePx)
            )

            trendPoints.add(Offset(centerX, topY - 4.dp.toPx()))
        }

        // Animated rising trend line connecting candle peaks
        if (trendPoints.size >= 2) {
            val trendPath = Path().apply {
                moveTo(trendPoints.first().x, trendPoints.first().y)
                for (i in 1 until trendPoints.size) {
                    lineTo(trendPoints[i].x, trendPoints[i].y)
                }
            }
            drawPath(
                path = trendPath,
                color = outlineColor,
                style = Stroke(
                    width = 2.6.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
            // Glowing live pulse dot riding along the peaks
            val activeIndex = ((trendPoints.size - 1) * sweep).toInt().coerceIn(0, trendPoints.lastIndex)
            val activePt = trendPoints[activeIndex]
            drawCircle(
                color = bullColor,
                radius = 5.dp.toPx(),
                center = activePt
            )
            drawCircle(
                color = outlineColor,
                radius = 5.dp.toPx(),
                center = activePt,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

@Composable
fun SplashLoadingOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val transition = rememberInfiniteTransition(label = "splash_progress")
    val barProgress by transition.animateFloat(
        initialValue = 0.15f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splash_bar"
    )

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(durationMillis = 280)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xCC070B14))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            NeoCard(
                backgroundColor = neo.yellow,
                shadowOffset = 6.dp,
                contentPadding = PaddingValues(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(neo.ink)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.loading_splash_subtitle),
                            fontFamily = SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center,
                            color = neo.card
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    StockCandlestickLoader(
                        width = 180.dp,
                        height = 84.dp,
                        bullColor = neo.green,
                        accentColor = neo.card,
                        outlineColor = neo.ink
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.header_title_prefix),
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = neo.ink
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .rotate(-2f)
                                .background(neo.green)
                                .border(BorderStroke(2.5.dp, neo.ink))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.header_title_highlight),
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = neo.ink
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Neo-Brutalist Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .background(neo.card)
                            .border(BorderStroke(2.5.dp, neo.ink))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(barProgress)
                                .height(14.dp)
                                .background(neo.green)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatingLoadingCard(
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    NeoCard(
        backgroundColor = neo.card,
        shadowOffset = 5.dp,
        contentPadding = PaddingValues(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StockCandlestickLoader(
                width = 152.dp,
                height = 68.dp,
                bullColor = neo.green,
                accentColor = neo.yellow,
                outlineColor = neo.ink
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.loading_calculating_title).uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = neo.ink,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.loading_calculating_subtitle),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = neo.mutedInk,
                textAlign = TextAlign.Center
            )
        }
    }
}
