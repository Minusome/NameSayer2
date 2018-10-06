package namesayer.util;

import java.util.List;

public class Result {

    private final List<String> foundNames;

    private final Status status;

    public enum Status{ALL_FOUND, NONE_FOUND, PARTIALLY_FOUND}


    public Result(Status status, List<String> foundNames) {
        this.status = status;
        this.foundNames = foundNames;
    }

    public List<String> getFoundNames() {
        return foundNames;
    }

    public Status getStatus() {
        return status;
    }


}
