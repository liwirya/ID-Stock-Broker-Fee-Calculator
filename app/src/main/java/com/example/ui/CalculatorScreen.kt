package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.Broker
import com.example.domain.CalculationResult
import com.example.domain.FeeMode
import com.example.domain.MarketType
import com.example.music.MusicPlayerState
import com.example.presentation.CalculatorUiState
import com.example.ui.components.BrokerLogoEmblem
import com.example.ui.components.BrokerPickerSheet
import com.example.ui.components.CalculatingLoadingCard
import com.example.ui.components.FlexingShareSheet
import com.example.ui.components.MusicPlayerSheet
import com.example.ui.components.MutedLoopingVideoBackground
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.components.NeoMarketPill
import com.example.ui.components.NeoNumericField
import com.example.ui.components.NeoSectionTag
import com.example.ui.components.NetRdnResultCard
import com.example.ui.components.ShareCardTheme
import com.example.ui.components.ShareDestination
import com.example.ui.components.SocialCreditsFooter
import com.example.ui.components.SplashLoadingOverlay
import com.example.ui.components.TopRightMusicButton
import com.example.ui.components.TradeConfirmationCard
import com.example.ui.components.URL_GITHUB_LIWIRYA
import com.example.ui.components.URL_INSTAGRAM_LIWIRYA
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    uiState: CalculatorUiState,
    musicState: MusicPlayerState,
    isMusicSheetOpen: Boolean,
    musicSnackbarMessage: String?,
    onMusicSnackbarConsumed: () -> Unit,
    onOpenMusicSheet: () -> Unit,
    onDismissMusicSheet: () -> Unit,
    onAddMp3Clicked: () -> Unit,
    onPlayTrackAt: (Int) -> Unit,
    onToggleMusicPlayPause: () -> Unit,
    onNextTrackClicked: () -> Unit,
    onPreviousTrackClicked: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    onSeekMusicTo: (Long) -> Unit,
    onDeleteTrack: (String) -> Unit,
    onOpenExternalUrl: (String) -> Unit,
    onStockSymbolChanged: (String) -> Unit,
    onQuickTickerSelected: (String) -> Unit,
    onMarketSelected: (MarketType) -> Unit,
    onOpenBrokerSheet: () -> Unit,
    onDismissBrokerSheet: () -> Unit,
    onBrokerSearchQueryChanged: (String) -> Unit,
    onBrokerSelected: (Broker) -> Unit,
    onBuyPriceChanged: (String) -> Unit,
    onSellPriceChanged: (String) -> Unit,
    onVolumeChanged: (String) -> Unit,
    onBuyFeeChanged: (String) -> Unit,
    onSellFeeChanged: (String) -> Unit,
    onIncludeStampChanged: (Boolean) -> Unit,
    onCalculateClicked: () -> Unit,
    onExampleBbcaClicked: () -> Unit,
    onResetClicked: () -> Unit,
    onCopySummary: (CalculationResult) -> Unit,
    onOpenShareSheet: () -> Unit,
    onDismissShareSheet: () -> Unit,
    onShareThemeSelected: (ShareCardTheme) -> Unit,
    onToggleShowRupiah: (Boolean) -> Unit,
    onShareDestination: (CalculationResult, ShareCardTheme, Boolean, ShareDestination, (Boolean) -> Unit) -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val copiedMessage = stringResource(R.string.snackbar_copied)
    val savedGalleryMessage = stringResource(R.string.snackbar_saved_gallery)
    val saveFailedMessage = stringResource(R.string.snackbar_save_failed)

    var resultOffsetY by remember { mutableIntStateOf(0) }
    var isSplashFinished by remember { mutableStateOf(false) }

    LaunchedEffect(musicSnackbarMessage) {
        if (!musicSnackbarMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(musicSnackbarMessage)
            onMusicSnackbarConsumed()
        }
    }

    LaunchedEffect(uiState.isCalculating, uiState.scrollToResultTrigger) {
        if (uiState.isCalculating || uiState.scrollToResultTrigger > 0) {
            delay(140)
            val target = if (resultOffsetY > 0) resultOffsetY else scrollState.maxValue
            scrollState.animateScrollTo(target)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main app content smoothly fades & scales in only AFTER the splash transition finishes
        androidx.compose.animation.AnimatedVisibility(
            visible = isSplashFinished,
            enter = fadeIn(animationSpec = tween(durationMillis = 480)) +
                scaleIn(initialScale = 0.97f, animationSpec = tween(durationMillis = 480)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Muted, continuously looping video background
                MutedLoopingVideoBackground(
                    videoResId = R.raw.bg_video,
                    scrimAlpha = 0.20f,
                    modifier = Modifier.fillMaxSize()
                )

                Scaffold(
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(16.dp)
                        ) { data ->
                            Snackbar(
                                containerColor = neo.ink,
                                contentColor = neo.card,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
                            ) {
                                Text(
                                    text = data.visuals.message,
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .imePadding()
                    ) {
                val isWideScreen = maxWidth >= 840.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = if (isWideScreen) 1080.dp else 600.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        NeoHeaderCard(
                            isMusicPlaying = musicState.isPlaying,
                            savedTrackCount = musicState.tracks.size,
                            onMusicButtonClick = onOpenMusicSheet
                        )
                        RegulatorBadgesRow()

                        if (isWideScreen && (uiState.result != null || uiState.isCalculating)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                ParameterFormCard(
                                    uiState = uiState,
                                    onStockSymbolChanged = onStockSymbolChanged,
                                    onQuickTickerSelected = onQuickTickerSelected,
                                    onMarketSelected = onMarketSelected,
                                    onOpenBrokerSheet = onOpenBrokerSheet,
                                    onBuyPriceChanged = onBuyPriceChanged,
                                    onSellPriceChanged = onSellPriceChanged,
                                    onVolumeChanged = onVolumeChanged,
                                    onBuyFeeChanged = onBuyFeeChanged,
                                    onSellFeeChanged = onSellFeeChanged,
                                    onIncludeStampChanged = onIncludeStampChanged,
                                    onCalculateClicked = {
                                        focusManager.clearFocus()
                                        onCalculateClicked()
                                    },
                                    onExampleBbcaClicked = {
                                        focusManager.clearFocus()
                                        onExampleBbcaClicked()
                                    },
                                    onResetClicked = {
                                        focusManager.clearFocus()
                                        onResetClicked()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(18.dp)
                                ) {
                                    if (uiState.isCalculating) {
                                        CalculatingLoadingCard()
                                    } else {
                                        val res = uiState.result
                                        if (res != null) {
                                            NetRdnResultCard(
                                                result = res,
                                                onCopySummary = {
                                                    onCopySummary(res)
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(copiedMessage)
                                                    }
                                                },
                                                onShareSummary = onOpenShareSheet
                                            )
                                            TradeConfirmationCard(result = res)
                                        }
                                    }
                                }
                            }
                        } else {
                            ParameterFormCard(
                                uiState = uiState,
                                onStockSymbolChanged = onStockSymbolChanged,
                                onQuickTickerSelected = onQuickTickerSelected,
                                onMarketSelected = onMarketSelected,
                                onOpenBrokerSheet = onOpenBrokerSheet,
                                onBuyPriceChanged = onBuyPriceChanged,
                                onSellPriceChanged = onSellPriceChanged,
                                onVolumeChanged = onVolumeChanged,
                                onBuyFeeChanged = onBuyFeeChanged,
                                onSellFeeChanged = onSellFeeChanged,
                                onIncludeStampChanged = onIncludeStampChanged,
                                onCalculateClicked = {
                                    focusManager.clearFocus()
                                    onCalculateClicked()
                                },
                                onExampleBbcaClicked = {
                                    focusManager.clearFocus()
                                    onExampleBbcaClicked()
                                },
                                onResetClicked = {
                                    focusManager.clearFocus()
                                    onResetClicked()
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coords ->
                                        resultOffsetY = coords.positionInParent().y.toInt()
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(18.dp)
                                ) {
                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = uiState.isCalculating,
                                        enter = fadeIn() + expandVertically(),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        CalculatingLoadingCard()
                                    }

                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = !uiState.isCalculating && uiState.result != null,
                                        enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                                            expandVertically(spring(stiffness = Spring.StiffnessMediumLow)),
                                        exit = fadeOut() + shrinkVertically()
                                    ) {
                                        val res = uiState.result
                                        if (res != null) {
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalArrangement = Arrangement.spacedBy(18.dp)
                                            ) {
                                                NetRdnResultCard(
                                                    result = res,
                                                    onCopySummary = {
                                                        onCopySummary(res)
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar(copiedMessage)
                                                        }
                                                    },
                                                    onShareSummary = onOpenShareSheet
                                                )
                                                TradeConfirmationCard(result = res)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        TransparencyNoteBox()
                        SocialCreditsFooter(
                            onOpenInstagram = { onOpenExternalUrl(URL_INSTAGRAM_LIWIRYA) },
                            onOpenGitHub = { onOpenExternalUrl(URL_GITHUB_LIWIRYA) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
                }
            }
        }

        SplashLoadingOverlay(
            visible = !isSplashFinished,
            onLoadingFinished = { isSplashFinished = true },
            modifier = Modifier.fillMaxSize()
        )
    }

    if (uiState.isBrokerSheetOpen) {
        BrokerPickerSheet(
            selectedBrokerId = uiState.selectedBroker.id,
            defaultBrokerId = uiState.defaultBrokerId,
            searchQuery = uiState.brokerSearchQuery,
            onSearchQueryChange = onBrokerSearchQueryChanged,
            onBrokerSelected = onBrokerSelected,
            onDismiss = onDismissBrokerSheet
        )
    }

    if (isMusicSheetOpen) {
        MusicPlayerSheet(
            state = musicState,
            onAddMp3Clicked = onAddMp3Clicked,
            onPlayTrackAt = onPlayTrackAt,
            onTogglePlayPause = onToggleMusicPlayPause,
            onNextClicked = onNextTrackClicked,
            onPreviousClicked = onPreviousTrackClicked,
            onCycleRepeatMode = onCycleRepeatMode,
            onSeekTo = onSeekMusicTo,
            onDeleteTrack = onDeleteTrack,
            onDismiss = onDismissMusicSheet
        )
    }

    val activeResult = uiState.result
    if (uiState.isShareSheetOpen && activeResult != null) {
        FlexingShareSheet(
            result = activeResult,
            selectedTheme = uiState.selectedShareTheme,
            showRupiah = uiState.showRupiahOnCard,
            onThemeSelected = onShareThemeSelected,
            onToggleShowRupiah = onToggleShowRupiah,
            onShareDestination = { destination ->
                onShareDestination(
                    activeResult,
                    uiState.selectedShareTheme,
                    uiState.showRupiahOnCard,
                    destination
                ) { savedOk ->
                    if (destination == ShareDestination.SAVE_GALLERY) {
                        onDismissShareSheet()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (savedOk) savedGalleryMessage else saveFailedMessage
                            )
                        }
                    }
                }
            },
            onDismiss = onDismissShareSheet
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NeoHeaderCard(
    isMusicPlaying: Boolean,
    savedTrackCount: Int,
    onMusicButtonClick: () -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    NeoCard(
        backgroundColor = neo.yellow,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(neo.ink)
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = stringResource(R.string.header_tag),
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        letterSpacing = 0.8.sp,
                        color = neo.card
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.header_title_prefix),
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 23.sp,
                        lineHeight = 27.sp,
                        letterSpacing = (-0.5).sp,
                        color = neo.ink
                    )
                    Box(
                        modifier = Modifier
                            .rotate(-1.5f)
                            .background(neo.green)
                            .border(BorderStroke(2.dp, neo.ink))
                            .padding(horizontal = 7.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.header_title_highlight),
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 23.sp,
                            lineHeight = 27.sp,
                            letterSpacing = (-0.5).sp,
                            color = neo.ink
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.header_subtitle),
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    color = neo.ink
                )
            }

            TopRightMusicButton(
                isPlaying = isMusicPlaying,
                trackCount = savedTrackCount,
                onClick = onMusicButtonClick
            )
        }
    }
}

@Composable
private fun RegulatorBadgesRow() {
    val neo = LocalNeoBrutalistColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RegulatorBadge(
            value = stringResource(R.string.badge_brokers_val),
            label = stringResource(R.string.badge_brokers_lbl),
            backgroundColor = neo.blue,
            modifier = Modifier.weight(1f)
        )
        RegulatorBadge(
            value = stringResource(R.string.badge_levy_val),
            label = stringResource(R.string.badge_levy_lbl),
            backgroundColor = neo.purple,
            modifier = Modifier.weight(1f)
        )
        RegulatorBadge(
            value = stringResource(R.string.badge_pph_val),
            label = stringResource(R.string.badge_pph_lbl),
            backgroundColor = neo.orange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RegulatorBadge(
    value: String,
    label: String,
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
            .padding(horizontal = 6.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontFamily = SpaceMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = neo.ink,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label.uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                color = neo.ink,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParameterFormCard(
    uiState: CalculatorUiState,
    onStockSymbolChanged: (String) -> Unit,
    onQuickTickerSelected: (String) -> Unit,
    onMarketSelected: (MarketType) -> Unit,
    onOpenBrokerSheet: () -> Unit,
    onBuyPriceChanged: (String) -> Unit,
    onSellPriceChanged: (String) -> Unit,
    onVolumeChanged: (String) -> Unit,
    onBuyFeeChanged: (String) -> Unit,
    onSellFeeChanged: (String) -> Unit,
    onIncludeStampChanged: (Boolean) -> Unit,
    onCalculateClicked: () -> Unit,
    onExampleBbcaClicked: () -> Unit,
    onResetClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neo = LocalNeoBrutalistColors.current
    val market = uiState.selectedMarket
    val broker = uiState.selectedBroker
    val isBaseMode = broker.feeMode == FeeMode.BASE_FEE

    NeoCard(modifier = modifier.fillMaxWidth()) {
        // Card Title + Market Pill
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.card_title_parameters).uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = neo.ink
            )
            NeoMarketPill(text = market.badge)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(neo.ink)
        )

        // Market Type Section
        Spacer(modifier = Modifier.height(14.dp))
        NeoSectionTag(text = stringResource(R.string.section_market_type))
        Spacer(modifier = Modifier.height(10.dp))

        MarketSelectorDropdown(
            selectedMarket = market,
            onMarketSelected = onMarketSelected
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Market Description Info Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(neo.infoBox)
                .border(BorderStroke(2.dp, neo.ink))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append(market.descTitle)
                        append(" ")
                    }
                    append(market.descBody)
                },
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
                color = neo.ink
            )
        }

        // Price & Volume Section
        Spacer(modifier = Modifier.height(14.dp))
        NeoSectionTag(text = stringResource(R.string.section_price_volume))
        Spacer(modifier = Modifier.height(10.dp))

        StockSymbolInputSection(
            value = uiState.stockSymbolText,
            onValueChange = onStockSymbolChanged,
            onQuickTickerSelected = onQuickTickerSelected
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeoNumericField(
                label = stringResource(R.string.label_buy_price),
                value = uiState.buyPriceText,
                onValueChange = onBuyPriceChanged,
                keyboardType = KeyboardType.Number,
                testTag = "buy_price_input",
                modifier = Modifier.weight(1f)
            )
            NeoNumericField(
                label = stringResource(R.string.label_sell_price),
                value = uiState.sellPriceText,
                onValueChange = onSellPriceChanged,
                keyboardType = KeyboardType.Number,
                testTag = "sell_price_input",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeoNumericField(
                label = market.volLabel,
                value = uiState.volumeText,
                onValueChange = onVolumeChanged,
                keyboardType = KeyboardType.Number,
                testTag = "volume_input",
                modifier = Modifier.weight(1f)
            )
            NeoNumericField(
                label = stringResource(R.string.label_total_shares),
                value = uiState.totalSharesPreview,
                onValueChange = {},
                enabled = false,
                testTag = "total_shares_preview",
                modifier = Modifier.weight(1f)
            )
        }

        // Broker Section
        Spacer(modifier = Modifier.height(14.dp))
        NeoSectionTag(text = stringResource(R.string.section_broker))
        Spacer(modifier = Modifier.height(10.dp))

        BrokerSelectorTrigger(
            broker = broker,
            isDefault = broker.id == uiState.defaultBrokerId,
            onClick = onOpenBrokerSheet
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Broker Note Yellow Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(neo.noteYellow)
                .border(BorderStroke(2.dp, neo.ink))
                .padding(horizontal = 12.dp, vertical = 9.dp)
        ) {
            Text(
                text = broker.notes,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = neo.ink
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeoNumericField(
                label = stringResource(
                    if (isBaseMode) R.string.label_buy_fee_base else R.string.label_buy_fee_all_in
                ),
                value = uiState.buyFeePctText,
                onValueChange = onBuyFeeChanged,
                enabled = !market.freeBuy,
                keyboardType = KeyboardType.Decimal,
                testTag = "buy_fee_input",
                modifier = Modifier.weight(1f)
            )
            NeoNumericField(
                label = stringResource(
                    if (isBaseMode) R.string.label_sell_fee_base else R.string.label_sell_fee_all_in
                ),
                value = uiState.sellFeePctText,
                onValueChange = onSellFeeChanged,
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
                testTag = "sell_fee_input",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // e-Meterai Checkbox Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(neo.checkboxSurface)
                .border(BorderStroke(2.dp, neo.ink))
                .defaultMinSize(minHeight = 48.dp)
                .testTag("include_stamp_checkbox")
                .clickable { onIncludeStampChanged(!uiState.includeStamp) }
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.includeStamp,
                onCheckedChange = onIncludeStampChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = neo.ink,
                    uncheckedColor = neo.ink,
                    checkmarkColor = neo.card
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.label_include_stamp),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                color = neo.ink
            )
        }

        // Stale Warning Banner
        AnimatedVisibility(
            visible = uiState.isStale,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .background(neo.orange)
                    .border(BorderStroke(2.dp, neo.ink))
                    .padding(horizontal = 12.dp, vertical = 9.dp)
                    .testTag("stale_warning_banner"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.WarningAmber,
                    contentDescription = stringResource(R.string.cd_warning_icon),
                    tint = neo.ink,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.stale_warning_banner).uppercase(),
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.5.sp,
                    color = neo.ink
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Primary Calculate Button
        NeoButton(
            text = stringResource(R.string.btn_calculate),
            onClick = onCalculateClicked,
            backgroundColor = neo.green,
            pressedColor = neo.greenPressed,
            shadowOffset = 5.dp,
            trailingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
            fontSizeSp = 15f,
            testTag = "calculate_button",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Secondary Action Buttons (BBCA Example & Reset)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeoButton(
                text = stringResource(R.string.btn_example_bbca),
                onClick = onExampleBbcaClicked,
                backgroundColor = neo.yellow,
                shadowOffset = 3.dp,
                icon = Icons.Rounded.TrendingUp,
                fontSizeSp = 11.5f,
                testTag = "example_bbca_button",
                modifier = Modifier.weight(1f)
            )
            NeoButton(
                text = stringResource(R.string.btn_reset_form),
                onClick = onResetClicked,
                backgroundColor = neo.card,
                shadowOffset = 3.dp,
                icon = Icons.Rounded.Refresh,
                fontSizeSp = 11.5f,
                testTag = "reset_form_button",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MarketSelectorDropdown(
    selectedMarket: MarketType,
    onMarketSelected: (MarketType) -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_select_market).uppercase(),
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            color = neo.ink,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 3.dp, bottom = 3.dp)
                    .drawBehind {
                        val o = 3.dp.toPx()
                        drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
                    }
                    .background(if (expanded) neo.noteYellow else neo.card)
                    .border(BorderStroke(3.dp, neo.ink))
                    .defaultMinSize(minHeight = 50.dp)
                    .testTag("market_selector_button")
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedMarket.optionLabel,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = neo.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.cd_select_market),
                    tint = neo.ink,
                    modifier = Modifier.size(22.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(neo.card)
                    .border(BorderStroke(3.dp, neo.ink))
            ) {
                MarketType.entries.forEach { marketOption ->
                    val isSelected = marketOption == selectedMarket
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = marketOption.optionLabel,
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = neo.ink,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = neo.ink,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            expanded = false
                            onMarketSelected(marketOption)
                        },
                        modifier = Modifier
                            .background(if (isSelected) neo.noteYellow else neo.card)
                            .testTag("market_option_${marketOption.name.lowercase()}")
                    )
                }
            }
        }
    }
}

