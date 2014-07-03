package org.gradle

import static org.gradle.PerfUtil.*


UniversePair p = UniversePair.samplePair()

assert round(p.arithmeticExcessDeviationFromAverageSquaredTotal()) == 21.69
assert round(p.arithmeticTrackingError()) == 0.95
assert round(p.annualizedArithmeticTrackingError()) == 3.29
assert round(p.arithmeticInformationRatio()) == -0.43

assert round(p.geometricExcessDeviationFromAverageSquaredTotal()) == 20.78
assert round(p.geometricTrackingError()) == 0.93
assert round(p.annualizedGeometricTrackingError()) == 3.22
assert round(p.geometricInformationRatio()) == -0.40
