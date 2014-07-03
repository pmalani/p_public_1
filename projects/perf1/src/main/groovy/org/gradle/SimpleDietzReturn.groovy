package org.gradle

import static org.gradle.PerfUtil.*


class DietzVs {
    def vs = []

    void leftShift(InputV a) {
        vs << a
    }

    /**
     * Assumes cashflow is received half way. Note the "2" in the equation.
     * @return
     */
    def r() {
        def end = vs[-1].mv
        def start = vs[0].mv

        def cashFlowTotal = vs.collect { x-> x.cashFlow}.sum()
        return percent((end - start - cashFlowTotal) / (start + (cashFlowTotal /2)))
    }
}

DietzVs vs = new DietzVs()
vs << new InputV(mv: 74.2)
vs << new InputV(mv: 103.1, cashFlow: 37.1)
vs << new InputV(mv: 104.4)

assert vs.r() == -7.44

DietzVs vs1 = new DietzVs()
vs1 << new InputV(mv: 100)
vs1 << new InputV(mv: 1200, cashFlow: 1000)
vs1 << new InputV(mv: 700)

assert vs1.r() == -66.67