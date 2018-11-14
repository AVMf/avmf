package org.avmframework;

import org.avmframework.variable.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides basic functionality for vectors optimized by the AVM --- both the main vector or
 * variables that have a vector-based representation, such as {@link
 * org.avmframework.variable.StringVariable}.
 *
 * @author Phil McMinn
 */
public abstract class AbstractVector {

  /** The variables in this vector. */
  protected List<Variable> variables = new ArrayList<>();

  /**
   * Returns the variable at the index supplied.
   *
   * @param index The index of the variable.
   * @return The variable at the index.
   */
  public Variable getVariable(int index) {
    return variables.get(index);
  }

  /**
   * Returns the number of variables in the vector.
   *
   * @return The number of variables in the vector.
   */
  public int size() {
    return variables.size();
  }

  /**
   * Produces a copy of the vector where all the variables are deep copied.
   *
   * @param copy A copy of the vector.
   */
  protected void deepCopyVariables(AbstractVector copy) {
    copy.variables.clear();
    for (Variable var : variables) {
      copy.variables.add(var.deepCopy());
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AbstractVector)) {
      return false;
    }

    AbstractVector that = (AbstractVector) obj;

    return variables != null ? variables.equals(that.variables) : that.variables == null;
  }

  @Override
  public int hashCode() {
    return variables != null ? variables.hashCode() : 0;
  }

  @Override
  public String toString() {
    boolean first = true;
    String out = "[";
    for (Variable var : variables) {
      if (first) {
        first = false;
      } else {
        out += ", ";
      }
      out += var;
    }
    out += "]";
    return out;
  }
}
