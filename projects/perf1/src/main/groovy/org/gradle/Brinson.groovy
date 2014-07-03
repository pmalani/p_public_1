package org.gradle

import static org.gradle.PerfUtil.*

def portfolio = SectorWeightedUniverse.samplePortfolioForAttribution()
assert portfolio.rtn == 8.3

def benchmark = SectorWeightedUniverse.sampleBenchmarkForAttribution()
assert benchmark.rtn == 6.4

def pair = SectorWeightedUniversePair.sample()
assert pair.allocation == 5.2
assert pair.selection == 9.4

assert pair.excessAllocation == -1.2
assert pair.allocationEffects == [0, -0.4, -0.8]
assert pair.excessAllocation == pair.allocationEffects.sum()
 
assert pair.excessSelection == 3.0
assert pair.selectionEffects == [4, -0.2, -0.8]
assert pair.excessSelection == pair.selectionEffects.sum()

assert pair.interaction == 0.1
assert pair.interactionEffects == [0, -0.1, 0.2]
assert pair.interaction == pair.interactionEffects.sum()

assert pair.allocationEffects2 == [0, -1.04, -0.16]
assert pair.excessAllocation == pair.allocationEffects2.sum()

assert pair.selectionPlusInteraction == [4.0, -0.3, -0.6]
assert pair.portfolio.rtn - pair.allocation == pair.selectionPlusInteraction.sum()

assert pair.geometricAllocation == -1.13
assert pair.geometricAllocationEffects == [0, -0.98, -0.15]

assert pair.geometricSelection == 2.95
assert pair.geometricSelectionPlusInteraction == [3.8, -0.29, -0.57]