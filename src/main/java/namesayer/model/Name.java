package namesayer.model;

import java.io.Serializable;

/**
 * Abstract base class for any type of Name
 */
public abstract class Name implements Comparable<Name>, Serializable {

    protected String name;

    public Name(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Name name1 = (Name) o;

        return name.equals(name1.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(Name o) {
        return this.toString().toLowerCase().compareTo(o.toString().toLowerCase());
    }
}
