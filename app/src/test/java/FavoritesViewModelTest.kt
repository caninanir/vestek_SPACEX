package com.caninanir.spacex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.caninanir.spacex.presentation.viewmodel.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
        val listenerSlot = slot<EventListener<DocumentSnapshot>>()

        val documentReferenceMock = mockk<DocumentReference>(relaxed = true)
        val listenerRegistrationMock = mockk<ListenerRegistration>(relaxed = true)

        every { firestore.collection("users").document(testEmail) } returns documentReferenceMock
        every { documentReferenceMock.addSnapshotListener(capture(listenerSlot)) } returns listenerRegistrationMock

        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)

        // Trigger the listener
        listenerSlot.captured.onEvent(documentSnapshotMock, null)

        advanceUntilIdle()

        assertEquals(expectedFavorites, favoritesViewModel.favorites.value)
    }

    @Test
    fun `toggleFavorite updates favorite status in firestore`() = runTest {
        val testEmail = "test12@gmail.com"
        val rocketName = "Falcon 1"
        val initialState = mapOf("favorites" to mapOf(rocketName to true))

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }
        every { firebaseAuth.currentUser } returns firebaseUserMock

        val documentSnapshotMock = mockk<DocumentSnapshot>(relaxed = true) {
            every { data } returns initialState
        }
        val listenerSlot = slot<EventListener<DocumentSnapshot>>()
        val documentReferenceMock = mockk<DocumentReference>(relaxed = true)

        every { firestore.collection("users").document(testEmail) } returns documentReferenceMock
        every { documentReferenceMock.addSnapshotListener(capture(listenerSlot)) } returns mockk<ListenerRegistration>(relaxed = true)

        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)

        // Trigger listener to set initial favorites
        listenerSlot.captured.onEvent(documentSnapshotMock, null)

        favoritesViewModel.toggleFavorite(rocketName)

        advanceUntilIdle()

        verify { documentReferenceMock.update("favorites.$rocketName", false) }
    }

    @Test
    fun `toggleFavorite adds favorite status in firestore`() = runTest {
        val testEmail = "test12@gmail.com"
        val rocketName = "Falcon 9"
        val initialState = mapOf("favorites" to mapOf("Falcon 1" to true))

        val firebaseUserMock = mockk<FirebaseUser>(relaxed = true) {
            every { email } returns testEmail
        }
        every { firebaseAuth.currentUser } returns firebaseUserMock

        val documentSnapshotMock = mockk<DocumentSnapshot>(relaxed = true) {
            every { data } returns initialState
        }
        val listenerSlot = slot<EventListener<DocumentSnapshot>>()
        val documentReferenceMock = mockk<DocumentReference>(relaxed = true)

        every { firestore.collection("users").document(testEmail) } returns documentReferenceMock
        every { documentReferenceMock.addSnapshotListener(capture(listenerSlot)) } returns mockk<ListenerRegistration>(relaxed = true)

        favoritesViewModel = FavoritesViewModel(firebaseAuth, firestore)

        // Trigger listener to set initial favorites
        listenerSlot.captured.onEvent(documentSnapshotMock, null)

        favoritesViewModel.toggleFavorite(rocketName)

        advanceUntilIdle()

        verify { documentReferenceMock.update("favorites.$rocketName", true) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
        Timber.uprootAll()
    }
}