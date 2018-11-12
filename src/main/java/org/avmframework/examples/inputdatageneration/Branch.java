package org.avmframework.examples.inputdatageneration;

public class Branch {
  public static String TRUE = "T";
  public static String FALSE = "F";

  protected int node;
  protected boolean edge;

  public Branch(int id, boolean edge) {
    this.node = id;
    this.edge = edge;
  }

  public int getId() {
    return node;
  }

  public boolean getEdge() {
    return edge;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Branch)) {
      return false;
    }

    Branch branch = (Branch) obj;

    if (node != branch.node) {
      return false;
    }
    return edge == branch.edge;
  }

  @Override
  public int hashCode() {
    int result = node;
    result = 31 * result + (edge ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return node + (edge ? TRUE : FALSE);
  }

  public static Branch instantiate(String id) {
    return instantiate(id, null);
  }

  public static Branch instantiate(String id, TestObject testObject) {
    if (id.length() >= 2) {
      String edgeCharacter = id.substring(id.length() - 1, id.length());

      boolean gotEdge = false;
      boolean edge = false;

      if (edgeCharacter.equals(TRUE)) {
        edge = true;
        gotEdge = true;
      } else if (edgeCharacter.equals(FALSE)) {
        edge = false;
        gotEdge = true;
      }

      if (gotEdge) {
        String nodeString = id.substring(0, id.length() - 1);
        try {
          int node = Integer.parseInt(nodeString);

          boolean validNode = node >= 1;
          System.out.println(testObject.getNumBranchingNodes());
          if (testObject != null && node > testObject.getNumBranchingNodes()) {
            validNode = false;
          }
          if (!validNode) {
            throw new RuntimeException(
                "Branch node \"" + node + "\" does not exist for the test object specified");
          }

          return new Branch(node, edge);

        } catch (NumberFormatException exception) {
          throw new RuntimeException("Branch node \"" + nodeString + "\" not recognized");
        }
      } else {
        throw new RuntimeException("Unrecognized branch edge (must be \"T\" or \"F\")");
      }
    } else {
      throw new RuntimeException("Branch ID should be over two characters long");
    }
  }
}
