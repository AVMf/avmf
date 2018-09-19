package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.*;

public class ValueSet {
	
	private List<String> values;
	
	public ValueSet(){
		this.values = new ArrayList<String>();
	}
	public ValueSet(List<String> v){
		this.values = v;
	}
	
	public boolean isequal(ValueSet vs){
		boolean checkResult = true;
		
		int l_v = this.values.size();
		int l_vs = vs.values.size();
		
		if( l_v == l_vs){
			
			for( int i = 0; i < l_v; i ++){
				int tag = 0;
				for(int j = 0; j < l_vs; j ++){
					if( this.values.get(i).equals(vs.values.get(j))){
						tag = 1;
						break;
					}
					else {
						//do nothing
					}
				}
				if( tag == 0){
					checkResult = false;
					break;
				}
			}
			
		}
		else {
			checkResult = false;
		}
		
		return checkResult;
	}
	
	
	
	
	public void AddConstriantsForValueSet(String c){
		this.values.add(c);
	}
	public void RemoveConstriantsForValueSet(int index){
		this.values.remove(index);
	}
	public void setValueSet(List<String> constraints){
		this.values = constraints;
	}
	public List<String> getValueSet(){
		return this.values;
	}
	
	
	/**
	 * 
	 * type:
	 * 0-INTEGER_CATEGORICAL_TYPE
	 * 1-DOUBLE_CATEGORICAL_TYPE(impossible type)
	 * 2-INTEGER_NUMERICAL_TYPE
	 * 3-DOUBLE_NUMERICAL_TYPE
	 * 
	 * */
	public static void CalculateConstraints(){
		
	}
	
	
	
	
	
	
	public static void main(String[] args){
		
		//Idle: activecall 0, videoquality 0
		
		ValueSet vs_idle_1 = new ValueSet();
		vs_idle_1.AddConstriantsForValueSet("activecall == 0");
		ValueSet vs_idle_2 = new ValueSet();
		vs_idle_2.AddConstriantsForValueSet("videoquality == 0");
		
		//Connected_1: activecall 1, videoquality 3
		
		ValueSet vs_idle_3 = new ValueSet();
		vs_idle_3.AddConstriantsForValueSet("activecall == 1");
		ValueSet vs_idle_4 = new ValueSet();
		vs_idle_4.AddConstriantsForValueSet("videoquality == 3");
		
		//Notfull: 1 < activecall < 4, videoquality 3(max = 4, need to know <, <=, >, >=, ==)
		
		ValueSet vs_idle_5 = new ValueSet();
		vs_idle_5.AddConstriantsForValueSet("activecall > 1");
		vs_idle_5.AddConstriantsForValueSet("activecall < 4");
		ValueSet vs_idle_6 = new ValueSet();
		vs_idle_6.AddConstriantsForValueSet("videoquality == 3");
		
		
		
		
	}
	
}
