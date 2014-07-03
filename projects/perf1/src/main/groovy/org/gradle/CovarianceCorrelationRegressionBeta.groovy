package org.gradle

import static org.gradle.PerfUtil.*


UniversePair p = UniversePair.samplePair()
assert round(p.covariance())   == 14.1
assert round(p.correlation()) == 0.97
assert round(p.regressionBeta()) == 1.0
assert round(p.regressionAlpha()) == -0.1
