package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.domain.Broker
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceMonoFontFamily

private enum class BrokerSymbol {
    CANDLE_UP,
    SPARK_STAR,
    DIAMOND_SPLIT,
    WAVE_RIBBON,
    BANK_PILLAR,
    SHIELD_CREST,
    LOTUS_PETAL,
    GLOBE_GRID,
    SUN_HORIZON,
    CROWN_PEAK,
    HEX_NODE,
    ARROW_BOLT,
    SLIDERS_CUSTOM
}

@Immutable
private data class BrokerBrandIdentity(
    val bgColor: Color,
    val accentColor: Color,
    val inkOnBg: Color,
    val symbol: BrokerSymbol
)

private fun buildOfficialIconUrls(broker: Broker): List<String> {
    if (broker.isCustom) return emptyList()
    val domains = listOfNotNull(broker.websiteDomain, broker.fallbackDomain).distinct()
    return buildList {
        domains.forEach { domain ->
            add("https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://$domain&size=128")
        }
        domains.forEach { domain ->
            add("https://www.google.com/s2/favicons?domain=$domain&sz=128")
        }
    }
}

private fun brandIdentityFor(brokerId: String): BrokerBrandIdentity = when (brokerId) {
    // Retail & Digital
    "stockbit" -> BrokerBrandIdentity(Color(0xFF00AB6B), Color(0xFFFFFFFF), Color(0xFFFFFFFF), BrokerSymbol.CANDLE_UP)
    "ajaib" -> BrokerBrandIdentity(Color(0xFF1D6CE0), Color(0xFFFFDE59), Color(0xFFFFFFFF), BrokerSymbol.SPARK_STAR)
    "mirae" -> BrokerBrandIdentity(Color(0xFF003B71), Color(0xFFF58220), Color(0xFFFFFFFF), BrokerSymbol.DIAMOND_SPLIT)
    "ipot" -> BrokerBrandIdentity(Color(0xFF312E81), Color(0xFF38BDF8), Color(0xFFFFFFFF), BrokerSymbol.ARROW_BOLT)
    "pluang_plu" -> BrokerBrandIdentity(Color(0xFF1E3A8A), Color(0xFF60A5FA), Color(0xFFFFFFFF), BrokerSymbol.HEX_NODE)
    "pluangmaju" -> BrokerBrandIdentity(Color(0xFF4C1D95), Color(0xFFFACC15), Color(0xFFFFFFFF), BrokerSymbol.CROWN_PEAK)

    // BUMN / Bank-Backed
    "mandiri" -> BrokerBrandIdentity(Color(0xFF003D79), Color(0xFFFFB703), Color(0xFFFFFFFF), BrokerSymbol.WAVE_RIBBON)
    "bni" -> BrokerBrandIdentity(Color(0xFF006677), Color(0xFFF15A22), Color(0xFFFFFFFF), BrokerSymbol.SUN_HORIZON)
    "bri" -> BrokerBrandIdentity(Color(0xFF00529C), Color(0xFFF37021), Color(0xFFFFFFFF), BrokerSymbol.SHIELD_CREST)
    "bca" -> BrokerBrandIdentity(Color(0xFF0060AF), Color(0xFF93C5FD), Color(0xFFFFFFFF), BrokerSymbol.BANK_PILLAR)
    "bahana" -> BrokerBrandIdentity(Color(0xFFC8102E), Color(0xFFFFDE59), Color(0xFFFFFFFF), BrokerSymbol.ARROW_BOLT)

    // Swasta & Asing
    "pancaglobal" -> BrokerBrandIdentity(Color(0xFF0F766E), Color(0xFF5EEAD4), Color(0xFFFFFFFF), BrokerSymbol.GLOBE_GRID)
    "paramitra" -> BrokerBrandIdentity(Color(0xFF047857), Color(0xFFA7F3D0), Color(0xFFFFFFFF), BrokerSymbol.DIAMOND_SPLIT)
    "kisi" -> BrokerBrandIdentity(Color(0xFF1E40AF), Color(0xFFF43F5E), Color(0xFFFFFFFF), BrokerSymbol.CROWN_PEAK)
    "phintraco" -> BrokerBrandIdentity(Color(0xFF0D9488), Color(0xFFFDE047), Color(0xFFFFFFFF), BrokerSymbol.CANDLE_UP)
    "sinarmas" -> BrokerBrandIdentity(Color(0xFFDC2626), Color(0xFFFFFFFF), Color(0xFFFFFFFF), BrokerSymbol.SUN_HORIZON)
    "lotus" -> BrokerBrandIdentity(Color(0xFFBE185D), Color(0xFFF9A8D4), Color(0xFFFFFFFF), BrokerSymbol.LOTUS_PETAL)
    "kbvalbury" -> BrokerBrandIdentity(Color(0xFFF59E0B), Color(0xFF451A03), Color(0xFF111111), BrokerSymbol.SHIELD_CREST)
    "sucor" -> BrokerBrandIdentity(Color(0xFF15803D), Color(0xFFBEF264), Color(0xFFFFFFFF), BrokerSymbol.CANDLE_UP)
    "rhb" -> BrokerBrandIdentity(Color(0xFF0284C7), Color(0xFFBAE6FD), Color(0xFFFFFFFF), BrokerSymbol.WAVE_RIBBON)
    "samuel" -> BrokerBrandIdentity(Color(0xFF1D4ED8), Color(0xFFFACC15), Color(0xFFFFFFFF), BrokerSymbol.SPARK_STAR)
    "yuanta" -> BrokerBrandIdentity(Color(0xFF0369A1), Color(0xFF38BDF8), Color(0xFFFFFFFF), BrokerSymbol.HEX_NODE)
    "megacapital" -> BrokerBrandIdentity(Color(0xFFEA580C), Color(0xFFFEF08A), Color(0xFFFFFFFF), BrokerSymbol.CROWN_PEAK)
    "nhkorindo" -> BrokerBrandIdentity(Color(0xFF166534), Color(0xFFFACC15), Color(0xFFFFFFFF), BrokerSymbol.BANK_PILLAR)
    "phillip" -> BrokerBrandIdentity(Color(0xFF004B87), Color(0xFFF97316), Color(0xFFFFFFFF), BrokerSymbol.SHIELD_CREST)
    "pilarmas" -> BrokerBrandIdentity(Color(0xFFB45309), Color(0xFFFDE68A), Color(0xFFFFFFFF), BrokerSymbol.BANK_PILLAR)
    "profindo" -> BrokerBrandIdentity(Color(0xFF1E293B), Color(0xFF34D399), Color(0xFFFFFFFF), BrokerSymbol.CANDLE_UP)
    "cgs" -> BrokerBrandIdentity(Color(0xFFB91C1C), Color(0xFFFCA5A5), Color(0xFFFFFFFF), BrokerSymbol.GLOBE_GRID)
    "trimegah" -> BrokerBrandIdentity(Color(0xFF991B1B), Color(0xFFFDE047), Color(0xFFFFFFFF), BrokerSymbol.DIAMOND_SPLIT)
    "henan" -> BrokerBrandIdentity(Color(0xFF0F172A), Color(0xFF38BDF8), Color(0xFFFFFFFF), BrokerSymbol.HEX_NODE)
    "mnc" -> BrokerBrandIdentity(Color(0xFF0C4A6E), Color(0xFFFB923C), Color(0xFFFFFFFF), BrokerSymbol.ARROW_BOLT)
    "kiwoom" -> BrokerBrandIdentity(Color(0xFFBE123C), Color(0xFF93C5FD), Color(0xFFFFFFFF), BrokerSymbol.SPARK_STAR)
    "ciptadana" -> BrokerBrandIdentity(Color(0xFF1D4ED8), Color(0xFF93C5FD), Color(0xFFFFFFFF), BrokerSymbol.WAVE_RIBBON)
    "maybank" -> BrokerBrandIdentity(Color(0xFFFFC800), Color(0xFF111111), Color(0xFF111111), BrokerSymbol.CROWN_PEAK)
    "panin" -> BrokerBrandIdentity(Color(0xFF003366), Color(0xFFF59E0B), Color(0xFFFFFFFF), BrokerSymbol.SHIELD_CREST)
    "pacific" -> BrokerBrandIdentity(Color(0xFF0E7490), Color(0xFF67E8F9), Color(0xFFFFFFFF), BrokerSymbol.WAVE_RIBBON)
    "minnapadi" -> BrokerBrandIdentity(Color(0xFF3F6212), Color(0xFFD9F99D), Color(0xFFFFFFFF), BrokerSymbol.LOTUS_PETAL)
    "uob" -> BrokerBrandIdentity(Color(0xFF1E3A8A), Color(0xFFEF4444), Color(0xFFFFFFFF), BrokerSymbol.BANK_PILLAR)

    // Custom modes
    "custom_all_in" -> BrokerBrandIdentity(Color(0xFFFFDE59), Color(0xFF111111), Color(0xFF111111), BrokerSymbol.SLIDERS_CUSTOM)
    "custom_base" -> BrokerBrandIdentity(Color(0xFFC084FC), Color(0xFF111111), Color(0xFF111111), BrokerSymbol.SLIDERS_CUSTOM)
    else -> BrokerBrandIdentity(Color(0xFF334155), Color(0xFFFFDE59), Color(0xFFFFFFFF), BrokerSymbol.CANDLE_UP)
}

