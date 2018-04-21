package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class NetworkEnvironment {

	private PacketLoss packetloss;
	private PacketDelay packetdelay;
	private PacketDuplication packetduplication;
	private PacketCorruption packetcorruption;
	
	private PacketReordering packetreordering;//abstract
	
	public NetworkEnvironment(){
		this.packetloss = new PacketLoss();
		this.packetdelay = new PacketDelay();
		this.packetduplication = new PacketDuplication();
		this.packetcorruption = new PacketCorruption();
	}
	public NetworkEnvironment(double dataflow_loss_percentage, double dataflow_delay_time,
			double dataflow_duplication_percentage, double dataflow_corruption_percentage){
		this.packetloss = new PacketLoss();
		this.packetloss.setDataflowLossPercentage(dataflow_loss_percentage);
		this.packetdelay = new PacketDelay();
		this.packetdelay.setDataflowDelayTime(dataflow_delay_time);
		this.packetduplication = new PacketDuplication();
		this.packetduplication.setDataflowDuplicationPercentage(dataflow_duplication_percentage);
		this.packetcorruption = new PacketCorruption();
		this.packetcorruption.setDataflowCorruptionPercentage(dataflow_corruption_percentage);
	}
	
	
	
	
	/**
	 * This method may be used in future.
	 * */
	public static NetworkEnvironment deltaNetworkEnvironment( NetworkEnvironment ne_source,
			 NetworkEnvironment ne_target){
		NetworkEnvironment delta = new NetworkEnvironment();
		delta.setPacketLoss( ne_target.getPacketLoss() - ne_source.getPacketLoss());
		delta.setPacketDelay( ne_target.getPacketDelay() - ne_source.getPacketDelay());
		delta.setPacketDuplication( ne_target.getPacketDuplication() - ne_source.getPacketDuplication());
		delta.setPacketCorruption(ne_target.getPacketCorruption() - ne_source.getPacketCorruption());
		return delta;
	}
	
	
	public void setPacketLoss(double dataflow_loss_percentage){
		this.packetloss.setDataflowLossPercentage(dataflow_loss_percentage);
	}
	public double getPacketLoss(){
		return this.packetloss.getDataflowLossPercentage();
	}
	
	public void setPacketDelay(double dataflow_delay_time){
		this.packetdelay.setDataflowDelayTime(dataflow_delay_time);
	}
	public double getPacketDelay(){
		return this.packetdelay.getDataflowDelayTime();
	}
	
	public void setPacketDuplication(double dataflow_duplication_percentage){
		this.packetduplication.setDataflowDuplicationPercentage(dataflow_duplication_percentage);
	}
	public double getPacketDuplication(){
		return this.packetduplication.getDataflowDuplicationPercentage();
	}
	
	public void setPacketCorruption(double dataflow_corruption_percentage){
		this.packetcorruption.setDataflowCorruptionPercentage(dataflow_corruption_percentage);
	}
	public double getPacketCorruption(){
		return this.packetcorruption.getDataflowCorruptionPercentage();
	}
	
	public String getPacketLossName(){
		return this.packetloss.getNetworkPropertyName();
	}
	public String getPacketDelayName(){
		return this.packetdelay.getNetworkPropertyName();
	}
	public String getPacketDuplicationName(){
		return this.packetduplication.getNetworkPropertyName();
	}
	public String getPacketCorruptionName(){
		return this.packetcorruption.getNetworkPropertyName();
	}
	
	
	private class PacketLoss{
		
		private String name;
		private double dataflow_loss_percentage;
		//some other properties
		
		public PacketLoss(){
			this.name = "PacketLoss";
			this.dataflow_loss_percentage = 0;
			//considering the situation that the netem file should be added to the system when the first time!
		}
		
		public String getNetworkPropertyName(){
			return this.name;
		}
		public double getDataflowLossPercentage(){
			return this.dataflow_loss_percentage;
		}
		public void setDataflowLossPercentage(double dataflow_loss_percentage){
			this.dataflow_loss_percentage = dataflow_loss_percentage;
		}
		
		
	}
	private class PacketDelay{
		
		String name;
		double dataflow_delay_time;
		//might have some other properties
		
		public PacketDelay(){
			this.name = "PacketDelay";
			this.dataflow_delay_time = 0;
		}
		
		public String getNetworkPropertyName(){
			return this.name;
		}
		public double getDataflowDelayTime(){
			return this.dataflow_delay_time;
		}
		public void setDataflowDelayTime(double dataflow_delay_time){
			this.dataflow_delay_time = dataflow_delay_time;
		}
		
		
	}
	private class PacketDuplication{
		String name;
		double dataflow_duplication_percentage;
		
		public PacketDuplication(){
			this.name = "PacketDuplication";
			this.dataflow_duplication_percentage = 0;
		}
		
		public String getNetworkPropertyName(){
			return this.name;
		}
		public double getDataflowDuplicationPercentage(){
			return this.dataflow_duplication_percentage;
		}
		public void setDataflowDuplicationPercentage(double dataflow_duplication_percentage){
			this.dataflow_duplication_percentage = dataflow_duplication_percentage;
		}
		
	}
	private class PacketCorruption{
		
		String name;
		double dataflow_corruption_percentage;
		
		public PacketCorruption(){
			this.name = "PacketCorruption";
			this.dataflow_corruption_percentage = 0;
		}
		
		public String getNetworkPropertyName(){
			return this.name;
		}
		public double getDataflowCorruptionPercentage(){
			return this.dataflow_corruption_percentage;
		}
		public void setDataflowCorruptionPercentage(double dataflow_corruption_percentage){
			this.dataflow_corruption_percentage = dataflow_corruption_percentage;
		}
		
		
	}
	
	private class PacketReordering{}
	
	public static void main(String[] args){
		
	}
	
	
}
