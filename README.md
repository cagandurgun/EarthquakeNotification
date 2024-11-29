# Author
Çağan Durgun

---

# Overview
This project simulates a notification system for earthquakes, enabling watcher management and notifications based on proximity and earthquake magnitude. It processes watcher and earthquake data from files and handles requests such as adding/removing watchers and querying the largest earthquake.

---

# How to Run
1. Download the source code.
2. Make sure the source code and the data files (`watcherFile` and `earthquakeFile`) are in the same directory.
3. Open a terminal and navigate to the directory containing these files:
    ```bash
    cd /path/to/your/project/directory
    ```
4. Compile the program:
    ```bash
    javac EarthquakeNotification.java
    ```
5. Run the program:
    ```bash
    java EarthquakeNotification [--all] <watcherFile> <earthquakeFile>
    ```
    - `<watcherFile>`: The file containing watcher data.
    - `<earthquakeFile>`: The file containing earthquake data.
    - If you want to enable the `--all` option, use it before the file arguments. This option will print all earthquake insertions into the queue.

---

# Known Bugs and Limitations
There are currently no known bugs or limitations. However, for this project, I created new classes 'KDTree2D' and 'Point2D', which are optimized versions of the ones used in the previous assignment. I removed unnecessary or dead code in these classes to fit the current requirements of the project. Therefore, they may not be compatible with previous Assignment 3 test cases.
