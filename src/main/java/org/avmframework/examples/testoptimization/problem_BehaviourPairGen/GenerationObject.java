package org.avmframework.examples.testoptimization.problem_BehaviourPairGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.FixedPointVariable;
import org.avmframework.variable.IntegerVariable;

public class GenerationObject extends ObjectiveFunction {

  private ConstraintList constraints; // Its set method uses BPair
  private StateMachine existingstatemachine;
  private List<BehaviourPair> ListofExistingBehaviourPairs;
  private List<BehaviourPair> ListofExistingTestCases;

  private List<Solution> SolutionsOfExistingBehaviourPairs;
  private List<Solution> SolutionsOfExistingTestCases;

  public List<BehaviourPair> getExistingTestCases() {
    return this.ListofExistingTestCases;
  }

  public void setExistingTestCases(List<BehaviourPair> ExistingTestCases) {
    this.ListofExistingTestCases = ExistingTestCases;
  }

  public void AddOneTestCase2ExistingTestCases(BehaviourPair tc) {
    this.ListofExistingTestCases.add(tc);
  }

  public void setSolutionsOfExistingTestCases(List<Solution> ExistingTestCases) {
    this.SolutionsOfExistingTestCases = ExistingTestCases;
  }

  public List<Solution> getSolutionsOfExistingTestCases() {
    return this.SolutionsOfExistingTestCases;
  }

  public List<BehaviourPair> getListofExistingBehaviourPairs() {
    return this.ListofExistingBehaviourPairs;
  }

  /** Construct Methods */
  public GenerationObject() {}

  public GenerationObject(StateMachine existingstatemachine) {
    this.setexistingstatemachine(existingstatemachine);
  }

  /**
   * ===== This set function is only for initial. ==== ===== When updating, need another function,
   * which only change other but constraints. =====
   */
  public void setexistingstatemachine(StateMachine existingstatemachine) {
    this.existingstatemachine = existingstatemachine;
    this.ListofExistingBehaviourPairs = BehaviourPair.StateMachineParser(this.existingstatemachine);
    System.out.println(
        "========== length ListofExistingBehaviourPairs: "
            + this.ListofExistingBehaviourPairs.size());
    // this.setConstraints();
  }

  public StateMachine getexistingstatemachine() {
    return this.existingstatemachine;
  }

  public void EmptySetOfSolutionsOfExistingBehaviourPairs() {
    this.SolutionsOfExistingBehaviourPairs = null;
  }

  public void FillSetofSolutionsOfExistingBehaviourPairs(
      List<BehaviourPair> ExistingBehaviourPairs) {
    this.SolutionsOfExistingBehaviourPairs = new ArrayList<Solution>();
    int len = ExistingBehaviourPairs.size();
    for (int i = 0; i < len; i++) {
      Solution temp = TransformBPair2Solution(ExistingBehaviourPairs.get(i));
      this.SolutionsOfExistingBehaviourPairs.add(temp);
    }
  }

  public void InitialSetOfExistingTestCases() {
    // EmptySetOfExistingTestCases();
    this.SolutionsOfExistingTestCases = new ArrayList<Solution>();
  }

  // Used when reading BehaviourPairs from files.
  public Solution TransformBPair2Solution(BehaviourPair bpair) {
    Solution v = new Solution();

    // Source State
    State sourceState = bpair.getsourcestate();
    List<State> allexistingstates = this.existingstatemachine.getallstates();
    int len = allexistingstates.size();
    for (int i = 0; i < len; i++) {
      State currentState = allexistingstates.get(i);
      if (currentState.getStateName().equals(sourceState.getStateName())) {
        v.addsolutionmember("SourceState", i);
        break;
      }
    }

    // Target State
    /** ### Need to consider whether the type is the correct type! ### */
    State targetState = bpair.gettargetstate();
    v.addsolutionmember("activecall", targetState.getSystemVariables().get("activecall"));
    v.addsolutionmember("videoquality", targetState.getSystemVariables().get("videoquality"));

    return v;
  }

  /*
   * When generating test cases(BPs here, actually), traverse every constraint and pick up one value.
   * type:
   * 0-INTEGER_CATEGORICAL_TYPE
   * 1-DOUBLE_CATEGORICAL_TYPE(impossible type)
   * 2-INTEGER_NUMERICAL_TYPE
   * 3-DOUBLE_NUMERICAL_TYPE
   *
   * */

