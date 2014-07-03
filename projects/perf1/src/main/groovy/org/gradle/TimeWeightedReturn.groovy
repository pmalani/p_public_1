package org.gradle

import static org.gradle.PerfUtil.*

class TimeWeightedVs {
    def vs = []

    void leftShift(InputV a) {
        vs << a
    }

    def r() {
        def total = 1
        for (int i = 1; i < vs.size; i++) {
            def current = vs[i]
            def previous = vs[i-1]
            total *= (current.mv - current.cashFlow) / previous.mv
        }
        return percent(total - 1)
    }
}

TimeWeightedVs vs = new TimeWeightedVs()
vs << new InputV(mv: 74.2)
vs << new InputV(mv: 103.1, cashFlow: 37.1)
vs << new InputV(mv: 104.4)

assert vs.r() == -9.93

TimeWeightedVs vs1 = new TimeWeightedVs()
vs1 << new InputV(mv: 100)
vs1 << new InputV(mv: 1200, cashFlow: 1000)
vs1 << new InputV(mv: 700)

assert vs1.r() == 16.67