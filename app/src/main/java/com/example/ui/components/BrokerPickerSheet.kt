package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.Broker
import com.example.domain.BrokerCatalog
import com.example.domain.BrokerCategory
import com.example.domain.StockFeeCalculator
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrokerPickerSheet(
    selectedBrokerId: String,
    defaultBrokerId: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBrokerSelected: (Broker) -> Unit,
    onDismiss: () -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredBrokers = remember(searchQuery) {
        BrokerCatalog.search(searchQuery)
    }
    val groupedBrokers = remember(filteredBrokers) {
        BrokerCategory.entries.mapNotNull { category ->
            val items = filteredBrokers.filter { it.category == category }
            if (items.isEmpty()) null else category to items
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(0.dp),
        containerColor = neo.canvas,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .border(BorderStroke(3.dp, neo.ink))
                .navigationBarsPadding()
        ) {
            // Neo-Brutalist Sheet Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(neo.yellow)
                    .border(BorderStroke(0.dp, neo.ink))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.broker_sheet_title).uppercase(),
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = neo.ink
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.BookmarkAdded,
                            contentDescription = null,
                            tint = neo.ink,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.broker_saved_default_hint),
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp,
                            color = neo.ink
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(48.dp)
                        .background(neo.card)
                        .border(BorderStroke(2.dp, neo.ink))
                        .testTag("close_broker_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.cd_close_sheet),
                        tint = neo.ink
                    )
                }
            }

            // Thick separator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(neo.ink)
            )

            // Search Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
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
                        .defaultMinSize(minHeight = 48.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.cd_search_broker),
                        tint = neo.ink,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = stringResource(R.string.broker_search_placeholder),
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = neo.mutedInk,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            cursorBrush = SolidColor(neo.ink),
                            textStyle = TextStyle(
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = neo.ink
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("broker_search_input")
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("clear_broker_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.cd_clear_search),
                                tint = neo.ink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            if (groupedBrokers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NeoCard(backgroundColor = neo.card, shadowOffset = 3.dp) {
                        Text(
                            text = stringResource(R.string.broker_empty_search),
                            style = MaterialTheme.typography.bodyMedium,
                            color = neo.ink
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    groupedBrokers.forEach { (category, brokers) ->
                        item(key = "header_${category.name}") {
                            NeoSectionTag(
                                text = category.title,
                                modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                            )
                        }
                        items(items = brokers, key = { it.id }) { broker ->
                            BrokerRowItem(
                                broker = broker,
                                isSelected = broker.id == selectedBrokerId,
                                isDefault = broker.id == defaultBrokerId,
                                onClick = { onBrokerSelected(broker) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BrokerRowItem(
    broker: Broker,
    isSelected: Boolean,
    isDefault: Boolean,
    onClick: () -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    val cardBg = if (isSelected) neo.noteYellow else neo.card

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 3.dp, bottom = 3.dp)
            .drawBehind {
                val o = 3.dp.toPx()
                drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
            }
            .background(cardBg)
            .border(BorderStroke(if (isSelected) 3.dp else 2.dp, neo.ink))
            .defaultMinSize(minHeight = 60.dp)
            .testTag("broker_item_${broker.id}")
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Broker Brand Logo Emblem
            BrokerLogoEmblem(
                broker = broker,
                size = 54.dp
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = broker.name,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.5.sp,
                    color = neo.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = broker.notes,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.5.sp,
                    color = neo.mutedInk,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val buyRate = StockFeeCalculator.formatNumberClean(broker.buy)
                    val sellRate = StockFeeCalculator.formatNumberClean(broker.sell)
                    Box(
                        modifier = Modifier
                            .background(neo.infoBox)
                            .border(BorderStroke(1.5.dp, neo.ink))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "B $buyRate% / J $sellRate%",
                            fontFamily = SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            color = neo.ink
                        )
                    }
                    if (isDefault) {
                        Box(
                            modifier = Modifier
                                .background(neo.green)
                                .border(BorderStroke(1.5.dp, neo.ink))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
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
                    if (broker.promotional) {
                        Box(
                            modifier = Modifier
                                .background(neo.purple)
                                .border(BorderStroke(1.5.dp, neo.ink))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.broker_promo_badge),
                                fontFamily = SpaceMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = neo.ink
                            )
                        }
                    }
                    if (broker.minimumFee != null) {
                        Box(
                            modifier = Modifier
                                .background(neo.orange)
                                .border(BorderStroke(1.5.dp, neo.ink))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.broker_min_fee_badge),
                                fontFamily = SpaceMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = neo.ink
                            )
                        }
                    }
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(neo.ink),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = neo.card,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
