package fr.delcey.fizzbuzz.data

import fr.delcey.fizzbuzz.TestCoroutineRule
import fr.delcey.fizzbuzz.getDefaultFizzbuzzRules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FizzbuzzRulesRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repository: FizzbuzzRulesRepository

    @Before
    fun setUp() {
        repository = FizzbuzzRulesRepository()
    }

    @Test
    fun `initial case - rules are null at start`() = testCoroutineRule.runBlockingTest {
        // When
        val result = repository.getRulesFlow().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `initial case - rules history is empty at start`() = testCoroutineRule.runBlockingTest {
        // When
        val result = repository.getRulesHistoryFlow().first()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `nominal case - on rules change, rule flow emit the new rules`() = testCoroutineRule.runBlockingTest {
        // Given
        val newRules = getDefaultFizzbuzzRules()

        // When
        repository.setNewRules(newRules)
        val result = repository.getRulesFlow().first()

        // Then
        assertEquals(getDefaultFizzbuzzRules(), result)
    }

    @Test
    fun `nominal case - on rules change, rule history flow emit aggregated history`() = testCoroutineRule.runBlockingTest {
        // Given
        val newRules = getDefaultFizzbuzzRules()

        // When
        repository.setNewRules(newRules)
        val result = repository.getRulesHistoryFlow().first()

        // Then
        assertEquals(listOf(getDefaultFizzbuzzRules()), result)
    }

    @Test
    fun `edge case - on a second rules change, rule history flow emit aggregated history of 2 rules`() = testCoroutineRule.runBlockingTest {
        // Given
        val newRules = getDefaultFizzbuzzRules()
        val newRulesBis = getDefaultFizzbuzzRules()

        // When
        repository.setNewRules(newRules)
        repository.setNewRules(newRulesBis)
        val result = repository.getRulesHistoryFlow().first()

        // Then
        assertEquals(
            listOf(
                getDefaultFizzbuzzRules(),
                getDefaultFizzbuzzRules()
            ), result
        )
    }

    @Test
    fun `edge case - on a second rules change, rule history flow emit aggregated history of 2 different rules`() = testCoroutineRule.runBlockingTest {
        // Given
        val newRules = getDefaultFizzbuzzRules()
        val newRulesBis = getDefaultFizzbuzzRules(1, 2, 5, "foo", "bar")

        // When
        repository.setNewRules(newRules)
        repository.setNewRules(newRulesBis)
        val result = repository.getRulesHistoryFlow().first()

        // Then
        assertEquals(
            listOf(
                getDefaultFizzbuzzRules(),
                getDefaultFizzbuzzRules(1, 2, 5, "foo", "bar")
            ), result
        )
    }
}