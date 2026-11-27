package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.CalculationResult
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexingShareSheet(
    result: CalculationResult,
    selectedTheme: ShareCardTheme,
    showRupiah: Boolean,
    onThemeSelected: (ShareCardTheme) -> Unit,
    onToggleShowRupiah: (Boolean) -> Unit,
    onShareDestination: (ShareDestination) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val neo = LocalNeoBrutalistColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val previewBitmap = remember(result, selectedTheme, showRupiah) {
        ShareCardRenderer.renderBitmap(
            context = context,
            result = result,
            theme = selectedTheme,
            showRupiah = showRupiah,
            widthPx = 540,
            heightPx = 675
        )
    }

    val thumbnailBitmaps = remember(result, showRupiah) {
        ShareCardTheme.entries.associateWith { theme ->
            ShareCardRenderer.renderBitmap(
                context = context,
                result = result,
                theme = theme,
                showRupiah = showRupiah,
                widthPx = 160,
                heightPx = 200
            )
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFF15171C),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(top = 14.dp, bottom = 22.dp)
        ) {
            // Header ("Portfolio Flexing" + X)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.flexing_sheet_title),
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(48.dp)
                        .testTag("close_flexing_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.cd_close_flexing),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Live Card Preview (4:5 portrait)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = previewBitmap.asImageBitmap(),
                    contentDescription = "Preview kartu flexing saham",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .aspectRatio(0.8f)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            BorderStroke(1.5.dp, Color(0xFF2D3340)),
                            RoundedCornerShape(20.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle Show/Hide Rupiah Amount
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.flexing_toggle_nominal),
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = Color(0xFFE2E8F0)
                )
                Switch(
                    checked = showRupiah,
                    onCheckedChange = onToggleShowRupiah,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = neo.ink,
                        checkedTrackColor = neo.green,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFF334155)
                    ),
                    modifier = Modifier.testTag("flexing_toggle_rupiah_switch")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Theme Thumbnails Strip
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(ShareCardTheme.entries) { theme ->
                    val isSelected = theme == selectedTheme
                    val thumb = thumbnailBitmaps[theme]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(74.dp)
                            .testTag("share_theme_${theme.name.lowercase()}")
                            .clickable { onThemeSelected(theme) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 74.dp, height = 92.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    BorderStroke(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) neo.green else Color(0xFF334155)
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                        ) {
                            if (thumb != null) {
                                Image(
                                    bitmap = thumb.asImageBitmap(),
                                    contentDescription = theme.label,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = theme.label,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.5.sp,
                            color = if (isSelected) neo.green else Color(0xFF94A3B8),
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // "Bagikan ke" Title + 5 Circular Share Buttons
            Text(
                text = stringResource(R.string.flexing_share_to),
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShareCircleAction(
                    label = stringResource(R.string.flexing_action_save),
                    background = Brush.linearGradient(listOf(Color.White, Color(0xFFE2E8F0))),
                    icon = Icons.Rounded.Download,
                    iconTint = Color(0xFF111111),
                    testTag = "share_action_save",
                    onClick = { onShareDestination(ShareDestination.SAVE_GALLERY) }
                )
                ShareCircleAction(
                    label = stringResource(R.string.flexing_action_story),
                    background = Brush.linearGradient(
                        listOf(
                            Color(0xFFF58529),
                            Color(0xFFDD2A7B),
                            Color(0xFF8134AF)
                        )
                    ),
                    icon = Icons.Rounded.CameraAlt,
                    iconTint = Color.White,
                    testTag = "share_action_story",
                    onClick = { onShareDestination(ShareDestination.INSTAGRAM_STORY) }
                )
                ShareCircleAction(
                    label = stringResource(R.string.flexing_action_whatsapp),
                    background = Brush.linearGradient(listOf(Color(0xFF25D366), Color(0xFF128C7E))),
                    icon = Icons.AutoMirrored.Rounded.Chat,
                    iconTint = Color.White,
                    testTag = "share_action_whatsapp",
                    onClick = { onShareDestination(ShareDestination.WHATSAPP) }
                )
                ShareCircleTextAction(
                    label = stringResource(R.string.flexing_action_facebook),
                    background = Brush.linearGradient(listOf(Color(0xFF1877F2), Color(0xFF1558B0))),
                    glyph = "f",
                    glyphTint = Color.White,
                    testTag = "share_action_facebook",
                    onClick = { onShareDestination(ShareDestination.FACEBOOK) }
                )
                ShareCircleAction(
                    label = stringResource(R.string.flexing_action_more),
                    background = Brush.linearGradient(listOf(Color.White, Color(0xFFE2E8F0))),
                    icon = Icons.Rounded.MoreVert,
                    iconTint = Color(0xFF111111),
                    testTag = "share_action_more",
                    onClick = { onShareDestination(ShareDestination.MORE) }
                )
            }
        }
    }
}

@Composable
private fun ShareCircleAction(
    label: String,
    background: Brush,
    icon: ImageVector,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .testTag(testTag)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.5.sp,
            color = Color(0xFFCBD5E1),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ShareCircleTextAction(
    label: String,
    background: Brush,
    glyph: String,
    glyphTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .testTag(testTag)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = glyph,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                color = glyphTint
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.5.sp,
            color = Color(0xFFCBD5E1),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}
