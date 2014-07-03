package org.gradle

import static org.gradle.PerfUtil.*

// All values in percent
class SharpeRatioCalculator {
    def annualizedReturn
    def annualizedRisk
    def riskFreeRate = 2

    def calculate() {
        (annualizedReturn - riskFreeRate) / annualizedRisk
    }

    def mSquared(annualizedBenchmarkRisk) {
        annualizedReturn + calculate() * (annualizedBenchmarkRisk - annualizedRisk)
    }
}

def portfolioA = new SharpeRatioCalculator( annualizedReturn : 7.9, annualizedRisk : 5.5)
assert round (portfolioA.calculate()) == 1.07

def portfolioB = new SharpeRatioCalculator( annualizedReturn : 6.9, annualizedRisk : 3.2)
assert round (portfolioB.calculate()) == 1.53

def benchmark = new SharpeRatioCalculator( annualizedReturn : 7.5, annualizedRisk : 4.5)
assert round (benchmark.calculate()) == 1.22

assert round (portfolioA.mSquared(4.5)) == 6.83

assert round (portfolioB.mSquared(4.5)) == 8.89