@Composable
private fun BrokerSelectorTrigger(
    broker: Broker,
    isDefault: Boolean,
    onClick: () -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_select_broker).uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp,
                color = neo.ink
            )
            if (isDefault) {
                Box(
                    modifier = Modifier
                        .background(neo.green)
                        .border(BorderStroke(1.5.dp, neo.ink))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = stringResource(R.string.broker_default_badge),
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = neo.ink
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 3.dp, bottom = 3.dp)
                .drawBehind {
                    val o = 3.dp.toPx()
                    drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
                }
                .background(neo.card)
                .border(BorderStroke(3.dp, neo.ink))
                .defaultMinSize(minHeight = 52.dp)
                .testTag("broker_selector_button")
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BrokerLogoEmblem(
                broker = broker,
                size = 46.dp
            )
            Text(
                text = broker.displayLabel,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = neo.ink,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.cd_select_broker),
                tint = neo.ink,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun TransparencyNoteBox() {
    val neo = LocalNeoBrutalistColors.current
    NeoCard(
        backgroundColor = neo.card,
        shadowOffset = 3.dp,
        contentPadding = PaddingValues(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(neo.yellow)
                .border(BorderStroke(1.5.dp, neo.ink))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = stringResource(R.string.note_transparency_title).uppercase(),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.5.sp,
                color = neo.ink
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.note_transparency_body),
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.5.sp,
            lineHeight = 18.sp,
            color = neo.ink
        )
    }
}