  public Vector setUpVector(int size) {

    Vector vector = new Vector();
    // source state label
    vector.addVariable(
        new IntegerVariable(0, 0, this.existingstatemachine.getallstates().size() - 1));
    // target state - activecall
    vector.addVariable(new IntegerVariable(0, 0, 3));
    // target state - videoquality
    vector.addVariable(new IntegerVariable(0, 0, 3));
    // user operation
    vector.addVariable(new IntegerVariable(0, 0, 2));
    // network condition - Packet Loss
    vector.addVariable(new FixedPointVariable(0, 1, 0, 100));
    // network condition - Delay
    vector.addVariable(new FixedPointVariable(0, 1, 0, 100));
    // network condition - Packet Duplication
    vector.addVariable(new FixedPointVariable(0, 1, 0, 100));
    // network condition - Packet Corruption
    vector.addVariable(new FixedPointVariable(0, 1, 0, 100));
    return vector;
  }

  public void setConstraints() {
    // TODO Auto-generated method stub
    // Scheme: from BehaviourPair to Problem Constraints
    this.constraints = new ConstraintList();

    this.constraints.addOneConstraint(
        "SourceState", 0, this.existingstatemachine.getallstates().size() - 1, 0);

    // Target State(search variables)
    this.constraints.addOneConstraint("activecall", 0, 3, 2);
    this.constraints.addOneConstraint("videoquality", 0, 3, 2);

    this.constraints.addOneConstraint("UserOperation", 0, 2, 0); // 0-null, 1-dial, 2-disconnect

    this.constraints.addOneConstraint("PacketLoss", 0, 100, 3);
    this.constraints.addOneConstraint("PacketDelay", 0, 100, 3);
    this.constraints.addOneConstraint("PacketDuplication", 0, 100, 3);
    this.constraints.addOneConstraint("PacketCorruption", 0, 100, 3);
  }

  public ConstraintList getConstraints() {
    // TODO Auto-generated method stub
    return this.constraints;
  }

  public ObjectiveValue computeObjectiveValue(Vector v) {

    double result = 0;

    // first, the similarity of the current solution with the existing behavior pairs which are from
    // state machine.
    double result_similarity = 0;
    List<Transition> lt = this.getexistingstatemachine().getalltransitions();
    int len1 = lt.size();
    int clen1 = len1;

    for (int i = 0; i < len1; i++) {
      double tempp = SimilarityBetweenVectorAndTransition(v, lt.get(i));
      if (tempp == 0) {
        clen1--;
      } else {
        result_similarity = result_similarity + tempp;
      }
    }
    result_similarity = result_similarity / clen1;

    // second, diversity with the existing solutions
    // Using the solutions of SolutionsOfExistingTestCases directly.
    int len2 = this.SolutionsOfExistingTestCases.size();
    double result_diversity = 0;

    for (int i = 0; i < len2; i++) {
      result_diversity =
          result_diversity + DiversityOfTwoVectors(v, this.SolutionsOfExistingTestCases.get(i));
    }

    result_diversity = result_diversity / len2;

    result =
        max_min_normalization(result_diversity, 0, 3)
            + max_min_normalization(result_similarity, 0, 3);
    result = 1 - result / 2;

    return NumericObjectiveValue.LowerIsBetterObjectiveValue(result, 0);
  }

  public static double normalization(double x) {
    double nor = x / (x + 1);
    return nor;
  }

  public static double max_min_normalization(double x, double min, double max) {
    double nor = (x - min) / (max - min);
    return nor;
  }

