package org.avmframework.examples;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AlternatingVariableMethod;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.testoptimization.behaviourpair.GenerationObject;
import org.avmframework.examples.testoptimization.behaviourpair.Solution;
import org.avmframework.examples.testoptimization.behaviourpair.State;
import org.avmframework.examples.testoptimization.behaviourpair.StateMachine;
import org.avmframework.examples.testoptimization.behaviourpair.Transition;
import org.avmframework.examples.testoptimization.behaviourpair.ValueSet;
import org.avmframework.examples.util.ArgsParser;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* This example shows generation problem termed as "test case generation".
 * Test cases in this context are abstract based on a simplified standard UML
 * state machine diagram.
 * Test cases here represent behaviors (named as BehaviourPair in the program)
 * in the state machine, and these behaviors haven't haven't been validated.
 * To generate new test cases, we need the existing test cases generated
 * previously (read from file 03BehaviourPairs-1.0.txt), and the already known
 * behaviors (as the transitions with network environment conditions stored in
 * state machine, and state machine is read from 02StateMachine-1.0.txt).
 * Test cases or BehaviourPairs are encoded as Solutions, and a Solution dosen't
 * have actually difference with Vector defined by AVMf,
 * So, we transform Solution to Vector, and implement AVMf (except the function
 * readBehaviourPairsFromFile).
 *
 * As mentioned before, test cases here represent possible behaviors used to
 * explore unknown system behaviors.
 * Test case generation involves two objectives:
 *  1. maximize the similarity between the test cases and the known behaviors
 *  (transitions with network conditions in State Machine);
 *  2. maximize the diversity among the test cases representing generated
 *  possible system behaviors.
 */

public class TestCaseGeneration {

  // HOW TO RUN:
  //
  // java class org.avmframework.examples.AllZeros [search]
  //   where:
  //     - [search] is an optional parameter denoting which search to use
  //       (e.g., "IteratedPatternSearch", "GeometricSearch" or "LatticeSearch")

  // CHANGE THE FOLLOWING CONSTANTS TO EXPLORE THEIR EFFECT ON THE SEARCH:

  // - search constants
  static final String SEARCH_NAME = "IteratedPatternSearch"; // can also be set at the command line
  static final int MAX_EVALUATIONS = 200000;

  public static void main(String[] args) {

    // Problem Define
    GenerationObject problem = new GenerationObject();
    // Set State Machine
    StateMachine stateMachine = readStateMachineFromFile(
        "src/main/java/org/avmframework/examples/testoptimization/"
        + "behaviourpair/02StateMachine-1.0.txt");
    problem.setexistingstatemachine(stateMachine);
    // Set Constraints
    problem.setConstraints();
    // Initial Set of generated possible behaviors
    problem.initialSetOfExistingTestCases();
    List<Solution> list = readBehaviourPairsFromFile(
        "src/main/java/org/avmframework/examples/testoptimization/"
        + "behaviourpair/03BehaviourPairs-1.0.txt", stateMachine);
    problem.setSolutionsOfExistingTestCases(list);

    // set up the termination policy
    TerminationPolicy terminationPolicy =
        TerminationPolicy.createMaxEvaluationsTerminationPolicy(MAX_EVALUATIONS);

    // set up random initialization of vectors
    RandomGenerator randomGenerator = new MersenneTwister();
    Initializer initializer = new RandomInitializer(randomGenerator);

    // instantiate local search from command line, using default (set by the constant SEARCH_NAME)
    // if none supplied
    LocalSearch localSearch =
        new ArgsParser(TestCaseGeneration.class, args).parseSearchParam(SEARCH_NAME);

    // set up the AlternatingVariableMethod
    AlternatingVariableMethod avm = new AlternatingVariableMethod(
        localSearch, terminationPolicy, initializer);

    Vector vector = problem.setUpVector(9);

    // perform the search
    Monitor monitor = avm.search(vector, problem);

    System.out.println("Fitness value: " + monitor.getBestObjVal());
    System.out.println("Running time: " + monitor.getRunningTime());

    Vector bv = monitor.getBestVector();
    System.out.print(
        "Source state: "
            + stateMachine.getAllStates().get(
                Integer.parseInt(bv.getVariable(0).toString())).getStateName()
            + "; Target State-activeacll: "
            + Integer.parseInt(bv.getVariable(1).toString())
            + "; Target State-videoquality: "
            + Integer.parseInt(bv.getVariable(2).toString())
            + "; User Operation: "
            + Integer.parseInt(bv.getVariable(3).toString())
            + "; PacketLoss :"
            + Double.parseDouble(bv.getVariable(4).toString())
            + "; PacketDelay :"
            + Double.parseDouble(bv.getVariable(5).toString())
            + "; PacketDuplication :"
            + Double.parseDouble(bv.getVariable(6).toString())
            + "; PacketCorruption :"
            + Double.parseDouble(bv.getVariable(7).toString()));
    System.out.println();
  }

