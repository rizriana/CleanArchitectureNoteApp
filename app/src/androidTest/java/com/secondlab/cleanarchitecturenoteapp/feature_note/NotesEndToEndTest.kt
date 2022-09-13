package com.secondlab.cleanarchitecturenoteapp.feature_note

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.secondlab.cleanarchitecturenoteapp.core.util.TestTags.CONTENT_TEXT_FIELDS
import com.secondlab.cleanarchitecturenoteapp.core.util.TestTags.NOTE_ITEM
import com.secondlab.cleanarchitecturenoteapp.core.util.TestTags.TITLE_TEXT_FIELDS
import com.secondlab.cleanarchitecturenoteapp.di.AppModule
import com.secondlab.cleanarchitecturenoteapp.feature_note.presentation.MainActivity
import com.secondlab.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.secondlab.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesScreen
import com.secondlab.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import com.secondlab.cleanarchitecturenoteapp.ui.theme.CleanArchitectureNoteAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            CleanArchitectureNoteAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route +
                                "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {
        // Click on FAB to get to add note screen
        composeRule.onNodeWithContentDescription("Add").performClick()

        // Enter texts in title and content text fields
        composeRule.onNodeWithTag(TITLE_TEXT_FIELDS).performTextInput("test-title")
        composeRule.onNodeWithTag(CONTENT_TEXT_FIELDS).performTextInput("test-content")

        // Save the new
        composeRule.onNodeWithContentDescription("Save").performClick()

        // Make sure there is a note in the list with out title and content
        composeRule.onNodeWithTag("test-title").assertIsDisplayed()

        // Click on note to edit it
        composeRule.onNodeWithText("test-title").performClick()

        // Make sure title and content text fields contain note title and content
        composeRule.onNodeWithTag(TITLE_TEXT_FIELDS).assertTextEquals("test-title")
        composeRule.onNodeWithTag(CONTENT_TEXT_FIELDS).assertTextEquals("test-content")

        // Add the text "2" to the title text fields
        composeRule.onNodeWithTag(TITLE_TEXT_FIELDS).performTextInput("2")

        // Update the note
        composeRule.onNodeWithContentDescription("Save").performClick()

        // Make sure the update was applied to the list
        composeRule.onNodeWithText("test-title2").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            // Click on FAB to get to add note screen
            composeRule.onNodeWithContentDescription("Add").performClick()

            // Enter texts in title and content text fields
            composeRule.onNodeWithTag(TITLE_TEXT_FIELDS).performTextInput(i.toString())
            composeRule.onNodeWithTag(CONTENT_TEXT_FIELDS).performTextInput(i.toString())

            // Save the new
            composeRule.onNodeWithContentDescription("Save").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()

        composeRule.onAllNodesWithTag(NOTE_ITEM)[0].assertTextContains("3")
        composeRule.onAllNodesWithTag(NOTE_ITEM)[1].assertTextContains("2")
        composeRule.onAllNodesWithTag(NOTE_ITEM)[2].assertTextContains("1")
    }
}