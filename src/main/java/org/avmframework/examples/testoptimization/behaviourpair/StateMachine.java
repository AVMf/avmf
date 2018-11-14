package org.avmframework.examples.testoptimization.behaviourpair;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {

  /**
   * 0. A start State, which is just like the root of one tree. 1. List of all states and list of
   * all transitions.
   */
  private State startstate;

  private List<State> states;
  private List<Transition> transitions;

  public StateMachine() {
    this.startstate = null;
    this.states = new ArrayList<State>();
    this.transitions = new ArrayList<Transition>();
  }

  public StateMachine(State startstate) {
    this.startstate = startstate;
    this.states = new ArrayList<State>();
    this.transitions = new ArrayList<Transition>();
    this.addOneState(this.startstate);
  }

  /** this method is a frame, and currently we build state machine manually. */
  public void initial() {
    // do something
  }

  /**
   * Need more methods.
   *
   * <p>How to add transitions when build a state machine!
   */
  public State getState(String stateName) {
    State state = null;

    int length = this.getAllStates().size();
    for (int i = 0; i < length; i++) {
      if (stateName.equals(this.getAllStates().get(i).getStateName())) {
        state = this.getAllStates().get(i);
      }
    }
    return state;
  }

  public int getStateNumber(String stateName) {

    int stateNumber = -1;

    int length = this.getAllStates().size();
    for (int i = 0; i < length; i++) {
      if (stateName.equals(this.getAllStates().get(i).getStateName())) {
        stateNumber = i;
      }
    }
    return stateNumber;
  }

  // Check whether a state is a new state ----- 2016.1026
  public boolean isANewState(State state) {
    boolean checkResult = true;
    int length = this.states.size();
    for (int i = 0; i < length; i++) {
      if (state.isEqualValueSet(this.states.get(i))) {
        // do nothing
      } else {
        checkResult = false;
      }
    }
    return checkResult;
  }
  // Check whether a transition is a new transition(only source state and trigger and conditions)
  // ----- 2016.1026

  public boolean isANewTransition(Transition transition) {
    boolean checkResult = true;

    int length = this.transitions.size();

    for (int i = 0; i < length; i++) {
      if (this.transitions.get(i).isequalTriggerAndCondition(transition)) {
        break;
      }
    }

    return checkResult;
  }

  // Update StateMachine by a New Transition(include judging part) ----- 2016.1026
  public int updateStateMachinebyAddANewTransition(Transition transition) {
    int lengthOfState = this.states.size();

    State sourceStateTransitionInStateMachine = null;
    State targetStateTransitionransition = transition.getTargetState();

    int labebWhetherBreak = 0;

    for (int i = 0; i < lengthOfState; i++) {

      int cr = this.states.get(i).checkRelationshipBetweenTwoStates(targetStateTransitionransition);

      if (cr == 1 || cr == 2) {

        State transitionState = this.states.get(i);

        List<Transition> listTransition = transitionState.getinTransitions();

        int lengthOfListTransition = listTransition.size();
        for (int j = 0; j < lengthOfListTransition; j++) {
          if (listTransition.get(j).getSourceState().isEqualValueSet(transition.getSourceState())) {
            sourceStateTransitionInStateMachine = listTransition.get(j).getSourceState();
            break;
          }
        }

        if (sourceStateTransitionInStateMachine == null) {
          // it is not a new state, but there is a new transition, then update

          // Find the actual source state
          for (int k = 0; k < lengthOfState; k++) {
            if (this.states.get(k).isEqualValueSet(transition.getSourceState())) {
              sourceStateTransitionInStateMachine = this.states.get(k);
              break;
            }
          }
          // Add one out transition --- the new transition
          sourceStateTransitionInStateMachine.addOutTransition(transition);
          // Replace the target state
          transition.setTargetState(transitionState);
          // Add one in transition --- the new transition
          transitionState.addInTransition(transition);

          this.transitions.add(transition);

          return 1;
        } else {
          // It is not a new transition, do not update
        }

        labebWhetherBreak = 1;
        break; // find a similar state, then get out of the loop
      } else {
        // Not sure whether it is a new state
        // do nothing
      }
    }
    if (labebWhetherBreak == 0) {
      // Do not encounter any break
      // it is a new state, then update

      // Find the actual source state
      for (int k = 0; k < lengthOfState; k++) {
        if (this.states.get(k).isEqualValueSet(transition.getSourceState())) {
          sourceStateTransitionInStateMachine = this.states.get(k);
          break;
        }
      }
      // Add one out transition --- the new transition
      sourceStateTransitionInStateMachine.addOutTransition(transition);
      // t.getTargetState().addInTransition(t);

      this.transitions.add(transition);
      this.states.add(transition.getTargetState());

      // Update the new target state's name
      transition.getTargetState().setStateName("new" + this.states.size());

      return 2;
    }
    return 0;
  }

  public int checkNewTransition(Transition transition) {
    int lengthOfState = this.states.size();

    State sourceStateTransitionInStateMachine = null;
    State targetStateTransition = transition.getTargetState();

    int labebWhetherBreak = 0;

    for (int i = 0; i < lengthOfState; i++) {

      int cr = this.states.get(i).checkRelationshipBetweenTwoStates(targetStateTransition);

      if (cr == 1 || cr == 2) {

        State transitionState = this.states.get(i);

        List<Transition> listTransition = transitionState.getinTransitions();

        int lengthOfListTransition = listTransition.size();
        for (int j = 0; j < lengthOfListTransition; j++) {
          if (listTransition.get(j).getSourceState().isEqualValueSet(transition.getSourceState())) {
            sourceStateTransitionInStateMachine = listTransition.get(j).getSourceState();
            break;
          }
        }

        if (sourceStateTransitionInStateMachine == null) {
          // it is not a new state, but there is a new transition, then update

          // Find the actual source state
          for (int k = 0; k < lengthOfState; k++) {
            if (this.states.get(k).isEqualValueSet(transition.getSourceState())) {
              sourceStateTransitionInStateMachine = this.states.get(k);
              break;
            }
          }

          return 1;
        } else {
          // It is not a new transition, do not update
        }

        labebWhetherBreak = 1;
        break; // find a similar state, then get out of the loop
      } else {
        // Not sure whether it is a new state
        // do nothing
      }
    }
    if (labebWhetherBreak == 0) {
      // Do not encounter any break
      // it is a new state, then update

      // Find the actual source state
      for (int k = 0; k < lengthOfState; k++) {
        if (this.states.get(k).isEqualValueSet(transition.getSourceState())) {
          sourceStateTransitionInStateMachine = this.states.get(k);
          break;
        }
      }
      return 2;
    }
    return 0;
  }
  // !!Revise related transition?? ----- 2016.1026

  public List<State> getAllStates() {
    return this.states;
  }

  public void addOneState(State state) {
    this.states.add(state);
  }

  public List<Transition> getAllTransitions() {
    return this.transitions;
  }

  public void addOneTransition(Transition transition) {
    this.transitions.add(transition);
  }

  public void setStartState(State state) {
    this.startstate = state;
  }

  public State getstartstate() {
    return this.startstate;
  }
}