  /** ValueSet. first version */
  public double variableConstraintConstraintDistance(ValueSet Vc, ValueSet BPc) {

    int l_Vc = Vc.getValueSet().size();
    int l_BPc = BPc.getValueSet().size();
    double distance = 0;

    if (l_Vc == 1 && l_BPc == 1) {

      String con0 = Vc.getValueSet().get(0);
      String con1 = BPc.getValueSet().get(0);

      String[] srcs0 = con0.split(" ");
      String[] srcs1 = con1.split(" ");

      if (srcs0[1].equals("==") && srcs1[1].equals("==")) {
        int temp0 = Integer.parseInt(srcs0[2]);
        int temp1 = Integer.parseInt(srcs1[2]);

        distance = normalization((double) temp0) - normalization((double) temp1);

      } else {
        System.out.println("Warning: Illegel!");
      }

    } else if (l_Vc == 1 && l_BPc == 2) {

      String con00 = Vc.getValueSet().get(0);
      String con10 = BPc.getValueSet().get(0);
      String con11 = BPc.getValueSet().get(1);

      int temp00 = 0;
      int temp10 = 0;
      int temp11 = 0;

      String[] srcs00 = con00.split(" ");
      String[] srcs10 = con10.split(" ");
      String[] srcs11 = con11.split(" ");

      double mid1 = 1;

      if (srcs00[1].equals("==")) {
        temp00 = Integer.parseInt(srcs00[2]);
      } else {
        System.out.println("Warning: Illegel!");
      }

      if ((srcs10[1].equals("<") || srcs10[1].equals("<="))
          && (srcs11[1].equals(">") || srcs11[1].equals(">="))) {
        temp11 = Integer.parseInt(srcs10[2]);
        temp10 = Integer.parseInt(srcs11[2]);
        mid1 = ((double) temp11 - (double) temp10) / 2 + (double) temp10;
      } else if ((srcs10[1].equals(">") || srcs10[1].equals(">="))
          && (srcs11[1].equals("<") || srcs11[1].equals("<="))) {
        temp11 = Integer.parseInt(srcs11[2]);
        temp10 = Integer.parseInt(srcs10[2]);
        mid1 = ((double) temp11 - (double) temp10) / 2 + (double) temp10;
      } else {
        System.out.println("Warning: Illegel!");
      }

      distance = normalization((double) temp00) - normalization(mid1);

    } else if (l_Vc == 2 && l_BPc == 1) {

      String con00 = Vc.getValueSet().get(0);
      String con01 = Vc.getValueSet().get(1);
      String con11 = BPc.getValueSet().get(0);

      int temp00 = 0;
      int temp01 = 0;
      int temp11 = 0;

      String[] srcs00 = con00.split(" ");
      String[] srcs01 = con01.split(" ");
      String[] srcs11 = con11.split(" ");

      double mid0 = 1;

      if ((srcs00[1].equals("<") || srcs00[1].equals("<="))
          && (srcs01[1].equals(">") || srcs01[1].equals(">="))) {
        temp01 = Integer.parseInt(srcs00[2]);
        temp00 = Integer.parseInt(srcs01[2]);
        mid0 = ((double) temp01 - (double) temp00) / 2 + (double) temp00;
      } else if ((srcs00[1].equals(">") || srcs00[1].equals(">="))
          && (srcs01[1].equals("<") || srcs01[1].equals("<="))) {
        temp01 = Integer.parseInt(srcs01[2]);
        temp00 = Integer.parseInt(srcs00[2]);
        mid0 = ((double) temp01 - (double) temp00) / 2 + (double) temp00;
      } else {
        System.out.println("Warning: Illegel!");
      }

      if (srcs11[1].equals("==")) {
        temp11 = Integer.parseInt(srcs11[2]);
      } else {
        System.out.println("Warning: Illegel!");
      }

      distance = normalization(mid0) - normalization((double) temp11);

    } else if (l_Vc == 2 && l_BPc == 2) {

      String con00 = Vc.getValueSet().get(0);
      String con01 = Vc.getValueSet().get(1);
      String con10 = BPc.getValueSet().get(0);
      String con11 = BPc.getValueSet().get(1);

      int temp00 = 0;
      int temp01 = 0;
      int temp10 = 0;
      int temp11 = 0;

      String[] srcs00 = con00.split(" ");
      String[] srcs01 = con01.split(" ");
      String[] srcs10 = con10.split(" ");
      String[] srcs11 = con11.split(" ");

      double mid0 = 0;
      double mid1 = 1;

      if ((srcs00[1].equals("<") || srcs00[1].equals("<="))
          && (srcs01[1].equals(">") || srcs01[1].equals(">="))) {
        temp01 = Integer.parseInt(srcs00[2]);
        temp00 = Integer.parseInt(srcs01[2]);
        mid0 = ((double) temp01 - (double) temp00) / 2 + (double) temp00;
      } else if ((srcs00[1].equals(">") || srcs00[1].equals(">="))
          && (srcs01[1].equals("<") || srcs01[1].equals("<="))) {
        temp01 = Integer.parseInt(srcs01[2]);
        temp00 = Integer.parseInt(srcs00[2]);
        mid0 = ((double) temp01 - (double) temp00) / 2 + (double) temp00;
      } else {
        System.out.println("Warning: Illegel!");
      }

      if ((srcs10[1].equals("<") || srcs10[1].equals("<="))
          && (srcs11[1].equals(">") || srcs11[1].equals(">="))) {
        temp11 = Integer.parseInt(srcs10[2]);
        temp10 = Integer.parseInt(srcs11[2]);
        mid1 = ((double) temp11 - (double) temp10) / 2 + (double) temp10;
      } else if ((srcs10[1].equals(">") || srcs10[1].equals(">="))
          && (srcs11[1].equals("<") || srcs11[1].equals("<="))) {
        temp11 = Integer.parseInt(srcs11[2]);
        temp10 = Integer.parseInt(srcs10[2]);
        mid1 = ((double) temp11 - (double) temp10) / 2 + (double) temp10;
      } else {
        System.out.println("Warning: Illegel!");
      }

      distance = normalization(mid1) - normalization(mid0);

    } else {
      System.out.println("Warning: Illegel constraints!");
    }

    return distance;
  }