private val QUICK_BEI_TICKERS = listOf("BBCA", "BBRI", "BMRI", "TLKM", "ANTM", "BREN")

@Composable
private fun StockSymbolInputSection(
    value: String,
    onValueChange: (String) -> Unit,
    onQuickTickerSelected: (String) -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val fieldBg = if (isFocused) neo.noteYellow else neo.card
    val shadowDp = if (isFocused) 4.dp else 3.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_stock_symbol).uppercase(),
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            color = neo.ink,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = shadowDp, bottom = shadowDp)
                .drawBehind {
                    val offsetPx = shadowDp.toPx()
                    drawRect(
                        color = neo.ink,
                        topLeft = Offset(offsetPx, offsetPx),
                        size = Size(size.width, size.height)
                    )
                }
                .background(fieldBg)
                .border(BorderStroke(3.dp, neo.ink))
                .defaultMinSize(minHeight = 48.dp)
                .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.placeholder_stock_symbol),
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = neo.mutedInk,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(neo.ink),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    textStyle = TextStyle(
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = neo.ink
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stock_symbol_input")
                )
            }
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("clear_stock_symbol_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.cd_clear_stock),
                        tint = neo.ink,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QUICK_BEI_TICKERS.forEach { ticker ->
                val isSelected = value.equals(ticker, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 56.dp, minHeight = 48.dp)
                        .background(if (isSelected) neo.yellow else neo.card)
                        .border(BorderStroke(2.dp, neo.ink))
                        .testTag("quick_ticker_${ticker.lowercase()}")
                        .clickable { onQuickTickerSelected(ticker) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ticker,
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = neo.ink
                    )
                }
            }
        }
    }
}
