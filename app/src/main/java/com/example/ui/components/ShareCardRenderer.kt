package com.example.ui.components

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.example.R
import com.example.domain.CalculationResult
import com.example.domain.StockFeeCalculator
import com.example.domain.UnitMode
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

object ShareCardRenderer {

    private const val DESIGN_W = 1080f
    private const val DESIGN_H = 1350f

    fun renderBitmap(
        context: Context,
        result: CalculationResult,
        theme: ShareCardTheme,
        showRupiah: Boolean,
        widthPx: Int = 1080,
        heightPx: Int = 1350
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.scale(widthPx / DESIGN_W, heightPx / DESIGN_H)

        val grotesk = ResourcesCompat.getFont(context, R.font.space_grotesk) ?: Typeface.DEFAULT
        val groteskBold = Typeface.create(grotesk, Typeface.BOLD)
        val mono = ResourcesCompat.getFont(context, R.font.space_mono) ?: Typeface.MONOSPACE
        val monoBold = Typeface.create(mono, Typeface.BOLD)

        val isProfit = result.netProfit >= 0.0

        // 1. Background & decorative artwork per theme
        when (theme) {
            ShareCardTheme.CANDLE_DARK -> drawCandleDarkBackdrop(canvas, isProfit)
            ShareCardTheme.NEON_ARROW -> drawNeonArrowBackdrop(canvas, isProfit)
            ShareCardTheme.EMERALD_LIGHT -> drawEmeraldLightBackdrop(canvas, isProfit)
            ShareCardTheme.NEO_BRUTALIST -> drawNeoBrutalistBackdrop(canvas, isProfit)
            ShareCardTheme.MIDNIGHT_GOLD -> drawMidnightGoldBackdrop(canvas, isProfit)
            ShareCardTheme.CYBER_TERMINAL -> drawCyberTerminalBackdrop(canvas, isProfit)
        }

        val isLight = theme == ShareCardTheme.EMERALD_LIGHT || theme == ShareCardTheme.NEO_BRUTALIST
        val primaryText = if (isLight) Color.parseColor("#111111") else Color.parseColor("#FFFFFF")
        val secondaryText = if (isLight) Color.parseColor("#475569") else Color.parseColor("#94A3B8")
        val heroAccent = when {
            theme == ShareCardTheme.MIDNIGHT_GOLD && isProfit -> Color.parseColor("#FACC15")
            isProfit && isLight -> Color.parseColor("#16A34A")
            isProfit -> Color.parseColor("#4ADE80")
            isLight -> Color.parseColor("#DC2626")
            else -> Color.parseColor("#FF6B6B")
        }

        // 2. Top Center Broker Header
        val brokerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryText
            typeface = groteskBold
            textSize = 36f
            textAlign = Paint.Align.CENTER
        }
        val brokerSubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = secondaryText
            typeface = monoBold
            textSize = 23f
            textAlign = Paint.Align.CENTER
        }
        val shortBroker = if (result.brokerName.length > 30) {
            result.brokerName.take(28) + "…"
        } else {
            result.brokerName
        }
        canvas.drawText(shortBroker, DESIGN_W / 2f, 110f, brokerPaint)
        canvas.drawText(
            "${result.brokerCode} • ${result.marketType.resTag}",
            DESIGN_W / 2f,
            146f,
            brokerSubPaint
        )

        // 3. Stock Title Row ("Return Saya di Saham [TICKER]")
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isLight) Color.parseColor("#1E293B") else Color.parseColor("#E2E8F0")
            typeface = grotesk
            textSize = 38f
        }
        val leftX = 76f
        val stockRowY = 266f
        val prefixText = "Return Saya di Saham"
        canvas.drawText(prefixText, leftX, stockRowY, labelPaint)

        val tickerText = result.stockSymbol.ifBlank { "BEI" }
        val prefixWidth = labelPaint.measureText(prefixText)
        val badgeLeft = leftX + prefixWidth + 22f
        val tickerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (theme == ShareCardTheme.NEO_BRUTALIST) Color.parseColor("#111111") else primaryText
            typeface = groteskBold
            textSize = 36f
        }
        val tickerW = tickerPaint.measureText(tickerText)
        val badgeRect = RectF(badgeLeft, stockRowY - 42f, badgeLeft + tickerW + 40f, stockRowY + 14f)
        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = when {
                theme == ShareCardTheme.NEO_BRUTALIST -> Color.parseColor("#FFDE59")
                isLight -> Color.parseColor("#E2E8F0")
                else -> Color.parseColor("#1E293B")
            }
        }
        val badgeStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = if (theme == ShareCardTheme.NEO_BRUTALIST) 4f else 2.5f
            color = if (theme == ShareCardTheme.NEO_BRUTALIST) Color.parseColor("#111111") else heroAccent
        }
        val cornerR = if (theme == ShareCardTheme.NEO_BRUTALIST) 0f else 14f
        canvas.drawRoundRect(badgeRect, cornerR, cornerR, badgeBgPaint)
        canvas.drawRoundRect(badgeRect, cornerR, cornerR, badgeStrokePaint)
        canvas.drawText(tickerText, badgeLeft + 20f, stockRowY - 2f, tickerPaint)

        // 4. Giant Hero Return Percentage
        val signPrefix = if (result.netProfitPct >= 0.0) "+ " else "- "
        val absPct = String.format(Locale.US, "%.2f%%", kotlin.math.abs(result.netProfitPct))
        val heroText = signPrefix + absPct
        val heroPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = heroAccent
            typeface = groteskBold
            textSize = 118f
        }
        if (theme == ShareCardTheme.NEO_BRUTALIST) {
            val shadowPaint = Paint(heroPaint).apply { color = Color.parseColor("#111111") }
            canvas.drawText(heroText, leftX + 5f, 412f + 5f, shadowPaint)
        }
        canvas.drawText(heroText, leftX, 412f, heroPaint)

        // 5. Left Column Metrics (Harga Beli, Harga Jual, BEP, optional Net RDN & Volume)
        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryText
            typeface = monoBold
            textSize = 46f
        }
        val captionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = secondaryText
            typeface = grotesk
            textSize = 29f
        }

        var cursorY = 556f
        // Harga Beli
        canvas.drawText(StockFeeCalculator.formatIdr(result.buyPrice), leftX, cursorY, valuePaint)
        canvas.drawText("Harga beli", leftX, cursorY + 38f, captionPaint)

        // Harga Jual
        cursorY += 126f
        canvas.drawText(StockFeeCalculator.formatIdr(result.sellPrice), leftX, cursorY, valuePaint)
        canvas.drawText("Harga jual", leftX, cursorY + 38f, captionPaint)

        // Harga BEP
        cursorY += 126f
        canvas.drawText(StockFeeCalculator.formatIdr(result.bepPrice), leftX, cursorY, valuePaint)
        canvas.drawText("Harga breakeven (BEP)", leftX, cursorY + 38f, captionPaint)

        // Optional Net Profit Rupiah + Total Fee
        if (showRupiah) {
            cursorY += 136f
            val netValPaint = Paint(valuePaint).apply {
                color = heroAccent
                textSize = 48f
            }
            val unitLabel = if (result.marketType.unitMode == UnitMode.LOT) {
                "${StockFeeCalculator.formatNumberClean(result.volume)} Lot"
            } else {
                "${StockFeeCalculator.formatNumberClean(result.totalShares)} Lembar"
            }
            val netSign = if (result.netProfit > 0.0) "+" else ""
            canvas.drawText(
                netSign + StockFeeCalculator.formatIdr(result.netProfit),
                leftX,
                cursorY,
                netValPaint
            )
            canvas.drawText(
                "Net Masuk RDN • $unitLabel • Biaya ${StockFeeCalculator.formatIdr(result.totalCost)}",
                leftX,
                cursorY + 38f,
                captionPaint.apply { textSize = 25f }
            )
        }

        // 6. Bottom-Left App Watermark (inspired by iKISI bottom-left brand block)
        drawAppWatermark(canvas, leftX, 1218f, isLight, groteskBold, monoBold)

        return bitmap
    }

    private fun drawAppWatermark(
        canvas: Canvas,
        x: Float,
        y: Float,
        isLight: Boolean,
        groteskBold: Typeface,
        monoBold: Typeface
    ) {
        val iconSize = 68f
        val iconRect = RectF(x, y, x + iconSize, y + iconSize)
        val yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FFDE59") }
        val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#4ADE80") }
        val inkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#111111") }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = if (isLight) Color.parseColor("#111111") else Color.parseColor("#FFFFFF")
        }

        canvas.drawRect(iconRect, yellowPaint)
        canvas.drawRect(RectF(x, y, x + iconSize, y + 22f), greenPaint)
        canvas.drawRect(RectF(x + 12f, y + 44f, x + 22f, y + 60f), inkPaint)
        canvas.drawRect(RectF(x + 29f, y + 36f, x + 39f, y + 60f), inkPaint)
        canvas.drawRect(RectF(x + 46f, y + 28f, x + 56f, y + 60f), inkPaint)
        canvas.drawRect(iconRect, borderPaint)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isLight) Color.parseColor("#111111") else Color.parseColor("#FFFFFF")
            typeface = groteskBold
            textSize = 31f
        }
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isLight) Color.parseColor("#475569") else Color.parseColor("#94A3B8")
            typeface = monoBold
            textSize = 20f
        }
        canvas.drawText("Fee Saham BEI", x + iconSize + 20f, y + 32f, titlePaint)
        canvas.drawText("CREDIT BY LIWIRYA", x + iconSize + 20f, y + 60f, subPaint)
    }

    private fun drawCandleDarkBackdrop(canvas: Canvas, isProfit: Boolean) {
        // Obsidian dark gradient
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, DESIGN_W, DESIGN_H,
                Color.parseColor("#080A0F"),
                Color.parseColor("#020305"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, DESIGN_W, DESIGN_H, bgPaint)

        // Soft emerald/crimson ambient radial glow behind 3D candles
        val glowColor = if (isProfit) Color.parseColor("#3322C55E") else Color.parseColor("#33EF4444")
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                820f, 940f, 440f,
                glowColor,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(820f, 940f, 440f, glowPaint)

        // Left glass-chrome 3D candlestick
        draw3DCandle(
            canvas = canvas,
            centerX = 635f,
            bodyTop = 1015f,
            bodyBottom = 1175f,
            bodyWidth = 108f,
            wickTop = 870f,
            wickBottom = 1320f,
            leftFaceColor = Color.parseColor("#1E242D"),
            rightFaceColor = Color.parseColor("#475569"),
            topHighlightColor = Color.parseColor("#CBD5E1"),
            edgeColor = Color.parseColor("#94A3B8")
        )

        // Center hero 3D colored candlestick (bullish green or bearish red)
        val leftFace = if (isProfit) Color.parseColor("#3F7D20") else Color.parseColor("#991B1B")
        val rightFace = if (isProfit) Color.parseColor("#84CC16") else Color.parseColor("#F87171")
        val topFace = if (isProfit) Color.parseColor("#BEF264") else Color.parseColor("#FCA5A5")
        val edge = if (isProfit) Color.parseColor("#D9F99D") else Color.parseColor("#FECACA")
        draw3DCandle(
            canvas = canvas,
            centerX = 815f,
            bodyTop = 760f,
            bodyBottom = 1150f,
            bodyWidth = 138f,
            wickTop = 580f,
            wickBottom = 1320f,
            leftFaceColor = leftFace,
            rightFaceColor = rightFace,
            topHighlightColor = topFace,
            edgeColor = edge
        )

        // Right high glass-chrome 3D candlestick
        draw3DCandle(
            canvas = canvas,
            centerX = 975f,
            bodyTop = 640f,
            bodyBottom = 860f,
            bodyWidth = 114f,
            wickTop = 515f,
            wickBottom = 1040f,
            leftFaceColor = Color.parseColor("#1E242D"),
            rightFaceColor = Color.parseColor("#64748B"),
            topHighlightColor = Color.parseColor("#E2E8F0"),
            edgeColor = Color.parseColor("#CBD5E1")
        )
    }

    private fun draw3DCandle(
        canvas: Canvas,
        centerX: Float,
        bodyTop: Float,
        bodyBottom: Float,
        bodyWidth: Float,
        wickTop: Float,
        wickBottom: Float,
        leftFaceColor: Int,
        rightFaceColor: Int,
        topHighlightColor: Int,
        edgeColor: Int
    ) {
        val halfW = bodyWidth / 2f
        val depth = bodyWidth * 0.24f

        // Metallic wick
        val wickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                centerX - 10f, wickTop, centerX + 10f, wickTop,
                Color.parseColor("#1E293B"),
                Color.parseColor("#94A3B8"),
                Shader.TileMode.MIRROR
            )
        }
        canvas.drawRoundRect(
            RectF(centerX - 11f, wickTop, centerX + 11f, wickBottom),
            10f, 10f, wickPaint
        )

        // Left shaded bevel face
        val leftPath = Path().apply {
            moveTo(centerX - halfW, bodyTop + depth)
            lineTo(centerX, bodyTop)
            lineTo(centerX, bodyBottom)
            lineTo(centerX - halfW, bodyBottom + depth * 0.6f)
            close()
        }
        canvas.drawPath(leftPath, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = leftFaceColor })

        // Right lit bevel face
        val rightPath = Path().apply {
            moveTo(centerX, bodyTop)
            lineTo(centerX + halfW, bodyTop + depth)
            lineTo(centerX + halfW, bodyBottom + depth * 0.6f)
            lineTo(centerX, bodyBottom)
            close()
        }
        canvas.drawPath(rightPath, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = rightFaceColor })

        // Top diamond bevel
        val topPath = Path().apply {
            moveTo(centerX, bodyTop - depth * 0.7f)
            lineTo(centerX + halfW, bodyTop + depth)
            lineTo(centerX, bodyTop + depth * 1.6f)
            lineTo(centerX - halfW, bodyTop + depth)
            close()
        }
        canvas.drawPath(topPath, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = topHighlightColor })

        // Specular edge lines
        val edgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = edgeColor
            alpha = 190
        }
        canvas.drawPath(leftPath, edgePaint)
        canvas.drawPath(rightPath, edgePaint)
    }

    private fun drawNeonArrowBackdrop(canvas: Canvas, isProfit: Boolean) {
        canvas.drawColor(Color.parseColor("#06080D"))

        val mainGlow = if (isProfit) Color.parseColor("#4ADE80") else Color.parseColor("#FF5A5F")
        val softGlow = if (isProfit) Color.parseColor("#4422C55E") else Color.parseColor("#44EF4444")

        val radial = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                740f, 920f, 430f,
                softGlow,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(740f, 920f, 430f, radial)

        // Subtle star dust dots
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = mainGlow; alpha = 90 }
        val pts = listOf(
            580f to 720f, 910f to 680f, 620f to 1120f, 950f to 1080f,
            820f to 620f, 520f to 960f, 980f to 820f, 700f to 1180f
        )
        pts.forEachIndexed { idx, (px, py) ->
            canvas.drawCircle(px, py, if (idx % 2 == 0) 5f else 3f, dotPaint)
        }

        // Giant glowing neon arrow (up-right for profit, down-right for loss)
        val arrowPath = Path().apply {
            if (isProfit) {
                moveTo(570f, 1080f)
                lineTo(830f, 820f)
                lineTo(750f, 740f)
                lineTo(955f, 725f)
                lineTo(940f, 930f)
                lineTo(860f, 850f)
                lineTo(600f, 1110f)
                close()
            } else {
                moveTo(570f, 760f)
                lineTo(830f, 1020f)
                lineTo(750f, 1100f)
                lineTo(955f, 1115f)
                lineTo(940f, 910f)
                lineTo(860f, 990f)
                lineTo(600f, 730f)
                close()
            }
        }

        val outerHalo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 34f
            color = mainGlow
            alpha = 45
            strokeJoin = Paint.Join.ROUND
        }
        val midHalo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 16f
            color = mainGlow
            alpha = 120
            strokeJoin = Paint.Join.ROUND
        }
        val coreLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 6f
            color = Color.WHITE
            strokeJoin = Paint.Join.ROUND
        }
        canvas.drawPath(arrowPath, outerHalo)
        canvas.drawPath(arrowPath, midHalo)
        canvas.drawPath(arrowPath, coreLine)
    }

    private fun drawEmeraldLightBackdrop(canvas: Canvas, isProfit: Boolean) {
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, DESIGN_H,
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#F1F5F9"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, DESIGN_W, DESIGN_H, bgPaint)

        val ringPrimary = if (isProfit) Color.parseColor("#16A34A") else Color.parseColor("#DC2626")
        val ringLight = if (isProfit) Color.parseColor("#86EFAC") else Color.parseColor("#FCA5A5")
        val ringSoft = if (isProfit) Color.parseColor("#DCFCE7") else Color.parseColor("#FEE2E2")

        // Concentric 3D tilted target rings in bottom-right (like iKISI light card)
        val cx = 820f
        val cy = 1040f
        val radii = listOf(310f to ringSoft, 235f to ringLight, 165f to ringPrimary, 95f to Color.WHITE)
        radii.forEach { (r, col) ->
            // 3D base shadow
            val shadowRect = RectF(cx - r, cy - r * 0.52f + 22f, cx + r, cy + r * 0.52f + 22f)
            canvas.drawOval(shadowRect, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#1E293B")
                alpha = 26
            })
            val oval = RectF(cx - r, cy - r * 0.52f, cx + r, cy + r * 0.52f)
            canvas.drawOval(oval, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = col })
            canvas.drawOval(oval, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 5f
                color = Color.WHITE
                alpha = 190
            })
        }

        // Center 3D spire arrow
        val spire = Path().apply {
            moveTo(cx, cy - 170f)
            lineTo(cx + 68f, cy - 20f)
            lineTo(cx, cy + 16f)
            lineTo(cx - 68f, cy - 20f)
            close()
        }
        canvas.drawPath(spire, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ringPrimary })
    }

    private fun drawNeoBrutalistBackdrop(canvas: Canvas, isProfit: Boolean) {
        canvas.drawColor(Color.parseColor("#F4F0EA"))

        // Dotted canvas
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#111111")
            alpha = 45
        }
        var y = 32f
        while (y < DESIGN_H) {
            var x = 32f
            while (x < DESIGN_W) {
                canvas.drawCircle(x, y, 3f, dotPaint)
                x += 40f
            }
            y += 40f
        }

        val ink = Color.parseColor("#111111")
        // Yellow top bar
        canvas.drawRect(0f, 0f, DESIGN_W, 184f, Paint().apply { color = Color.parseColor("#FFDE59") })
        canvas.drawLine(0f, 184f, DESIGN_W, 184f, Paint().apply { color = ink; strokeWidth = 8f })

        // Decorative Neo-Brutalist ascending bar block bottom-right
        val accent = if (isProfit) Color.parseColor("#4ADE80") else Color.parseColor("#FF6B6B")
        val bars = listOf(
            RectF(630f, 1020f, 730f, 1280f) to Color.parseColor("#60A5FA"),
            RectF(760f, 900f, 860f, 1280f) to Color.parseColor("#FFDE59"),
            RectF(890f, 750f, 990f, 1280f) to accent
        )
        val shadowPaint = Paint().apply { color = ink }
        val strokePaint = Paint().apply { color = ink; style = Paint.Style.STROKE; strokeWidth = 8f }
        bars.forEach { (rect, fill) ->
            canvas.drawRect(
                RectF(rect.left + 12f, rect.top + 12f, rect.right + 12f, rect.bottom + 12f),
                shadowPaint
            )
            canvas.drawRect(rect, Paint().apply { color = fill })
            canvas.drawRect(rect, strokePaint)
        }

        // Outer frame border
        canvas.drawRect(
            RectF(12f, 12f, DESIGN_W - 12f, DESIGN_H - 12f),
            Paint().apply { color = ink; style = Paint.Style.STROKE; strokeWidth = 10f }
        )
    }

    private fun drawMidnightGoldBackdrop(canvas: Canvas, isProfit: Boolean) {
        val bg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, DESIGN_W, DESIGN_H,
                Color.parseColor("#0B132B"),
                Color.parseColor("#1C2541"),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, DESIGN_W, DESIGN_H, bg)

        // Subtle gold grid
        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FACC15")
            alpha = 26
            strokeWidth = 2f
        }
        for (i in 1..10) {
            val gy = i * 125f
            canvas.drawLine(0f, gy, DESIGN_W, gy, gridPaint)
        }

        // Sweeping chart curve in bottom area
        val curve = Path().apply {
            if (isProfit) {
                moveTo(0f, 1240f)
                cubicTo(320f, 1180f, 520f, 980f, 700f, 920f)
                cubicTo(840f, 870f, 940f, 760f, 1080f, 640f)
            } else {
                moveTo(0f, 960f)
                cubicTo(320f, 980f, 560f, 1050f, 740f, 1120f)
                cubicTo(880f, 1170f, 980f, 1220f, 1080f, 1260f)
            }
        }
        val fillCurve = Path(curve).apply {
            lineTo(DESIGN_W, DESIGN_H)
            lineTo(0f, DESIGN_H)
            close()
        }
        val goldMain = if (isProfit) Color.parseColor("#FACC15") else Color.parseColor("#FB7185")
        canvas.drawPath(
            fillCurve,
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = LinearGradient(
                    0f, 640f, 0f, DESIGN_H,
                    Color.argb(80, Color.red(goldMain), Color.green(goldMain), Color.blue(goldMain)),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
            }
        )
        canvas.drawPath(
            curve,
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 8f
                color = goldMain
                strokeCap = Paint.Cap.ROUND
            }
        )
    }

    private fun drawCyberTerminalBackdrop(canvas: Canvas, isProfit: Boolean) {
        canvas.drawColor(Color.parseColor("#0D1117"))
        val accent = if (isProfit) Color.parseColor("#22C55E") else Color.parseColor("#F43F5E")

        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            alpha = 32
            strokeWidth = 2f
        }
        var gx = 60f
        while (gx < DESIGN_W) {
            canvas.drawLine(gx, 0f, gx, DESIGN_H, gridPaint)
            gx += 72f
        }
        var gy = 60f
        while (gy < DESIGN_H) {
            canvas.drawLine(0f, gy, DESIGN_W, gy, gridPaint)
            gy += 72f
        }

        // Terminal corner brackets
        val bracketPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            style = Paint.Style.STROKE
            strokeWidth = 8f
        }
        canvas.drawLine(40f, 40f, 110f, 40f, bracketPaint)
        canvas.drawLine(40f, 40f, 40f, 110f, bracketPaint)
        canvas.drawLine(DESIGN_W - 110f, 40f, DESIGN_W - 40f, 40f, bracketPaint)
        canvas.drawLine(DESIGN_W - 40f, 40f, DESIGN_W - 40f, 110f, bracketPaint)
        canvas.drawLine(40f, DESIGN_H - 40f, 110f, DESIGN_H - 40f, bracketPaint)
        canvas.drawLine(40f, DESIGN_H - 110f, 40f, DESIGN_H - 40f, bracketPaint)
        canvas.drawLine(DESIGN_W - 110f, DESIGN_H - 40f, DESIGN_W - 40f, DESIGN_H - 40f, bracketPaint)
        canvas.drawLine(DESIGN_W - 40f, DESIGN_H - 110f, DESIGN_W - 40f, DESIGN_H - 40f, bracketPaint)
    }

    fun saveCardToGallery(context: Context, bitmap: Bitmap, fileBaseName: String): Boolean {
        val fileName = "${fileBaseName}_${System.currentTimeMillis()}.png"
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/FeeSahamBEI")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: return false
                resolver.openOutputStream(uri)?.use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                } ?: return false
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
                true
            } else {
                val dir = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "FeeSahamBEI"
                ).apply { mkdirs() }
                val file = File(dir, fileName)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                true
            }
        } catch (_: Exception) {
            false
        }
    }

    fun writeShareCardToCache(context: Context, bitmap: Bitmap): Uri {
        val cacheFolder = File(context.cacheDir, "shared_cards").apply { mkdirs() }
        val outFile = File(cacheFolder, "fee_saham_card_${System.currentTimeMillis()}.png")
        FileOutputStream(outFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            outFile
        )
    }

    fun launchShareIntent(
        context: Context,
        uri: Uri,
        destination: ShareDestination,
        captionText: String,
        chooserTitle: String
    ) {
        val targetPackage = when (destination) {
            ShareDestination.INSTAGRAM_STORY -> "com.instagram.android"
            ShareDestination.WHATSAPP -> "com.whatsapp"
            ShareDestination.FACEBOOK -> "com.facebook.katana"
            else -> null
        }

        if (destination == ShareDestination.INSTAGRAM_STORY && isPackageInstalled(context, "com.instagram.android")) {
            val storyIntent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                setDataAndType(uri, "image/png")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setPackage("com.instagram.android")
            }
            if (storyIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(storyIntent)
                return
            }
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, captionText)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (targetPackage != null && isPackageInstalled(context, targetPackage)) {
            shareIntent.setPackage(targetPackage)
            try {
                context.startActivity(shareIntent)
                return
            } catch (_: Exception) {
                shareIntent.setPackage(null)
            }
        }
        context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
    }

    fun shareCardImage(
        context: Context,
        bitmap: Bitmap,
        destination: ShareDestination,
        captionText: String,
        chooserTitle: String
    ) {
        val uri = writeShareCardToCache(context, bitmap)
        launchShareIntent(context, uri, destination, captionText, chooserTitle)
    }

    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: Exception) {
            false
        }
    }
}
