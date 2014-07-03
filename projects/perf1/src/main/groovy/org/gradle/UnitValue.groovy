package org.gradle

import static org.gradle.PerfUtil.*

// in millions
def mvPeriodStart = 74.2
def mvPeriodEnd = 104.4
def cashFlowOnDay14 = 37.1
def mvBeforeCashFlow = 66

def startingUnitPrice = 1
def unitPrice = startingUnitPrice
def totalUnits = mvPeriodStart / unitPrice
println "unit price: ${unitPrice}, total units: ${totalUnits}"

unitPrice = mvBeforeCashFlow / totalUnits

totalUnits += cashFlowOnDay14 / unitPrice
println "unit price: ${unitPrice}, total units: ${totalUnits}"

unitPrice = mvPeriodEnd / totalUnits
println "unit price: ${unitPrice}, total units: ${totalUnits}"

def perf = unitPrice /startingUnitPrice -1

println unitPrice
println totalUnits
println perf

assert percent(perf) == -9.93