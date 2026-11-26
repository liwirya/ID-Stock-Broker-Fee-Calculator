package com.example.domain

import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToLong

data class CalculationInput(
    val marketType: MarketType,
    val broker: Broker,
    val buyPrice: Double,
    val sellPrice: Double,
    val volume: Double,
    val buyFeePct: Double,
    val sellFeePct: Double,
    val includeStamp: Boolean
)

data class CalculationResult(
    val resultMarketTag: String,
    val brokerName: String,
    val brokerCode: String,
    val marketType: MarketType,
    val buyPrice: Double,
    val sellPrice: Double,
    val volume: Double,
    val totalShares: Double,
    val buyValue: Double,
    val sellValue: Double,
    val brokerPureBuyFee: Double,
    val brokerPureSellFee: Double,
    val brokerVatTotal: Double,
    val levyBeiTotal: Double,
    val kpeiRate: Double,
    val kpeiTotal: Double,
    val levyVatTotal: Double,
    val pphFinal: Double,
    val stampCost: Double,
    val isDayTradeStampLabel: Boolean,
    val totalCost: Double,
    val grossProfit: Double,
    val netProfit: Double,
    val netProfitPct: Double,
    val bepPrice: Double,
    val minFeeNote: String,
    val freeBuy: Boolean
)

object StockFeeCalculator {

