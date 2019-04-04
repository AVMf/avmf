package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class State {

  /**
   * 0.state name 1.describe system state, which consists of system variables a. 2.in-transitions
   * 3.out-transitions
   *
   * <p>If we have a new state, which haven't been inserted to our state machine, its in-transitions
   * and out-transitions are both null/empty.
   *
   * <p>How to represent network environment? --- be not used by state 4.Insert network environment
   * into state, however network belongs to environment. So, the NetworkEnvironment is only used to
   * compute the transition.
   */
  private String stateLabel;

  private HashMap<String, ValueSet> systemVariablesValueSet;

  private String stateName; // 0.state name

  // private HashMap<String, Double> systemVariables_double;
  // private HashMap<String, Integer> systemVariables_int;
  private HashMap<String, Object> systemVariables; // 1.system variables

  private List<Transition> inTransitions; // 2.in-transitions
  private List<Transition> outTransitions; // 3.out-transitions

  // private NetworkEnvironment networkenvironment;
  // 4.network environment, this part exists in transitions!

  /** Construct methods. */
  public State() {
    this.systemVariables = new HashMap<String, Object>();
    this.inTransitions = new ArrayList<Transition>();
    this.outTransitions = new ArrayList<Transition>();

    this.stateLabel = "PredictedState";
  }

  public State(HashMap<String, Object> variables, String name) {
    this.systemVariables = variables;
    this.inTransitions = new ArrayList<Transition>();
    this.outTransitions = new ArrayList<Transition>();
    this.stateName = name;

    this.stateLabel = "PredictedState";
  }

  public State(String name, HashMap<String, ValueSet> variables) {
    this.systemVariablesValueSet = variables;
    this.inTransitions = new ArrayList<Transition>();
    this.outTransitions = new ArrayList<Transition>();
    this.stateName = name;

    this.stateLabel = "StateMachineState";
  }

  /**
   * Equal method: Only compare the variables' value between the two states Assume that the two
   * states' number of variables are totally same.
   */
  public boolean isequal(State state) {

    // if this.stateLabel

    if (this.getSystemVariables().size() == state.getSystemVariables().size()) {
      HashMap<String, Object> vals = this.systemVariables;
      Iterator it = vals.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
        String key = entry.getKey();
        Object val = entry.getValue(); // need to test whether it is right!o==o.
        if (state.getSystemVariables().get(key) != null) {
          if (state.getSystemVariables().get(key) == val) {
            // do nothing
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  public boolean isEqualValueSet(State state) {

    // if this.stateLabel

    if (this.getSystemVariablesValueSet().size() == state.getSystemVariablesValueSet().size()) {
      HashMap<String, ValueSet> vals = this.systemVariablesValueSet;
      Iterator it = vals.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>) it.next();
        String key = entry.getKey();
        ValueSet val = entry.getValue(); // need to test whether it is right!o==o.
        if (state.getSystemVariablesValueSet().get(key) != null) {
          if (state.getSystemVariablesValueSet().get(key).isequal(val)) {
            // do nothing
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }
  // Check the relationship between two states
  // The state is a new state constructed by program, and its variables are all like "x == v"
  // So, there are 3 types of results
  // the new state == one state :: 2
  // the new state belong to one state :: 1
  // the new state != && not belong to One state :: 0

  public int checkRelationshipBetweenTwoStates(State newState) {

    List<Integer> checkR = new ArrayList<Integer>();

    if (this.getSystemVariablesValueSet().size()
        == newState.getSystemVariablesValueSet().size()) {

      HashMap<String, ValueSet> vals = this.systemVariablesValueSet;
      Iterator it = vals.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>) it.next();
        String key = entry.getKey();
        ValueSet val = entry.getValue(); // need to test whether it is right!o==o.
        if (newState.getSystemVariablesValueSet().get(key) != null) {
          int check = checkValueSetRelationship(
              newState.getSystemVariablesValueSet().get(key), val);
          checkR.add(check);
        } else {
          System.out.println("======= Warning: wrong state check! =========");
          return -1;
        }
      }

      int lcr = checkR.size();
      int tag0 = 0;
      int tag1 = 0;
      int tag2 = 0;
      for (int i = 0; i < lcr; i++) {
        if (checkR.get(i) == 0) {
          tag0++;
        } else if (checkR.get(i) == 1) {
          tag1++;
        } else if (checkR.get(i) == 2) {
          tag2++;
        }
      }

      if (tag0 == 0) {
        if (tag1 == 0) {
          if (tag2 == 0) {
            System.out.println("======= Warning: check error! =========");
            return -1;
          } else {
            return 2;
          }
        } else {
          return 1;
        }
      } else {
        return 0;
      }

    } else {
      System.out.println("======= Warning: wrong state check! =========");
      return -1;
    }
  }

  public int checkValueSetRelationship(ValueSet v1, ValueSet v2) {

    int lengthValues1 = v1.getValueSet().size();
    int lengthValues2 = v2.getValueSet().size();
    // v1 should be the new state's variable
    // v2 should be the existing state's variable

    int checkResult = -1;

    if (lengthValues1 == 1 && lengthValues2 == 1) {

      String con0 = v1.getValueSet().get(0);
      String con1 = v2.getValueSet().get(0);

      String[] srcs0 = con0.split(" ");
      String[] srcs1 = con1.split(" ");

      if (srcs0[1].equals("==") && srcs1[1].equals("==")) {
        int temp0 = Integer.parseInt(srcs0[2]);
        int temp1 = Integer.parseInt(srcs1[2]);

        if (temp0 == temp1) {
          checkResult = 2;
        } else {
          checkResult = 0;
        }

      } else {
        // checkResult = -1
        System.out.println("Warning: Illegal!");
      }
    } else if (lengthValues1 == 1 && lengthValues2 == 2) { // ex. a < x <b

      String con00 = v1.getValueSet().get(0);
      String con10 = v2.getValueSet().get(0);
      String con11 = v2.getValueSet().get(1);

      int temp00 = 0;
      int temp10 = 0;
      int temp11 = 0;

      String[] srcs00 = con00.split(" ");
      String[] srcs10 = con10.split(" ");
      String[] srcs11 = con11.split(" ");

      if (srcs00[1].equals("==")) {
        temp00 = Integer.parseInt(srcs00[2]);
      } else {
        System.out.println("Warning: Illegal!");
      }

      if ((srcs10[1].equals("<") || srcs10[1].equals("<="))
          && (srcs11[1].equals(">") || srcs11[1].equals(">="))) {
        temp11 = Integer.parseInt(srcs10[2]);
        temp10 = Integer.parseInt(srcs11[2]);
      } else if ((srcs10[1].equals(">") || srcs10[1].equals(">="))
          && (srcs11[1].equals("<") || srcs11[1].equals("<="))) {
        temp11 = Integer.parseInt(srcs11[2]);
        temp10 = Integer.parseInt(srcs10[2]);
      } else {
        System.out.println("Warning: Illegal!");
      }

      if (temp11 > temp00 && temp10 < temp00) {
        checkResult = 1;
      } else {
        checkResult = 0;
      }

    } else {
      System.out.println("=== Warning: illegal! ===");
    }

    return checkResult;
  }

  /** Transition Get and Set this class's members Methods. */
  public void addInTransition(Transition transition) {
    this.inTransitions.add(transition);
  }

  public boolean removeinTransition(Transition transition) {
    if (this.inTransitions.isEmpty()) {
      return false;
    } else {
      int len = this.inTransitions.size();
      for (int i = 0; i < len; i++) {
        if (this.inTransitions.get(i).isequal(transition)) {
          this.inTransitions.remove(i);
          System.out.println("number of transitions:" + len + ", " + this.inTransitions.size());
        }
      }
      return true;
    }
  }

  public List<Transition> getinTransitions() {
    return this.inTransitions;
  }

  public void addOutTransition(Transition transition) {
    this.outTransitions.add(transition);
  }

  public boolean removeoutTransition(Transition transition) {
    if (this.outTransitions.isEmpty()) {
      return false;
    } else {
      int len = this.outTransitions.size();
      for (int i = 0; i < len; i++) {
        if (this.outTransitions.get(i).isequal(transition)) {
          this.outTransitions.remove(i);
          System.out.println("number of transitions:" + len + ", " + this.outTransitions.size());
        }
      }
      return true;
    }
  }

  public List<Transition> getoutTransitions() {
    return this.outTransitions;
  }

  /** System Variables - Object Get and Set this class's members Methods. */
  public void setSystemVariables(HashMap<String, Object> variables) {
    Iterator it = variables.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
      String key = entry.getKey();
      Object val = entry.getValue();
      this.setOneSystemVariable(val, key);
    }
  }

  public void setOneSystemVariable(Object value, String variablename) {
    this.systemVariables.put(variablename, value);
  }

  public HashMap<String, Object> getSystemVariables() {
    return this.systemVariables;
    // It is better to have a new variable as the returned result.
  }

  public HashMap<String, ValueSet> getSystemVariablesValueSet() {
    return this.systemVariablesValueSet;
  }
  /**
   * System Variables - ValueSet Get and Set this class's members Methods
   *
   * <p>The related method is written in StateMachine.
   */

  /** StateName Get and Set this class's members Methods. */
  public String getStateName() {
    return this.stateName;
  }

  public void setStateName(String statename) {
    this.stateName = statename;
  }

  public static void main(String[] args) {
    // test all the methods
  }
}
