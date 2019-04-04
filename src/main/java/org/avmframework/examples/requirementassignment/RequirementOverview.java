package org.avmframework.examples.requirementassignment;

// a class to know the range of dependencies, importances,
// and complexity of the requirements

public class RequirementOverview {
  private double minDep = 1;
  private double maxDep = 1;
  private double minImp = 1;
  private double maxImp = 1;
  private double minComp = 1;
  private double maxComp = 1;

  public double getMinDep() {
    return minDep;
  }

  public void setMinDep(double minDep) {
    this.minDep = minDep;
  }

  public double getMaxDep() {
    return maxDep;
  }

  public void setMaxDep(double maxDep) {
    this.maxDep = maxDep;
  }

  public double getMinImp() {
    return minImp;
  }

  public void setMinImp(double minImp) {
    this.minImp = minImp;
  }

  public double getMaxImp() {
    return maxImp;
  }

  public void setMaxImp(double maxImp) {
    this.maxImp = maxImp;
  }

  public double getMinComp() {
    return minComp;
  }

  public void setMinComp(double minComp) {
    this.minComp = minComp;
  }

  public double getMaxComp() {
    return maxComp;
  }

  public void setMaxComp(double maxComp) {
    this.maxComp = maxComp;
  }
}