  public double variableValueConstraintDistance(int Vv, ValueSet BPc) {

    int l = BPc.getValueSet().size();

    double distance = 0;

    if (l == 1) {
      String con = BPc.getValueSet().get(0);
      String[] srcs = con.split(" ");
      if (srcs[1].equals("==")) {

        int tempv1 = Integer.parseInt(srcs[2]);
        // System.out.println("srcs[2]: " + srcs[2]);

        distance = normalization((double) Vv) - normalization((double) tempv1);

      } else if (srcs[1].equals("<")) {
        System.out.println("For now, Illegal constraint.");
      } else if (srcs[1].equals("<=")) {
        System.out.println("For now, Illegal constraint.");
      } else if (srcs[1].equals(">")) {
        System.out.println("For now, Illegal constraint.");
      } else if (srcs[1].equals(">=")) {
        System.out.println("For now, Illegal constraint.");
      } else {
        System.out.println("Warning: Illegal constraint!");
      }

    } else if (l == 2) {

      // Only considering part of situation

      String con0 = BPc.getValueSet().get(0);
      String con1 = BPc.getValueSet().get(1);

      int tempv1 = 0;
      int tempv2 = 0;

      String[] srcs0 = con0.split(" ");
      if (srcs0[1].equals("==")) {
        System.out.println("For now, Illegal constraint.");
      } else if (srcs0[1].equals("<")) {
        tempv2 = Integer.parseInt(srcs0[2]);
      } else if (srcs0[1].equals("<=")) {
        tempv2 = Integer.parseInt(srcs0[2]);
      } else if (srcs0[1].equals(">")) {
        tempv1 = Integer.parseInt(srcs0[2]);
      } else if (srcs0[1].equals(">=")) {
        tempv1 = Integer.parseInt(srcs0[2]);
      } else {
        System.out.println("Warning: Illegal constraint!");
      }

      String[] srcs1 = con1.split(" ");
      if (srcs1[1].equals("==")) {
        System.out.println("For now, Illegal constraint.");
      } else if (srcs1[1].equals("<")) {
        tempv2 = Integer.parseInt(srcs0[2]);
      } else if (srcs1[1].equals("<=")) {
        tempv2 = Integer.parseInt(srcs0[2]);
      } else if (srcs1[1].equals(">")) {
        tempv1 = Integer.parseInt(srcs0[2]);
      } else if (srcs1[1].equals(">=")) {
        tempv1 = Integer.parseInt(srcs0[2]);
      } else {
        System.out.println("Warning: Illegal constraint!");
      }

      double mid = ((double) tempv2 - (double) tempv1) / 2 + (double) tempv1;
      distance = normalization((double) Vv) - normalization(mid);

    } else {
      System.out.println("Warning: illegal ValueSet!");
    }

    return distance;
  }

