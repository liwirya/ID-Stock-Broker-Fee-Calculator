package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.BrokerPreferencesRepository
import com.example.domain.CalculationResult
import com.example.domain.StockFeeCalculator
import com.example.presentation.CalculatorViewModel
import com.example.ui.CalculatorScreen
import com.example.ui.components.ShareCardRenderer
import com.example.ui.components.ShareCardTheme
import com.example.ui.components.ShareDestination
import com.example.ui.theme.FeeSahamTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            val preferencesRepository = remember {
                BrokerPreferencesRepository(applicationContext)
            }
            val calculatorViewModel: CalculatorViewModel = viewModel(
                factory = CalculatorViewModel.Factory(preferencesRepository)
            )
            val uiState by calculatorViewModel.uiState.collectAsStateWithLifecycle()

            FeeSahamTheme {
                CalculatorScreen(
                    uiState = uiState,
                    onStockSymbolChanged = calculatorViewModel::onStockSymbolChanged,
                    onQuickTickerSelected = calculatorViewModel::onQuickTickerSelected,
                    onMarketSelected = calculatorViewModel::onMarketSelected,
                    onOpenBrokerSheet = calculatorViewModel::onOpenBrokerSheet,
                    onDismissBrokerSheet = calculatorViewModel::onDismissBrokerSheet,
                    onBrokerSearchQueryChanged = calculatorViewModel::onBrokerSearchQueryChanged,
                    onBrokerSelected = calculatorViewModel::onBrokerSelected,
                    onBuyPriceChanged = calculatorViewModel::onBuyPriceChanged,
                    onSellPriceChanged = calculatorViewModel::onSellPriceChanged,
                    onVolumeChanged = calculatorViewModel::onVolumeChanged,
                    onBuyFeeChanged = calculatorViewModel::onBuyFeeChanged,
                    onSellFeeChanged = calculatorViewModel::onSellFeeChanged,
                    onIncludeStampChanged = calculatorViewModel::onIncludeStampChanged,
                    onCalculateClicked = calculatorViewModel::onCalculateClicked,
                    onExampleBbcaClicked = calculatorViewModel::onExampleBbcaClicked,
                    onResetClicked = calculatorViewModel::onResetClicked,
                    onCopySummary = { result -> copyTradeConfirmation(result) },
                    onOpenShareSheet = calculatorViewModel::onOpenShareSheet,
                    onDismissShareSheet = calculatorViewModel::onDismissShareSheet,
                    onShareThemeSelected = calculatorViewModel::onShareThemeSelected,
                    onToggleShowRupiah = calculatorViewModel::onToggleShowRupiah,
                    onShareDestination = { result, theme, showRupiah, destination, onDone ->
                        handleShareCardDestination(result, theme, showRupiah, destination, onDone)
                    }
                )
            }
        }
    }

    private fun copyTradeConfirmation(result: CalculationResult) {
        val text = StockFeeCalculator.formatTradeConfirmationShareText(result)
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.app_name), text)
        clipboard.setPrimaryClip(clip)
    }

    private fun handleShareCardDestination(
        result: CalculationResult,
        theme: ShareCardTheme,
        showRupiah: Boolean,
        destination: ShareDestination,
        onDone: (Boolean) -> Unit
    ) {
        lifecycleScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                ShareCardRenderer.renderBitmap(
                    context = applicationContext,
                    result = result,
                    theme = theme,
                    showRupiah = showRupiah,
                    widthPx = 1080,
                    heightPx = 1350
                )
            }

            if (destination == ShareDestination.SAVE_GALLERY) {
                val stockTag = result.stockSymbol.ifBlank { "BEI" }
                val saved = withContext(Dispatchers.IO) {
                    ShareCardRenderer.saveCardToGallery(
                        context = applicationContext,
                        bitmap = bitmap,
                        fileBaseName = "FeeSaham_${stockTag}"
                    )
                }
                onDone(saved)
            } else {
                val caption = StockFeeCalculator.formatTradeConfirmationShareText(result)
                val chooserTitle = getString(R.string.share_chooser_title)
                val uri = withContext(Dispatchers.IO) {
                    ShareCardRenderer.writeShareCardToCache(applicationContext, bitmap)
                }
                try {
                    ShareCardRenderer.launchShareIntent(
                        context = this@MainActivity,
                        uri = uri,
                        destination = destination,
                        captionText = caption,
                        chooserTitle = chooserTitle
                    )
                    onDone(true)
                } catch (_: Exception) {
                    onDone(false)
                }
            }
        }
    }
}
