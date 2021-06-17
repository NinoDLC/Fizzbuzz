package fr.delcey.fizzbuzz.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FizzbuzzRulesRepository {

    private val rules = MutableStateFlow<FizzbuzzRules?>(null)

    private val rulesHistory = MutableStateFlow<List<FizzbuzzRules>>(emptyList())

    fun getRulesFlow(): Flow<FizzbuzzRules?> = rules

    fun setNewRules(fizzbuzzRules: FizzbuzzRules) {
        rules.value = fizzbuzzRules
        rulesHistory.value = rulesHistory.value + fizzbuzzRules
    }

    fun getRulesHistoryFlow(): Flow<List<FizzbuzzRules>> = rulesHistory
}