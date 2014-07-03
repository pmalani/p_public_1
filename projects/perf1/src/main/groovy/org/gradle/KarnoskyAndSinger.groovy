package org.gradle

SectorWeightedUniversePair pair = SectorWeightedUniversePair.sampleWithLocalInterestRate()
pair.r == 7.45
pair.b == 5.5
pair.averageLocalBenchmarkReturnPremium == 1.7
pair.currencyPlusInterestBenchmarkReturn == 3.8

pair.allocationEffects3 == [0, 0.38, 0.87]
pair.selectionPlusInteraction == [2, -0.3, -0.6]
pair.currencyEffects3 == [0, 0.17, -0.72]
pair.currencyForwardEffects3 == [-0.23, -0.34, 0.72]