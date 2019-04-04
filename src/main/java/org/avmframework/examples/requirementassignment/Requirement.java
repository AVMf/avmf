package org.avmframework.examples.requirementassignment;

// the structure of the requirement class that maps to the contents of the requirements file
public class Requirement {
  private String id;
  private String identifier;
  private String description;
  private double dependency;
  private double importance;
  private double complexity;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getDependency() {
    return dependency;
  }

  public void setDependency(double dependency) {
    this.dependency = dependency;
  }

  public double getImportance() {
    return importance;
  }

  public void setImportance(double importance) {
    this.importance = importance;
  }

  public double getComplexity() {
    return complexity;
  }

  public void setComplexity(double complexity) {
    this.complexity = complexity;
  }
}
