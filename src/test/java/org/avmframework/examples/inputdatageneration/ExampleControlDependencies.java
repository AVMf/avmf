package org.avmframework.examples.inputdatageneration;

import org.avmframework.examples.inputdatageneration.line.LineBranchTargetObjectiveFunction;

public class ExampleControlDependencies {

    // use the control dependencies in the Line example
    static ControlDependencies DEPS = new LineBranchTargetObjectiveFunction(null){
        public ControlDependencies getControlDependencies() {
            return controlDependencies;
        }
    }.getControlDependencies();
}
