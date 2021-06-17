package fr.delcey.fizzbuzz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import fr.delcey.fizzbuzz.ui.list.FizzbuzzListViewModel
import fr.delcey.fizzbuzz.ui.parameters.ParametersViewModel
import fr.delcey.fizzbuzz.ui.stats.StatsViewModel

object ViewModelFactory : ViewModelProvider.Factory {

    private val fizzbuzzRulesRepository by lazy { FizzbuzzRulesRepository() }
    private val coroutineDispatchers by lazy { CoroutineDispatchers() }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FizzbuzzListViewModel::class.java) -> FizzbuzzListViewModel(
                fizzbuzzRulesRepository,
                coroutineDispatchers
            )
            modelClass.isAssignableFrom(ParametersViewModel::class.java) -> ParametersViewModel(
                fizzbuzzRulesRepository,
                coroutineDispatchers
            )
            modelClass.isAssignableFrom(StatsViewModel::class.java) -> StatsViewModel(
                MainApplication.sInstance,
                fizzbuzzRulesRepository,
                coroutineDispatchers
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}