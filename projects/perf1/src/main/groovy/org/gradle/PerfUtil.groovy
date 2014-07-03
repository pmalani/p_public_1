package org.gradle

import java.math.RoundingMode

class PerfUtil {
    static final squareRootOf12 = Math.sqrt(12)

    static def round(x) {
        if (x instanceof Double) x = new BigDecimal(x)
        x.setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros()
    }

    static def percent(x) {
        round (x * 100)
    }

    static List deviationFromAverage(List a) {
        def average = a.sum() / a.size()
        a.collect { r -> (r - average) }
    }

    static List squared(List l) {
        l.collect { a -> a ** 2 }
    }

    static def annualized(a) {
        a * squareRootOf12
    }

    static def round(List l) {
        l.collect { x -> round(x) }
    }
}