  // Read generated test cases (BehaviourPairs) from file
  public static List<Solution> readBehaviourPairsFromFile(String fileName, StateMachine sm) {

    File file = new File(fileName);
    BufferedReader reader = null;

    // BehaviourPair bp = new BehaviourPair();
    List<Solution> existingBehaviourPair = new ArrayList<Solution>();
    Solution tempBehaviourPair = null;

    try {

      reader = new BufferedReader(new FileReader(file));
      String tempString = null;

      while ((tempString = reader.readLine()) != null) {

        System.out.println(tempString);

        if (tempString.startsWith("BP")) {
          // do nothing or read the NO. of BP
          tempBehaviourPair = new Solution();
        } else if (tempString.startsWith("  Source_State")) {
          System.out.println("===== Read One Behaviour Pair: read the source state =====");

          String[] srcSourceState = tempString.split(" \\{");
          String sourceStateName = srcSourceState[1].substring(0, srcSourceState[1].length() - 1);
          System.out.println("----- Source State Name: " + sourceStateName + " -----");
          tempBehaviourPair.addSolutionMember("SourceState", sm.getStateNumber(sourceStateName));
        } else if (tempString.startsWith("  Target_State")) {
          System.out.println("===== Read One Behaviour Pair: read the target state =====");

          String[] srcTargetState = tempString.split(" \\{\\}; ");
          // String targetStateName = srcTargetState[1].substring(0, srcTargetState[1].length() -
          // 1);
          // System.out.println("----- Target State Name: " + targetStateName + " -----");
          // tempBehaviourPair.addSolutionMember("", o);

          String[] srcTargetStateV = srcTargetState[1].split("; ");
          String[] srcActiveCall = srcTargetStateV[0].split(" \\{");
          String srcActiveCallCon = srcActiveCall[1].substring(0, srcActiveCall[1].length() - 1);
          String[] srcActiveCallV = srcActiveCallCon.split("== ");
          int activeCallV = Integer.parseInt(srcActiveCallV[1]);
          String[] srcVideoQuality = srcTargetStateV[1].split(" \\{");
          String srcVideoQualityCon = srcVideoQuality[1]
              .substring(0, srcVideoQuality[1].length() - 1);
          String[] srcVideoQualityV = srcVideoQualityCon.split("== ");
          int videoQualityV = Integer.parseInt(srcVideoQualityV[1]);

          tempBehaviourPair.addSolutionMember("activecall", activeCallV);
          tempBehaviourPair.addSolutionMember("videoquality", videoQualityV);

        } else if (tempString.startsWith("  trigger")) {
          System.out.println("===== Read One Behaviour Pair: read the trigger =====");

          String[] trigger = tempString.split(" \\{");
          String triggerName = trigger[1].substring(0, trigger[1].length() - 1);
          System.out.println("----- Trigger Name: " + triggerName + " -----");
          int userOperationBehaviourPair;
          if (triggerName.equals("null")) {
            userOperationBehaviourPair = 0;
          } else if (triggerName.equals("dial")) {
            userOperationBehaviourPair = 1;
          } else if (triggerName.equals("disconnect")) {
            userOperationBehaviourPair = 2;
          } else {
            userOperationBehaviourPair = -1;
            System.out.println("***** Warning: calculate triggers! *****");
          }
          tempBehaviourPair.addSolutionMember("UserOperation", userOperationBehaviourPair);
        } else if (tempString.startsWith("  PacketLoss")) {

          System.out.println("===== Read One Behaviour Pair: read the network environment =====");

          HashMap<String, ValueSet> guardCondition = new HashMap<String, ValueSet>();

          String tempStringNetworkEnvironment = tempString.substring(2);

          String[] srcNetworkEnvironment = tempStringNetworkEnvironment.split(", ");

          int integerNetworkEnvironmentVar = 0;

          for (; integerNetworkEnvironmentVar <= srcNetworkEnvironment.length - 1; ) {

            String[] networkEnvironmentVarWithConstraints =
                srcNetworkEnvironment[integerNetworkEnvironmentVar].split(" \\{");

            String networkEnvironmentVarConstraints =
                networkEnvironmentVarWithConstraints[1].substring(
                    0, networkEnvironmentVarWithConstraints[1].length() - 1);

            String[] srcConstraintValue =
                networkEnvironmentVarConstraints.split(" == ");

            tempBehaviourPair.addSolutionMember(
                srcConstraintValue[0], Double.parseDouble(srcConstraintValue[1]));

            System.out.println(
                "----- Guard Condition: "
                    + srcConstraintValue[0]
                    + ", "
                    + tempBehaviourPair.getsolution().get(srcConstraintValue[0])
                    + " -----");

            integerNetworkEnvironmentVar = integerNetworkEnvironmentVar + 1;
          }
          // Add guard condition to transition, then add transition to state machine.

          existingBehaviourPair.add(tempBehaviourPair);
          Solution.printSolution(tempBehaviourPair);

          tempBehaviourPair = null;

        } else {
          System.out.println("***** Error: Line Label Unknown! *****");
        }
      }

      System.out.println("BP List Length: " + existingBehaviourPair.size());

      reader.close();

    } catch (IOException exception0) {
      exception0.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException exception1) {
          // nothing to catch
        }
      }
    }
    return existingBehaviourPair;
  }

  // Read State Machine from file
  public static StateMachine readStateMachineFromFile(String fileName) {

    File file = new File(fileName);
    BufferedReader reader = null;

    StateMachine stateMachine = new StateMachine();
    Transition transition = null;
    State sourceState = null;
    State targetState = null;

    try {

      reader = new BufferedReader(new FileReader(file));
      String tempString = null;

      boolean inTransition = false;
      int initialLabel = 0; // Used to tag the start state

      while ((tempString = reader.readLine()) != null) {

        initialLabel++;

        System.out.println(tempString);

        if (!inTransition) { // Not in One transition.

          String[] srcs = tempString.split(" ");

          if (srcs[0].equals("State")) {
            System.out.println("===== Read One State =====");

            if (!srcs[1].startsWith("{")) {
              System.out.println("***** Error: The format of State Machine is not correct! *****");
              return null;
            }

            HashMap<String, ValueSet> valueSet = new HashMap<String, ValueSet>();

            srcs = null;
            srcs = tempString.split("; ");
            if (srcs.length != 3) {
              System.out.println(
                  "***** Error: Something wrong with format of State and its variables! *****");
              return null;
            }

            int integerVar = 1;

            for (; integerVar <= srcs.length - 1; ) {

              String[] varWithConstraints = srcs[integerVar].split(" \\{");

              String varConstraints =
                  varWithConstraints[1].substring(0, varWithConstraints[1].length() - 1);
              System.out.println("Constraint:" + varConstraints);
              ValueSet valueSetTemp = new ValueSet();
              String[] constraintsOfOneVar = varConstraints.split(", ");
              for (int integerConstraint = 0;
                  integerConstraint < constraintsOfOneVar.length;
                  integerConstraint++) {
                valueSetTemp.addConstriantsForValueSet(constraintsOfOneVar[integerConstraint]);
              }

              valueSet.put(varWithConstraints[0], valueSetTemp);

              integerVar = integerVar + 1;
            }

            String stateName = srcs[1].substring(1, srcs[1].length() - 2);
            State tempState = new State(stateName, valueSet);

            if (initialLabel == 1) {
              System.out.println("----- Add start state. -----");
              stateMachine.setStartState(tempState);
              stateMachine.addOneState(tempState);
            } else {
              System.out.println("----- Add one state. -----");
              stateMachine.addOneState(tempState);
            }

          } else if (srcs[0].equals("Transition")) {
            System.out.println("===== Read One Transition: begin to read one transition =====");
            inTransition = true;
          } else {
            System.out.println("***** Error: Line Label Unknown! *****");
          }

        } else {
          // In one transition.

          if (tempString.startsWith("  Source_State")) {
            System.out.println("===== Read One Transition: read the source state =====");

            String[] srcSourceState = tempString.split(" \\{");
            String sourceStateName =
                srcSourceState[1].substring(0, srcSourceState[1].length() - 1);
            System.out.println("----- Source State Name: " + sourceStateName + " -----");
            sourceState = stateMachine.getState(sourceStateName);

          } else if (tempString.startsWith("  Target_State")) {
            System.out.println("===== Read One Transition: read the target state =====");

            String[] srcTargetState = tempString.split(" \\{");
            String targetStateName =
                srcTargetState[1].substring(0, srcTargetState[1].length() - 1);
            System.out.println("----- Target State Name: " + targetStateName + " -----");
            targetState = stateMachine.getState(targetStateName);

            transition = new Transition(sourceState, targetState);
            sourceState.addOutTransition(transition);
            targetState.addInTransition(transition);
          } else if (tempString.startsWith("  trigger")) {
            System.out.println("===== Read One Transition: read the trigger =====");

            String[] srcTrigger = tempString.split(" \\{");
            String trigger = srcTrigger[1].substring(0, srcTrigger[1].length() - 1);
            System.out.println("----- Trigger Name: " + trigger + " -----");

            transition.addTriggers(trigger);
          } else if (tempString.startsWith("  PacketLoss")) {
            System.out.println("===== Read One Transition: read the network environment =====");

            HashMap<String, ValueSet> guardCondition = new HashMap<String, ValueSet>();

            String tempStringNetworkEnvironment = tempString.substring(2);

            String[] srcNetworkEnvironment = tempStringNetworkEnvironment.split(", ");

            int integerNetworkEnvironmentVar = 0;

            for (; integerNetworkEnvironmentVar <= srcNetworkEnvironment.length - 1; ) {

              String[] networkEnvironmentVarWithConstraints =
                  srcNetworkEnvironment[integerNetworkEnvironmentVar].split(" \\{");

              String networkEnvironmentVarConstraints =
                  networkEnvironmentVarWithConstraints[1].substring(
                      0, networkEnvironmentVarWithConstraints[1].length() - 1);

              ValueSet valueSetTemp = new ValueSet();
              String[] constraintsOfOneNetworkEnvironmentVar =
                  networkEnvironmentVarConstraints.split(", ");

              for (int integerConstraint = 0;
                  integerConstraint < constraintsOfOneNetworkEnvironmentVar.length;
                  integerConstraint++) {
                valueSetTemp.addConstriantsForValueSet(
                    constraintsOfOneNetworkEnvironmentVar[integerConstraint]);
              }

              guardCondition.put(networkEnvironmentVarWithConstraints[0], valueSetTemp);
              System.out.println(
                  "----- Guard Condition: "
                      + networkEnvironmentVarWithConstraints[0]
                      + ", "
                      + valueSetTemp.getValueSet().get(0)
                      + " -----");

              transition.setConditions(guardCondition);

              integerNetworkEnvironmentVar = integerNetworkEnvironmentVar + 1;
            }
            // Add guard condition to transition, then add transition to state machine.

            // System.out.println("Corrup: " + NECorrupValue + ", len: " + l_NE_C);
            // t.getNetworkEnvironment().setPacketLoss(Double.parseDouble(NECorrupValue));

            stateMachine.addOneTransition(transition);

            inTransition = false;
            sourceState = null;
            targetState = null;
            transition = null;
          } else {
            System.out.println("***** Error: Line Label Unknown! *****");
          }
        }
      }
      reader.close();

    } catch (IOException exception0) {
      exception0.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException exception1) {
          // nothing to catch
        }
      }
    }

    return stateMachine;
  }
}
