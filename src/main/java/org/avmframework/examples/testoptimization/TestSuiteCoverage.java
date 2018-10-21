package org.avmframework.examples.testoptimization;

import java.util.HashSet;
import java.util.Set;

// total coverage of the original test suite
public class TestSuiteCoverage {
  public Set<String> coverage = new HashSet<String>();
  public int executionTime;
  public double faultDetection;

  public Set<String> getCoverage() {
    return coverage;
  }

  public void setCoverage(Set<String> coverage) {
    this.coverage = coverage;
  }

  public int getExecutionTime() {
    return executionTime;
  }

  public void setExecutionTime(int executionTime) {
    this.executionTime = executionTime;
  }

  public double getFaultDetection() {
    return faultDetection;
  }

  public void setFaultDetection(double faultDetection) {
    this.faultDetection = faultDetection;
  }
}
