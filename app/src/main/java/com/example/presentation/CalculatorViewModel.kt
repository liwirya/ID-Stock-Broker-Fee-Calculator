package com.example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.BrokerPreferencesStore
import com.example.domain.Broker
import com.example.domain.BrokerCatalog
import com.example.domain.CalculationInput
import com.example.domain.CalculationResult
import com.example.domain.MarketType
import com.example.domain.StockFeeCalculator
import com.example.domain.UnitMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorUiState(
    val selectedMarket: MarketType = MarketType.REGULAR,
    val selectedBroker: Broker = BrokerCatalog.findById(BrokerCatalog.DEFAULT_BROKER_ID),
    val defaultBrokerId: String = BrokerCatalog.DEFAULT_BROKER_ID,
    val buyPriceText: String = "1000",
    val sellPriceText: String = "1100",
    val volumeText: String = "10",
    val buyFeePctText: String = "0.15",
    val sellFeePctText: String = "0.25",
    val includeStamp: Boolean = true,
    val totalSharesPreview: String = "1000",
    val hasCalculatedOnce: Boolean = false,
    val isStale: Boolean = false,
    val result: CalculationResult? = null,
    val scrollToResultTrigger: Int = 0,
    val isBrokerSheetOpen: Boolean = false,
    val brokerSearchQuery: String = ""
)

