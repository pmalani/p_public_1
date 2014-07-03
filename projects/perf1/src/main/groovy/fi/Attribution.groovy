package fi

Universe portfolio = Universe.samplePorfolio()
Component portfolioTotal = portfolio.totalComponent
assert portfolioTotal.weight == 100
assert portfolioTotal.duration == 5.3
assert portfolioTotal.rtn == 3.86
assert portfolioTotal.riskFreeRtn == 0.58
assert portfolio.impliedYieldChanges == [-0.59, -0.4, -0.75, -0.62]

Universe benchmark = Universe.sampleBenchmark()
Component benchmarkTotal = benchmark.totalComponent
assert benchmarkTotal.weight == 100
assert benchmarkTotal.duration == 3.9
assert benchmarkTotal.rtn == 3
assert benchmarkTotal.riskFreeRtn == 0.59
assert benchmark.impliedYieldChanges == [-0.5, -0.2, -0.93, -0.62]

UniversePair pair = UniversePair.sampleUniversePair()
pair.durationNotional == 3.87
pair.durationAdjustedSemiNotional == 3.7
pair.durationAllocation == 0.87
pair.marketAllocation == [-0.06, 0.03, -0.14]
pair.securitySelection == [0.35, 0.04, -0.22]
pair.currencyAllocation == [0, -0.05, 0.04]