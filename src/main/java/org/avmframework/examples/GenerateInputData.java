package org.avmframework.examples;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.TestObject;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;

import java.lang.*;

public class GenerateInputData {

    static final String[] TEST_OBJECT_OPTIONS = {"calendar", "line", "triangle"};
    static final String[] SEARCH_OPTIONS = {"iteratedpatternsearch", "geometric", "lattice"};

    static final int TEST_OBJECT_ARGS_INDEX = 0, BRANCH_ARGS_INDEX = 1, SEARCH_NAME_ARGS_INDEX = 2;

    static final int MAX_EVALUATIONS = 100000;

    public static void main(String[] args) {
        // instantiate the test object using command line parameters
        TestObject testObject = parseTestObjectFromArgs(args);

        // instantiate the branch using command line parameters
        Branch target = parseBranchFromArgs(args, testObject);

        // instantiate the local search using command line parameters
        LocalSearch localSearch = parseSearchFromArgs(args);

        // set up the objective function
        ObjectiveFunction objFun = testObject.getObjectiveFunction(target);

        // set up the vector
        Vector vector = testObject.getVector();

        // set up the termination policy
        TerminationPolicy terminationPolicy = TerminationPolicy.createMaxEvaluationsTerminationPolicy(MAX_EVALUATIONS);

        // set up random initialization of vectors
        RandomGenerator randomGenerator = new MersenneTwister();
        Initializer initializer = new RandomInitializer(randomGenerator);

        // set up the AVM
        AVM avm = new AVM(localSearch, terminationPolicy, initializer);

        // perform the search
        Monitor monitor = avm.search(vector, objFun);

        // output the results
        System.out.println("Best solution: " + monitor.getBestVector());
        System.out.println("Best objective value: " + monitor.getBestObjVal());
        System.out.println(
                "Number of objective function evaluations: " + monitor.getNumEvaluations() +
                        " (unique: " + monitor.getNumUniqueEvaluations() + ")"
        );
        System.out.println("Running time: " + monitor.getRunningTime() + "ms");
    }

    private static TestObject parseTestObjectFromArgs(String[] args) {
        if (args.length > TEST_OBJECT_ARGS_INDEX) {
            String testObjectStr = args[TEST_OBJECT_ARGS_INDEX].toLowerCase();

            String testObjectName = "";
            for (String option: TEST_OBJECT_OPTIONS) {
                if (testObjectStr.equals(option)) {
                    testObjectName = option;
                }
            }

            if (testObjectName.equals("")) {
                System.out.println("ERROR: unrecognized test object \"" + args[TEST_OBJECT_ARGS_INDEX] + "\"");
            } else {
                String testObjectClassName = "org.avmframework.examples.inputdatageneration." +
                        testObjectName + "." + StringUtils.capitalize(testObjectName) + "TestObject";

                try {
                    Class<?> testObjectClass = Class.forName(testObjectClassName);
                    return (TestObject) testObjectClass.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("ERROR: wrong number of arguments");
        }

        printUsage();
        System.exit(1);
        return null;
    }

    private static Branch parseBranchFromArgs(String[] args, TestObject testObject) {
        if (args.length > BRANCH_ARGS_INDEX) {
            String branchStr = args[BRANCH_ARGS_INDEX];

            if (branchStr.length() >= 2) {
                String outcomeStr = branchStr.substring(branchStr.length()-1, branchStr.length()).toLowerCase();

                boolean gotOutcome = false;
                boolean outcome = false;

                if (outcomeStr.equals("t")) {
                    outcome = true;
                    gotOutcome = true;
                } else if (outcomeStr.equals("f")){
                    outcome = false;
                    gotOutcome = true;
                }

                if (gotOutcome) {
                    String idStr = branchStr.substring(0, branchStr.length() - 1);
                    try {
                        int id = Integer.parseInt(idStr);

                        if (id > 0 && id <= testObject.getNumBranchingNodes()) {
                            return new Branch(id, outcome);
                        } else {
                            System.out.println("ERROR: branch number does not exist for the test object specified");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: branch number not recognized");
                    }
                } else {
                    System.out.println("ERROR: unrecognized branch type (must be \"t\" or \"f\"");
                }

            } else {
                System.out.println("ERROR: branch should be over two characters long");
            }
        } else {
            System.out.println("ERROR: wrong number of arguments");
        }

        printUsage();
        System.exit(1);
        return null;
    }

    private static LocalSearch parseSearchFromArgs(String[] args) {
        // set up the local search, which can be overridden at the command line
        String localSearchName = "IteratedPatternSearch";
        if (args.length > SEARCH_NAME_ARGS_INDEX)  {
            String localSearchStr = args[SEARCH_NAME_ARGS_INDEX].toLowerCase();

            if (localSearchStr.equals("iteratedpatternsearch")) {
                localSearchName = "IteratedPatternSearch";
            } else if (localSearchStr.equals("geometricsearch")) {
                localSearchName = "GeometricSearch";
            } else if (localSearchStr.equals("latticesearch")) {
                localSearchName = "LatticeSearch";
            }
        }
        return LocalSearch.instantiate(localSearchName);
    }

    private static void printUsage() {
        String testObjectOtionsString = "\"" + StringUtils.join(TEST_OBJECT_OPTIONS, "\", \"") + "\"";
        //String searchOptionsString = "\"" + StringUtils.join(SEARCH_OPTIONS, "\",\"") + "\"";

        System.out.println("USAGE: java org.avmframework.examples.GenerateInputData testobject branch [search]");
        System.out.println("  where:");
        System.out.println("  -- testobject is one of " + testObjectOtionsString);
        System.out.println("  -- branch is of the form X(t|f) where X is a branching node number (e.g., \"5t\")");
        //System.out.println("-- search (optional) is one of " + searchOptionString);
    }
}