    fun calculate(input: CalculationInput): CalculationResult {
        val rule = input.marketType
        val broker = input.broker

        val buyPrice = input.buyPrice.coerceAtLeast(0.0)
        val sellPrice = input.sellPrice.coerceAtLeast(0.0)
        val vol = input.volume.coerceAtLeast(0.0)
        val inputBuyRate = if (rule.freeBuy) 0.0 else (input.buyFeePct.coerceAtLeast(0.0) / 100.0)
        val inputSellRate = input.sellFeePct.coerceAtLeast(0.0) / 100.0

        val shares = if (rule.unitMode == UnitMode.LOT) vol * 100.0 else vol
        val buyValue = buyPrice * shares
        val sellValue = sellPrice * shares

        // Tarif Regulator Baku (SRO)
        val levyBeiRate = RegulatorRates.LEVY_BEI_KSEI
        val kpeiRate = rule.kpeiRate
        val levyVatRate = levyBeiRate * RegulatorRates.VAT_EFFECTIVE
        val totalRegBuyRate = if (rule.freeBuy) 0.0 else (levyBeiRate + kpeiRate + levyVatRate)
        val totalRegSellRate = levyBeiRate + kpeiRate + levyVatRate + RegulatorRates.PPH_FINAL

        var brokerPureBuyFee: Double
        val brokerPureSellFee: Double
        var brokerVatTotal: Double

        if (broker.feeMode == FeeMode.BASE_FEE) {
            // Mode Base Fee: input adalah komisi murni broker
            brokerPureBuyFee = buyValue * inputBuyRate
            brokerPureSellFee = sellValue * inputSellRate
            brokerVatTotal = (brokerPureBuyFee + brokerPureSellFee) * RegulatorRates.VAT_EFFECTIVE
        } else {
            // Mode All-In: pecah komisi murni broker dari total tarif All-In agar tidak double counting
            val netBrokerBuyWithVatRate = if (rule.freeBuy) 0.0 else max(0.0, inputBuyRate - totalRegBuyRate)
            val netBrokerSellWithVatRate = max(0.0, inputSellRate - totalRegSellRate)

            val buyBrokerGross = buyValue * netBrokerBuyWithVatRate
            val sellBrokerGross = sellValue * netBrokerSellWithVatRate

            brokerPureBuyFee = buyBrokerGross / (1.0 + RegulatorRates.VAT_EFFECTIVE)
            brokerPureSellFee = sellBrokerGross / (1.0 + RegulatorRates.VAT_EFFECTIVE)
            brokerVatTotal = (buyBrokerGross - brokerPureBuyFee) + (sellBrokerGross - brokerPureSellFee)
        }

        // Cek Minimum Fee Harian (contoh: Mandiri Sekuritas Rp 5.000/hari)
        var minFeeNote = ""
        val minFee = broker.minimumFee
        if (minFee != null && (brokerPureBuyFee + brokerPureSellFee) > 0.0 && (brokerPureBuyFee + brokerPureSellFee) < minFee) {
            val diff = minFee - (brokerPureBuyFee + brokerPureSellFee)
            brokerPureBuyFee += diff
            brokerVatTotal = (brokerPureBuyFee + brokerPureSellFee) * RegulatorRates.VAT_EFFECTIVE
            minFeeNote = " [Min Fee ${formatIdr(minFee)}]"
        }

        // Pungutan Regulator & Pajak
        val levyBeiTotal = (if (rule.freeBuy) 0.0 else buyValue * levyBeiRate) + (sellValue * levyBeiRate)
        val kpeiTotal = (if (rule.freeBuy) 0.0 else buyValue * kpeiRate) + (sellValue * kpeiRate)
        val levyVatTotal = (if (rule.freeBuy) 0.0 else buyValue * levyVatRate) + (sellValue * levyVatRate)
        val pphFinal = sellValue * RegulatorRates.PPH_FINAL

        // e-Meterai (PMK 134/2021)
        var stampCost = 0.0
        var isDayTradeStampLabel = false
        if (input.includeStamp) {
            if (rule == MarketType.DAY_TRADE) {
                if ((buyValue + sellValue) > RegulatorRates.STAMP_THRESHOLD) {
                    stampCost = RegulatorRates.STAMP_DUTY
                    isDayTradeStampLabel = true
                }
            } else {
                if (!rule.freeBuy && buyValue > RegulatorRates.STAMP_THRESHOLD) {
                    stampCost += RegulatorRates.STAMP_DUTY
                }
                if (sellValue > RegulatorRates.STAMP_THRESHOLD) {
                    stampCost += RegulatorRates.STAMP_DUTY
                }
            }
        }

        // Total Biaya & Profit
        val totalCost = brokerPureBuyFee + brokerPureSellFee + brokerVatTotal +
            levyBeiTotal + kpeiTotal + levyVatTotal + pphFinal + stampCost
        val grossProfit = sellValue - buyValue
        val netProfit = grossProfit - totalCost

        val totalBuyCost = if (rule.freeBuy) {
            0.0
        } else {
            brokerPureBuyFee + (brokerPureBuyFee * RegulatorRates.VAT_EFFECTIVE) + (buyValue * totalRegBuyRate)
        }
        val totalModalBeli = buyValue + totalBuyCost
        val netPct = if (totalModalBeli > 0.0) (netProfit / totalModalBeli) * 100.0 else 0.0

        // Harga Breakeven (BEP) per lembar
        val effectiveSellDeductionRate = if (broker.feeMode == FeeMode.BASE_FEE) {
            (inputSellRate * (1.0 + RegulatorRates.VAT_EFFECTIVE)) + totalRegSellRate
        } else {
            inputSellRate
        }
        val bepPrice = if (shares > 0.0 && effectiveSellDeductionRate < 1.0) {
            ceil(totalModalBeli / (shares * (1.0 - effectiveSellDeductionRate)))
        } else {
            0.0
        }

        return CalculationResult(
            resultMarketTag = "${broker.code} • ${rule.resTag}",
            brokerName = broker.name,
            brokerCode = broker.code,
            marketType = rule,
            buyPrice = buyPrice,
            sellPrice = sellPrice,
            volume = vol,
            totalShares = shares,
            buyValue = buyValue,
            sellValue = sellValue,
            brokerPureBuyFee = brokerPureBuyFee,
            brokerPureSellFee = brokerPureSellFee,
            brokerVatTotal = brokerVatTotal,
            levyBeiTotal = levyBeiTotal,
            kpeiRate = kpeiRate,
            kpeiTotal = kpeiTotal,
            levyVatTotal = levyVatTotal,
            pphFinal = pphFinal,
            stampCost = stampCost,
            isDayTradeStampLabel = isDayTradeStampLabel,
            totalCost = totalCost,
            grossProfit = grossProfit,
            netProfit = netProfit,
            netProfitPct = netPct,
            bepPrice = bepPrice,
            minFeeNote = minFeeNote,
            freeBuy = rule.freeBuy
        )
    }

