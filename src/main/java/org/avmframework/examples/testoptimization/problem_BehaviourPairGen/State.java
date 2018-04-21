package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.avmframework.examples.testoptimization.problem_BehaviourPairGen.*;

public class State {

	/**
	 * 0.state name
	 * 1.describe system state, which consists of system variables
	 *   a.
	 * 2.in-transitions
	 * 3.out-transitions
	 * 
	 * If we have a new state, which haven't been inserted to our state machine, 
	 * its in-transitions and out-transitions are both null/empty.
	 * 
	 * How to represent network environment? --- be not used by state
	 * 4.Insert network environment into state, however network belongs to 
	 * environment. So, the NetworkEnvironment is only used to compute the transition.
	 * 
	 * */
	
	private String stateLabel;
	private HashMap<String, ValueSet> systemVariables_ValueSet;
	
	
	
	
	private String stateName;//0.state name
	
	//private HashMap<String, Double> system_variables_double;
	//private HashMap<String, Integer> system_variables_int;
	private HashMap<String, Object> system_variables;//1.system variables
	
	private List<Transition> in_transitions;//2.in-transitions
	private List<Transition> out_transitions;//3.out-transitions
	
	
	//private NetworkEnvironment networkenvironment;
	//4.network environment, this part exists in transitions!
	
	
	/**
	 * Construct methods
	 * */
	public State(){
		this.system_variables = new HashMap<String, Object>();
		this.in_transitions = new ArrayList<Transition>();
		this.out_transitions = new ArrayList<Transition>();
		
		this.stateLabel = "PredictedState";
	}
	public State(HashMap<String, Object> variables, String name){
		this.system_variables = variables;
		this.in_transitions = new ArrayList<Transition>();
		this.out_transitions = new ArrayList<Transition>();
		this.stateName = name;
		
		this.stateLabel = "PredictedState";
	}
	
	public State(String name, HashMap<String, ValueSet> variables){
		this.systemVariables_ValueSet = variables;
		this.in_transitions = new ArrayList<Transition>();
		this.out_transitions = new ArrayList<Transition>();
		this.stateName = name;
		
		this.stateLabel = "StateMachineState";
	}
	
