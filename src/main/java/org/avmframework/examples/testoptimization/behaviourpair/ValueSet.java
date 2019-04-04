package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.ArrayList;
import java.util.List;

public class ValueSet {

  private List<String> values;

  public ValueSet() {
    this.values = new ArrayList<String>();
  }

  public ValueSet(List<String> vals) {
    this.values = vals;
  }

  public boolean isequal(ValueSet valueSet) {
    boolean checkResult = true;

    int lengthValues = this.values.size();
    int lengthValueSet = valueSet.values.size();

    if (lengthValues == lengthValueSet) {

      for (int i = 0; i < lengthValues; i++) {
        int tag = 0;
        for (int j = 0; j < lengthValueSet; j++) {
          if (this.values.get(i).equals(valueSet.values.get(j))) {
            tag = 1;
            break;
          } else {
            // do nothing
          }
        }
        if (tag == 0) {
          checkResult = false;
          break;
        }
      }

    } else {
      checkResult = false;
    }

    return checkResult;
  }

  public void addConstriantsForValueSet(String constraints) {
    this.values.add(constraints);
  }

  public void removeConstriantsForValueSet(int index) {
    this.values.remove(index);
  }

  public void setValueSet(List<String> constraints) {
    this.values = constraints;
  }

  public List<String> getValueSet() {
    return this.values;
  }

  /**
   * type: 0-INTEGER_CATEGORICAL_TYPE 1-DOUBLE_CATEGORICAL_TYPE(impossible type)
   * 2-INTEGER_NUMERICAL_TYPE 3-DOUBLE_NUMERICAL_TYPE.
   */
  public static void calculateConstraints() {}

  public static void main(String[] args) {

    // Idle: activecall 0, videoquality 0

    ValueSet valueSetIdle1 = new ValueSet();
    valueSetIdle1.addConstriantsForValueSet("activecall == 0");
    ValueSet valueSetIdle2 = new ValueSet();
    valueSetIdle2.addConstriantsForValueSet("videoquality == 0");

    // Connected_1: activecall 1, videoquality 3

    ValueSet valueSetIdle3 = new ValueSet();
    valueSetIdle3.addConstriantsForValueSet("activecall == 1");
    ValueSet valueSetIdle4 = new ValueSet();
    valueSetIdle4.addConstriantsForValueSet("videoquality == 3");

    // Notfull: 1 < activecall < 4, videoquality 3(max = 4, need to know <, <=, >, >=, ==)

    ValueSet valueSetIdle5 = new ValueSet();
    valueSetIdle5.addConstriantsForValueSet("activecall > 1");
    valueSetIdle5.addConstriantsForValueSet("activecall < 4");
    ValueSet valueSetIdle6 = new ValueSet();
    valueSetIdle6.addConstriantsForValueSet("videoquality == 3");
  }
}
