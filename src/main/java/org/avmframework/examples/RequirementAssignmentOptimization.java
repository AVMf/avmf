package org.avmframework.examples;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AlternatingVariableMethod;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.requirementassignment.Requirement;
import org.avmframework.examples.requirementassignment.RequirementOverview;
import org.avmframework.examples.requirementassignment.TestObject;
import org.avmframework.examples.testoptimization.TestCase;
import org.avmframework.examples.util.ArgsParser;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.variable.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* This example shows requirement assignment optimization problem.
 * The requirements need to be optimally assigned to the different stakeholders.
 * It involves three objectives.
 * The objectives and their weights are defined at the class "RequirementObjectiveFunction"
 * More objectives can be added as in the paper below:
 * https://link.springer.com/article/10.1007/s10664-015-9418-0
 */

public class RequirementAssignmentOptimization {

  // HOW TO RUN:
  //
  // java class org.avmframework.examples.AllZeros [search]
  // where:
  // - [search] is an optional parameter denoting which search to use
  // (e.g., "IteratedPatternSearch", "GeometricSearch" or "LatticeSearch")

  // CHANGE THE FOLLOWING CONSTANTS TO EXPLORE THEIR EFFECT ON THE SEARCH:

  // - search constants
  static final String SEARCH_NAME = "IteratedPatternSearch"; // can also be
  // set at the
  // command line
  static final int MAX_EVALUATIONS = 200000;
  static final int numberOfStakeholders = 10; // number of stakeholders to
  // distribute requirements

  public static void main(String[] args) {
    List<Requirement> reqList = new ArrayList<Requirement>();
    RequirementOverview reqOverview = new RequirementOverview();
    List<TestCase> selectedTestSuite = new ArrayList<TestCase>();

    // list of 287 requirements in simplified text file with key attributes,
    // such as id, identifier, description
    String file =
        "src/main/java/org/avmframework/examples/requirementassignment/requirementList.txt";
    // list of requirements
    reqList = readReqList(file, reqList, reqOverview);

    // instantiate the test object using command line parameters
    TestObject testObject = new TestObject();

    // instantiate local search from command line, using default (set by the
    // constant SEARCH_NAME) if none supplied
    LocalSearch localSearch =
        new ArgsParser(RequirementAssignmentOptimization.class, args).parseSearchParam(SEARCH_NAME);

    // set up the objective function
    ObjectiveFunction objFun = testObject.getObjectiveFunction(reqList, reqOverview);

    // set up the vector
    Vector vector = testObject.setUpVector(reqList.size(), numberOfStakeholders);

    // set up the termination policy
    TerminationPolicy terminationPolicy =
        TerminationPolicy.createMaxEvaluationsTerminationPolicy(MAX_EVALUATIONS);

    // set up random initialization of vectors
    RandomGenerator randomGenerator = new MersenneTwister();
    Initializer initializer = new RandomInitializer(randomGenerator);

    // set up the AlternatingVariableMethod
    AlternatingVariableMethod avm = new AlternatingVariableMethod(
        localSearch, terminationPolicy, initializer);

    // perform the search
    Monitor monitor = avm.search(vector, objFun);

    // output the results
    Map<String, List<Requirement>> reqMap = optimizedReq(monitor, reqList);
    System.out.println(
        "The 287 requirements (represented by ids) are added to the"
            + numberOfStakeholders
            + " stakeholders as follow:");
    for (Map.Entry<String, List<Requirement>> entry : reqMap.entrySet()) {
      System.out.print("Stakeholder  " + entry.getKey() + ":   ");
      for (int i = 0; i < entry.getValue().size(); i++) {
        System.out.print(entry.getValue().get(i).getId());
        if (i + 1 < entry.getValue().size()) {
          System.out.print(", ");
        }
      }
      System.out.print("\n");
    }
    System.out.print("\n");
    System.out.println("Best solution: " + monitor.getBestVector());
    System.out.println("Best objective value: " + monitor.getBestObjVal());
    System.out.println(
        "Number of objective function evaluations: "
            + monitor.getNumEvaluations()
            + " (unique: "
            + monitor.getNumUniqueEvaluations()
            + ")");
    System.out.println("Running time: " + monitor.getRunningTime() + "ms");
  }

  // read the requirement file and populate the requirement list
  private static List<Requirement> readReqList(
      String file, List<Requirement> reqList, RequirementOverview reqOverview) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(file));
      String str;
      int counter = 0;
      while ((str = in.readLine()) != null) {
        if (counter == 0) { // the first row contains the column names
          counter++;
          continue;
        } else {
          String[] details = str.split("\t");

          Requirement req = new Requirement();

          req.setId(details[0]);
          req.setIdentifier(details[1]);
          req.setDescription(details[2]);
          req.setComplexity(computeComplexity(details[2]));
          req.setDependency(Double.parseDouble(details[3]));
          req.setImportance(Double.parseDouble(details[4]));

          reqList.add(req);
          reqOverview =
              computeMinMax(
                  reqOverview, req.getComplexity(), req.getDependency(), req.getImportance());
        }
      }
      in.close();
    } catch (IOException exception) {
      // nothing to catch
    }
    return reqList;
  }

  // calculate the complexity of requirement based on their description
  public static double computeComplexity(String description) {
    int sentences = description.split(".").length + 1;
    int words = description.split(" ").length;
    double complexity = words / sentences;
    return complexity;
  }

  public static RequirementOverview computeMinMax(
      RequirementOverview reqOverview, double comp, double dep, double imp) {
    if (reqOverview.getMinComp() > comp) {
      reqOverview.setMinComp(comp);
    }
    if (reqOverview.getMaxComp() < comp) {
      reqOverview.setMaxComp(comp);
    }

    if (reqOverview.getMinDep() > dep) {
      reqOverview.setMinDep(dep);
    }
    if (reqOverview.getMaxDep() < dep) {
      reqOverview.setMaxDep(dep);
    }

    if (reqOverview.getMinImp() > imp) {
      reqOverview.setMinImp(imp);
    }
    if (reqOverview.getMaxImp() < imp) {
      reqOverview.setMaxImp(imp);
    }

    return reqOverview;
  }

  // final solution
  protected static Map<String, List<Requirement>> optimizedReq(
      Monitor monitor, List<Requirement> req) {
    Map<String, List<Requirement>> reqMap = new HashMap<String, List<Requirement>>();

    for (int i = 0; i < monitor.getBestVector().size(); i++) {
      Variable var = monitor.getBestVector().getVariable(i);
      List<Requirement> tempReq = new ArrayList<Requirement>();
      // value less than 0 means the requirement is not assigned to any
      // stakeholder
      if (Integer.parseInt(var.toString()) >= 0) {

        if (reqMap.containsKey(var.toString())) {
          tempReq = reqMap.get(var.toString());
        }
        tempReq.add(req.get(i));
      }
      reqMap.put(var.toString(), tempReq);
    }
    return reqMap;
  }
}