  /** 201606 ValueSet the second version */
  public double DiversityOfTwoVectors(Vector v1, Solution v2) { // Distance of two solutions

    // map all variables into the interval (0, 1), then use measures, for instance, Euclidean
    // metric.

    // HashMap<String, Object> solution_v1 = v1.getsolution();
    HashMap<String, Object> solution_v2 = v2.getsolution();

    // v1
    // source state
    State sourceState_v1 =
        this.existingstatemachine
            .getallstates()
            .get(Integer.parseInt(v1.getVariable(0).toString()));
    // source state v2
    State sourceState_v2 =
        this.existingstatemachine.getallstates().get((int) solution_v2.get("SourceState"));
    double distance_source_ac =
        variableCalculateConstraintDistance_v201606(
            sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
            sourceState_v2.getSystemVariables_ValueSet().get("activecall"));
    double distance_source_vq =
        variableCalculateConstraintDistance_v201606(
            sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
            sourceState_v2.getSystemVariables_ValueSet().get("videoquality"));

    // target state
    int activecall_targetState_v1 = Integer.parseInt(v1.getVariable(1).toString());
    int videoquality_targetState_v1 = Integer.parseInt(v1.getVariable(2).toString());
    double nor_activecall_targetState_v1 = normalization((double) activecall_targetState_v1);
    double nor_videoquality_targetState_v1 = normalization((double) videoquality_targetState_v1);
    // user operations
    int useroperation_v1 = Integer.parseInt(v1.getVariable(3).toString());
    double nor_useroperation_v1 = normalization((double) useroperation_v1);

    // network environment
    double packetloss_v1 = Double.parseDouble(v1.getVariable(4).toString());
    double packetdelay_v1 = Double.parseDouble(v1.getVariable(5).toString());
    double packetduplication_v1 = Double.parseDouble(v1.getVariable(6).toString());
    double packetcorruption_v1 = Double.parseDouble(v1.getVariable(7).toString());
    double nor_packetloss_v1 = normalization(packetloss_v1);
    double nor_packetdelay_v1 = normalization(packetdelay_v1);
    double nor_packetduplication_v1 = normalization(packetduplication_v1);
    double nor_packetcorruption_v1 = normalization(packetcorruption_v1);

    // v2

    // target state
    int activecall_targetState_v2 = (int) solution_v2.get("activecall");
    int videoquality_targetState_v2 = (int) solution_v2.get("videoquality");
    double nor_activecall_targetState_v2 = normalization((double) activecall_targetState_v2);
    double nor_videoquality_targetState_v2 = normalization((double) videoquality_targetState_v2);

    // user operations
    int useroperation_v2 = (int) solution_v2.get("UserOperation");
    double nor_useroperation_v2 = normalization((double) useroperation_v2);

    // network environment
    double packetloss_v2 = (double) solution_v2.get("PacketLoss");
    double packetdelay_v2 = (double) solution_v2.get("PacketDelay");
    double packetduplication_v2 = (double) solution_v2.get("PacketDuplication");
    double packetcorruption_v2 = (double) solution_v2.get("PacketCorruption");
    double nor_packetloss_v2 = normalization(packetloss_v2);
    double nor_packetdelay_v2 = normalization(packetdelay_v2);
    double nor_packetduplication_v2 = normalization(packetduplication_v2);
    double nor_packetcorruption_v2 = normalization(packetcorruption_v2);

    // Eculidean Metric
    double result = 0;
    result =
        Math.pow(normalization(distance_source_ac), 2)
            + Math.pow(normalization(distance_source_vq), 2)
            + Math.pow((nor_activecall_targetState_v1 - nor_activecall_targetState_v2), 2)
            + Math.pow((nor_videoquality_targetState_v1 - nor_videoquality_targetState_v2), 2)
            + Math.pow((nor_useroperation_v1 - nor_useroperation_v2), 2)
            + Math.pow((nor_packetloss_v1 - nor_packetloss_v2), 2)
            + Math.pow((nor_packetdelay_v1 - nor_packetdelay_v2), 2)
            + Math.pow((nor_packetduplication_v1 - nor_packetduplication_v2), 2)
            + Math.pow((nor_packetcorruption_v1 - nor_packetcorruption_v2), 2);
    result = Math.sqrt(result);

    return result;
  }

  public double SimilarityBetweenVectorAndTransition(
      Vector v1, Transition t1) { // 9 variables in space

    double result = 3 - DistanceBetweenVectorAndTransition(v1, t1);

    return result;
  }

