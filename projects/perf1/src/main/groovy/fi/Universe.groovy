package fi

import static org.gradle.PerfUtil.*

class Component {
    String sector
    Number weight
    Number duration
    Number rtn
    Number riskFreeRtn

    Number getImpliedYieldChange() {
        round(-(rtn - riskFreeRtn) / duration)
    }
}

class Universe {
    List<Component> components

    Component getAt(int i) {
        components[i]
    }

    void putAt(int i, Component c) {
        components[i] = c
    }

    Component getTotalComponent() {
        Number totalWeight = 0
        Number totalDuration = 0
        Number totalRtn = 0
        Number totalRiskFreeRtn = 0
        components.each { c ->
            totalWeight += c.weight
            totalDuration += c.weight / 100 * c.duration
            totalRtn += c.weight / 100 * c.rtn
            totalRiskFreeRtn += c.weight / 100 * c.riskFreeRtn
        }
        new Component(sector: "Total", weight: totalWeight, duration:totalDuration, rtn:totalRtn, riskFreeRtn: totalRiskFreeRtn)
    }

    List<Number> getImpliedYieldChanges() {
        List<Component> c = []
        c.addAll(components)
        c << totalComponent
        c.collect { x -> x.impliedYieldChange }
    }

    static samplePorfolio() {
        return new Universe(components : [
            new Component(sector: "UK", weight: 50, duration:7.8, rtn:5.6, riskFreeRtn: 1.0),
            new Component(sector: "Japan", weight: 20, duration:1.0, rtn:0.5, riskFreeRtn: 0.1),
            new Component(sector: "US", weight: 30, duration:4.0, rtn:3.2, riskFreeRtn: 0.2)
        ])
    }

    static sampleBenchmark() {
        return new Universe(components : [
            new Component(sector: "UK", weight: 50, duration:5.0, rtn:3.5, riskFreeRtn: 1.0),
            new Component(sector: "Japan", weight:10, duration:2.0, rtn:0.5, riskFreeRtn: 0.1),
            new Component(sector: "US", weight: 40, duration:3.0, rtn:3.0, riskFreeRtn: 0.2)
        ])
    }
}

class UniversePair {
    Universe portfolio
    Universe benchmark

    Component getPortfolioTotalComponent() {
        portfolio.totalComponent
    }

    Component getBenchmarkTotalComponent() {
        benchmark.totalComponent
    }

    Number getDurationBeta() {
        portfolioTotalComponent.duration / benchmarkTotalComponent.duration
    }

    Number getR() {
        portfolioTotalComponent.rtn
    }

    Number getB() {
        benchmarkTotalComponent.rtn
    }

    Number getXr() {
        portfolioTotalComponent.riskFreeRtn
    }

    Number getXb() {
        benchmarkTotalComponent.riskFreeRtn
    }

    Number getYb() {
        benchmarkTotalComponent.impliedYieldChange
    }

    Number W(int i) {
        portfolio[i].weight
    }

    Number Db(int i) {
        benchmark[i].duration
    }

    Number Yb(int i) {
        benchmark[i].impliedYieldChange
    }

    Number Yr(int i) {
        portfolio[i].impliedYieldChange
    }

    Number D(int i) {
        portfolio[i].duration
    }

    Number w(int i) {
        benchmark[i].weight
    }

    Number Xb(int i) {
        benchmark[i].riskFreeRtn
    }

    Number getDurationNotional() {
        Number a = 0
        portfolio.components.eachWithIndex { c, i ->
            a += Db(i) * W(i) * -Yb(i)
        }
        durationBeta * a + xb
    }

    Number getDurationAdjustedSemiNotional() {
        Number a = 0
        portfolio.components.eachWithIndex { c, i ->
            a += D(i) * w(i) * -Yb(i)
        }
        a + xb
    }

    Number getDurationAllocation() {
        durationNotional - b
    }

    Number marketAllocationForComponent(int i) {
        (D(i) * w(i) - durationBeta * Db(i) * W(i)) * (-Yb(i) + yb)
    }

    Number securitySelectionForComponent(int i) {
        D(i) * w(i) * (-Yr(i) + Yb(i))
    }

    Number currencyAllocationForComponent(int i) {
        (w(i) - W(i)) * (Xb(i) - xb)
    }

    List<Number> getMarketAllocation() {
        getAllocation(this.&marketAllocationForComponent)
    }

    List<Number> getSecuritySelection() {
        getAllocation(this.&securitySelectionForComponent)
    }

    List<Number> getCurrencyAllocation() {
        getAllocation(this.&currencyAllocationForComponent)
    }

    List<Number> getAllocation(Closure aClosure) {
        List<Number> a = []
        portfolio.components.eachWithIndex { c, i ->
            a << aClosure(i)
        }
        return a
    }

    static sampleUniversePair() {
        new UniversePair(portfolio: Universe.samplePorfolio(), benchmark: Universe.sampleBenchmark())
    }
}