package org.gradle

import static org.gradle.PerfUtil.*

def pair = SectorWeightedUniversePair.sampleWithForwards()
//assert percent (pair.r) == 15.29
//println percent(pair.r)
//assert percent (pair.b) == 12.54
println percent(pair.b)
assert pair.benchmark.e == 9.0
assert pair.benchmark.d == 1.0
assert round(pair.getAllocationEffects2()) == [0, -1.14, -0.26]
assert round(pair.getSelectionEffects()) == [4, -0.22, -0.96]
assert round(pair.getInteractionEffects()) == [0, -0.11, 0.24]
assert round(pair.getCurrencyEffects()) == [0, 0, -0.9]
//assert round(pair.getCurrencyForwardEffects()) == [0.9, 0, 1.3]
assert round(pair.getForwardPremiumEffects()) == [0, 0, -0.1]
