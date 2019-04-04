package org.avmframework.examples.testoptimization;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
  private String id;
  private List<String> apiCovered = new ArrayList<String>();
  private int time;
  private int numFaultExecution;
  private int numTotalExecution;
  private double faultDetection;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getApiCovered() {
    return apiCovered;
  }

  public void setApiCovered(List<String> apiCovered) {
    this.apiCovered = apiCovered;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getNumFailedExecution() {
    return numFaultExecution;
  }

  public void setNumFailedExecution(int numFaultExecution) {
    this.numFaultExecution = numFaultExecution;
  }

  public int getNumTotalExecution() {
    return numTotalExecution;
  }

  public void setNumTotalExecution(int numTotalExecution) {
    this.numTotalExecution = numTotalExecution;
  }

  public double getFaultDetection() {
    return faultDetection;
  }

  public void setFaultDetection(double faultDetection) {
    this.faultDetection = faultDetection;
  }
}
