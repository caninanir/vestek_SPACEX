package com.caninanir.spacex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caninanir.spacex.presentation.viewmodel.FavoritesViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import timber.log.Timber


@ExperimentalCoroutinesApi
class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firestore = mockk<FirebaseFirestore>(relaxed = true)
    private lateinit var favoritesViewModel: FavoritesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxed = true)
        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println("Timber log - [$tag]: $message")
            }
        })
    }

    @Test
    fun `fetchFavorites initializes with user favorites`() = runTest {
        val testEmail = "test12@gmail.com"
        val initialFavorites = mapOf("Falcon 1" to true, "Falcon 9" to false)
        val expectedFavorites = setOf("Falcon 1")

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }
        every { firebaseAuth.currentUser } returns firebaseUserMock

        val documentSnapshotMock = mockk<DocumentSnapshot>(relaxed = true) {
            every { data } returns mapOf("favorites" to initialFavorites)
        }
        val listenerMock = slot<(DocumentSnapshot?, FirebaseFirestoreException?) -> Unit>()

        val documentReferenceMock = mockk<DocumentReference>(relaxed = true)
        val listenerRegistrationMock = mockk<ListenerRegistration>(relaxed = true)

        // Correctly mock the nested calls
        every { firestore.collection("users").document(testEmail) } returns documentReferenceMock
        every { documentReferenceMock.addSnapshotListener(capture(listenerMock)) } returns listenerRegistrationMock

        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)

        listenerMock.captured(documentSnapshotMock, null)

        advanceUntilIdle()

        assertEquals(expectedFavorites, favoritesViewModel.favorites.value)
    }

    @Test
    fun `toggleFavorite updates favorite status in firestore`() = runTest {
        val testEmail = "test12@gmail.com"
        val rocketName = "Falcon 1"
        val initialFavorites = setOf("Falcon 1")

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }
        every { firebaseAuth.currentUser } returns firebaseUserMock

        val documentReferenceMock = mockk<DocumentReference>(relaxed = true)
        every { firestore.collection("users").document(testEmail) } returns documentReferenceMock

        // Simulate initial favorite status
        val documentSnapshotMock = mockk<DocumentSnapshot>(relaxed = true) {
            every { data } returns mapOf("favorites" to initialFavorites.associateWith { true })
        }
        val listenerMock = slot<(DocumentSnapshot?, FirebaseFirestoreException?) -> Unit>()
        every { documentReferenceMock.addSnapshotListener(capture(listenerMock)) } returns mockk(relaxed = true)

        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)

        // Trigger listener to set initial favorites
        listenerMock.captured(documentSnapshotMock, null)

        // Now toggle favorite
        favoritesViewModel.toggleFavorite(rocketName)

        advanceUntilIdle()

        verify { documentReferenceMock.update("favorites.Falcon 1", false) }
    }

    @Test
    fun `toggleFavorite adds favorite status in firestore`() = runTest {
        val testEmail = "test12@gmail.com"
        val rocketName = "Falcon 9"
        val initialFavorites = setOf("Falcon 1")

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }
        every { firebaseAuth.currentUser } returns firebaseUserMock

        val documentReferenceMock = mockk<DocumentReference>(relaxed = true)
        every { firestore.collection("users").document(testEmail) } returns documentReferenceMock

        // Simulate initial favorite status
        val documentSnapshotMock = mockk<DocumentSnapshot>(relaxed = true) {
            every { data } returns mapOf("favorites" to initialFavorites.associateWith { true })
        }
        val listenerMock = slot<(DocumentSnapshot?, FirebaseFirestoreException?) -> Unit>()
        every { documentReferenceMock.addSnapshotListener(capture(listenerMock)) } returns mockk(relaxed = true)

        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)

        // Trigger listener to set initial favorites
        listenerMock.captured(documentSnapshotMock, null)

        // Now toggle favorite
        favoritesViewModel.toggleFavorite(rocketName)

        advanceUntilIdle()

        verify { documentReferenceMock.update("favorites.Falcon 9", true) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the Main dispatcher to the original Main dispatcher
        unmockkAll()
        Timber.uprootAll() // Remove any Timber trees
    }
}