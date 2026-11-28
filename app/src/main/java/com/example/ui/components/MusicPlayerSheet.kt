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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.music.MusicPlayerState
import com.example.music.RepeatMode
import com.example.music.formatMusicDuration
import com.example.ui.theme.LocalNeoBrutalistColors
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.SpaceMonoFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSheet(
    state: MusicPlayerState,
    onAddMp3Clicked: () -> Unit,
    onPlayTrackAt: (Int) -> Unit,
    onTogglePlayPause: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onDeleteTrack: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        containerColor = neo.canvas,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(3.dp, neo.ink))
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(neo.yellow)
                            .border(BorderStroke(2.dp, neo.ink)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = neo.ink,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.music_sheet_title).uppercase(),
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = neo.ink
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("close_music_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.cd_close_music),
                        tint = neo.ink
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(neo.ink)
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Now Playing Neo-Brutalist Control Deck
            NeoCard(
                backgroundColor = if (state.isPlaying) neo.green else neo.yellow,
                shadowOffset = 4.dp,
                contentPadding = PaddingValues(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val activeTrack = state.currentTrack
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(neo.ink)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.music_now_playing_label),
                            fontFamily = SpaceMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = neo.card
                        )
                    }
                    val repeatLabel = when (state.repeatMode) {
                        RepeatMode.OFF -> stringResource(R.string.music_repeat_off)
                        RepeatMode.REPEAT_ALL -> stringResource(R.string.music_repeat_all)
                        RepeatMode.REPEAT_ONE -> stringResource(R.string.music_repeat_one)
                    }
                    Text(
                        text = repeatLabel.uppercase(),
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        color = neo.ink
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = activeTrack?.title ?: stringResource(R.string.music_idle_title),
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = neo.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                val safeDuration = state.durationMs.coerceAtLeast(1L)
                val sliderPos = state.positionMs.coerceIn(0L, safeDuration).toFloat()
                Slider(
                    value = if (state.durationMs > 0L) sliderPos else 0f,
                    onValueChange = { newPos ->
                        if (state.durationMs > 0L) onSeekTo(newPos.toLong())
                    },
                    valueRange = 0f..safeDuration.toFloat(),
                    enabled = state.durationMs > 0L,
                    colors = SliderDefaults.colors(
                        thumbColor = neo.ink,
                        activeTrackColor = neo.ink,
                        inactiveTrackColor = neo.card
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("music_seek_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatMusicDuration(state.positionMs),
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = neo.ink
                    )
                    Text(
                        text = formatMusicDuration(state.durationMs),
                        fontFamily = SpaceMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = neo.ink
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Transport Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MusicControlSquareButton(
                        icon = if (state.repeatMode == RepeatMode.REPEAT_ONE) {
                            Icons.Rounded.RepeatOne
                        } else {
                            Icons.Rounded.Repeat
                        },
                        contentDescription = stringResource(R.string.cd_music_repeat),
                        backgroundColor = if (state.repeatMode == RepeatMode.OFF) neo.card else neo.blue,
                        testTag = "music_repeat_button",
                        onClick = onCycleRepeatMode
                    )
                    MusicControlSquareButton(
                        icon = Icons.Rounded.SkipPrevious,
                        contentDescription = stringResource(R.string.cd_music_prev),
                        backgroundColor = neo.card,
                        testTag = "music_prev_button",
                        onClick = onPreviousClicked
                    )
                    MusicControlSquareButton(
                        icon = if (state.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = stringResource(R.string.cd_music_play_pause),
                        backgroundColor = neo.ink,
                        iconTint = neo.card,
                        sizeDp = 56,
                        testTag = "music_play_pause_button",
                        onClick = onTogglePlayPause
                    )
                    MusicControlSquareButton(
                        icon = Icons.Rounded.SkipNext,
                        contentDescription = stringResource(R.string.cd_music_next),
                        backgroundColor = neo.card,
                        testTag = "music_next_button",
                        onClick = onNextClicked
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Add MP3 Button
            NeoButton(
                text = stringResource(R.string.music_add_mp3),
                onClick = onAddMp3Clicked,
                backgroundColor = neo.blue,
                shadowOffset = 4.dp,
                icon = Icons.Rounded.Add,
                fontSizeSp = 13.5f,
                testTag = "music_add_mp3_button",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Saved Playlist Section
            if (state.tracks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(neo.card)
                        .border(BorderStroke(2.dp, neo.ink))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.music_empty_title).uppercase(),
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = neo.ink,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.music_empty_subtitle),
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = neo.mutedInk,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                NeoSectionTag(
                    text = stringResource(R.string.music_saved_list_title, state.tracks.size)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(state.tracks, key = { _, t -> t.id }) { index, track ->
                        val isCurrent = index == state.currentTrackIndex
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isCurrent) neo.noteYellow else neo.card)
                                .border(BorderStroke(2.dp, neo.ink))
                                .defaultMinSize(minHeight = 52.dp)
                                .testTag("music_track_item_$index")
                                .clickable { onPlayTrackAt(index) }
                                .padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(if (isCurrent && state.isPlaying) neo.green else neo.ink),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCurrent && state.isPlaying) {
                                        Icons.Rounded.GraphicEq
                                    } else {
                                        Icons.Rounded.MusicNote
                                    },
                                    contentDescription = null,
                                    tint = if (isCurrent && state.isPlaying) neo.ink else neo.card,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = track.title,
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = neo.ink,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "#${index + 1} • MP3 Tersimpan",
                                    fontFamily = SpaceMonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp,
                                    color = neo.mutedInk
                                )
                            }
                            IconButton(
                                onClick = { onDeleteTrack(track.id) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("music_delete_track_$index")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DeleteOutline,
                                    contentDescription = stringResource(R.string.cd_music_delete),
                                    tint = neo.ink,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicControlSquareButton(
    icon: ImageVector,
    contentDescription: String,
    backgroundColor: Color,
    iconTint: Color = LocalNeoBrutalistColors.current.ink,
    sizeDp: Int = 48,
    testTag: String,
    onClick: () -> Unit
) {
    val neo = LocalNeoBrutalistColors.current
    Box(
        modifier = Modifier
            .padding(end = 3.dp, bottom = 3.dp)
            .drawBehind {
                val o = 3.dp.toPx()
                drawRect(neo.ink, Offset(o, o), Size(size.width, size.height))
            }
            .size(sizeDp.dp)
            .background(backgroundColor)
            .border(BorderStroke(2.5.dp, neo.ink))
            .testTag(testTag)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(26.dp)
        )
    }
}