    fun formatIdr(amount: Double): String {
        val isNegative = amount < 0.0
        val roundedAbs = abs(amount).roundToLong()
        val digits = roundedAbs.toString()
        val formatted = buildString {
            for (i in digits.indices) {
                if (i > 0 && (digits.length - i) % 3 == 0) {
                    append('.')
                }
                append(digits[i])
            }
        }
        return (if (isNegative) "-Rp " else "Rp ") + formatted
    }

    fun formatPercent(value: Double, decimals: Int = 2, showPlusSign: Boolean = false): String {
        val sign = if (showPlusSign && value >= 0.0) "+" else ""
        return sign + String.format(Locale.US, "%.${decimals}f%%", value)
    }

    fun formatNumberClean(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.roundToLong().toString()
        } else {
            value.toBigDecimal().stripTrailingZeros().toPlainString()
        }
    }

    fun formatTradeConfirmationShareText(result: CalculationResult): String {
        val unitText = if (result.marketType.unitMode == UnitMode.LOT) {
            "${formatNumberClean(result.volume)} Lot (${formatNumberClean(result.totalShares)} lbr)"
        } else {
            "${formatNumberClean(result.totalShares)} Lembar"
        }
        val kpeiPct = String.format(Locale.US, "%.3f", result.kpeiRate * 100.0)
        val buyFeeLabel = if (result.freeBuy) {
            "Komisi Broker Beli (e-IPO 0%)"
        } else {
            "Komisi Murni Broker (Beli)"
        } + result.minFeeNote
        val stampLabel = if (result.isDayTradeStampLabel) {
            "e-Meterai (Akumulasi Harian > 10 Jt)"
        } else {
            "e-Meterai (PMK 134/2021)"
        }

        return buildString {
            appendLine("=== TRADE CONFIRMATION • FEE SAHAM BEI ===")
            appendLine("Pasar   : ${result.marketType.badge}")
            appendLine("Broker  : ${result.brokerName} (${result.brokerCode})")
            appendLine("Beli    : ${formatIdr(result.buyPrice)} | Jual: ${formatIdr(result.sellPrice)}")
            appendLine("Volume  : $unitText")
            appendLine("------------------------------------------")
            appendLine("NET RDN : ${formatIdr(result.netProfit)} (${formatPercent(result.netProfitPct, 2, showPlusSign = true)})")
            appendLine("Gross   : ${formatIdr(result.grossProfit)}")
            appendLine("Biaya   : ${formatIdr(result.totalCost)}")
            appendLine("BEP/lbr : ${formatIdr(result.bepPrice)}")
            appendLine("------------------------------------------")
            appendLine("RINCIAN BIAYA & PAJAK:")
            appendLine("• Nilai Beli Bruto       : ${formatIdr(result.buyValue)}")
            appendLine("• Nilai Jual Bruto       : ${formatIdr(result.sellValue)}")
            appendLine("• $buyFeeLabel : ${formatIdr(result.brokerPureBuyFee)}")
            appendLine("• Komisi Murni (Jual)    : ${formatIdr(result.brokerPureSellFee)}")
            appendLine("• PPN Komisi (PMK 131)   : ${formatIdr(result.brokerVatTotal)}")
            appendLine("• Levy BEI & KSEI 0.03%  : ${formatIdr(result.levyBeiTotal)}")
            appendLine("• Kliring KPEI $kpeiPct%   : ${formatIdr(result.kpeiTotal)}")
            appendLine("• PPN Levy (11%)         : ${formatIdr(result.levyVatTotal)}")
            appendLine("• PPh Final Jual (0.10%) : ${formatIdr(result.pphFinal)}")
            appendLine("• $stampLabel : ${formatIdr(result.stampCost)}")
            append("Total Semua Biaya & Pajak: ${formatIdr(result.totalCost)}")
        }
    }
}
