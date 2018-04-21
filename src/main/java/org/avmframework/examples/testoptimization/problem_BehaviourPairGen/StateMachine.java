package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.*;

public class StateMachine {

	/**
	 * 
	 * 0. A start State, which is just like the root of one tree.
	 * 1. List of all states and list of all transitions.
	 * 
	 * */
	
	private State startstate;
	
	private List<State> states;
	private List<Transition> transitions;
	
	
	
	
	public StateMachine(){
		this.startstate = null;
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		
	}
	public StateMachine(State startstate){
		this.startstate = startstate;
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.addonestate(this.startstate);
	}
	
	/**
	 * this method is a frame, and currently we build state machine manually.
	 * */
	public void initial(){
		//do something
	}
	
	
	/**
	 * Need more methods!
	 * 
	 * How to add transitions when build a state machine!
	 * */
	
	public State getState(String stateName){
		State r = null;
		
		int l = this.getallstates().size();
		for( int i = 0; i < l; i ++){
			if(stateName.equals( this.getallstates().get(i).getStateName())){
				r = this.getallstates().get(i);
			}
		}
		return r;
	}
	public int getStateNO(String stateName){
		
		int r = -1;
		
		int l = this.getallstates().size();
		for( int i = 0; i < l; i ++){
			if(stateName.equals( this.getallstates().get(i).getStateName())){
				r = i;
			}
		}
		return r;
	}
	
	
	//Check whether a state is a new state ----- 2016.1026
	public boolean isANewState(State s){
		boolean checkResult = true;
		int l = this.states.size();
		for(int i = 0; i < l; i ++){
			if( s.isequal_ValueSet( this.states.get(i))){
				//do nothing
			}
			else {
				checkResult = false;
			}
		}
		return checkResult;
	}
	//Check whether a transition is a new transition(only source state and trigger and conditions) ----- 2016.1026
	public boolean isANewTransition(Transition t){
		boolean checkResult = true;
		
		int l = this.transitions.size();
		
		for(int i = 0; i < l; i ++){
			if( this.transitions.get(i).isequalTriggerAndCondition(t))
				break;
		}
		
		return checkResult;
	}
	
	
	
	//Update StateMachine by a New Transition(include judging part) ----- 2016.1026
	public int UpdateStateMachinebyAddANewTransition(Transition t){
		int l_state = this.states.size();
		
		State sourceState_t_inSM = null;
		State targetState_t = t.gettargetstate();
		
		int labeb_whether_break = 0;
		
		for(int i = 0; i < l_state; i ++){
			
			int cr = this.states.get(i).checkRelationshipBetweenTwoStates(targetState_t);
			
			if( cr == 1 || cr == 2){
				
				State ts = this.states.get(i);
				
				List<Transition> lt = ts.getinTransitions();
				
				int l_lt = lt.size();
				for(int j = 0; j < l_lt; j ++){
					if( lt.get(j).getsourcestate().isequal_ValueSet(t.getsourcestate())){
						sourceState_t_inSM = lt.get(j).getsourcestate();
						break;
					}
				}
				
				if( sourceState_t_inSM == null){
					//it is not a new state, but there is a new transition, then update
					
					//Find the actual source state
					for( int k = 0; k < l_state; k ++){
						if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
							sourceState_t_inSM = this.states.get(k);
							break;
						}
					}
					//Add one out transition --- the new transition
					sourceState_t_inSM.addoutTransition(t);
					//Replace the target state
					t.settargetstate(ts);
					//Add one in transition --- the new transition
					ts.addinTransition(t);
					
					this.transitions.add(t);
					
					return 1;
				}
				else {
					//It is not a new transition, do not update
				}
				
				labeb_whether_break = 1;
				break;//find a similar state, then get out of the loop
			}
			else {
				//Not sure whether it is a new state
				//do nothing
			}
			
		}
		if( labeb_whether_break == 0){
			//Do not encounter any break
			//it is a new state, then update
			
			//Find the actual source state
			for( int k = 0; k < l_state; k ++){
				if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
					sourceState_t_inSM = this.states.get(k);
					break;
				}
			}
			//Add one out transition --- the new transition
			sourceState_t_inSM.addoutTransition(t);
			//t.gettargetstate().addinTransition(t);
			
			this.transitions.add(t);
			this.states.add(t.gettargetstate());
			
			//Update the new target state's name
			t.gettargetstate().setStateName("new" + this.states.size());
			
			return 2;
		}
		return 0;
		
	}
	public int CheckNewTransition(Transition t){
		int l_state = this.states.size();
		
		State sourceState_t_inSM = null;
		State targetState_t = t.gettargetstate();
		
		int labeb_whether_break = 0;
		
		for(int i = 0; i < l_state; i ++){
			
			int cr = this.states.get(i).checkRelationshipBetweenTwoStates(targetState_t);
			
			if( cr == 1 || cr == 2){
				
				State ts = this.states.get(i);
				
				List<Transition> lt = ts.getinTransitions();
				
				int l_lt = lt.size();
				for(int j = 0; j < l_lt; j ++){
					if( lt.get(j).getsourcestate().isequal_ValueSet(t.getsourcestate())){
						sourceState_t_inSM = lt.get(j).getsourcestate();
						break;
					}
				}
				
				if( sourceState_t_inSM == null){
					//it is not a new state, but there is a new transition, then update
					
					//Find the actual source state
					for( int k = 0; k < l_state; k ++){
						if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
							sourceState_t_inSM = this.states.get(k);
							break;
						}
					}
					
					return 1;
				}
				else {
					//It is not a new transition, do not update
				}
				
				labeb_whether_break = 1;
				break;//find a similar state, then get out of the loop
			}
			else {
				//Not sure whether it is a new state
				//do nothing
			}
			
		}
		if( labeb_whether_break == 0){
			//Do not encounter any break
			//it is a new state, then update
			
			//Find the actual source state
			for( int k = 0; k < l_state; k ++){
				if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
					sourceState_t_inSM = this.states.get(k);
					break;
				}
			}
			return 2;
		}
		return 0;
		
	}
	//!!Revise related transition?? ----- 2016.1026
	
	
	
	public List<State> getallstates(){
		return this.states;
	}
	public void addonestate(State s){
		this.states.add(s);
	}
	
	public List<Transition> getalltransitions(){
		return this.transitions;
	}
	public void addonetransition(Transition t){
		this.transitions.add(t);
	}
	
	public void setstartstate(State s){
		this.startstate = s;
	}
	public State getstartstate(){
		return this.startstate;
	}
	
}
