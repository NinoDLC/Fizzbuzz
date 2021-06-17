package fr.delcey.fizzbuzz

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

data class CoroutineDispatchers(
    val mainDispatcher: CoroutineContext = Dispatchers.Main,
    val ioDispatcher: CoroutineContext = Dispatchers.IO
)