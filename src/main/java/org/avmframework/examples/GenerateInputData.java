package org.avmframework.examples;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.avmframework.AVM;
import org.avmframework.Monitor;
import org.avmframework.TerminationPolicy;
import org.avmframework.Vector;
import org.avmframework.examples.inputdatageneration.Branch;
import org.avmframework.examples.inputdatageneration.TestObject;
import org.avmframework.examples.inputdatageneration.line.LineTestObject;
import org.avmframework.initialization.Initializer;
import org.avmframework.initialization.RandomInitializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;

import java.lang.*;

public class GenerateInputData {

    static final int BRANCH_ARGS_INDEX = 0, SEARCH_NAME_ARGS_INDEX = 1;

    static final int MAX_EVALUATIONS = 100000;

    public static void main(String[] args) {

        TestObject testObject = new LineTestObject();
        Branch target = parseBranchFromArgs(args);

        ObjectiveFunction objFun = testObject.getObjectiveFunction(target);
        Vector vector = testObject.getVector();

        // set up the local search
        // Geometric or lattice search can be used if "GeometricSearch" or "LatticeSearch" are provided as a parameter
        java.lang.String localSearchName = "IteratedPatternSearch";
        if (args.length > 1 && (args[1].equals("GeometricSearch") || args[1].equals("LatticeSearch"))) {
            localSearchName = args[1];
        }
        LocalSearch localSearch = LocalSearch.instantiate(localSearchName);

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

    private static Branch parseBranchFromArgs(String[] args) {
        if (args.length > BRANCH_ARGS_INDEX) {
            String branchStr = args[BRANCH_ARGS_INDEX];
            String errorMessage = "Invalid branch ID – \"" + branchStr +
                    "\". Branch IDs should be of the form (X)(t|f) where X is an integer (e.g., \"1t\")";

            if (branchStr.length() >= 2) {
                String outcomeStr = branchStr.substring(branchStr.length()-1, branchStr.length()).toLowerCase();
                boolean outcome = false;
                if (outcomeStr.equals("t")) {
                    outcome = true;
                } else if (outcomeStr.equals("f")){
                    outcome = false;
                } else {
                    printArgsErrorAndExit(errorMessage);
                }

                String idStr = branchStr.substring(0, branchStr.length() - 1);
                int id = 0;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    printArgsErrorAndExit(errorMessage);
                }

                return new Branch(id, outcome);
            } else {
                printArgsErrorAndExit(errorMessage);
            }
        } else {
            printArgsErrorAndExit("Invalid number of arguments");
        }

        return null;
    }

    private static LocalSearch parseSearchFromArgs(String[] args) {
        // set up the local search, which can be overridden at the command line
        String localSearchName = "IteratedPatternSearch";
        if (args.length > SEARCH_NAME_ARGS_INDEX)  {
            String param = args[SEARCH_NAME_ARGS_INDEX].toLowerCase();

            if (param.equals("iteratedpatternsearch")) {
                localSearchName = "IteratedPatternSearch";
            } else if (param.equals("geometricsearch")) {
                localSearchName = "GeometricSearch";
            } else if (param.equals("latticesearch")) {
                localSearchName = "LatticeSearch";
            }
        }
        return LocalSearch.instantiate(localSearchName);
    }

    private static void printArgsErrorAndExit(String error) {
        System.out.println("ERROR:   " + error);
        System.out.println("USAGE:   java org.avmframework.examples.GenerateInputData branch [search_name]");
        System.out.println("EXAMPLE: java org.avmframework.examples.GenerateInputData 1t LatticeSearch");
        System.exit(1);
    }
}
