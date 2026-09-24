/*
 * Copyright (c) 2026 Element Creations Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

@file:OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)

package io.element.android.features.messages.impl.threads

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runAndroidComposeUiTest
import com.google.common.truth.Truth.assertThat
import io.element.android.features.messages.impl.threads.list.ThreadsListState
import io.element.android.features.messages.impl.threads.list.ThreadsListView
import io.element.android.features.messages.impl.threads.list.aThreadListRowItem
import io.element.android.libraries.designsystem.preview.USER_NAME_ALICE
import io.element.android.libraries.matrix.api.core.RoomId
import io.element.android.libraries.matrix.api.core.ThreadId
import io.element.android.tests.testutils.robolectric.RobolectricTest
import io.element.android.tests.testutils.setSafeContent
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class ThreadsListViewTest : RobolectricTest() {
    @Test
    fun `clicking a thread opens its timeline`() = runAndroidComposeUiTest<ComponentActivity> {
        val threadId = ThreadId("\$thread-id")
        var selectedThreadId: ThreadId? = null
        val state = ThreadsListState(
            roomId = RoomId("!room-id:server"),
            roomName = "Room",
            roomAvatarUrl = null,
            isRoomTombstoned = false,
            heroes = persistentListOf(),
            threads = persistentListOf(aThreadListRowItem(threadId = threadId)),
            isLoading = false,
            initialLoadFailed = false,
            paginationFailed = false,
            isPaginating = false,
            eventSink = {},
        )
        setSafeContent(clearAndroidUiDispatcher = true) {
            ThreadsListView(
                state = state,
                onThreadClick = { selectedThreadId = it },
                onBackClick = {},
            )
        }

        onNodeWithText(USER_NAME_ALICE).performClick()

        assertThat(selectedThreadId).isEqualTo(threadId)
    }
}
