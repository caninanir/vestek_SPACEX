import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.caninanir.spacex.MainActivity
import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.presentation.ui.features.informationscreens.rockets.RocketsScreen
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
class FooTest {
    @get:Rule val hiltRule = HiltAndroidRule(this)

//    @Inject
//    lateinit var rocket: Rocket
    private val num = 10

    @Test
    fun testFoo() {
        hiltRule.inject()
        assertNotNull(num)
    }
}