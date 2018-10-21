package org.avmframework;

import org.avmframework.initialization.DefaultInitializer;
import org.avmframework.localsearch.IteratedPatternSearch;

public class AVMs {

  public static AVM anyAVM() {
    return new AVM(
        new IteratedPatternSearch(),
        TerminationPolicy.createMaxEvaluationsTerminationPolicy(100),
        new DefaultInitializer());
  }

  public static AVM anyAVMWithTerminationPolicy(TerminationPolicy tp) {
    return new AVM(new IteratedPatternSearch(), tp, new DefaultInitializer());
  }
}
