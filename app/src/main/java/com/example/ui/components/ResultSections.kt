package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.CalculationResult
import com.example.domain.StockFeeCalculator
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NetRdnResultCard(
    result: CalculationResult,
    onCopySummary: () -> Unit,
    onShareSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val isLoss = result.netProfit < 0.0
    val highlightBg = if (isLoss) neo.pink else neo.green

    NeoCard(modifier = modifier.fillMaxWidth()) {
        // Card Title + Result Market Pill
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.card_title_result).uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = neo.ink
            )
            NeoMarketPill(text = result.resultMarketTag)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(neo.ink)
        )
        Spacer(modifier = Modifier.height(14.dp))

        // Profit / Loss Highlight Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 3.dp, bottom = 3.dp)
                .drawBehind {
                    val o = 3.dp.toPx()
                    drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
                }
                .background(highlightBg)
                .border(BorderStroke(3.dp, neo.ink))
                .padding(horizontal = 16.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.label_net_profit_loss).uppercase(),
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    letterSpacing = 0.5.sp,
                    color = neo.ink,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = StockFeeCalculator.formatIdr(result.netProfit),
                    fontFamily = SpaceMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = neo.ink,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .background(neo.ink)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = StockFeeCalculator.formatPercent(
                            result.netProfitPct,
                            decimals = 2,
                            showPlusSign = true
                        ),
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = neo.card
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2-Column Summary Grid (Gross Profit & Total Deduction)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryStatBox(
                label = stringResource(R.string.label_gross_profit),
                value = StockFeeCalculator.formatIdr(result.grossProfit),
                backgroundColor = neo.card,
                modifier = Modifier.weight(1f)
            )
            SummaryStatBox(
                label = stringResource(R.string.label_total_deduction),
                value = StockFeeCalculator.formatIdr(result.totalCost),
                backgroundColor = neo.yellow,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Copy & Share Trade Confirmation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeoButton(
                text = stringResource(R.string.btn_copy_summary),
                onClick = onCopySummary,
                backgroundColor = neo.blue,
                shadowOffset = 3.dp,
                icon = Icons.Rounded.ContentCopy,
                fontSizeSp = 12f,
                testTag = "copy_summary_button",
                modifier = Modifier.weight(1f)
            )
            NeoButton(
                text = stringResource(R.string.btn_share_summary),
                onClick = onShareSummary,
                backgroundColor = neo.purple,
                shadowOffset = 3.dp,
                icon = Icons.Rounded.Share,
                fontSizeSp = 12f,
                testTag = "share_summary_button",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryStatBox(
    label: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    Box(
        modifier = modifier
            .padding(end = 3.dp, bottom = 3.dp)
            .drawBehind {
                val o = 3.dp.toPx()
                drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
            }
            .background(backgroundColor)
            .border(BorderStroke(3.dp, neo.ink))
            .padding(horizontal = 10.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label.uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = neo.ink,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = neo.ink,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TradeConfirmationCard(
    result: CalculationResult,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val kpeiPctStr = String.format(Locale.US, "%.3f", result.kpeiRate * 100.0)
    val buyCommissionLabel = (if (result.freeBuy) {
        stringResource(R.string.row_broker_buy_fee_ipo)
    } else {
        stringResource(R.string.row_broker_buy_fee)
    }) + result.minFeeNote
    val stampLabel = if (result.isDayTradeStampLabel) {
        stringResource(R.string.row_stamp_daytrade)
    } else {
        stringResource(R.string.row_stamp_default)
    }

    NeoCard(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.card_title_breakdown).uppercase(),
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = neo.ink,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(neo.ink)
        )

        // Group 1: Nilai Transaksi
        Spacer(modifier = Modifier.height(14.dp))
        NeoSectionTag(text = stringResource(R.string.group_tx_value))
        Spacer(modifier = Modifier.height(4.dp))
        BreakdownRow(
            label = stringResource(R.string.row_buy_value),
            value = StockFeeCalculator.formatIdr(result.buyValue)
        )
        BreakdownRow(
            label = stringResource(R.string.row_sell_value),
            value = StockFeeCalculator.formatIdr(result.sellValue)
        )

        // Group 2: Komisi Sekuritas
        Spacer(modifier = Modifier.height(14.dp))
        NeoSectionTag(text = stringResource(R.string.group_broker_commission))
        Spacer(modifier = Modifier.height(4.dp))
        BreakdownRow(
            label = buyCommissionLabel,
            value = StockFeeCalculator.formatIdr(result.brokerPureBuyFee)
        )
        BreakdownRow(
            label = stringResource(R.string.row_broker_sell_fee),
            value = StockFeeCalculator.formatIdr(result.brokerPureSellFee)
        )
        BreakdownRow(
            label = stringResource(R.string.row_broker_vat),
            value = StockFeeCalculator.formatIdr(result.brokerVatTotal)
        )

        // Group 3: Pungutan Regulator & Pajak
        Spacer(modifier = Modifier.height(14.dp))
        NeoSectionTag(text = stringResource(R.string.group_regulator_tax))
        Spacer(modifier = Modifier.height(4.dp))
        BreakdownRow(
            label = stringResource(R.string.row_levy_bei),
            value = StockFeeCalculator.formatIdr(result.levyBeiTotal)
        )
        BreakdownRow(
            label = stringResource(R.string.row_kpei_format, kpeiPctStr),
            value = StockFeeCalculator.formatIdr(result.kpeiTotal)
        )
        BreakdownRow(
            label = stringResource(R.string.row_levy_vat),
            value = StockFeeCalculator.formatIdr(result.levyVatTotal)
        )
        BreakdownRow(
            label = stringResource(R.string.row_pph_final),
            value = StockFeeCalculator.formatIdr(result.pphFinal)
        )
        BreakdownRow(
            label = stampLabel,
            value = StockFeeCalculator.formatIdr(result.stampCost),
            showDivider = false
        )

        // BEP Highlight Row
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(neo.bepRow)
                .border(BorderStroke(2.dp, neo.ink))
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.row_bep_price),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = neo.ink,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Text(
                text = StockFeeCalculator.formatIdr(result.bepPrice),
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = neo.ink
            )
        }

        // Total Semua Biaya & Pajak Row
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(neo.yellow)
                .border(BorderStroke(3.dp, neo.ink))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.row_total_cost).uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.5.sp,
                color = neo.ink,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Text(
                text = StockFeeCalculator.formatIdr(result.totalCost),
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = neo.ink
            )
        }
    }
}

@Composable
private fun BreakdownRow(
    label: String,
    value: String,
    showDivider: Boolean = true
) {
    val neo = LocalNeoBrutalistColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = neo.ink,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            )
            Text(
                text = value,
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = neo.ink
            )
        }
        if (showDivider) {
            NeoDashedDivider()
        }
    }
}
