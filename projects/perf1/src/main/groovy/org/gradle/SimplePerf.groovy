package org.gradle

import static org.gradle.PerfUtil.*


def ve = 112
def vs = 100

assert ve / vs == 1.12, 'Weath Ratio'

assert (ve - vs) / vs == 0.12, 'Rate of return'

assert (ve/vs) -1 == 0.12, 'Rate of return'

ve = null
vs = null
def mv = [100, 112, 95, 99, 107, 115]
def wr = []
for (i in mv) {
    if (!ve) {
        ve = i
        continue
    }
    vs = ve
    ve = i
    wr << (ve/vs)
}
def ror = wr.collect { i -> i - 1 }
assert ror.collect { r -> percent(r) } == [
    12,
    -15.18,
    4.21,
    8.08,
    7.48
]

assert 1.15 == round(wr.inject { x, y -> x * y }), 'Linking'