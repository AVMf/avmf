package org.avmframework.examples.testoptimization.behaviourpair;

public class NetworkEnvironment {

  private PacketLoss packetloss;
  private PacketDelay packetdelay;
  private PacketDuplication packetduplication;
  private PacketCorruption packetcorruption;

  private PacketReordering packetreordering; // abstract

  public NetworkEnvironment() {
    this.packetloss = new PacketLoss();
    this.packetdelay = new PacketDelay();
    this.packetduplication = new PacketDuplication();
    this.packetcorruption = new PacketCorruption();
  }

  public NetworkEnvironment(
      double dataFlowLossPercentage,
      double dataFlowDelayTime,
      double dataFlowDuplicationPercentage,
      double dataFlowCorruptionPercentage) {
    this.packetloss = new PacketLoss();
    this.packetloss.setDataflowLossPercentage(dataFlowLossPercentage);
    this.packetdelay = new PacketDelay();
    this.packetdelay.setDataflowDelayTime(dataFlowDelayTime);
    this.packetduplication = new PacketDuplication();
    this.packetduplication.setDataflowDuplicationPercentage(dataFlowDuplicationPercentage);
    this.packetcorruption = new PacketCorruption();
    this.packetcorruption.setDataflowCorruptionPercentage(dataFlowCorruptionPercentage);
  }

  /** This method may be used in future. */
  public static NetworkEnvironment deltaNetworkEnvironment(
      NetworkEnvironment networkEnvironmentSource, NetworkEnvironment networkEnvironmentTarget) {
    NetworkEnvironment delta = new NetworkEnvironment();
    delta.setPacketLoss(networkEnvironmentTarget.getPacketLoss()
        - networkEnvironmentSource.getPacketLoss());
    delta.setPacketDelay(networkEnvironmentTarget.getPacketDelay()
        - networkEnvironmentSource.getPacketDelay());
    delta.setPacketDuplication(networkEnvironmentTarget.getPacketDuplication()
        - networkEnvironmentSource.getPacketDuplication());
    delta.setPacketCorruption(networkEnvironmentTarget.getPacketCorruption()
        - networkEnvironmentSource.getPacketCorruption());
    return delta;
  }

  public void setPacketLoss(double dataFlowLossPercentage) {
    this.packetloss.setDataflowLossPercentage(dataFlowLossPercentage);
  }

  public double getPacketLoss() {
    return this.packetloss.getDataflowLossPercentage();
  }

  public void setPacketDelay(double dataFlowDelayTime) {
    this.packetdelay.setDataflowDelayTime(dataFlowDelayTime);
  }

  public double getPacketDelay() {
    return this.packetdelay.getDataflowDelayTime();
  }

  public void setPacketDuplication(double dataFlowDuplicationPercentage) {
    this.packetduplication.setDataflowDuplicationPercentage(dataFlowDuplicationPercentage);
  }

  public double getPacketDuplication() {
    return this.packetduplication.getDataflowDuplicationPercentage();
  }

  public void setPacketCorruption(double dataFlowCorruptionPercentage) {
    this.packetcorruption.setDataflowCorruptionPercentage(dataFlowCorruptionPercentage);
  }

  public double getPacketCorruption() {
    return this.packetcorruption.getDataflowCorruptionPercentage();
  }

  public String getPacketLossName() {
    return this.packetloss.getNetworkPropertyName();
  }

  public String getPacketDelayName() {
    return this.packetdelay.getNetworkPropertyName();
  }

  public String getPacketDuplicationName() {
    return this.packetduplication.getNetworkPropertyName();
  }

  public String getPacketCorruptionName() {
    return this.packetcorruption.getNetworkPropertyName();
  }

  private class PacketLoss {

    private String name;
    private double dataFlowLossPercentage;
    // some other properties

    public PacketLoss() {
      this.name = "PacketLoss";
      this.dataFlowLossPercentage = 0;
      // considering the situation that the netem file should be added to the system when the first
      // time!
    }

    public String getNetworkPropertyName() {
      return this.name;
    }

    public double getDataflowLossPercentage() {
      return this.dataFlowLossPercentage;
    }

    public void setDataflowLossPercentage(double dataFlowLossPercentage) {
      this.dataFlowLossPercentage = dataFlowLossPercentage;
    }
  }

  private class PacketDelay {

    String name;
    double dataFlowDelayTime;
    // might have some other properties

    public PacketDelay() {
      this.name = "PacketDelay";
      this.dataFlowDelayTime = 0;
    }

    public String getNetworkPropertyName() {
      return this.name;
    }

    public double getDataflowDelayTime() {
      return this.dataFlowDelayTime;
    }

    public void setDataflowDelayTime(double dataFlowDelayTime) {
      this.dataFlowDelayTime = dataFlowDelayTime;
    }
  }

  private class PacketDuplication {
    String name;
    double dataFlowDuplicationPercentage;

    public PacketDuplication() {
      this.name = "PacketDuplication";
      this.dataFlowDuplicationPercentage = 0;
    }

    public String getNetworkPropertyName() {
      return this.name;
    }

    public double getDataflowDuplicationPercentage() {
      return this.dataFlowDuplicationPercentage;
    }

    public void setDataflowDuplicationPercentage(double dataFlowDuplicationPercentage) {
      this.dataFlowDuplicationPercentage = dataFlowDuplicationPercentage;
    }
  }

  private class PacketCorruption {

    String name;
    double dataFlowCorruptionPercentage;

    public PacketCorruption() {
      this.name = "PacketCorruption";
      this.dataFlowCorruptionPercentage = 0;
    }

    public String getNetworkPropertyName() {
      return this.name;
    }

    public double getDataflowCorruptionPercentage() {
      return this.dataFlowCorruptionPercentage;
    }

    public void setDataflowCorruptionPercentage(double dataFlowCorruptionPercentage) {
      this.dataFlowCorruptionPercentage = dataFlowCorruptionPercentage;
    }
  }

  private class PacketReordering {}

  public static void main(String[] args) {}
}
