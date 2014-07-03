package org.gradle


import static org.gradle.PerfUtil.*

class Bond {
    List cashFlow
    List presentValue
    List spotRates
    def irrY
    

    Bond(cf, List pv, List rates, def irrY) {
        presentValue = pv
        // coupon payments
        cashFlow = [cf] * (rates.size - 1)
        // final payment
        cashFlow << (100+ cf)
        spotRates = rates
        this.irrY = irrY;
    }

    List getTimeXPresentValue() {
        List r = []
        presentValue.eachWithIndex { x, i ->
            r << x * (i+1)
        }
        r
    }
    
    def getMacaulayDuration() {
        timeXPresentValue.sum() / presentValue.sum()
    }
    
    def getModifiedDuration() {
        macaulayDuration / ( 1 + irrY/100)
    }
    

    static Bond makeSampleBond() {
        List rates = [3.5]* 8 + [3.75]* 8 + [4]* 4
        new Bond (6, [
            5.78,
            5.56,
            5.35,
            5.15,
            4.96,
            4.77,
            4.59,
            4.42,
            4.25,
            4.1,
            3.94,
            3.79,
            3.65,
            3.52,
            3.38,
            3.26,
            3.13,
            3.02,
            2.9,
            49.39
        ], rates, 3.89)
    }
}

Bond b = Bond.makeSampleBond()


println b.cashFlow
println b.cashFlow.size()
println b.spotRates
println b.spotRates.size()
println b.presentValue

println b.timeXPresentValue

assert round(b.macaulayDuration) == 13.13
assert round(b.modifiedDuration) == 12.64

// should be able to calculate present value since we know the spot rate?
// should be able to calculate the internal rate of return?