	/**
	 * Equal method:
	 * Only compare the variables' value between the two states
	 * Assume that the two states' number of variables are totally same.
	 * */
	public boolean isequal(State s){
		
		//if this.stateLabel
		
		if( this.getSystemVariables().size() == s.getSystemVariables().size()){
			HashMap<String, Object> vals = this.system_variables;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
				String key = entry.getKey();
				Object val = entry.getValue();//need to test whether it is right!o==o.
				if( s.getSystemVariables().get(key) != null){
					if( s.getSystemVariables().get(key) == val){
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
	
	public boolean isequal_ValueSet(State s){
		
		//if this.stateLabel
		
		if( this.getSystemVariables_ValueSet().size() == s.getSystemVariables_ValueSet().size()){
			HashMap<String, ValueSet> vals = this.systemVariables_ValueSet;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>)it.next();
				String key = entry.getKey();
				ValueSet val = entry.getValue();//need to test whether it is right!o==o.
				if( s.getSystemVariables_ValueSet().get(key) != null){
					if( s.getSystemVariables_ValueSet().get(key).isequal(val)){
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
	//Check the relationship between two states
	//The state is a new state constructed by program, and its variables are all like "x == v"
	//So, there are 3 types of results
	//the new state == one state :: 2
	//the new state belong to one state :: 1
	//the new state != && not belong to One state :: 0
	public int checkRelationshipBetweenTwoStates(State newState){
		
		List<Integer> checkR = new ArrayList<Integer>();
		
		if( this.getSystemVariables_ValueSet().size() == newState.getSystemVariables_ValueSet().size()){
			
			HashMap<String, ValueSet> vals = this.systemVariables_ValueSet;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>)it.next();
				String key = entry.getKey();
				ValueSet val = entry.getValue();//need to test whether it is right!o==o.
				if( newState.getSystemVariables_ValueSet().get(key) != null){
					int c = checkValueSetRelationship( newState.getSystemVariables_ValueSet().get(key), val);
					checkR.add(c);
				}
				else {
					System.out.println("======= Warning: wrong state check! =========");
					return -1;
				}
			}
			
			int l_cr = checkR.size();
			int tag_0 = 0;
			int tag_1 = 0;
			int tag_2 = 0;
			for( int i = 0; i < l_cr; i ++){
				if( checkR.get(i) == 0){
					tag_0 ++;
				}
				else if( checkR.get(i) == 1){
					tag_1 ++;
				}
				else if( checkR.get(i) == 2){
					tag_2 ++;
				}
			}
			
			if( tag_0 == 0){
				if( tag_1 == 0){
					if( tag_2 == 0){
						System.out.println("======= Warning: check error! =========");
						return -1;
					}
					else {
						return 2;
					}
				}
				else {
					return 1;
				}
			}
			else {
				return 0;
			}
			
		}
		else {
			System.out.println("======= Warning: wrong state check! =========");
			return -1;
		}
		
		
	}
	public int checkValueSetRelationship(ValueSet v1, ValueSet v2){
		
		int l_v1 = v1.getValueSet().size();
		int l_v2 = v2.getValueSet().size();
		//v1 should be the new state's variable
		//v2 should be the existing state's variable
		
		int checkResult = -1;
		
		
		if( l_v1 == 1 && l_v2 == 1){
			
			String con0 = v1.getValueSet().get(0);
			String con1 = v2.getValueSet().get(0);
			
			String[] srcs0 = con0.split(" ");
			String[] srcs1 = con1.split(" ");
			
			if( srcs0[1].equals("==") && srcs1[1].equals("==")){
				int temp0 = Integer.parseInt(srcs0[2]);
				int temp1 = Integer.parseInt(srcs1[2]);
				
				if( temp0 == temp1){
					checkResult = 2;
				}
				else {
					checkResult = 0;
				}
				
			}
			else {
				//checkResult = -1
				System.out.println("Warning: Illegel!");
			}
		}
		else if( l_v1 == 1 && l_v2 == 2){//ex. a < x <b
			
			String con00 = v1.getValueSet().get(0);
			String con10 = v2.getValueSet().get(0);
			String con11 = v2.getValueSet().get(1);
			
			int temp00 = 0;
			int temp10 = 0;
			int temp11 = 0;
			
			String[] srcs00 = con00.split(" ");
			String[] srcs10 = con10.split(" ");
			String[] srcs11 = con11.split(" ");
			
			if( srcs00[1].equals("==")){
				temp00 = Integer.parseInt(srcs00[2]);
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			if( ( srcs10[1].equals("<") || srcs10[1].equals("<="))&& ( srcs11[1].equals(">") || srcs11[1].equals(">="))){
				temp11 = Integer.parseInt( srcs10[2]);
				temp10 = Integer.parseInt( srcs11[2]);
			}
			else if( ( srcs10[1].equals(">") || srcs10[1].equals(">="))&& ( srcs11[1].equals("<") || srcs11[1].equals("<="))){
				temp11 = Integer.parseInt( srcs11[2]);
				temp10 = Integer.parseInt( srcs10[2]);
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			if( temp11 > temp00 && temp10 < temp00){
				checkResult = 1;
			}
			else {
				checkResult = 0;
			}
			
		}
		else {
			System.out.println("=== Warning: illegel! ===");
		}
		
		return checkResult;
	}
	
	
	
	
	
	
	
	
	/**
	 * Transition
	 * Get and Set this class's members Methods
	 * */
	public void addinTransition(Transition t){
		this.in_transitions.add(t);
	}
	public boolean removeinTransition(Transition t){
		if( this.in_transitions.isEmpty()){
			return false;
		}
		else {
			int len = this.in_transitions.size();
			for(int i = 0; i < len; i ++){
				if( this.in_transitions.get(i).isequal(t)){
					this.in_transitions.remove(i);
					System.out.println("number of transitions:" + len + ", " + 
					this.in_transitions.size());
				}
			}
			return true;
		}
	}
	public List<Transition> getinTransitions(){
		return this.in_transitions;
	}
	
	public void addoutTransition(Transition t){
		this.out_transitions.add(t);
	}
	public boolean removeoutTransition(Transition t){
		if( this.out_transitions.isEmpty()){
			return false;
		}
		else {
			int len = this.out_transitions.size();
			for(int i = 0; i < len; i ++){
				if( this.out_transitions.get(i).isequal(t)){
					this.out_transitions.remove(i);
					System.out.println("number of transitions:" + len + ", " + 
					this.out_transitions.size());
				}
			}
			return true;
		}
	}
	public List<Transition> getoutTransitions(){
		return this.out_transitions;
	}
	
	/**
	 * System Variables - Object
	 * Get and Set this class's members Methods
	 * */
	public void setSystemVariables(HashMap<String, Object> variables){
		Iterator it = variables.entrySet().iterator();
		while( it.hasNext()){
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
			String key = entry.getKey();
			Object val = entry.getValue();
			this.setOneSystemVariable(val, key);
		}
	}
	public void setOneSystemVariable(Object value, String variablename){
		this.system_variables.put(variablename, value);
	}
	public HashMap<String, Object> getSystemVariables(){
		return this.system_variables;
		//It is better to have a new variable as the returned result.
	}
	public HashMap<String, ValueSet> getSystemVariables_ValueSet(){
		return this.systemVariables_ValueSet;
	}
	/**
	 * System Variables - ValueSet
	 * Get and Set this class's members Methods
	 * 
	 * The related method is written in StateMachine.
	 * 
	 * */
	
	
	
	
	
	
	/**
	 * StateName
	 * Get and Set this class's members Methods
	 * */
	public String getStateName(){
		return this.stateName;
	}
	public void setStateName(String statename){
		this.stateName = statename;
	}
	
	
	
	
	public static void main(String[] args){
		//test all the methods
	}
	
}
