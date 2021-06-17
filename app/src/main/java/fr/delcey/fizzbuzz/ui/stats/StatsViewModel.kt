package fr.delcey.fizzbuzz.ui.stats

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import fr.delcey.fizzbuzz.CoroutineDispatchers
import fr.delcey.fizzbuzz.R
import fr.delcey.fizzbuzz.data.FizzbuzzRules
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import kotlinx.coroutines.flow.map

class StatsViewModel(
    private val application: Application,
    fizzbuzzRulesRepository: FizzbuzzRulesRepository,
    dispatchers: CoroutineDispatchers
) : ViewModel() {

    val viewStateLiveData: LiveData<String> = fizzbuzzRulesRepository.getRulesHistoryFlow().map {
        val mostPopularRuleAndCount = getMostPopularRuleAndCount(it)

        map(mostPopularRuleAndCount)
    }.asLiveData(dispatchers.ioDispatcher)

    private fun getMostPopularRuleAndCount(fizzbuzzRulesList: List<FizzbuzzRules>): Map.Entry<FizzbuzzRules, Int>? {
        val frequencies = fizzbuzzRulesList.groupingBy { it }.eachCount()
        return frequencies.maxByOrNull { it.value }
    }

    private fun map(fizzbuzzRulesEntry: Map.Entry<FizzbuzzRules, Int>?): String = if (fizzbuzzRulesEntry == null) {
        application.getString(R.string.none)
    } else {
        application.getString(
            R.string.stats_template,
            fizzbuzzRulesEntry.key.int1,
            fizzbuzzRulesEntry.key.int2,
            fizzbuzzRulesEntry.key.limit,
            fizzbuzzRulesEntry.key.str1,
            fizzbuzzRulesEntry.key.str2,
            application.resources.getQuantityString(R.plurals.times, fizzbuzzRulesEntry.value, fizzbuzzRulesEntry.value)
        )
    }
}