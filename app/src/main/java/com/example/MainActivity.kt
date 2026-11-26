package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.BrokerPreferencesRepository
import com.example.domain.CalculationResult
import com.example.domain.StockFeeCalculator
import com.example.presentation.CalculatorViewModel
import com.example.ui.CalculatorScreen
import com.example.ui.theme.FeeSahamTheme

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
                    onShareSummary = { result -> shareTradeConfirmation(result) }
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

    private fun shareTradeConfirmation(result: CalculationResult) {
        val text = StockFeeCalculator.formatTradeConfirmationShareText(result)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(
            Intent.createChooser(shareIntent, getString(R.string.share_chooser_title))
        )
    }
}
