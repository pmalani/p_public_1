package org.gradle

import static org.gradle.PerfUtil.*

class SectorWeightedUniversePair {
    SectorWeightedUniverse portfolio
    SectorWeightedUniverse benchmark

    def getLocalSemiNotionalRtn() {
        (0..<portfolio.components.size()).collect { i ->
            w(i) * bL(i)
        }.sum()
    }

    def rL(i, List c = portfolio.components) {
        c.get(i).localRtn / 100
    }

    def bL(i, List c = benchmark.components) {
        c.get(i).localRtn / 100
    }

    def w(i, List c = portfolio.components) {
        c.get(i).weight
    }

    def b(i, List c = benchmark.components) {
        c.get(i).rtn / 100
    }

    def x(i, List c = benchmark.components) {
        c.get(i).localInterestRate
    }

    def W(i, List c = benchmark.components) {
        c.get(i).weight
    }

    def r(i, List c = portfolio.components) {
        c.get(i).rtn / 100
    }

    def e(i, List c = portfolio.components) {
        c.get(i).currencySurprise / 100
    }

    def d(i, List c = portfolio.components) {
        c.get(i).forwardPremium / 100
    }

    def c(i, List c = portfolio.components) {
        e(i,c) + d(i,c)
    }

    def getAllocation() {
        (0..<portfolio.components.size()).collect { i ->
            w(i) * b(i)
        }.sum()
    }

    def getSelection() {
        (0..<portfolio.components.size()).collect { i ->
            W(i) * r(i)
        }.sum()
    }

    def getExcessAllocation() {
        allocation - benchmark.rtn
    }

