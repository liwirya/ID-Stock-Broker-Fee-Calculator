package com.example.domain

object BrokerCatalog {
    const val DEFAULT_BROKER_ID = "stockbit"
    const val CUSTOM_ALL_IN_ID = "custom_all_in"

    val allBrokers: List<Broker> = listOf(
        // === TOP RETAIL & DIGITAL BROKERS ===
        Broker(
            id = "stockbit",
            code = "XL",
            name = "Stockbit Sekuritas Digital",
            displayLabel = "Stockbit Sekuritas Digital (XL) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.RETAIL_DIGITAL,
            dayTrade = DayTradeConfig(available = true, buy = 0.15, sell = 0.25),
            notes = "Tarif All-In (sudah termasuk Levy 0.0433%, PPN, & PPh 0.1%)",
            websiteDomain = "stockbit.com"
        ),
        Broker(
            id = "ajaib",
            code = "XC",
            name = "Ajaib Sekuritas Asia",
            displayLabel = "Ajaib Sekuritas Asia (XC) — 0.1513% / 0.2513%",
            buy = 0.1513,
            sell = 0.2513,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.RETAIL_DIGITAL,
            dayTrade = DayTradeConfig(available = true, buy = 0.1513, sell = 0.2513),
            notes = "Tarif All-In sesuai penyesuaian PPN PMK 131/2024 (+0.1% jika Market Order)",
            websiteDomain = "ajaib.co.id"
        ),
        Broker(
            id = "mirae",
            code = "YP",
            name = "Mirae Asset Sekuritas",
            displayLabel = "Mirae Asset Sekuritas (YP) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.RETAIL_DIGITAL,
            dayTrade = DayTradeConfig(available = true, buy = 0.08, sell = 0.18),
            notes = "Akun Reguler 0.15%/0.25%; Akun Khusus Day Trade otomatis 0.08%/0.18%",
            websiteDomain = "miraeasset.co.id",
            fallbackDomain = "sekuritas.miraeasset.co.id"
        ),
        Broker(
            id = "ipot",
            code = "PD",
            name = "Indo Premier Sekuritas / IPOT",
            displayLabel = "Indo Premier Sekuritas / IPOT (PD) — 0.19% / 0.29%",
            buy = 0.19,
            sell = 0.29,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.RETAIL_DIGITAL,
            dayTrade = DayTradeConfig(available = true, buy = 0.19, sell = 0.29),
            notes = "Tarif All-In Online Trading IPOT",
            websiteDomain = "indopremier.com"
        ),
        Broker(
            id = "pluang_plu",
            code = "PLU",
            name = "Pluang Saham ID (Mitra AB)",
            displayLabel = "Pluang Saham ID (Mitra AB) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.RETAIL_DIGITAL,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif All-In terintegrasi Anggota Bursa berizin OJK",
            websiteDomain = "pluang.com"
        ),
        Broker(
            id = "pluangmaju",
            code = "RO",
            name = "Pluang Maju Sekuritas",
            displayLabel = "Pluang Maju Sekuritas (RO) — 0.30% / 0.30%",
            buy = 0.30,
            sell = 0.30,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.RETAIL_DIGITAL,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif All-In Pluang Maju Sekuritas (RO)",
            websiteDomain = "pluangmajusekuritas.com",
            fallbackDomain = "pluang.com"
        ),

        // === BUMN / BANK-BACKED SEKURITAS ===
        Broker(
            id = "mandiri",
            code = "CC",
            name = "Mandiri Sekuritas / MOST",
            displayLabel = "Mandiri Sekuritas / MOST (CC) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.BUMN_BANK,
            minimumFee = 5000.0,
            minimumFeePeriod = "daily",
            dayTrade = DayTradeConfig(available = true, buy = 0.10, sell = 0.20),
            notes = "Berlaku minimum fee komisi Rp 5.000/hari untuk akun reguler tertentu",
            websiteDomain = "mandirisekuritas.co.id",
            fallbackDomain = "most.co.id"
        ),
        Broker(
            id = "bni",
            code = "NI",
            name = "BNI Sekuritas / BIONS",
            displayLabel = "BNI Sekuritas / BIONS (NI) — 0.17% / 0.27%",
            buy = 0.17,
            sell = 0.27,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.BUMN_BANK,
            dayTrade = DayTradeConfig(available = true, buy = 0.12, sell = 0.22),
            notes = "Tarif All-In BIONS Online",
            websiteDomain = "bnisekuritas.co.id",
            fallbackDomain = "bions.id"
        ),
        Broker(
            id = "bri",
            code = "OD",
            name = "BRI Danareksa / BRIGHTS",
            displayLabel = "BRI Danareksa / BRIGHTS (OD) — 0.17% / 0.27%",
            buy = 0.17,
            sell = 0.27,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.BUMN_BANK,
            dayTrade = DayTradeConfig(available = true, buy = 0.17, sell = 0.27),
            notes = "Tarif All-In BRIGHTS Online",
            websiteDomain = "bridanareksasekuritas.co.id",
            fallbackDomain = "brights.id"
        ),
        Broker(
            id = "bca",
            code = "SQ",
            name = "BCA Sekuritas / BEST",
            displayLabel = "BCA Sekuritas / BEST (SQ) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.BUMN_BANK,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif All-In BEST Online Trading",
            websiteDomain = "bcasekuritas.co.id",
            fallbackDomain = "bca.co.id"
        ),
        Broker(
            id = "bahana",
            code = "DX",
            name = "Bahana Sekuritas / DX Trade",
            displayLabel = "Bahana Sekuritas / DX Trade (DX) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.BUMN_BANK,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif standar online trading BUMN",
            websiteDomain = "bahanasekuritas.id"
        ),

        // === SEKURITAS SWASTA & ASING POPULER ===
        Broker(
            id = "pancaglobal",
            code = "PG",
            name = "Panca Global Sekuritas / Hei5",
            displayLabel = "Panca Global Sekuritas / Hei5 (PG) — 0.10% / 0.20%",
            buy = 0.10,
            sell = 0.20,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            promotional = true,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif All-In Aplikasi Hei5",
            websiteDomain = "pancaglobal.co.id"
        ),
        Broker(
            id = "paramitra",
            code = "PS",
            name = "Paramitra Alfa Sekuritas",
            displayLabel = "Paramitra Alfa Sekuritas (PS) — 0.10% / 0.20%",
            buy = 0.10,
            sell = 0.20,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            promotional = true,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif All-In Paramitra Alfa Sekuritas",
            websiteDomain = "alphainvestasi.id",
            fallbackDomain = "paramitra.com"
        ),
        Broker(
            id = "kisi",
            code = "BQ",
            name = "Korea Investment & Sekuritas / KISI",
            displayLabel = "Korea Investment & Sekuritas / KISI (BQ) — 0.13% / 0.23%",
            buy = 0.13,
            sell = 0.23,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            promotional = true,
            dayTrade = DayTradeConfig(available = true, buy = 0.13, sell = 0.23),
            notes = "Aplikasi KOINS / iKISI",
            websiteDomain = "kisi.co.id"
        ),
        Broker(
            id = "phintraco",
            code = "AT",
            name = "Phintraco Sekuritas / Profits",
            displayLabel = "Phintraco Sekuritas / Profits (AT) — 0.15% / 0.18%",
            buy = 0.15,
            sell = 0.18,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            dayTrade = DayTradeConfig(available = false),
            notes = "Tarif All-In Profits Anywhere",
            websiteDomain = "phintracosekuritas.com"
        ),
        Broker(
            id = "sinarmas",
            code = "DH",
            name = "Sinarmas Sekuritas / SimInvest",
            displayLabel = "Sinarmas Sekuritas / SimInvest (DH) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            dayTrade = DayTradeConfig(available = true, buy = 0.125, sell = 0.225),
            notes = "Aplikasi SimInvest",
            websiteDomain = "sinarmassekuritas.co.id",
            fallbackDomain = "siminvest.id"
        ),
        Broker(
            id = "lotus",
            code = "YJ",
            name = "Lotus Andalan Sekuritas",
            displayLabel = "Lotus Andalan Sekuritas (YJ) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In LOTS Online Trading",
            websiteDomain = "lotussekuritas.com",
            fallbackDomain = "lots.co.id"
        ),
        Broker(
            id = "kbvalbury",
            code = "CP",
            name = "KB Valbury Sekuritas",
            displayLabel = "KB Valbury Sekuritas (CP) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Online Trading",
            websiteDomain = "kbvalbury.com"
        ),
        Broker(
            id = "sucor",
            code = "AZ",
            name = "Sucor Sekuritas / SPOT",
            displayLabel = "Sucor Sekuritas / SPOT (AZ) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In SPOT",
            websiteDomain = "sucorsekuritas.com"
        ),
        Broker(
            id = "rhb",
            code = "DR",
            name = "RHB Sekuritas / TradeSmart",
            displayLabel = "RHB Sekuritas / TradeSmart (DR) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In RHB TradeSmart",
            websiteDomain = "rhbtradesmart.co.id",
            fallbackDomain = "rhbgroup.com"
        ),
        Broker(
            id = "samuel",
            code = "IF",
            name = "Samuel Sekuritas / STAR",
            displayLabel = "Samuel Sekuritas / STAR (IF) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In STAR Online",
            websiteDomain = "star.id",
            fallbackDomain = "samuel.co.id"
        ),
        Broker(
            id = "yuanta",
            code = "FS",
            name = "Yuanta Sekuritas / YSinvest",
            displayLabel = "Yuanta Sekuritas / YSinvest (FS) — 0.15% / 0.25%",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In YSinvest",
            websiteDomain = "yuanta.co.id"
        ),
        Broker(
            id = "megacapital",
            code = "CD",
            name = "Mega Capital Sekuritas",
            displayLabel = "Mega Capital Sekuritas (CD) — 0.17% / 0.28%",
            buy = 0.17,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In InvestasiKu / M-Trade",
            websiteDomain = "megasekuritas.id",
            fallbackDomain = "investasiku.id"
        ),
        Broker(
            id = "nhkorindo",
            code = "XA",
            name = "NH Korindo Sekuritas Indonesia",
            displayLabel = "NH Korindo Sekuritas Indonesia (XA) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In NAIK / Online Trading",
            websiteDomain = "nhis.co.id",
            fallbackDomain = "nhsec.co.id"
        ),
        Broker(
            id = "phillip",
            code = "KK",
            name = "Phillip Sekuritas Indonesia / POEMS",
            displayLabel = "Phillip Sekuritas Indonesia / POEMS (KK) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In POEMS ID",
            websiteDomain = "phillip.co.id",
            fallbackDomain = "poems.co.id"
        ),
        Broker(
            id = "pilarmas",
            code = "PO",
            name = "Pilarmas Investindo Sekuritas",
            displayLabel = "Pilarmas Investindo Sekuritas (PO) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Pilarmas Online Trading",
            websiteDomain = "pilarmas-investindo.co.id"
        ),
        Broker(
            id = "profindo",
            code = "RG",
            name = "Profindo Sekuritas Indonesia",
            displayLabel = "Profindo Sekuritas Indonesia (RG) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In ProClick Online Trading",
            websiteDomain = "profindo.com"
        ),
        Broker(
            id = "cgs",
            code = "YU",
            name = "CGS International Sekuritas",
            displayLabel = "CGS International Sekuritas (YU) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In CGS iTrade",
            websiteDomain = "cgsi.co.id"
        ),
        Broker(
            id = "trimegah",
            code = "LG",
            name = "Trimegah Sekuritas Indonesia",
            displayLabel = "Trimegah Sekuritas Indonesia (LG) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Online Trading",
            websiteDomain = "trimegah.com"
        ),
        Broker(
            id = "henan",
            code = "HP",
            name = "Henan Putihrai Sekuritas / HPX",
            displayLabel = "Henan Putihrai Sekuritas / HPX (HP) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In HPX Online",
            websiteDomain = "hpsekuritas.id"
        ),
        Broker(
            id = "mnc",
            code = "EP",
            name = "MNC Sekuritas / MotionTrade",
            displayLabel = "MNC Sekuritas / MotionTrade (EP) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In MotionTrade",
            websiteDomain = "mncsekuritas.id"
        ),
        Broker(
            id = "kiwoom",
            code = "AG",
            name = "Kiwoom Sekuritas / HERO",
            displayLabel = "Kiwoom Sekuritas / HERO (AG) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In HERO",
            websiteDomain = "kiwoom.co.id"
        ),
        Broker(
            id = "ciptadana",
            code = "KI",
            name = "Ciptadana Sekuritas Asia",
            displayLabel = "Ciptadana Sekuritas Asia (KI) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Online",
            websiteDomain = "ciptadana.com"
        ),
        Broker(
            id = "maybank",
            code = "ZP",
            name = "Maybank Sekuritas Indonesia",
            displayLabel = "Maybank Sekuritas Indonesia (ZP) — 0.18% / 0.28%",
            buy = 0.18,
            sell = 0.28,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Maybank Trade ID",
            websiteDomain = "maybank-ke.co.id",
            fallbackDomain = "maybank.co.id"
        ),
        Broker(
            id = "panin",
            code = "GR",
            name = "Panin Sekuritas Tbk / POST",
            displayLabel = "Panin Sekuritas Tbk / POST (GR) — 0.20% / 0.30%",
            buy = 0.20,
            sell = 0.30,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In POST Online",
            websiteDomain = "pans.co.id"
        ),
        Broker(
            id = "pacific",
            code = "AP",
            name = "Pacific Sekuritas Indonesia",
            displayLabel = "Pacific Sekuritas Indonesia (AP) — 0.20% / 0.30%",
            buy = 0.20,
            sell = 0.30,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Pacific Sekuritas Indonesia",
            websiteDomain = "pacificsekuritas.id",
            fallbackDomain = "pacificcapital.co.id"
        ),
        Broker(
            id = "minnapadi",
            code = "MU",
            name = "Minna Padi Investama Sekuritas Tbk",
            displayLabel = "Minna Padi Investama Sekuritas Tbk (MU) — 0.25% / 0.35%",
            buy = 0.25,
            sell = 0.35,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In Minna Padi Investama Sekuritas",
            websiteDomain = "minnapadi.com"
        ),
        Broker(
            id = "uob",
            code = "AI",
            name = "UOB Kay Hian Sekuritas",
            displayLabel = "UOB Kay Hian Sekuritas (AI) — 0.25% / 0.35%",
            buy = 0.25,
            sell = 0.35,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.SWASTA_ASING,
            notes = "Tarif All-In UTRADE",
            websiteDomain = "utrade.co.id",
            fallbackDomain = "uobkayhian.com"
        ),

        // === MODE CUSTOM ===
        Broker(
            id = CUSTOM_ALL_IN_ID,
            code = "CST",
            name = "Custom — Tarif All-In",
            displayLabel = "Custom — Tarif All-In (Sudah termasuk Pajak & Levy)",
            buy = 0.15,
            sell = 0.25,
            feeMode = FeeMode.ALL_IN,
            category = BrokerCategory.CUSTOM,
            dayTrade = DayTradeConfig(available = true),
            notes = "Input total tarif All-In (termasuk pajak & levy bursa)"
        ),
        Broker(
            id = "custom_base",
            code = "BASE",
            name = "Custom — Base Fee Murni",
            displayLabel = "Custom — Base Fee Murni (+ Levy 0.0433%, PPN, & PPh 0.1%)",
            buy = 0.10,
            sell = 0.10,
            feeMode = FeeMode.BASE_FEE,
            category = BrokerCategory.CUSTOM,
            dayTrade = DayTradeConfig(available = true),
            notes = "Input komisi murni broker; biaya BEI, KPEI, KSEI, PPN, & PPh akan ditambahkan otomatis"
        )
    )

    fun findById(id: String): Broker =
        allBrokers.firstOrNull { it.id == id } ?: allBrokers.first()

    fun search(query: String): List<Broker> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return allBrokers
        return allBrokers.filter { broker ->
            broker.name.lowercase().contains(trimmed) ||
                broker.code.lowercase().contains(trimmed) ||
                broker.displayLabel.lowercase().contains(trimmed) ||
                broker.notes.lowercase().contains(trimmed)
        }
    }
}
