package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.delay

@Composable
fun AnimatedBrandLogoLoader(
    modifier: Modifier = Modifier,
    logoSize: Dp = 116.dp,
    ringStrokeWidth: Dp = 5.dp,
    ringAccentColor: Color = Color(0xFFFF3B5C),
    ringSecondaryColor: Color = Color(0xFFFFDE59)
) {
    val neo = LocalNeoBrutalistColors.current
    val transition = rememberInfiniteTransition(label = "brand_logo_loader")

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "logo_ring_rotation"
    )

    val pulseScale by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse_scale"
    )

    val outerSize = logoSize + 24.dp

    Box(
        modifier = modifier.size(outerSize),
        contentAlignment = Alignment.Center
    ) {
        // Rotating vibrant arc ring around the user's logo
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation)
        ) {
            val strokePx = ringStrokeWidth.toPx()
            drawCircle(
                color = ringSecondaryColor.copy(alpha = 0.25f),
                style = Stroke(width = strokePx)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        ringSecondaryColor,
                        ringAccentColor,
                        ringAccentColor
                    )
                ),
                startAngle = 0f,
                sweepAngle = 250f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        // Crisp circular emblem displaying the user's 3rd logo photo
        Box(
            modifier = Modifier
                .size(logoSize)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(Color.White)
                .border(BorderStroke(3.dp, neo.ink), CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_liwirya_logo),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SplashLoadingOverlay(
    visible: Boolean,
    onLoadingFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1850, easing = FastOutSlowInEasing)
        )
        delay(220)
        onLoadingFinished()
    }

    AnimatedVisibility(
        visible = visible,
        exit = fadeOut(animationSpec = tween(durationMillis = 420)) +
            scaleOut(targetScale = 1.05f, animationSpec = tween(durationMillis = 420)),
        modifier = modifier
    ) {
        // 100% opaque dedicated splash canvas so the main page is never revealed prematurely
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0F1D),
                            Color(0xFF111827),
                            Color(0xFF090D16)
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            NeoCard(
                backgroundColor = neo.yellow,
                shadowOffset = 6.dp,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(neo.ink)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
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

                    Spacer(modifier = Modifier.height(22.dp))

                    AnimatedBrandLogoLoader(
                        logoSize = 118.dp,
                        ringStrokeWidth = 5.dp,
                        ringAccentColor = Color(0xFFFF2E55),
                        ringSecondaryColor = neo.ink
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.header_title_prefix),
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
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
                                fontSize = 22.sp,
                                color = neo.ink
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Smooth 0% -> 100% Neo-Brutalist Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(neo.card)
                            .border(BorderStroke(2.5.dp, neo.ink))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress.value.coerceIn(0.02f, 1f))
                                .height(16.dp)
                                .background(neo.green)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "MEMUAT ${(progress.value * 100).toInt()}%",
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = neo.ink
                    )
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
            AnimatedBrandLogoLoader(
                logoSize = 72.dp,
                ringStrokeWidth = 4.dp,
                ringAccentColor = Color(0xFFFF2E55),
                ringSecondaryColor = neo.green
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
