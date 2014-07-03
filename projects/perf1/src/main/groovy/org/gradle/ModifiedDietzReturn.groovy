package org.gradle

import static org.gradle.PerfUtil.*

import org.joda.time.Days
import org.joda.time.LocalDate


class ModifiedDietzVs {
    def vs = []

    void leftShift(InputV a) {
        vs << a
    }

    def weightedCashFlow() {
        def end = vs[-1].date
        def start = vs[0].date
        def totalDays = Days.daysBetween(end, start).days
        vs.collect { x -> x.cashFlow * ((totalDays - Days.daysBetween(x.date, start).days) / totalDays) }.sum()
    }

    def r() {
        def end = vs[-1].mv
        def start = vs[0].mv

        def cashFlowTotal = vs.collect { x-> x.cashFlow}.sum()
        return percent((end - start - cashFlowTotal) / (start + weightedCashFlow()))
    }
}

ModifiedDietzVs vs = new ModifiedDietzVs()
vs << new InputV(mv: 74.2, date: new LocalDate(2013,12,31))
vs << new InputV(mv: 103.1, cashFlow: 37.1, date: new LocalDate(2014,1,14))
vs << new InputV(mv: 104.4, date: new LocalDate(2014,1,31))


assert vs.r() == -7.3
