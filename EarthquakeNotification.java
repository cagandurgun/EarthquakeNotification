import java.io.File;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.LinkedList;

public class EarthquakeNotification {

    public static void main(String[] args) {
        // Check if the required number of arguments is provided
        if (args.length < 2 || args.length > 3) {
            System.err.println("Usage java EarthquakeNotification [--all] <watcherFile> <earthquakeFile>");
            System.exit(1);
        }

        // Flag to track if "--all" option is provided
        boolean isAllEnabled = false;

        // Variables to store file paths
        String watcherFilePath = null;
        String earthquakeFilePath = null;

        // Parse arguments
        if (args.length == 3) {
            // Check if the first argument is "--all"
            if ("--all".equals(args[0])) {
                isAllEnabled = true;
                watcherFilePath = args[1];
                earthquakeFilePath = args[2];
            } else {
                System.err.println("Usage java EarthquakeNotification [--all] <watcherFile> <earthquakeFile>");
                System.exit(1);
            }
        } else {
            // If only 2 arguments are provided, assign them as file paths
            watcherFilePath = args[0];
            earthquakeFilePath = args[1];
        }

        // Validate files
        File watcherFile = new File(watcherFilePath);
        File earthquakeFile = new File(earthquakeFilePath);

        if (!watcherFile.exists() || !watcherFile.isFile()) {
            System.err.println("Watcher file " + watcherFilePath + " does not exist");
            System.exit(1);
        }

        if (!earthquakeFile.exists() || !earthquakeFile.isFile()) {
            System.err.println("Earthquake file " + earthquakeFilePath + " does not exist");
            System.exit(1);
        }

        // Initialize and run the simulation
        try {
            processEvents(isAllEnabled, EarthquakeNotificationInitializer.loadWatchers(watcherFile), EarthquakeNotificationInitializer.loadEarthquakes(earthquakeFile));
        } catch (Exception e) {
            System.exit(1);
        }
    }

    /**
     * Processes events using the provided arrays.
     *
     * @param isAllEnabled            Flag indicating whether to enable --all mode
     * @param watchers                Array containing watcher events
     * @param earthquakes             Array containing earthquake events
     */
    private static void processEvents(boolean isAllEnabled, Watcher[] watchers, Earthquake[] earthquakes) {

        KDTree2D watcherTree = new KDTree2D();
        Queue<Earthquake> earthquakeQueue = new LinkedList<>();
        PriorityQueue<Earthquake> magnitudeOrderedPq = new PriorityQueue<>((a, b) -> Double.compare(b.magnitude(), a.magnitude()));

        // Start the simulation with time = 0 (or any other starting time you need)
        int currentTime;

        // Placeholder for processing logic
        int watcherIndex = 0;
        int earthquakeIndex = 0;

        // Process all events until all watchers and earthquakes are processed
        while (watcherIndex < watchers.length || earthquakeIndex < earthquakes.length) {
            // Check the minimum time from both arrays to synchronize
            int nextWatcherTime = watcherIndex >= watchers.length ? Integer.MAX_VALUE : watchers[watcherIndex].getTime();
            int nextEarthquakeTime = earthquakeIndex >= earthquakes.length ? Integer.MAX_VALUE : earthquakes[earthquakeIndex].time();

            // Set the current time to the minimum of both times
            currentTime = Math.min(nextWatcherTime, nextEarthquakeTime);

            while (!earthquakeQueue.isEmpty() && currentTime - earthquakeQueue.peek().time() > 6) {
                Earthquake outdatedEarthquake = earthquakeQueue.poll();
                magnitudeOrderedPq.remove(outdatedEarthquake);
            }

            // Process all watchers with the current time
            while (watcherIndex < watchers.length && watchers[watcherIndex].getTime() == currentTime) {
                Watcher watcher = watchers[watcherIndex];

                switch (watcher.getRequestType()) {
                    case add:
                        watcherTree.insert(watcher.getLocation());
                        System.out.println(watcher.getName() + " is added to the watcher-tree");
                        break;
                    case delete:

                        Point2D delete = null;

                        for(Watcher w : watchers) {
                            if (w.getRequestType().equals(RequestType.add)) {
                                if (w.getName().equals(watcher.getName())) {
                                    delete = w.getLocation();
                                }
                            }
                        }

                        watcherTree.remove(delete);

                        System.out.println(watcher.getName() + " is removed from the watcher-tree");
                        break;
                    case queryLargest:
                        Earthquake largestEarthquake = magnitudeOrderedPq.peek();
                        System.out.println(largestEarthquake != null ? "Largest earthquake in the past 6 hours:\nMagnitude " + largestEarthquake.magnitude() + " at " + largestEarthquake.place() : "No records");
                        break;
                    default:
                        break;
                }

                watcherIndex++;
                System.out.println();
            }

            while (earthquakeIndex < earthquakes.length && earthquakes[earthquakeIndex].time() == currentTime) {
                Earthquake earthquake = earthquakes[earthquakeIndex];

                earthquakeQueue.add(earthquake);
                magnitudeOrderedPq.add(earthquake);

                if (isAllEnabled) { System.out.println("Earthquake " + earthquake.place() + " is inserted into the earthquake-queue"); }

                Iterable<Point2D> nearbyPoints = watcherTree.printCircularRange(earthquake.coordinates().location(), 2 * Math.pow(earthquake.magnitude(), 3));

                for (Watcher w : watchers) {
                    for (Point2D point : nearbyPoints) {
                        if (w.getRequestType().equals(RequestType.add)) {
                            if (w.getLocation().equals(point)) {
                                System.out.println("Earthquake " + earthquake.place() + " is close to " + w.getName());
                            }
                        }
                    }
                }

                earthquakeIndex = earthquakeIndex + 2;
                watcherIndex++;
                if (watcherIndex < watchers.length || earthquakeIndex < earthquakes.length) {
                    System.out.println();
                    watcherIndex--;
                    earthquakeIndex--;
                }
            }
        }
    }
}