class CalculatorViewModel(
    private val preferencesStore: BrokerPreferencesStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val savedId = preferencesStore.defaultBrokerIdFlow.first()
            val savedBroker = BrokerCatalog.findById(savedId)
            _uiState.update { state ->
                val fees = resolveFees(state.selectedMarket, savedBroker, state.buyFeePctText, state.sellFeePctText)
                state.copy(
                    selectedBroker = savedBroker,
                    defaultBrokerId = savedBroker.id,
                    buyFeePctText = fees.first,
                    sellFeePctText = fees.second
                )
            }
        }
    }

    fun onMarketSelected(market: MarketType) {
        _uiState.update { state ->
            val currentVol = state.volumeText.toDoubleOrNull() ?: 0.0
            val adjustedVolText = when {
                market == MarketType.NEGO && currentVol <= 50.0 -> "150"
                market != MarketType.NEGO && currentVol >= 100.0 -> "10"
                else -> state.volumeText
            }
            val fees = resolveFees(market, state.selectedBroker, state.buyFeePctText, state.sellFeePctText)
            state.copy(
                selectedMarket = market,
                volumeText = adjustedVolText,
                buyFeePctText = fees.first,
                sellFeePctText = fees.second,
                totalSharesPreview = computeSharesPreview(market, adjustedVolText),
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onBrokerSelected(broker: Broker) {
        _uiState.update { state ->
            val fees = resolveFees(
                market = state.selectedMarket,
                broker = broker,
                currentBuyText = formatRate(broker.buy),
                currentSellText = formatRate(broker.sell)
            )
            state.copy(
                selectedBroker = broker,
                defaultBrokerId = broker.id,
                buyFeePctText = fees.first,
                sellFeePctText = fees.second,
                isBrokerSheetOpen = false,
                brokerSearchQuery = "",
                isStale = state.hasCalculatedOnce
            )
        }
        viewModelScope.launch {
            preferencesStore.saveDefaultBrokerId(broker.id)
        }
    }

    fun onBuyPriceChanged(raw: String) {
        val sanitized = sanitizeNumericInput(raw, allowDecimal = false)
        _uiState.update { state ->
            state.copy(
                buyPriceText = sanitized,
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onSellPriceChanged(raw: String) {
        val sanitized = sanitizeNumericInput(raw, allowDecimal = false)
        _uiState.update { state ->
            state.copy(
                sellPriceText = sanitized,
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onVolumeChanged(raw: String) {
        val sanitized = sanitizeNumericInput(raw, allowDecimal = false)
        _uiState.update { state ->
            state.copy(
                volumeText = sanitized,
                totalSharesPreview = computeSharesPreview(state.selectedMarket, sanitized),
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onBuyFeeChanged(raw: String) {
        val sanitized = sanitizeNumericInput(raw, allowDecimal = true)
        _uiState.update { state ->
            val updatedBroker = if (!state.selectedBroker.isCustom) {
                BrokerCatalog.findById(BrokerCatalog.CUSTOM_ALL_IN_ID)
            } else {
                state.selectedBroker
            }
            state.copy(
                selectedBroker = updatedBroker,
                buyFeePctText = sanitized,
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onSellFeeChanged(raw: String) {
        val sanitized = sanitizeNumericInput(raw, allowDecimal = true)
        _uiState.update { state ->
            val updatedBroker = if (!state.selectedBroker.isCustom) {
                BrokerCatalog.findById(BrokerCatalog.CUSTOM_ALL_IN_ID)
            } else {
                state.selectedBroker
            }
            state.copy(
                selectedBroker = updatedBroker,
                sellFeePctText = sanitized,
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onIncludeStampChanged(checked: Boolean) {
        _uiState.update { state ->
            state.copy(
                includeStamp = checked,
                isStale = state.hasCalculatedOnce
            )
        }
    }

    fun onCalculateClicked() {
        _uiState.update { state ->
            val calcResult = StockFeeCalculator.calculate(
                CalculationInput(
                    marketType = state.selectedMarket,
                    broker = state.selectedBroker,
                    buyPrice = state.buyPriceText.toDoubleOrNull() ?: 0.0,
                    sellPrice = state.sellPriceText.toDoubleOrNull() ?: 0.0,
                    volume = state.volumeText.toDoubleOrNull() ?: 0.0,
                    buyFeePct = state.buyFeePctText.toDoubleOrNull() ?: 0.0,
                    sellFeePct = state.sellFeePctText.toDoubleOrNull() ?: 0.0,
                    includeStamp = state.includeStamp
                )
            )
            state.copy(
                result = calcResult,
                hasCalculatedOnce = true,
                isStale = false,
                scrollToResultTrigger = state.scrollToResultTrigger + 1
            )
        }
    }

    fun onExampleBbcaClicked() {
        val stockbit = BrokerCatalog.findById(BrokerCatalog.DEFAULT_BROKER_ID)
        _uiState.update { state ->
            val market = MarketType.REGULAR
            val calcResult = StockFeeCalculator.calculate(
                CalculationInput(
                    marketType = market,
                    broker = stockbit,
                    buyPrice = 9500.0,
                    sellPrice = 9800.0,
                    volume = 10.0,
                    buyFeePct = stockbit.buy,
                    sellFeePct = stockbit.sell,
                    includeStamp = state.includeStamp
                )
            )
            state.copy(
                selectedMarket = market,
                selectedBroker = stockbit,
                buyPriceText = "9500",
                sellPriceText = "9800",
                volumeText = "10",
                buyFeePctText = formatRate(stockbit.buy),
                sellFeePctText = formatRate(stockbit.sell),
                totalSharesPreview = computeSharesPreview(market, "10"),
                result = calcResult,
                hasCalculatedOnce = true,
                isStale = false,
                scrollToResultTrigger = state.scrollToResultTrigger + 1
            )
        }
    }

    fun onResetClicked() {
        _uiState.update { state ->
            val defaultBroker = BrokerCatalog.findById(state.defaultBrokerId)
            val market = MarketType.REGULAR
            state.copy(
                selectedMarket = market,
                selectedBroker = defaultBroker,
                buyPriceText = "1000",
                sellPriceText = "1100",
                volumeText = "10",
                buyFeePctText = formatRate(defaultBroker.buy),
                sellFeePctText = formatRate(defaultBroker.sell),
                includeStamp = true,
                totalSharesPreview = computeSharesPreview(market, "10"),
                hasCalculatedOnce = false,
                isStale = false,
                result = null
            )
        }
    }

    fun onOpenBrokerSheet() {
        _uiState.update { it.copy(isBrokerSheetOpen = true, brokerSearchQuery = "") }
    }

    fun onDismissBrokerSheet() {
        _uiState.update { it.copy(isBrokerSheetOpen = false, brokerSearchQuery = "") }
    }

    fun onBrokerSearchQueryChanged(query: String) {
        _uiState.update { it.copy(brokerSearchQuery = query) }
    }

    private fun resolveFees(
        market: MarketType,
        broker: Broker,
        currentBuyText: String,
        currentSellText: String
    ): Pair<String, String> {
        val useDtBuy = market == MarketType.DAY_TRADE && broker.dayTrade.available && broker.dayTrade.buy != null
        val useDtSell = market == MarketType.DAY_TRADE && broker.dayTrade.available && broker.dayTrade.sell != null

        val buyText = when {
            market.freeBuy -> "0"
            broker.isCustom -> currentBuyText
            useDtBuy -> formatRate(broker.dayTrade.buy!!)
            else -> formatRate(broker.buy)
        }

        val sellText = when {
            broker.isCustom -> currentSellText
            useDtSell -> formatRate(broker.dayTrade.sell!!)
            else -> formatRate(broker.sell)
        }
        return buyText to sellText
    }

    private fun computeSharesPreview(market: MarketType, volumeText: String): String {
        val vol = volumeText.toDoubleOrNull() ?: 0.0
        val shares = if (market.unitMode == UnitMode.LOT) vol * 100.0 else vol
        return StockFeeCalculator.formatNumberClean(shares)
    }

    private fun formatRate(rate: Double): String = StockFeeCalculator.formatNumberClean(rate)

    private fun sanitizeNumericInput(raw: String, allowDecimal: Boolean): String {
        val normalized = raw.replace(',', '.')
        var dotSeen = false
        return buildString {
            for (ch in normalized) {
                when {
                    ch.isDigit() -> append(ch)
                    allowDecimal && ch == '.' && !dotSeen -> {
                        dotSeen = true
                        append(ch)
                    }
                }
            }
        }
    }

    class Factory(
        private val preferencesStore: BrokerPreferencesStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalculatorViewModel(preferencesStore) as T
        }
    }
}
