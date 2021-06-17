package fr.delcey.fizzbuzz.ui.parameters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.fizzbuzz.CoroutineDispatchers
import fr.delcey.fizzbuzz.TestCoroutineRule
import fr.delcey.fizzbuzz.data.FizzbuzzRules
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import fr.delcey.fizzbuzz.getDefaultFizzbuzzRules
import fr.delcey.fizzbuzz.observeForTesting
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ParametersViewModelTest {

    companion object {
        private const val EXPECTED_INT1 = "3"
        private const val EXPECTED_INT2 = "5"
        private const val EXPECTED_LIMIT = "100"
        private const val EXPECTED_STR1 = "fizz"
        private const val EXPECTED_STR2 = "buzz"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val fizzbuzzRulesRepository: FizzbuzzRulesRepository = mockk()
    private val dispatchers = CoroutineDispatchers(testCoroutineRule.testCoroutineDispatcher, testCoroutineRule.testCoroutineDispatcher)

    @Before
    fun setUp() {
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(getDefaultFizzbuzzRules())
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runBlockingTest {
        // Given
        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(getDefaultParametersViewState(), it.value)
            verify(exactly = 1) { fizzbuzzRulesRepository.getRulesFlow() }
            confirmVerified(fizzbuzzRulesRepository)
        }
    }

    @Test
    fun `initial case - no rule available, expose default rules`() = testCoroutineRule.runBlockingTest {
        // Given
        every { fizzbuzzRulesRepository.getRulesFlow() } returns flowOf(null)
        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.viewStateLiveData.observeForTesting {

            // Then
            assertEquals(getDefaultParametersViewState(), it.value)
        }
    }

    @Test
    fun `verify onButtonConfirmClicked`() = testCoroutineRule.runBlockingTest {
        // Given
        val int1 = "1"
        val int2 = "2"
        val limit = "5"
        val str1 = "foo"
        val str2 = "bar"

        justRun { fizzbuzzRulesRepository.setNewRules(any()) }

        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.onButtonConfirmClicked(int1, int2, limit, str1, str2)

        // Then
        verify(exactly = 1) {
            fizzbuzzRulesRepository.setNewRules(
                FizzbuzzRules(
                    int1 = 1,
                    int2 = 2,
                    limit = 5,
                    str1 = str1,
                    str2 = str2
                )
            )
        }
        confirmVerified(fizzbuzzRulesRepository)
    }

    @Test
    fun `error case - verify onButtonConfirmClicked with faulty int1`() = testCoroutineRule.runBlockingTest {
        // Given
        val int1 = "incorrect int"
        val int2 = "2"
        val limit = "5"
        val str1 = "foo"
        val str2 = "bar"

        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.onButtonConfirmClicked(int1, int2, limit, str1, str2)

        // Then
        verify(exactly = 0) { fizzbuzzRulesRepository.setNewRules(any()) }
    }

    @Test
    fun `error case - verify onButtonConfirmClicked with faulty int1 & int2`() = testCoroutineRule.runBlockingTest {
        // Given
        val int1 = "incorrect int"
        val int2 = ""
        val limit = "5"
        val str1 = "foo"
        val str2 = "bar"

        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.onButtonConfirmClicked(int1, int2, limit, str1, str2)

        // Then
        verify(exactly = 0) { fizzbuzzRulesRepository.setNewRules(any()) }
    }

    @Test
    fun `error case - verify onButtonConfirmClicked with faulty int1, int2 & limit`() = testCoroutineRule.runBlockingTest {
        // Given
        val int1 = "incorrect int"
        val int2 = ""
        val limit = "5.5"
        val str1 = "foo"
        val str2 = "bar"

        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.onButtonConfirmClicked(int1, int2, limit, str1, str2)

        // Then
        verify(exactly = 0) { fizzbuzzRulesRepository.setNewRules(any()) }
    }

    @Test
    fun `error case - verify onButtonConfirmClicked with faulty int2 & limit`() = testCoroutineRule.runBlockingTest {
        // Given
        val int1 = "1"
        val int2 = ""
        val limit = "5.5"
        val str1 = "foo"
        val str2 = "bar"

        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.onButtonConfirmClicked(int1, int2, limit, str1, str2)

        // Then
        verify(exactly = 0) { fizzbuzzRulesRepository.setNewRules(any()) }
    }

    @Test
    fun `error case - verify onButtonConfirmClicked with faulty limit`() = testCoroutineRule.runBlockingTest {
        // Given
        val int1 = "1"
        val int2 = "2"
        val limit = "5.5"
        val str1 = "foo"
        val str2 = "bar"

        val viewModel = ParametersViewModel(fizzbuzzRulesRepository, dispatchers)

        // When
        viewModel.onButtonConfirmClicked(int1, int2, limit, str1, str2)

        // Then
        verify(exactly = 0) { fizzbuzzRulesRepository.setNewRules(any()) }
    }

    // region OUT
    private fun getDefaultParametersViewState() = ParametersViewState(
        EXPECTED_INT1,
        EXPECTED_INT2,
        EXPECTED_LIMIT,
        EXPECTED_STR1,
        EXPECTED_STR2,
    )
    // endregion OUT
}