package org.avmframework.examples.inputdatageneration;

public class Branch {
    protected int id;
    protected boolean outcome;

    public Branch(int id, boolean outcome) {
        this.id = id;
        this.outcome = outcome;
    }

    public int getID() {
        return id;
    }

    public boolean getOutcome() {
        return outcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch)) return false;

        Branch branch = (Branch) o;

        if (id != branch.id) return false;
        return outcome == branch.outcome;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (outcome ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return id + (outcome ? "T" : "F");
    }
}