  public double DistanceBetweenVectorAndTransition(
      Vector v1, Transition t1) { // Distance of two solutions

    // map all variables into the interval (0, 1), then use measures, for instance, Euclidean
    // metric.

    // Distances of the source states
    State sourceState_v1 =
        this.existingstatemachine
            .getallstates()
            .get(Integer.parseInt(v1.getVariable(0).toString()));
    State sourceState_t = t1.getsourcestate();
    double distance_source_ac =
        variableCalculateConstraintDistance_v201606(
            sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
            t1.getsourcestate().getSystemVariables_ValueSet().get("activecall"));
    double distance_source_vq =
        variableCalculateConstraintDistance_v201606(
            sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
            t1.getsourcestate().getSystemVariables_ValueSet().get("videoquality"));

    double nor_distance_source_ac = normalization(distance_source_ac);
    double nor_distance_source_vq = normalization(distance_source_vq);

    // Distances of the target states
    int activecall_targetState_v1 = Integer.parseInt(v1.getVariable(1).toString());
    int videoquality_targetState_v1 = Integer.parseInt(v1.getVariable(2).toString());
    ValueSet activecall_targetState_t =
        t1.gettargetstate().getSystemVariables_ValueSet().get("activecall");
    ValueSet videoquality_targetState_t =
        t1.gettargetstate().getSystemVariables_ValueSet().get("videoquality");

    double distance_target_ac =
        variableCalculateConstraintDistance_v201606(
            activecall_targetState_v1, activecall_targetState_t);
    double distance_target_vq =
        variableCalculateConstraintDistance_v201606(
            videoquality_targetState_v1, videoquality_targetState_t);

    double nor_distance_target_ac = normalization(distance_target_ac);
    double nor_distance_target_vq = normalization(distance_target_vq);

    // Distances of the triggers
    int useroperation_v1 = Integer.parseInt(v1.getVariable(3).toString());
    double nor_useroperation_v1 = normalization((double) useroperation_v1);
    int useroperation_BP;
    String useroperation_String_BP = t1.getTriggers().get(0);
    if (useroperation_String_BP.equals("null")) {
      useroperation_BP = 0;
    } else if (useroperation_String_BP.equals("dial")) {
      useroperation_BP = 1;
    } else if (useroperation_String_BP.equals("disconnect")) {
      useroperation_BP = 2;
    } else {
      useroperation_BP = -1;
      System.out.println("***** Warning: calculate triggers! *****");
    }
    double nor_useroperation_BP = normalization((double) useroperation_BP);
    double distance_trigger = nor_useroperation_BP - nor_useroperation_v1;

    // Distances of Network Environment
    double packetloss_v1 = Double.parseDouble(v1.getVariable(4).toString());
    double packetdelay_v1 = Double.parseDouble(v1.getVariable(5).toString());
    double packetduplication_v1 = Double.parseDouble(v1.getVariable(6).toString());
    double packetcorruption_v1 = Double.parseDouble(v1.getVariable(7).toString());

    ValueSet packetloss_t = t1.getConditions().get("PacketLoss");
    ValueSet packetdelay_t = t1.getConditions().get("PacketDelay");
    ValueSet packetduplication_t = t1.getConditions().get("PacketDuplication");
    ValueSet packetcorruption_t = t1.getConditions().get("PacketCorruption");
    double distance_packetloss =
        variableCalculateConstraintDistance_v201606(packetloss_v1, packetloss_t);
    double distance_packetdelay =
        variableCalculateConstraintDistance_v201606(packetdelay_v1, packetdelay_t);
    double distance_packetduplication =
        variableCalculateConstraintDistance_v201606(packetduplication_v1, packetduplication_t);
    double distance_packetcorruption =
        variableCalculateConstraintDistance_v201606(packetcorruption_v1, packetcorruption_t);

    double nor_distance_packetloss = normalization(distance_packetloss);
    double nor_distance_packetdelay = normalization(distance_packetdelay);
    double nor_distance_packetduplication = normalization(distance_packetduplication);
    double nor_distance_packetcorruption = normalization(distance_packetcorruption);

    double result = 0;
    result =
        Math.pow(nor_distance_source_ac, 2)
            + Math.pow(nor_distance_source_vq, 2)
            + Math.pow(nor_distance_target_ac, 2)
            + Math.pow(nor_distance_target_vq, 2)
            + Math.pow(distance_trigger, 2)
            + Math.pow(nor_distance_packetloss, 2)
            + Math.pow(nor_distance_packetdelay, 2)
            + Math.pow(nor_distance_packetduplication, 2)
            + Math.pow(nor_distance_packetcorruption, 2);
    result = Math.sqrt(result);

    if (packetloss_v1 < 10
        || packetdelay_v1 < 1
        || packetduplication_v1 < 1
        || packetcorruption_v1 < 1) {
      return 3.0;
    }
    if (distance_packetloss < 1
        || distance_packetdelay < 1
        || distance_packetduplication < 1
        || distance_packetcorruption < 1) {
      return 3.0;
    }
    if (distance_packetloss
            + distance_packetdelay
            + distance_packetduplication
            + distance_packetcorruption
        < 20) {
      return 3.0;
    }

    return result;
  }

