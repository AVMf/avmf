package org.avmframework.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.testoptimization.problem_BehaviourPairGen.*;
import org.avmframework.examples.util.*;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;

/* This example shows generation problem termed as "test case generation".
 * Test cases in this context are abstract based on a simplified standard UML state machine diagram.
 * Test cases here represent behaviors (named as BehaviourPair in the program) in the state machine, and these behaviors haven't haven't been validated.
 * To generate new test cases, we need the existing test cases generated previously (read from file 03BehaviourPairs-1.0.txt),
 * and the already known behaviors (as the transitions with network environment conditions stored in state machine, and state machine is read from 02StateMachine-1.0.txt).
 * Test cases or BehaviourPairs are encoded as Solutions, and a Solution dosen't have actually difference with Vector defined by AVMf,
 * So, we transform Solution to Vector, and implement AVMf (except the function ReadBehaviourPairsFromFile).
 *
 * As mentioned before, test cases here represent possible behaviors used to explore unknown system behaviors.
 * Test case generation involves two objectives:
 *  1. maximize the similarity between the test cases and the known behaviors (transitions with network conditions in State Machine);
 *  2. maximize the diversity among the test cases representing generated possible system behaviors.
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

  public static void main(String args[]) {

    // Problem Define
    GenerationObject p = new GenerationObject();
    // Set State Machine
    StateMachine s =
        ReadStateMachineFromFile(
            "src/main/java/org/avmframework/examples/testoptimization/problem_BehaviourPairGen/02StateMachine-1.0.txt");
    p.setexistingstatemachine(s);
    // Set Constraints
    p.setConstraints();
    // Initial Set of generated possible behaviors
    p.InitialSetOfExistingTestCases();
    List<Solution> l =
        ReadBehaviourPairsFromFile(
            "src/main/java/org/avmframework/examples/testoptimization/problem_BehaviourPairGen/03BehaviourPairs-1.0.txt",
            s);
    p.setSolutionsOfExistingTestCases(l);

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

    // set up the AVM
    AVM avm = new AVM(localSearch, terminationPolicy, initializer);

    Vector vector = p.setUpVector(9);

    // perform the search
    Monitor monitor = avm.search(vector, p);

    System.out.println("Fitness value: " + monitor.getBestObjVal());
    System.out.println("Running time: " + monitor.getRunningTime());

    Vector bv = monitor.getBestVector();
    System.out.print(
        "Source state: "
            + s.getallstates().get(Integer.parseInt(bv.getVariable(0).toString())).getStateName()
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
  public static List<Solution> ReadBehaviourPairsFromFile(String fileName, StateMachine sm) {

    File file = new File(fileName);
    BufferedReader reader = null;

    // BehaviourPair bp = new BehaviourPair();
    List<Solution> ExistingBP = new ArrayList<Solution>();
    Solution tempBP = null;

    try {

      reader = new BufferedReader(new FileReader(file));
      String tempString = null;

      while ((tempString = reader.readLine()) != null) {

        System.out.println(tempString);

        if (tempString.startsWith("BP")) {
          // do nothing or read the NO. of BP
          tempBP = new Solution();
        } else if (tempString.startsWith("  Source_State")) {
          System.out.println("===== Read One Behaviour Pair: read the source state =====");

          String[] src_SourceState = tempString.split(" \\{");
          String sourceStateName = src_SourceState[1].substring(0, src_SourceState[1].length() - 1);
          System.out.println("----- Source State Name: " + sourceStateName + " -----");
          tempBP.addsolutionmember("SourceState", sm.getStateNO(sourceStateName));
        } else if (tempString.startsWith("  Target_State")) {
          System.out.println("===== Read One Behaviour Pair: read the target state =====");

          String[] src_TargetState = tempString.split(" \\{\\}; ");
          // String targetStateName = src_TargetState[1].substring(0, src_TargetState[1].length() -
          // 1);
          // System.out.println("----- Target State Name: " + targetStateName + " -----");
          // tempBP.addsolutionmember("", o);

          String[] src_TS_v = src_TargetState[1].split("; ");
          String[] src_ac = src_TS_v[0].split(" \\{");
          String src_ac_con = src_ac[1].substring(0, src_ac[1].length() - 1);
          String[] src_ac_v = src_ac_con.split("== ");
          int ac_v = Integer.parseInt(src_ac_v[1]);
          String[] src_vc = src_TS_v[1].split(" \\{");
          String src_vc_con = src_vc[1].substring(0, src_vc[1].length() - 1);
          String[] src_vc_v = src_vc_con.split("== ");
          int vc_v = Integer.parseInt(src_vc_v[1]);

          tempBP.addsolutionmember("activecall", ac_v);
          tempBP.addsolutionmember("videoquality", vc_v);

        } else if (tempString.startsWith("  trigger")) {
          System.out.println("===== Read One Behaviour Pair: read the trigger =====");

          String[] trigger = tempString.split(" \\{");
          String triggerName = trigger[1].substring(0, trigger[1].length() - 1);
          System.out.println("----- Trigger Name: " + triggerName + " -----");
          int useroperation_BP;
          if (triggerName.equals("null")) {
            useroperation_BP = 0;
          } else if (triggerName.equals("dial")) {
            useroperation_BP = 1;
          } else if (triggerName.equals("disconnect")) {
            useroperation_BP = 2;
          } else {
            useroperation_BP = -1;
            System.out.println("***** Warning: calculate triggers! *****");
          }
          tempBP.addsolutionmember("UserOperation", useroperation_BP);
        } else if (tempString.startsWith("  PacketLoss")) {

          System.out.println("===== Read One Behaviour Pair: read the network environment =====");

          HashMap<String, ValueSet> guardcondition = new HashMap<String, ValueSet>();

          String tempString_networkenvironment = tempString.substring(2);

          String[] src_networkenvironment = tempString_networkenvironment.split(", ");

          int i_NEvar = 0;

          for (; i_NEvar <= src_networkenvironment.length - 1; ) {

            String[] NEvar_with_constraints = src_networkenvironment[i_NEvar].split(" \\{");

            String NEvar_constraints =
                NEvar_with_constraints[1].substring(0, NEvar_with_constraints[1].length() - 1);

            String[] src_constraintValue = NEvar_constraints.split(" == ");

            tempBP.addsolutionmember(
                src_constraintValue[0], Double.parseDouble(src_constraintValue[1]));

            System.out.println(
                "----- Guard Condition: "
                    + src_constraintValue[0]
                    + ", "
                    + tempBP.getsolution().get(src_constraintValue[0])
                    + " -----");

            i_NEvar = i_NEvar + 1;
          }
          // Add guard condition to transition, then add transition to state machine.

          ExistingBP.add(tempBP);
          Solution.printSolution(tempBP);

          tempBP = null;

        } else {
          System.out.println("***** Error: Line Label Unknown! *****");
        }
      }

      System.out.println("BP List Length: " + ExistingBP.size());

      reader.close();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return ExistingBP;
  }

  // Read State Machine from file
  public static StateMachine ReadStateMachineFromFile(String fileName) {

    File file = new File(fileName);
    BufferedReader reader = null;

    StateMachine S = new StateMachine();
    Transition t = null;
    State sourceState = null;
    State targetState = null;

    try {

      reader = new BufferedReader(new FileReader(file));
      String tempString = null;

      boolean inTransition = false;
      int initial_label = 0; // Used to tag the start state

      while ((tempString = reader.readLine()) != null) {

        initial_label++;

        System.out.println(tempString);

        if (!inTransition) { // Not in One transition.

          String[] srcs = tempString.split(" ");

          if (srcs[0].equals("State")) {
            System.out.println("===== Read One State =====");

            if (!srcs[1].startsWith("{")) {
              System.out.println("***** Error: The format of State Machine is not correct! *****");
              return null;
            }

            String stateName = srcs[1].substring(1, srcs[1].length() - 2);
            HashMap<String, ValueSet> vs = new HashMap<String, ValueSet>();

            srcs = null;
            srcs = tempString.split("; ");
            if (srcs.length != 3) {
              System.out.println(
                  "***** Error: Something wrong with format of State and its variables! *****");
              return null;
            }

            int i_var = 1;

            for (; i_var <= srcs.length - 1; ) {

              String[] var_with_constraints = srcs[i_var].split(" \\{");

              String var_constraints =
                  var_with_constraints[1].substring(0, var_with_constraints[1].length() - 1);
              System.out.println("Constraint:" + var_constraints);
              ValueSet valueset_temp = new ValueSet();
              String[] constraints_of_one_var = var_constraints.split(", ");
              for (int i_constraint = 0;
                  i_constraint < constraints_of_one_var.length;
                  i_constraint++) {
                valueset_temp.AddConstriantsForValueSet(constraints_of_one_var[i_constraint]);
              }

              vs.put(var_with_constraints[0], valueset_temp);

              i_var = i_var + 1;
            }

            State tempState = new State(stateName, vs);

            if (initial_label == 1) {
              System.out.println("----- Add start state. -----");
              S.setstartstate(tempState);
              S.addonestate(tempState);
            } else {
              System.out.println("----- Add one state. -----");
              S.addonestate(tempState);
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

            String[] src_SourceState = tempString.split(" \\{");
            String sourceStateName =
                src_SourceState[1].substring(0, src_SourceState[1].length() - 1);
            System.out.println("----- Source State Name: " + sourceStateName + " -----");
            sourceState = S.getState(sourceStateName);

          } else if (tempString.startsWith("  Target_State")) {
            System.out.println("===== Read One Transition: read the target state =====");

            String[] src_TargetState = tempString.split(" \\{");
            String targetStateName =
                src_TargetState[1].substring(0, src_TargetState[1].length() - 1);
            System.out.println("----- Target State Name: " + targetStateName + " -----");
            targetState = S.getState(targetStateName);

            t = new Transition(sourceState, targetState);
            sourceState.addoutTransition(t);
            targetState.addinTransition(t);
          } else if (tempString.startsWith("  trigger")) {
            System.out.println("===== Read One Transition: read the trigger =====");

            String[] src_Trigger = tempString.split(" \\{");
            String trigger = src_Trigger[1].substring(0, src_Trigger[1].length() - 1);
            System.out.println("----- Trigger Name: " + trigger + " -----");

            t.addTriggers(trigger);
          } else if (tempString.startsWith("  PacketLoss")) {
            System.out.println("===== Read One Transition: read the network environment =====");

            HashMap<String, ValueSet> guardcondition = new HashMap<String, ValueSet>();

            String tempString_networkenvironment = tempString.substring(2);

            String[] src_networkenvironment = tempString_networkenvironment.split(", ");

            int i_NEvar = 0;

            for (; i_NEvar <= src_networkenvironment.length - 1; ) {

              String[] NEvar_with_constraints = src_networkenvironment[i_NEvar].split(" \\{");

              String NEvar_constraints =
                  NEvar_with_constraints[1].substring(0, NEvar_with_constraints[1].length() - 1);

              ValueSet valueset_temp = new ValueSet();
              String[] constraints_of_one_NEvar = NEvar_constraints.split(", ");

              for (int i_constraint = 0;
                  i_constraint < constraints_of_one_NEvar.length;
                  i_constraint++) {
                valueset_temp.AddConstriantsForValueSet(constraints_of_one_NEvar[i_constraint]);
              }

              guardcondition.put(NEvar_with_constraints[0], valueset_temp);
              System.out.println(
                  "----- Guard Condition: "
                      + NEvar_with_constraints[0]
                      + ", "
                      + valueset_temp.getValueSet().get(0)
                      + " -----");

              t.setConditions(guardcondition);

              i_NEvar = i_NEvar + 1;
            }
            // Add guard condition to transition, then add transition to state machine.

            // System.out.println("Corrup: " + NECorrupValue + ", len: " + l_NE_C);
            // t.getNetworkEnvironment().setPacketLoss(Double.parseDouble(NECorrupValue));

            S.addonetransition(t);

            inTransition = false;
            sourceState = null;
            targetState = null;
            t = null;
          } else {
            System.out.println("***** Error: Line Label Unknown! *****");
          }
        }
      }
      reader.close();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }

    return S;
  }
}
