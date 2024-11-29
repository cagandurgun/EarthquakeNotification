public class Watcher {
    private final int time;
    private final RequestType requestType;
    private Point2D location;
    private String name;

    // add
    public Watcher(int time, RequestType requestType, Point2D location, String name) {
        this.time = time;
        this.requestType = requestType;
        this.location = location;
        this.name = name;
    }

    // delete
    public Watcher(RequestType requestType, int time, String name) {
        this.requestType = requestType;
        this.time = time;
        this.name = name;
    }

    // queryLargest
    public Watcher(int time, RequestType requestType) {
        this.time = time;
        this.requestType = requestType;
    }

    public int getTime() { return time; }

    public RequestType getRequestType() { return requestType; }

    public Point2D getLocation() { return location; }

    public String getName() { return name; }
}