    def getAllocationEffects() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..<c.size()).collect { i ->
            (w(i,c) - W(i,d)) * b(i,d)
        }
    }

    def getExcessSelection() {
        selection - benchmark.rtn
    }

    def getSelectionEffects() {
        def c = portfolio.assets
        def d = benchmark.assets

        (0..<c.size()).collect { i ->
            W(i,d) * (r(i,c) -b(i,d))
        }
    }

    def getInteraction() {
        portfolio.rtn - allocation - selection + benchmark.rtn
    }

    def getInteractionEffects() {
        def c = portfolio.assets
        def d = benchmark.assets

        (0..<c.size()).collect { i ->
            (w(i,c) - W(i,d)) * (r(i,c) -b(i,d))
        }
    }

    def getCurrencyEffects() {
        def c = portfolio.assets
        def d = benchmark.assets

        (0..<c.size()).collect { i ->
            (w(i,c) - W(i,d)) * (e(i,c) -e)
        }
    }

    def getCurrencyEffects3() {
        def port = portfolio.assets
        def bogie = benchmark.assets

        (0..<port.size()).collect { i ->
            (w(i,port) - W(i,bogie)) * (c(i,port) + x(i,bogie) - currencyPlusInterestBenchmarkReturn)
        }
    }


    // in this case r(i) is same as f(i)
    def getCurrencyForwardEffects() {
        def c = portfolio.forwards
        def d = benchmark.forwards

        (0..<c.size()).collect { i ->
            (w(i,c) - W(i,d)) * (r(i,c) -e)
        }
    }

    def getCurrencyForwardEffects3() {
        def port = portfolio.forwards
        def bogie = benchmark.forwards

        (0..<port.size()).collect { i ->
            (w(i,port) - W(i,bogie)) * (c(i,port) + x(i,bogie) - currencyPlusInterestBenchmarkReturn)
        }
    }


    def getForwardPremiumEffects() {
        def x = portfolio.assets
        def y = benchmark.assets

        (0..<x.size()).collect { i ->
            (w(i,x) - W(i,y)) * (d(i,x) -d)
        }
    }

    def getB() {
        benchmark.rtn /100
    }

    def getR() {
        portfolio.rtn / 100
    }

    def getBL() {
        benchmark.localRtn /100
    }

    def getRL() {
        portfolio.localRtn / 100
    }

    def getE() {
        benchmark.e / 100
    }

    def getD() {
        benchmark.d / 100
    }

    // Takes bogie return into consideration!!!
    def getAllocationEffects2() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..< c.size()).collect { i ->
            (w(i,c) - W(i,d)) * (b(i,d) - b)
        }
    }

    // Takes local interest into consideration
    def getAllocationEffects3() {
        def c = portfolio.assets
        def d = benchmark.assets
        def ci = averageLocalBenchmarkReturnPremium
        (0..< c.size()).collect { i ->
            (w(i,c) - W(i,d)) * (b(i,d) - x(i,d) - ci)
        }
    }

    def getSelectionPlusInteraction() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..<c.size()).collect { i ->
            w(i,c) * (r(i,c) - b(i,d))
        }
    }

    def getGeometricAllocationEffects() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..<c.size()).collect { i ->
            round((w(i,c) - W(i,d)) * ((1+b(i,d))/(1+b) - 1))
        }
    }

    def getNaiveGeometricAllocationEffects() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..<c.size()).collect { i ->
            round((w(i,c) - W(i,d)) * ((1+bL(i,d))/(1+BL) - 1))
        }
    }

    def getGeometricSelectionPlusInteraction() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..<c.size()).collect { i ->
            round(w(i,c) * ((r(i,c) - b(i,d))/(1+ allocation/100)))
        }
    }

    def getNaiveGeometricSelectionEffects() {
        def c = portfolio.assets
        def d = benchmark.assets
        (0..<c.size()).collect { i ->
            round(w(i,c) * ((1 + rL(i,c))/(1 + bL(i,d))- 1) * (1 + bL(i,d)/(1 + localSemiNotionalRtn/100)))
        }
    }

    def getNaiveCurrencyAttribution() {
        (1 + RL)/(1 + localSemiNotionalRtn/100) * (1 + localSemiNotionalRtn/100)/(1 + BL) * (1 + r)/(1 + RL) * (1 + BL)/(1 + b) - 1
    }

    def getGeometricAllocation() {
        percent(((1 + allocation/100)/(1+b)) -1)
    }

    def getGeometricSelection() {
        percent((1 +r)/(1 + allocation/100) -1)
    }

    def getAverageLocalBenchmarkReturnPremium() {
        benchmark.assets.sum { x ->
            x.weight * (x.rtn - x.currencyRtn)
        }
    }

    def getCurrencyPlusInterestBenchmarkReturn() {
        benchmark.assets.sum { x ->
            x.weight * (x.currencyRtn + x.localInterestRate)
        }
    }

    def getNaiveCurrencyPortfolioReturn() {
        (1 + r) / (1 + RL) - 1
    }

    def getNaiveCurrencyBenchmarkReturn() {
        (1 + b ) / (1 + BL) - 1
    }

    def getNaiveCurrnecyAllocation() {
        naiveCurrencyPortfolioReturn / naiveCurrencyBenchmarkReturn - 1
    }

    def getImpliedPortfolioCurrencyRtn() {
        portfolio.currencyRtn
    }

    def getImpliedBenchmarkCurrencyRtn() {
        benchmark.currencyRtn
    }

    def getRealPortfolioCurrencyRtn() {
        portfolio.realCurrencyRtn
    }

    def getRealBenchmarkCurrencyRtn() {
        benchmark.realCurrencyRtn
    }

    def getRealPortfolioCurrencyComponentRtn() {
        portfolio.realCurrencyComponentRtn
    }

    def getRealBenchmarkCurrencyComponentRtn() {
        benchmark.realCurrencyComponentRtn
    }

    def getCurrencyAllocation() {
        def a = portfolio.assets
        def d = benchmark.assets
        (0..<a.size()).collect { i ->
            round((w(i,a) - W(i,d)) * ((1+c(i,d))/(1+impliedBenchmarkCurrencyRtn/100) - 1))
        }
    }

    static SectorWeightedUniversePair sample() {
        SectorWeightedUniversePair a = new SectorWeightedUniversePair()
        a.portfolio = SectorWeightedUniverse.samplePortfolioForAttribution()
        a.benchmark = SectorWeightedUniverse.sampleBenchmarkForAttribution()
        return a
    }

    static SectorWeightedUniversePair sampleWithForwards() {
        SectorWeightedUniversePair a = new SectorWeightedUniversePair ()
        a.portfolio = SectorWeightedUniverse.samplePortfolioWithForwards()
        a.benchmark = SectorWeightedUniverse.sampleBenchmarkWithForwards()
        return a
    }

    static SectorWeightedUniversePair sampleWithLocalInterestRate() {
        SectorWeightedUniversePair a = new SectorWeightedUniversePair ()
        a.portfolio = SectorWeightedUniverse.samplePortfolioWithLocalInterestRate()
        a.benchmark = SectorWeightedUniverse.sampleBenchmarkWithLocalInterestRates()
        return a
    }

    static SectorWeightedUniversePair sampleWithLocalAndBase() {
        SectorWeightedUniversePair a = new SectorWeightedUniversePair ()
        a.portfolio = SectorWeightedUniverse.samplePortfolioWithLocalAndBaseReturn()
        a.benchmark = SectorWeightedUniverse.sampleBenchmarkWithLocalAndBaseReturn()
        return a
    }

    static SectorWeightedUniversePair sampleWithInterestRateDifferential() {
        SectorWeightedUniversePair a = new SectorWeightedUniversePair ()
        a.portfolio = SectorWeightedUniverse.samplePortfolioWithInterestRateDifferential()
        a.benchmark = SectorWeightedUniverse.sampleBenchmarkWithLocalAndBaseReturn()
        return a
    }
}

