package fr.delcey.fizzbuzz.ui.parameters

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import fr.delcey.fizzbuzz.CoroutineDispatchers
import fr.delcey.fizzbuzz.data.FizzbuzzRules
import fr.delcey.fizzbuzz.data.FizzbuzzRulesRepository
import kotlinx.coroutines.flow.firstOrNull

class ParametersViewModel(
    private val fizzBuzzRulesRepository: FizzbuzzRulesRepository,
    dispatchers: CoroutineDispatchers
) : ViewModel() {

    companion object {
        private const val DEFAULT_INT1 = 3
        private const val DEFAULT_INT2 = 5
        private const val DEFAULT_LIMIT = 100
        private const val DEFAULT_STR1 = "fizz"
        private const val DEFAULT_STR2 = "buzz"
    }

    val viewStateLiveData: LiveData<ParametersViewState> = liveData(dispatchers.ioDispatcher) {
        val initialRules = fizzBuzzRulesRepository.getRulesFlow().firstOrNull() ?: FizzbuzzRules(
            DEFAULT_INT1,
            DEFAULT_INT2,
            DEFAULT_LIMIT,
            DEFAULT_STR1,
            DEFAULT_STR2
        )

        emit(map(initialRules))
    }

    private fun map(fizzbuzzRules: FizzbuzzRules) = ParametersViewState(
        int1 = fizzbuzzRules.int1.toString(),
        int2 = fizzbuzzRules.int2.toString(),
        limit = fizzbuzzRules.limit.toString(),
        str1 = fizzbuzzRules.str1,
        str2 = fizzbuzzRules.str2
    )

    fun onButtonConfirmClicked(int1: String, int2: String, limit: String, str1: String, str2: String) {
        val int1Parsed = int1.toIntOrNull()
        val int2Parsed = int2.toIntOrNull()
        val limitParsed = limit.toIntOrNull()

        if (int1Parsed != null && int2Parsed != null && limitParsed != null) {
            fizzBuzzRulesRepository.setNewRules(
                FizzbuzzRules(
                    int1Parsed,
                    int2Parsed,
                    limitParsed,
                    str1,
                    str2
                )
            )
        }
    }
}