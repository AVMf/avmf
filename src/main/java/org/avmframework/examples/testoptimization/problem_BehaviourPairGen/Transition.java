package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.*;


public class Transition {
	
	/**
	 * 
	 * 0. Source state
	 * 1. Target state
	 * 2. Trigger-Event Trigger
	 *    {user operations, network environment parameters}
	 * 3. Conditions - Guard Conditions - Network Environment related
	 *    {}
	 * 4. Effect-ignore for now
	 *    {}
	 * */
	
	private HashMap<String, ValueSet> conditions;
	private List<String> triggers;
	
	
	private State sourcestate;
	private State targetstate;
	
	
	/**
	 * Construct methods
	 * */
	public Transition(){
		this.conditions = new HashMap<String, ValueSet>();
		this.triggers = new ArrayList<String>();
		this.sourcestate = new State();
		this.targetstate = new State();
		
		
	}
	public Transition(State sourcestate, State targetstate){
		this.conditions = new HashMap<String, ValueSet>();
		this.triggers = new ArrayList<String>();
		this.sourcestate = sourcestate;
		this.targetstate = targetstate;
		
		
	}
	public Transition(NetworkEnvironment ne){
		this.conditions = new HashMap<String, ValueSet>();
		this.triggers = new ArrayList<String>();
		this.sourcestate = new State();
		this.targetstate = new State();
		
		
		//this.triggers.add(ne.getPacketDelayName());
		//this.triggers.add(ne.getPacketLossName());
		//this.triggers.add(ne.getPacketCorruptionName());
		//this.triggers.add(ne.getPacketDuplicationName());
		
	}
	
	
	
	
	/**
	 * Functional Methods
	 * SetTriggerByNetworkEnvironment
	 * Set the trigger of the transition by network environment.
	 * 
	 * Haven't been used!
	 * */
	public static void SetTriggerByNetworkEnvironment(Transition t, NetworkEnvironment ne){
		
		String delay = "PacketDelay == ";
		double v_delay = ne.getPacketDelay();
		delay = delay + v_delay;
		
		String loss = "PacketLoss == ";
		double v_loss = ne.getPacketLoss();
		loss = loss + v_loss;
		
		String corrupt = "PacketCorruption == ";
		double v_corrupt = ne.getPacketCorruption();
		corrupt = corrupt + v_corrupt;
		
		String duplicate = "PacketDuplication == ";
		double v_duplicate = ne.getPacketDuplication();
		duplicate = duplicate + v_duplicate;
		
		t.addTriggers(delay);
		t.addTriggers(loss);
		t.addTriggers(corrupt);
		t.addTriggers(duplicate);
		
	}
	
	
	/**
	 * ABANDONED!
	 * Functional Methods
	 * SetTransitionByNetworkEnvironment: actually set the guard conditions
	 * 
	 * */
	public static void SetTransitionByNetworkEnvironment(Transition t, NetworkEnvironment ne){
		/*
		String delay = "PacketDelay == ";
		double v_delay = ne.getPacketDelay();
		delay = delay + v_delay;
		
		String loss = "PacketLoss == ";
		double v_loss = ne.getPacketLoss();
		loss = loss + v_loss;
		
		String corrupt = "PacketCorruption == ";
		double v_corrupt = ne.getPacketCorruption();
		corrupt = corrupt + v_corrupt;
		
		String duplicate = "PacketDuplication == ";
		double v_duplicate = ne.getPacketDuplication();
		duplicate = duplicate + v_duplicate;
		
		t.addConditions(delay);
		t.addConditions(loss);
		t.addConditions(corrupt);
		t.addConditions(duplicate);
		*/
		System.out.println("Method SetTransitionByNetworkEnvironment has been out of time!");
	}
	
	
	//This method is only used in state to set(remove) the transitions of one specific state.
	//Only compare source state, target state, and triggers.
	public boolean isequal(Transition t){
		if( !this.sourcestate.isequal(t.sourcestate)){
			return false;
		}
		if( !this.targetstate.isequal(t.targetstate)){
			return false;
		}
		int len = this.triggers.size();
		if( len == t.getTriggers().size()){
			for(int i = 0; i < len; i ++){
				String tempTriggers = this.triggers.get(i);
				
				boolean temp = false;//This means one condition has the same condition in the other transition.
				for(int j = 0; j < len; j ++){
					if(tempTriggers.equals(t.getTriggers().get(j))){
						temp = true;
					}
				}
				if(temp == false){
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		
		return true;
	}
	
	public boolean isequalTriggerAndCondition(Transition t){
		//Notice!
		if( !this.sourcestate.isequal_ValueSet(t.sourcestate)){
			return false;
		}
		
		//only use trigger's index 0
		if( !t.triggers.get(0).equals(this.triggers.get(0))){
			return false;
		}
		
		//conditions
		if( t.getConditions().size() == this.getConditions().size()){
			HashMap<String, ValueSet> vals = this.conditions;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>)it.next();
				String key = entry.getKey();
				ValueSet val = entry.getValue();
				if( t.getConditions().get(key) != null){
					if( t.getConditions().get(key).isequal(val)){
						//do nothing
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			
			}
			
		}
		else {
			return false;
		}
		return true;
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
	public void setConditions(HashMap<String, ValueSet> GC){
		this.conditions = GC;
	}
	public void addOneCondition(String var, String condition){
		this.conditions.get(var).getValueSet().add(condition);
	}
	public void addConditions(String var, ValueSet vs){
		this.conditions.put(var, vs);
	}
	public HashMap<String, ValueSet> getConditions(){
		return this.conditions;
	}
	public void addTriggers(String trigger){
		this.triggers.add(trigger);
	}
	public List<String> getTriggers(){
		return this.triggers;
	}
	
	
	
	
	public static void main(String[] args){
		
	}
	
	
}
