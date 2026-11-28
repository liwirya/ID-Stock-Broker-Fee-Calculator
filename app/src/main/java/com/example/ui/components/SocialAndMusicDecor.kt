package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

const val URL_INSTAGRAM_LIWIRYA = "https://www.instagram.com/mynameisliwirya"
const val URL_GITHUB_LIWIRYA = "https://github.com/liwirya"

@Composable
fun TopRightMusicButton(
    isPlaying: Boolean,
    trackCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val pulseScale = if (isPlaying) {
        val transition = rememberInfiniteTransition(label = "music_btn_pulse")
        val value by transition.animateFloat(
            initialValue = 0.94f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "music_icon_scale"
        )
        value
    } else {
        1f
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(end = 3.dp, bottom = 3.dp)
                .drawBehind {
                    val o = 3.dp.toPx()
                    drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
                }
                .size(48.dp)
                .background(if (isPlaying) neo.green else neo.card)
                .border(BorderStroke(2.5.dp, neo.ink))
                .testTag("open_music_player_button")
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.GraphicEq else Icons.Rounded.MusicNote,
                contentDescription = stringResource(R.string.cd_music_player),
                tint = neo.ink,
                modifier = Modifier
                    .size(24.dp)
                    .scale(pulseScale)
            )
        }

        if (trackCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(neo.ink)
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = trackCount.coerceAtMost(99).toString(),
                    fontFamily = SpaceMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    lineHeight = 10.sp,
                    color = neo.yellow
                )
            }
        }
    }
}

@Composable
fun SocialCreditsFooter(
    onOpenInstagram: () -> Unit,
    onOpenGitHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val igDesc = stringResource(R.string.cd_instagram_link)
    val ghDesc = stringResource(R.string.cd_github_link)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Instagram Button
            Box(
                modifier = Modifier
                    .padding(end = 3.dp, bottom = 3.dp)
                    .drawBehind {
                        val o = 3.dp.toPx()
                        drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
                    }
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFEDA75),
                                Color(0xFFFA7E1E),
                                Color(0xFFD62976),
                                Color(0xFF962FBF),
                                Color(0xFF4F5BD5)
                            )
                        )
                    )
                    .border(BorderStroke(2.5.dp, neo.ink))
                    .testTag("social_instagram_button")
                    .semantics { contentDescription = igDesc }
                    .clickable(role = Role.Button, onClick = onOpenInstagram),
                contentAlignment = Alignment.Center
            ) {
                InstagramGlyph(modifier = Modifier.size(26.dp))
            }

            // GitHub Button
            Box(
                modifier = Modifier
                    .padding(end = 3.dp, bottom = 3.dp)
                    .drawBehind {
                        val o = 3.dp.toPx()
                        drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
                    }
                    .size(48.dp)
                    .background(Color(0xFF181717))
                    .border(BorderStroke(2.5.dp, neo.ink))
                    .testTag("social_github_button")
                    .semantics { contentDescription = ghDesc }
                    .clickable(role = Role.Button, onClick = onOpenGitHub),
                contentAlignment = Alignment.Center
            ) {
                GitHubGlyph(modifier = Modifier.size(26.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .background(neo.ink)
                .border(BorderStroke(2.dp, neo.yellow))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.credit_footer_text),
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.6.sp,
                color = neo.card,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InstagramGlyph(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = w * 0.105f
        val pad = stroke * 0.7f
        // Rounded camera outline
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(pad, pad),
            size = Size(w - pad * 2f, h - pad * 2f),
            cornerRadius = CornerRadius(w * 0.28f, w * 0.28f),
            style = Stroke(width = stroke)
        )
        // Center lens ring
        drawCircle(
            color = Color.White,
            radius = w * 0.21f,
            center = Offset(w / 2f, h / 2f),
            style = Stroke(width = stroke)
        )
        // Top-right flash dot
        drawCircle(
            color = Color.White,
            radius = w * 0.068f,
            center = Offset(w * 0.73f, h * 0.27f)
        )
    }
}

@Composable
private fun GitHubGlyph(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.49f

        // Stylized GitHub Octocat silhouette in crisp white
        val headPath = Path().apply {
            // Left ear
            moveTo(w * 0.26f, h * 0.34f)
            lineTo(w * 0.23f, h * 0.14f)
            lineTo(w * 0.40f, h * 0.24f)
            // Top of head
            quadraticTo(cx, h * 0.20f, w * 0.60f, h * 0.24f)
            // Right ear
            lineTo(w * 0.77f, h * 0.14f)
            lineTo(w * 0.74f, h * 0.34f)
            // Right cheek down to torso
            cubicTo(w * 0.86f, h * 0.45f, w * 0.84f, h * 0.67f, w * 0.65f, h * 0.71f)
            // Right leg
            cubicTo(w * 0.68f, h * 0.76f, w * 0.68f, h * 0.84f, w * 0.68f, h * 0.92f)
            lineTo(w * 0.34f, h * 0.92f)
            // Left leg
            lineTo(w * 0.34f, h * 0.82f)
            // Tail curl on left
            cubicTo(w * 0.22f, h * 0.84f, w * 0.18f, h * 0.75f, w * 0.13f, h * 0.73f)
            cubicTo(w * 0.19f, h * 0.70f, w * 0.24f, h * 0.76f, w * 0.34f, h * 0.75f)
            lineTo(w * 0.34f, h * 0.71f)
            // Left cheek back up
            cubicTo(w * 0.16f, h * 0.67f, w * 0.14f, h * 0.45f, w * 0.26f, h * 0.34f)
            close()
        }
        drawPath(path = headPath, color = Color.White)

        // Subtle dark eye slits for unmistakable Octocat mark
        drawCircle(color = Color(0xFF181717), radius = w * 0.045f, center = Offset(w * 0.40f, cy))
        drawCircle(color = Color(0xFF181717), radius = w * 0.045f, center = Offset(w * 0.60f, cy))
    }
}
