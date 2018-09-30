package namesayer.model;

import java.nio.file.Path;

public class Name implements Comparable<Name> {

    protected String name;
    protected Path directory;

    public Name(String name, Path directory) {
        this.name = name;
        this.directory = directory;
    }


    public String getName() {
        return name;
    }

    public Path getDirectory() {
        return directory;
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
