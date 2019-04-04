package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.ArrayList;
import java.util.List;

public class BehaviourPair {

  /**
   * It is very familiar with transition. NetworkEnvironment---actually exists in transitions.
   *
   * <p>However, BP is more similar with test cases, and it represents one situation of all the
   * situation represented by transition.
   *
   * <p>1.source state, 2.target state, 3.triggers, 4.the value of the variable of the guard
   * conditions(eg. network environment, using specific data structure to represent)
   */
  private State sourceState;

  private State targetState;

  // transition - trigger
  private List<String> triggers;

  // transition - gc's concrete value - network environment
  private NetworkEnvironment networkEnvironmentVariables;

  /** Construct methods. */
  public BehaviourPair() {
    this.sourceState = new State();
    this.targetState = new State();
    this.triggers = new ArrayList<String>();
    this.networkEnvironmentVariables = new NetworkEnvironment();
  }

  public BehaviourPair(
      State source,
      List<String> trigs,
      State target,
      NetworkEnvironment networkEnvironment) { // network part unfinished
    this.sourceState = source;
    this.targetState = target;
    this.triggers = trigs;
    this.networkEnvironmentVariables = networkEnvironment;
  }

  /** Parser to BPair list from a state machine. */
  public static List<BehaviourPair> stateMachineParser(StateMachine stateMachine) {

    List<BehaviourPair> behaviourPairs = new ArrayList<BehaviourPair>();
    /*
    List<Transition> transitionsInStateMachine = stateMachine.getAllTransitions();
    //List<State> statesinStateMachine = stateMachine.getAllStates();
    int length = transitionsInStateMachine.size();
    for(int i = 0; i < length; i ++){
      //network part unfinished
      State source = transitionsInStateMachine.get(i).getSourceState();
      State target = transitionsInStateMachine.get(i).getTargetState();
      Transition transition = transitionsInStateMachine.get(i);

      NetworkEnvironment networkEnvironment = null
      //= new NetworkEnvironment(transition)
      ;
      BehaviourPair behaviourPair = new BehaviourPair(
          source, transition, target, networkEnvironment);
      behaviourPairs.add(behaviourPair);
    }*/
    return behaviourPairs;
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

  public List<String> getTrigger() {
    return this.triggers;
  }

  public void setTrigger(List<String> transition) {
    this.triggers = transition;
  }

  public NetworkEnvironment getNetworkEnvironment() {
    return this.networkEnvironmentVariables;
  }

  public void setNetworkEnvironment(NetworkEnvironment networkEnvironment) {
    this.networkEnvironmentVariables = networkEnvironment;
  }
}
