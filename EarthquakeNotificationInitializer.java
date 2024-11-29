import java.io.*;
import java.util.*;

public class EarthquakeNotificationInitializer {

    // Loads watcher data into the watcher array
    public static Watcher[] loadWatchers(File watcherFile) {
        List<Watcher> watcherList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(watcherFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                int time = Integer.parseInt(parts[0]);
                String requestTypeStr = parts[1];
                RequestType requestType;

                // Check if the requestType is 'query-largest' and convert it to RequestType.queryLargest
                if ("query-largest".equals(requestTypeStr)) {
                    requestType = RequestType.queryLargest;
                } else {
                    requestType = RequestType.valueOf(requestTypeStr);
                }

                switch (requestType) {
                    case add:
                        double latitude = Double.parseDouble(parts[2]);
                        double longitude = Double.parseDouble(parts[3]);
                        String name = parts[4];
                        watcherList.add(new Watcher(time, requestType, new Point2D(latitude, longitude), name));
                        break;

                    case delete:
                        watcherList.add(new Watcher(requestType, time, parts[2]));
                        break;

                    case queryLargest:
                        watcherList.add(new Watcher(time, requestType));
                        break;

                    default:
                        System.err.println("Unknown request type: " + requestType);
                }
            }
        } catch (IOException | NumberFormatException _) {

        }
        // Convert List to array and return
        return watcherList.toArray(new Watcher[0]);
    }

    // Loads earthquake data into the earthquake array
    public static Earthquake[] loadEarthquakes(File earthquakeFile) {
        List<Earthquake> earthquakeList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(earthquakeFile))) {
            String line;
            Earthquake earthquake;
            int time = 0;
            String place = "";
            double magnitude = 0.0;
            Point2D location = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals("<earthquake>")) {
                    time = 0;
                    place = "";
                    magnitude = 0.0;
                    location = null;
                } else {
                    if (!line.startsWith("<id>") || !line.endsWith("</id>")) {
                        if (line.startsWith("<time>") && line.endsWith("</time>")) {
                            time = Integer.parseInt(line.replace("<time>", "").replace("</time>", "").trim());
                        } else if (line.startsWith("<place>") && line.endsWith("</place>")) {
                            place = line.replace("<place>", "").replace("</place>", "").trim();
                        } else if (line.startsWith("<coordinates>") && line.endsWith("</coordinates>")) {
                            String[] coords = line.replace("<coordinates>", "").replace("</coordinates>", "").trim().split(",");
                            double latitude = Double.parseDouble(coords[0]);
                            double longitude = Double.parseDouble(coords[1]);
                            location = new Point2D(latitude, longitude);
                        } else if (line.startsWith("<magnitude>") && line.endsWith("</magnitude>")) {
                            magnitude = Double.parseDouble(line.replace("<magnitude>", "").replace("</magnitude>", "").trim());
                        } else if (line.equals("</earthquake>")) {
                            earthquake = new Earthquake(time, place, new Coordinate(location), magnitude);
                            earthquakeList.add(earthquake);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException _) {}
        // Convert List to array and return
        return earthquakeList.toArray(new Earthquake[0]);
    }
}
