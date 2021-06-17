package fr.delcey.fizzbuzz

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test

class CoroutineDispatchersTest {
    @Test
    fun `verify default dispatchers values`() {
        // When
        val coroutineDispatchers = CoroutineDispatchers()

        // Then
        assertEquals(Dispatchers.Main, coroutineDispatchers.mainDispatcher)
        assertEquals(Dispatchers.IO, coroutineDispatchers.ioDispatcher)
    }
}