  public double variableCalculateConstraintDistance_v201606(double Vv, ValueSet BPc) {

    List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
    if (BPc == null) {
      System.out.println("BPc == null");
    }
    if (BPc.getValueSet().isEmpty()) {
      System.out.println("BPc.getValueSet().isEmpty()");
    }
    int l = BPc.getValueSet().size();

    for (int i = 0; i < l; i++) {
      String[] srcs = BPc.getValueSet().get(i).split(" ");
      ValueSetConstraint temp = new ValueSetConstraint();
      temp.varLeft = srcs[0];
      temp.operator = srcs[1];
      temp.varRight = srcs[2];
      BPCList.add(temp);

      // System.out.println("Operator:" + temp.operator);

    }

    int numOfLessThan = 0;
    int numOfGreaterThan = 0;
    int numOfEqualTo = 0;

    for (int i = 0; i < l; i++) {

      if (BPCList.get(i).operator.equals("==")) {
        // System.out.println("==");
        numOfEqualTo++;
      } else if (BPCList.get(i).operator.equals("<=") || BPCList.get(i).operator.equals("<")) {
        numOfLessThan++;
        // System.out.println("Less");
      } else if (BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")) {
        numOfGreaterThan++;
        // System.out.println("Greater");
      } else {
        System.out.println("***** Error: Wrong operator! --- *****");
        return -1;
      }
    }

    if ((numOfLessThan >= 2)
        || (numOfGreaterThan >= 2)
        || (numOfLessThan == 1 && numOfGreaterThan >= 2)
        || (numOfLessThan >= 2 && numOfGreaterThan == 1)) {
      System.out.println("***** Warning: Unsolved situation 1! *****");
      return -1;
    }

    double distance = 0;

    // Normalization
    Vv = normalization(Vv);

    if (numOfLessThan == 0 && numOfGreaterThan == 0) {
      if (numOfEqualTo == 1) {
        distance = Vv - normalization(Double.parseDouble(BPCList.get(0).varRight));
        if (distance < 0) {
          distance = distance * (-1);
        }
      } else {
        System.out.println("***** Warning: Some Impossible situation! *****");
        return -1;
      }
    } else if (numOfLessThan == 0 && numOfGreaterThan == 1) {
      double scrop = 0;
      for (int i = 0; i < l; i++) {
        if (BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")) {
          scrop = Double.parseDouble(BPCList.get(i).varRight);
        }
      }
      if (Vv >= scrop) {
        distance = 0;
      } else {
        distance = normalization(scrop) - Vv;
      }
    } else if (numOfLessThan == 1 && numOfGreaterThan == 0) {
      double scrop = 0;
      for (int i = 0; i < l; i++) {
        if (BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")) {
          scrop = Double.parseDouble(BPCList.get(i).varRight);
        }
      }
      if (Vv <= scrop) {
        distance = 0;
      } else {
        distance = Vv - normalization(scrop);
      }
    } else if (numOfLessThan == 1 && numOfGreaterThan == 1) {
      double scropG = 0;
      double scropL = 0;
      for (int i = 0; i < l; i++) {
        if (BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")) {
          scropG = Double.parseDouble(BPCList.get(i).varRight);
        }
        if (BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")) {
          scropL = Double.parseDouble(BPCList.get(i).varRight);
        }
      }
      if (scropG <= scropL) {
        if (Vv <= scropL && Vv >= scropG) {
          distance = 0;
        } else if (Vv <= scropL && Vv <= scropG) {
          distance = normalization(scropG) - Vv;
        } else if (Vv >= scropL && Vv >= scropG) {
          distance = Vv - normalization(scropL);
        } else {
          System.out.println("***** Warning: Impossible situation! *****");
          return -1;
        }
      } else {
        if (Vv >= scropG || Vv <= scropL) {
          distance = 0;
        } else {
          double t1 = normalization(scropG) - Vv;
          double t2 = Vv - normalization(scropL);
          if (t1 >= t2) {
            distance = t2;
          } else {
            distance = t1;
          }
        }
      }

    } else {
      System.out.println("***** Warning: Unsolved situation 2! *****");
      return -1;
    }

    return distance;
  }

