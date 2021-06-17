package fr.delcey.fizzbuzz.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import fr.delcey.fizzbuzz.CoroutineDispatchers
import fr.delcey.fizzbuzz.data.FizzbuzzRules
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class FizzbuzzListViewModel(
    fizzbuzzRulesRepository: FizzbuzzRulesRepository,
    dispatchers: CoroutineDispatchers,
) : ViewModel() {

    val viewStateLiveData: LiveData<List<String>> = fizzbuzzRulesRepository.getRulesFlow().filterNotNull().map { rules ->
        generateList(rules)
    }.asLiveData(dispatchers.ioDispatcher)

    private fun generateList(rules: FizzbuzzRules): List<String> = List(rules.limit) { index ->
        val humanIndex = index + 1

        val isFizz = if (rules.int1 != 0) {
            humanIndex % rules.int1 == 0
        } else {
            false
        }
        val isBuzz = if (rules.int2 != 0) {
            humanIndex % rules.int2 == 0
        } else {
            false
        }

        when {
            isFizz && isBuzz -> "${rules.str1}${rules.str2}"
            isFizz -> rules.str1
            isBuzz -> rules.str2
            else -> humanIndex.toString()
        }
    }
}