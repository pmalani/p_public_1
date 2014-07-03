package org.gradle

import static org.gradle.PerfUtil.*

class Uv {
    def mv
    def cashFlow = 0
    def unitPrice = 1

    def calculateUnits(units) {
        if (cashFlow) {
            def priorMv = mv - cashFlow
            unitPrice = priorMv / units
            def additionalUnits = cashFlow / unitPrice
            return units + additionalUnits
        }
        unitPrice = mv / units
        return units
    }

    def initializeTotalUnits() {
        return mv / unitPrice
    }

    String toString() {
        "unit price: ${unitPrice}"
    }
}

class Uvs {
    def uvs = []

    def leftShift(Uv a) {
        uvs << a
    }

    void calculateUnits() {
        def totalUnits = uvs[0].initializeTotalUnits()
        uvs.each { u ->
            totalUnits = u.calculateUnits(totalUnits)
        }
    }

    def perf() {
        percent(uvs[-1].unitPrice / uvs[0].unitPrice - 1)
    }

    void prettyPrint() {
        uvs.each { u ->
            println u.toString()
        }
    }
}


Uvs uvs = new Uvs()
uvs << new Uv (mv: 74.2)
uvs << new Uv (mv: 103.1, cashFlow: 37.1)
uvs << new Uv (mv: 104.4)
uvs.calculateUnits()
uvs.prettyPrint()
uvs.perf() == -9.93

