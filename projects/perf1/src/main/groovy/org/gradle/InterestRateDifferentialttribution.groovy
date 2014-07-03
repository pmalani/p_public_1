package org.gradle

SectorWeightedUniversePair pair = SectorWeightedUniversePair.sampleWithInterestRateDifferential()
pair.r == 17.81
pair.impliedPortfolioCurrencyRtn == 9.29
pair.realPortfolioCurrencyComponentRtn == [0, 1.8, 8.12]

//pair.b == 6.4
//pair.localSemiNotionalRtn == 5.2
//pair.RL == 17.51
//pair.BL == 16.96
//pair.naiveCurrencyPortfolioReturn == 8.5
//pair.naiveCurrencyBenchmarkReturn == 9.92
//pair.naiveCurrnecyAllocation == -1.29
//pair.naiveGeometricAllocationEffects == [0, -0.98, -0.15]
//pair.naiveGeometricSelectionEffects == [3.8, -0.29, -0.57]
//pair.naiveCurrencyAttribution == 0.47
//pair.impliedBenchmarkCurrencyRtn == 10
//pair.realPortfolioCurrencyRtn == 8.5
//pair.realBenchmarkCurrencyRtn == 9.92
//pair.realBenchmarkCurrencyComponentRtn == [0, 2.63, 5.87]
//pair.currencyAllocation == [0, 0, -0.91]
