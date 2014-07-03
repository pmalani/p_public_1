package org.gradle

import static org.gradle.PerfUtil.*



def returnsInPercent = [10.5, -5.6, 23.4, -15.7, 8.9]

def returns = returnsInPercent.collect { x -> (1 + x/100) }

println returns

def cummulativeReturns = returns.inject { acc, x -> acc * x } - 1
assert percent(cummulativeReturns) == 18.17

def arithmeticAverage = returnsInPercent.sum() / returnsInPercent.size
assert arithmeticAverage == 4.3

def geometricAverage = returns.inject { acc, x ->
    acc * x
} ** (1/returns.size) - 1

assert percent(geometricAverage) == 3.4


assert percent (( (1 + geometricAverage) ** returns.size()) - 1)  == percent(cummulativeReturns)

assert percent (( (1 + arithmeticAverage/100) ** returns.size()) - 1)  != percent(cummulativeReturns)