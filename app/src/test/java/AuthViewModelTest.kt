import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caninanir.spacex.presentation.viewmodel.AuthViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxed = true)
        authViewModel = AuthViewModel(firebaseAuth, firestore)
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println("Timber log - [$tag]: $message")
            }
        })
    }

    @Test
    fun `login with correct credentials should update userState`() = runTest {
        val testEmail = "test12@gmail.com"
        val testPassword = "123456"

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }

        val authResultMock = mockk<AuthResult> {
            every { user } returns firebaseUserMock
        }

        val taskMock: Task<AuthResult> = Tasks.forResult(authResultMock)

        coEvery { firebaseAuth.signInWithEmailAndPassword(testEmail, testPassword) } returns taskMock

        authViewModel.login(testEmail, testPassword)

        advanceUntilIdle() // Ensure all coroutines complete

        val user = authViewModel.userState.value
        assertEquals(testEmail, user?.email)
    }

    @Test
    fun `login with incorrect credentials should update loginErrorState`() = runTest {
        val testEmail = "test12@gmail.com"
        val testPassword = "wrongpassword"

        val errorMessage = "Your password or e-mail address is wrong."

        val taskMock: Task<AuthResult> = Tasks.forException(FirebaseAuthInvalidCredentialsException("", errorMessage))

        coEvery { firebaseAuth.signInWithEmailAndPassword(testEmail, testPassword) } returns taskMock

        authViewModel.login(testEmail, testPassword)

        advanceUntilIdle() // Ensure all coroutines complete

        val loginError = authViewModel.loginErrorState.value
        assertEquals(errorMessage, loginError)
    }

    @Test
    fun `logout should clear userState`() = runTest {
        val testEmail = "test12@gmail.com"
        val testPassword = "123456"

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }

        val authResultMock = mockk<AuthResult> {
            every { user } returns firebaseUserMock
        }

        val taskMock: Task<AuthResult> = Tasks.forResult(authResultMock)

        coEvery { firebaseAuth.signInWithEmailAndPassword(testEmail, testPassword) } returns taskMock

        authViewModel.login(testEmail, testPassword)
        advanceUntilIdle() // Ensure login is complete

        authViewModel.logout()
        advanceUntilIdle() // Ensure logout is complete

        val user = authViewModel.userState.value
        assertEquals(null, user)
    }

    @Test
    fun `create account with correct credentials should update userState`() = runTest {
        val testEmail = "test1234@gmail.com"
        val testPassword = "123456"

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }

        val authResultMock = mockk<AuthResult> {
            every { user } returns firebaseUserMock
        }

        val taskMock: Task<AuthResult> = Tasks.forResult(authResultMock)

        coEvery { firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword) } returns taskMock

        authViewModel.createAccount(testEmail, testPassword)

        advanceUntilIdle() // Ensure all coroutines complete

        val user = authViewModel.userState.value
        assertEquals(testEmail, user?.email)
    }

    @Test
    fun `create account with existing email should update loginErrorState`() = runTest {
        val testEmail = "test12@gmail.com"
        val testPassword = "123456"

        val errorMessage = "The email address is already in use by another account."

        val taskMock: Task<AuthResult> = Tasks.forException(FirebaseAuthUserCollisionException(errorMessage,""))

        coEvery { firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword) } returns taskMock

        authViewModel.createAccount(testEmail, testPassword)

        advanceUntilIdle() // Ensure all coroutines complete

        val loginError = authViewModel.loginErrorState.value
        assertEquals(errorMessage, loginError)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the Main dispatcher to the original Main dispatcher
        unmockkAll()
        Timber.uprootAll() // Remove any Timber trees
    }
}