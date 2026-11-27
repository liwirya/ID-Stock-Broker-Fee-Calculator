package com.example

import com.example.domain.BrokerCatalog
import com.example.domain.CalculationInput
import com.example.domain.MarketType
import com.example.domain.StockFeeCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StockFeeCalculatorTest {

    @Test
    fun `regular market all-in matches exact HTML formulas without double counting`() {
        val stockbit = BrokerCatalog.findById("stockbit")
        val res = StockFeeCalculator.calculate(
            CalculationInput(
                marketType = MarketType.REGULAR,
                broker = stockbit,
                buyPrice = 1000.0,
                sellPrice = 1100.0,
                volume = 10.0,
                buyFeePct = 0.15,
                sellFeePct = 0.25,
                includeStamp = true
            )
        )

        assertEquals(1000.0, res.totalShares, 0.001)
        assertEquals(1_000_000.0, res.buyValue, 0.001)
        assertEquals(1_100_000.0, res.sellValue, 0.001)
        // Total fee for 0.15% buy + 0.25% sell = 1500 + 2750 = 4250, no stamp (< 10jt)
        assertEquals(4_250.0, res.totalCost, 0.01)
        assertEquals(100_000.0, res.grossProfit, 0.01)
        assertEquals(95_750.0, res.netProfit, 0.01)
        assertEquals(0.0, res.stampCost, 0.01)
        assertEquals(1005.0, res.bepPrice, 0.01)
        assertEquals("XL • RG • T+2", res.resultMarketTag)
    }

    @Test
    fun `e-IPO applies zero buy fees and zero buy stamp`() {
        val stockbit = BrokerCatalog.findById("stockbit")
        val res = StockFeeCalculator.calculate(
            CalculationInput(
                marketType = MarketType.IPO,
                broker = stockbit,
                buyPrice = 500.0,
                sellPrice = 600.0,
                volume = 300.0, // 30,000 shares -> Buy 15jt, Sell 18jt
                buyFeePct = 0.0,
                sellFeePct = 0.25,
                includeStamp = true
            )
        )

        assertEquals(0.0, res.brokerPureBuyFee, 0.001)
        // Only sell side > 10jt triggers 1x e-Meterai Rp 10.000
        assertEquals(10_000.0, res.stampCost, 0.01)
        assertEquals(18_000_000.0 * 0.0025 + 10_000.0, res.totalCost, 0.01)
    }

    @Test
    fun `negotiated market uses share units and 0_009 percent KPEI clearing`() {
        val stockbit = BrokerCatalog.findById("stockbit")
        val res = StockFeeCalculator.calculate(
            CalculationInput(
                marketType = MarketType.NEGO,
                broker = stockbit,
                buyPrice = 2000.0,
                sellPrice = 2100.0,
                volume = 150.0, // 150 shares (odd lot)
                buyFeePct = 0.15,
                sellFeePct = 0.25,
                includeStamp = false
            )
        )

        assertEquals(150.0, res.totalShares, 0.001)
        assertEquals(0.00009, res.kpeiRate, 0.000001)
        assertEquals((300_000.0 + 315_000.0) * 0.00009, res.kpeiTotal, 0.01)
    }

    @Test
    fun `daytrade applies single accumulated daily e-meterai when buy plus sell exceeds 10M`() {
        val mirae = BrokerCatalog.findById("mirae")
        val res = StockFeeCalculator.calculate(
            CalculationInput(
                marketType = MarketType.DAY_TRADE,
                broker = mirae,
                buyPrice = 6000.0,
                sellPrice = 6100.0,
                volume = 10.0, // Buy 6M + Sell 6.1M = 12.1M > 10M
                buyFeePct = 0.08,
                sellFeePct = 0.18,
                includeStamp = true
            )
        )

        assertEquals(10_000.0, res.stampCost, 0.01)
        assertTrue(res.isDayTradeStampLabel)
    }

    @Test
    fun `mandiri sekuritas enforces minimum daily commission 5000`() {
        val mandiri = BrokerCatalog.findById("mandiri")
        val res = StockFeeCalculator.calculate(
            CalculationInput(
                marketType = MarketType.REGULAR,
                broker = mandiri,
                buyPrice = 1000.0,
                sellPrice = 1100.0,
                volume = 10.0,
                buyFeePct = 0.18,
                sellFeePct = 0.28,
                includeStamp = false
            )
        )

        assertEquals(5_000.0, res.brokerPureBuyFee + res.brokerPureSellFee, 0.01)
        assertEquals(" [Min Fee Rp 5.000]", res.minFeeNote)
    }

    @Test
    fun `broker catalog contains 38 OJK brokers with V1_0_1 revisions and new additions`() {
        assertEquals(40, BrokerCatalog.allBrokers.size) // 38 OJK AB + 2 custom modes

        // Revised brokers
        val pg = BrokerCatalog.findById("pancaglobal")
        assertEquals(0.10, pg.buy, 0.0001)
        assertEquals(0.20, pg.sell, 0.0001)

        val xa = BrokerCatalog.findById("nhkorindo")
        assertEquals(0.18, xa.buy, 0.0001)
        assertEquals(0.28, xa.sell, 0.0001)

        val dh = BrokerCatalog.findById("sinarmas")
        assertEquals(0.15, dh.buy, 0.0001)
        assertEquals(0.25, dh.sell, 0.0001)

        val gr = BrokerCatalog.findById("panin")
        assertEquals(0.20, gr.buy, 0.0001)
        assertEquals(0.30, gr.sell, 0.0001)

        val kk = BrokerCatalog.findById("phillip")
        assertEquals(0.18, kk.buy, 0.0001)
        assertEquals(0.28, kk.sell, 0.0001)

        val at = BrokerCatalog.findById("phintraco")
        assertEquals(0.15, at.buy, 0.0001)
        assertEquals(0.18, at.sell, 0.0001)

        // Newly added brokers
        val expectedNew = listOf(
            Triple("paramitra", "PS", 0.10 to 0.20),
            Triple("lotus", "YJ", 0.15 to 0.25),
            Triple("megacapital", "CD", 0.17 to 0.28),
            Triple("minnapadi", "MU", 0.25 to 0.35),
            Triple("pacific", "AP", 0.20 to 0.30),
            Triple("pilarmas", "PO", 0.18 to 0.28),
            Triple("pluangmaju", "RO", 0.30 to 0.30),
            Triple("profindo", "RG", 0.18 to 0.28)
        )
        expectedNew.forEach { (id, code, fees) ->
            val b = BrokerCatalog.findById(id)
            assertEquals(code, b.code)
            assertEquals(fees.first, b.buy, 0.0001)
            assertEquals(fees.second, b.sell, 0.0001)
        }

        val nonCustomBrokers = BrokerCatalog.allBrokers.filterNot { it.isCustom }
        assertEquals(38, nonCustomBrokers.size)
        assertTrue(nonCustomBrokers.all { !it.websiteDomain.isNullOrBlank() })

        assertTrue(BrokerCatalog.search("YP").any { it.id == "mirae" })
        assertTrue(BrokerCatalog.search("MOST").any { it.id == "mandiri" })
        assertTrue(BrokerCatalog.search("PS").any { it.id == "paramitra" })
        assertTrue(BrokerCatalog.search("Lotus").any { it.id == "lotus" })
    }

    @Test
    fun `formatIdr formats positive and negative rupiah accurately`() {
        assertEquals("Rp 0", StockFeeCalculator.formatIdr(0.0))
        assertEquals("Rp 95.750", StockFeeCalculator.formatIdr(95750.0))
        assertEquals("-Rp 12.345", StockFeeCalculator.formatIdr(-12345.0))
    }

    @Test
    fun `stock symbol is normalized uppercase and included in share summary`() {
        val kisi = BrokerCatalog.findById("kisi")
        val res = StockFeeCalculator.calculate(
            CalculationInput(
                stockSymbol = "  irsx ",
                marketType = MarketType.REGULAR,
                broker = kisi,
                buyPrice = 212.0,
                sellPrice = 218.0,
                volume = 10.0,
                buyFeePct = kisi.buy,
                sellFeePct = kisi.sell,
                includeStamp = false
            )
        )

        assertEquals("IRSX", res.stockSymbol)
        val shareText = StockFeeCalculator.formatTradeConfirmationShareText(res)
        assertTrue(shareText.contains("Saham   : IRSX"))
    }
}
