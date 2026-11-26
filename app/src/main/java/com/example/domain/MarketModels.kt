package com.example.domain

enum class FeeMode {
    ALL_IN,
    BASE_FEE
}

enum class UnitMode {
    LOT,
    SHARES
}

enum class BrokerCategory(val title: String) {
    RETAIL_DIGITAL("Top Retail & Digital"),
    BUMN_BANK("BUMN & Bank-Backed"),
    SWASTA_ASING("Swasta & Asing Populer"),
    CUSTOM("Mode Custom")
}

data class DayTradeConfig(
    val available: Boolean,
    val buy: Double? = null,
    val sell: Double? = null,
    val extraFee: Double = 0.0
)

data class Broker(
    val id: String,
    val code: String,
    val name: String,
    val displayLabel: String,
    val buy: Double,
    val sell: Double,
    val feeMode: FeeMode,
    val category: BrokerCategory,
    val minimumFee: Double? = null,
    val minimumFeePeriod: String? = null,
    val promotional: Boolean = false,
    val dayTrade: DayTradeConfig = DayTradeConfig(available = false),
    val notes: String
) {
    val isCustom: Boolean
        get() = id.startsWith("custom")
}

object RegulatorRates {
    const val LEVY_BEI_KSEI = 0.0003       // 0.03% (BEI + KSEI)
    const val KPEI_REGULAR = 0.0001        // 0.01% (Kliring KPEI Reguler/Tunai)
    const val KPEI_NEGO = 0.00009          // 0.009% (Kliring KPEI Pasar Negosiasi SKB 2026)
    const val VAT_EFFECTIVE = 0.11         // 11% efektif atas jasa keuangan (PMK 131/2024)
    const val PPH_FINAL = 0.001            // 0.10% PPh Pasal 4(2) atas Nilai Jual
    const val STAMP_DUTY = 10_000.0        // Rp 10.000 e-Meterai (> Rp 10 Juta / hari)
    const val STAMP_THRESHOLD = 10_000_000.0
}

enum class MarketType(
    val optionLabel: String,
    val shortChipLabel: String,
    val badge: String,
    val resTag: String,
    val descTitle: String,
    val descBody: String,
    val unitMode: UnitMode,
    val volLabel: String,
    val kpeiRate: Double,
    val freeBuy: Boolean
) {
    REGULAR(
        optionLabel = "Pasar Reguler (Regular Market - RG | T+2)",
        shortChipLabel = "Reguler (RG)",
        badge = "PASAR REGULER (RG)",
        resTag = "RG • T+2",
        descTitle = "Pasar Reguler (RG):",
        descBody = "Satuan Lot (1 Lot = 100 lembar), settlement T+2, berlaku fraksi harga BEI, Levy BEI+KSEI 0,03%, KPEI 0,01%, PPN Levy 0,0033%, PPN Komisi, dan PPh Final jual 0,1%.",
        unitMode = UnitMode.LOT,
        volLabel = "Jumlah Lot (1 Lot = 100 lbr)",
        kpeiRate = RegulatorRates.KPEI_REGULAR,
        freeBuy = false
    ),
    NEGO(
        optionLabel = "Pasar Negosiasi (Negotiated Market - NG | Satuan Lembar)",
        shortChipLabel = "Negosiasi (NG)",
        badge = "PASAR NEGOSIASI (NG)",
        resTag = "NG • KESEPAKATAN",
        descTitle = "Pasar Negosiasi (NG):",
        descBody = "Transaksi tawar-menawar langsung antar pihak (crossing / block sale / odd-lot). Volume dihitung dalam satuan Lembar, kliring KPEI 0,009% (SKB BEI-KPEI-KSEI), tanpa batas ARA/ARB.",
        unitMode = UnitMode.SHARES,
        volLabel = "Jumlah Lembar Saham (Satuan Lembar)",
        kpeiRate = RegulatorRates.KPEI_NEGO,
        freeBuy = false
    ),
    TUNAI(
        optionLabel = "Pasar Tunai (Cash Market - TN | Settlement T+0)",
        shortChipLabel = "Tunai (TN)",
        badge = "PASAR TUNAI (TN)",
        resTag = "TN • T+0 (HARI SAMA)",
        descTitle = "Pasar Tunai (TN):",
        descBody = "Penyelesaian transaksi dilakukan pada hari yang sama (T+0) pada sesi I. Satuan perdagangan Lot (100 lembar) dengan struktur pungutan bursa & pajak sama seperti Reguler.",
        unitMode = UnitMode.LOT,
        volLabel = "Jumlah Lot (1 Lot = 100 lbr)",
        kpeiRate = RegulatorRates.KPEI_REGULAR,
        freeBuy = false
    ),
    DAY_TRADE(
        optionLabel = "Day Trade / Intraday (Beli & Jual Hari Sama)",
        shortChipLabel = "Day Trade",
        badge = "DAY TRADE / INTRADAY",
        resTag = "DAY TRADE • INTRADAY",
        descTitle = "Day Trade (Beli & Jual di Hari Sama):",
        descBody = "Otomatis memakai tarif khusus Day Trade jika broker mendukung (misal Mirae/MOST/BIONS). e-Meterai dihitung dari akumulasi transaksi harian (> Rp 10 Juta = 1x Materai).",
        unitMode = UnitMode.LOT,
        volLabel = "Jumlah Lot (1 Lot = 100 lbr)",
        kpeiRate = RegulatorRates.KPEI_REGULAR,
        freeBuy = false
    ),
    IPO(
        optionLabel = "Transaksi e-IPO (Beli Pasar Perdana → Jual Reguler)",
        shortChipLabel = "e-IPO",
        badge = "TRANSAKSI e-IPO",
        resTag = "IPO → PASAR SEKUNDER",
        descTitle = "Pasar Perdana (e-IPO):",
        descBody = "Saat penjatahan beli IPO dikenakan 0% Komisi Broker, 0% Levy, & 0% PPN. Biaya broker, levy, PPN, dan PPh Final 0,1% hanya dikenakan saat saham dijual di pasar sekunder.",
        unitMode = UnitMode.LOT,
        volLabel = "Jumlah Lot Penjatahan IPO",
        kpeiRate = RegulatorRates.KPEI_REGULAR,
        freeBuy = true
    )
}
