import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SimpleUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testButtonClick() {
        composeTestRule.setContent {
            var count by remember { mutableStateOf(0) }
            Button(onClick = { count++ }) {
                Text("Clicked $count times")
            }
        }

        composeTestRule.onNodeWithText("Clicked 0 times")
            .performClick()
        composeTestRule.onNodeWithText("Clicked 1 times").assertExists()
    }
}