  public double variableCalculateConstraintDistance_v201606(ValueSet Vv, ValueSet BPc) {

    List<ValueSetConstraint> BPCList_v = new ArrayList<ValueSetConstraint>();
    List<ValueSetConstraint> BPCList_bp = new ArrayList<ValueSetConstraint>();

    int l_v = Vv.getValueSet().size();
    int l_bp = BPc.getValueSet().size();

    for (int i = 0; i < l_v; i++) {
      String[] srcs = Vv.getValueSet().get(i).split(" ");
      ValueSetConstraint temp = new ValueSetConstraint();
      temp.varLeft = srcs[0];
      temp.operator = srcs[1];
      temp.varRight = srcs[2];
      BPCList_v.add(temp);
    }
    for (int i = 0; i < l_bp; i++) {
      String[] srcs = BPc.getValueSet().get(i).split(" ");
      ValueSetConstraint temp = new ValueSetConstraint();
      temp.varLeft = srcs[0];
      temp.operator = srcs[1];
      temp.varRight = srcs[2];
      BPCList_bp.add(temp);
    }

    int numOfLessThan_v = 0;
    int numOfGreaterThan_v = 0;
    int numOfEqualTo_v = 0;
    int numOfLessThan_bp = 0;
    int numOfGreaterThan_bp = 0;
    int numOfEqualTo_bp = 0;

    for (int i = 0; i < l_v; i++) {
      if (BPCList_v.get(i).operator.equals("==")) {
        numOfEqualTo_v++;
      } else if (BPCList_v.get(i).operator.equals("<=") || BPCList_v.get(i).operator.equals("<")) {
        numOfLessThan_v++;
      } else if (BPCList_v.get(i).operator.equals(">=") || BPCList_v.get(i).operator.equals(">")) {
        numOfGreaterThan_v++;
      } else {
        System.out.println("***** Error: Wrong operator1! *****");
        return -1;
      }
    }
    for (int i = 0; i < l_bp; i++) {
      if (BPCList_bp.get(i).operator.equals("==")) {
        numOfEqualTo_bp++;
      } else if (BPCList_bp.get(i).operator.equals("<=")
          || BPCList_bp.get(i).operator.equals("<")) {
        numOfLessThan_bp++;
      } else if (BPCList_bp.get(i).operator.equals(">=")
          || BPCList_bp.get(i).operator.equals(">")) {
        numOfGreaterThan_bp++;
      } else {
        System.out.println("***** Error: Wrong operator2! *****");
        return -1;
      }
    }

    if ((numOfLessThan_v >= 2)
        || (numOfGreaterThan_v >= 2)
        || (numOfLessThan_bp >= 2)
        || (numOfGreaterThan_bp >= 2)) {
      System.out.println("***** Warning: Unsolved situation 3! *****");
      return -1;
    }
    if (l_v != l_bp) {

      return 1;
    }

    double distance = 0;

    if (numOfLessThan_v == numOfLessThan_bp && numOfGreaterThan_v == numOfGreaterThan_bp) {
      if (numOfLessThan_v == 0 && numOfGreaterThan_v == 0) {
        if (numOfEqualTo_v == 1 && numOfEqualTo_bp == 1) {
          if (Double.parseDouble(BPCList_v.get(0).varRight)
              == Double.parseDouble(BPCList_bp.get(0).varRight)) {
            distance = 0;
          } else {
            distance = 1;
          }
        } else {
          System.out.println("***** Warning: Unsolved situation 5! *****");
          return -1;
        }
      } else if (numOfLessThan_v == 1 && numOfGreaterThan_v == 0) {
        double scrop_v = 0;
        double scrop_bp = 0;
        for (int i = 0; i < l_v; i++) {
          if (BPCList_v.get(i).operator.equals("<") || BPCList_v.get(i).operator.equals("<=")) {
            scrop_v = Double.parseDouble(BPCList_v.get(i).varRight);
          }
        }
        for (int i = 0; i < l_bp; i++) {
          if (BPCList_bp.get(i).operator.equals("<") || BPCList_bp.get(i).operator.equals("<=")) {
            scrop_bp = Double.parseDouble(BPCList_bp.get(i).varRight);
          }
        }
        if (scrop_v == scrop_bp) {
          distance = 0;
        } else {
          distance = 1;
        }
      } else if (numOfLessThan_v == 0 && numOfGreaterThan_v == 1) {
        double scrop_v = 0;
        double scrop_bp = 0;
        for (int i = 0; i < l_v; i++) {
          if (BPCList_v.get(i).operator.equals(">") || BPCList_v.get(i).operator.equals(">=")) {
            scrop_v = Double.parseDouble(BPCList_v.get(i).varRight);
          }
        }
        for (int i = 0; i < l_bp; i++) {
          if (BPCList_bp.get(i).operator.equals(">") || BPCList_bp.get(i).operator.equals(">=")) {
            scrop_bp = Double.parseDouble(BPCList_bp.get(i).varRight);
          }
        }
        if (scrop_v == scrop_bp) {
          distance = 0;
        } else {
          distance = 1;
        }
      } else if (numOfLessThan_v == 1 && numOfGreaterThan_v == 1) {
        double scropG_v = 0;
        double scropL_v = 0;
        double scropG_bp = 0;
        double scropL_bp = 0;
        for (int i = 0; i < l_v; i++) {
          if (BPCList_v.get(i).operator.equals("<") || BPCList_v.get(i).operator.equals("<=")) {
            scropL_v = Double.parseDouble(BPCList_v.get(i).varRight);
          }
          if (BPCList_v.get(i).operator.equals(">") || BPCList_v.get(i).operator.equals(">=")) {
            scropG_v = Double.parseDouble(BPCList_v.get(i).varRight);
          }
        }
        for (int i = 0; i < l_bp; i++) {
          if (BPCList_bp.get(i).operator.equals("<") || BPCList_bp.get(i).operator.equals("<=")) {
            scropL_bp = Double.parseDouble(BPCList_bp.get(i).varRight);
          }
          if (BPCList_bp.get(i).operator.equals(">") || BPCList_bp.get(i).operator.equals(">=")) {
            scropG_bp = Double.parseDouble(BPCList_bp.get(i).varRight);
          }
        }
        if (scropL_v == scropL_bp && scropG_v == scropG_bp) {
          distance = 0;
        } else {
          distance = 1;
        }
      } else {
        System.out.println("***** Warning: Unsolved situation 6! *****");
        return -1;
      }

    } else {
      System.out.println("***** Warning: Unsolved situation 7! *****");
      return -1;
    }

    return distance;
  }

  class ValueSetConstraint {
    String varLeft;
    String operator;
    String varRight;
  }
}
