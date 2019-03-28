package org.avmframework;

import org.avmframework.initialization.DefaultInitializer;
import org.avmframework.localsearch.IteratedPatternSearch;

public class AlternatingVariableMethods {

  public static AlternatingVariableMethod anyAlternatingVariableMethod() {
    return new AlternatingVariableMethod(
        new IteratedPatternSearch(),
        TerminationPolicy.createMaxEvaluationsTerminationPolicy(100),
        new DefaultInitializer());
  }

  public static AlternatingVariableMethod anyAlternatingVariableMethodWithTerminationPolicy(
      TerminationPolicy tp) {
    return new AlternatingVariableMethod(new IteratedPatternSearch(), tp, new DefaultInitializer());
  }
}