class UniverseType {
    SectorWeightedUniverse universe

    public UniverseType(SectorWeightedUniverse a) {
        universe = a
    }

    def getRtn() {
        universe.components.findAll { a -> !a.isForward  }.collect { c ->
            c.weight * c.rtn /100
        }.inject { acc, x ->
            acc + x
        }
    }
}

class KnsUniverseType extends UniverseType {
    public KnsUniverseType(SectorWeightedUniverse a) {
        super(a)
    }

    def getRtn() {
        universe.assets.sum { x ->
            x.weight + (x.rtn + x.currencyRtn)
        }
        +
                universe.forwards.sum { x ->
                    x.wight + (x.localInterestRate + x.currencyRtn)
                }
    }
}

class SectorWeightedUniverse {

    List components = []
    UniverseType universeType

    public SectorWeightedUniverse() {
        universeType = new UniverseType(this)
    }

    void useKnsUniverseType() {
        universeType = new KnsUniverseType(this)
    }

    def getAssets() {
        components.findAll { x -> !x.isForward }
    }

    def getForwards() {
        components.findAll { x -> x.isForward }
    }

    def getRtn() {
        components.findAll { a -> !a.isForward  }.collect { c ->
            c.weight * c.rtn /100
        }.inject { acc, x ->
            acc + x
        }
    }

    def getLocalRtn() {
        components.sum { c -> c.weight * c.localRtn / 100 }
    }

    def getCurrencyRtn() {
        components.sum { c -> c.weight * c.currencyRtn / 100 }
    }

    def getRealCurrencyRtn() {
        (1 + rtn) / (1 + localRtn) - 1
    }

    def getRealCurrencyComponentRtn() {
        components.collect { c ->
            c.weight * c.currencyRtn * (1 + c.localRtn) / (1 + rtn)
        }
    }

    def getE() {
        components.findAll { a -> !a.isForward  }.collect { c ->
            c.weight * c.currencySurprise/100
        }.inject { acc, x->
            acc+ x
        }
    }

    def getD() {
        components.collect { c ->
            c.weight * c.forwardPremium/100
        }.inject { acc, x->
            acc+ x
        }
    }

    void add(SectorWeightedUniverseComponent a) {
        components << a
    }

