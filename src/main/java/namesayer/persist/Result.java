package namesayer.persist;

/**
 * Contains an enum to describe whether a user requested name is found
 */

public class Result {

    //The canonical string for the discovered name
    private String discoveredName;

    private final Status status;

    public enum Status{ALL_FOUND, NONE_FOUND, PARTIALLY_FOUND}


    public Result(Status status, String foundNames) {
        this.status = status;
        this.discoveredName = foundNames;
    }

    public String getDiscoveredName() {
        return discoveredName;
    }

    public Status getStatus() {
        return status;
    }


}
