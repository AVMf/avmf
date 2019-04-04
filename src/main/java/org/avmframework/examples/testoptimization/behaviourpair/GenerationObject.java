package org.avmframework.examples.testoptimization.behaviourpair;

import org.avmframework.Vector;
import org.avmframework.objective.NumericObjectiveValue;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.FixedPointVariable;
import org.avmframework.variable.IntegerVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenerationObject extends ObjectiveFunction {

  private ConstraintList constraints; // Its set method uses BPair
  private StateMachine existingstatemachine;
  private List<BehaviourPair> listOfExistingBehaviourPairs;
  private List<BehaviourPair> listOfExistingTestCases;

  private List<Solution> solutionsOfExistingBehaviourPairs;
  private List<Solution> solutionsOfExistingTestCases;

  public List<BehaviourPair> getExistingTestCases() {
    return this.listOfExistingTestCases;
  }

  public void setExistingTestCases(List<BehaviourPair> existingTestCases) {
    this.listOfExistingTestCases = existingTestCases;
  }

  public void addOneTestCase2ExistingTestCases(BehaviourPair tc) {
    this.listOfExistingTestCases.add(tc);
  }

  public void setSolutionsOfExistingTestCases(List<Solution> existingTestCases) {
    this.solutionsOfExistingTestCases = existingTestCases;
  }

  public List<Solution> getSolutionsOfExistingTestCases() {
    return this.solutionsOfExistingTestCases;
  }

  public List<BehaviourPair> getListofExistingBehaviourPairs() {
    return this.listOfExistingBehaviourPairs;
  }

  /** Construct Methods. */
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
    this.listOfExistingBehaviourPairs = BehaviourPair.stateMachineParser(this.existingstatemachine);
    System.out.println(
        "========== length listOfExistingBehaviourPairs: "
            + this.listOfExistingBehaviourPairs.size());
    // this.setConstraints();
  }

  public StateMachine getexistingstatemachine() {
    return this.existingstatemachine;
  }

  public void emptySetOfSolutionsOfExistingBehaviourPairs() {
    this.solutionsOfExistingBehaviourPairs = null;
  }

  public void fillSetofSolutionsOfExistingBehaviourPairs(
      List<BehaviourPair> existingBehaviourPairs) {
    this.solutionsOfExistingBehaviourPairs = new ArrayList<Solution>();
    int len = existingBehaviourPairs.size();
    for (int i = 0; i < len; i++) {
      Solution temp = transformBPair2Solution(existingBehaviourPairs.get(i));
      this.solutionsOfExistingBehaviourPairs.add(temp);
    }
  }

  public void initialSetOfExistingTestCases() {
    // EmptySetOfExistingTestCases();
    this.solutionsOfExistingTestCases = new ArrayList<Solution>();
  }

  // Used when reading BehaviourPairs from files.
  public Solution transformBPair2Solution(BehaviourPair bpair) {
    Solution sol = new Solution();

    // Source State
    State sourceState = bpair.getSourceState();
    List<State> allexistingstates = this.existingstatemachine.getAllStates();
    int len = allexistingstates.size();
    for (int i = 0; i < len; i++) {
      State currentState = allexistingstates.get(i);
      if (currentState.getStateName().equals(sourceState.getStateName())) {
        sol.addSolutionMember("SourceState", i);
        break;
      }
    }

    // Target State
    /** ### Need to consider whether the type is the correct type! ###. */
    State targetState = bpair.getTargetState();
    sol.addSolutionMember("activecall", targetState.getSystemVariables().get("activecall"));
    sol.addSolutionMember("videoquality", targetState.getSystemVariables().get("videoquality"));

    return sol;
  }

  /*
   * When generating test cases(BPs here, actually), traverse every constraint
   * and pick up one value.
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
        new IntegerVariable(0, 0, this.existingstatemachine.getAllStates().size() - 1));
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
        "SourceState", 0, this.existingstatemachine.getAllStates().size() - 1, 0);

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

  public ObjectiveValue computeObjectiveValue(Vector vector) {

    // first, the similarity of the current solution with the existing behavior pairs which are from
    // state machine.
    double resultSimilarity = 0;
    List<Transition> listTransition = this.getexistingstatemachine().getAllTransitions();
    int len1 = listTransition.size();
    int clen1 = len1;

    for (int i = 0; i < len1; i++) {
      double tempp = similarityBetweenVectorAndTransition(vector, listTransition.get(i));
      if (tempp == 0) {
        clen1--;
      } else {
        resultSimilarity = resultSimilarity + tempp;
      }
    }
    resultSimilarity = resultSimilarity / clen1;

    // second, diversity with the existing solutions
    // Using the solutions of solutionsOfExistingTestCases directly.
    int len2 = this.solutionsOfExistingTestCases.size();
    double resultDiversity = 0;
    double result = 0;

    for (int i = 0; i < len2; i++) {
      resultDiversity =
          resultDiversity + diversityOfTwoVectors(vector, this.solutionsOfExistingTestCases.get(i));
    }

    resultDiversity = resultDiversity / len2;

    result =
        maxMinNormalization(resultDiversity, 0, 3)
            + maxMinNormalization(resultSimilarity, 0, 3);
    result = 1 - result / 2;

    return NumericObjectiveValue.lowerIsBetterObjectiveValue(result, 0);
  }

  public static double normalization(double num) {
    double nor = num / (num + 1);
    return nor;
  }

  public static double maxMinNormalization(double num, double min, double max) {
    double nor = (num - min) / (max - min);
    return nor;
  }

  /** ValueSet. first version */
  public double variableConstraintConstraintDistance(ValueSet vc, ValueSet bpc) {

    int lengthValuesc = vc.getValueSet().size();
    int lbpc = bpc.getValueSet().size();
    double distance = 0;

    if (lengthValuesc == 1 && lbpc == 1) {

      String con0 = vc.getValueSet().get(0);
      String con1 = bpc.getValueSet().get(0);

      String[] srcs0 = con0.split(" ");
      String[] srcs1 = con1.split(" ");

      if (srcs0[1].equals("==") && srcs1[1].equals("==")) {
        int temp0 = Integer.parseInt(srcs0[2]);
        int temp1 = Integer.parseInt(srcs1[2]);

        distance = normalization((double) temp0) - normalization((double) temp1);

      } else {
        System.out.println("Warning: Illegal!");
      }

    } else if (lengthValuesc == 1 && lbpc == 2) {

      String con00 = vc.getValueSet().get(0);
      String con10 = bpc.getValueSet().get(0);
      String con11 = bpc.getValueSet().get(1);

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
        System.out.println("Warning: Illegal!");
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
        System.out.println("Warning: Illegal!");
      }

      distance = normalization((double) temp00) - normalization(mid1);

    } else if (lengthValuesc == 2 && lbpc == 1) {

      String con00 = vc.getValueSet().get(0);
      String con01 = vc.getValueSet().get(1);
      String con11 = bpc.getValueSet().get(0);

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
        System.out.println("Warning: Illegal!");
      }

      if (srcs11[1].equals("==")) {
        temp11 = Integer.parseInt(srcs11[2]);
      } else {
        System.out.println("Warning: Illegal!");
      }

      distance = normalization(mid0) - normalization((double) temp11);

    } else if (lengthValuesc == 2 && lbpc == 2) {

      String con00 = vc.getValueSet().get(0);
      String con01 = vc.getValueSet().get(1);
      String con10 = bpc.getValueSet().get(0);
      String con11 = bpc.getValueSet().get(1);

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
        System.out.println("Warning: Illegal!");
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
        System.out.println("Warning: Illegal!");
      }

      distance = normalization(mid1) - normalization(mid0);

    } else {
      System.out.println("Warning: Illegal constraints!");
    }

    return distance;
  }

  public double variableValueConstraintDistance(int vv, ValueSet bpc) {

    int length = bpc.getValueSet().size();

    double distance = 0;

    if (length == 1) {
      String con = bpc.getValueSet().get(0);
      String[] srcs = con.split(" ");
      if (srcs[1].equals("==")) {

        int tempv1 = Integer.parseInt(srcs[2]);
        // System.out.println("srcs[2]: " + srcs[2]);

        distance = normalization((double) vv) - normalization((double) tempv1);

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

    } else if (length == 2) {

      // Only considering part of situation

      String con0 = bpc.getValueSet().get(0);
      String con1 = bpc.getValueSet().get(1);

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
      distance = normalization((double) vv) - normalization(mid);

    } else {
      System.out.println("Warning: illegal ValueSet!");
    }

    return distance;
  }

  /** 201606 ValueSet the second version. */
  public double diversityOfTwoVectors(Vector v1, Solution v2) { // Distance of two solutions

    // map all variables into the interval (0, 1), then use measures, for instance, Euclidean
    // metric.

    // HashMap<String, Object> solution_v1 = v1.getsolution();
    HashMap<String, Object> solutionv2 = v2.getsolution();

    // v1
    // source state
    State sourceStatev1 =
        this.existingstatemachine
            .getAllStates()
            .get(Integer.parseInt(v1.getVariable(0).toString()));
    // source state v2
    State sourceStatev2 =
        this.existingstatemachine.getAllStates().get((int) solutionv2.get("SourceState"));
    double distanceSourceActiveCall =
        variableCalculateConstraintDistancev201606(
            sourceStatev1.getSystemVariablesValueSet().get("activecall"),
            sourceStatev2.getSystemVariablesValueSet().get("activecall"));
    double distanceSourceVideoQuality =
        variableCalculateConstraintDistancev201606(
            sourceStatev1.getSystemVariablesValueSet().get("videoquality"),
            sourceStatev2.getSystemVariablesValueSet().get("videoquality"));

    // target state
    int activeCallTargetStatev1 = Integer.parseInt(v1.getVariable(1).toString());
    int videoQualityTargetStatev1 = Integer.parseInt(v1.getVariable(2).toString());
    double norActiveCallTargetStatev1 = normalization((double) activeCallTargetStatev1);
    double norVideoQualityTargetStatev1 = normalization((double) videoQualityTargetStatev1);
    // user operations
    int userOperationv1 = Integer.parseInt(v1.getVariable(3).toString());
    double norUserOperationv1 = normalization((double) userOperationv1);

    // network environment
    double packetLossv1 = Double.parseDouble(v1.getVariable(4).toString());
    double packetDelayv1 = Double.parseDouble(v1.getVariable(5).toString());
    double packetDuplicationv1 = Double.parseDouble(v1.getVariable(6).toString());
    double packetCorruptionv1 = Double.parseDouble(v1.getVariable(7).toString());
    double norPacketLossv1 = normalization(packetLossv1);
    double norPacketDelayv1 = normalization(packetDelayv1);
    double norPacketDuplicationv1 = normalization(packetDuplicationv1);
    double norPacketCorruptionv1 = normalization(packetCorruptionv1);

    // v2

    // target state
    int activeCallTargetStatev2 = (int) solutionv2.get("activecall");
    int videoQualityTargetStatev2 = (int) solutionv2.get("videoquality");
    double norActiveCallTargetStatev2 = normalization((double) activeCallTargetStatev2);
    double norVideoQualityTargetStatev2 = normalization((double) videoQualityTargetStatev2);

    // user operations
    int userOperationv2 = (int) solutionv2.get("UserOperation");
    double norUserOperationv2 = normalization((double) userOperationv2);

    // network environment
    double packetLossv2 = (double) solutionv2.get("PacketLoss");
    double packetDelayv2 = (double) solutionv2.get("PacketDelay");
    double packetDuplicationv2 = (double) solutionv2.get("PacketDuplication");
    double packetCorruptionv2 = (double) solutionv2.get("PacketCorruption");
    double norPacketLossv2 = normalization(packetLossv2);
    double norPacketDelayv2 = normalization(packetDelayv2);
    double norPacketDuplicationv2 = normalization(packetDuplicationv2);
    double norPacketCorruptionv2 = normalization(packetCorruptionv2);

    // Eculidean Metric
    double result = 0;
    result =
        Math.pow(normalization(distanceSourceActiveCall), 2)
            + Math.pow(normalization(distanceSourceVideoQuality), 2)
            + Math.pow((norActiveCallTargetStatev1 - norActiveCallTargetStatev2), 2)
            + Math.pow((norVideoQualityTargetStatev1 - norVideoQualityTargetStatev2), 2)
            + Math.pow((norUserOperationv1 - norUserOperationv2), 2)
            + Math.pow((norPacketLossv1 - norPacketLossv2), 2)
            + Math.pow((norPacketDelayv1 - norPacketDelayv2), 2)
            + Math.pow((norPacketDuplicationv1 - norPacketDuplicationv2), 2)
            + Math.pow((norPacketCorruptionv1 - norPacketCorruptionv2), 2);
    result = Math.sqrt(result);

    return result;
  }

  public double similarityBetweenVectorAndTransition(
      Vector v1, Transition t1) { // 9 variables in space

    double result = 3 - distanceBetweenVectorAndTransition(v1, t1);

    return result;
  }

  public double distanceBetweenVectorAndTransition(
      Vector v1, Transition t1) { // Distance of two solutions

    // map all variables into the interval (0, 1), then use measures, for instance, Euclidean
    // metric.

    // Distances of the source states
    State sourceStatev1 =
        this.existingstatemachine
            .getAllStates()
            .get(Integer.parseInt(v1.getVariable(0).toString()));
    State sourceStateT = t1.getSourceState();
    double distanceSourceActiveCall =
        variableCalculateConstraintDistancev201606(
            sourceStatev1.getSystemVariablesValueSet().get("activecall"),
            t1.getSourceState().getSystemVariablesValueSet().get("activecall"));
    double distanceSourceVideoQuality =
        variableCalculateConstraintDistancev201606(
            sourceStatev1.getSystemVariablesValueSet().get("videoquality"),
            t1.getSourceState().getSystemVariablesValueSet().get("videoquality"));

    double norDistanceSourceActiveCall = normalization(distanceSourceActiveCall);
    double norDistanceSourceVideoQuality = normalization(distanceSourceVideoQuality);

    // Distances of the target states
    int activeCallTargetStatev1 = Integer.parseInt(v1.getVariable(1).toString());
    int videoQualityTargetStatev1 = Integer.parseInt(v1.getVariable(2).toString());
    ValueSet activeCallTargetStateT =
        t1.getTargetState().getSystemVariablesValueSet().get("activecall");
    ValueSet videoQualityTargetStateT =
        t1.getTargetState().getSystemVariablesValueSet().get("videoquality");

    double distanceTargetActiveCall =
        variableCalculateConstraintDistancev201606(
            activeCallTargetStatev1, activeCallTargetStateT);
    double distanceTargetVideoQuality =
        variableCalculateConstraintDistancev201606(
            videoQualityTargetStatev1, videoQualityTargetStateT);

    double norDistanceTargetActiveCall = normalization(distanceTargetActiveCall);
    double norDistanceTargetVideoQuality = normalization(distanceTargetVideoQuality);

    // Distances of the triggers
    int userOperationv1 = Integer.parseInt(v1.getVariable(3).toString());
    double norUserOperationv1 = normalization((double) userOperationv1);
    int userOperationBehaviourPair;
    String userOperationStringBehaviourPair = t1.getTriggers().get(0);
    if (userOperationStringBehaviourPair.equals("null")) {
      userOperationBehaviourPair = 0;
    } else if (userOperationStringBehaviourPair.equals("dial")) {
      userOperationBehaviourPair = 1;
    } else if (userOperationStringBehaviourPair.equals("disconnect")) {
      userOperationBehaviourPair = 2;
    } else {
      userOperationBehaviourPair = -1;
      System.out.println("***** Warning: calculate triggers! *****");
    }
    double norUserOperationBehaviourPair = normalization((double) userOperationBehaviourPair);
    double distanceTrigger = norUserOperationBehaviourPair - norUserOperationv1;

    // Distances of Network Environment
    double packetLossv1 = Double.parseDouble(v1.getVariable(4).toString());
    double packetDelayv1 = Double.parseDouble(v1.getVariable(5).toString());
    double packetDuplicationv1 = Double.parseDouble(v1.getVariable(6).toString());
    double packetCorruptionv1 = Double.parseDouble(v1.getVariable(7).toString());

    ValueSet packetLossT = t1.getConditions().get("PacketLoss");
    ValueSet packetDelayT = t1.getConditions().get("PacketDelay");
    ValueSet packetDuplicationT = t1.getConditions().get("PacketDuplication");
    ValueSet packetCorruptionT = t1.getConditions().get("PacketCorruption");
    double distancePacketLoss =
        variableCalculateConstraintDistancev201606(packetLossv1, packetLossT);
    double distancePacketDelay =
        variableCalculateConstraintDistancev201606(packetDelayv1, packetDelayT);
    double distancePacketDuplication =
        variableCalculateConstraintDistancev201606(packetDuplicationv1, packetDuplicationT);
    double distancePacketCorruption =
        variableCalculateConstraintDistancev201606(packetCorruptionv1, packetCorruptionT);

    double norDistancePacketLoss = normalization(distancePacketLoss);
    double norDistancePacketDelay = normalization(distancePacketDelay);
    double norDistancePacketDuplication = normalization(distancePacketDuplication);
    double norDistancePacketCorruption = normalization(distancePacketCorruption);

    double result = 0;
    result =
        Math.pow(norDistanceSourceActiveCall, 2)
            + Math.pow(norDistanceSourceVideoQuality, 2)
            + Math.pow(norDistanceTargetActiveCall, 2)
            + Math.pow(norDistanceTargetVideoQuality, 2)
            + Math.pow(distanceTrigger, 2)
            + Math.pow(norDistancePacketLoss, 2)
            + Math.pow(norDistancePacketDelay, 2)
            + Math.pow(norDistancePacketDuplication, 2)
            + Math.pow(norDistancePacketCorruption, 2);
    result = Math.sqrt(result);

    if (packetLossv1 < 10
        || packetDelayv1 < 1
        || packetDuplicationv1 < 1
        || packetCorruptionv1 < 1) {
      return 3.0;
    }
    if (distancePacketLoss < 1
        || distancePacketDelay < 1
        || distancePacketDuplication < 1
        || distancePacketCorruption < 1) {
      return 3.0;
    }
    if (distancePacketLoss
            + distancePacketDelay
            + distancePacketDuplication
            + distancePacketCorruption
        < 20) {
      return 3.0;
    }

    return result;
  }

  public double variableCalculateConstraintDistancev201606(double vv, ValueSet bpc) {

    List<ValueSetConstraint> bpcList = new ArrayList<ValueSetConstraint>();
    if (bpc == null) {
      System.out.println("bpc == null");
    }
    if (bpc.getValueSet().isEmpty()) {
      System.out.println("bpc.getValueSet().isEmpty()");
    }
    int length = bpc.getValueSet().size();

    for (int i = 0; i < length; i++) {
      String[] srcs = bpc.getValueSet().get(i).split(" ");
      ValueSetConstraint temp = new ValueSetConstraint();
      temp.varLeft = srcs[0];
      temp.operator = srcs[1];
      temp.varRight = srcs[2];
      bpcList.add(temp);

      // System.out.println("Operator:" + temp.operator);

    }

    int numOfLessThan = 0;
    int numOfGreaterThan = 0;
    int numOfEqualTo = 0;

    for (int i = 0; i < length; i++) {

      if (bpcList.get(i).operator.equals("==")) {
        // System.out.println("==");
        numOfEqualTo++;
      } else if (bpcList.get(i).operator.equals("<=") || bpcList.get(i).operator.equals("<")) {
        numOfLessThan++;
        // System.out.println("Less");
      } else if (bpcList.get(i).operator.equals(">") || bpcList.get(i).operator.equals(">=")) {
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
    vv = normalization(vv);

    if (numOfLessThan == 0 && numOfGreaterThan == 0) {
      if (numOfEqualTo == 1) {
        distance = vv - normalization(Double.parseDouble(bpcList.get(0).varRight));
        if (distance < 0) {
          distance = distance * (-1);
        }
      } else {
        System.out.println("***** Warning: Some Impossible situation! *****");
        return -1;
      }
    } else if (numOfLessThan == 0 && numOfGreaterThan == 1) {
      double scrop = 0;
      for (int i = 0; i < length; i++) {
        if (bpcList.get(i).operator.equals(">") || bpcList.get(i).operator.equals(">=")) {
          scrop = Double.parseDouble(bpcList.get(i).varRight);
        }
      }
      if (vv >= scrop) {
        distance = 0;
      } else {
        distance = normalization(scrop) - vv;
      }
    } else if (numOfLessThan == 1 && numOfGreaterThan == 0) {
      double scrop = 0;
      for (int i = 0; i < length; i++) {
        if (bpcList.get(i).operator.equals("<") || bpcList.get(i).operator.equals("<=")) {
          scrop = Double.parseDouble(bpcList.get(i).varRight);
        }
      }
      if (vv <= scrop) {
        distance = 0;
      } else {
        distance = vv - normalization(scrop);
      }
    } else if (numOfLessThan == 1 && numOfGreaterThan == 1) {
      double scropG = 0;
      double scropL = 0;
      for (int i = 0; i < length; i++) {
        if (bpcList.get(i).operator.equals(">") || bpcList.get(i).operator.equals(">=")) {
          scropG = Double.parseDouble(bpcList.get(i).varRight);
        }
        if (bpcList.get(i).operator.equals("<") || bpcList.get(i).operator.equals("<=")) {
          scropL = Double.parseDouble(bpcList.get(i).varRight);
        }
      }
      if (scropG <= scropL) {
        if (vv <= scropL && vv >= scropG) {
          distance = 0;
        } else if (vv <= scropL && vv <= scropG) {
          distance = normalization(scropG) - vv;
        } else if (vv >= scropL && vv >= scropG) {
          distance = vv - normalization(scropL);
        } else {
          System.out.println("***** Warning: Impossible situation! *****");
          return -1;
        }
      } else {
        if (vv >= scropG || vv <= scropL) {
          distance = 0;
        } else {
          double t1 = normalization(scropG) - vv;
          double t2 = vv - normalization(scropL);
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

  public double variableCalculateConstraintDistancev201606(ValueSet vv, ValueSet bpc) {

    List<ValueSetConstraint> bpcListV = new ArrayList<ValueSetConstraint>();
    List<ValueSetConstraint> bpcListBehaviourPair = new ArrayList<ValueSetConstraint>();

    int lengthValues = vv.getValueSet().size();
    int lbp = bpc.getValueSet().size();

    for (int i = 0; i < lengthValues; i++) {
      String[] srcs = vv.getValueSet().get(i).split(" ");
      ValueSetConstraint temp = new ValueSetConstraint();
      temp.varLeft = srcs[0];
      temp.operator = srcs[1];
      temp.varRight = srcs[2];
      bpcListV.add(temp);
    }
    for (int i = 0; i < lbp; i++) {
      String[] srcs = bpc.getValueSet().get(i).split(" ");
      ValueSetConstraint temp = new ValueSetConstraint();
      temp.varLeft = srcs[0];
      temp.operator = srcs[1];
      temp.varRight = srcs[2];
      bpcListBehaviourPair.add(temp);
    }

    int numOfLessThanV = 0;
    int numOfGreaterThanV = 0;
    int numOfEqualToV = 0;
    int numOfLessThanBehaviourPair = 0;
    int numOfGreaterThanBehaviourPair = 0;
    int numOfEqualToBehaviourPair = 0;

    for (int i = 0; i < lengthValues; i++) {
      if (bpcListV.get(i).operator.equals("==")) {
        numOfEqualToV++;
      } else if (bpcListV.get(i).operator.equals("<=") || bpcListV.get(i).operator.equals("<")) {
        numOfLessThanV++;
      } else if (bpcListV.get(i).operator.equals(">=") || bpcListV.get(i).operator.equals(">")) {
        numOfGreaterThanV++;
      } else {
        System.out.println("***** Error: Wrong operator1! *****");
        return -1;
      }
    }
    for (int i = 0; i < lbp; i++) {
      if (bpcListBehaviourPair.get(i).operator.equals("==")) {
        numOfEqualToBehaviourPair++;
      } else if (bpcListBehaviourPair.get(i).operator.equals("<=")
          || bpcListBehaviourPair.get(i).operator.equals("<")) {
        numOfLessThanBehaviourPair++;
      } else if (bpcListBehaviourPair.get(i).operator.equals(">=")
          || bpcListBehaviourPair.get(i).operator.equals(">")) {
        numOfGreaterThanBehaviourPair++;
      } else {
        System.out.println("***** Error: Wrong operator2! *****");
        return -1;
      }
    }

    if ((numOfLessThanV >= 2)
        || (numOfGreaterThanV >= 2)
        || (numOfLessThanBehaviourPair >= 2)
        || (numOfGreaterThanBehaviourPair >= 2)) {
      System.out.println("***** Warning: Unsolved situation 3! *****");
      return -1;
    }
    if (lengthValues != lbp) {

      return 1;
    }

    double distance = 0;

    if (numOfLessThanV == numOfLessThanBehaviourPair && numOfGreaterThanV
        == numOfGreaterThanBehaviourPair) {
      if (numOfLessThanV == 0 && numOfGreaterThanV == 0) {
        if (numOfEqualToV == 1 && numOfEqualToBehaviourPair == 1) {
          if (Double.parseDouble(bpcListV.get(0).varRight)
              == Double.parseDouble(bpcListBehaviourPair.get(0).varRight)) {
            distance = 0;
          } else {
            distance = 1;
          }
        } else {
          System.out.println("***** Warning: Unsolved situation 5! *****");
          return -1;
        }
      } else if (numOfLessThanV == 1 && numOfGreaterThanV == 0) {
        double scropV = 0;
        double scropBehaviourPair = 0;
        for (int i = 0; i < lengthValues; i++) {
          if (bpcListV.get(i).operator.equals("<") || bpcListV.get(i).operator.equals("<=")) {
            scropV = Double.parseDouble(bpcListV.get(i).varRight);
          }
        }
        for (int i = 0; i < lbp; i++) {
          if (bpcListBehaviourPair.get(i).operator.equals("<")
              || bpcListBehaviourPair.get(i).operator.equals("<=")) {
            scropBehaviourPair = Double.parseDouble(bpcListBehaviourPair.get(i).varRight);
          }
        }
        if (scropV == scropBehaviourPair) {
          distance = 0;
        } else {
          distance = 1;
        }
      } else if (numOfLessThanV == 0 && numOfGreaterThanV == 1) {
        double scropV = 0;
        double scropBehaviourPair = 0;
        for (int i = 0; i < lengthValues; i++) {
          if (bpcListV.get(i).operator.equals(">") || bpcListV.get(i).operator.equals(">=")) {
            scropV = Double.parseDouble(bpcListV.get(i).varRight);
          }
        }
        for (int i = 0; i < lbp; i++) {
          if (bpcListBehaviourPair.get(i).operator.equals(">")
              || bpcListBehaviourPair.get(i).operator.equals(">=")) {
            scropBehaviourPair = Double.parseDouble(bpcListBehaviourPair.get(i).varRight);
          }
        }
        if (scropV == scropBehaviourPair) {
          distance = 0;
        } else {
          distance = 1;
        }
      } else if (numOfLessThanV == 1 && numOfGreaterThanV == 1) {
        double scropGreaterThanV = 0;
        double scropLessThanV = 0;
        double scropGreaterThanBehaviourPair = 0;
        double scropLessThanBehaviourPair = 0;
        for (int i = 0; i < lengthValues; i++) {
          if (bpcListV.get(i).operator.equals("<") || bpcListV.get(i).operator.equals("<=")) {
            scropLessThanV = Double.parseDouble(bpcListV.get(i).varRight);
          }
          if (bpcListV.get(i).operator.equals(">") || bpcListV.get(i).operator.equals(">=")) {
            scropGreaterThanV = Double.parseDouble(bpcListV.get(i).varRight);
          }
        }
        for (int i = 0; i < lbp; i++) {
          if (bpcListBehaviourPair.get(i).operator.equals("<")
              || bpcListBehaviourPair.get(i).operator.equals("<=")) {
            scropLessThanBehaviourPair = Double.parseDouble(bpcListBehaviourPair.get(i).varRight);
          }
          if (bpcListBehaviourPair.get(i).operator.equals(">")
              || bpcListBehaviourPair.get(i).operator.equals(">=")) {
            scropGreaterThanBehaviourPair
                = Double.parseDouble(bpcListBehaviourPair.get(i).varRight);
          }
        }
        if (scropLessThanV == scropLessThanBehaviourPair && scropGreaterThanV
            == scropGreaterThanBehaviourPair) {
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