    void addComponent(String s, w, r, boolean isForward = false) {
        add(new SectorWeightedUniverseComponent(s,w,r, isForward))
    }

    void addComponent(String s, w, r, cs, fp) {
        add(new SectorWeightedUniverseComponent(s,w,r,cs,fp))
    }

    void addComponentWithLocalInterestRate(String s, w, r, localInterestRate, currencyRtn) {
        add (SectorWeightedUniverseComponent.makeWithLocalInterestRate(s,w,r,currencyRtn, localInterestRate))
    }

    void addForwardWithLocalInterestRate(String s, w, localInterestRate, currencyRtn) {
        add (SectorWeightedUniverseComponent.makeForwardWithLocalInterestRate(s,w,currencyRtn, localInterestRate))
    }

    void addComponentWithLocalAndBaseReturn(String s, weight, localRtn, baseRtn, currencyRtn) {
        add(SectorWeightedUniverseComponent.makeWithBaseAndLocalRtn(s, weight, localRtn, baseRtn, currencyRtn))
    }

    static SectorWeightedUniverse samplePortfolioWithInterestRateDifferential() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponentWithLocalAndBaseReturn("UK equities", 40, 20, 20, 0)
        u.addComponentWithLocalAndBaseReturn("Japanese equities", 30, -5, 4.7, 10.2)
        u.addComponentWithLocalAndBaseReturn("US equities", 30, 6, 28, 20.8)
        return u
    }

    static SectorWeightedUniverse samplePortfolioWithLocalAndBaseReturn() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponentWithLocalAndBaseReturn("UK equities", 40, 20, 20, 0)
        u.addComponentWithLocalAndBaseReturn("Japanese equities", 30, -5, 4.5, 10)
        u.addComponentWithLocalAndBaseReturn("US equities", 30, 6, 27.2, 20)
        return u
    }

    static SectorWeightedUniverse sampleBenchmarkWithLocalAndBaseReturn() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponentWithLocalAndBaseReturn("UK equities", 40, 10, 10, 0)
        u.addComponentWithLocalAndBaseReturn("Japanese equities", 20, -4, 5.6, 10)
        u.addComponentWithLocalAndBaseReturn("US equities", 40, 8, 29.6, 20)
        return u
    }

    static SectorWeightedUniverse samplePortfolioWithLocalInterestRate() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.useKnsUniverseType();
        u.addComponentWithLocalInterestRate("UK equities", 40, 15, 1.5, 0)
        u.addComponentWithLocalInterestRate("Japanese equities", 30, 5, 0.5, 5)
        u.addComponentWithLocalInterestRate("US equities", 30, -8, 1, 10)
        u.addForwardWithLocalInterestRate("GBP", 40, 1.5 ,0)
        u.addForwardWithLocalInterestRate("JPY", -30,0.5, 5)
        u.addForwardWithLocalInterestRate("USD", -10, 1, 10)
        return u
    }

    static SectorWeightedUniverse sampleBenchmarkWithLocalInterestRates() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.useKnsUniverseType();
        u.addComponentWithLocalInterestRate("UK equities", 40, 10, 1.5, 0)
        u.addComponentWithLocalInterestRate("Japanese equities", 20, 6, 0.5, 5)
        u.addComponentWithLocalInterestRate("US equities", 40, -6, 1, 10)
        u.addForwardWithLocalInterestRate("GBP", 30, 1.5 ,0)
        u.addForwardWithLocalInterestRate("JPY", -10,0.5, 5)
        u.addForwardWithLocalInterestRate("USD", -20, 1, 10)
        return u
    }

    static SectorWeightedUniverse samplePortfolioWithForwards() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponent("UK equities", 40, 20, 0, 0)
        u.addComponent("Japanese equities", 30, 4.5, 9, 1)
        u.addComponent("US equities", 30, 27.2, 18, 2)
        u.addComponent("GBP", 20, 0, true)
        u.addComponent("JPY", -15, 8.91, true)
        u.addComponent("USD", -10, 17.65, true)
        return u
    }

    static SectorWeightedUniverse sampleBenchmarkWithForwards() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponent("UK equities", 40, 10, 0, 0)
        u.addComponent("Japanese equities", 20, 5.6, 9, 1)
        u.addComponent("US equities", 40, 29.6, 18, 2)
        u.addComponent("GBP", 30, 0, true)
        u.addComponent("JPY", -10, 8.91, true)
        u.addComponent("USD", -20, 17.65, true)
        return u
    }

    static SectorWeightedUniverse samplePortfolioForAttribution() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponent("UK equities", 40, 20)
        u.addComponent("Japanese equities", 30, -5)
        u.addComponent("US equities", 30, 6)
        return u
    }

    static SectorWeightedUniverse sampleBenchmarkForAttribution() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponent("UK equities", 40, 10)
        u.addComponent("Japanese equities", 20, -4)
        u.addComponent("US equities", 40, 8)
        return u
    }

    static SectorWeightedUniverse sample() {
        SectorWeightedUniverse u = new SectorWeightedUniverse()
        u.addComponent("UK", 20, 15)
        u.addComponent("Norway", 4, -4.21)
        u.addComponent("Sweden", 3, -3.1)
        u.addComponent("France", 15, -10.75)
        u.addComponent("US", 35, 6.7)
        u.addComponent("Japan", 20, 26.5)
        u.addComponent("Australia", 3, 20.75)
        return u
    }

    def weightForSector(String s) {
        components.find { x ->
            x.sector == s
        }?.weight
    }
}

