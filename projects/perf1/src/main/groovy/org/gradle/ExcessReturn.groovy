package org.gradle

import static org.gradle.PerfUtil.*

def portfolioStartValue = 1000000
def portfolioEndValue = 1070000

def portfolioRtn = percent((portfolioEndValue - portfolioStartValue) / portfolioStartValue)
assert  portfolioRtn == 7.00

def benchmarkRtn = 5.00

def arithmeticExcessRtn = portfolioRtn - benchmarkRtn
assert arithmeticExcessRtn == 2.00

def geometricExcessRtrn = percent((1  + portfolioRtn/100) / (1 + benchmarkRtn/100) - 1)
assert geometricExcessRtrn == 1.9