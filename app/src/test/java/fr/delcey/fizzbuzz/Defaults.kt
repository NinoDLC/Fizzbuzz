package fr.delcey.fizzbuzz

import fr.delcey.fizzbuzz.data.FizzbuzzRules

const val DEFAULT_FIZZBUZZ_RULES_INT1 = 3
const val DEFAULT_FIZZBUZZ_RULES_INT2 = 5
const val DEFAULT_FIZZBUZZ_RULES_LIMIT = 100
const val DEFAULT_FIZZBUZZ_RULES_STR1 = "fizz"
const val DEFAULT_FIZZBUZZ_RULES_STR2 = "buzz"

fun getDefaultFizzbuzzRules(
    int1: Int = DEFAULT_FIZZBUZZ_RULES_INT1,
    int2: Int = DEFAULT_FIZZBUZZ_RULES_INT2,
    limit: Int = DEFAULT_FIZZBUZZ_RULES_LIMIT,
    str1: String = DEFAULT_FIZZBUZZ_RULES_STR1,
    str2: String = DEFAULT_FIZZBUZZ_RULES_STR2,
) = FizzbuzzRules(
    int1 = int1,
    int2 = int2,
    limit = limit,
    str1 = str1,
    str2 = str2
)