class SectorWeightedUniverseComponent {
    String sector
    def weight
    private rtn
    def currencySurprise = 0.0
    def forwardPremium = 0.0
    boolean isForward
    Number localInterestRate = 0.0
    Number localRtn = 0.0

    SectorWeightedUniverseComponent() {
    }

    SectorWeightedUniverseComponent(String c, w, r, boolean isForward = false) {
        sector = c
        weight = w
        rtn = r
        this.isForward = isForward
    }

    SectorWeightedUniverseComponent(String c, w, r, cs, fp) {
        this(c, w, r)
        currencySurprise = cs
        forwardPremium = fp
    }

    static SectorWeightedUniverseComponent makeWithLocalInterestRate(String sector, Number weight, Number rtn,
            Number currencyRtn, Number localInterestRate) {
        SectorWeightedUniverseComponent a = new SectorWeightedUniverseComponent(sector, weight, rtn)
        a.forwardPremium = currencyRtn
        a.localInterestRate = localInterestRate
        return a
    }

    static SectorWeightedUniverseComponent makeForwardWithLocalInterestRate(String sector, Number weight, Number rtn,
            Number localInterestRate) {
        SectorWeightedUniverseComponent a = new SectorWeightedUniverseComponent(sector, weight, rtn, true)
        a.localInterestRate = localInterestRate
        return a
    }

    static SectorWeightedUniverseComponent makeWithBaseAndLocalRtn(String sector, Number weight,
            Number localRtn, Number baseRtn, Number currencyRtn) {
        SectorWeightedUniverseComponent a = new SectorWeightedUniverseComponent(sector, weight, baseRtn)
        a.forwardPremium = currencyRtn
        a.localRtn = localRtn
        return a
    }

    def getCurrencyRtn() {
        currencySurprise + forwardPremium
    }

    def getRtn() {
        rtn - currencyRtn
    }
}

SectorWeightedUniverse u = SectorWeightedUniverse.sample()
def contributionRtnExcludingAustralia = u.components.findAll{ a ->
    a.sector != 'Australia'
}
.collect { c ->
    c.weight * c.rtn/100
}
.inject { acc, x ->
    acc + x
}
assert  round(contributionRtnExcludingAustralia) == 8.77
def rtnExcludingAustralia = contributionRtnExcludingAustralia / (1 - u.weightForSector("Australia")/100)
assert round(rtnExcludingAustralia) == 9.04