@Composable
fun BrokerLogoEmblem(
    broker: Broker,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp
) {
    val neo = LocalNeoBrutalistColors.current
    val context = LocalContext.current
    val identity = remember(broker.id) { brandIdentityFor(broker.id) }
    val iconUrls = remember(broker.id, broker.websiteDomain, broker.fallbackDomain) {
        buildOfficialIconUrls(broker)
    }
    var attemptIndex by remember(broker.id) { mutableIntStateOf(0) }
    val currentUrl = iconUrls.getOrNull(attemptIndex)

    Column(
        modifier = modifier
            .size(size)
            .background(if (currentUrl != null) neo.card else identity.bgColor)
            .border(BorderStroke(2.dp, neo.ink)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (currentUrl != null) {
                val imageRequest = remember(currentUrl) {
                    ImageRequest.Builder(context)
                        .data(currentUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                }
                SubcomposeAsyncImage(
                    model = imageRequest,
                    contentDescription = "Logo ${broker.name}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val asyncPainter = painter
                    val state = asyncPainter.state
                    when (state) {
                        is AsyncImagePainter.State.Success -> {
                            val intrinsic = asyncPainter.intrinsicSize
                            val isTinyDefaultGlobe =
                                intrinsic.width in 1f..16f && intrinsic.height in 1f..16f
                            if (isTinyDefaultGlobe && attemptIndex < iconUrls.lastIndex) {
                                LaunchedEffect(currentUrl) {
                                    attemptIndex += 1
                                }
                                BrokerFallbackCanvas(identity = identity)
                            } else if (isTinyDefaultGlobe) {
                                BrokerFallbackCanvas(identity = identity)
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(neo.card)
                                        .padding(3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = asyncPainter,
                                        contentDescription = "Logo ${broker.name}",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                        is AsyncImagePainter.State.Error -> {
                            if (attemptIndex < iconUrls.lastIndex) {
                                LaunchedEffect(currentUrl) {
                                    attemptIndex += 1
                                }
                            }
                            BrokerFallbackCanvas(identity = identity)
                        }
                        else -> {
                            BrokerFallbackCanvas(identity = identity)
                        }
                    }
                }
            } else {
                BrokerFallbackCanvas(identity = identity)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(neo.ink)
                .padding(vertical = 1.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = broker.code,
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                lineHeight = 11.sp,
                color = neo.card,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun BrokerFallbackCanvas(
    identity: BrokerBrandIdentity
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(identity.bgColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawBrokerSymbol(
                symbol = identity.symbol,
                primary = identity.accentColor,
                secondary = identity.inkOnBg
            )
        }
    }
}

private fun DrawScope.drawBrokerSymbol(
    symbol: BrokerSymbol,
    primary: Color,
    secondary: Color
) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f
    val strokeW = (w * 0.11f).coerceAtLeast(2f)

    when (symbol) {
        BrokerSymbol.CANDLE_UP -> {
            val barW = w * 0.2f
            drawRect(secondary.copy(alpha = 0.85f), Offset(w * 0.12f, h * 0.56f), Size(barW, h * 0.34f))
            drawRect(primary, Offset(w * 0.40f, h * 0.38f), Size(barW, h * 0.52f))
            drawRect(secondary, Offset(w * 0.68f, h * 0.16f), Size(barW, h * 0.74f))
        }

        BrokerSymbol.SPARK_STAR -> {
            val path = Path().apply {
                moveTo(cx, h * 0.08f)
                quadraticTo(cx, cy, w * 0.92f, cy)
                quadraticTo(cx, cy, cx, h * 0.92f)
                quadraticTo(cx, cy, w * 0.08f, cy)
                quadraticTo(cx, cy, cx, h * 0.08f)
                close()
            }
            drawPath(path, color = primary)
            drawCircle(color = secondary, radius = w * 0.1f, center = Offset(cx, cy))
        }

        BrokerSymbol.DIAMOND_SPLIT -> {
            val left = Path().apply {
                moveTo(cx, h * 0.1f)
                lineTo(w * 0.14f, cy)
                lineTo(cx, h * 0.9f)
                close()
            }
            val right = Path().apply {
                moveTo(cx, h * 0.1f)
                lineTo(w * 0.86f, cy)
                lineTo(cx, h * 0.9f)
                close()
            }
            drawPath(left, color = secondary)
            drawPath(right, color = primary)
        }

        BrokerSymbol.WAVE_RIBBON -> {
            val wave1 = Path().apply {
                moveTo(w * 0.1f, h * 0.62f)
                cubicTo(w * 0.35f, h * 0.25f, w * 0.65f, h * 0.85f, w * 0.9f, h * 0.38f)
            }
            val wave2 = Path().apply {
                moveTo(w * 0.1f, h * 0.38f)
                cubicTo(w * 0.35f, h * 0.08f, w * 0.65f, h * 0.62f, w * 0.9f, h * 0.18f)
            }
            drawPath(wave1, color = primary, style = Stroke(strokeW * 1.2f, cap = StrokeCap.Round))
            drawPath(wave2, color = secondary, style = Stroke(strokeW, cap = StrokeCap.Round))
        }

        BrokerSymbol.BANK_PILLAR -> {
            val roof = Path().apply {
                moveTo(cx, h * 0.10f)
                lineTo(w * 0.88f, h * 0.34f)
                lineTo(w * 0.12f, h * 0.34f)
                close()
            }
            drawPath(roof, color = primary)
            val colW = w * 0.14f
            drawRect(secondary, Offset(w * 0.20f, h * 0.42f), Size(colW, h * 0.36f))
            drawRect(secondary, Offset(cx - colW / 2f, h * 0.42f), Size(colW, h * 0.36f))
            drawRect(secondary, Offset(w * 0.66f, h * 0.42f), Size(colW, h * 0.36f))
            drawRect(primary, Offset(w * 0.12f, h * 0.80f), Size(w * 0.76f, h * 0.12f))
        }

        BrokerSymbol.SHIELD_CREST -> {
            val shield = Path().apply {
                moveTo(cx, h * 0.10f)
                lineTo(w * 0.84f, h * 0.24f)
                lineTo(w * 0.76f, h * 0.66f)
                lineTo(cx, h * 0.92f)
                lineTo(w * 0.24f, h * 0.66f)
                lineTo(w * 0.16f, h * 0.24f)
                close()
            }
            drawPath(shield, color = primary)
            drawLine(
                color = secondary,
                start = Offset(w * 0.34f, cy),
                end = Offset(w * 0.66f, cy),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
            drawLine(
                color = secondary,
                start = Offset(cx, h * 0.32f),
                end = Offset(cx, h * 0.68f),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }

        BrokerSymbol.LOTUS_PETAL -> {
            val centerPetal = Path().apply {
                moveTo(cx, h * 0.12f)
                cubicTo(w * 0.72f, h * 0.36f, w * 0.68f, h * 0.72f, cx, h * 0.84f)
                cubicTo(w * 0.32f, h * 0.72f, w * 0.28f, h * 0.36f, cx, h * 0.12f)
                close()
            }
            val leftPetal = Path().apply {
                moveTo(w * 0.14f, h * 0.38f)
                quadraticTo(w * 0.38f, h * 0.44f, cx, h * 0.84f)
                quadraticTo(w * 0.20f, h * 0.76f, w * 0.14f, h * 0.38f)
                close()
            }
            val rightPetal = Path().apply {
                moveTo(w * 0.86f, h * 0.38f)
                quadraticTo(w * 0.62f, h * 0.44f, cx, h * 0.84f)
                quadraticTo(w * 0.80f, h * 0.76f, w * 0.86f, h * 0.38f)
                close()
            }
            drawPath(leftPetal, color = primary)
            drawPath(rightPetal, color = primary)
            drawPath(centerPetal, color = secondary)
        }

        BrokerSymbol.GLOBE_GRID -> {
            val r = w * 0.38f
            drawCircle(color = primary, radius = r, center = Offset(cx, cy), style = Stroke(strokeW))
            drawLine(secondary, Offset(cx - r, cy), Offset(cx + r, cy), strokeWidth = strokeW * 0.8f)
            drawLine(secondary, Offset(cx, cy - r), Offset(cx, cy + r), strokeWidth = strokeW * 0.8f)
            drawOval(
                color = secondary,
                topLeft = Offset(cx - r * 0.52f, cy - r),
                size = Size(r * 1.04f, r * 2f),
                style = Stroke(strokeW * 0.75f)
            )
        }

        BrokerSymbol.SUN_HORIZON -> {
            drawCircle(color = primary, radius = w * 0.22f, center = Offset(cx, h * 0.46f))
            drawRect(secondary, Offset(w * 0.12f, h * 0.66f), Size(w * 0.76f, h * 0.12f))
            drawRect(primary, Offset(w * 0.24f, h * 0.82f), Size(w * 0.52f, h * 0.08f))
        }

        BrokerSymbol.CROWN_PEAK -> {
            val crown = Path().apply {
                moveTo(w * 0.12f, h * 0.78f)
                lineTo(w * 0.18f, h * 0.28f)
                lineTo(w * 0.40f, h * 0.52f)
                lineTo(cx, h * 0.18f)
                lineTo(w * 0.60f, h * 0.52f)
                lineTo(w * 0.82f, h * 0.28f)
                lineTo(w * 0.88f, h * 0.78f)
                close()
            }
            drawPath(crown, color = primary)
            drawRect(secondary, Offset(w * 0.12f, h * 0.82f), Size(w * 0.76f, h * 0.10f))
        }

        BrokerSymbol.HEX_NODE -> {
            val hex = Path().apply {
                moveTo(cx, h * 0.10f)
                lineTo(w * 0.86f, h * 0.30f)
                lineTo(w * 0.86f, h * 0.70f)
                lineTo(cx, h * 0.90f)
                lineTo(w * 0.14f, h * 0.70f)
                lineTo(w * 0.14f, h * 0.30f)
                close()
            }
            drawPath(hex, color = primary, style = Stroke(strokeW, join = StrokeJoin.Miter))
            drawCircle(color = secondary, radius = w * 0.16f, center = Offset(cx, cy))
        }

        BrokerSymbol.ARROW_BOLT -> {
            val trend = Path().apply {
                moveTo(w * 0.14f, h * 0.76f)
                lineTo(w * 0.42f, h * 0.48f)
                lineTo(w * 0.56f, h * 0.62f)
                lineTo(w * 0.86f, h * 0.22f)
            }
            drawPath(trend, color = primary, style = Stroke(strokeW * 1.25f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            val tip = Path().apply {
                moveTo(w * 0.64f, h * 0.22f)
                lineTo(w * 0.86f, h * 0.22f)
                lineTo(w * 0.86f, h * 0.44f)
            }
            drawPath(tip, color = secondary, style = Stroke(strokeW * 1.15f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }

        BrokerSymbol.SLIDERS_CUSTOM -> {
            drawLine(primary, Offset(w * 0.16f, h * 0.30f), Offset(w * 0.84f, h * 0.30f), strokeWidth = strokeW)
            drawRoundRect(secondary, Offset(w * 0.56f, h * 0.18f), Size(w * 0.18f, h * 0.24f), CornerRadius(2f, 2f))
            drawLine(primary, Offset(w * 0.16f, h * 0.70f), Offset(w * 0.84f, h * 0.70f), strokeWidth = strokeW)
            drawRoundRect(secondary, Offset(w * 0.26f, h * 0.58f), Size(w * 0.18f, h * 0.24f), CornerRadius(2f, 2f))
        }
    }
}
