package namesayer.model;

import java.nio.file.Path;

public abstract class Name implements Comparable<Name> {

    protected String name;

    static Path directory;

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void setDirectory(Path directory) {
        Name.directory = directory;
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
        return name.hashCode();
    }

    @Override
    public int compareTo(Name o) {
        return this.getName().compareTo(o.getName());
    }
}
