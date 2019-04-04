package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Transition {

  /**
   * 0. Source state 1. Target state 2. Trigger-Event Trigger {user operations, network environment
   * parameters} 3. Conditions - Guard Conditions - Network Environment related {} 4. Effect-ignore
   * for now {}
   */
  private HashMap<String, ValueSet> conditions;

  private List<String> triggers;

  private State sourceState;
  private State targetState;

  /** Construct methods. */
  public Transition() {
    this.conditions = new HashMap<String, ValueSet>();
    this.triggers = new ArrayList<String>();
    this.sourceState = new State();
    this.targetState = new State();
  }

  public Transition(State sourceState, State targetState) {
    this.conditions = new HashMap<String, ValueSet>();
    this.triggers = new ArrayList<String>();
    this.sourceState = sourceState;
    this.targetState = targetState;
  }

  public Transition(NetworkEnvironment ne) {
    this.conditions = new HashMap<String, ValueSet>();
    this.triggers = new ArrayList<String>();
    this.sourceState = new State();
    this.targetState = new State();

    // this.triggers.add(ne.getPacketDelayName());
    // this.triggers.add(ne.getPacketLossName());
    // this.triggers.add(ne.getPacketCorruptionName());
    // this.triggers.add(ne.getPacketDuplicationName());

  }

  /**
   * Functional Methods setTriggerByNetworkEnvironment Set the trigger of the transition by network
   * environment.
   *
   * <p>Haven't been used!
   */
  public static void setTriggerByNetworkEnvironment(
      Transition transition, NetworkEnvironment networkEnvironment) {

    String delay = "PacketDelay == ";
    double varDelay = networkEnvironment.getPacketDelay();
    delay = delay + varDelay;

    String loss = "PacketLoss == ";
    double varLoss = networkEnvironment.getPacketLoss();
    loss = loss + varLoss;

    String corrupt = "PacketCorruption == ";
    double varCorrupt = networkEnvironment.getPacketCorruption();
    corrupt = corrupt + varCorrupt;

    String duplicate = "PacketDuplication == ";
    double varDuplicate = networkEnvironment.getPacketDuplication();
    duplicate = duplicate + varDuplicate;

    transition.addTriggers(delay);
    transition.addTriggers(loss);
    transition.addTriggers(corrupt);
    transition.addTriggers(duplicate);
  }

  /**
   * ABANDONED. Functional Methods setTransitionByNetworkEnvironment: actually set the guard.
   * conditions
   */
  public static void setTransitionByNetworkEnvironment(
      Transition transition, NetworkEnvironment networkEnvironment) {
    /*
    String delay = "PacketDelay == ";
    double varDelay = networkEnvironment.getPacketDelay();
    delay = delay + varDelay;

    String loss = "PacketLoss == ";
    double varLoss = networkEnvironment.getPacketLoss();
    loss = loss + varLoss;

    String corrupt = "PacketCorruption == ";
    double varCorrupt = networkEnvironment.getPacketCorruption();
    corrupt = corrupt + varCorrupt;

    String duplicate = "PacketDuplication == ";
    double varDuplicate = networkEnvironment.getPacketDuplication();
    duplicate = duplicate + varDuplicate;

    transition.addConditions(delay);
    transition.addConditions(loss);
    transition.addConditions(corrupt);
    transition.addConditions(duplicate);
    */
    System.out.println("Method setTransitionByNetworkEnvironment has been out of time!");
  }

  // This method is only used in state to set(remove) the transitions of one specific state.
  // Only compare source state, target state, and triggers.
  public boolean isequal(Transition transition) {
    if (!this.sourceState.isequal(transition.sourceState)) {
      return false;
    }
    if (!this.targetState.isequal(transition.targetState)) {
      return false;
    }
    int length = this.triggers.size();
    if (length == transition.getTriggers().size()) {
      for (int i = 0; i < length; i++) {
        String tempTriggers = this.triggers.get(i);

        boolean temp =
            false; // This means one condition has the same condition in the other transition.
        for (int j = 0; j < length; j++) {
          if (tempTriggers.equals(transition.getTriggers().get(j))) {
            temp = true;
          }
        }
        if (temp == false) {
          return false;
        }
      }
    } else {
      return false;
    }

    return true;
  }

  public boolean isequalTriggerAndCondition(Transition transition) {
    // Notice!
    if (!this.sourceState.isEqualValueSet(transition.sourceState)) {
      return false;
    }

    // only use trigger's index 0
    if (!transition.triggers.get(0).equals(this.triggers.get(0))) {
      return false;
    }

    // conditions
    if (transition.getConditions().size() == this.getConditions().size()) {
      HashMap<String, ValueSet> vals = this.conditions;
      Iterator it = vals.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>) it.next();
        String key = entry.getKey();
        ValueSet val = entry.getValue();
        if (transition.getConditions().get(key) != null) {
          if (transition.getConditions().get(key).isequal(val)) {
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

  /** Set and Get this class's members Methods. */
  public State getSourceState() {
    return this.sourceState;
  }

  public void setSourceState(State state) {
    this.sourceState = state;
  }

  public State getTargetState() {
    return this.targetState;
  }

  public void setTargetState(State state) {
    this.targetState = state;
  }

  public void setConditions(HashMap<String, ValueSet> gc) {
    this.conditions = gc;
  }

  public void addOneCondition(String var, String condition) {
    this.conditions.get(var).getValueSet().add(condition);
  }

  public void addConditions(String var, ValueSet valueSet) {
    this.conditions.put(var, valueSet);
  }

  public HashMap<String, ValueSet> getConditions() {
    return this.conditions;
  }

  public void addTriggers(String trigger) {
    this.triggers.add(trigger);
  }

  public List<String> getTriggers() {
    return this.triggers;
  }

  public static void main(String[] args) {}
}
