package org.gradle

import static org.gradle.PerfUtil.*

class UniversePair {
    Universe portfolio
    Universe benchmark

    def covariance() {
        List a = portforlioDeviationTimesBenchmarkDeviation()
        a.sum() / a.size()
    }

    List portforlioDeviationTimesBenchmarkDeviation() {
        List deviationPairs = GroovyCollections.transpose(portfolio.deviationFromAverage(), benchmark.deviationFromAverage())
        return deviationPairs.collect { x -> x[0] * x[1] }
    }

    def correlation() {
        covariance() / (portfolio.monthlyStandardDeviation() * benchmark.monthlyStandardDeviation())
    }

    def regressionBeta() {
        portforlioDeviationTimesBenchmarkDeviation().sum() / benchmark.deviationSquared().sum()
    }

    def regressionAlpha() {
        def beta = regressionBeta()
        portfolio.averageReturn() - beta ** 2
    }

    List regressionResidual() {
        def beta = regressionBeta()
        def alpha = regressionAlpha()
        returnPairs().collect { r -> r[0] - r[1] * beta - alpha }
    }

    List returnPairs() {
        GroovyCollections.transpose(portfolio.monthlyReturns, benchmark.monthlyReturns)
    }

    List geometricExcessReturn() {
        returnPairs().collect { r ->  ((1+ r[0]/100) / (1+ r[1]/100) - 1) * 100 }
    }

    def geometricExcessDeviationFromAverageSquaredTotal() {
        squared(deviationFromAverage(geometricExcessReturn())).sum()
    }

    def geometricTrackingError() {
        trackingError(geometricExcessDeviationFromAverageSquaredTotal())
    }

    def annualizedGeometricTrackingError() {
        annualized(geometricTrackingError())
    }

    def geometricInformationRatio() {
        (((1 + portfolio.annualizedReturn()) / (1 + benchmark.annualizedReturn()))  - 1) * 100 / annualizedGeometricTrackingError()
    }

    List arithmeticExcessReturn() {
        returnPairs().collect { r -> r[0] - r[1] }
    }

    List arithmeticExcessDeviationFromAverage() {
        deviationFromAverage(arithmeticExcessReturn())
    }

    List arithmeticExcessDeviationFromAverageSquared() {
        squared(arithmeticExcessDeviationFromAverage())
    }

    def arithmeticExcessDeviationFromAverageSquaredTotal() {
        arithmeticExcessDeviationFromAverageSquared().sum()
    }

    def arithmeticTrackingError() {
        trackingError(arithmeticExcessDeviationFromAverageSquaredTotal())
    }

    def annualizedArithmeticTrackingError() {
        annualized(arithmeticTrackingError())
    }

    def arithmeticInformationRatio() {
        (percent (portfolio.annualizedReturn() - benchmark.annualizedReturn())) / annualizedArithmeticTrackingError()
    }

    def trackingError(total) {
        Math.sqrt(total / portfolio.monthlyReturns.size())
    }

    static UniversePair samplePair() {
        new UniversePair (portfolio : Universe.samplePortfolio(), benchmark : Universe.sampleBenchmark())
    }
}

class Universe {
    def monthlyReturns = []

    Universe() {
    }

    Universe(returns) {
        monthlyReturns = returns
    }

    def averageReturn() {
        monthlyReturns.sum() / monthlyReturns.size()
    }

    List deviationFromAverage() {
        def a = averageReturn()
        monthlyReturns.collect { r -> r - a }
    }

    List absoluteDeviation() {
        deviationFromAverage().collect { r -> Math.abs(r) }
    }

    List deviationSquared() {
        deviationFromAverage().collect { r -> r ** 2 }
    }

    def meanAbsoluteDeviation() {
        List a = absoluteDeviation()
        a.sum() / a.size()
    }

    def monthlyStandardDeviation() {
        List a = deviationSquared()
        Math.sqrt(a.sum() / a.size())
    }

    def annualizedStandardDeviation() {
        monthlyStandardDeviation() * Math.sqrt(12)
    }

    def annualizedReturn() {
        def r = monthlyReturns.collect { x -> 1 + x / 100 }
        def geometricAverage = r.inject { acc, x ->
            acc * x
        } ** (1/r.size) - 1
        ((1 + geometricAverage) ** 12) - 1
    }

    static Universe samplePortfolio() {
        def monthlyReturn = [
            0.3,
            2.6,
            1.1,
            -1.0,
            1.5,
            2.5,
            1.6,
            6.7,
            -1.4,
            4.0,
            -0.5,
            8.1,
            4.0,
            -3.7,
            -6.1,
            1.7,
            -4.9,
            -2.2,
            7.0,
            5.8,
            -6.5,
            2.4,
            -0.5,
            -0.9
        ]

        new Universe(monthlyReturn)
    }

    static Universe sampleBenchmark() {
        new Universe([
            0.2,
            2.5,
            1.8,
            -1.1,
            1.4,
            1.8,
            1.4,
            6.5,
            -1.5,
            4.2,
            -0.6,
            8.3,
            3.9,
            -3.8,
            -6.2,
            1.5,
            -4.8,
            2.1,
            6.0,
            5.6,
            -6.7,
            1.9,
            -0.3,
            0.0
        ])
    }
}

def u = Universe.samplePortfolio()
assert u.averageReturn() == 0.9
assert round(u.meanAbsoluteDeviation()) == 3.11
assert round(u.monthlyStandardDeviation()) == 3.87
assert round(u.annualizedStandardDeviation()) == 13.41
assert percent(u.annualizedReturn()) == 10.37