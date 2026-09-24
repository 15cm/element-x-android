/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.features.messages.impl.threads.list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.matrix.api.core.ThreadId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal class ThreadsListStatePreviewParam : PreviewParameterProvider<ThreadsListState> {
    override val values = sequenceOf(
        aThreadsListState(isLoading = true),
        aThreadsListState(),
        aThreadsListState(threads = persistentListOf()),
        aThreadsListState(threads = persistentListOf(), initialLoadFailed = true),
        aThreadsListState(paginationFailed = true),
        aThreadsListState(isPaginating = true),
    )
}

private fun aThreadsListState(
    threads: kotlinx.collections.immutable.ImmutableList<ThreadListRowItem> =
        List(10) { aThreadListRowItem(threadId = ThreadId("\$thread-$it")) }.toImmutableList(),
    isLoading: Boolean = false,
    initialLoadFailed: Boolean = false,
    paginationFailed: Boolean = false,
    isPaginating: Boolean = false,
) = ThreadsListState(
    roomId = RoomId("!room-id:server"),
    roomName = "Room",
    roomAvatarUrl = null,
    isRoomTombstoned = false,
    heroes = persistentListOf(),
    threads = threads,
    isLoading = isLoading,
    initialLoadFailed = initialLoadFailed,
    paginationFailed = paginationFailed,
    isPaginating = isPaginating,
    eventSink = {},
)
