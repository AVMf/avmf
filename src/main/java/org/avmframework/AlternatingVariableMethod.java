package org.avmframework;

import org.avmframework.initialization.Initializer;
import org.avmframework.localsearch.LocalSearch;
import org.avmframework.objective.ObjectiveFunction;
import org.avmframework.objective.ObjectiveValue;
import org.avmframework.variable.AtomicVariable;
import org.avmframework.variable.Variable;
import org.avmframework.variable.VectorVariable;

/**
 * The main class for instantiating and running an AVM search.
 *
 * @author Phil McMinn
 */
public class AlternatingVariableMethod {

  /** The local search to be used by this AVM instance. */
  protected LocalSearch localSearch;

  /** The termination policy to be used by this AVM instance. */
  protected TerminationPolicy tp;

  /** The initializer used to initialize the values of variables at the start of the AVM search. */
  protected Initializer initializer;

  /**
   * The initializer used to initialize the values of variables following the restarting of the
   * search (after it has become trapped in a local optimum.
   */
  protected Initializer restarter;

  /** The monitor instance being used by a search that is currently in progress. */
  protected Monitor monitor;

  /** The objective function being used by a search that is currently in progress. */
  protected ObjectiveFunction objFun;

  /** The vector being optimized by a search that is currently in progress. */
  protected Vector vector;

  /**
   * Constructs an AVM instance.
   *
   * @param localSearch A local search instance.
   * @param tp The termination policy to be used by the search.
   * @param initializer The initializer to be used to initialize variables at the start <i>and</i>
   *     to restart the search.
   */
  public AlternatingVariableMethod(
      LocalSearch localSearch, TerminationPolicy tp, Initializer initializer) {
    this(localSearch, tp, initializer, initializer);
  }

  /**
   * Constructs an AVM instance.
   *
   * @param localSearch A local search instance.
   * @param tp The termination policy to be used by the search.
   * @param initializer The initializer to be used to initialize variables at the start of the
   *     search.
   * @param restarter The initializer to be used to initialize variables when restarting the search.
   */
  public AlternatingVariableMethod(
      LocalSearch localSearch,
      TerminationPolicy tp,
      Initializer initializer,
      Initializer restarter) {
    this.localSearch = localSearch;
    this.tp = tp;
    this.initializer = initializer;
    this.restarter = restarter;
  }

  /**
   * Performs the AVM search.
   *
   * @param vector The vector of variables to be optimized.
   * @param objFun The objective function to optimize the variables against.
   * @return A Monitor instance detailing the progression statistics of the completed search
   *     process.
   */
  public Monitor search(Vector vector, ObjectiveFunction objFun) {
    // set up the monitor
    this.monitor = new Monitor(tp);

    // set up the objective function
    this.objFun = objFun;
    objFun.setMonitor(monitor);

    // initialize the vector
    this.vector = vector;
    initializer.initialize(vector);

    // is there anything to optimize?
    if (vector.size() == 0) {
      throw new EmptyVectorException();
    }

    try {
      // the loop terminates when a TerminationException is thrown
      while (true) {

        // search over the vector's variables
        alternatingVariableSearch(vector);

        // restart the search
        monitor.observeRestart();
        restarter.initialize(vector);
      }

    } catch (TerminationException exception) {
      // the search has ended
      monitor.observeTermination();
    }

    return monitor;
  }

  /**
   * Conducts an AVM search over an arbitrary vector --- either the complete vector being optimized,
   * or a variable that it is itself a vector (e.g., a string).
   *
   * @param abstractVector The vector to be optimized.
   * @throws TerminationException If the search terminates (as dictated by the TerminationPolicy
   *     this instance was constructed with).
   */
  protected void alternatingVariableSearch(AbstractVector abstractVector)
      throws TerminationException {
    ObjectiveValue lastImprovement = objFun.evaluate(vector);
    int nonImprovement = 0;

    while (nonImprovement < abstractVector.size()) {

      // alternate through the variables
      int variableIndex = 0;
      while (variableIndex < abstractVector.size() && nonImprovement < abstractVector.size()) {

        // perform a local search on this variable
        Variable var = abstractVector.getVariable(variableIndex);
        variableSearch(var);

        // check if the current objective value has improved on the last
        ObjectiveValue current = objFun.evaluate(vector);
        if (current.betterThan(lastImprovement)) {
          lastImprovement = current;
          nonImprovement = 0;
        } else {
          nonImprovement++;
        }

        variableIndex++;
      }
    }
  }

  /**
   * Invoke a search over a particular variable.
   *
   * @param var The variable to be optimized.
   * @throws TerminationException If the search terminates (as dictated by the TerminationPolicy
   *     this instance was constructed with).
   */
  protected void variableSearch(Variable var) throws TerminationException {
    if (var instanceof AtomicVariable) {
      atomicVariableSearch((AtomicVariable) var);
    } else if (var instanceof VectorVariable) {
      vectorVariableSearch((VectorVariable) var);
    }
  }

  /**
   * Invoke a search over a variable of type AtomicVariable.
   *
   * @param atomicVar The variable to be optimized.
   * @throws TerminationException If the search terminates (as dictated by the TerminationPolicy
   *     this instance was constructed with).
   */
  protected void atomicVariableSearch(AtomicVariable atomicVar) throws TerminationException {
    localSearch.search(atomicVar, vector, objFun);
  }

  /**
   * Invoke a search over a variable of type VectorVariable.
   *
   * @param vectorVar The variable to be optimized.
   * @throws TerminationException If the search terminates (as dictated by the TerminationPolicy
   *     this instance was constructed with).
   */
  protected void vectorVariableSearch(VectorVariable vectorVar) throws TerminationException {
    ObjectiveValue current = objFun.evaluate(vector);

    // try moves that increase the vector size
    progressivelyChangeVectorVariableSize(vectorVar, current, true);

    // try moves that decrease the vector size
    progressivelyChangeVectorVariableSize(vectorVar, current, false);

    // now for the alternating variable search...
    alternatingVariableSearch(vectorVar);
  }

  /**
   * Attempt moves to increase and decrease the size of a VectorVariable in a series of moves until
   * the objective function value does not improve.
   *
   * @param vectorVar The vector variable.
   * @param current The current objective value of the overall vector before moves are attempted.
   * @param increase Denotes whether to increase (value is true) or decrease (value is false) the
   *     size of vectorVar.
   * @throws TerminationException Terminates the method if the next ObjectiveValue
   *     doesn't exist (null)
   */
  private void progressivelyChangeVectorVariableSize(
      VectorVariable vectorVar, ObjectiveValue current, boolean increase)
      throws TerminationException {
    ObjectiveValue next = null;

    // try moves that increase the vector size
    do {
      if (next != null) {
        current = next;
      }

      changeVectorVariableSize(vectorVar, increase);

      next = objFun.evaluate(vector);
    }
    while (next.betterThan(current));

    // reverse the last move, if there was a change
    changeVectorVariableSize(vectorVar, !increase);
  }

  /**
   * Perform a single move to increase or decrease the size of a VectorVariable.
   *
   * @param vectorVar The vector variable.
   * @param increase Denotes whether to increase (value is true) or decrease (value is false) the
   *     size of vectorVar.
   */
  private void changeVectorVariableSize(VectorVariable vectorVar, boolean increase) {
    if (increase) {
      vectorVar.increaseSize();
    } else {
      vectorVar.decreaseSize();
    }
  }
}
