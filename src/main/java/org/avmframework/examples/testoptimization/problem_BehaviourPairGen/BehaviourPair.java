package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.*;

public class BehaviourPair {

	/**
	 * It is very familiar with transition.
	 * NetworkEnvironment---actually exists in transitions.
	 * 
	 * However, BP is more similar with test cases, and it represents one situation
	 * of all the situation represented by transition.
	 * 
	 * 1.source state, 2.target state, 3.triggers, 4.the value of the variable of
	 * the guard conditions(eg. network environment, using specific data structure
	 * to represent)
	 * 
	 * */
	
	private State sourcestate;
	private State targetstate;
	
	//transition - trigger
	private List<String> triggers;
	
	//transition - GC's concrete value - network environment
	private NetworkEnvironment networkenvironmentVariables;
	
	/**
	 * Construct methods
	 * */
	public BehaviourPair(){
		this.sourcestate = new State();
		this.targetstate = new State();
		this.triggers = new ArrayList<String>();
		this.networkenvironmentVariables = new NetworkEnvironment();
	}
	public BehaviourPair(State source, List<String> t, State target, NetworkEnvironment ne){//network part unfinished
		this.sourcestate = source;
		this.targetstate = target;
		this.triggers = t;
		this.networkenvironmentVariables = ne;
	}
	
	/**
	 * Parser to BPair list from a state machine
	 * */
	public static List<BehaviourPair> StateMachineParser(StateMachine statemachine){
		
		List<BehaviourPair> behaviourpairs = new ArrayList<BehaviourPair>();
		/*
		List<Transition> transitionsinStateMachine = statemachine.getalltransitions();
		//List<State> statesinStateMachine = statemachine.getallstates();
		int len = transitionsinStateMachine.size();
		for(int i = 0; i < len; i ++){
			//network part unfinished
			State source = transitionsinStateMachine.get(i).getsourcestate();
			State target = transitionsinStateMachine.get(i).gettargetstate();
			Transition t = transitionsinStateMachine.get(i);
			
			NetworkEnvironment ne = null
			//= new NetworkEnvironment(t)
			;
			BehaviourPair BPair = new BehaviourPair(source, t, target, ne);
			behaviourpairs.add(BPair);
		}*/
		return behaviourpairs;
	}
	
	
	/**
	 * Set and Get this class's members Methods
	 * */
	public State getsourcestate(){
		return this.sourcestate;
	}
	public void setsourcestate(State s){
		this.sourcestate = s;
	}
	public State gettargetstate(){
		return this.targetstate;
	}
	public void settargetstate(State s){
		this.targetstate = s;
	}
	public List<String> gettrigger(){
		return this.triggers;
	}
	public void settrigger(List<String> t){
		this.triggers = t;
	}
	public NetworkEnvironment getnetworkenvironment(){
		return this.networkenvironmentVariables;
	}
	public void setnetworkenvironment(NetworkEnvironment ne){
		this.networkenvironmentVariables = ne;
	}
}
