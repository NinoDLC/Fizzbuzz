package fr.delcey.fizzbuzz.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.fizzbuzz.CoroutineDispatchers
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_INT1
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_INT2
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_LIMIT
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_STR1
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_STR2
import fr.delcey.fizzbuzz.TestCoroutineRule
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import fr.delcey.fizzbuzz.getDefaultFizzbuzzRules
import fr.delcey.fizzbuzz.observeForTesting
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FizzbuzzListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val fizzbuzzRulesRepository: FizzbuzzRulesRepository = mockk()
    private val dispatchers = CoroutineDispatchers(testCoroutineRule.testCoroutineDispatcher, testCoroutineRule.testCoroutineDispatcher)

    @Before
    fun setUp() {
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(limit = 16))
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runBlockingTest {
        // Given
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(getDefaultFizzbuzzList(), it.value)
            verify(exactly = 1) { fizzbuzzRulesRepository.getRulesFlow() }
            confirmVerified(fizzbuzzRulesRepository)
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(null)
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertNull(it.value)
            verify(exactly = 1) { fizzbuzzRulesRepository.getRulesFlow() }
            confirmVerified(fizzbuzzRulesRepository)
        }
    }

    @Test
    fun `edge case - with limit = 1`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(limit = 1))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(listOf("1"), it.value)
        }
    }

    @Test
    fun `edge case - with limit = 0`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(limit = 0))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(emptyList<String>(), it.value)
        }
    }

    @Test
    fun `edge case - with limit = 2`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(limit = 2))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(listOf("1", "2"), it.value)
        }
    }

    @Test
    fun `edge case - with int1 == int2`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(int2 = DEFAULT_FIZZBUZZ_RULES_INT1))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting { liveData ->

            // Then
            assertEquals(
                getDefaultRawList().map {
                    if (it % DEFAULT_FIZZBUZZ_RULES_INT1 == 0) {
                        "$DEFAULT_FIZZBUZZ_RULES_STR1$DEFAULT_FIZZBUZZ_RULES_STR2"
                    } else {
                        it.toString()
                    }
                },
                liveData.value
            )
        }
    }

    @Test
    fun `edge case - with int1 == 0`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(int1 = 0))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting { liveData ->

            // Then
            assertEquals(
                getDefaultRawList().map {
                    if (it % DEFAULT_FIZZBUZZ_RULES_INT2 == 0) {
                        DEFAULT_FIZZBUZZ_RULES_STR2
                    } else {
                        it.toString()
                    }
                },
                liveData.value
            )
        }
    }

    @Test
    fun `edge case - with int2 == 0`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(int2 = 0))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting { liveData ->

            // Then
            assertEquals(
                getDefaultRawList().map {
                    if (it % DEFAULT_FIZZBUZZ_RULES_INT1 == 0) {
                        DEFAULT_FIZZBUZZ_RULES_STR1
                    } else {
                        it.toString()
                    }
                },
                liveData.value
            )
        }
    }

    @Test
    fun `edge case - with int1 == 0 and int2 == 0`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules(int1 = 0, int2 = 0))
        val viewModel = FizzbuzzListViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting { liveData ->

            // Then
            assertEquals(getDefaultRawList().map { it.toString() }, liveData.value)
        }
    }

    // region OUT
    private fun getDefaultFizzbuzzList(): List<String> = listOf(
        "1",
        "2",
        "fizz",
        "4",
        "buzz",
        "fizz",
        "7",
        "8",
        "fizz",
        "buzz",
        "11",
        "fizz",
        "13",
        "14",
        "fizzbuzz",
        "16"
    )

    private fun getDefaultRawList(): List<Int> = List(DEFAULT_FIZZBUZZ_RULES_LIMIT) {
        it + 1
    }
    // endregion OUT
}