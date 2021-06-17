package fr.delcey.fizzbuzz.ui.stats

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.fizzbuzz.CoroutineDispatchers
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_INT1
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_INT2
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_LIMIT
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_STR1
import fr.delcey.fizzbuzz.DEFAULT_FIZZBUZZ_RULES_STR2
import fr.delcey.fizzbuzz.R
import fr.delcey.fizzbuzz.TestCoroutineRule
import fr.delcey.fizzbuzz.data.FizzbuzzRules
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import fr.delcey.fizzbuzz.getDefaultFizzbuzzRules
import fr.delcey.fizzbuzz.observeForTesting
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatsViewModelTest {

    companion object {
        private const val EXPECTED_STRING_NONE = "none"
        private const val EXPECTED_STRING_STATS_TEMPLATE = "stats_template"
        private const val EXPECTED_STRING_PLURAL_TIMES = "times"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val application: Application = mockk()
    private val fizzbuzzRulesRepository: FizzbuzzRulesRepository = mockk()
    private val dispatchers = CoroutineDispatchers(testCoroutineRule.testCoroutineDispatcher, testCoroutineRule.testCoroutineDispatcher)

    @Before
    fun setUp() {
        every { application.getString(R.string.none) } returns EXPECTED_STRING_NONE
        every {
            application.getString(
                R.string.stats_template,
                DEFAULT_FIZZBUZZ_RULES_INT1,
                DEFAULT_FIZZBUZZ_RULES_INT2,
                DEFAULT_FIZZBUZZ_RULES_LIMIT,
                DEFAULT_FIZZBUZZ_RULES_STR1,
                DEFAULT_FIZZBUZZ_RULES_STR2,
                EXPECTED_STRING_PLURAL_TIMES
            )
        } returns EXPECTED_STRING_STATS_TEMPLATE
        every { application.resources.getQuantityString(R.plurals.times, any(), any()) } returns EXPECTED_STRING_PLURAL_TIMES

        every { fizzbuzzRulesRepository.getRulesHistoryFlow() } returns flowOf(getDefaultRulesHistoryList())
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runBlockingTest {
        // Given
        val viewModel = StatsViewModel(application, fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(EXPECTED_STRING_STATS_TEMPLATE, it.value)
            verify(exactly = 1) {
                application.getString(
                    R.string.stats_template,
                    DEFAULT_FIZZBUZZ_RULES_INT1,
                    DEFAULT_FIZZBUZZ_RULES_INT2,
                    DEFAULT_FIZZBUZZ_RULES_LIMIT,
                    DEFAULT_FIZZBUZZ_RULES_STR1,
                    DEFAULT_FIZZBUZZ_RULES_STR2,
                    EXPECTED_STRING_PLURAL_TIMES
                )
                application.resources.getQuantityString(R.plurals.times, 2, 2)
                fizzbuzzRulesRepository.getRulesHistoryFlow()
            }
            confirmVerified(application, fizzbuzzRulesRepository)
        }
    }

    @Test
    fun `initial case - no stats available`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesHistoryFlow() } returns flowOf(emptyList())
        val viewModel = StatsViewModel(application, fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(EXPECTED_STRING_NONE, it.value)
            verify(exactly = 1) {
                application.getString(R.string.none)
                fizzbuzzRulesRepository.getRulesHistoryFlow()
            }
            confirmVerified(application, fizzbuzzRulesRepository)
        }
    }

    @Test
    fun `edge case - only one stats available`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesHistoryFlow() } returns flowOf(listOf(getDefaultFizzbuzzRules()))
        val viewModel = StatsViewModel(application, fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(EXPECTED_STRING_STATS_TEMPLATE, it.value)
        }
    }

    @Test
    fun `edge case - stats become available after a delay`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesHistoryFlow() } returns flow {
            emit(listOf())
            delay(500)
            emit(getDefaultRulesHistoryList())
        }
        val viewModel = StatsViewModel(application, fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {
            advanceTimeBy(500)

            // Then
            assertEquals(EXPECTED_STRING_STATS_TEMPLATE, it.value)
        }
    }

    // region IN
    private fun getDefaultRulesHistoryList(): List<FizzbuzzRules> = listOf(
        getDefaultFizzbuzzRules(),
        getDefaultFizzbuzzRules(),
        getDefaultFizzbuzzRules(int1 = 1, int2 = 2, limit = 50, str1 = "foo", str2 = "bar")
    )
    // endregion IN
}